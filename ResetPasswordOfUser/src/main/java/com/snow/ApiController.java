package com.snow;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

@RestController
public class ApiController {
	  // private String orgurl ="https://api.sys.eu.cfdev.canopy-cloud.com/v2/organizations";  
	   private String uaaUrl = "http://uaatokengenerator.apps.eu.cfdev.canopy-cloud.com/v1/get-UAA-token";
	   RestTemplate restTemplate = new RestTemplate();
	 
	@RequestMapping("/Snow-proxy/v2/ResetPassword")   
	public ResponseEntity<String> associateUserWithSpace() throws FileNotFoundException, IOException {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		
		System.getProperties().put("http.proxyHost","proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("http.proxyPort","84"); 
		System.getProperties().put("https.proxyHost","proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("https.proxyPort","84"); 
		
	    String uaatoken =  getUaaToken();
	    
	    String username = "snow@gmail.com";
	    System.out.println(username);
	 //   String uaaId= getUserUaaId(userEmailId);
	    String url= "https://uaa.sys.eu.cfdev.canopy-cloud.com/password_resets";
	    headers.add("Authorization", uaatoken);
	    headers.add("Content-Type", "application/json");
	  //  headers.add("Accept", "application/json");
	  //  headers.add("If-Match", "0");
	    
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
	    String json = IOUtils.toString(new FileInputStream("./src/main/resources/PasswordDetails.json"));
	    HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
	    
	   // ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class).getStatusCode();
	    //return response;	
	    return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
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
		
		System.out.println(orgName);
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
		System.getProperties().put("http.proxyHost","proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("http.proxyPort","84"); 
		System.getProperties().put("https.proxyHost","proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("https.proxyPort","84"); 
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
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
		
		System.getProperties().put("http.proxyHost","proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("http.proxyPort","84"); 
		System.getProperties().put("https.proxyHost","proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("https.proxyPort","84"); 
		
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
