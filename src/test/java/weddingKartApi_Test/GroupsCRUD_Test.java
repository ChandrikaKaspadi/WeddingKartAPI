 package weddingKartApi_Test;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.restassured.response.Response;
import weddingKart_Endpoints.IEndPoints;
import weddingKart_GenericUtility.BaseClass;
import weddingKart_GenericUtility.Generic;

public class GroupsCRUD_Test extends BaseClass {
	int groupId1, groupId2; // Store created group IDs
	
	
	@Test(dataProvider = "groupNames")
	public void addNewGroupInWedding_Test(String groupName) {    
	    test.log(Status.INFO, "Adding a new group: " + groupName);

	    JSONObject jObj = new JSONObject();
	    jObj.put("wedding_id", wedding_Id);
	    jObj.put("group_name", groupName);

	    Response response = given()
	            .spec(reqSpecObj)
	            .body(jObj)
	            .post(IEndPoints.groupsAddNewGroup);

	    // Log the full response body
	    response.then().log().all()
	    .spec(responseSpecObj);	
	    test.log(Status.INFO, "Group created successfully" );
	}
	
	@Test(dependsOnMethods = {"addNewGroupInWedding_Test"})
	public void listGroupsInWedding_Test() {
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

	    test.log(Status.INFO, "Completed listing all groups in Wedding " + groupIds);
	    

	}



	@Test(dependsOnMethods = {"listGroupsInWedding_Test"})
	public void renameGroup_Test() {
		test.log(Status.INFO, "Starting to rename the group...");
//		HashMap<String,Object> hm = new HashMap<String,Object>();
//		hm.put("wedding_id", "wedding_Id");
//		hm.put("group_id",groupId1);
//		hm.put("new_group_name", "Sangeet");
//		Generic.getObjects(hm);
//		Response response =  Generic.getResponse(reqSpecObj, Generic.getObjects(hm), IEndPoints.groupsRenameGroup);
//		test.log(Status.INFO, "Completed renaming the group.");
		
		
		JSONObject jObj=new JSONObject();
		jObj.put("wedding_id",wedding_Id);
		jObj.put("group_id",groupId1);
		jObj.put("new_group_name", "Sangeet");
		test.log(Status.INFO, "Request body created for renaming group: " + jObj.toString());
		Response response = given()
				.spec(reqSpecObj)
				.body(jObj)
				.post(IEndPoints.groupsRenameGroup);
		test.log(Status.INFO, "Rename group request sent to the server.");
		response.then().log().all()
		.spec(responseSpecObj);	
		test.log(Status.INFO, "Completed renaming the group.");
	}
	

	@Test(dependsOnMethods = {"renameGroup_Test",})
	public void mergeGroups_Test() {
		 test.log(Status.INFO, "Starting to merge 2 groups from the wedding...");
		JSONObject jObj=new JSONObject();
		jObj.put("wedding_id",wedding_Id);
		jObj.put("group_id1",groupId1);
		jObj.put("group_id2",groupId2);
		jObj.put("merged_group_name", "merged_t1");
		test.log(Status.INFO, "Request body created for merging groups: " + jObj.toString());
		Response response = given()
				.spec(reqSpecObj)
				.body(jObj)
				.post(IEndPoints.groupsMergeGroup);
		test.log(Status.INFO, "Merge groups request sent to the server.");
		response.then().log().all()
		.spec(responseSpecObj);	
		test.log(Status.INFO, "Completed merging groups.");
			
	}
		
	
	@Test(dependsOnMethods = "mergeGroups_Test")
	public void deleteGroupFromWedding_Test() {
		 test.log(Status.INFO, "Starting to delete a group from the wedding...");
		JSONObject jObj=new JSONObject();
		jObj.put("wedding_id",wedding_Id);
		jObj.put("group_name","test");
		test.log(Status.INFO, "Request body created: " + jObj.toString());
		Response response = given()
				.spec(reqSpecObj)
				.body(jObj)
				.delete(IEndPoints.groupsDeleteGroupFromWedding);
		test.log(Status.INFO, "Delete group request sent to the server.");
		response.then().log().all()
		.spec(responseSpecObj);	
		test.log(Status.INFO, "Completed deletion of the group from the wedding.");
	}

	@Test(dependsOnMethods = "mergeGroups_Test")
	public void deleteAllGroupsFromWedding_Test() {
		test.log(Status.INFO, "Starting to delete all groups from the wedding...");
		JSONObject jObj=new JSONObject();
		jObj.put("wedding_id",wedding_Id);
		test.log(Status.INFO, "Request body created: " + jObj.toString());
		Response response = given()
				.spec(reqSpecObj)
				.body(jObj)
				.delete(IEndPoints.groupsDeleteAllGroups);
		test.log(Status.INFO, "Delete all groups request sent to the server.");
		response.then().log().all()
		.spec(responseSpecObj);	
		test.log(Status.INFO, "Completed deletion of all groups from the wedding.");
	}
	
	
	@DataProvider(name = "groupNames")
	public Object[][] groupNames() {
	    return new Object[][] { {"Haldi"}, {"Mehendi"}, {"Reception"} };
	}
	
}
