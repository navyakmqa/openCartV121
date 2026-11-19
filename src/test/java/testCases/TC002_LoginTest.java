package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.MyAccountPage;
import testBase.BaseClass;

public class TC002_LoginTest extends BaseClass {
	
	@Test(groups={"Regression","Master"})
	public void verify_account_login()
	{
		logger.info("**** TC002_LoginTest started ****");
		
		try {
			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			hp.clickLogin();
			
			LoginPage lp = new LoginPage(driver);
			
			logger.info("Entering login details");
			lp.setEmailId(p.getProperty("loginEmailId"));
			lp.setPassword(p.getProperty("loginPassword"));
			lp.clickLogin();
			
			MyAccountPage accp = new MyAccountPage(driver);
			if(accp.isMyAccountExist()==true)
			{
				Assert.assertTrue(true);
				logger.info("Login successful");
			}
			else
			{
				logger.info("Login failed");
				Assert.assertTrue(false);
			}
		}
		catch(Exception e)
		{
			logger.error("Login test failed");
			Assert.fail();
		}
		
		logger.info("**** TC002_LoginTest finished ****");
		
	}
}
