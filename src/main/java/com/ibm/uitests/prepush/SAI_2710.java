package com.ibm.uitests.prepush;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ibm.uitests.common.UITest;
import com.ibm.uitests.pages.DashboardTab;
import com.ibm.uitests.pages.EndpointsTab;
import com.ibm.uitests.pages.EndpointsTab.Endpoints;
import com.ibm.uitests.pages.EventsTab;
import com.ibm.uitests.pages.LoginPage;
import com.ibm.uitests.pages.HomePage.Tab;
import com.ibm.uitests.pages.HttpRequest;

public class SAI_2710 extends UITest{
	
	private String[] codes = new String[9];

	public SAI_2710(WebDriver driver) {
		super(driver);
		
		setTestName(this.getClass().getName());
		logInfo(getTestName()+": Begin Test");
		
		if(setup()){
			test(test1());
			test(test2());
			test(test3());
		}
		
		cleanup();
	}

	public static void main(String[] args) {
		new SAI_2710(new FirefoxDriver());
	}

	private boolean setup(){
		logInfo(getTestName() + ": Setup - New event type registered.");
		
		codes[0] = "ibmcartAbandonment";
		codes[1] = "ibmcartAbandonmentItem";
		codes[2] = "ibmcartPurchase";
		codes[3] = "ibmcartPurchaseItem";
		codes[4] = "ibmcartConversion";
		codes[5] = "ibmcartAbandonedConversion";
		codes[6] = "ibmProduceView";
		codes[7] = "ibmSearchedSite";
		codes[8] = "ibmVisitedSite";
		
		gotoGUI();
		
		new LoginPage(driver, true);
		
		DashboardTab dashboardTab = new DashboardTab(driver);
		waitFor(500);
		dashboardTab.clickTab(Tab.ENDPOINTS);
		
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		setEventKey(endpointsTab.createNewEndpoint(getEventName(), getDescription(), Endpoints.EVENT, false));
		
		return getEventKey() != null && !getEventKey().equals("");
	}
	
	private boolean test1(){

		EndpointsTab endpointsTab = new EndpointsTab(driver);
		for(String code: codes){
			HttpRequest response = endpointsTab.createEventType(code, getDescription(), code, getEventKey());
			
			if(response.getResponseCode() >= 300){
				logWarn("Failed to add eventtype: "+code);
				logWarn("Test 1: FAIL.");
				return false;
			}
		}
		
		logInfo("Test 1: PASS.");
		return true;
	}
	
	private boolean test2(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		endpointsTab.clickTab(Tab.EVENTS);
		EventsTab eventsTab = new EventsTab(driver);
		eventsTab.openSubscribeModal();
		
		for(String code: codes){
			eventsTab.searchEventtype(code);
			if(!eventsTab.eventtypeExists()){
				logWarn("Cannot confirm the eventtype in the UI: "+code);
				logWarn("Test 2: FAIL.");
				return false;
			}
		}
		
		logInfo("Test 2: PASS.");
		return true;
	}
	
	private boolean test3(){
		EventsTab eventsTab = new EventsTab(driver);
		for(String code: codes){
			waitFor(200);
			eventsTab.searchEventtype(code);
			waitFor(1000);
			if(!eventsTab.getEventtypeInfo(code, getDescription(), getEventName(), "IBM")){
				logWarn("Cannot confirm the eventtype information in the information popup");
				logWarn("Test 3: FAIL.");
				return false;
			}
			eventsTab.closeEventtypeInfo();
		}
		
		logInfo("Test 3: PASS.");
		return true;
	}
	
	private void cleanup(){
		logInfo(getTestName()+": Clean-up");
		driver.quit();
		
		APIdisableEndpoint(getEventKey());
		APIdeleteEndpoint(getEventKey());
	}
}
