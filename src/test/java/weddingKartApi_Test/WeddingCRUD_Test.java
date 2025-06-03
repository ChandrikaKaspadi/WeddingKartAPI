package weddingKartApi_Test;

import static org.testng.Assert.assertTrue;
import org.json.simple.JSONObject;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.restassured.response.Response;
import weddingKart_Endpoints.IEndPoints;
import weddingKart_GenericUtility.BaseClass;

import static io.restassured.RestAssured.*;
public class WeddingCRUD_Test extends BaseClass {
	public Integer weddingId;
	@Test
	public void listAllWeddings_Test() {
		test.log(Status.INFO, "Starting to list all weddings...");
		Response response = given()
		.spec(reqSpecObj)
		.get(IEndPoints.weddingsAllWeddingList);
		test.log(Status.INFO, "Request sent to retrieve all weddings.");
		test.log(Status.INFO, "Response Status Code: " + response.getStatusCode());
		response.then().log().all()
		.spec(responseSpecObj);	
		test.log(Status.INFO, "Completed listing all weddings.");
	}
	
	
	@Test
	public void addNewWedding_Test() {
		test.log(Status.INFO, "Starting to add a new wedding...");
		JSONObject jObj=new JSONObject();
		jObj.put("wedding_name","Test wedding");
		jObj.put("groom_first_name","GroomApiTest");
		jObj.put("bride_first_name","BrideApiTest");
		jObj.put("groom_phone_number","+912589632587");
		jObj.put("bride_phone_number","+912587412589");
		jObj.put("wedding_date", "22-12-2025");
		test.log(Status.INFO, "Request body created: " + jObj.toString());

		Response response = given()
		.spec(reqSpecObj)
		.body(jObj)
		.post(IEndPoints.addNewWedding);
		test.log(Status.INFO, "New wedding request sent to the server.");
		 weddingId = response.jsonPath().getInt("result.id");
		 test.log(Status.INFO, "Wedding ID received: " + weddingId);
		response.then().log().all()
		.spec(responseSpecObj);	
		test.log(Status.INFO, "Completed adding a new wedding.");
	}
	
	
	@Test(dependsOnMethods = "addNewWedding_Test")
	public void updateWeddingDetails_Test() {
		test.log(Status.INFO, "Starting to update wedding details...");
		JSONObject jObj=new JSONObject();
		jObj.put("wedding_id",weddingId );
		jObj.put("wedding_name","Test wedding");
		jObj.put("groom_first_name","GroomTest");
		jObj.put("bride_first_name","BrideTest");
		jObj.put("groom_phone_number","+912589632159");
		jObj.put("bride_phone_number","+912587412258");
		
		 test.log(Status.INFO, "Request Payload created: " + jObj.toString());

		
		Response response = given()
				.spec(reqSpecObj)
				.body(jObj)
				.put(IEndPoints.weddingsUpdateWeddingDetails);
		test.log(Status.INFO, "Response Status Code: " + response.getStatusCode());
		test.log(Status.INFO, "Response Body: " + response.asString());
		response.then().log().all()
		.spec(responseSpecObj);	
		test.log(Status.INFO, "Completed updating wedding details.");
	}
	
	@Test(dependsOnMethods = "addNewWedding_Test")
	public void getSingleWedding_Test() {
		test.log(Status.INFO, "Starting to retrieve details for wedding ID: " + weddingId);
		Response response=given()
				.queryParam("wedding_id", weddingId)
		.spec(reqSpecObj)
		.get(IEndPoints.singleWedding);
		test.log(Status.INFO, "Response Status Code: " + response.getStatusCode());
		response.then().log().all()
		.spec(responseSpecObj);
		test.log(Status.INFO, "Completed retrieving wedding details for ID: " + weddingId);
		
	}
	
	@Test(dependsOnMethods = {"addNewWedding_Test", "updateWeddingDetails_Test", "getSingleWedding_Test"})
	public void deleteWedding_Test() {
		test.log(Status.INFO, "Starting to delete wedding with ID: " + weddingId);
		JSONObject jObj=new JSONObject();
		jObj.put("wedding_id", weddingId);
		test.log(Status.INFO, "Request Payload: " + jObj.toString());
		Response response=given()		
		.spec(reqSpecObj)
		.body(jObj)
		.delete(IEndPoints.deleteWedding);
		test.log(Status.INFO, "Response Status Code: " + response.getStatusCode());
		response.then().log().all()
		.spec(responseSpecObj);
		test.log(Status.INFO, "Completed deleting wedding with ID: " + weddingId);
		
	}
	@Test
	public void listWeddingCreatedByMe() {
		test.log(Status.INFO, "Starting to retrieve the list of weddings created by me.");
		Response response = given()
		.spec(reqSpecObj)
		.get(IEndPoints.weddingsListOfWeddingCreatedByMe);
		test.log(Status.INFO, "Response Status Code: " + response.getStatusCode());
		test.log(Status.INFO, "Response Body: " + response.asString());
		response.then().log().all()
		.spec(responseSpecObj);	
		test.log(Status.INFO, "Completed retrieving the list of weddings created by me.");
	}
	

}
