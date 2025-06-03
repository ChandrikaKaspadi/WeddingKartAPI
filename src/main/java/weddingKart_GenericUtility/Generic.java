package weddingKart_GenericUtility;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import weddingKart_Endpoints.IEndPoints;

public class Generic {

	public static  ExtentTest test;
	
	public static JSONObject getObjects(HashMap<String , Object> hm) {
		JSONObject jObj=new JSONObject();
		for (Map.Entry<String, Object> set :
            hm.entrySet()) {

           // Printing all elements of a Map
           System.out.println(set.getKey() + " = "
                              + set.getValue());
           
   		jObj.put(set.getKey(),set.getValue());
   		Generic.log("Obtained json object is", String.valueOf(jObj));
       }
		return jObj;
		
	}
	
	public static Response getResponse(RequestSpecification spec,JSONObject jObj,String IEndPoints) {
		Response response = given()
				.spec(spec)
				.body(jObj)
				.post(IEndPoints);
		Generic.log("Obtained response is", String.valueOf(response));
		return response;
	}
	
	public static void log(String status, String info) {
		switch (status) {
        case "pass":
        	test.log(Status.PASS, info);
            break;
        case "info":
        	test.log(Status.INFO, info);
            break;
        case "warn":
        	test.log(Status.WARNING, info);
            break;
        case "fail":
        	test.log(Status.FAIL, info);
            break;
        default:
            System.out.println("Invalid logging status");
    }
	}
	
}


