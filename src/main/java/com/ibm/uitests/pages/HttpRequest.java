package com.ibm.uitests.pages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.ibm.uitests.common.BaseTest;

public class HttpRequest extends BaseTest{
	
	private int responseCode;
	private String response;


	public HttpRequest(String URL, String method, String json, String authParameter, String authValue){
		
		this.response = post(URL, method, json, authParameter, authValue);
		
	}
	
	public HttpRequest(String URL, String authParameter, String authValue){
		this.response = get(URL, authParameter, authValue);
	}
	
	public int getResponseCode() {
		return responseCode;
	}

	public String getResponse() {
		return response;
	}

	public String post(String URL, String method, String json, String authParameter, String authValue){
		StringBuilder responseSB = null;
		
		try{    
        URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod(method);
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty(authParameter, authValue);
         
        // Write data
        OutputStream os = connection.getOutputStream();
        os.write(json.getBytes());
         
        // Read response
        responseSB = new StringBuilder();
        
        responseCode = connection.getResponseCode();
        

        logInfo(URL +" :: "+responseCode);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
          
        String line;
        while ( (line = br.readLine()) != null)
            responseSB.append(line);
                 
        // Close streams
        br.close();
        os.close();
        }catch(IOException e){
        	logWarn(e.getMessage());
        }
         
        return responseSB.toString();
    }
	
	public String get(String URL, String authParameter, String authValue){
		String USER_AGENT = "Mozilla/5.0";
		String result = null;
		URL obj;
		try {
			obj = new URL(URL);
			HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			connection.setRequestMethod("GET");

			//add request header
			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Content-Type", "application/json");
	        connection.setRequestProperty(authParameter, authValue);

			responseCode = connection.getResponseCode();
			
			logInfo(URL +" :: "+responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(connection.getInputStream()));
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
}
