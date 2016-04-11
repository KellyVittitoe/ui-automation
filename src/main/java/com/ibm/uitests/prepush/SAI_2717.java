package com.ibm.uitests.prepush;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import com.ibm.uitests.common.UITest;
import com.ibm.uitests.pages.DashboardTab;
import com.ibm.uitests.pages.EndpointsTab;
import com.ibm.uitests.pages.EventsTab;
import com.ibm.uitests.pages.EventsTab.EventsSubTab;
import com.ibm.uitests.pages.EventsTableView;
import com.ibm.uitests.pages.HomePage.Tab;
import com.ibm.uitests.pages.LoginPage;
import com.ibm.uitests.pages.EndpointsTab.Endpoints;
import com.ibm.uitests.pages.EndpointsTableView;

public class SAI_2717 extends UITest{
	
	private String eventKey, destinationKey;

	public static void main(String[] args) {
		new SAI_2717(new FirefoxDriver());
	}

	public SAI_2717(WebDriver driver){
		super(driver);
		setTestName(this.getClass().getName());
		
		logInfo(getTestName()+": Begin Test");
		
		if(setup()){
			
			test(test1());
			test(test2());
			
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
	 * Create test destination for subscription
	 * @return
	 */
	public boolean setup(){
		logInfo(getTestName() + ": Setup - Create test event and destination endpoint.");
		
		gotoGUI();
		
		new LoginPage(driver, true);
		
		DashboardTab dashboardTab = new DashboardTab(driver);
		dashboardTab.clickTab(Tab.ENDPOINTS);
		
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		destinationKey = 
				endpointsTab.createNewEndpoint(getDestinationName(), getDescription(), Endpoints.DESTINATION);
		eventKey = 
				endpointsTab.createNewEndpoint(getEventName(), getDescription(), Endpoints.EVENT);
		
		
		endpointsTab = new EndpointsTab(driver);
		endpointsTab.refresh();
		
		return destinationKey != null && 
				!destinationKey.equals("") &&
				eventKey != null &&
				!eventKey.equals("");
	}
	
	/**
	 * DELETE Endpoint w/out Subscription
	 * @return
	 */
	private boolean test1(){
		
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		
		try{
			endpointsTab.deleteEndpoint(getEventName(), true);
			logFatal("Did not get the proper message when deleting an enabled endpoint.");
		}catch(IllegalStateException e){
			// Expected
		}
		
		endpointsTab.refresh();
		waitFor(250);
		
		if(!endpointsTab.endpointExists(getEventName())){
			logWarn("Endpoint was deleted, should of got Error Message");
			return false;
		}
		
		endpointsTab.refresh();
		
		endpointsTab = new EndpointsTab(driver);
		endpointsTab.toggleEndpoint(getEventName(), true);
		
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		
		if(!endpointsTab.isEndpointDisabled(getEventName())){
			logWarn("The endpoint was not disabled.");
		}
		
		endpointsTab = new EndpointsTab(driver);
		endpointsTab.deleteEndpoint(getEventName(), false);
		
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		
		if(!endpointsTab.endpointExists(getEventName())){
			logWarn("Delete cancelled, yet endpoint does not exist");
		}
		
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		
		endpointsTab.deleteEndpoint(getEventName(), true);
		
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		
		if(endpointsTab.endpointExists(getEventName())){
			logWarn("Endpoint still exists after delete process");
			return false;
		}else{
			logInfo("Endpoint deleted.");
			
			logInfo("Test 1: PASS");
			return true;
		}
		
		
	}
	
	private boolean test2(){
		
		waitFor(500);
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		
		endpointsTab.createNewEndpoint(getEventName(), getDescription(), Endpoints.EVENT);
		
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		endpointsTab.clickTab(Tab.EVENTS);
		
		EventsTab eventsTab = new EventsTab(driver);
		eventsTab.subscribeToEvent(getEventtypeName(), getDestinationName(), true);
		
		eventsTab.refresh();
		eventsTab = new EventsTab(driver);
		eventsTab.clickTab(Tab.ENDPOINTS);
		waitFor(250);
		endpointsTab = new EndpointsTab(driver);
		
		try{
			endpointsTab.deleteEndpoint(getEventName(), true);
			logWarn("Endpoint with event subscription, endpoint enabled, event was deleted.");
			return false;
		}catch(IllegalStateException e){
			// Expected
		}
		
		endpointsTab.toggleEndpoint(getEventName(), true);
		
		try{
			endpointsTab.deleteEndpoint(getEventName(), true);
			logWarn("Endpoint with event subscription, endpoint disabled, event was deleted.");
			return false;
		}catch(IllegalStateException e){
			// Expected
		}
		
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		
		//Enable the endpoint
		endpointsTab.toggleEndpoint(getEventName(), true);
		
		endpointsTab.clickTab(Tab.EVENTS);
		
		eventsTab = new EventsTab(driver);
		eventsTab.clickEventsSubTab(EventsSubTab.TABLE);
		
		EventsTableView tableView = new EventsTableView(driver);
		tableView.unsubscribe(getEventtypeName(), getDestinationName(), true);
		
		eventsTab = new EventsTab(driver);
		eventsTab.clickTab(Tab.ENDPOINTS);
		
		endpointsTab = new EndpointsTab(driver);
		try{
			endpointsTab.deleteEndpoint(getEventName(), true);
			logWarn("The endpoint was not disabled.");
			return false;
		}catch(IllegalStateException e){
			// Expected
		}
		
		endpointsTab.toggleEndpoint(getEventName(), true);
		
		endpointsTab.refresh();
		
		endpointsTab.deleteEndpoint(getEventName(), false);
		
		if(!endpointsTab.endpointExists(getEventName())){
			logWarn("Delete endpoint, click cancel, endpoint does not exist.");
			return false;
		}
		
		endpointsTab.deleteEndpoint(getEventName(), true);
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		
		if(endpointsTab.endpointExists(getEventName())){
			logWarn("Delete endpoint, click Yes, endpoint still exists.");
			return false;
		}
		
		EndpointsTableView view = new EndpointsTableView(driver);
		if(endpointsTab.databaseEndpointExists(getEventName())){
			logWarn("Deleted endpoint is still in the database.");
			return false;
		}
		
		logInfo("Test 2: PASS");
		return true;
	}
	
	private void cleanup(){
		logInfo(getTestName()+": Clean-up");
		driver.quit();
		
		APIdisableEndpoint(destinationKey);
		APIdeleteEndpoint(destinationKey);
	}
}
