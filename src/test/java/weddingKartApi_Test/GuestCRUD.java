package weddingKartApi_Test;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.restassured.response.Response;
import weddingKart_Endpoints.IEndPoints;
import weddingKart_GenericUtility.BaseClass;

public class GuestCRUD extends BaseClass {
	int groupId1, groupId2,groupId3; // Store created group IDs
	int guestId;  // Store created guest IDs

	// this script is to create data required for Guest CRUD 
	@Test(dataProvider = "groupNamesForGroupsCRUD")
	public void createGroupForGuestCRUD_Test(String groupName) {
		test.log(Status.INFO, "Starting group creation for Guest CRUD...");
		JSONObject jObj = new JSONObject();
		jObj.put("wedding_id", wedding_Id);
		jObj.put("group_name", groupName);
		test.log(Status.INFO, "Request body created: " + jObj.toString());
		Response response = given()
				.spec(reqSpecObj)
				.body(jObj)
				.post(IEndPoints.groupsAddNewGroup);
		test.log(Status.INFO, "Group creation request sent to the server.");

		// Log the full response body
		response.then().log().all()
		.spec(responseSpecObj);	
		test.log(Status.INFO, "Completed group creation test.");
	}

	// this script is to create data required for Guest CRUD 
	@Test(dependsOnMethods = {"createGroupForGuestCRUD_Test"})
	public void listGroupsInWeddingForGuestCRUD_Test() {
		test.log(Status.INFO, "Starting to list all groups in Wedding...");
		Response response = given()
				.spec(reqSpecObj)
				.queryParam("wedding_id", wedding_Id)
				.get(IEndPoints.groupsListGroupsInWedding);
		test.log(Status.INFO, "Request sent to retrieve all groups in weddings.");
		response.then().log().all()
		.spec(responseSpecObj);		
		List<Integer> groupIds = response.jsonPath().getList("result.groups.group_id");

		if (groupIds == null || groupIds.isEmpty()) {
			test.log(Status.FAIL, "No group_id returned in response! Response: " + response.asString());
			Assert.fail("group_id list is empty in API response");
		}

		// Assign IDs dynamically
		if (groupId1 == 0 && groupIds.size() > 0) {
			groupId1 = groupIds.get(0);
		}
		if (groupId2 == 0 && groupIds.size() > 1) {
			groupId2 = groupIds.get(1);
		}
		if (groupId3 == 0 && groupIds.size() > 2) {
			groupId3 = groupIds.get(2);
		}
		
		test.log(Status.INFO, "Completed listing all groups in Wedding " + groupIds);	    

	}


	@Test(dependsOnMethods = {"listGroupsInWeddingForGuestCRUD_Test"})

	public void addNewGuestUsingJsonFile_Test() {
		test.log(Status.INFO, "Starting to add new guests to the wedding...");
		if (groupId1 == 0 || wedding_Id == 0) {
			test.log(Status.FAIL, "Group ID or Wedding ID is not set. Ensure listGroupsInWeddingForGuestCRUD_Test runs successfully.");
			Assert.fail("Group ID or Wedding ID is missing.");
		}
		try {
			test.log(Status.INFO, "Reading guest data from JSON file...");
			String jsonData = new String(Files.readAllBytes(Paths.get("src/test/resources/TestData/guestData.json")));
			test.log(Status.INFO, "Successfully read JSON file.");

			// Replace placeholder with dynamic data
			jsonData = jsonData.replace("{{wedding_id}}", String.valueOf(wedding_Id));
			jsonData = jsonData.replace("{{group_id}}", String.valueOf(groupId1));  // Using stored instance variable


			
			Response response = given()
					.spec(reqSpecObj)
					.body(jsonData)
					.post(IEndPoints.guestsAddNewGuest);
			test.log(Status.INFO, " Request sent to add new guest...");

			// Log the full response body
			response.then().log().all()
			.spec(responseSpecObj);
			test.log(Status.INFO, "Completed adding new guest to wedding...");
		} catch (IOException e) {
			e.printStackTrace();
			test.log(Status.FAIL, "Failed to read JSON file: " + e.getMessage());
		}
	}

