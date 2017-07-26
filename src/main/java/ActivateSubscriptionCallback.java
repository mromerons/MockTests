import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import static org.mockserver.model.HttpStatusCode.OK_200;

/**
 * Created by mromero on 7/19/17.
 */
public class ActivateSubscriptionCallback implements ExpectationCallback {

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        String responseFileName = "";
        String createSubscriptionResponseFile = "";

        JSONParser parser = new JSONParser();
        JSONObject request = null;
        try {
            request = (JSONObject) parser.parse(httpRequest.getBodyAsString());
        }
        catch (Exception e) {
            System.out.println("Request cannot be converted to JSONObject: " + e.getMessage());
            e.printStackTrace();
        }

        String subscriptionId = (String) request.get("subscriptionId");
        if (subscriptionId.contains("CREATE")) {
            createSubscriptionResponseFile = "generate_create_subscription_response.json";
        } else if (subscriptionId.contains("UPDATE")) {
            createSubscriptionResponseFile = "generate_update_subscription_response.json";
        }

        JSONObject subscriptionObject = null;
        String subscriptionResponse = Utils.readResponseFile(createSubscriptionResponseFile);
        try {
            subscriptionObject = (JSONObject) parser.parse(subscriptionResponse);
        }
        catch (Exception e) {
            System.out.println("Subscription object from file be converted to JSONObject: " + e.getMessage());
            e.printStackTrace();
        }

        //String id = String.valueOf(subscriotionObject.get("id");
        String eventName = (String) subscriptionObject.get("eventName");
        String collectionId = (String) subscriptionObject.get("collectionId");
        String targetUrl = (String) subscriptionObject.get("targetUrl");
        //String status = (String) subscriotionObject.get("status");
        long batchSize = (long) subscriptionObject.get("batchSize");
        String created = (String) subscriptionObject.get("created");
        String lastUpdated = (String) subscriptionObject.get("created");

        JSONObject responseObject = new JSONObject();
        responseObject.put("id", subscriptionId);
        responseObject.put("eventName", eventName);
        responseObject.put("collectionId", collectionId);
        responseObject.put("targetUrl", targetUrl);
        responseObject.put("status", "active");
        responseObject.put("batchSize", batchSize);
        responseObject.put("backOffDelay", null);
        responseObject.put("activated", true);
        responseObject.put("initialPushRequested", null);
        responseObject.put("created", created);
        responseObject.put("lastUpdated", lastUpdated);

        //System.out.println("JSON RESPONSE: \n" + responseObject.toJSONString());

        if (eventName.equals("product.created")) {
            responseFileName = "activate_create_subscription_response.json";

        } else if (eventName.equals("product.updated")) {
            responseFileName = "activate_update_subscription_response.json";

        }

        Utils.checkIfFileExist(responseFileName);
        Utils.writeResponseFile(responseFileName, responseObject);
        //httpResponse.withBody(responseObject.toJSONString());

        HttpResponse httpServerResponse = createHttpServerResponse();
        httpServerResponse.withBody(responseObject.toJSONString());
        httpServerResponse.withDelay(Delay.milliseconds(1000));
        //System.out.println("Activated subscription for: " + responseObject.toJSONString());

        return httpServerResponse;
    }

    private HttpResponse createHttpServerResponse() {
        return new HttpResponse()
                .withStatusCode(OK_200.code())
                .withHeaders(
                        new Header("Content-Type", "application/json; charset=utf-8"),
                        new Header("Cache-Control", "public, max-age=86400"),
                        new Header("Content-Type", "application/json; charset=utf-8"),
                        new Header("Content-Type", "application/json; charset=utf-8"),
                        new Header("Content-Type", "application/json; charset=utf-8")
                );
    }
}
