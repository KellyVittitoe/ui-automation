package com.ibm.uitests.pages;

import org.openqa.selenium.WebDriver;

public class LoginPage extends Page {

	public LoginPage(WebDriver driver, boolean AUTO_LOGIN_FLAG) {
		super(driver);
		
		getElement("password", "Login page is not found!");
		
		if(AUTO_LOGIN_FLAG){
			logIn(getConfig("username"), getConfig("password"));
		}
	}
	
	public void logIn(String username, String password){
		getElement("username").sendKeys(username);
		getElement("password").sendKeys(password);
		getElement("logInButton").click();
	}
	
	public void clickResetPassword(){
		getElement("resetPasswordButton").click();
	}
	
	public void clickChangePassword(){
		getElement("changePasswordLink").click();
	}
	
	public boolean isMessage(String message){
		String display =  getElement("resetPasswordMessage").getText();
		return display.contains(message);
	}
	
	public void clickProductDocumentation(){
		waitFor(1000);
		getElement("help").click();
		waitFor(100);
		getElement("productDocumentation").click();
	}
}
