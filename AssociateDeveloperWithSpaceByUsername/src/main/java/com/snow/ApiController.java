package com.snow;



import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

@RestController
public class ApiController {
	  // private String orgurl ="https://api.sys.eu.cfdev.canopy-cloud.com/v2/organizations";  
	  // private String uaaUrl = "http://localhost:8181/Snow-proxy/v2/Authorization";
	
	
	 private String uaaUrl = "http://uaatokengenerator.apps.eu.cfdev.canopy-cloud.com/v1/get-UAA-token";   
	RestTemplate restTemplate = new RestTemplate();
	 
	 @Autowired(required = false) ApplicationInstanceInfo instanceInfo;
	
	@RequestMapping(value = "v1/associate-developer-with-space-by-username", method = RequestMethod.POST)   
	public ResponseEntity<String> associateDeveloperWithSpaceByUsername(Model model, @RequestBody String json) throws JsonParseException, JsonMappingException, IOException {
		model.addAttribute("instanceInfo", instanceInfo);
		System.getProperties().put("http.proxyHost","proxy-in.glb.my-it-solutions.net");
        System.getProperties().put("http.proxyPort","84"); 
        System.getProperties().put("https.proxyHost","proxy-in.glb.my-it-solutions.net");
        System.getProperties().put("https.proxyPort","84");
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	    String uaatoken =  getUaaToken();
	    ObjectMapper mapper = new ObjectMapper();
	    Map<String,Object> requestParams = mapper.readValue(json, Map.class);
	    String orgName = (String) requestParams.get("organizationName");
	    String orgGuid= getOrgGuid(orgName);
	    String spaceName = (String) requestParams.get("spaceName");
	   // String userEmailId =(String) requestParams.get("userEmailId");
	    String spaceGuid = getSpaceGuid(orgGuid, spaceName);
	    String username = (String) requestParams.get("username");
	    
	    Gson gson = new Gson(); 
		 
	    Map<String, String> params = new HashMap<String, String>();
	    params.put("username", username);
	    
	    System.out.println(username+spaceName);
	    
	    String url= "https://api.sys.eu.cfdev.canopy-cloud.com/v2/spaces/" + spaceGuid + "/developers";
	    headers.add("Authorization", uaatoken);
	    headers.add("Content-Type", "application/x-www-form-urlencoded");
	    headers.add("Host", "api.sys.eu.cfdev.canopy-cloud.com");
	    try {
			skipSslValidation(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			protected boolean hasError(HttpStatus statusCode) {
				return false;
			}
		});	
	    String jsonData = gson.toJson(params);
	    HttpEntity<String> requestEntity = new HttpEntity<>(jsonData,headers);
	    System.out.println(url);
	    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
	    return response;		
	}
	
	
	public String getUaaToken(){
		 String token =  restTemplate.getForObject(uaaUrl, String.class);
		 return token;
	}
	
	
	public String getOrgGuid(String orgName){
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		System.getProperties().put("http.proxyHost","proxy-in.glb.my-it-solutions.net");
        System.getProperties().put("http.proxyPort","84"); 
        System.getProperties().put("https.proxyHost","proxy-in.glb.my-it-solutions.net");
        System.getProperties().put("https.proxyPort","84");
		String url = "https://api.sys.eu.cfdev.canopy-cloud.com/v2/organizations?q=name:" + orgName;	
	    String uaatoken =  getUaaToken();
	    String guid="";
	    JsonObject resources = new JsonObject();
	    Gson gson = new GsonBuilder().create();
	    JsonObject job=new JsonObject();
	    headers.add("Authorization", uaatoken);
	    headers.add("Host", "api.sys.eu.cfdev.canopy-cloud.com");
	    try {
			skipSslValidation(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    HttpEntity<String> requestEntity = new HttpEntity<>("Headers", headers);
	    String orgInfo = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
	    try {
			job = gson.fromJson(orgInfo, JsonObject.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		if(job!=null){
		    if(job.getAsJsonArray("resources")!=null){
		    	resources=job.getAsJsonArray("resources").get(0).getAsJsonObject();
		    }		
		   
		    JsonObject metadata =resources.get("metadata").getAsJsonObject();
		    guid = metadata.get("guid").getAsString();
	    }
	   return guid;		
	}
	public String getSpaceGuid(String orgGuid, String spaceName){
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		System.getProperties().put("http.proxyHost","proxy-in.glb.my-it-solutions.net");
        System.getProperties().put("http.proxyPort","84"); 
        System.getProperties().put("https.proxyHost","proxy-in.glb.my-it-solutions.net");
        System.getProperties().put("https.proxyPort","84");
		String url = "https://api.sys.eu.cfdev.canopy-cloud.com/v2/organizations/" + orgGuid + "/spaces" + "?q=name:" + spaceName;	
	    String uaatoken =  getUaaToken();
	    String guid="";
	    JsonObject resources = new JsonObject();
	    Gson gson = new GsonBuilder().create();
	    JsonObject job=new JsonObject();
	    headers.add("Authorization", uaatoken);
	    headers.add("Host", "api.sys.eu.cfdev.canopy-cloud.com");
	    try {
			skipSslValidation(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    HttpEntity<String> requestEntity = new HttpEntity<>("Headers", headers);
	    String orgInfo = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
	    try {
			job = gson.fromJson(orgInfo, JsonObject.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		if(job!=null){
		    if(job.getAsJsonArray("resources")!=null){
		    	resources=job.getAsJsonArray("resources").get(0).getAsJsonObject();
		    }		
		    
		    JsonObject metadata =resources.get("metadata").getAsJsonObject();
		    guid = metadata.get("guid").getAsString();
	    }
		
	   return guid;		
	}
	public String getUserUaaId(String userEmailId){
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		String url= "https://uaa.sys.eu.cfdev.canopy-cloud.com/Users?filter=emails.value eq '" + userEmailId + "'";	
	    String uaatoken = getUaaToken();
	    String UaaId="";
	    JsonObject resources = new JsonObject();
	    Gson gson = new GsonBuilder().create();
	    JsonObject job=new JsonObject();
	    headers.add("Authorization", uaatoken);
	    headers.add("content-type", "application/json");
	    headers.add("Accept","application/json");
	    try {
			skipSslValidation(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    HttpEntity<String> requestEntity = new HttpEntity<>("Headers", headers);
	    String userinfo = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
	    try {
			job = gson.fromJson(userinfo, JsonObject.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		if(job!=null){
		    if(job.getAsJsonArray("resources")!=null){
		    	resources=job.getAsJsonArray("resources").get(0).getAsJsonObject();    
		    	UaaId = resources.get("id").getAsString();
		    }
	    }
	   return UaaId;	
	}
	
	public void skipSslValidation(String ConnectionURL) throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		
   }
}
