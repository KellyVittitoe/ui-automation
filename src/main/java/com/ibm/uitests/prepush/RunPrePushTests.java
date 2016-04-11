package com.ibm.uitests.prepush;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ibm.uitests.common.BaseTest;
import com.ibm.uitests.common.UITest;

public class RunPrePushTests extends BaseTest{
	
	private List<UITest> tests;
	private static List<String> doNotTest;

	public static void main(String[] args) {
		doNotTest = new ArrayList<String>();
		for(String t: args){
			doNotTest.add(t);
		}
		try{
			new RunPrePushTests();
		}catch (IOException e){
			e.printStackTrace();
		}
		
	}
	
	public RunPrePushTests() throws IOException{
		setup();
		test();
		printReport();
		cleanup();
	}
	
	private void printReport(){
		
		BufferedWriter writer = null;
        try {
            //create a temporary file
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            File logFile = new File("reports/PrePush-" +timeLog+".txt");

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            
            writer.write("Test start: "+timeLog+"");
            writer.write(System.getProperty("line.separator"));
            writer.write("Test name - Result");
            writer.write(System.getProperty("line.separator"));
            
            boolean result = true;
            
            for(UITest ui: tests){
            	String testResult = null;
            	if(ui.getStatus()){
            		testResult = "PASS";
            	}else{
            		testResult = "FAIL";
            		result = false;
            	}
    			writer.write(ui.getTestName() + "  - " + testResult);
    			writer.write(System.getProperty("line.separator"));
    		}
            
            if(result){
            	writer.write("All tests PASS.");
            }else{
            	writer.write("Pack contains FAILED tests.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }	
	}
	
	/**
	 * Setup Destination Endpoint for test usage.
	 */
	private void setup(){	
		
		String message = "Starting Prepush Tests Setup";
		logInfo(message);
		
		tests = new ArrayList<UITest>();
	
	}
	
	private void cleanup(){
		
	}
	
	/**
	 * Add tests to be tested by the program
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private void test() {

		WebDriver driver = new FirefoxDriver();
		
		try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("2706")){
			try {tests.add(new SAI_2706(driver));} 
			catch(Exception e){
				e.printStackTrace();
				logFatal(e.getMessage());
				driver.quit();
			}
		}
		
		try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("2708")){
			try {tests.add(new SAI_2708(driver));} 
			//catch (IOException | InterruptedException e) {e.printStackTrace();}
			catch(Exception e){
				e.printStackTrace();
				logFatal(e.getMessage());
				driver.quit();
			}
		}
		
		try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("2709")){
			try {tests.add(new SAI_2709(driver));} 
			//catch (IOException | InterruptedException e) {e.printStackTrace();}
			catch(Exception e){
				e.printStackTrace();
				logFatal(e.getMessage());
				driver.quit();
			}
		}
		
		try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("2710")){
			try {tests.add(new SAI_2710(driver));} 
			//catch (IOException | InterruptedException e) {e.printStackTrace();}
			catch(Exception e){
				e.printStackTrace();
				logFatal(e.getMessage());
				driver.quit();
			}
		}
		
		try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("2714")){
			try {tests.add(new SAI_2714(driver));} 
			//catch (IOException | InterruptedException e) {e.printStackTrace();}
			catch(Exception e){
				e.printStackTrace();
				logFatal(e.getMessage());
				driver.quit();
			}
		}
		
		try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("2715")){
			try {tests.add(new SAI_2715(driver));} 
			//catch (IOException | InterruptedException e) {e.printStackTrace();}
			catch(Exception e){
				e.printStackTrace();
				logFatal(e.getMessage());
				driver.quit();
			}
		}
		
		try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("2717")){
			try {tests.add(new SAI_2717(driver));} 
			//catch (IOException | InterruptedException e) {e.printStackTrace();}
			catch(Exception e){
				logFatal(e.getMessage());
				driver.quit();
			}
		}
		
		try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("2722")){
			try {tests.add(new SAI_2722(driver));} 
			//catch (IOException | InterruptedException e) {e.printStackTrace();}
			catch(Exception e){
				e.printStackTrace();
				logFatal(e.getMessage());
				
				driver.quit();
			}
		}
		
		/*
		 try {Thread.sleep(500);} catch (InterruptedException e1) {}
		 driver = new FirefoxDriver();
		if(!doNotTest.contains("2732")){
			try {tests.add(new SAI_2732());} 
			catch (IOException e) {e.printStackTrace();}
			catch(Exception e){
				e.printStackTrace();
				log.fatal(e.getMessage());
				driver.quit();
			}
		}
		
		try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("SAI_2733")){
			try {tests.add(new SAI_2733());}
			catch (IOException e) {e.printStackTrace();}
			catch(Exception e){
				e.printStackTrace();
				log.fatal(e.getMessage());
				driver.quit();
			}
		}*/
		
		try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("2753")){
			try {tests.add(new SAI_2753(driver));} 
			//catch (IOException | InterruptedException e) {e.printStackTrace();}
			catch(Exception e){
				e.printStackTrace();
				logFatal(e.getMessage());
				driver.quit();
			}
		}
		
		try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("2754")){
			try {tests.add(new SAI_2754(driver));} 
			//catch (IOException | InterruptedException e) {e.printStackTrace();}
			catch(Exception e){
				e.printStackTrace();
				logFatal(e.getMessage());
				driver.quit();
			}
		}
		
		try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("2758")){
			try {tests.add(new SAI_2758(driver));} 
			//catch (IOException | InterruptedException e) {e.printStackTrace();}
			catch(Exception e){
				e.printStackTrace();
				logFatal(e.getMessage());
				driver.quit();
			}
		}
		
		try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("2760")){
			try {tests.add(new SAI_2760(driver));} 
			//catch (IOException | InterruptedException e) {e.printStackTrace();}
			catch(Exception e){
				e.printStackTrace();
				logFatal(e.getMessage());
				driver.quit();
			}
		}
		
		try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("3296")){
			try {tests.add(new SAI_3296(driver));} 
			//catch (IOException | InterruptedException e) {e.printStackTrace();}
			catch(Exception e){
				e.printStackTrace();
				logFatal(e.getMessage());
				driver.quit();
			}
		}
		
		/*try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("3297")){
			try {tests.add(new SAI_3297(driver));} 
			//catch (IOException | InterruptedException e) {e.printStackTrace();}
			catch(Exception e){
				e.printStackTrace();
				logFatal(e.getMessage());
				driver.quit();
			}
		}*/
		
		try {Thread.sleep(500);} catch (InterruptedException e1) {}
		driver = new FirefoxDriver();
		if(!doNotTest.contains("2714")){
			try {tests.add(new SAI_2714(driver));} 
			catch(Exception e){
				logFatal(e.getMessage());
				driver.quit();
			}
		}
	}

}

