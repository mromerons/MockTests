import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockserver.model.HttpStatusCode.OK_200;

/**
 * Created by mromero on 7/18/17.
 */
public class CreateSubscriptionCallback implements ExpectationCallback {
    String eventName = "";
    Long integrationId = 0L;

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
            System.out.println("["+LocalDateTime.now()+"] ".concat("Request cannot be converted to JSONObject: ").concat(e.getMessage()));
            e.printStackTrace();
        }
        eventName = (String) request.get("eventName");
        String targetUrl = (String) request.get("targetUrl");
        long batchSize = (long) request.get("batchSize");
        String collectionId = (String) request.get("collectionId");

        System.out.println("["+LocalDateTime.now()+"] ".concat("Creating Subscription for Event: ").concat(eventName));

        pattern = Pattern.compile("\\d+/items");
        matcher = pattern.matcher(targetUrl);
        matcher.find();
        integrationId = Long.valueOf(matcher.group().replaceAll("/items", ""));

        JSONObject responseObject = new JSONObject();
        responseObject.put("eventName", eventName);
        responseObject.put("collectionId", collectionId);
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
        return httpServerResponse;
    }

    private HttpResponse createHttpServerResponse() {
        IntegrationTest.IntegrationId = integrationId;
        System.out.println("["+LocalDateTime.now()+"] ".concat("Subscription created for Event: ").concat(eventName));
        return new HttpResponse()
                .withStatusCode(OK_200.code())
                .withHeaders(
                        new Header("Content-Type", "application/json; charset=utf-8"),
                        new Header("Cache-Control", "public, max-age=86400")
                );
    }

}
