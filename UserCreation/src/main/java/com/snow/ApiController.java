package com.snow;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.core.env.Environment;
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

@RestController
public class ApiController {
	@Autowired
	Environment env;
	   private String url ="https://uaa.sys.eu.cfdev.canopy-cloud.com/Users";  
	   private MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	  
	   RestTemplate restTemplate = new RestTemplate();
	   @Autowired(required = false) ApplicationInstanceInfo instanceInfo;
	 
	@RequestMapping(value = "/v1/create-user" , method = RequestMethod.POST )   
	public ResponseEntity<String> Create_User(Model model,@RequestBody String json) throws FileNotFoundException, IOException{
		model.addAttribute("instanceInfo", instanceInfo);
		String uaatoken =  restTemplate.getForObject(env.getProperty("uaaUrl"), String.class);
	    headers.add("Authorization", uaatoken);
	    headers.add("Content-Type", env.getProperty("Content-Type-json"));
	    headers.add("Accept", env.getProperty("Host"));
	    ObjectMapper mapper = new ObjectMapper();
	    Map<String,Object> requestParams = mapper.readValue(json, Map.class);
	    		
	    HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestParams, headers);
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
	    return restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
		
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
