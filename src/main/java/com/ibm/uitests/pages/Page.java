package com.ibm.uitests.pages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ibm.uitests.common.BaseTest;

public abstract class Page extends BaseTest {
	
	private Map<String, String> identifiers;
	
	private String EVENT_AUTHENTICATION_KEY;
	private String AUDIENCE_AUTHENTICATION_KEY;
	private String DESTINATION_AUTHENTICATION_KEY;
	private String AUDIENCE_PRODUCER_AUTHENTICATION_KEY;
	private String AUDIENCE_CONSUMER_AUTHENTICATION_KEY;
	
	public WebDriver driver;
	private final String USER_AGENT = "Mozilla/5.0";
	
	public final int DRIVER_WAIT = 15;
	
	public Page(WebDriver driver){
		this.driver = driver;
		identifiers = getProperties("conf/identifiers.properties");
	}
	
	/**
	 * Results from file: identifiers.properties
	 * @param key
	 * @return
	 */
	protected String getIdentifiers(String key){
		return identifiers.get(key);
	}
	
	public WebElement getElement(String identifier){
		waitForOverlay();
		WebDriverWait wait = new WebDriverWait(driver, DRIVER_WAIT);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(getIdentifiers(identifier))));
		
		return driver.findElement(By.xpath(getIdentifiers(identifier)));
	}
	
	private void waitForOverlay(){
		boolean visible = true;
		do{
			try {
		        WebElement e = driver.findElement(By.xpath(".//*[@ng-show='global.requestInProgress']"));
		        visible = e.isDisplayed();
		        
		    } catch (org.openqa.selenium.NoSuchElementException e) {
		        visible = false;
		    }
			try {
		        WebElement e = driver.findElement(By.xpath(".//*[@ng-show='requestInProgress || isLoading > 0']"));
		        visible = visible || e.isDisplayed();
		    } catch (org.openqa.selenium.NoSuchElementException e) {
		        
		    }
			waitFor(300);
		}while(visible);
	}
	
	public WebElement getElement(String identifier, String message){
		try{
			return getElement(identifier);
		}catch(TimeoutException e){
			e.printStackTrace();
			throw new IllegalStateException(message);
		}
	}
	
	public List<WebElement> getElements(String identifier){
		List<WebElement> elements = null;
		try{
			elements = (new WebDriverWait(driver, DRIVER_WAIT))
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(getIdentifiers(identifier))));
		}catch(NoSuchElementException e){
			e.printStackTrace();
			logFatal(e.getMessage());
		}
		return elements;	  
	}
	
	public List<WebElement> getElements(String identifier, String message){
		try{
			return getElements(identifier);
		}catch(TimeoutException e){
			
			e.printStackTrace();
			throw new IllegalStateException(message);
		}
	}
	
	public WebElement getElementByTitle(String title){
		WebElement element = null;
		String path = ".//*[@title='"+title+"']";
		try{
			WebDriverWait wait = new WebDriverWait(driver, DRIVER_WAIT);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(path)));
			element =  driver.findElement(By.xpath(path));
			return element;
		}catch(NoSuchElementException e){
			e.printStackTrace();
			logFatal(e.getMessage());
		}catch(WebDriverException x){
			for(int i=0; i<10; i++){
				waitFor(100);
				try{
					element =  driver.findElement(By.xpath(path));
				}catch(WebDriverException ex){}
			}
		}
		
		return driver.findElement(By.xpath(path));
	}
	
	// HTTP GET request
	public String get(String url, String authProp, String authValue){
		String result = null;
		URL obj;
		try {
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestMethod("GET");

			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty(authProp, authValue);

			int responseCode = con.getResponseCode();
			
			String message = "GET"+" "+formatURL(url)+" :: " + responseCode;
			System.out.println(message);
			logInfo(message);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			result = response.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private String formatURL(String url){
		String[] split = url.split("/v1/");
		return "v1/"+split[1];
	}
	
	
	
	public String getAudienceProducerKey() {
		return AUDIENCE_PRODUCER_AUTHENTICATION_KEY;
	}

	public void setAudienceProducerKey(String AUDIENCE_PRODUCER_AUTHENTICATION_KEY) {
		this.AUDIENCE_PRODUCER_AUTHENTICATION_KEY = AUDIENCE_PRODUCER_AUTHENTICATION_KEY;
	}

	public String getAudienceConsumerKey() {
		return AUDIENCE_CONSUMER_AUTHENTICATION_KEY;
	}

	public void setAudienceConsumerKey(String AUDIENCE_CONSUMER_AUTHENTICATION_KEY) {
		this.AUDIENCE_CONSUMER_AUTHENTICATION_KEY = AUDIENCE_CONSUMER_AUTHENTICATION_KEY;
	}

	public String getEventKey() {
		return EVENT_AUTHENTICATION_KEY;
	}

	public void setEventKey(String EVENT_AUTHENTICATION_KEY) {
		this.EVENT_AUTHENTICATION_KEY = EVENT_AUTHENTICATION_KEY;
	}

	public String getAudienceKey() {
		return AUDIENCE_AUTHENTICATION_KEY;
	}

	public void setAudienceKey(String AUDIENCE_AUTHENTICATION_KEY) {
		this.AUDIENCE_AUTHENTICATION_KEY = AUDIENCE_AUTHENTICATION_KEY;
	}

	public String getDestinationKey() {
		return DESTINATION_AUTHENTICATION_KEY;
	}

	public void setDestinationKey(String DESTINATION_AUTHENTICATION_KEY) {
		this.DESTINATION_AUTHENTICATION_KEY = DESTINATION_AUTHENTICATION_KEY;
	}
	
	public static void browserShake(WebDriver driver) {
		Dimension size = driver.manage().window().getSize();
		int width = size.getWidth();
		int height = size.getHeight();
        driver.manage().window().setSize(new Dimension(width+50, height));
        driver.manage().window().maximize();
    }
	
	public boolean textExists(String text){
		List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'" + text + "')]"));
		return list.size() > 0;
	}
}
