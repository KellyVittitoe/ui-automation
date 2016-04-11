package com.ibm.uitests.prepush;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ibm.uitests.common.DBConn;
import com.ibm.uitests.common.UITest;
import com.ibm.uitests.pages.AudienceTab;
import com.ibm.uitests.pages.DashboardTab;
import com.ibm.uitests.pages.LoginPage;
import com.ibm.uitests.pages.HomePage.Tab;

public class SAI_2715 extends UITest{

	public SAI_2715(WebDriver driver) {
		super(driver);
		
		if(setup()){
			test(test1());
			test(test2());
		}
		
		cleanup();
	}

	public static void main(String[] args) {
		new SAI_2715(new FirefoxDriver());
	}
	
	private boolean setup(){
		logInfo(getTestName() + ": Share Audience Refresh.");
		
		gotoGUI();
		
		new LoginPage(driver, true);
		
		DashboardTab dashboardTab = new DashboardTab(driver);
		waitFor(500);
		dashboardTab.clickTab(Tab.AUDIENCES);
		
		try{
			new AudienceTab(driver);
			return true;
		}catch(IllegalStateException e){
			return false;
		}
	}

	private boolean test1(){
		AudienceTab audienceTab = new AudienceTab(driver);
		if(audienceTab.refreshAudienceModal()){
			logInfo("Test 1: PASS");
			return true;
		}
		
		logWarn("Cannot confirm audience modal refresh.");
		logWarn("Test 1: FAIL");
		return false;
	}
	
	private boolean test2(){
		String query = "select * from ubxscheduler.ubx_jobs_status where JOB_CATEGORY = 'SEGMENT_POPULATE_DB'  and account_id = 1591 order by JOB_RUN_ID desc";
		DBConn conn = new DBConn();
		String value = conn.selectValue(query, "STATUS");
		
		if(!value.equals("COMPLETE")){
			logWarn("JOB status was not complete.");
			logWarn("Test 2: FAIL");
			return false;
		}
		
		logInfo("Test 2: PASS");
		return true;
	}
	
	private void cleanup(){
		driver.quit();
	}
}
