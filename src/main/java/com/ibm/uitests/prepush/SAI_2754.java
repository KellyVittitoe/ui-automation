package com.ibm.uitests.prepush;

import java.io.IOException;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import com.ibm.uitests.common.UITest;
import com.ibm.uitests.pages.DashboardTab;
import com.ibm.uitests.pages.EndpointsTab;
import com.ibm.uitests.pages.LoginPage;
import com.ibm.uitests.pages.EndpointsTab.Endpoints;
import com.ibm.uitests.pages.EventsPathView;
import com.ibm.uitests.pages.EventsTab;
import com.ibm.uitests.pages.HomePage.Tab;

public class SAI_2754 extends UITest{
	
	private String updatedEventtypeName;

	public SAI_2754(WebDriver driver) {
		super(driver);
		
		setTestName(this.getClass().getName());
		
		if(setup()){
			test(test1("1", getEventtypeName()));
			test(test2("2", getEventtypeName(), getDestinationName(), getDescription(), true));
			test(test3());
			test(test4());
			cleanup();
		}else{
			logFatal(getTestName() + ": Setup failed.");
		}
	}

	public static void main(String[] args) {
		new SAI_2754(new FirefoxDriver());
	}
	
	public boolean setup(){
		logInfo(getTestName() + ": Setup - Create test event and destination endpoint.");
		
		gotoGUI();
		
		new LoginPage(driver, true);
		
		DashboardTab dashboardTab = new DashboardTab(driver);
		dashboardTab.clickTab(Tab.ENDPOINTS);
		
		waitFor(1500);
		
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		String eventKey = 
				endpointsTab.createNewEndpoint(getEventName(), getDescription(), Endpoints.EVENT);
		String destinationKey = 
				endpointsTab.createNewEndpoint(getDestinationName(), getDescription(), Endpoints.DESTINATION);
		
		setEventKey(eventKey);
		setDestinationKey(destinationKey);
		
		endpointsTab = new EndpointsTab(driver);
		endpointsTab.refresh();
		
		return destinationKey != null && 
				!destinationKey.equals("") &&
				eventKey != null &&
				!eventKey.equals("");
	}
	
	/**
	 * verify event type
	 * @return
	 */
	private boolean test1(String testNumber, String eventtypeName){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		if(!endpointsTab.databaseEventtypeExists(eventtypeName)){
			logWarn("Failed to verify eventtype in the database.");
			logWarn("Test "+testNumber+": Fail.");
			return false;
		}
		
		if(testNumber.equals("1")){
			logInfo("Test 1: Pass.");
		}
		
		return true;
	}
	
	/**
	 * Verify event in UI
	 * @return
	 */
	private boolean test2(String testNumber, String eventtypeName, String destinationName, String description, boolean subscribe){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		endpointsTab.refresh();
		endpointsTab.clickTab(Tab.EVENTS);
		EventsTab eventsTab = new EventsTab(driver);
		
		if(subscribe){
			
			try{
				eventsTab.subscribeToEvent(eventtypeName, destinationName, true);
			}catch(Exception e){
				logWarn("Unable to find eventtype in subscribe dialog.");
				logWarn("Test "+testNumber+": Fail.");
				return false;
			}
			
			eventsTab.refresh();
		}
		
		
		EventsPathView pathView = new EventsPathView(driver);
		pathView.getpublisherDetails(getEventName(), getDestinationName());
		
		
		
		if(!pathView.verifyProducer(getEventName(), description)){
			logWarn("Unable to confirm the event description in the UI.");
			logWarn("Test "+testNumber+": Fail.");
			return false;
		}
		
		if(testNumber.equals("2")){
			logInfo("Test 2: Pass");
		}
		
		return true;
		
	}
	
	/**
	 * Update eventtype with name and description
	 * @return
	 */
	private boolean test3(){
		EventsTab eventsTab = new EventsTab(driver);
		eventsTab.refresh();
		eventsTab.clickTab(Tab.ENDPOINTS);
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		endpointsTab.updateEventType(getEventtypeName()+"-Updated", getDescription()+"-Updated", getEventKey());
		
		if(test1("3", getEventtypeName()+"-Updated") && 
				test2("3", getEventtypeName()+"-Updated", getDestinationName(), getDescription(), false)){
			logInfo("Test 3: Pass.");
			return true;
		}
		
		logWarn("Test 3: Fail.");
		return false;
	}
	
	/**
	 * Update eventtype with name, description and code.
	 * @return
	 */
	private boolean test4(){
		EventsTab eventsTab = new EventsTab(driver);
		eventsTab.clickTab(Tab.ENDPOINTS);
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		
		int code = endpointsTab.updateEventType(getEventtypeName()+"-Updated", getDescription()+"-Updated", "new-code", getEventKey());
		
		if(code == 409){
			// Expected
			logInfo("Test 4: Pass.");
			return true;
		}
		
		logWarn("Server did not reject updating eventtype with new code.");
		logWarn("Test 4: Fail");
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	private void cleanup(){
		APIdisableEndpoint(getEventKey());
		APIdeleteEndpoint(getEventKey());
		APIdisableEndpoint(getDestinationKey());
		APIdeleteEndpoint(getDestinationKey());
		driver.quit();
	}
	
}
