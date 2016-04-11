package com.ibm.uitests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ResetPasswordPage extends Page{

	public ResetPasswordPage(WebDriver driver) {
		super(driver);
		try{
			getElement("username");
			getElement("email");
			getElement("resetPasswordCancelButton");
			getElement("resetPasswordButton");
		}catch(Exception e){
			String message = "Did not find the reset password page.";
			logWarn(message);
			throw new IllegalStateException(message);
		}
	}

	public void resetPassword(String username, String email, boolean commit){
		WebElement usernameTextBox = getElement("username");
		WebElement emailTextBox = getElement("email");
		
		usernameTextBox.clear();
		emailTextBox.clear();
		
		usernameTextBox.sendKeys(username);
		emailTextBox.sendKeys(email);
		
		if(commit){
			getElement("resetPasswordButton").click();
		}else{
			getElement("resetPasswordCancelButton").click();
		}
	}
}
