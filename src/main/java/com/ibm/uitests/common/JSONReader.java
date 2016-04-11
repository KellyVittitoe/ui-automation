package com.ibm.uitests.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JSONReader extends BaseTest{

	private String json;
	public JSONReader(String key, String name, String description) {
		String temp = read(key);
		this.json = addDetails(name, description, temp);
	}
	
	public JSONReader(String key){
		this.json = read(key);
	}
	
	private String read(String key){
		String loc = getConfig(key);
		
		BufferedReader read = null;
		String line;
		StringBuilder sb = new StringBuilder();
		try{
			read = new BufferedReader(new FileReader("json/"+loc));
			while((line = read.readLine()) != null){
				sb.append(line.trim());
			}
		}catch(IOException e){
			logFatal("Unable to read JSON file");
		}
		
		return sb.toString();
	}
	
	private String addDetails(String name, String description, String json){
		String temp = json.replaceFirst("\"NAME\"", "\""+name+"\"");
		return temp.replaceFirst("\"DESCRIPTION\"", "\""+description+"\"");
	}
	
	public String getJSON(){
		return json;
	}
}
