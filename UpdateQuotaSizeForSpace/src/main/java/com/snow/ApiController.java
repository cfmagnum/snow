package com.snow;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.ui.Model;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

@RestController
public class ApiController {
	  // private String orgurl ="https://api.sys.eu.cfdev.canopy-cloud.com/v2/organizations";  
	   private String uaaUrl = "http://uaatokengenerator.apps.eu.cfdev.canopy-cloud.com/v1/get-UAA-token";
	   RestTemplate restTemplate = new RestTemplate();
	   private Gson gson = new Gson(); 
	   @Autowired(required = false) ApplicationInstanceInfo instanceInfo;
	 
	@RequestMapping(value= "/v1/update-quota-size-of-space", method = RequestMethod.POST)   
	public ResponseEntity<String> updateQuotaSizeOfSpace(Model model,@RequestBody String json) throws FileNotFoundException, IOException {
		model.addAttribute("instanceInfo", instanceInfo);
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		System.getProperties().put("http.proxyHost","proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("http.proxyPort","84"); 
		System.getProperties().put("https.proxyHost","proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("https.proxyPort","84"); 
		//Map<String, Object> uriVariables = new Hashmap<String, Object>();
		String spaceName ="";
		String uaatoken="";
		String quota_definition_guid="";
		String url = "";
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String,Object> requestParams = mapper.readValue(json, Map.class);
	    uaatoken =  getUaaToken();
	    spaceName = (String) requestParams.get("spaceName");
	    quota_definition_guid= getQuotaDefinitionGuid(spaceName);
	    System.out.println(spaceName);
	    
	    url= "https://api.sys.eu.cfdev.canopy-cloud.com/v2/space_quota_definitions/" + quota_definition_guid;
	    headers.add("Authorization", uaatoken);
	    headers.add("Content-Type", "application/json");
	    headers.add("Host", "api.sys.eu.cfdev.canopy-cloud.com");
	    System.out.println(url);
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
	    
	    for(String key :requestParams.keySet()){
	    	if(key!="spaceName"){
	    		params.put(key, requestParams.get(key));
	    	}
	    }
	    System.out.println(params);
	    String jsonData = gson.toJson(params);
	    HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers);
	    
	   // System.out.println(params);
	    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
	    return response;		
	}
	
	
	public String getUaaToken(){
		 String token =  restTemplate.getForObject(uaaUrl, String.class);
		 return token;
	}
	
	
	public String getQuotaDefinitionGuid(String spaceName){
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		
		System.getProperties().put("http.proxyHost","proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("http.proxyPort","84"); 
		System.getProperties().put("https.proxyHost","proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("https.proxyPort","84"); 
		
		String url = "https://api.sys.eu.cfdev.canopy-cloud.com/v2/spaces?q=name:" + spaceName;	
		  System.out.println(url);
		
		String uaatoken =  getUaaToken();
	    String quota_definition_guid="";
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
	    System.out.println(requestEntity);
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
		    
		    JsonObject metadata =resources.get("entity").getAsJsonObject();
		    System.out.println(metadata);
		    quota_definition_guid = metadata.get("space_quota_definition_guid").getAsString();
	    }
		
	   return quota_definition_guid;		
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