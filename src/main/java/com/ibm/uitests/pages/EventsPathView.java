package com.ibm.uitests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EventsPathView extends EventsTab{
	
	// Publisher Properties
	private String providerName, 
					providerStatus,
					providerDescription, 
					providerAuthenticationKey;

	public String getProviderStatus() {
		return providerStatus;
	}

	public String getDestinationStatus() {
		return destinationStatus;
	}

	// Subscriber Properties
	private String destinationName,
					destinationStatus,
					destinationDescription,
					destinationAuthenticationKey,
					destinationEndpointURL,
					destinationEventDestinationURL;
	
	// Event Properties
	private String eventName,
					eventCode,
					eventVersion,
					eventDescription,
					eventAttributes;

	public EventsPathView(WebDriver driver) {
		super(driver);
	}
	
	public void getpublisherDetails(String publisher, String destination){
		getDetails(publisher, destination, 0);
		waitFor(300);
		this.providerName = getElement("eventsPathDetailsName").getText();
		this.providerStatus = getElement("eventsPathDetailsStatus").getText();
		this.providerDescription = getElement("eventsPathProviderDescription").getText();
		this.providerAuthenticationKey = getElement("eventsPathProviderAuthKey").getText();
		getElement("eventsPathDialogClose").click();
	}
	
	public void getSubscriberDetails(String publisher, String destination){
		getDetails(publisher, destination, 1);
		this.destinationName = getElement("eventsPathDetailsName").getText();
		this.destinationStatus = getElement("eventsPathDetailsStatus").getText();
		this.destinationDescription = getElement("eventsPathProviderDescription").getText();
		this.destinationAuthenticationKey = getElement("eventsPathProviderAuthKey").getText();
		this.destinationEndpointURL = getElement("eventsPathDestinationEndpointURL").getText();
		this.destinationEventDestinationURL = getElement("eventsPathDestinationEventDestinationURL").getText();
	}

	public void getEvents(String publisher, String destination){
		getDetails(publisher, destination, 2);
	}
	
	private void getDetails(String publisher, String destination, int index){
		WebElement publisherSearch = getElement("eventsSearchPublisher");
		publisherSearch.clear();
		publisherSearch.sendKeys(publisher);
		
		WebElement destinationSearch = getElement("eventsSearchDestination");
		destinationSearch.clear();
		destinationSearch.sendKeys(destination);
		
		refresh();
		
		getElement("eventsPath3Dot").click();
		getElement("eventsPathDetails").click();
	}
	
	public String getProviderName() {
		return providerName;
	}

	public String getProviderDescription() {
		return providerDescription;
	}

	public String getProviderAuthenticationKey() {
		return providerAuthenticationKey;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public String getDestinationDescription() {
		return destinationDescription;
	}

	public String getDestinationAuthenticationKey() {
		return destinationAuthenticationKey;
	}

	public String getDestinationEndpointURL() {
		return destinationEndpointURL;
	}

	public String getDestinationEventDestinationURL() {
		return destinationEventDestinationURL;
	}

	public String getEventName() {
		return eventName;
	}

	public String getEventCode() {
		return eventCode;
	}

	public String getEventVersion() {
		return eventVersion;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public String getEventAttributes() {
		return eventAttributes;
	}
	
	public boolean verifyProducer(String eventtypeName, String description){
		return eventtypeName.equals(providerName) && description.equals(providerDescription);
	}
}
