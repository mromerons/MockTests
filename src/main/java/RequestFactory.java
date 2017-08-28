import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mockserver.client.netty.NettyHttpClient;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by mromero on 7/25/17.
 */
public class RequestFactory{

    private static boolean initialRequest = true;

    public static String subsIdCreate = "";
    public static String subsIdUpdate = "";

    public static boolean subscriptionsCreated = false;
    public static String collectionId = "";

    public static final String integrationHost = "localhost";
    public static final int integrationPort = 8080;

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
                        //Send first the update before the create because Mock-Server callback is blocking Nearsoft Code
                        subsIdUpdate = CreateSubscriptionCallback.updateId;
                        System.out.println("["+LocalDateTime.now()+"] ".concat("Sending Activate Subscription Request for Subscription ID: ").concat(subsIdUpdate));
                        sendActivateSubscriptionRequest(subsIdUpdate);

                        subsIdCreate = CreateSubscriptionCallback.createId;
                        System.out.println("["+LocalDateTime.now()+"] ".concat("Sending Activate Subscription Request for Subscription ID: ").concat(subsIdCreate));
                        sendActivateSubscriptionRequest(subsIdCreate);

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

                        //Set the number of Products to send and how many Variants will have each Product
                        sendBatch(10, 2);
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

    public static CompletableFuture<Void> sendInitialIntegrationRequest() throws IOException {
        String requestBody = Utils.readResponseFile("shopify_initial_request.json");
        JSONObject initialBatch = null;
        JSONParser parser = new JSONParser();
        try {
            initialBatch = (JSONObject) parser.parse(requestBody);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        collectionId = createID();
        initialBatch.put("collectionId", collectionId);

        NettyHttpClient client = new NettyHttpClient();
        HttpRequest request = new HttpRequest();
        request
                .withMethod("POST")
                .withHeader(new Header("Host", integrationHost))
                .withHeader(new Header("Content-Type","application/json"))
                .withPath("/integrations/shopify")
                .withBody(initialBatch.toJSONString());
        return CompletableFuture.runAsync(() ->
            client.sendRequest(request, InetSocketAddress.createUnresolved(integrationHost, integrationPort)));
    }

    private static CompletableFuture<Void> sendActivateSubscriptionRequest(String subscriptionId) throws IOException {
        NettyHttpClient client = new NettyHttpClient();
        HttpRequest request = new HttpRequest();
        request
                .withMethod("POST")
                .withHeader(new Header("Host", integrationHost))
                .withHeader(new Header("Content-Type","application/json"))
                .withHeader(new Header("X-Hook-Setup-Call", "true"))
                .withHeader(new Header("X-Hook-Secret", "emmvalue"))
                .withHeader(new Header("subscriptionId", subscriptionId))
                .withHeader(new Header("Connection", "Close"))
                .withPath("/integrations/".concat(IntegrationTest.IntegrationId.toString().concat("/items")))
                .withBody("{ }");
        return CompletableFuture.runAsync(() ->
                client.sendRequest(request, InetSocketAddress.createUnresolved(integrationHost, integrationPort)));
    }

    private static CompletableFuture<Void> sendBatch(int products, int variants) throws IOException {
        String requestBody = BatchFactory.generateBatchPayload(products, variants);
        NettyHttpClient client = new NettyHttpClient();
        HttpRequest request = new HttpRequest();
        request
                .withMethod("POST")
                .withHeader(new Header("Host", integrationHost))
                .withHeader(new Header("Content-Type","application/json"))
                .withHeader(new Header("X-Hook-Setup-Call", "false"))
                .withHeader(new Header("X-Hook-Secret", "emmvalue"))
                .withHeader(new Header("Connection", "Close"))
                .withPath("/integrations/".concat(IntegrationTest.IntegrationId.toString().concat("/items")))
                .withBody(requestBody);
        return CompletableFuture.runAsync(() ->
            client.sendRequest(request, InetSocketAddress.createUnresolved(integrationHost, integrationPort)));
    }

    private static String createID(){
        return RandomStringUtils.randomAlphanumeric(10).toUpperCase();
    }
}
