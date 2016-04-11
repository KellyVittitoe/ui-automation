package com.ibm.uitests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class RegisterForUBXTrial extends Page{

	public RegisterForUBXTrial(WebDriver driver) {
		super(driver);
		
		if(!driver.getCurrentUrl().equals("https://apps.ibmsbsdd1.com/account/public/trial/signup?partNumber=UBX_TRIAL")){
			String message = "Did not find the Register For UBX Trial Page.";
			logWarn(message);
			throw new IllegalStateException(message);
		}
		
		register();
	}

	public void register(){
		getElement("registerEmail").sendKeys(getConfig("registerEmail"));
		getElement("registerEmail2").sendKeys(getConfig("registerEmail"));
		getElement("registerPassword").sendKeys(getConfig("registerPassword"));
		getElement("registerPassword2").sendKeys(getConfig("registerPassword"));
		getElement("registerFirstName").sendKeys(getConfig("registerFirstName"));
		getElement("registerLastName").sendKeys(getConfig("registerLastName"));
		Select dropdown = new Select(getElement("registerCountry"));
		dropdown.selectByVisibleText(getConfig("registerCountry"));
		getElement("registerSecurityQuestion").sendKeys(getConfig("registerQuestion"));
		getElement("registerSecurityAnswer").sendKeys(getConfig("registerAnswer"));
		getElement("registerSubmit").click();
	}
}
