package weddingKart_GenericUtility;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import weddingKart_Endpoints.IEndPoints;

import static io.restassured.RestAssured.given;

import java.sql.SQLException;

@Listeners(weddingKart_GenericUtility.ListenerImpClass.class)
public class BaseClass {
	public static RequestSpecification reqSpecObj;
	public static ResponseSpecification responseSpecObj;
	public PropertyFileUtility pFile=new PropertyFileUtility();
	public static  ExtentTest test;
	public static int wedding_Id;
	public JavaUtility jLib=new JavaUtility();

	@BeforeSuite(alwaysRun = true)
	public void configBS() throws Throwable {
		RequestSpecBuilder builder=new RequestSpecBuilder();
		builder.setContentType(ContentType.JSON);	
	//	builder.setAuth(oauth2(pFile.getDataFromPropertyFile("token")));
		builder.setBaseUri(pFile.getDataFromPropertyFile("BaseURI"));
		builder.addHeader("Authorization",pFile.getDataFromPropertyFile("token"));
          reqSpecObj = builder.build();
              
        ResponseSpecBuilder builder1=new ResponseSpecBuilder();
  		builder1.expectContentType(ContentType.JSON);
  		builder1.expectResponseTime(Matchers.lessThan(6000L));
  		builder1.expectStatusCode(200);
  		builder1.expectBody("status", Matchers.equalTo(1));
  		builder1.expectBody("error_code", Matchers.equalTo(0));
  		 responseSpecObj = builder1.build();
	}
	
	@BeforeClass(alwaysRun = true)
	public void configBC() {
		//create new Wedding
		JSONObject jObj=new JSONObject();
		jObj.put("wedding_name","Test wedding");
		jObj.put("groom_first_name","GroomApiTest");
		jObj.put("bride_first_name","BrideApiTest");
		jObj.put("groom_phone_number","+912589632587");
		jObj.put("bride_phone_number","+912587412589");
		jObj.put("wedding_date", "22-12-2025");
		Response response = given()
		.spec(reqSpecObj)
		.body(jObj)
		.post(IEndPoints.addNewWedding);
		wedding_Id = response.jsonPath().getInt("result.id");
		 response.then().log().all()
			.spec(responseSpecObj);		 
		 
		}

		
	@AfterClass(alwaysRun = true)
		public void configAC() {
		//delete wedding 
		
			JSONObject jObj=new JSONObject();
			jObj.put("wedding_id", wedding_Id);
			Response response=given()		
			.spec(reqSpecObj)
			.body(jObj)
			.delete(IEndPoints.deleteWedding);
			response.then().log().all()
			.spec(responseSpecObj);	
		}
	

	@AfterSuite(alwaysRun = true)
	public void configAS() throws SQLException {
		
		
	}
	
	



}
