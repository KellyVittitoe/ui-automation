package com.ibm.uitests.prepush;

import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ibm.uitests.common.UITest;

public class SAI_2732 extends UITest{
	
	private static Properties conf;
	private String auth;
	private String name = "Automation-DELETEME";
	private String account_id = "2521";
	
	private String testId = "SAI_2732";

	public static void main(String args[]){
		
		SAI_2732 test = new SAI_2732(new FirefoxDriver());
		
	}
	
	
	//Logger LOGGER;
	
	public SAI_2732( WebDriver driver){
		super(driver);
		setTestName(this.getClass().getName());
		logInfo(getTestName()+": Begin Test");
		//auth = getBasicAuth(systemUser, systemPassword);
		
		String message = "Starting testing for "+testId;
		System.out.println(message);
		logInfo(message);
		
		test(test1());
		
		test(test2());
		
		//cleanup(Integer.parseInt(account_id));
	}
	
	private boolean test1(){
		
		String content = "{\"name\":\"" + name +"\"}";
		//account_id = post(META+"v1/account", "POST", content, "Authorization", "Basic "+auth);
		
		return account_id != null;
	}
	
	private boolean test2(){
		if(account_id != null){
			//String result = get(META+"v1/account/"+account_id, "Authorization", "Basic "+auth);
			//return result.contains("\"account_id\":"+account_id);
			return false;
		}else{
			return false;
		}
	}
	
	public void cleanup(int id){
		//post(META+"v1/account/"+id , "DELETE", "", "Authorization", "Basic "+auth);
	}
}

