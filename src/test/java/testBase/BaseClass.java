package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;



public class BaseClass {
	
public static WebDriver driver;
//driver is made static here because, BaseClass object is created in onTestFailure method of ExtentReportManager
//so there would be two drivers created during runtime
//one is when the test methods extends to BaseClass, setup method gets executed and one driver will be initiated
//one more driver (object var) gets created when the BaseClass object is created in onTestFailure method (line no 90)
//to avoid this conflict, make driver variable as static so that there would be only driver independent of how many times the BaseClass object is created

public Logger logger;
public Properties p;
	
	
	@Parameters({"os", "browser"})
	@BeforeClass(groups= {"Sanity","Regression","Master"})
	public void setup(String os, String br) throws IOException
	{
		//initialize logger variable
		logger = LogManager.getLogger(this.getClass()); //this.getClass() is used to get the current class info
		
		//read from config.properties file
		FileReader file = new FileReader("./src//test//resources/config.properties");
		p=new Properties();
		p.load(file);
		
		//execution env is remote
		
		if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
		{
			DesiredCapabilities capabilities = new DesiredCapabilities();
			
			if(os.equalsIgnoreCase("windows"))
			{
				capabilities.setPlatform(Platform.WIN11);
			}
			else if(os.equalsIgnoreCase("mac"))
			{
				capabilities.setPlatform(Platform.MAC);
			}
			else if(os.equalsIgnoreCase("linux"))
			{
				capabilities.setPlatform(Platform.LINUX);
			}
			else
			{
				System.out.println("No matching os");
				return;
			}
			
			switch(br.toLowerCase())
			{
			case "chrome" : capabilities.setBrowserName("chrome"); break;
			case "edge" : capabilities.setBrowserName("MicrosoftEdge"); break;
			case "firefox" : capabilities.setBrowserName("firefox"); break;
			default : System.out.println("Invalid browser"); return;
			}
			
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),capabilities);
		}
		
		
		
		//execution env is local
		
		if(p.getProperty("execution_env").equalsIgnoreCase("local"))
		{
			//reading the properties from testng xml file to setup the browser
			switch(br.toLowerCase())
			{
			case "chrome" : driver = new ChromeDriver(); break;
			case "edge" : driver = new EdgeDriver(); break;
			case "firefox" : driver = new FirefoxDriver(); break;
			default : System.out.println("Invalid browser"); return;
			}
			
		}
				
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get(p.getProperty("appUrl")); //reading the url from config.properties file
		driver.manage().window().maximize();
	}
	
	@AfterClass(groups= {"Sanity","Regression","Master"})
	public void tearDown()
	{
		driver.quit();
	}
	
	public String randomString()
	{
		String genAlpha = RandomStringUtils.secure().nextAlphabetic(5); //generates random alphabetic string
		return genAlpha;
	}
	
	public String randomNumber()
	{
		String genNum = RandomStringUtils.secure().nextNumeric(10);
		return genNum;
	}
	
	public String randomAlphaNumeric()
	{
		String genAlphaNum = RandomStringUtils.secure().nextAlphanumeric(5);
		return genAlphaNum;
	}
	
	public String captureScreen(String tname) throws IOException
	{
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		TakesScreenshot takescreenshot = (TakesScreenshot) driver;
		File sourceFile = takescreenshot.getScreenshotAs(OutputType.FILE);
		
		String targetFilePath = System.getProperty("user.dir")+"\\screenshots\\"+tname+"_"+timeStamp+".png";
		System.out.println(targetFilePath);
		File targetFile = new File(targetFilePath);
		
		sourceFile.renameTo(targetFile);
		return targetFilePath;	
	}

}
