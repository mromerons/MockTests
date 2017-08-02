import org.mockserver.client.netty.NettyHttpClient;

import java.io.IOException;
import java.net.InetSocketAddress;
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
    public static boolean subscriptionsCreated = false;

    private static final String integrationHost = "localhost";
    private static final int integrationPort = 8080;

    public static void start(ScheduledExecutorService scheduler, Long lifetime){
        final Runnable beeper = new Runnable() {
            public void run() {
                //Message to check if this is alive, just for testing purposes
                //System.out.println("["+LocalDateTime.now()+"] Beep...");

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
                }

                //When venzee is ready to send batch items
                if (IntegrationTest.readyToSendBatches==true && subscriptionsCreated == true){
                    try {
                        System.out.println("["+LocalDateTime.now()+"] ".concat("Ready to send batch items, sleeping system for 10 seconds..."));
                        Thread.sleep(10000);
                        System.out.println("["+LocalDateTime.now()+"] ".concat("Sending Batch for Integration ID: ").concat(IntegrationTest.IntegrationId.toString()));
                        sendBatch();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        IntegrationTest.readyToSendBatches=false;
                        e.printStackTrace();
                    }

                    //Integration was processed
                    IntegrationTest.readyToSendBatches=false;
                    subscriptionsCreated=false;
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
        String requestBody = Utils.readResponseFile("shopify_initial_request.json");
        NettyHttpClient client = new NettyHttpClient();
        org.mockserver.model.HttpRequest request = new org.mockserver.model.HttpRequest();
        request
                .withMethod("POST")
                .withHeader("Host", integrationHost)
                .withHeader("Content-Type","application/json")
                .withPath("/integrations/shopify")
                .withBody(requestBody);
        client.sendRequest(request, InetSocketAddress.createUnresolved(integrationHost, integrationPort));
    }

    private static void sendActivateSubscriptionRequest(String subscriptionId) throws IOException {
        NettyHttpClient client = new NettyHttpClient();
        org.mockserver.model.HttpRequest request = new org.mockserver.model.HttpRequest();
        request
                .withMethod("POST")
                .withHeader("Host", integrationHost)
                .withHeader("Content-Type","application/json")
                .withHeader("X-Hook-Setup-Call", "true")
                .withHeader("X-Hook-Secret", "emmvalue")
                .withHeader("subscriptionId", subscriptionId)
                .withHeader("Connection", "Close")
                .withPath("/integrations/".concat(IntegrationTest.IntegrationId.toString().concat("/items")))
                .withBody("{ }");
        client.sendRequest(request, InetSocketAddress.createUnresolved(integrationHost, integrationPort));
    }

    private static void sendBatch() throws IOException {
        String requestBody = Utils.readResponseFile("test_batch_1prod.json");
        NettyHttpClient client = new NettyHttpClient();
        org.mockserver.model.HttpRequest request = new org.mockserver.model.HttpRequest();
        request
                .withMethod("POST")
                .withHeader("Host", integrationHost)
                .withHeader("Content-Type","application/json")
                .withHeader("X-Hook-Setup-Call", "false")
                .withHeader("X-Hook-Secret", "emmvalue")
                .withHeader("Connection", "Close")
                .withPath("/integrations/".concat(IntegrationTest.IntegrationId.toString().concat("/items")))
                .withBody(requestBody);
        client.sendRequest(request, InetSocketAddress.createUnresolved(integrationHost, integrationPort));
    }
}
