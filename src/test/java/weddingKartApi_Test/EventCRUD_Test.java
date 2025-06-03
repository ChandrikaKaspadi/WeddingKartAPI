package weddingKartApi_Test;

import static io.restassured.RestAssured.given;

import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.restassured.response.Response;
import weddingKart_Endpoints.IEndPoints;
import weddingKart_GenericUtility.BaseClass;

public class EventCRUD_Test extends BaseClass {
	public Integer eventId;

	@Test
	public void addEvent_Test() {
		System.out.println(wedding_Id);
		test.log(Status.INFO, "Staring to add Event to wedding...");
		JSONObject jObj=new JSONObject();
		jObj.put("wedding_id", wedding_Id);
		jObj.put("event_name", "reception");
		jObj.put("attire", "lehanga");
		jObj.put("date", jLib.getSystemDatePlusOneMonthYYYYMMDD());
		jObj.put("time", "4 pm");
		jObj.put("venue", "Bangalore");	
		Response response=given()
				.spec(reqSpecObj)
				.body(jObj)
				.post(IEndPoints.eventsAddEvent);
		test.log(Status.INFO, "Request sent to add event");

		response.then().log().all()
		.spec(responseSpecObj);
		test.log(Status.INFO, "Completed adding event to wedding");	
	}

	@Test(dependsOnMethods = "addEvent_Test")
	public void listEvents_Test() {
		test.log(Status.INFO, "Starting to list the Events");
		System.out.println(wedding_Id);

		Response response = given()
				.spec(reqSpecObj)
				.queryParam("wedding_id", wedding_Id)
				.get(IEndPoints.eventsListEvents);
		test.log(Status.INFO, "Request sent to list events");
		eventId=response.jsonPath().getInt("result.events[0].event_id");
		System.out.println(eventId);
		response.then().log().all()
		.spec(responseSpecObj);
		test.log(Status.INFO, "Completed listing events in the Wedding");	

	}

	@Test(dependsOnMethods ="listEvents_Test" )
	public void updateEvent_Test() {
		test.log(Status.INFO, "Staring to update Event to wedding...");
		JSONObject jObj=new JSONObject();
		jObj.put("wedding_id", wedding_Id);
		jObj.put("event_id", eventId);
		jObj.put("event_name", "reception");
		jObj.put("attire", "lehanga");
		jObj.put("date", jLib.getSystemDatePlusOneMonthYYYYMMDD());
		jObj.put("time", "4 pm");
		jObj.put("venue", "Bangalore");	
		Response response=given()
				.spec(reqSpecObj)
				.body(jObj)
				.put(IEndPoints.eventsUpdateEvent);
		test.log(Status.INFO, "Request sent to update event");
		response.then().log().all()
		.spec(responseSpecObj);
		test.log(Status.INFO, "Completed update event....");
	}
	
	@Test(dependsOnMethods = "updateEvent_Test")
	public void getEventByEventID_Test() {
		test.log(Status.INFO, "Starting to retrieve Event by  Event ID...");
		Response response=given()
				.spec(reqSpecObj)
				.queryParam("wedding_id", wedding_Id)
				.queryParam("event_id", eventId)
				.get(IEndPoints.eventsGetEventById);
		test.log(Status.INFO, "Request sent to retrieve event by event ID");
		response.then().log().all()
		.spec(responseSpecObj);
		test.log(Status.INFO, "Completed retrieving the event....");
		
	}
	
	@Test(dependsOnMethods="getEventByEventID_Test")
	public void deleteEventById_Test() {
		test.log(Status.INFO, "Staring to delete the event by event ID from wedding...");
		JSONObject jObj=new JSONObject();
		jObj.put("wedding_id", wedding_Id);
		jObj.put("event_id", eventId);
		Response response=given()
				.spec(reqSpecObj)
				.body(jObj)
				.delete(IEndPoints.eventsDeleteEventById);
		test.log(Status.INFO, "Request sent to delete event");
		response.then().log().all()
		.spec(responseSpecObj); 
		test.log(Status.INFO, "Completed deleting the event from wedding....");
	}

}
