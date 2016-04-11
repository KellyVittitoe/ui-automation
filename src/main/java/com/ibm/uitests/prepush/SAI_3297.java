package com.ibm.uitests.prepush;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ibm.uitests.common.DBConn;
import com.ibm.uitests.common.UITest;
import com.ibm.uitests.pages.AudienceTab;
import com.ibm.uitests.pages.DashboardTab;
import com.ibm.uitests.pages.EndpointsTab;
import com.ibm.uitests.pages.EventsTab;
import com.ibm.uitests.pages.HomePage.Tab;
import com.ibm.uitests.pages.LoginPage;
import com.ibm.uitests.pages.WelcomePage;
import com.ibm.uitests.pages.WelcomePage.LearnMore;

public class SAI_3297 extends UITest{

	public SAI_3297(WebDriver driver) {
		super(driver);
		setTestName(this.getClass().getName());
		logInfo(getTestName()+": Begin Test");

		if(setup()){
			test(test1());
		}
		cleanup();
		
	}

	public static void main(String[] args) {
		new SAI_3297(new FirefoxDriver());
	}

	/**
	 * Revert account to new account status
	 * @return
	 */
	private boolean setup(){
		
		logInfo(getTestName() + ": Setup - New User Navigation");		
		gotoGUI();
		
		String query = "UPDATE BPXDATA.BPX_USER SET USER_PREFERENCE=NULL WHERE USER_ID = '"+getConfig("username")+"'";
		DBConn conn = new DBConn();
		return conn.updateQuery(query);
	}
	
	private boolean test1(){

		new LoginPage(driver, true);
		WelcomePage welcomePage = new WelcomePage(driver, false);
		
		welcomePage.clickLearnAboutUBX();
		welcomePage = new WelcomePage(driver, false);
		
		
		// Verify Key Concepts Page and Links
		if(!welcomePage.isKeyConceptsPage()){
			logWarn("Did not find the Key Concepts page.");
			logWarn("Test 1: FAIL");
			return false;
		}
		if(welcomePage.verifyLearnMore(LearnMore.Endpoints, driver)){
			logWarn("Learn more link for: Endpoints - broken.");
			logWarn("Test 1: FAIL");
			return false;
		}
		if(welcomePage.verifyLearnMore(LearnMore.Events, driver)){
			logWarn("Learn more link for: Events - broken.");
			logWarn("Test 1: FAIL");
			return false;
		}
		if(welcomePage.verifyLearnMore(LearnMore.Audiences, driver)){
			logWarn("Learn more link for: Audience - broken.");
			logWarn("Test 1: FAIL");
			return false;
		}
		welcomePage.clickForwardArrow();
		
		// Verify Using UBX Page
		if(!welcomePage.isUsingUBXPage()){
			logWarn("Did not find the \"Using UBX\" page.");
			logWarn("Test 1: FAIL");
			return false;
		}
		welcomePage.clickForwardArrow();
		
		// Verify Ready to Try Page
		if(!welcomePage.isReadyToTryPage()){
			logWarn("Did not find the \"Ready to Try\" page.");
			logWarn("Test 1: FAIL");
			return false;
		}
		
		welcomePage.clickRegisterEndpoints();
		
		// Verify Endpoints Welcome Banner
		EndpointsTab endpointsTab = new EndpointsTab(driver);
		endpointsTab.clickRegisterCancelButton();
		if(!endpointsTab.hasWelcomeBanner()){
			logWarn("Did not find the Endpoints Welcome Banner.");
			logWarn("Test 1: FAIL");
			return false;
		}
		endpointsTab.clickTab(Tab.AUDIENCES);
		
		// Verify Audiences Welcome Banner
		AudienceTab audienceTab = new AudienceTab(driver);
		if(!audienceTab.hasWelcomeBanner()){
			logWarn("Did not find the Audiences Welcome Banner.");
			logWarn("Test 1: FAIL");
			return false;
		}
		audienceTab.clickTab(Tab.EVENTS);
		
		// Verify Events Welcome Banner
		EventsTab eventsTab = new EventsTab(driver);
		if(!eventsTab.hasWelcomeBanner()){
			logWarn("Did not find the Events Welcome Banner.");
			logWarn("Test 1: FAIL");
			return false;
		}
		
		// Log out
		eventsTab.logout();
		
		LoginPage loginPage = new LoginPage(driver, true);
		DashboardTab dashboardTab = new DashboardTab(driver);
		dashboardTab.clickTab(Tab.EVENTS);
		waitFor(500);
		
		// Close Events Welcome Banner
		eventsTab = new EventsTab(driver);
		eventsTab.closeWelcomeBanner();
		if(eventsTab.hasWelcomeBanner()){
			logWarn("Did not close the Events Welcome Banner.");
			logWarn("Test 1: FAIL");
			return false;
		}
		eventsTab.clickTab(Tab.AUDIENCES);
		
		// Close Audience Welcome Banner
		audienceTab = new AudienceTab(driver);
		audienceTab.closeWelcomeBanner();
		if(audienceTab.hasWelcomeBanner()){
			logWarn("Did not close the Endpoints Welcome Banner.");
			logWarn("Test 1: FAIL");
			return false;
		}
		endpointsTab.clickTab(Tab.ENDPOINTS);
		
		// Close Endpoints Welcome Banner
		endpointsTab = new EndpointsTab(driver);
		endpointsTab.closeWelcomeBanner();
		if(!endpointsTab.hasWelcomeBanner()){
			logWarn("Did not close the Endpoints Welcome Banner.");
			logWarn("Test 1: FAIL");
			return false;
		}
		
		endpointsTab.logout();
		
		loginPage = new LoginPage(driver, true);
		dashboardTab = new DashboardTab(driver);
		dashboardTab.clickTab(Tab.EVENTS);
		waitFor(500);
		
		// Verify closed Events Welcome Banner
		eventsTab = new EventsTab(driver);
		if(eventsTab.hasWelcomeBanner()){
			logWarn("Events Welcome Banner is still visible.");
			logWarn("Test 1: FAIL");
			return false;
		}
		eventsTab.clickTab(Tab.AUDIENCES);
				
		// Verify closed Audience Welcome Banner
		audienceTab = new AudienceTab(driver);
		if(audienceTab.hasWelcomeBanner()){
			logWarn("Endpoints Welcome Banner is still visible.");
			logWarn("Test 1: FAIL");
			return false;
		}
		endpointsTab.clickTab(Tab.ENDPOINTS);
				
		// Verify closed Endpoints Welcome Banner
		endpointsTab = new EndpointsTab(driver);
		if(!endpointsTab.hasWelcomeBanner()){
			logWarn("Endpoints Welcome Banner is still visible.");
			logWarn("Test 1: FAIL");
			return false;
			
		}
		
		logInfo("All tests: PASS");
		return true;
	}
	
	private void cleanup(){
		driver.quit();
	}
}
