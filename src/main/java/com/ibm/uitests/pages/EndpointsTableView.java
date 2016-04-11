package com.ibm.uitests.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EndpointsTableView extends HomePage{
	
	private List<EndpointTableRow> rows;
	
	public EndpointsTableView(WebDriver driver){
		super(driver);
		waitFor(1000);
		rows = new ArrayList<EndpointTableRow>();
		List<String> names = new ArrayList<String>();
		List<String> providers = new ArrayList<String>();
		List<String> descriptions = new ArrayList<String>();
		List<String> statuses = new ArrayList<String>();
		
		for(WebElement e: getElements("endpointName")){
			names.add(e.getText());
			providers.add(e.getText());
		}
		
		/*for(WebElement e: getElements("endpointProvider")){
			providers.add(e.getText());
		}*/
		
		for(WebElement e: getElements("endpointDescription")){
			descriptions.add(e.getText());
		}
		
		for(WebElement e: getElements("endpointStatus")){
			statuses.add(e.getText());
		}
		
		EndpointTableRow tableRow = null;
		for(int i=0; i<descriptions.size(); i++){
			tableRow = null;
			tableRow = new EndpointTableRow(driver, i);
			tableRow.setName(names.get(i));
			tableRow.setProvider(providers.get(i));
			tableRow.setDescription(descriptions.get(i));
			tableRow.setStatus(statuses.get(i));
			rows.add(tableRow);
		}
		/*List<WebElement> endpointsRows = getElements("endpointTableRow");
		List<WebElement> names = getElements("endpointName");
		List<WebElement> providers = getElements("endpointProvider");
		List<WebElement> descriptions = getElements("endpointDescription");
		List<WebElement> statuses = getElements("endpointStatus");
		EndpointTableRow tableRow = null;
		
		for(int i=0; i<endpointsRows.size(); i++){
			tableRow = new EndpointTableRow(driver, i);
			tableRow.setName(names.get(i).getText());
			tableRow.setProvider(providers.get(i).getText());
			tableRow.setDescription(descriptions.get(i).getText());
			tableRow.setStatus(statuses.get(i).getText());
			
			rows.add(tableRow);
		}*/
	}
	
	public boolean endpointExists(String name){
		for(EndpointTableRow row: rows){
			if(row.isEndpointName(name)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isEndpointDisabled(String name){
		for(EndpointTableRow row: rows){
			if(row.isEndpointName(name)){
				if(!row.isActive()){
					return true;
				}
				break;
			}
		}
		return false;
	}

	public void toggleEndpointStatus(String name, boolean commit){
		logInfo("Toggle endpoint status");
		boolean disableEndpoint = true;
		
		for(EndpointTableRow r: rows){
			if(r.isEndpointName(name)){
				if(!r.isActive()) disableEndpoint = false;
				r.toggleDisable();
			}
		}
		
		if(disableEndpoint){
			if(commit){
				logInfo("Click: Yes");
				getElement("confirmYes").click();
			}else{
				logInfo("Click: cancel");
				getElement("confirmCancel").click();
			}
		}
		
		
		waitFor(500);
	}
	
	public boolean deleteEndpoint(String name, boolean commit){
		logInfo("Deleting Endpoint: "+name);
		boolean found = false;
		for(EndpointTableRow r: rows){
			if(r.isEndpointName(name)){
				r.deleteEndpoint(commit);
				found=true;
				break;
			}
		}
		
		if(!found){
			logWarn("Endpoint is not found on table with Endpoint Name: "+name);
			return false;
		}
		
		return true;
	}
}

class EndpointTableRow extends HomePage{
	
	private String name, description, provider;
	private boolean status;
	private int index;
	
	public EndpointTableRow(WebDriver driver, int index){
		super(driver);
		this.index = index;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setProvider(String provider){
		this.provider = provider;
	}
	
	public String getProvider(){
		return provider;
	}
	
	public void setStatus(String status){
		this.status = status.equals("Active");
	}
	
	public boolean isActive(){
		return status;
	}
	
	public boolean isEndpointName(String name){
		return this.name.equals(name);
	}
	
	private void click3DotMenu(){
		logInfo("Click 3 Dot Menu");
		getElements("endpoint3DotMenu").get(index).click();
	}
	
	/**
	 * 0: Endpoint Details
	 * 1: Disable endpoint
	 * 2: Delete endpoint
	 * 
	 * @param index
	 */
	private void click3DotMenuItem(int index){
		
		switch(index){
			case 0:
				logInfo("Click Endpoint Details");
				break;
			case 1:
				logInfo("Click Disable Endpoint");
				break;
			case 2:
				logInfo("Click Delete Endpoint");
				break;
			default:
				logWarn("Invalid selection");
				break;
		}
		waitFor(250);
		getElements("endpoint3DotMenuItems").get(index).click();
	}
	
	public void toggleDisable(){
		click3DotMenu();
		click3DotMenuItem(1);
	}
	
	public void deleteEndpoint(boolean commit){
		click3DotMenu();
		click3DotMenuItem(2);
		
		String disableMessage = "Please disable this endpoint before deleting.";
		String confirmMessage = "Are you sure that you want to delete this endpoint?";
		String unsubscribeMessage = "Please delete all subscriptions to/from this endpoint before deleting.";
		String audienceMessage = "";
		
		String message = getMessage();
		if(message.equals(disableMessage)){
			closeErrorMessage();
			throw new IllegalStateException(disableMessage);
		}
		
		if(message.equals(unsubscribeMessage)){
			closeErrorMessage();
			throw new IllegalStateException(unsubscribeMessage);
		}
		
		if(message.equals(audienceMessage)){
			closeErrorMessage();
			throw new IllegalStateException(audienceMessage);
		}
		
		if(message.equals(confirmMessage)){
			confirmMessage(commit);
		}
		
		
	}
	
	private void closeErrorMessage(){
		getElement("confirmYes").click();
	}
	
	private void confirmMessage(boolean commit){
		if(commit){
			getElement("confirmYes").click();
		}else{
			getElement("confirmCancel").click();
		}
	}
	
	private String getMessage(){
		return getElement("messageText").getText();
	}
	
}

