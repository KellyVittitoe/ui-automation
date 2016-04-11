package com.ibm.uitests.pages;

import org.openqa.selenium.WebDriver;
import com.ibm.uitests.common.DBConn;
import com.ibm.uitests.common.JSONReader;

public class EndpointsTab extends HomePage{
	
	
	private final String AUTHORIZATION = "Authorization";
	private final String BEARER = "Bearer ";
	
	public enum Endpoints{
		EVENT, AUDIENCE_SP_PRODUCER, AUDIENCE_MM_CONSUMER, AUDIENCE_SP_CONSUMER, DESTINATION
	}

	public EndpointsTab(WebDriver driver) {
		super(driver);
		
		if(!isCurrentTab(Tab.ENDPOINTS)){
			String message = "Did not find the endpoints tab.";
			logWarn(message);
			throw new IllegalStateException(message);
		}
	}
	
	public String createNewEndpoint(String name, String description, Endpoints endpoints){
		return createNewEndpoint(name, description, endpoints, true);
	}
	
	public String createNewEndpoint(String name, String description, Endpoints endpoints, boolean EVENTTYPE_AUTO_FLAG){
		
		getElement("registerNewEndpoint").click();
		waitFor(550);
		getElementByTitle("Custom endpoint").click();
		//getElement("selectCustomEndpoint").click();
		getElement("registerNextButton").click();
		getElement("generateButton").click();
		
		waitFor(300);
		
		String authenticationKey = getElement("authenticationKey").getText();
		
		getElement("registerNextButton").click();
		getElement("registerFinishButton").click();
		
		waitFor(1500);
		
		switch(endpoints){
		case EVENT: createEventEndpoint(name, description, authenticationKey, EVENTTYPE_AUTO_FLAG);
			setEventKey(authenticationKey);
			logInfo("Created new Event Endpoint: "+authenticationKey);
			break;
			
		case AUDIENCE_SP_PRODUCER: createSPAudienceProducerEndpoint(name, description, authenticationKey, true);
			setAudienceProducerKey(authenticationKey);
			logInfo("Created new Audience Endpoint: "+authenticationKey);
			break;
			
		case AUDIENCE_MM_CONSUMER: createMMAudienceConsumerEndpoint(name, description, authenticationKey, true);
			setAudienceConsumerKey(authenticationKey);
			logInfo("Created new Audience Endpoint: "+authenticationKey);
			break;
			
		case AUDIENCE_SP_CONSUMER:createSPAudienceConsumerEndpoint(name, description, authenticationKey, true);
			setAudienceConsumerKey(authenticationKey);
			logInfo("Created new Audience Endpoint: "+authenticationKey);
			break;
		
		case DESTINATION: createDestinationEndpoint(name, description, authenticationKey);
			setDestinationKey(authenticationKey);
			logInfo("Created new Destination Endpoint: "+authenticationKey);
			break;
			
		default:
			break;
		}
		
		return authenticationKey;
	}

	private String createDestinationEndpoint(String name, String description, String authenticationKey){
		String destinationContent = "{"+
				
				" \"name\":\""+name+"\","+
				" \"description\":\""+description+"\","+
				" \"providerName\":\"IBM\","+
				" \"url\":\"http://10.124.80.139:5678/timedevent\","+
				" \"endpointTypes\":{"+
				" \"event\":{"+
				" \"destination\":{"+
				" \"enabled\":true,"+
				" \"url\":\"http://10.124.80.139:5678/timedevent\","+
				" \"destinationType\":\"push\""+
				" }"+
				" }"+
				" },"+
				" \"marketingDatabasesDefinition\":{"+
				" \"marketingDatabases\":["+
				" {"+
				" \"name\":\"marketingDBSource1\","+
				" \"identifiers\":["+
				"{"+
				"\"name\":\"destEmail\","+
				"\"type\":\"email1\""+
				"}"+
				"],"+
				"\"attributes\":["+
				"{"+
				"\"name\":\"attr\","+
				"\"type\":\"address\","+
				"\"isRequired\":true"+
				"}"+
				"]"+
				"}"+
				"]"+
				"}"+
				"}";
		
		HttpRequest http = new HttpRequest(getConfig("API")+"v1/endpoint", "PUT", destinationContent, "Authorization", BEARER+authenticationKey);
		return http.getResponse();
	}
	
