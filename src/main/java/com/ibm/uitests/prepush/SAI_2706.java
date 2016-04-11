package com.ibm.uitests.prepush;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ibm.uitests.common.UITest;
import com.ibm.uitests.pages.LoginPage;
import com.ibm.uitests.pages.Page;

public class SAI_2706 extends UITest {
	
	private String winHandleBefore;

	public SAI_2706(WebDriver driver) {
		super(driver);
		setTestName(this.getClass().getName());
		
		logInfo(getTestName()+": Begin Test");
		
		setup();
		test(test1());
		test(test2());
		cleanup();
	}

	public static void main(String[] args) {
		new SAI_2706(new FirefoxDriver());

	}
	
	private void setup(){
		logInfo(getTestName() + ": Setup - Verify Help - Product Documentation Link.");
		
		gotoGUI();
	}
	
	private boolean test1(){
		LoginPage login = new LoginPage(driver, true);
		
		winHandleBefore = driver.getWindowHandle();
		login.clickProductDocumentation();
		
		for(String winHandle : driver.getWindowHandles()){
			if(!winHandle.equals(winHandleBefore)){
				driver.switchTo().window(winHandle);
				logInfo("Test 1: PASS");
				return true;
			}
		}
		
		logWarn("Product Documentation did not open in a new window.");
		logWarn("Test 1: FAIL");
		return false;
		
	}
	
	public boolean test2(){
		driver.get("https://www.ibm.com/support/knowledgecenter/SS9JVY/UBX/WhatsNew_ubx/WhatsNewUBX.dita?lang=en");
		IBMKnowledgeCenterPage knowledgeCenter = new IBMKnowledgeCenterPage(driver);
		if(knowledgeCenter.isCurrentRelease(getConfig("RELEASE"))){
			logInfo("Test 2: PASS");
			return true;
		}
		
		logWarn("Unable to confirm current documentation for release: "+getConfig("RELEASE"));
		logWarn("Test 2: FAIL");
		return false;
	}

	public void cleanup(){
		driver.close();
		driver.switchTo().window(winHandleBefore);
		driver.quit();
	}
}

class IBMKnowledgeCenterPage extends Page{
	
	private String release;

	public IBMKnowledgeCenterPage(WebDriver driver) {
		super(driver);
		
		driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
		release = driver.findElement(By.xpath(".//*[@class='shortdesc']")).getText();
	}

	public boolean isCurrentRelease(String release){
		return this.release.contains(release);
	}
}
