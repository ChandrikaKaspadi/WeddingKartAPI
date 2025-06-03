package weddingKartApi_Test;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import weddingKart_Endpoints.IEndPoints;
import weddingKart_GenericUtility.BaseClass;

public class GuestCRUDExcelSheet_Test extends BaseClass {
	
	@Test (dataProvider = "excelFiles")
	public void parseGuestExcel_Test(String filePath) throws Throwable{
		test.log(Status.INFO, "Starting to parse guest from excel in the wedding...");
		JSONObject jObj = new JSONObject();
	    jObj.put("wedding_id", wedding_Id);
	 

		File file = new File(filePath);
		if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
		Response response = given()
				.multiPart("upload", file) // File upload
                .multiPart("wedding_id", "1021") // Text parameter
                .header("Content-Type", "multipart/form-data")
                .header("Authorization",pFile.getDataFromPropertyFile("token"))
				.post(pFile.getDataFromPropertyFile("BaseURI")+IEndPoints.guestsParseGuestExcel);
				test.log(Status.INFO, "Request sent to parse guest from excel.");
				response.then().log().all();
				JsonPath jsonResponse = response.jsonPath();
				System.out.println(jsonResponse);

		        // Capturing warnings from response
				
		        if (jsonResponse.get("result.warnings") != null) {
		            System.out.println("Warnings found: " + jsonResponse.getList("result.warnings"));
		        } else {
		            System.out.println("No warnings found.");
		        }

		        // Assertion to check if warnings exist
		        Assert.assertNotNull(jsonResponse.get("result.warnings"), "Warning list should not be null");
		        // Example: Verifying specific warning message (modify as needed)
		        if (!jsonResponse.getList("result.warnings").isEmpty()) {
		            Assert.assertTrue(jsonResponse.getList("result.warnings").contains("'Sheet2' -> Could not find header row with name and phone number columns"),
		                    "Expected warning message not found!");
		        }

		        test.log(Status.INFO, "Completed parsing guest from excel.");
			}

	
	
	@DataProvider(name = "excelFiles")
    public Object[][] excelFilesProvider() {
        String baseDir = System.getProperty("user.dir") + "\\src\\test\\resources\\guestExcelData\\";
        System.out.println("Base Directory: " + baseDir);
        
        return new Object[][]{
            { baseDir + "Good Excel File.xlsx" },
            { baseDir + "Guest list all bad sheets.xlsx" },
            { baseDir + "One Good One Bad File.xlsx" },
            { baseDir + "WrongNumbers.xlsx" }
        };
    }
}
