package com.ibm.uitests.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public abstract class BaseTest {
	
	private Logger log = Logger.getLogger(UITest.class);
	
	private Map<String, String> config;
	
	public BaseTest(){
		initializeLog();
		config = getProperties("conf/config.properties");
	}
	
	private void initializeLog(){
		try{
			PropertyConfigurator.configure("conf/log4j.properties");
		} catch(NoClassDefFoundError e){
			e.printStackTrace();
		}
	}
	
	public void logDebug(String message ){
		System.out.println(message);
		log.debug(message);
	}
	
	public void logInfo(String message){
		System.out.println(message);
		log.info(message);
	}
	
	public void logWarn(String message){
		System.out.println("WARN: "+message);
		log.warn(message);
	}
	
	public void logFatal(String message){
		System.out.println("FATAL: "+message);
		log.fatal(message);
	}
	
	protected Map<String, String> getProperties(String path){
		Map<String, String> prop = new HashMap<String, String>();
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(path));
		  
			for(String key : properties.stringPropertyNames()) {
				String value = properties.getProperty(key);
				prop.put(key, value);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return prop;
	}
	
	public String getConfig(String key){
		return config.get(key);
	}
	
	public void waitFor(int milliseconds){
		try {Thread.sleep(milliseconds);} catch (InterruptedException e) {}
	}
}
