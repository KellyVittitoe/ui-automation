package com.ibm.uitests.prepush;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ibm.uitests.common.UITest;
import com.ibm.uitests.pages.AudienceTab;
import com.ibm.uitests.pages.DashboardTab;
import com.ibm.uitests.pages.EndpointsTab;
import com.ibm.uitests.pages.LoginPage;
import com.ibm.uitests.pages.EndpointsTab.Endpoints;
import com.ibm.uitests.pages.HomePage.Tab;

public class SAI_2760 extends UITest{

	public SAI_2760(WebDriver driver) {
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
			test(test7());
			test(test9());
			test(test11());
			
		}
		
		cleanup();
	}

	public static void main(String[] args) {
		new SAI_2760(new FirefoxDriver());

	}
	
	/**
	 * Create test provider endpoint with event, and test destination endpoint. 
	 * @return
	 */
	public boolean setup(){
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
	
	/**
	 * Create segement producer and add necessary endpoint attributes
	 * Check:
	 * Endpoint id in db
	 * Endpoint is seen on endpoint tab
	 * Endpoint is seen on audience modal after refresh
	 * @return
	 */
	public boolean test1(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		if(!endpointsTab.endpointExists(getAudienceProducerName())){
			logWarn("Endpoint cannnot be confirmed on the UI");
			logWarn("Test 1: FAIL");
			return false;
		}
		
		if(!endpointsTab.databaseEndpointExists(getAudienceProducerName())){
			logWarn("Endpoint cannnot be confirmed in the database");
			logWarn("Test 1: FAIL");
			return false;
		}
		
		endpointsTab.clickTab(Tab.AUDIENCES);
		AudienceTab audienceTab = new AudienceTab(driver);
		if(!audienceTab.containsAudienceModal(getConfig("SP_AUDIENCE_PRODUCER"), true)){
			logWarn("Cannot confirm endpoint on audience modal");
			logWarn("Test 1: FAIL");
			return false;
		}
		
		logInfo("Test 1: PASS.");
		return true;
	}
	
	/**
	 * Segment enabled consumer endpoint
	 * Check:
	 * Endpoint id in db
	 * Endpoint is seen on endpoint tab
	 * Endpoint is seen on audience modal after refresh
	 * @return
	 */
	private boolean test2(){
		AudienceTab audienceTab = new AudienceTab(driver);
		audienceTab.clickTab(Tab.ENDPOINTS);
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		if(!endpointsTab.endpointExists(getAudienceConsumerName())){
			logWarn("Endpoint cannnot be confirmed on the UI");
			logWarn("Test 2: FAIL");
			return false;
		}
		
		if(!endpointsTab.databaseEndpointExists(getAudienceConsumerName())){
			logWarn("Endpoint cannnot be confirmed in the database");
			logWarn("Test 2: FAIL");
			return false;
		}
		
		endpointsTab.clickTab(Tab.AUDIENCES);
		audienceTab = new AudienceTab(driver);
		if(!audienceTab.containsAudienceModal(getConfig("MM_AUDIENCE_CONSUMER"), false)){
			logWarn("Cannot confirm endpoint on audience modal");
			logWarn("Test 2: FAIL");
			return false;
		}
		
		logInfo("Test 2: PASS.");
		return true;
	}

	/**
	 * Disable producer, verify it is disabled.
	 * @return
	 */
	private boolean test3(){
		AudienceTab audienceTab = new AudienceTab(driver);
		audienceTab.clickTab(Tab.ENDPOINTS);
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		endpointsTab.toggleEndpoint(getAudienceProducerName(), true);
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		if(!endpointsTab.isEndpointDisabled(getAudienceProducerName())){
			logWarn("Endpoint is not disabled");
			logWarn("Test 3: FAIL");
			return false;
		}
		
		logInfo("Test 3: PASS");
		return true;
	}
	
	/**
	 * Disable producer, verify it is disabled.
	 * @return
	 */
	private boolean test4(){
		
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		endpointsTab.toggleEndpoint(getAudienceConsumerName(), true);
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		if(!endpointsTab.isEndpointDisabled(getAudienceConsumerName())){
			logWarn("Endpoint is not disabled");
			logWarn("Test 4: FAIL");
			return false;
		}
		
		logInfo("Test 4: PASS");
		return true;
	}
	
	/**
	 * Enable both producer adn consumer endpoints
	 * @return
	 */
	private boolean test5(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		endpointsTab.toggleEndpoint(getAudienceProducerName(), true);
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		endpointsTab.toggleEndpoint(getAudienceConsumerName(), true);
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		
		if(endpointsTab.isEndpointDisabled(getAudienceProducerName())){
			logWarn("Endpoint is still disabled");
			logWarn("Test 5: FAIL");
			return false;
		}
		
		if(endpointsTab.isEndpointDisabled(getAudienceConsumerName())){
			logWarn("Endpoint is still disabled");
			logWarn("Test 5: FAIL");
			return false;
		}

		logInfo("Test 5: PASS");
		return true;
	}
	
	/**
	 * Attempt to delete either endpoint
	 * @return
	 */
	private boolean test6(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		try{
			endpointsTab.deleteEndpoint(getAudienceProducerName(), true);
			logWarn("Did not get correct message when deleting enabled endpoint.");
			logWarn("Test 6: FAIL");
			return false;
		}catch(IllegalStateException e){
			// Expected
		}
		
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		
		try{
			endpointsTab.deleteEndpoint(getAudienceConsumerName(), true);
			logWarn("Did not get correct message when deleting enabled endpoint.");
			logWarn("Test 6: FAIL");
			return false;
		}catch(IllegalStateException e){
			// Expected
		}
		
		logInfo("Test 6: PASS");
		return true;
	}
	
	/**
	 * Disable either endpoint and then click to delete - cancel.
	 * @return
	 */
	private boolean test7(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		endpointsTab.toggleEndpoint(getAudienceProducerName(), true);
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		
		endpointsTab.deleteEndpoint(getAudienceProducerName(), false);
		
		if(!endpointsTab.endpointExists(getAudienceProducerName())){
			logWarn("Delete endpoint press cancel - Endpoint no longer exists.");
			logWarn("Test 7: FAIL");
			logWarn("Test 8: FAIL");
			return false;
		}
		
		logInfo("Test 7: PASS");
		logInfo("Test 8: PASS");
		return true;
	}
	
	/**
	 * Disable either endpoint and then click to delete - cancel.
	 * @return
	 */
	private boolean test9(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
				
		endpointsTab.deleteEndpoint(getAudienceProducerName(), true);
		
		if(endpointsTab.endpointExists(getAudienceProducerName())){
			logWarn("Delete endpoint press yes - Endpoint still exists.");
			logWarn("Test 9: FAIL");
			logWarn("Test 10: FAIL");
			return false;
		}
		
		logInfo("Test 9: PASS");
		logInfo("Test 10: PASS");
		return true;
	}
	
	/** 	
	 * Create a new endpoint and endpoint attribute to replace this deleted endpoint so that you still have a producer adn a consumer segment enabled endpoint
	 * 
	 * @return
	 */
	private boolean test11(){
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		setAudienceProducerKey(endpointsTab.createNewEndpoint(getAudienceProducerName(), getDescription(), Endpoints.AUDIENCE_SP_PRODUCER));
		endpointsTab.refresh();
		endpointsTab = new EndpointsTab(driver);
		if(!endpointsTab.endpointExists(getAudienceProducerName())){
			logWarn("Endpoint cannnot be confirmed on the UI");
			logWarn("Test 1: FAIL");
			return false;
		}
		
		if(!endpointsTab.databaseEndpointExists(getAudienceProducerName())){
			logWarn("Endpoint cannnot be confirmed in the database");
			logWarn("Test 1: FAIL");
			return false;
		}
		
		logInfo("Test 11: PASS");
		return true;
	}
	
	/*private boolean test12(){
		
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		endpointsTab.clickTab(Tab.AUDIENCES);
		AudienceTab audienceTab = new AudienceTab(driver);
		audienceTab.shareAnAudience(getConfig("SP_AUDIENCE_PRODUCER"), getConfig("MM_AUDIENCE_CONSUMER"));
		
		return false;
	}*/
	
	private void cleanup(){
		logInfo(getTestName()+": Clean-up");
		driver.quit();
		
		APIdisableEndpoint(getAudienceProducerKey());
		APIdeleteEndpoint(getAudienceProducerKey());
		APIdisableEndpoint(getAudienceConsumerKey());
		APIdeleteEndpoint(getAudienceConsumerKey());
	}
}
