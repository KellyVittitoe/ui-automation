package com.ibm.uitests.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EventsTableView extends HomePage{
	
	private List<EventTableRow> rows;

	public EventsTableView(WebDriver driver){
		super(driver);
		
		rows = new ArrayList<EventTableRow>();
		
		List<WebElement> eventtype = getElements("eventsTableEventType");
		List<WebElement> publisher = getElements("eventsTablePublisher");
		List<WebElement> destination = getElements("eventsTableDestination");
		List<WebElement> status = getElements("eventsTableStatus");
		
		for(int i=0; i<eventtype.size(); i++){
			EventTableRow row = new EventTableRow(driver, i);
			row.setEventtype(eventtype.get(i).getText());
			row.setPublisher(publisher.get(i).getText());
			row.setDestination(destination.get(i).getText());
			row.setStatus(status.get(i).getText());
			
			rows.add(row);
		}
	}
	
	public boolean unsubscribe(String eventtype, String destination, boolean commit){
		boolean found = false;
		for(EventTableRow row: rows){
			if(row.isSubscription(eventtype, destination)){
				row.unsubscribeEvent(commit);
				found = true;
				break;
			}
		}
		if(found){
			logInfo("unsubscribe from event - successful");
			return true;
		}else{
			logWarn("unsubscribe from event - failed (Eventtype/Destination not found!)");
			return false;
		}
	}
	
	public boolean subscriptionExists(String eventtype, String destination){
		for(EventTableRow row: rows){
			if(row.getEventtype().equals(eventtype) && row.getDestination().equals(destination)){
				logInfo("Subscription exists.");
				return true;
			}
		}
		logWarn("Subscription not found!");
		return false;
	}
}

class EventTableRow extends EventsTab{
	private String eventtype,
					publisher,
					destination;
	private boolean status;
	private int index;
	
	public EventTableRow(WebDriver driver, int index){
		super(driver);
		this.index = index;
	}
	
	protected boolean isSubscription(String eventtype, String destination){
		return this.eventtype.equals(eventtype) && this.destination.equals(destination);
	}
	
	protected String getEventtype() {
		return eventtype;
	}
	protected void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}
	protected String getPublisher() {
		return publisher;
	}
	protected void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	protected String getDestination() {
		return destination;
	}
	protected void setDestination(String destination) {
		this.destination = destination;
	}
	protected boolean isStatus() {
		return status;
	}
	protected void setStatus(String status){
		this.status = status.equals("Active");
	}
	protected int getIndex() {
		return index;
	}
	protected void setIndex(int index) {
		this.index = index;
	}
	
	private void click3DotMenu(){
		getElements("endpoint3DotMenu").get(index).click();
		logInfo("Click 3 Dot Menu");
	}
	
	private void confirmUnsubscribe(boolean confirm){
		if(confirm){
			logInfo("Confirm Unsubscribe Dialog: Press Confirm");
			getElement("confirmYes").click();
		}else{
			logInfo("Confirm Unsubscribe Dialog: Press Cancel");
			getElement("confirmCancel").click();
		}
	}
	
	/**
	 * 0: Event Details
	 * 1: Disable
	 * 2: Unsubscribe
	 * 
	 * @param index
	 */
private void click3DotMenuItem(int index){
		
		switch(index){
			case 0: getElement("3DotItemOne").click();
				logInfo("Click Event Details.");
				break;
			case 1: getElement("3DotItemTwo").click();
				logInfo("Toggle Enable/Disable");
				break;
			case 2: getElement("3DotItemThree").click();
				logInfo("Click unsubscribe.");
				break;
			default: logWarn("Unknown command.");
				break;
		}
	}
	
	public void toggleDisable(){
		logInfo("Toggle Disable/Enable");
		click3DotMenu();
		click3DotMenuItem(1);
		
	}
	
	public void unsubscribeEvent(boolean commit){
		logInfo("Unsubscribe from Event");
		click3DotMenu();
		click3DotMenuItem(2);
		confirmUnsubscribe(commit);
	}
}
