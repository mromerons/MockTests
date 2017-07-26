import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockserver.model.HttpStatusCode.OK_200;

/**
 * Created by mromero on 7/18/17.
 */
public class CreateSubscriptionCallback implements ExpectationCallback {

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        Pattern pattern;
        Matcher matcher;
        String responseFileName = "";

        JSONParser parser = new JSONParser();

        JSONObject request = null;
        try {
            request = (JSONObject) parser.parse(httpRequest.getBodyAsString());
        }
        catch (ParseException e) {
            System.out.println("Request cannot be converted to JSONObject: " + e.getMessage());
            e.printStackTrace();
        }
        String eventName = (String) request.get("eventName");
        System.out.println(eventName);
        String targetUrl = (String) request.get("targetUrl");
        long batchSize = (long) request.get("batchSize");
        String collectionId = (String) request.get("collectionId");

        //pattern = Pattern.compile("\\d+/items");
        //matcher = pattern.matcher(targetUrl);
        //matcher.find();
        //String IntegrationId = matcher.group(0).replaceAll("/items", "");

        JSONObject responseObject = new JSONObject();
        responseObject.put("eventName", eventName);
        responseObject.put("collectionId", collectionId);
        //responseObject.put("targetUrl", "http://localhost:8080/integrations/shopify/".concat(IntegrationId).concat("/items"));
        responseObject.put("targetUrl", targetUrl);
        responseObject.put("batchSize", batchSize);
        responseObject.put("status", "inactive");
        responseObject.put("created", Instant.now().toString());
        responseObject.put("lastUpdated", Instant.now().toString());

        if (eventName.equals("product.created")) {
            responseObject.put("id", "CREATE10X1");
            responseFileName = "generate_create_subscription_response.json";

        } else if (eventName.equals("product.updated")) {
            responseObject.put("id", "UPDATE20Z2");
            responseFileName = "generate_update_subscription_response.json";
        }

        Utils.checkIfFileExist(responseFileName);
        Utils.writeResponseFile(responseFileName, responseObject);

        HttpResponse httpServerResponse = createHttpServerResponse();
        httpServerResponse.withBody(responseObject.toJSONString());
        httpServerResponse.withDelay(Delay.milliseconds(1000));
        //System.out.println("Created subscription for: " + eventName);

        return httpServerResponse;
    }

    private HttpResponse createHttpServerResponse() {
        return new HttpResponse()
                .withStatusCode(OK_200.code())
                .withHeaders(
                        new Header("Content-Type", "application/json; charset=utf-8"),
                        new Header("Cache-Control", "public, max-age=86400")
                );
    }

}
