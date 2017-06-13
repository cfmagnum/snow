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
	   private String uaaUrl = "http://localhost:8181/Snow-proxy/v2/Authorization";
	   RestTemplate restTemplate = new RestTemplate();
	 
	@RequestMapping("/Snow-proxy/v2/UpdateQuotaSizeOfOrg")   
	public ResponseEntity<String> associateUserWithOrg() throws FileNotFoundException, IOException {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	    String uaatoken =  getUaaToken();
	    String orgName = "snow-test4";
	    String quota_definition_guid= getQuotaDefinitionGuid(orgName);
	    String url= "https://api.sys.eu.cfdev.canopy-cloud.com/v2/quota_definitions/" + quota_definition_guid;
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
	    String json = IOUtils.toString(new FileInputStream("./src/main/resources/QuotaUpdates.json"));
	    HttpEntity<String> httpEntity = new HttpEntity<>(json, headers);
	    
	    
	    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);
	    return response;		
	}
	
	
	public String getUaaToken(){
		 String token =  restTemplate.getForObject(uaaUrl, String.class);
		 return token;
	}
	
	
	public String getQuotaDefinitionGuid(String orgName){
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		String url = "https://api.sys.eu.cfdev.canopy-cloud.com/v2/organizations?q=name:" + orgName;	
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
		    quota_definition_guid = metadata.get("quota_definition_guid").getAsString();
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