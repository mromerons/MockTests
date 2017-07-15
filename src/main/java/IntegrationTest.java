import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndProxy;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.MatchType;
import org.mockserver.mockserver.MockServer;
import org.mockserver.model.Body;
import org.mockserver.model.Header;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.model.JsonBody;
import org.mockserver.model.Parameter;
import org.mockserver.model.ParameterBody;
import org.mockserver.model.RegexBody;

import io.netty.handler.codec.http.HttpResponse;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.ws.Response;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.integration.ClientAndProxy.startClientAndProxy;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.matchers.Times.unlimited;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
//import static org.mockserver.model.HttpStatusCode.*;

public class IntegrationTest {
    private ClientAndServer mockServer;
    final String shopifyStoreName = "nearsoft5cgw";
    final String resourcePath = "src/main/resources/";

    @Before
    public void startServer(){
        mockServer = startClientAndServer(1080);
        System.out.println("Mock Server started");
    }

    @After
    public void stopServer(){
        mockServer.stop();
        System.out.println("Mock Server stopped");
    }

    @Test
    public void integrationTests() throws IOException{
        mapGenerateToken("GENERATE_TOKEN", mockServer);     //GENERATE TOKEN
        mapCreateSubscription("CREATE_SUBSCRIPTION", mockServer);     //CREATE SUBSCRIPTION
        mapActivateSubscription("ACTIVATE_SUBSCRIPTION", mockServer);     //ACTIVATE SUBSCRIPTION

        // JUST FOR TEST PURPOSES
        while (true) {   }
    }

    /*
    public void mapResponses(String operation, String resource, ClientAndServer mockServer) throws IOException {
    	if(!operation.contains("subscriptions")){
    		map(operation, resource, mockServer);
    	}
    	else{
    		mapCreateSubscription(operation, mockServer);
    	}
    }
    */

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
                                .withBody(getJsonResponse(resourcePath + "token_response.json"))
                );
    }

    private void mapCreateSubscription(final String operation, MockServerClient client) throws IOException {
        client
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/api/orgs/".concat(shopifyStoreName).concat("/subscriptions"))
                                .withBody(new JsonBody("{eventName: 'product.created'}", MatchType.ONLY_MATCHING_FIELDS))
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
                                .withBody(getJsonResponse(resourcePath + "subscription_request_create_response.json"))

                );
        client
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(operation)
                                .withBody(new JsonBody("{eventName: 'product.updated'}", MatchType.ONLY_MATCHING_FIELDS))
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
                                .withBody(getJsonResponse(resourcePath + "subscription_request_update_response.json"))

                );
    }


    private void mapActivateSubscription(final String operation, MockServerClient client) throws IOException {
        client
                .when(
                        request()
                                .withMethod("POST")
                                //.withPath("/api/orgs/subscriptions/[a-zA-Z0-9-_]+/activate")
                                .withPath("/api/orgs/subscriptions/TFE0GM29K5/activate")
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
                                .withBody(getJsonResponse(resourcePath + "activate_subscription_response_1.json"))


                );
        client
                .when(
                        request()
                                .withMethod("POST")
                                //.withPath("/api/orgs/subscriptions/[a-zA-Z0-9-_]+/activate")
                                .withPath("/api/orgs/subscriptions/SQ42O0YFBW/activate")
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
                                .withBody(getJsonResponse(resourcePath + "activate_subscription_response_2.json"))
                );
    }


    public static String getJsonResponse(String resourceName){
        JSONParser parser = new JSONParser();
        String response="";

        try {
            FileReader responseFile = new FileReader(resourceName);
            response = parser.parse(responseFile).toString();
            //System.out.println("jsonResponse: "+response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    public void generateCustomResponseFiles(){
        //TODO: Change parameters in response files to make an accurate mock test

        String targetUrl = "";
        int integrationId = 0;

        //"targetUrl" : "http://localhost:8080/integrations/shopify/1499231379880/items",


        //"jsons/venzee_request.json"

        //"jsons/token_response"
        //"jsons/subscription_request_create_response.json"
        //"jsons/subscription_request_update_response.json"
        //"jsons/activate_subscription_response_1.json"
        //"jsons/activate_subscription_response_2.json"
    }



    //How to load json
    /*
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
    */

}
