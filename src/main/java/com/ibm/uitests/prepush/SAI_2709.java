package com.ibm.uitests.prepush;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ibm.uitests.common.UITest;
import com.ibm.uitests.pages.DashboardTab;
import com.ibm.uitests.pages.EndpointsTab;
import com.ibm.uitests.pages.EndpointsTab.Endpoints;
import com.ibm.uitests.pages.LoginPage;
import com.ibm.uitests.pages.HomePage.Tab;

public class SAI_2709 extends UITest {
	
	public int count = 0;

	public SAI_2709(WebDriver driver) {
		super(driver);
		
setTestName(this.getClass().getName());
		
		logInfo(getTestName()+": Begin Test");
		
		if(setup()){
			test(test1());
			test(test4());
			test(test5());
			test(test6());
		}
		
		cleanup();
	}

	public static void main(String[] args) {
		new SAI_2709(new FirefoxDriver());
	}
	
	public boolean setup(){
		logInfo(getTestName() + ": Setup - Create test event and destination endpoint.");
		
		gotoGUI();
		
		new LoginPage(driver, true);
		
		return true;
	}
	
	public boolean test1(){
		DashboardTab dashboardTab = new DashboardTab(driver);
		waitFor(500);
		dashboardTab.clickTab(Tab.ENDPOINTS);
		
		EndpointsTab endpointsTab = new EndpointsTab(driver); 
		this.count = endpointsTab.databaseEndpointCount();
		
		if(count <0){
			logWarn("Failed to get initial count of endpoints.");
			logWarn("Test 1: FAIL.");
			return false;
		}
		
		setEventKey(endpointsTab.createNewEndpoint(getEventName(), getDescription(), Endpoints.EVENT));
		
		if(getEventKey() == null || getEventKey().equals("")){
			logWarn("Failed to create new endpoint.");
			logWarn("Test 1: FAIL.");
			return false;
		}
		
		logInfo("Test 1: PASS");
		return true;
	}

	public boolean test4(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		setDestinationKey(endpointsTab.createNewEndpoint(getDestinationName(), getDescription(), Endpoints.DESTINATION));
		
		if(getEventKey() == null || getEventKey().equals("")){
			logWarn("Failed to create new endpoint.");
			logWarn("Test 4: FAIL.");
			return false;
		}
		
		logInfo("Test 4: PASS");
		return true;
	}
	
	public boolean test5(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		
		if(!endpointsTab.endpointExists(getEventName())){
			logWarn("Cannot verify publisher endpoint in the UI.");
			logWarn("Test 5: FAIL.");
			return false;
		}
		
		if(!endpointsTab.endpointExists(getDestinationName())){
			logWarn("Cannot verify subscriber endpoint in the UI.");
			logWarn("Test 5: FAIL.");
			return false;
		}
		
		logInfo("Test 5: PASS");
		return true;
	}
	
	public boolean test6(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		if(endpointsTab.databaseEndpointCount() != this.count +2){
			logWarn("Cannot verify addition of endpoints in the database.");
			logWarn("Test 6: FAIL.");
			return false;
		}
		
		logInfo("Test 6: PASS");
		return true;
	}
	
	public void cleanup(){
		logInfo(getTestName()+": Clean-up");
		driver.quit();
		
		APIdisableEndpoint(getEventKey());
		APIdeleteEndpoint(getEventKey());
		APIdisableEndpoint(getDestinationKey());
		APIdeleteEndpoint(getDestinationKey());
	}
}