	private String createMMAudienceConsumerEndpoint(String name, String description, String authenticationKey, boolean autoAddAttributes){
		JSONReader reader = new JSONReader("MMConsumer", name, description);
		String segmentEndpointContent =	reader.getJSON();
		HttpRequest http = new HttpRequest(getConfig("API")+"v1/endpoint", "PUT", segmentEndpointContent, "Authorization", BEARER+authenticationKey);
		
		if(autoAddAttributes){
			createMMEConsumerEndpointAttributes(authenticationKey);
		}
		
		return http.getResponse();
	}
	
	private String createSPAudienceProducerEndpoint(String name, String description, String authenticationKey, boolean autoAddAttributes){
		JSONReader reader = new JSONReader("SPProducerConsumer", name, description);
		String segmentEndpointContent = reader.getJSON();
				
		HttpRequest http = new HttpRequest(getConfig("API")+"v1/endpoint", "PUT", segmentEndpointContent, "Authorization", BEARER+authenticationKey);
		
		if(autoAddAttributes){
			createSPProducerEndpointAttributes(authenticationKey);
		}
		
		return http.getResponse();
	}
	
	private String createSPAudienceConsumerEndpoint(String name, String description, String authenticationKey, boolean autoAddAttributes){
		JSONReader reader = new JSONReader("SPProducerConsumer", name, description);
		String segmentEndpointContent = reader.getJSON();
				
		HttpRequest http = new HttpRequest(getConfig("API")+"v1/endpoint", "PUT", segmentEndpointContent, "Authorization", BEARER+authenticationKey);
		
		if(autoAddAttributes){
			createSPProducerEndpointAttributes(authenticationKey);
		}
		
		return http.getResponse();
	}
	
	public String createMMEConsumerEndpointAttributes(String authenticationKey){
		JSONReader reader = new JSONReader("MMAttributes");
		String json = reader.getJSON();
		String url = "v1/endpointattributes/authkey/"+authenticationKey;
		HttpRequest http = new HttpRequest(getConfig("API")+url, "PUT", json, AUTHORIZATION, BEARER+authenticationKey);
		return http.getResponse();
	}
	
	public String createSPProducerEndpointAttributes(String authenticationKey){
		JSONReader reader = new JSONReader("SPProducerConsumerAttributes");
		String json = reader.getJSON();
		String url = "v1/endpointattributes/authkey/"+authenticationKey;
		HttpRequest http = new HttpRequest(getConfig("API")+url, "PUT", json, AUTHORIZATION, BEARER+authenticationKey);
		return http.getResponse();
	}
	
	public String createSPConsumerEndpointAttributes(String authenticationKey){
		JSONReader reader = new JSONReader("SPProducerConsumerAttributes");
		String json = reader.getJSON();
		String url = "v1/endpointattributes/authkey/"+authenticationKey;
		HttpRequest http = new HttpRequest(getConfig("API")+url, "PUT", json, AUTHORIZATION, BEARER+authenticationKey);
		return http.getResponse();
	}
	
	public String createEventEndpoint(String name, String description, String authenticationKey, boolean addEventType){
		String endpointContent = 
				"{"+
						"\"name\":\""+name+"\","+
						"\"description\":\""+description+"\","+
						"\"providerName\":\"IBM\","+
						"\"endpointTypes\":{"+
							"\"event\":{"+
								"\"source\":{"+
									"\"enabled\":true"+
								"}"+           
							"}"+
							"},"+
				    "\"marketingDatabasesDefinition\":{"+
				        "\"marketingDatabases\":["+
				            "{"+
				                "\"name\":\""+name+"\","+
				                "\"identifiers\":["+
				                    "{"+
				                        "\"name\":\"homeEmail\","+
				                        "\"type\":\"email\""+
				                    "},"+
				                    "{"+
				                        "\"name\":\"workEmail\","+
				                        "\"type\":\"email\""+
				                    "},"+
				            "{"+
				                        "\"name\":\"cookieId\","+
				                        "\"type\":\"web\""+
				                    "}"+
				                "],"+
				                "\"attributes\":["+
				                    "{"+
				                        "\"name\":\"homeAddress\","+
				                        "\"type\":\"address\","+
				                        "\"isRequired\":true"+
				                    "},"+
				                    "{"+
				                        "\"name\":\"workAddress\","+
				                        "\"type\":\"address\","+
				                        "\"isRequired\":true"+
				                    "}"+
				                "]"+
				            "}"+
				        "]"+
				    "}"+
				"}"; // END endpointContent
		
		
		HttpRequest http = new HttpRequest(getConfig("API")+"v1/endpoint", "PUT", endpointContent, AUTHORIZATION, BEARER+authenticationKey);
		
		
		
		if(addEventType){
			logInfo("Adding default EVENTYPE to Endpoint.");
			String eventtypeName = getConfig("EVENTTYPE")+pickSeed(name);
			createEventType(eventtypeName, description, authenticationKey);
		}
		
		return http.getResponse();
	}
	
