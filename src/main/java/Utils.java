import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by mromero on 7/19/17.
 */
public class Utils {
    private static String resourcesPath = "src/main/resources/";

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
            //System.out.println("["+ LocalDateTime.now()+"] ".concat("UTILS: jsonResponse: "+response));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
