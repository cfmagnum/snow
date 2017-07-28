package com.snow;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.core.env.Environment;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;



@RestController
public class ApiController {
	   
	@Autowired
	Environment env; 
	   private RestTemplate restTemplate = new RestTemplate();
	   private Gson gson = new Gson(); 
	   @Autowired(required = false) ApplicationInstanceInfo instanceInfo;
	   
	@RequestMapping(value="/v1/reset-password-of-user" ,method = RequestMethod.POST )   
	public ResponseEntity<String> resetPassword(Model model, @RequestBody String json) throws  IOException{
		model.addAttribute("instanceInfo",instanceInfo);
		 Map<String, String> params = new HashMap<String, String>();
		 ObjectMapper mapper = new ObjectMapper();
		 Map<String,Object> requestParams = mapper.readValue(json, Map.class);
		 MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		 String userName = "";
		 String oldPassword ="";
		 String newPassword ="";
		 String userId ="";
		 String userEmailId="";
		 String authToken="";
		 String clientName="";
		 String url ="";
		 String userToken ="";
		 Map<String, Object> userParams = new HashMap<String, Object>();
		 userName = (String) requestParams.get("username");
		 oldPassword= (String) requestParams.get("oldPassword");
		 newPassword= (String) requestParams.get("newPassword");
		 userEmailId = (String) requestParams.get("userEmailId");
		 authToken= (String) requestParams.get("authToken");
		 clientName=(String) requestParams.get("clientName");
	     userId = getUserUaaId(userEmailId,authToken,clientName);
	 
	     userParams.put("userName", userName);
	     userParams.put("password", oldPassword);
	     userParams.put("clientName", clientName);
	     String userData = gson.toJson(userParams);
	   
	     String urlforToken  = env.getProperty("url-uaa-token-api");
	    
	     userToken= restTemplate.postForObject(urlforToken, userData, String.class);
	    
	     url =  env.getProperty("url-" + clientName)+ "/" + userId + "/password";
	
	    
	    headers.add("Authorization", userToken);
	    headers.add("Content-Type", env.getProperty("Content-Type-json"));
	    headers.add("Accept", env.getProperty("Content-Type-json"));
	    
	     params.put("oldPassword", oldPassword);
		 params.put("password",newPassword);
		
		 
		 String jsonData = gson.toJson(params);
		 
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonData,headers);
      
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
		return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
		
	}
	
	public String getUserUaaId(String userEmailId, String authToken,String clientName) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		String url =  env.getProperty("url-" +clientName)+"?filter=emails.value eq '"
				+ userEmailId + "'";
		
		String UaaId = "";
		JsonObject resources = new JsonObject();
		Gson gson = new GsonBuilder().create();
		JsonObject job = new JsonObject();
		headers.add("Authorization", authToken);
		headers.add("content-type", "application/json");
		headers.add("Accept", "application/json");
		try {
			skipSslValidation(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpEntity<String> requestEntity = new HttpEntity<>("Headers", headers);
	
		String userinfo = restTemplate.exchange(url, HttpMethod.GET,
				requestEntity, String.class).getBody();
		try {
			job = gson.fromJson(userinfo, JsonObject.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (job != null) {
			if (job.getAsJsonArray("resources") != null) {
				resources = job.getAsJsonArray("resources").get(0)
						.getAsJsonObject();
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