	public HttpRequest createEventType(String name, String description, String authenticationKey){
		String eventtypeContent = "{"+
				"\"name\": \""+name+"\","+
				"\"description\": \""+description+"\","+
				"\"code\": \"ibmcartAbandonment\""+
		"}";
		
		HttpRequest http = new HttpRequest(getConfig("API")+"v1/eventtype","POST", eventtypeContent, AUTHORIZATION, BEARER+authenticationKey);
		return http;
	}
	
	public HttpRequest createEventType(String name, String description, String code, String authenticationKey){
		String eventtypeContent = "{"+
				"\"name\": \""+name+"\","+
				"\"description\": \""+description+"\","+
				"\"code\": \""+code+"\""+
		"}";
		
		HttpRequest http = new HttpRequest(getConfig("API")+"v1/eventtype","POST", eventtypeContent, AUTHORIZATION, BEARER+authenticationKey);
		return http;
	}
	
	public String updateEventType(String name, String description, String authenticationKey){
		String eventtypeContent = "{"+
				"\"name\": \""+name+"\","+
				"\"description\": \""+description+"\","+
				"\"code\": \"ibmcartAbandonment\""+
		"}";
		
		HttpRequest http = new HttpRequest(getConfig("API")+"v1/eventtype","PUT", eventtypeContent, AUTHORIZATION, BEARER+authenticationKey);
		return http.getResponse();
	}
	
	public int updateEventType(String name, String description, String code, String authenticationKey){
		String eventtypeContent = "{"+
				"\"name\": \""+name+"\","+
				"\"description\": \""+description+"\","+
				"\"code\": \""+code+"\""+
		"}";
		
		HttpRequest http = new HttpRequest(getConfig("API")+"v1/eventtype","PUT", eventtypeContent, AUTHORIZATION, BEARER+authenticationKey);
		
		
		return http.getResponseCode();
	}
	
	public boolean databaseEndpointExists(String name){
		waitFor(500);
		String query = "SELECT ENDPOINT_ID FROM BPXDATA.BPX_ENDPOINTS WHERE ENDPOINT_NAME='"+name+"'";
		try{
			return new DBConn().valueExists(query);
		}catch(NullPointerException e){
			logFatal("Unable to connect to the database.");
		}
		
		return false;
	}
	
	public int databaseEndpointCount(){
		String query = "SELECT COUNT(*) AS ENDPOINT_COUNT FROM BPXDATA.BPX_ENDPOINTS WHERE BPX_ACCOUNT_ID = '1591'";
		try{
			return new DBConn().getCount(query);
		}catch(NullPointerException e){
			logFatal("Unable to connect to the database.");
		}
		
		return -1;
	}
	
	public boolean databaseEventtypeExists(String name){
		waitFor(500);
		String query = "SELECT EVENT_TYPE_ID FROM BPXDATA.BPX_EVENT_TYPE WHERE NAME='"+name+"'";
		return new DBConn().valueExists(query);
	}
	
	public void deleteEndpoint(String name, boolean commit){
		EndpointsTableView tableView = new EndpointsTableView(driver);	
		tableView.deleteEndpoint(name, commit);
	}
	
	public void toggleEndpoint(String name, boolean commit){
		EndpointsTableView tableView = new EndpointsTableView(driver);
		tableView.toggleEndpointStatus(name, commit);
	}
	
	public boolean endpointExists(String name){
		EndpointsTableView tableView = new EndpointsTableView(driver);
		return tableView.endpointExists(name);
	}
	
	public boolean isEndpointDisabled(String name){
		EndpointsTableView tableView = new EndpointsTableView(driver);
		return tableView.isEndpointDisabled(name);
	}
	
	private String pickSeed(String name){
		return name.substring(name.length()-5);
	}
	
	public void clickRegisterCancelButton(){
		getElement("registerFinishButton").click();
	}
}