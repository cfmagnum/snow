package com.snow;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiController {
	   private String url ="http://api.sys.eu.cfdev.canopy-cloud.com/v2/organizations";  
	   private MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	   private String uaaUrl = "http://localhost:8181/Snow-proxy/v2/Authorization";
	   RestTemplate restTemplate = new RestTemplate();
	 
	@RequestMapping("/Snow-proxy/v2/ListOrgs")   
	public ResponseEntity<String> getOrgs() throws FileNotFoundException, IOException{
	    String uaatoken =  restTemplate.getForObject(uaaUrl, String.class);
	    headers.add("Authorization", uaatoken);
	    headers.add("Content-Type", "application/json");
	    HttpEntity<String> requestEntity = new HttpEntity<>("Headers", headers);
	    return restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		
	}
}
