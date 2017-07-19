import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpResponse.notFoundResponse;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.ACCEPTED_202;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mockserver.mock.action.ExpectationCallback;

import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mromero on 7/17/17.
 */

public class TestExpectationCallback {//implements ExpectationCallback {
    /*
    public static HttpResponse httpResponse = response()
            .withStatusCode(ACCEPTED_202.code())
            .withHeaders(
                    header("x-callback", "test_callback_header"),
                    header("Content-Length", "a_callback_response".getBytes().length),
                    header("Connection", "keep-alive")
            )
            .withBody("a_callback_response");

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        if (httpRequest.getPath().getValue().endsWith("/callback")) {

            Pattern pattern;
            Matcher matcher;

            JSONParser parser = new JSONParser();
            try {
                JSONObject request = (JSONObject) parser.parse(httpRequest.getBodyAsString());
                String eventName = (String) request.get("eventName");
                String targetUrl = (String) request.get("targetUrl");
                String batchSize = String.valueOf(request.get("batchSize"));
                String collectionId = (String) request.get("collectionId");


                pattern = Pattern.compile("\\d+/items");
                matcher = pattern.matcher(targetUrl);
                matcher.find();
                String IntegrationId = matcher.group(0).replaceAll("/items", "");

                //PREPARE RESPONSE FILE
                JSONObject responseObject = new JSONObject();
                responseObject.put("eventName", eventName);
                responseObject.put("collectionId", collectionId);
                responseObject.put("targetUrl", "http://localhost:8080/integrations/shopify/".concat(IntegrationId).concat("/items"));
                responseObject.put("batchSize", batchSize);
                responseObject.put("status", "inactive");
                //responseObject.put("id", "TFE0GM29K5");
                //responseObject.put("id", "CREATE20X1");
                //responseObject.put("id", "UPDATE20Z2");
                if(eventName.equals("product.create")){
                    responseObject.put("id", "CREATE10X1");
                }
                else if(eventName.equals("product.update")){
                    responseObject.put("id", "UPDATE20Z2");
                }
                responseObject.put("created", Instant.now().toString());
                responseObject.put("lastUpdated",  Instant.now().toString());

                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                FileWriter responseFile = new FileWriter("src/main/resources/responseFile.json");
                //responseFile.write(responseObject.toJSONString());
                responseFile.write(gson.toJson(responseObject));
                responseFile.flush();
                responseFile.close();
                System.out.println("Successfully Copied JSON Object to File...");




            }
            catch (Exception e) {
                e.printStackTrace();
            }
            //return response;

            /*
            JSONObject jsonObject = new JSONObject();

            String name = (String) jsonObject.get("Name");
            String author = (String) jsonObject.get("Author");
            JSONArray companyList = (JSONArray) jsonObject.get("Company List");

            System.out.println("Name: " + name);
            System.out.println("Author: " + author);
            System.out.println("\nCompany List:");
            Iterator<String> iterator = companyList.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
            *--/


            return httpResponse;
        } else {
            return notFoundResponse();
        }
    }

    /*
    public static void main(String [] args){
        //System.out.println("Classpath: " + TestExpectationCallback.class);

        ClassLoader loader = TestExpectationCallback.class.getClassLoader();
        System.out.println("ClassLoader: " + loader.getResource("TestExpectationCallback.class"));
    }
    */
}
