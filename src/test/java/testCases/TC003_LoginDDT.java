package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.MyAccountPage;
import testBase.BaseClass;
import utilities.DataProviders;

public class TC003_LoginDDT extends BaseClass {
	
	@Test(dataProvider="LoginData", dataProviderClass=DataProviders.class, groups="DDT")
	public void verify_loginDDT(String emailId, String password, String exp)
	{
		logger.info("**** TC003_LoginDDT started ****");
		
		try 
		{
			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			hp.clickLogin();
			
			LoginPage lp = new LoginPage(driver);
			lp.setEmailId(emailId);
			lp.setPassword(password);
			lp.clickLogin();
			
			MyAccountPage accp = new MyAccountPage(driver);
			boolean targetPage = accp.isMyAccountExist();
			
			if(exp.equalsIgnoreCase("valid"))
			{
				if(targetPage==true)
				{	
					logger.info("Login successfull");
					accp.clickLogout();
					Assert.assertTrue(true);
				}
				else
				{
					logger.error("Login failed");
					Assert.assertTrue(false);
				}
			}
			
			if(exp.equalsIgnoreCase("invalid"))
			{
				if(targetPage==true)
				{
					logger.error("Login not working correctly for invalid creds");
					accp.clickLogout();
					Assert.assertTrue(false);
				}
				else
				{
					logger.info("Login working correctly for invalid creds");
					Assert.assertTrue(true);
				}
			}
			
		}
		
		catch(Exception e)
			{
				Assert.fail();
			}
			
		logger.info("**** TC003_LoginDDT finished ****");
		
	}
}
