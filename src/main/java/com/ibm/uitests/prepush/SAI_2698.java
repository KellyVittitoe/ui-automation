package com.ibm.uitests.prepush;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import com.ibm.uitests.common.UITest;
import com.ibm.uitests.pages.ChangePasswordPage;
import com.ibm.uitests.pages.DashboardTab;
import com.ibm.uitests.pages.LoginPage;
import com.ibm.uitests.pages.ResetPasswordPage;

public class SAI_2698 extends UITest{
	
	private String tempPassword;

	public SAI_2698(WebDriver driver) {
		super(driver);
		
		setTestName(this.getClass().getName());
		
		setup();
		test(test1());
		test(test2());
		test(test3());
		test(test4());
		test(test5());
		test(test6());
		test(test7());
		test(test8());
		cleanup();
	}

	public static void main(String[] args) {
		new SAI_2714(new FirefoxDriver());

	}
	
	private void setup(){
		logInfo(getTestName() + ": Setup - Reset Password.");
		
		gotoGUI();		
	}
	
	/**
	 * Click reset password link
	 * @return
	 */
	public boolean test1(){
		
		LoginPage login = new LoginPage(driver, false);
		login.clickResetPassword();
		
		try{
			new ResetPasswordPage(driver);
		}catch(IllegalStateException e){
			logWarn(e.getMessage());
			logWarn("Test 1: FAIL");
			return false;
		}
		
		
		
		logInfo("Test 1: PASS");
		return true;
	}
	
	/**
	 * Click reset password with missing inputs in text fields.
	 * @return
	 */
	public boolean test2(){
		ResetPasswordPage resetPassword = new ResetPasswordPage(driver);
		resetPassword.resetPassword(getConfig("username"), "", true);
		
		// Ensure we are still on reset password page.
		try{
			new ResetPasswordPage(driver);
		}catch(IllegalStateException e){
			logWarn("Missing error message for missing email.");
			logWarn("Test 2: FAIL");
			return false;
		}
		
		resetPassword = new ResetPasswordPage(driver);
		resetPassword.resetPassword("", getConfig("email"), true);
		
		// Ensure we are still on reset password page.
		try{
			new ResetPasswordPage(driver);
		}catch(IllegalStateException e){
			logWarn("Missing error message for missing username.");
			logWarn("Test 2: FAIL");
			return false;
		}
		
		logInfo("Test 2: PASS");
		return true;
	}
	
	/**
	 * Enter username and email - click cancel
	 * @return
	 */
	public boolean test3(){
		ResetPasswordPage resetPassword = new ResetPasswordPage(driver);
		resetPassword.resetPassword(getConfig("username"), getConfig("email"), false);
		
		try{
			new LoginPage(driver, false);
		}catch(IllegalStateException e){
			logWarn("Did not reach the login page");
			logWarn("Test 3: FAIL");
			return false;
		}
		
		logInfo("Test 3: PASS");
		return true;
	}
	
	/**
	 * Enter valid username and password - click reset password
	 * @return
	 */
	public boolean test4(){
		
		LoginPage login = new LoginPage(driver, false);
		login.clickResetPassword();
		
		ResetPasswordPage resetPassword = new ResetPasswordPage(driver);
		resetPassword.resetPassword(getConfig("username"), getConfig("email"), true);
		
		login = new LoginPage(driver, false);
		if(!login.isMessage("Your password has been reset. A new password will be sent to the email address provided.")){
			logWarn("Failed to verify confirmation message for resetting password.");
			logWarn("Test 4: FAIL");
			return false;
		}
		
		logInfo("Test 4: PASS");
		return true;
	}
	
	/**
	 * Go to email provided for reset
	 * Get temporary password
	 * @return
	 */
	public boolean test5(){
		waitFor(3000);
		Gmail email = new Gmail(getConfig("email"), getConfig("emailPassword"));
		this.tempPassword = email.getTempPassword();
		final String emailMessage = "You will be prompted to create a new password as soon as you sign into UBX with this temporary password.  Please note that this temporary password will expire after 12 hours.This email was sent from a notification-only address that cannot accept incoming email. Please do not reply to this message.";
		if(email.getMessage() == null){
			logWarn("Failed to retrieve the temporary password from gmail.");
			logWarn("Test 5: FAIL");
			return false;
		}
		if(email.getMessage().equals(emailMessage) && tempPassword != null){
			logInfo("Test 5: PASS");
			return true;
		}
		
		logWarn("Failed to get expected message in email.");
		logWarn("Test 5: FAIL");
		return false;
	}
	
	/**
	 * Login to console using temp password provided in the email
	 * @return
	 */
	public boolean test6(){
		LoginPage login = new LoginPage(driver, false);
		login.logIn(getConfig("username"), tempPassword);
		if(login.isMessage("Your password is about to expire. You must change your password before you can continue.")){
			logWarn("Failed to change the password.");
			logWarn("Test 6: FAIL");
			return false;
		}
		login.clickChangePassword();
		logInfo("Test 6: PASS");
		return true;
	}
	
	/**
	 * Provide the in the reset password for old and new password
	 * Get prompted for success
	 * @return
	 */
	public boolean test7(){
		new ChangePasswordPage(driver, tempPassword);
		
		LoginPage login = new LoginPage(driver, false);
		if(!login.isMessage("Your password has been successfully changed. Please login again.")){
			logWarn("Failed to change the password.");
			logWarn("Test 7: FAIL");
			return false;
		}
		
		logInfo("Test 7: PASS");
		return true;
	}
	
	/**
	 * Login using new credintials
	 * @return
	 */
	public boolean test8(){
		LoginPage login = new LoginPage(driver, true);
		try{
			new DashboardTab(driver);
		}catch(IllegalStateException e){
			logWarn("Failed to login with the new password.");
			logWarn("Test 8: FAIL");
			return false;
		}
		
		logInfo("Test 8: PASS");
		return true;
	}
	
	public void cleanup(){
		driver.quit();
	}

}

