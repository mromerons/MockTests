import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by mromero on 7/25/17.
 */
public class RequestFactory{

    private static boolean initialRequest = true;
    private static boolean activeSubscription = false;

    private static final String integrationHost = "http://localhost:8080";

    public static void start(ScheduledExecutorService scheduler, Long lifetime){
        //scheduler = Executors.newScheduledThreadPool(1);
        final Runnable beeper = new Runnable() {
            public void run() {
                System.out.println("beep");
                if (initialRequest==true){
                    System.out.println("Something!");
                    try {
                        System.out.println("Sending Initial Request...");
                        sendInitialIntegrationRequest();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    initialRequest=false;
                }
                //TODO: SUBSCRIPTION READY TO ACTIVATE
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

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Hook-Setup-Call", true);
        headers.set("subscriptionId","TestHeader");

        request.setHeaders(headers);
        request.execute();
    }

}
