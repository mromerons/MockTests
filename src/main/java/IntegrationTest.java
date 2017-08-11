import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpStatusCode;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.unlimited;
import static org.mockserver.model.HttpClassCallback.callback;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class IntegrationTest {
    private ClientAndServer mockServer;
    public static final String venzeeStoreName = "nearsoft5cgw";
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static Long IntegrationId = 0L;
    public static boolean readyToSendBatches = false;

    @Before
    public void startServer(){
        org.mockserver.configuration.ConfigurationProperties.overrideLogLevel("OFF");
        mockServer = startClientAndServer(1080);
        System.out.println("Mock Server started");
    }

    @After
    public void stopServer(){
        mockServer.stop();
        System.out.println("Mock Server stopped");
    }

    @Test
    public void integrationTests() throws IOException, InterruptedException {
        RequestFactory.start(scheduler, 1L);

        mapGenerateToken("GENERATE_TOKEN", mockServer);     //GENERATE TOKEN
        mapCreateSubscription("CREATE_SUBSCRIPTION", mockServer);     //CREATE SUBSCRIPTION
        mapActivateSubscription("ACTIVATE_SUBSCRIPTION", mockServer);     //ACTIVATE SUBSCRIPTION
        mapFetchFirstBatch("FETCH_FIRST_BATCH", mockServer);     //FETCH FIRST BATCH
        mapNotification("NOTIFICATION", mockServer);     //NOTIFICATION

        //JUST FOR TEST PURPOSES
        while (true) {
            Thread.sleep(1000L);
        }
    }

    private void mapGenerateToken(final String operation, MockServerClient client) throws IOException {
        client
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/api/app/token")
                        ,
                        unlimited()
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody(Utils.readResponseFile("token_response.json"))
                );
    }

    private void mapCreateSubscription(final String operation, MockServerClient client) throws IOException {
        client
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/api/orgs/".concat(venzeeStoreName).concat("/subscriptions"))
                                //.withBody(new JsonBody("{eventName: 'product.created'}", MatchType.ONLY_MATCHING_FIELDS))
                        ,
                        unlimited()
                )
                .callback(
                        callback()
                                .withCallbackClass(CreateSubscriptionCallback.class.getName())
                );
    }

    private void mapActivateSubscription(final String operation, MockServerClient client) throws IOException {
        client
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/api/orgs/subscriptions/[a-zA-Z0-9-_]+/activate")
                        ,
                        unlimited()
                )
                .callback(
                        callback()
                                .withCallbackClass(ActivateSubscriptionCallback.class.getName())
                );
    }

    private void mapFetchFirstBatch(final String operation, MockServerClient client) throws IOException {
        client
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/api/connectors/initialpush")
                        ,
                        unlimited()
                )
                .callback(
                        callback()
                                .withCallbackClass(FetchFirstBatchCallback.class.getName())
                );
    }

    private void mapNotification(final String operation, MockServerClient client) throws IOException {
        client
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/api/notifications/".concat(venzeeStoreName).concat("/"))
                        ,
                        unlimited()
                )
                .callback(
                        callback()
                                .withCallbackClass(NotificationCallback.class.getName())
                );
    }

}
