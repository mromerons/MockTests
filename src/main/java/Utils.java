import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mockserver.model.HttpRequest;

import java.io.FileReader;
import java.io.FileWriter;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.File;

/**
 * Created by mromero on 7/19/17.
 */
public class Utils {
    private static String resourcesPath = "src/main/resources/";

    public static String getJsonResponse(String resourceName) {

        JSONParser parser = new JSONParser();
        String response = "";

        try {
            FileReader responseFile = new FileReader(resourceName);
            response = parser.parse(responseFile).toString();
            //System.out.println("jsonResponse: "+response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static JSONObject parseJson(HttpRequest httpRequest){
        Pattern pattern;
        Matcher matcher;

        JSONParser parser = new JSONParser();
        JSONObject responseObject = new JSONObject();

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

            //Gson gson = new GsonBuilder().setPrettyPrinting().create();
            //FileWriter responseFile = new FileWriter("src/main/resources/create_subscription_response.json");
            //responseFile.write(gson.toJson(responseObject));
            //responseFile.flush();
            //responseFile.close();

            //httpResponse.withBody(responseObject.toJSONString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return responseObject;
    }

    public static void checkIfFileExist(String fileName) {
        try{
            String tempFile = resourcesPath.concat(fileName);
            File fileTemp = new File(tempFile);
            if (fileTemp.exists()){
                fileTemp.delete();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void writeResponseFile(String responseFileName, JSONObject responseObject) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter responseFile = new FileWriter(resourcesPath.concat(responseFileName));
            responseFile.write(gson.toJson(responseObject));
            responseFile.flush();
            responseFile.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String readResponseFile(String resourceName) {
        JSONParser parser = new JSONParser();
        String response = "";

        try {
            FileReader responseFile = new FileReader(resourcesPath.concat(resourceName));
            response = parser.parse(responseFile).toString();
            //System.out.println("UTILS: jsonResponse: "+response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
