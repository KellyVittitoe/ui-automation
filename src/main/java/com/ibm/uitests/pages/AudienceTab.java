package com.ibm.uitests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ibm.uitests.common.UITest;
import com.ibm.uitests.pages.HomePage.Tab;

public class AudienceTab extends HomePage{

	public AudienceTab(WebDriver driver) {
		super(driver);
		
		if(!isCurrentTab(Tab.AUDIENCES)){
			String message = "Did not find the audience tab.";
			logWarn(message);
			throw new IllegalStateException(message);
		}
	}

	public boolean containsAudienceModal(String audienceName, boolean isProducer){
		
		getElement("shareAnAudience").click();
		
		ShareAudienceModal shareModal = new ShareAudienceModal(driver);
		shareModal.refreshAudienceModal();
		
		if(isProducer){
			shareModal.searchProviderName(audienceName);
			try{
				waitFor(1500);
				getElement("clickAudienceProducer").click();
				//getElementByTitle(audienceName).click();
				shareModal.pressCancel();
				return true;
			}catch(TimeoutException e){
				logWarn("Cannot find provider audience.");
				// Expected if not found
			}
		}else{
			shareModal.searchConsumerName(audienceName);
			try{
				getElement("clickAudienceConsumer").click();
				//getElementByTitle(audienceName).click();
				shareModal.pressCancel();
				return true;
			}catch(TimeoutException e){
				logWarn("Cannot find consumer audience.");
				// Expected if not found
			}
		}
		
		shareModal.pressCancel();
		return false;
	}
	
	public void shareAnAudience(String producer, String consumer){
		boolean fail = true; 
		ShareAudienceModal shareModal = null;
		do{
			getElement("shareAnAudience").click();
			
			shareModal = new ShareAudienceModal(driver);
			shareModal.refreshAudienceModal();
			try{
				shareModal = new ShareAudienceModal(driver);
				waitFor(1000);
				shareModal.searchProviderName(producer);

				getElement("clickAudienceProducer").click();
				
				shareModal.searchConsumerName(consumer);
				getElement("clickAudienceConsumer").click();
				getElement("audienceModalNextButton").click();
				fail = false;
			}catch(TimeoutException e){
				fail = true;
				shareModal.pressCancel();
			}
		}while(fail);
		
		/*
		getElement("shareAnAudience").click();
		
		ShareAudienceModal shareModal = new ShareAudienceModal(driver);
		shareModal.refreshAudienceModal();
		
		shareModal = new ShareAudienceModal(driver);
		waitFor(1000);
		shareModal.searchProviderName(producer);

		getElement("clickAudienceProducer").click();
		//getElementByTitle(producer).click();
		
		shareModal.searchConsumerName(consumer);
		getElement("clickAudienceConsumer").click();
		//getElementByTitle(consumer).click();
		 
		 */
		
		// Subscription Details
		shareModal = new ShareAudienceModal(driver);
		if(shareModal.textExists("Source to destination mapping")){
			waitFor(250);
			getElement("audienceModalNextButton").click();
			
			shareModal = new ShareAudienceModal(driver);
			if(shareModal.textExists("Schedule")){
				getElement("audienceModalNextButton").click();
				
				shareModal = new ShareAudienceModal(driver);
				if(shareModal.textExists("Destination audience actions")){
					waitFor(500);
					getElements("audienceModalAddNewRecords").get(0).click();
					
					getElement("audienceModalShareButton").click();
					
				}else{
					logFatal("Could not find Audience Modal: 'Destination audience actions' page.");
				}
			}else{
				logFatal("Could not find Audience Modal: 'Schedule' page.");
			}
		}else{
			logFatal("Could not find Audience Modal: 'Source to destination mapping' page.");
		}
	}
	
	public boolean isAudienceShared(String producerEndpoint, String consumerEndpoint){
		AudienceTableView tableView = new AudienceTableView(driver);
		return tableView.audienceExists(producerEndpoint, consumerEndpoint);
	}
	
	public boolean refreshAudienceModal(){
		getElement("shareAnAudience").click();
		ShareAudienceModal shareModal = new ShareAudienceModal(driver);
		return shareModal.refreshAudienceModal();
	}
	
	public boolean isAudienceInMMResultAPI(String audienceConsumerName, String mmAuthKey){
		HttpRequest request = new HttpRequest("https://ubx-qa1-segment-MM.adm01.com/MMEndpoint/v1/segments",
												"Authorization",mmAuthKey);
		return request.getResponse().contains(audienceConsumerName);
	}
}

class ShareAudienceModal extends HomePage{
	public ShareAudienceModal(WebDriver driver){
		super(driver);
	}
	
	public boolean refreshAudienceModal(){
		getElement("refreshAudienceModal").click();
		String lastRefreshed = getElement("lastRefreshed").getText();
		
		for(int i=0; i<60; i++){
			waitFor(1000);
			if(!getElement("lastRefreshed").getText().equals(lastRefreshed)){
				logInfo("Audience Modal refresh successful.");
				return true;
			}
		}
		return false;
	}
	
	public void searchProviderName(String name){
		WebElement search = getElement("searchAudienceProducerName");
		search.clear();
		waitFor(250);
		search.sendKeys(name);
		
	}
	
	public void searchConsumerName(String name){
		WebElement search = getElement("searchAudienceConsumerName");
		search.clear();
		search.sendKeys(name);
	}
	
	public void pressCancel(){
		getElement("shareAudienceCancelButton").click();
	}
}
