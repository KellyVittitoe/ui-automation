package com.ibm.uitests.pages;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WelcomePage extends HomePage{

	public WelcomePage(WebDriver driver, boolean auto_start_working) {
		super(driver);
		
		try{
			getElement("welcome");
		}catch(Exception e){
			logWarn("Did not find the welcome page.");
			throw new IllegalStateException("Did not find the welcome page.");
		}
		
		if(auto_start_working){
			clickStartWorking();
		}
	}
	
	public enum LearnMore{
		Endpoints, Events, Audiences
	}
	
	public void clickStartWorking(){
		getElement("StartWorkingButton").click();
	}
	
	public void clickLearnAboutUBX(){
		getElement("LearnAboutUBXButton").click();
	}
	
	public void clickRegisterEndpoints(){
		getElement("welcomeRegisterEndpoints").click();
	}
	
	public boolean isKeyConceptsPage(){
		String text = getElement("welcomeKeyConcepts").getText();
		return text.equals("Key concepts");
	}
	
	public boolean isUsingUBXPage(){
		String text = getElement("welcomeUsingUBX").getText();
		return text.contains("Using UBX");
	}
	
	public boolean isReadyToTryPage(){
		String text = getElement("welcomeReadytoTry").getText();
		return text.contains("Ready to try");
	}
	
	public void clickForwardArrow(){
		getElement("welcomeForwardArrow").click();
	}
	
	public boolean verifyLearnMore(LearnMore learnMore, WebDriver driver){
		this.driver = driver;
		String identifier = "welcome"+learnMore.toString();
		getElement(identifier).click();
		
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
	    driver.switchTo().window(tabs2.get(1));
	    
	    boolean isCurrentURL =  driver.getCurrentUrl().toString().equals("https://www.ibm.com/support/knowledgecenter/SS9JVY/UBX/kc_welcome_UBX.dita?lang=en");
	    if(!isCurrentURL){
	    	logWarn("Unable to confirm new window after clicking \"Learn more\"");
	    }
	    
	    driver.close();
	    driver.switchTo().window(tabs2.get(0));
	    
	    return !isCurrentURL;
	}
	
	
}
