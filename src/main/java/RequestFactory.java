import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by mromero on 7/25/17.
 */
public class RequestFactory{

    private static boolean initialRequest = true;

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
                if (IntegrationTest.IntegrationId != 0L && initialRequest==false){
                    try {
                        System.out.println("["+LocalDateTime.now()+"] ".concat("Sending Activate Subscription Request for Subscription ID: CREATE10X1"));
                        sendActivateSubscriptionRequest("CREATE10X1");
                        System.out.println("["+LocalDateTime.now()+"] ".concat("Sending Activate Subscription Request for Subscription ID: UPDATE20Z2"));
                        sendActivateSubscriptionRequest("UPDATE20Z2");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

}
