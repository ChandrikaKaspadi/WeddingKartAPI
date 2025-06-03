package weddingKartApi_Test;

import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import weddingKart_Endpoints.IEndPoints;
import weddingKart_GenericUtility.BaseClass;
import weddingKart_GenericUtility.ListenerImpClass;

import static io.restassured.RestAssured.*;

public class UserLogin_Test extends BaseClass {

	@Test
	public void sendOTP_Test() {
		
		Response response = given()
				.spec(reqSpecObj)
				.queryParam("phone_number", "+919999999999")		
				.get(IEndPoints.sendOTP);
		test.log(Status.INFO, "OTP request sent to the server.");
		response.then()
		.body("result.user_id", Matchers.equalTo(42));
		test.log(Status.INFO, "Response validated for user_id equal to 42.");
		response.then().log().all()
		.spec(responseSpecObj);
	}


	@Test(dependsOnMethods = "sendOTP_Test")
	public void authenticateOTP_Test(){
		test.log(Status.INFO, "Starting OTP authentication process...");
		JSONObject jObj=new JSONObject();
		jObj.put("phone_number", "+919999999999");
		jObj.put("otp", "1234");
		test.log(Status.INFO, "Request body created: " + jObj.toString());
		Response response = given()			
				.spec(reqSpecObj)
				.body(jObj)
				.post(IEndPoints.authenticate);
		test.log(Status.INFO, "OTP authentication request sent to the server.");
		response.then()
		.body("result.user_id", Matchers.equalTo(42))
		.body("result.is_new_user", Matchers.equalTo(false))
		.body("result.referral_code", Matchers.equalTo("utSgX6"))
		.body("result.first_name", Matchers.equalTo("Mayank"))
		.body("result.last_name", Matchers.equalTo("Jaiswal"))
		.body("result.phone_number", Matchers.equalTo("+919999999999"));
		test.log(Status.INFO, "Response validated for user_id, is_new_user, referral_code, first_name, last_name, and phone_number.");
		response.then() .log().all()
		.spec(responseSpecObj);
		test.log(Status.INFO, "Completed OTP authentication process.");
	}

	@Test
	public void updateUserDetails( ) {
		test.log(Status.INFO, "Starting user update process...");
		JSONObject jObj=new JSONObject();
		jObj.put("first_name", "Mayank");
		jObj.put("last_name", "Jaiswal");
		jObj.put("fcm_token","fr5n1uVKSC2Qw2v2S3pMUi:APA91bGJy14z0UiOGqQuMXNejFl2lySZJDOKatWo5TdmLEZHBF5qVt55J6_7bLZ_xaDutOagyrBWyN8JmKTmWm1HER_7nh-RD3qsICgciu5g6jM-TD5XVsA");
		test.log(Status.INFO, "Request body: " + jObj.toString());
		Response response = given()			
				.spec(reqSpecObj)
				.contentType(ContentType.JSON)
				.body(jObj)	
				.post(IEndPoints.updateUserDetails);
		test.log(Status.INFO, "Response received from the server.");
		response.then()
		.body("result.firstname", Matchers.equalTo("Mayank")) 
		.body("result.lastname", Matchers.equalTo("Jaiswal")) 
		.body("result.phone_number", Matchers.equalTo("+919999999999"));
		test.log(Status.INFO, "Response body validated for first name, last name, and phone number.");
		response.then() .log().all()
		.spec(responseSpecObj);
		test.log(Status.INFO, "Completed user update process.");
	}

	@Test
	public void userCredits() {
		test.log(Status.INFO, "Starting user credits request...");
		Response response = given()
				.spec(reqSpecObj)
				.queryParam("phone_number", "+919999999999")
				.get(IEndPoints.userCredits);
		test.log(Status.INFO, "User credits request sent to the server.");
		response.then()
		.body("result.referral_code", Matchers.equalTo("utSgX6"));
		response.then().log().all()
		.spec(responseSpecObj);	
		test.log(Status.INFO, "Completed user credits request.");
	}

}



