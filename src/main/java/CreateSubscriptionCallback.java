import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.io.FileWriter;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpResponse.notFoundResponse;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.ACCEPTED_202;
import static org.mockserver.model.HttpStatusCode.OK_200;

/**
 * Created by mromero on 7/18/17.
 */
public class CreateSubscriptionCallback implements ExpectationCallback {
    public static HttpResponse httpResponse = response()
            .withStatusCode(OK_200.code())
            .withHeaders(
                    new Header("Content-Type", "application/json; charset=utf-8"),
                    new Header("Cache-Control", "public, max-age=86400")
            )
            .withBody(IntegrationTest.getJsonResponse("src/main/resources/create_subscription_response.json"));

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        System.out.println("CALLBACK!");
        Pattern pattern;
        Matcher matcher;

        JSONParser parser = new JSONParser();
        try {
            System.out.println("TRY!");
            JSONObject request = (JSONObject) parser.parse(httpRequest.getBodyAsString());
            String eventName = (String) request.get("eventName");
            String targetUrl = (String) request.get("targetUrl");
            String batchSize = String.valueOf(request.get("batchSize"));
            String collectionId = (String) request.get("collectionId");

            pattern = Pattern.compile("\\d+/items");
            matcher = pattern.matcher(targetUrl);
            matcher.find();
            String IntegrationId = matcher.group(0).replaceAll("/items", "");

            JSONObject responseObject = new JSONObject();
            responseObject.put("eventName", eventName);
            responseObject.put("collectionId", collectionId);
            responseObject.put("targetUrl", "http://localhost:8080/integrations/shopify/".concat(IntegrationId).concat("/items"));
            responseObject.put("batchSize", batchSize);
            responseObject.put("status", "inactive");

            System.out.println("EventName: " + eventName);
            if(eventName.equals("product.created")){
                responseObject.put("id", "CREATE10X1");
            }
            else if(eventName.equals("product.updated")){
                responseObject.put("id", "UPDATE20Z2");
            }
            responseObject.put("created", Instant.now().toString());
            responseObject.put("lastUpdated",  Instant.now().toString());

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            FileWriter responseFile = new FileWriter("src/main/resources/create_subscription_response.json");
            responseFile.write(gson.toJson(responseObject));
            responseFile.flush();
            responseFile.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return httpResponse;
    }
}