	@Test(dependsOnMethods = "addNewGuestUsingJsonFile_Test")
	public void listAllGuest_Test() {
	    test.log(Status.INFO, "Starting to list all guests in the wedding...");

		Response response=given()
		.spec(reqSpecObj)
		.queryParam("wedding_id", wedding_Id)
		.queryParam("page", 1)
		.get(IEndPoints.guestsListAllGuest);
		test.log(Status.INFO, "Request sent to retrieve all guest in weddings.");


		// Extract guest ID
	    List<Integer> guestIds = response.jsonPath().getList("result.guest_list.guest_id");
	    if (guestIds == null || guestIds.isEmpty()) {
	        test.log(Status.FAIL, "No guest_id returned in response! Response: " + response.asString());
	        Assert.fail("guest_id list is empty in API response");
	    }
	    if (guestId == 0 && guestIds.size() > 0) {
	        guestId = guestIds.get(0);
	    }
	    test.log(Status.INFO, "Completed listing all guests in Wedding " + guestId);

	}
	

@Test(dependsOnMethods =  {"listAllGuest_Test","listGroupsInWeddingForGuestCRUD_Test"})
public void moveGuestToGroup_Test() {
	test.log(Status.INFO, "Starting to move guest from one group to another in the wedding...");
	JSONObject jObj = new JSONObject();
    jObj.put("wedding_id", wedding_Id);
    jObj.put("guest_ids", new int[]{guestId}); 
    jObj.put("from_group_id", groupId1);
    jObj.put("to_group_ids", new int[]{groupId2}); // Wrap groupId2 in a list

	Response response = given()
	.spec(reqSpecObj)
	.body(jObj)
	.post(IEndPoints.guestsMoveGuestToGroup);
	test.log(Status.INFO, "Request sent to move guest from one group to another group.");
	response.then().log().all()
	.spec(responseSpecObj);
	test.log(Status.INFO, "Completed moving guest from "+groupId1+" group to "+groupId2+" group");		
}


//create one more api to check wheather the guest is moved
//@Test(dependsOnMethods = {"moveGuestToGroup_Test"})
//public void verifyGuestMovedToGroup_Test() {
//    test.log(Status.INFO, "Starting to verify if the guest has been moved to the new group...");
//
//    Response response = given()
//            .spec(reqSpecObj)
//            .queryParam("wedding_id", wedding_Id)
//            .queryParam("guest_id", guestId)
//            .get(IEndPoints.guestsListAllGuest); // Assuming this endpoint returns guest details, including group info
//
//    test.log(Status.INFO, "Request sent to retrieve guest details...");
//
//    // Extract the group ID of the guest from the response
//    int currentGroupId = response.jsonPath().getInt("result.guest_list.find { it.guest_id == " + guestId + " }.group_id");
//
//    // Verify the group ID is as expected
//    if (currentGroupId == groupId2) {
//        test.log(Status.INFO, "Guest " + guestId + " successfully moved to group " + groupId2);
//    } else {
//        test.log(Status.FAIL, "Guest " + guestId + " is in group " + currentGroupId + ", expected group: " + groupId2);
//        Assert.fail("Guest has not been moved to the expected group.");
//    }
//
//    test.log(Status.INFO, "Completed verification of guest movement.");
//}

@Test(dependsOnMethods =  {"moveGuestToGroup_Test"})
public void copyToGroup_Test() {
	test.log(Status.INFO, "Starting to copy guest from one group to another  group in the wedding...");
	JSONObject jObj = new JSONObject();
    jObj.put("wedding_id", wedding_Id);
    jObj.put("guest_ids", new int[]{guestId}); 
    jObj.put("to_group_ids", new int[]{groupId2, groupId3}); // Wrap groupId2 in a list
    Response response = given()
    		.spec(reqSpecObj)
    		.body(jObj)
    		.post(IEndPoints.guestsCopyGuestToGroup);
    		test.log(Status.INFO, "Request sent to copy guest from one group to another group.");
    		response.then().log().all()
    		.spec(responseSpecObj);
    		test.log(Status.INFO, "Completed copying guest from "+groupId1+" group to "+groupId2+" group");	
    
}

//create api to wheather that is copied

@Test(dependsOnMethods = {"copyToGroup_Test"})
public void updateGuest_Test() {
	test.log(Status.INFO, "Starting to update guest details...");
    if (guestId == 0 || wedding_Id == 0) {
        test.log(Status.FAIL, "Guest ID or Wedding ID is missing. Ensure guest creation ran successfully.");
        Assert.fail("Guest ID or Wedding ID is not set.");
    }

    try {
        test.log(Status.INFO, "Reading update guest data from JSON file...");
        String jsonData = new String(Files.readAllBytes(Paths.get("src/test/resources/TestData/updateGuestData.json")));
        test.log(Status.INFO, "Successfully read JSON file.");

        // Replace placeholder with dynamic data
        jsonData = jsonData.replace("{{guest_id}}", String.valueOf(guestId));
        jsonData = jsonData.replace("{{wedding_id}}", String.valueOf(wedding_Id));
        jsonData = jsonData.replace("{{group_id}}", String.valueOf(groupId1));

        test.log(Status.INFO, "Sending API request to update guest details...");
        Response response = given()
                .spec(reqSpecObj)  
                .body(jsonData)
                .put(IEndPoints.guestsUpdateGuest); 

        // Log the full response body
        response.then().log().all()
                .spec(responseSpecObj);
        test.log(Status.INFO, "Updated guest details Suscessfully...");

    } catch (IOException e) {
        e.printStackTrace();
        test.log(Status.FAIL, "Failed to read JSON file: " + e.getMessage());
    }	
}
//api to check whether it is updated

@Test(dependsOnMethods = "updateGuest_Test")
public void getGuestById_Test() {
	test.log(Status.INFO, "Started retrieving guest by Guest Id....");
	if (guestId == 0 || wedding_Id == 0) {
        test.log(Status.FAIL, "Guest ID or Wedding ID is missing. Ensure guest creation ran successfully.");
        Assert.fail("Guest ID or Wedding ID is not set.");
    }
	Response response = given()
            .spec(reqSpecObj)  
            .queryParam("wedding_id", wedding_Id)
            .queryParam("guest_id", guestId)
            .get(IEndPoints.guestsGetGuestByID); 
	test.log(Status.INFO, "Request sent to retrive guest by ID");
    response.then().log().all()
            .spec(responseSpecObj);
    test.log(Status.INFO, "Suscessfully retrieved the guest details by guest id...");
		
}

@Test(dependsOnMethods ="getGuestById_Test" )
public void deleteGuestFromGroup() {

	test.log(Status.INFO, "Started deleting guest from group....");
	if (guestId == 0 || wedding_Id == 0) {
        test.log(Status.FAIL, "Guest ID or Wedding ID is missing. Ensure guest creation ran successfully.");
        Assert.fail("Guest ID or Wedding ID is not set.");
    }
	JSONObject jObj = new JSONObject();
    jObj.put("wedding_id", wedding_Id);
    jObj.put("guest_ids", new int[]{guestId}); 
    jObj.put("from_group_id", groupId1); 
	
	Response response = given()
            .spec(reqSpecObj) 
            .body(jObj)
            .post(IEndPoints.guestsDeleteGuestFromGroup); 
	test.log(Status.INFO, "Request sent to delete guest from group "+groupId1);
    response.then().log().all()
            .spec(responseSpecObj);
    test.log(Status.INFO, "Suscessfully deleted the guest from the group...");
	
}

@Test(dependsOnMethods = "deleteGuestFromGroup")
public void purgeOneGuest_Test() {
	test.log(Status.INFO, "Starting to delete....");
	if (guestId == 0 || wedding_Id == 0) {
        test.log(Status.FAIL, "Guest ID or Wedding ID is missing. Ensure guest creation ran successfully.");
        Assert.fail("Guest ID or Wedding ID is not set.");
    }
	JSONObject jObj = new JSONObject();
    jObj.put("wedding_id", wedding_Id);
    jObj.put("guest_ids", new int[]{guestId});    
    Response response=given()
    		.spec(reqSpecObj)
    		.body(jObj)
    		.delete(IEndPoints.guestsPurgeGuests);
    test.log(Status.INFO, "Request sent to purge guest from group");
    response.then().log().all()
    .spec(responseSpecObj);
    test.log(Status.INFO, "Suscessfully purged the guest from the group..."); 
	
}

@Test(dependsOnMethods = "purgeOneGuest_Test")
public void purgeAllGuest_Test() {
	test.log(Status.INFO, "Starting to delete....");
	if (guestId == 0 || wedding_Id == 0) {
        test.log(Status.FAIL, "Guest ID or Wedding ID is missing. Ensure guest creation ran successfully.");
        Assert.fail("Guest ID or Wedding ID is not set.");
    }
	JSONObject jObj = new JSONObject();
    jObj.put("wedding_id", wedding_Id);    
    Response response=given()
    		.spec(reqSpecObj)
    		.body(jObj)
    		.delete(IEndPoints.guestsPurgeAllGuests);
    test.log(Status.INFO, "Request sent to purge all guest from wedding");
    response.then().log().all()
    .spec(responseSpecObj);
    test.log(Status.INFO, "Suscessfully purged all the guest from the wedding..."); 
	
}

	@DataProvider(name = "groupNamesForGroupsCRUD")
	public Object[][] groupNames() {
		return new Object[][] { {"Test1"}, {"Test2"}, {"Test3"} };
	}

}
