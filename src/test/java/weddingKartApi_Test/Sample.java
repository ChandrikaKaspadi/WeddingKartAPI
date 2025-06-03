package weddingKartApi_Test;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import weddingKart_Endpoints.IEndPoints;
import weddingKart_GenericUtility.BaseClass;

public class Sample extends BaseClass {
	@Test(dataProvider = "excelFiles")
    public void parseGuestExcel_Test(String filePath, String expectedWarning) throws Throwable {
        test.log(Status.INFO, "Starting to parse guest from Excel: **" + filePath + "**");

        // Creating JSON request body
        JSONObject jObj = new JSONObject();
        jObj.put("wedding_id", "1021");  // Use dynamic wedding_Id if needed

        // Validate file existence
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        // Sending request
        Response response = given()
                .multiPart("upload", file) // File upload
                .multiPart("wedding_id", "1021") // Text parameter
                .header("Content-Type", "multipart/form-data")
                .header("Authorization", pFile.getDataFromPropertyFile("token"))
                .post(pFile.getDataFromPropertyFile("BaseURI") + IEndPoints.guestsParseGuestExcel);

        test.log(Status.INFO, "Request sent to parse guest from Excel: **" + filePath + "**");
        response.then().log().all();

        // Extracting response JSON
        JsonPath jsonResponse = response.jsonPath();

        // Capturing warnings from response
        List<String> warnings = jsonResponse.getList("result.warnings");

        // Logging warnings in test logs
        if (warnings != null && !warnings.isEmpty()) {
            test.log(Status.WARNING, "Warnings found in file: **" + filePath + "**");
            for (String warning : warnings) {
                test.log(Status.WARNING, "⚠ " + warning);
            }
        } else {
            test.log(Status.PASS, "✅ No warnings found in file: **" + filePath + "**");
        }

        // Assertion: Validate warnings based on expected result
        if (expectedWarning != null && !expectedWarning.isEmpty()) {
            Assert.assertNotNull(warnings, "Expected warnings but found none!");

            // Assert that expected warning is present
            boolean isWarningPresent = warnings.stream().anyMatch(warning -> warning.contains(expectedWarning));
            Assert.assertTrue(isWarningPresent, "❌ Expected warning not found in response!");
            test.log(Status.PASS, "✅ Expected warning found: **" + expectedWarning + "**");
        } else {
            // If no warning is expected, ensure warnings list is empty or null
            Assert.assertTrue(warnings == null || warnings.isEmpty(), "❌ Unexpected warnings found!");
        }

        test.log(Status.INFO, "Completed parsing guest from Excel: **" + filePath + "**");
    }
	
	

    @DataProvider(name = "excelFiles")
    public Object[][] excelFilesProvider() {
        String baseDir = System.getProperty("user.dir") + "\\src\\test\\resources\\guestExcelData\\";
        
        return new Object[][]{
            {baseDir + "Good Excel File.xlsx", ""},  // No warnings expected
            {baseDir + "Guest list all bad sheets.xlsx", "'Sheet2' -> Could not find header row with name and phone number columns"},
            {baseDir + "One Good One Bad File.xlsx", "Sheet1:A2 -> No phone numbers found for Deepika"},
            {baseDir + "WrongNumbers.xlsx", "Sheet1:A3 -> No phone numbers found for Chaitra"}
        };
    }
}