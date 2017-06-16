package com.snow;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;







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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.ui.Model;

@RestController
public class ApiController {
	   private String orgurl ="https://api.sys.eu.cfdev.canopy-cloud.com/v2/organizations";  
	   private String uaaUrl = "http://uaatokengenerator.apps.eu.cfdev.canopy-cloud.com/v1/get-UAA-token";
	   RestTemplate restTemplate = new RestTemplate();
	   @Autowired(required = false) ApplicationInstanceInfo instanceInfo;
	 
	@RequestMapping(value="/v1/delete-org", method = RequestMethod.POST)   
	public ResponseEntity<String> DeleteOrg(Model model,@RequestBody String json) throws FileNotFoundException, IOException{
		model.addAttribute("instanceInfo", instanceInfo);
		MultiValueMap<String, String> orgheaders = new LinkedMultiValueMap<String, String>();
	    String uaatoken =  getUaaToken();
	    ObjectMapper mapper = new ObjectMapper();
	    Map<String,Object> requestParams = mapper.readValue(json, Map.class);
	    String orgName = "snow-test4";
	    String orgId= getOrgid(orgName);
	    String urlfordelete= orgurl + "/" + orgId;
	    orgheaders.add("Authorization", uaatoken);
	    orgheaders.add("Content-Type", "application/json");
	    orgheaders.add("Host", "api.sys.eu.cfdev.canopy-cloud.com");
	    try {
			skipSslValidation(orgurl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			protected boolean hasError(HttpStatus statusCode) {
				return false;
			}
		});	
	  //  HttpEntity<String> requestEntity = new HttpEntity<>("Headers", orgheaders);
	    HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestParams, orgheaders);
	    
	    ResponseEntity<String> response = restTemplate.exchange(urlfordelete, HttpMethod.DELETE, httpEntity, String.class);
	    return response;		
	}
	public String getUaaToken(){
		 String token =  restTemplate.getForObject(uaaUrl, String.class);
		 return token;
	}
	public String getOrgid(String orgName){
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		String urlForId = "https://api.sys.eu.cfdev.canopy-cloud.com/v2/organizations?q=name:" + orgName;	
	    String uaatoken =  getUaaToken();
	    String orgId="";
	    JsonObject resources = new JsonObject();
	    Gson gson = new GsonBuilder().create();
	    JsonObject job=new JsonObject();
	    headers.add("Authorization", uaatoken);
	    headers.add("Host", "api.sys.eu.cfdev.canopy-cloud.com");
	    try {
			skipSslValidation(urlForId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    HttpEntity<String> requestEntity = new HttpEntity<>("Headers", headers);
	    String orgInfo = restTemplate.exchange(urlForId, HttpMethod.GET, requestEntity, String.class).getBody();
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
		    	orgId = metadata.get("guid").getAsString();
	    }
	   return orgId;		
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
