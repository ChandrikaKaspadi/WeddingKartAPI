package weddingKart_GenericUtility;

import java.time.LocalDateTime;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ListenerImpClass extends BaseClass implements ITestListener,ISuiteListener {

	public ExtentSparkReporter spark;
	public ExtentReports report;

	public void onStart(ISuite suite) {	
		//Spark report configuration
		String time=LocalDateTime.now().toString().replace(':','-');
		spark=new ExtentSparkReporter("./AdvancedReport/report"+time+".html");
		spark.config().setDocumentTitle("WeddingKart Results");
		spark.config().setReportName("WeddingKartAPI Report");
		spark.config().setTheme(Theme.DARK);

		//Attach report, Add environmental information and create Test
		report=new ExtentReports();
		report.attachReporter(spark);
	}

	public void onFinish(ISuite suite) {
		//report backup"
		report.flush();
	}

	public void onTestStart(ITestResult result) {
		test = report.createTest(result.getMethod().getMethodName());
		test.log(Status.INFO, result.getMethod().getMethodName()+"==>STARTED<====");

	}

	public void onTestSuccess(ITestResult result) {
		test.log(Status.PASS, result.getMethod().getMethodName()+"==>COMPLETED<====");
	}

	public void onTestFailure(ITestResult result) {
		test.log(Status.FAIL, result.getMethod().getMethodName()+"==>FAILED<====");
	}

	public void onTestSkipped(ITestResult result) {
		test.log(Status.SKIP, result.getMethod().getMethodName()+"==>SKIPPED<==");


	}
}
