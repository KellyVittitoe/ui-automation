package com.ibm.uitests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public abstract class HomePage extends Page{

	public HomePage(WebDriver driver){
		super(driver);
		
		getElement("isHomePage","UBX Homepage not found!");
	}
	
	public enum Tab{
		EVENTS, AUDIENCES, ENDPOINTS, DASHBOARD
	}
	
	/**
	 * Get current tab
	 * @return
	 */
	public boolean isCurrentTab(Tab tabName){
		WebElement currentTab = getElement("currentTabName");
		return currentTab.getAttribute("heading").toUpperCase().equals(tabName.toString());
	}
	
	public void clickTab(Tab tabName){
		String tab = tabName.toString();
		tab = tab.substring(0,1) + tab.substring(1).toLowerCase();
		refresh();
		waitFor(1500);
		getElement(tab).click();
	}
	
	public void refresh(){
		waitFor(1000);
		WebElement element = getElement("refresh");
		Actions action = new Actions(driver);
		action.moveToElement(element).click().perform();
		waitFor(500);
	}
	
	public boolean messageExists(String message){
		String path = "//body[text()='" + message + "']";
		WebElement element = null;
		try{
			element = driver.findElement(By.xpath(path));
		}catch(Exception e){}
		
		return element != null;
	}
	
	public boolean hasWelcomeBanner(){
		try{
			getElement("welcomeBanner");
			return true;
		}catch(Exception e){}
		
		return false;
	}
	
	public void closeWelcomeBanner(){
		getElement("welcomeBannerClose").click();
	}
	
	public void logout(){
		logInfo("Logging out.");
		getElement("user").click();
		getElement("logout").click();
		
		try{
			LoginPage loginPage = new LoginPage(driver, false);
			logInfo("Successfully logged out.");
		}catch(Exception e){
			logWarn("Failed to log user off.");
		}
		
	}
}
