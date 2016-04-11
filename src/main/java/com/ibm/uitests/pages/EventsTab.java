package com.ibm.uitests.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ibm.uitests.common.DBConn;

public class EventsTab extends HomePage{
	
	public enum EventsSubTab{
		PATH, TABLE
	}

	public EventsTab(WebDriver driver){
		super(driver);
		
		if(!isCurrentTab(Tab.EVENTS)){
			String message = "Did not find the events tab.";
			logWarn(message);
			throw new IllegalStateException(message);
		}
	}
	
	public void openSubscribeModal(){
		getElement("subscribeToEventsButton").click();
	}
	
	public void searchEventtype(String name){
		WebElement searchEvents = getElement("searchEvents");
		searchEvents.clear();
		searchEvents.sendKeys(name);
	}
	
	public boolean getEventtypeInfo(String eventtype, String description, String endpoint, String provider){
		getElement("subscribeInfo").click();
		String content = getElement("subscribePopup").getText();
		waitFor(200);
		return content.contains(eventtype) && content.contains(description) && content.contains(endpoint) && content.contains(provider);
	}
	
	public void closeEventtypeInfo(){
		getElement("subscribeInfoClose").click();
	}
	
	public boolean eventtypeExists(){
		try{
			getElement("subscribeEventtype");
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public void subscribeToEvent(String eventType, String destinationEndpoint, boolean commit){
		
		logInfo("Subscribe to event: "+eventType+" with destination: "+destinationEndpoint);
		openSubscribeModal();
		
		WebElement searchEvents = getElement("searchEvents");
		WebElement searchDestination = getElement("searchDestinations");
		searchEvents.clear();
		searchEvents.sendKeys(eventType);
		getElementByTitle(eventType).click();
		
		searchDestination.clear();
		searchDestination.sendKeys(destinationEndpoint);
		getElementByTitle(destinationEndpoint).click();
		
		List<WebElement> buttons = getElements("subscribeButtons");
		
		if(commit){
			buttons.get(0).click();
			logInfo("Click Yes");
		}else{
			buttons.get(1).click();
			logInfo("Click cancel");
		}
	}
	
	public void clickEventsSubTab(EventsSubTab tab){
		switch(tab){
		case PATH: getElement("eventsPathView").click();
			logInfo("Click Path View button");
			break;
		case TABLE: getElement("eventsTableView").click();
			logInfo("Click Table View button");
			break;
		}
	}
	
	public void searchProducerFilter(String name){
		WebElement filter = getElement("eventsSearchPublisher");
		filter.clear();
		filter.sendKeys(name);
	}
	
	public void searchDestinationFilter(String name){
		WebElement filter = getElement("eventsSearchDestination");
		filter.clear();
		filter.sendKeys(name);
	}
	
	public boolean databaseEventExistsByName(String name){
		String query = "SELECT EVENT_TYPE_ID FROM BPXDATA.BPX_EVENT_TYPE WHERE NAME='"+name+"'";
		return new DBConn().valueExists(query);
	}
	
	public boolean databaseEventExistsById(String id){
		String query = "SELECT EVENT_TYPE_ID FROM BPXDATA.BPX_EVENT_TYPE WHERE EVENT_TYPE_ID="+id;
		return new DBConn().valueExists(query);
	}
}
