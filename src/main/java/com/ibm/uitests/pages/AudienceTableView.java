package com.ibm.uitests.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AudienceTableView extends HomePage{
	
	private List<ShareAudience> rows;
	
	public AudienceTableView(WebDriver driver){
		super(driver);
		
		waitFor(1000);
		rows = new ArrayList<ShareAudience>();
		List<String> producerAudience = new ArrayList<String>();
		List<String> producerEndpoint = new ArrayList<String>();
		List<String> consumerAudience = new ArrayList<String>();
		List<String> consumerEndpoint = new ArrayList<String>();
		List<String> status = new ArrayList<String>();
		
		for(WebElement e: getElements("audienceProducerAudience")){
			producerAudience.add(e.getText());
		}
		
		for(WebElement e: getElements("audienceProducerEndpoint")){
			producerEndpoint.add(e.getText());
		}
		
		for(WebElement e: getElements("audienceConsumerAudience")){
			consumerAudience.add(e.getText());
		}
		
		for(WebElement e: getElements("audienceConsumerEndpoint")){
			consumerEndpoint.add(e.getText());
		}
		
		for(WebElement e: getElements("audienceStatus")){
			String text = e.getText().trim();
			if(!text.equals("")){
				status.add(e.getText());
			}
			
		}
		
		for(int i=0; i<producerAudience.size(); i++){
			ShareAudience audience = new ShareAudience();
			audience.setProducerAudienceName(producerAudience.get(i));
			audience.setProducerEndpointName(producerEndpoint.get(i));
			audience.setConsumerAudienceName(consumerAudience.get(i));
			audience.setConsumerEndpointName(consumerEndpoint.get(i));
			audience.setStatus(status.get(i));
			rows.add(audience);
		}
	
	}
	
	public boolean audienceExists(String producerEndpoint, String consumerEndpoint){
		for(ShareAudience a: rows){
			if(a.getProducerEndpointName().equals(producerEndpoint) && 
					a.getConsumerEndpointName().equals(consumerEndpoint)){
				
				return true;
			}
		}
		
		return false;
	}
	
	public String getAudienceStatus(String producerAudience, String consumerAudience){
		for(ShareAudience a: rows){
			if(a.getProducerAudienceName().equals(producerAudience) && a.getConsumerAudienceName().equals(consumerAudience)){
					return a.getStatus();
			}
		}
		
		return null;
	}
}

class ShareAudience{
	private String producerAudienceName, producerEndpointName, consumerAudienceName, consumerEndpointName, status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProducerAudienceName() {
		return producerAudienceName;
	}

	public void setProducerAudienceName(String producerAudienceName) {
		this.producerAudienceName = producerAudienceName;
	}

	public String getProducerEndpointName() {
		return producerEndpointName;
	}

	public void setProducerEndpointName(String producerEndpointName) {
		this.producerEndpointName = producerEndpointName;
	}

	public String getConsumerAudienceName() {
		return consumerAudienceName;
	}

	public void setConsumerAudienceName(String consumerAudienceName) {
		this.consumerAudienceName = consumerAudienceName;
	}

	public String getConsumerEndpointName() {
		return consumerEndpointName;
	}

	public void setConsumerEndpointName(String consumerEndpointName) {
		this.consumerEndpointName = consumerEndpointName;
	}
	
}