package com.ibm.uitests.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.WebDriver;

import com.ibm.uitests.pages.HttpRequest;

public abstract class UITest extends BaseTest {
	
	private String eventName, 
					eventtypeName,
					destinationName,
					audienceProducerName,
					audienceConsumerName,
					eventKey,
					destinationKey,
					audienceProducerKey,
					audienceConsumerKey,
					audienceKey,
					description;
	
	
	

	public String getAudienceProducerKey() {
		return audienceProducerKey;
	}

	public void setAudienceProducerKey(String audienceProducerKey) {
		this.audienceProducerKey = audienceProducerKey;
	}

	public String getAudienceConsumerKey() {
		return audienceConsumerKey;
	}

	public void setAudienceConsumerKey(String audienceConsumerKey) {
		this.audienceConsumerKey = audienceConsumerKey;
	}

	public List<Boolean> getTestResult() {
		return testResult;
	}

	public void setTestResult(List<Boolean> testResult) {
		this.testResult = testResult;
	}

	private String testName;
	private List<Boolean> testResult;
	
	public WebDriver driver;
	
	public UITest(WebDriver driver){
		this.driver = driver;
		this.testResult = new ArrayList<Boolean>();
		
		this.eventName = getConfig("EVENTS")+generateSeed();
		this.eventtypeName = getConfig("EVENTTYPE")+generateSeed();
		this.destinationName = getConfig("DESTINATION")+generateSeed();
		this.audienceProducerName = getConfig("AUDIENCE")+"-Producer"+generateSeed();
		this.audienceConsumerName = getConfig("AUDIENCE")+"-Consumer"+generateSeed();
		this.description = getConfig("DESCRIPTION");
	}
	
	/*public String getBasicAuth(String user, String password){
		String authString = user + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		return authStringEnc;
	}*/
	
	public void wait(int milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setTestName(String name){
		this.testName = name.replaceAll("_", " ");
	}
	
	public String getTestName(){
		return this.testName;
	}
	
	public void test(boolean result){
		this.testResult.add(result);
	}
	
	public boolean testResult(){
		for(boolean b: testResult){
			if(!b){
				return b;
			}
		}
		return true;
	}
	
	public void gotoGUI(){
		driver.manage().window().maximize();
		driver.get(getConfig("LogInURLQA1"));
	}
	
	public void gotoPage(String url){
		driver.manage().window().maximize();
		driver.get(url);
	}
	
	public String APIdisableEndpoint(String authenticationKey){
		HttpRequest http = new HttpRequest(getConfig("API")+"v1/endpoint/disable", "PUT", "", "Authorization", "Bearer "+authenticationKey);
		return http.getResponse();
	}
	
	public String APIdeleteEndpoint(String authenticationKey){
		HttpRequest http = new HttpRequest(getConfig("API")+"v1/endpoint", "DELETE", "", "Authorization", "Bearer "+authenticationKey);
		return http.getResponse();
	}
	
	public int generateSeed() {
	    Random r = new Random( System.currentTimeMillis() );
	    return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
	}
	
	public String getEventName() {
		return eventName;
	}

	public String getEventtypeName() {
		return eventtypeName;
	}

	public String getDestinationName() {
		return destinationName;
	}
	
	public String getAudienceProducerName(){
		return audienceProducerName;
	}
	
	public String getAudienceConsumerName(){
		return audienceConsumerName;
	}

	public String getEventKey() {
		return eventKey;
	}

	public String getDestinationKey() {
		return destinationKey;
	}

	public String getDescription() {
		return description;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	
	public void setAudienceKey(String audienceKey){
		this.audienceKey = audienceKey;
	}
	
	public String getAudienceKey(){
		return audienceKey;
	}

	public void setDestinationKey(String destinationKey) {
		this.destinationKey = destinationKey;
	}
	
	public boolean getStatus(){
		for(Boolean b: testResult){
			if(b == false){
				return false;
			}
		}
		return true;
	}
}