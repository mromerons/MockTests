import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.json.JSONObject.NULL;

/**
 * Created by mromero on 7/25/17.
 */
public class RequestFactory{

    private static boolean initialRequest = true;
    private static boolean subscriptionsCreated = false;

    private static final String integrationHost = "http://localhost:8080";

    public static void start(ScheduledExecutorService scheduler, Long lifetime){
        final Runnable beeper = new Runnable() {
            public void run() {
                System.out.println("["+LocalDateTime.now()+"] Beep...");

                //Initial Request
                if (initialRequest==true){
                    try {
                        System.out.println("["+LocalDateTime.now()+"] ".concat("Sending Initial Request..."));
                        sendInitialIntegrationRequest();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    initialRequest=false;
                }

                //When subscription is already created and ready to be activated
                if (subscriptionsCreated==false && initialRequest==false && IntegrationTest.IntegrationId != 0L){
                    try {
                        System.out.println("["+LocalDateTime.now()+"] ".concat("Sending Activate Subscription Request for Subscription ID: CREATE10X1"));
                        sendActivateSubscriptionRequest("CREATE10X1");
                        System.out.println("["+LocalDateTime.now()+"] ".concat("Sending Activate Subscription Request for Subscription ID: UPDATE20Z2"));
                        sendActivateSubscriptionRequest("UPDATE20Z2");
                        subscriptionsCreated = true;
                    } catch (IOException e) {
                        IntegrationTest.IntegrationId=0L;
                        e.printStackTrace();
                    }
                    //IntegrationTest.IntegrationId=0L;
                }

                //When venzee is ready to send batch items
                if (IntegrationTest.readyToSendBatches==true && subscriptionsCreated == true){
                    try {
                        System.out.println("["+LocalDateTime.now()+"] ".concat("Ready to send batch items, sleeping system for 10 seconds..."));
                        Thread.sleep(10000);
                        System.out.println("["+LocalDateTime.now()+"] ".concat("Sending Batch: CREATE10X1"));
                        sendBatch();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        IntegrationTest.readyToSendBatches=false;
                        e.printStackTrace();
                    }
                    IntegrationTest.readyToSendBatches=false;
                    subscriptionsCreated=false;

                    //Finished Integration
                    IntegrationTest.IntegrationId=0L;
                }
            }
        };

        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 5, 10, SECONDS);

        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                beeperHandle.cancel(true);
            }
        }, lifetime, HOURS);
    }

    private static void sendInitialIntegrationRequest() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        GenericUrl url = new GenericUrl(integrationHost.concat("/integrations/shopify"));
        HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
        String requestBody = Utils.readResponseFile("shopify_initial_request.json");

        HttpRequest request = requestFactory.buildPostRequest(url, ByteArrayContent.fromString("application/json", requestBody));
        request.setRequestMethod("POST");
        request.execute();
    }

    private static void sendActivateSubscriptionRequest(String subscriptionId) throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        GenericUrl url = new GenericUrl(integrationHost.concat("/integrations/".concat(IntegrationTest.IntegrationId.toString().concat("/items"))));
        HttpRequestFactory requestFactory = httpTransport.createRequestFactory();

        HttpRequest request = requestFactory.buildPostRequest(url, ByteArrayContent.fromString("application/json", ""));
        request.setRequestMethod("POST");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Hook-Setup-Call", true);
        headers.set("X-Hook-Secret", "emmvalue");
        headers.set("subscriptionId", subscriptionId);
        headers.set("Connection", "Close");

        request.setHeaders(headers);
        request.execute();
    }

    private static void sendBatch() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        GenericUrl url = new GenericUrl(integrationHost.concat("/integrations/".concat(IntegrationTest.IntegrationId.toString().concat("/items"))));
        HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
        BatchFactory.generateBatch();
        String requestBody = Utils.readResponseFile("test_batch_1prod.json");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Hook-Setup-Call", false);
        headers.set("X-Hook-Secret", "emmvalue");
        //headers.set("subscriptionId", subscriptionId);
        headers.set("Connection", "Close");

        HttpRequest request = requestFactory.buildPostRequest(url, ByteArrayContent.fromString("application/json", requestBody));
        request.setRequestMethod("POST");
        request.setHeaders(headers);
        request.execute();
    }
}
