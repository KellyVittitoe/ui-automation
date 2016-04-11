package com.ibm.uitests.pages;

import org.openqa.selenium.WebDriver;

public class ChangePasswordPage extends Page{

	public ChangePasswordPage(WebDriver driver, String tempPassword) {
		super(driver);
		
		try{
			getElement("username");
			getElement("changePasswordOldPassword");
			getElement("changePasswordNewPassword1");
			getElement("changePasswordNewPassword2");
			getElement("changePasswordButton");
		}catch(Exception e){
			String message = "Did not find the change password page.";
			logWarn(message);
			throw new IllegalStateException(message);
		}
		
		changePassword(tempPassword);
	}

	public void changePassword(String tempPassword){
		getElement("username").sendKeys(getConfig("username"));
		getElement("changePasswordOldPassword").sendKeys(tempPassword);
		getElement("changePasswordNewPassword1").sendKeys(getConfig("password"));
		getElement("changePasswordNewPassword2").sendKeys(getConfig("password"));
		getElement("changePasswordButton").click();
	}
}
