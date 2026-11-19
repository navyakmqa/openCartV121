package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObject.AccountRegistrationPage;
import pageObject.HomePage;
import testBase.BaseClass;

//BeforeClass, AfterClass, Test annotation methods in BaseClass gets executed when extends is mentioned
public class TC001_AccountRegistrationTest extends BaseClass {

	
	@Test(groups={"Sanity","Master"})
	public void verify_account_registration() throws InterruptedException
	{
		
		logger.info("***** TC001_AccountRegistrationTest started *****");
		
		try {
		HomePage hp = new HomePage(driver);
		AccountRegistrationPage acc = new AccountRegistrationPage(driver);
		
		hp.clickMyAccount();
		
		hp.clickRegister();
		logger.info("Clicked on Register");
		
		String pwd = randomAlphaNumeric()+"@";
		
		logger.info("Entering registration details");
		acc.setFirstName(randomString().toUpperCase());
		acc.setLastName(randomString().toUpperCase());
		acc.setEmail(randomString()+"@gmail.com");
		acc.setTelPhone(randomNumber());
		acc.setPassword(pwd);
		acc.setConfirmPwd(pwd);
		
		acc.clickAgree();
		acc.clickContinue();
		
		String msg = acc.confirmMsg();
		
		if(msg.equals("Your Account Has Been Created!"))
		{
			Assert.assertTrue(true);
			logger.info("Registration is completed");
		}
		
		else
		{
			logger.error("Registration failed");
			Assert.assertTrue(false);
		}
		
		}
		
		catch(Exception e)
		{
			logger.info("Account registration test failed");
			logger.info(e.getMessage());
			Assert.fail(); //it is to mark the test case as failed
		}
		
		logger.info("***** TC001_AccountRegistrationTest finished *****");
	}
	
	
}
