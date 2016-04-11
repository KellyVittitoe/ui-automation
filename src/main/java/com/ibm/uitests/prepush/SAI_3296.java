package com.ibm.uitests.prepush;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ibm.uitests.common.UITest;
import com.ibm.uitests.pages.RegisterForUBXTrial;

public class SAI_3296 extends UITest{

	public SAI_3296(WebDriver driver) {
		super(driver);
		
		setTestName(this.getClass().getName());
		logInfo(getTestName()+": Begin Test");
		
		setup();
		test(test1());
		
	}

	public static void main(String[] args) {
		new SAI_3296(new FirefoxDriver());
	}
	
	private void setup(){
		logInfo(getTestName() + ": Setup - Create an account.");
	}
	
	private boolean test1(){
		// Register For Universal Behavior Exchange Trial
		gotoPage("https://apps.ibmsbsdd1.com/account/public/trial/signup?partNumber=UBX_TRIAL");
		
		new RegisterForUBXTrial(driver);
		
		return false;
	}
	
	/**
	 * SUSPENDED this test due to anticipated changes
	 * 
	 */
}
