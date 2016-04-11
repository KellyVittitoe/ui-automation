package com.ibm.uitests.pages;

import org.openqa.selenium.WebDriver;

import com.ibm.uitests.pages.HomePage.Tab;

public class DashboardTab extends HomePage{

	public DashboardTab(WebDriver driver) {
		super(driver);
		
		if(!isCurrentTab(Tab.DASHBOARD)){
			String message = "";
			logWarn(message);
			throw new IllegalStateException(message);
		}
	}
	
	
}
