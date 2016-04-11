package com.ibm.uitests.prepush;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ibm.uitests.common.UITest;
import com.ibm.uitests.pages.DashboardTab;
import com.ibm.uitests.pages.EndpointsTab;
import com.ibm.uitests.pages.EventsTab;
import com.ibm.uitests.pages.EventsTab.EventsSubTab;
import com.ibm.uitests.pages.EventsTableView;
import com.ibm.uitests.pages.LoginPage;
import com.ibm.uitests.pages.EndpointsTab.Endpoints;
import com.ibm.uitests.pages.HomePage.Tab;

public class SAI_2722 extends UITest{
	
	public static void main(String[] args) {
		new SAI_2722(new FirefoxDriver());
	}

	public SAI_2722(WebDriver driver){
		super(driver);
		setTestName(this.getClass().getName());
		
		logInfo(getTestName()+": Begin Test");
		
		if(setup()){
			
			test(test1());
			test(test2());
			test(test3());
			test(test4());
			test(test5());
			test(test6());
			
			if(testResult()){
				logInfo(getTestName()+": All Tests: PASS.");
			}else{
				logWarn(getTestName()+": Test finished: with Failure(s).");
			}
		}else{
			logFatal(getTestName() + ": Setup FAILED!");
		}
		cleanup();
	}
	
	/**
	 * Create test provider endpoint with event, and test destination endpoint. 
	 * @return
	 */
	private boolean setup(){
		logInfo(getTestName() + ": Setup - Create test event and destination endpoint.");
		
		gotoGUI();
		
		new LoginPage(driver, true);
		
		DashboardTab dashboardTab = new DashboardTab(driver);
		dashboardTab.clickTab(Tab.ENDPOINTS);
		
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		setDestinationKey(endpointsTab.createNewEndpoint(getDestinationName(), getConfig("DESCRIPTION"), Endpoints.DESTINATION));
		setEventKey(endpointsTab.createNewEndpoint(getEventName(), getConfig("DESCRIPTION"), Endpoints.EVENT));
		
		
		endpointsTab = new EndpointsTab(driver);
		endpointsTab.refresh();
		
		return getDestinationKey() != null && 
				!getDestinationKey().equals("") &&
				getEventKey() != null &&
				!getEventKey().equals("");
	}
	
	/**
	 * Verify Endpoint in database
	 * Subscribe to the event in UI
	 * @return
	 */
	private boolean test1(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		if(!endpointsTab.databaseEndpointExists(getEventName())){
			logWarn("Endpoint is not found in the database: "+getEventName());
			logWarn("Test 1: FAIL");
			return false;
		}
		
		endpointsTab.clickTab(Tab.EVENTS);
		EventsTab eventsTab = new EventsTab(driver);
		eventsTab.subscribeToEvent(getEventtypeName(), getDestinationName(), true);
		
		eventsTab.refresh();
		eventsTab = new EventsTab(driver);
		eventsTab.clickEventsSubTab(EventsSubTab.TABLE);
		
		EventsTableView tableView = new EventsTableView(driver);
		if(!tableView.subscriptionExists(getEventtypeName(), getDestinationName())){
			logWarn("Subscription was not found!");
			logWarn("Test 1: FAIL");
			return false;
		}
		
		logInfo("Test 1: PASS");
		return true;
	}
	
	/**
	 * Unsubscribe - cancel
	 * @return
	 */
	private boolean test2(){
		EventsTableView tableView = new EventsTableView(driver);
		tableView.unsubscribe(getEventtypeName(), getDestinationName(), false);
		
		tableView = new EventsTableView(driver);
		if(!tableView.subscriptionExists(getEventtypeName(), getDestinationName())){
			logWarn("Unsubscribed (canceled) - Subscription was not found!");
			logWarn("Test 2: FAIL");
			return false;
		}
		
		logInfo("Test 2: PASS");
		return true;
	}
	
	/**
	 * Unsubscribe - Yes
	 * @return
	 */
	private boolean test3(){
		EventsTableView tableView = new EventsTableView(driver);
		tableView.unsubscribe(getEventtypeName(), getDestinationName(), true);
		tableView.refresh();
		
		try{
			tableView = new EventsTableView(driver);
			if(tableView.subscriptionExists(getEventtypeName(), getDestinationName())){
				logWarn("Unsubscribed - Subscription was not found!");
				logWarn("Test 3: FAIL");
				return false;
			}
		}catch(TimeoutException e){
			// Expected if there are no other subscriptions
		}
		
		logInfo("Test 3: PASS");
		return true;
	}
	
	/**
	 * Verify endpoint in BPX_ENDPOINT_TYPE table
	 * @return
	 */
	private boolean test4(){
		EventsTab eventsTab = new EventsTab(driver);
		if(!eventsTab.databaseEventExistsByName(getEventtypeName())){
			logWarn("Event type is not found in the database after unsubscribe.");
			logWarn("Test 4: FAIL");
			return false;
		}
		logInfo("Event type confirmed in database after unsubscribe.");
		logInfo("Test 4: PASS");
		return true;
	}
	
	/**
	 * Verify filters on events tab - cannnot find the event
	 * @return
	 */
	private boolean test5(){
		EventsTab eventsTab = new EventsTab(driver);
		try{
			eventsTab.clickEventsSubTab(EventsSubTab.PATH);
			eventsTab = new EventsTab(driver);
			
			eventsTab.searchProducerFilter(getEventName());
			if(eventsTab.messageExists("Your search returned no results")){
				logWarn("Event is not found by filter.");
				logWarn("Test 5: FAIL");
				return false;
			}
		}catch(TimeoutException e){
			// Expected if no other subscriptions
		}
		
		logInfo("Confirmed event is still found via filter.");
		logInfo("Test 5: PASS");
		return true;
	}
	
	/**
	 * Verify deleting the endpoint deletes the event
	 * @return
	 */
	private boolean test6(){
		EventsTab eventsTab = new EventsTab(driver);
		eventsTab.clickTab(Tab.ENDPOINTS);
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		endpointsTab.toggleEndpoint(getEventName(), true);
		
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		endpointsTab.deleteEndpoint(getEventName(), true);
		
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		endpointsTab.clickTab(Tab.EVENTS);
		
		eventsTab = new EventsTab(driver);
		if(eventsTab.databaseEventExistsByName(getEventName())){
			logWarn("Event type is still in database after endpoint deletion.");
			logWarn("Test 6: FAIL");
			return false;
		}
		
		logInfo("Confirmed endpoint deletion includes event type.");
		logInfo("Test 6: PASS");
		return true;
	}

	
	private void cleanup(){
		logInfo(getTestName()+": Clean-up");
		driver.quit();
		
		APIdisableEndpoint(getDestinationKey());
		APIdeleteEndpoint(getDestinationKey());
	}
}
