package com.ibm.uitests.prepush;

import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ibm.uitests.common.DBConn;
import com.ibm.uitests.common.UITest;


public class SAI_2733 extends UITest {
	
	public static void main(String args[]){
		new SAI_2733(new FirefoxDriver());
	}

	
	private int account_id;
	//private String name = "Automation-DELETEME";
	private String auth;
	
	
	private final String USER_ID = "Automation-DELETEME";
	
	public SAI_2733(WebDriver driver) {
		
		super(driver);
		setTestName(this.getClass().getName());
		logInfo(getTestName()+": Begin Test");
		//auth = getBasicAuth(systemUser, systemPassword);
		
		//String message = "Starting testing for "+testId;
		
		//setup();

		test(test1());
		test(test2());
		
		//logTestResult(getStatus(), testId+ " Test Result:");
		
		cleanup();
	}
	
	private boolean test1(){
		String content = "{"+
    "\"user_id\":\""+USER_ID+"\","+
    "\"account_id\":"+account_id+","+
    "\"last_name\":\"Automation\","+
    "\"first_name\":\"Test\","+
    //"\"email\":\""+email1+"\","+
   // "\"password\":\""+systemPassword+"\""+
    "}";
		//String result = post(META+"v1/user", "POST", content, "Authorization", "Basic "+auth);
		
		return false;//result.equals("true");
	}
	
	private boolean test2(){
		String result = "";// get(META+"v1/user/"+USER_ID, "Authorization", "Basic "+auth);
		return result.contains("\"user_id\":\""+USER_ID);
	}
	
	private void setup(String systemPassword, String systemUser) throws IOException{
		// Get UBX url and login account info
		systemUser = getConfig("su");
		systemPassword = getConfig("sup");

		//auth = getBasicAuth(systemUser, systemPassword);
		
		DBConn conn = new DBConn();
		//account_id = conn.getAccountId(USER_ID);
		//System.out.println(account_id);
	}
	
	public void cleanup(){
		
		//post(META+"v1/user/"+USER_ID , "DELETE", "", "Authorization", "Basic "+auth);
		//post(META+"v1/account/"+account_id , "DELETE", "", "Authorization", "Basic "+auth);
	}
}

