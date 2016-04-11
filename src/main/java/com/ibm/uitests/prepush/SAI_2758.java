package com.ibm.uitests.prepush;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ibm.uitests.common.UITest;
import com.ibm.uitests.pages.DashboardTab;
import com.ibm.uitests.pages.EndpointsTab;
import com.ibm.uitests.pages.LoginPage;
import com.ibm.uitests.pages.EndpointsTab.Endpoints;
import com.ibm.uitests.pages.HomePage.Tab;
import com.ibm.uitests.pages.HttpRequest;

public class SAI_2758 extends UITest{

	public SAI_2758(WebDriver driver) {
		super(driver);
		
		setTestName(this.getClass().getName());
		
		if(setup()){
			test(test1());
			test(test2());
			test(test4());
			test(test6());
			cleanup();
		}else{
			logFatal(getTestName() + ": Setup failed.");
		}
	}

	public static void main(String[] args) {
		new SAI_2758(new FirefoxDriver());
	}

	/**
	 * Create test endpoint
	 * @return
	 */
	private boolean setup(){
		logInfo(getTestName() + ": Setup - Create test event and destination endpoint.");
		
		gotoGUI();
		
		new LoginPage(driver, true);
		
		DashboardTab dashboardTab = new DashboardTab(driver);
		dashboardTab.clickTab(Tab.ENDPOINTS);
		
		waitFor(1500);
		
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		String eventKey = 
				endpointsTab.createNewEndpoint(getEventName(), getDescription(), Endpoints.EVENT);
		
		setEventKey(eventKey);
		
		endpointsTab = new EndpointsTab(driver);
		endpointsTab.refresh();
		
		return eventKey != null &&
				!eventKey.equals("");
	}
	
	/**
	 * Disable endpoint from ui
	 * @return
	 */
	private boolean test1(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		endpointsTab.toggleEndpoint(getEventName(), true);
		endpointsTab.refresh();
		if(endpointsTab.isEndpointDisabled(getEventName())){
			logInfo("Test 1: Pass.");
			return true;
		}
		
		logWarn("Endpoint is not disabled");
		logWarn("Test 1: Fail.");
		return false;
	}
	
	/**
	 * Try to create new endpoint with same key
	 * Verify false
	 * @return
	 */
	private boolean test2(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		HttpRequest response = endpointsTab.createEventType(getEventtypeName(), getDescription(), getEventKey());
		
		if(response.getResponseCode() == 409){
			logInfo("Test 2: Pass.");
			return true;
		}
		
		logWarn("Created new eventtype with existing disabled endpoint authentication key.");
		logWarn("Test 2: Fail.");
		return false;
	}
	
	/**
	 * Verify Processor log
	 * @return
	 */
	private boolean test3(){
		return false;
	}
	
	/**
	 * Enable endpoint
	 * Verify true
	 * @return
	 */
	private boolean test4(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		endpointsTab.toggleEndpoint(getEventName(), true);
		endpointsTab.refresh();
		waitFor(500);
		endpointsTab = new EndpointsTab(driver);
		if(endpointsTab.isEndpointDisabled(getEventName())){
			logWarn("Failed to enable endpoint.");
			logWarn("Test 4: Fail.");
			return false;
		}
		
		
		endpointsTab = new EndpointsTab(driver);
		String response = endpointsTab.createEventEndpoint(getEventName(), getDescription(), getEventKey(), false);
		
		if(response.equals("true")){
			logInfo("Test 4: Pass.");
			return true;
		}
		
		logWarn("Failed to create eventtype with existing active endpoint authentication key.");
		logWarn("Test 4: Fail.");
		return false;
	}
	
	/**
	 * Verify Processor log
	 * @return
	 */
	private boolean test5(){
		return false;
	}
	
	/**
	 * Delete endpoint
	 * Verify 401
	 * @return
	 */
	private boolean test6(){
		APIdisableEndpoint(getEventKey());
		APIdeleteEndpoint(getEventKey());
		
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		HttpRequest response = endpointsTab.createEventType(getEventtypeName(), getDescription(), getEventKey());
		
		if(response.getResponseCode() == 401){
			logInfo("Test 6: Pass.");
			return true;
		}
		
		logWarn("Create eventtype with deleted endpoint authentication key.");
		logWarn("Test 6: Fail.");
		return false;
	}
	
	/**
	 * Update eventtype with name, description and code.
	 * @return
	 */
	private void cleanup(){
		
		driver.quit();
	}

}
