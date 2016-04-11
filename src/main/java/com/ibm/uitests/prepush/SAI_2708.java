package com.ibm.uitests.prepush;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ibm.uitests.common.UITest;
import com.ibm.uitests.pages.AudienceTab;
import com.ibm.uitests.pages.AudienceTableView;
import com.ibm.uitests.pages.DashboardTab;
import com.ibm.uitests.pages.EndpointsTab;
import com.ibm.uitests.pages.LoginPage;
import com.ibm.uitests.pages.EndpointsTab.Endpoints;
import com.ibm.uitests.pages.HomePage.Tab;

public class SAI_2708 extends UITest{

	public SAI_2708(WebDriver driver) {
		super(driver);
		setTestName(this.getClass().getName());
		logInfo(getTestName()+": Begin Test");
		
		if(setup()){
			test(test1());
			test(test2());
		}
		cleanup();
	}

	public static void main(String[] args) {
		new SAI_2708(new FirefoxDriver());
	}

	private boolean setup(){
		logInfo(getTestName() + ": Setup - Create test event and destination endpoint.");
			
		gotoGUI();
			
		new LoginPage(driver, true);
			
		DashboardTab dashboardTab = new DashboardTab(driver);
		waitFor(500);
		dashboardTab.clickTab(Tab.ENDPOINTS);
			
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		setAudienceConsumerKey(endpointsTab.createNewEndpoint(getAudienceConsumerName(), getDescription(), Endpoints.AUDIENCE_MM_CONSUMER));
		setAudienceProducerKey(endpointsTab.createNewEndpoint(getAudienceProducerName(), getDescription(), Endpoints.AUDIENCE_SP_PRODUCER));
		
		endpointsTab = new EndpointsTab(driver);
		endpointsTab.refresh();
		
		return getAudienceConsumerKey() != null && 
				!getAudienceConsumerKey().equals("") &&
				getAudienceProducerKey() != null &&
				!getAudienceProducerKey().equals("");
	}
	
	private boolean test1(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		endpointsTab.clickTab(Tab.AUDIENCES);
		
		AudienceTab audienceTab = new AudienceTab(driver);
		audienceTab.shareAnAudience(getConfig("SP_AUDIENCE_PRODUCER"), getConfig("MM_AUDIENCE_CONSUMER"));
		
		AudienceTableView tableView = new AudienceTableView(driver);
		String status = null;
		boolean running = true;
		do{
			tableView.refresh();
			tableView = new AudienceTableView(driver);
			status = tableView.getAudienceStatus(getConfig("SP_AUDIENCE_PRODUCER"), getConfig("MM_AUDIENCE_CONSUMER"));
			running = status.contains("Running");
			waitFor(5000);
		}while(running);
		
		if(status.contains("Failed")){
			logFatal("Audience creation failed.");
			logFatal("Test 1: FAILED");
			return false;
		}
		return status.contains("Complete");
	}
	
	private boolean test2(){
		AudienceTab audienceTab = new AudienceTab(driver);
		if(!audienceTab.isAudienceInMMResultAPI(getConfig("MM_AUDIENCE_CONSUMER"), getAudienceConsumerKey())){
			logFatal("Cannot confirm the Media Math Audience in from the Media Math API.");
			logFatal("Test 2: FAILED");
			return false;
		}
		
		return true;
	}
	
	private void cleanup(){
		logInfo(getTestName()+": Clean-up");
		driver.quit();
		
		APIdisableEndpoint(getAudienceProducerKey());
		APIdeleteEndpoint(getAudienceProducerKey());
		APIdisableEndpoint(getAudienceConsumerKey());
		APIdeleteEndpoint(getAudienceConsumerKey());
	}
}
