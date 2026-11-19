package utilities;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class ExtentReportManager implements ITestListener {
	
	public ExtentSparkReporter sparkReporter; //UI of the report
	public ExtentReports extents; //Populate common info on the report
	public ExtentTest test; //Create test case entries in the report and update status of the test methods
	String repName;
	
	public void onStart(ITestContext testContext) 
	{
		/* code to generate current time stamp
		SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		Date dt= new Date();
		String currentdatetimestamp = df.format(dt);
		*/
		
		//above code is simplified to one line
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		
		repName = "Test-Report-"+timeStamp+".html";
		
		sparkReporter = new ExtentSparkReporter(".\\reports\\"+repName); //specify the path of the report
		
		sparkReporter.config().setDocumentTitle("Open Cart Automation report"); //Title of the report
		sparkReporter.config().setReportName("Open Cart Functional testing"); //Name of the report
		sparkReporter.config().setTheme(Theme.DARK); // Theme - Theme.DARK or Theme.STANDARD
		
		extents = new ExtentReports();

		extents.attachReporter(sparkReporter); 
		
		//common info
		extents.setSystemInfo("Application", "openCart");
		extents.setSystemInfo("Module", "Admin");
		extents.setSystemInfo("Sub Module", "Customers");
		extents.setSystemInfo("User name", System.getProperty("user.name"));
		extents.setSystemInfo("Environment", "QA");
		
		//testContext is used to get the current xml parameters
		String os = testContext.getCurrentXmlTest().getParameter("os");
		extents.setSystemInfo("Operating system", os);
		
		String browser = testContext.getCurrentXmlTest().getParameter("browser");
		extents.setSystemInfo("Browser", browser);
		
		List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();	
		if(!includedGroups.isEmpty())
		{
			extents.setSystemInfo("Groups", includedGroups.toString());
		}
		
	}
	
	public void onTestSuccess(ITestResult result) //result var will have the details of the passed test method
	{
		test = extents.createTest(result.getTestClass().getName()); //to get the name of the class in which the method is executed
		//test = extents.createTest(result.getName()); //this returns only the test method which is executed 
		test.assignCategory(result.getMethod().getGroups()); //to display groups in report
		test.log(Status.PASS, result.getName()+" is successfully executed"); // log the method name and the status
	}
	
	public void onTestFailure(ITestResult result) //result var will have the details of the failed test method
	{
		test = extents.createTest(result.getTestClass().getName()); 
		test.assignCategory(result.getMethod().getGroups()); 
		test.log(Status.FAIL, result.getName()+" failed");
		test.log(Status.INFO, "Failure casuse is : "+result.getThrowable().getMessage()); //log the cause of failure
		
		try {
			String imgPath = new BaseClass().captureScreen(result.getName());
			test.addScreenCaptureFromPath(imgPath);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onTestSkipped(ITestResult result) //result var will have the details of the skipped test method
	{
		test = extents.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.SKIP,result.getName()+" is skipped");
		test.log(Status.INFO, result.getThrowable().getMessage());
	}
	
	public void onFinish(ITestContext context) 
	{
		extents.flush();
		
		String pathOfExtentReport = System.getProperty("user.dir")+"\\reports\\"+repName;
		File extentReport = new File(pathOfExtentReport);
		
		try {
			Desktop.getDesktop().browse(extentReport.toURI());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	
	
}
