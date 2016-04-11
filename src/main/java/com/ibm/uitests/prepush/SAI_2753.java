package com.ibm.uitests.prepush;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ibm.uitests.common.UITest;
import com.ibm.uitests.pages.DashboardTab;
import com.ibm.uitests.pages.EndpointsTab;
import com.ibm.uitests.pages.LoginPage;
import com.ibm.uitests.pages.EndpointsTab.Endpoints;
import com.ibm.uitests.pages.HomePage.Tab;

public class SAI_2753 extends UITest{
	
	private String eventName, updatedEventName, eventKey;

	public static void main(String[] args) {
		new SAI_2753(new FirefoxDriver());
	}
	
	public SAI_2753(WebDriver driver){
		super(driver);
		setTestName(this.getClass().getName());
		
		if(setup()){
			test(test1());
			test(test2());
			test(test3());
			cleanup();
		}else{
			logFatal(getTestName() + ": Setup failed.");
		}
		
	}

	/**
	 * Create test endpoint
	 * @return
	 */
	private boolean setup(){
		logInfo(getTestName() + ": Setup - Create test event.");
		
		eventName = getConfig("EVENTS");
		updatedEventName = eventName+"-Updated";
		
		gotoGUI();
		
		new LoginPage(driver, true);
		
		DashboardTab dashboardTab = new DashboardTab(driver);
		dashboardTab.clickTab(Tab.ENDPOINTS);
		
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		
		eventKey = endpointsTab.createNewEndpoint(eventName, getConfig("DESCRIPTION"), Endpoints.EVENT);
		
		
		endpointsTab = new EndpointsTab(driver);
		endpointsTab.refresh();
		
		return eventKey != null &&
				!eventKey.equals("");
	}
	
	/**
	 * verify initial name
	 * update and endpoint
	 * @return
	 */
	private boolean test1(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		if(!endpointsTab.endpointExists(eventName)){
			logWarn("Cannot verify the original endpoint name in the UI");
			logWarn("Test 1: FAILED");
			return false;
		}
		
		if(!endpointsTab.databaseEndpointExists(eventName)){
			logWarn("Cannot verify the original endpoint name in the database");
			logWarn("Test 1: FAILED");
			return false;
		}
		
		endpointsTab = new EndpointsTab(driver);
		endpointsTab.createEventEndpoint(updatedEventName, getConfig("DESCRIPTION"), eventKey, false);
		
		logInfo("Test 1: PASS.");
		return true;
	}
	
	/**
	 * Verify change in database
	 * @return
	 */
	private boolean test2(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		endpointsTab.refresh();
		if(!endpointsTab.databaseEndpointExists(updatedEventName)){
			logWarn("Cannot confirm endpoint name update in the database.");
			logWarn("Test2: FAIL");
			return false;
		}
		
		
		logInfo("Test 2: PASS.");
		return true;
	}
	
	/**
	 * Verify update in ui
	 * @return
	 */
	private boolean test3(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		if(!endpointsTab.endpointExists(updatedEventName)){
			logWarn("Cannot confirm endpoint name update in the UI.");
			logWarn("Test3: FAIL");
			return false;
		}
		
		logInfo("Test 3: PASS.");
		return true;
	}
	
	/**
	 * delete endpoint
	 * @return
	 */
	private void cleanup(){
		APIdisableEndpoint(eventKey);
		APIdeleteEndpoint(eventKey);
		driver.quit();
	}
}
