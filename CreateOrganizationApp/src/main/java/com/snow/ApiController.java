package com.snow;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ApiController {
	   private String url ="http://api.sys.eu.cfdev.canopy-cloud.com/v2/organizations";  
	   private MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	   //private String uaaUrl = "http://localhost:8181/Snow-proxy/v2/Authorization";
	   private String uaaUrl = "http://uaatokengenerator.apps.eu.cfdev.canopy-cloud.com/v1/get-UAA-token";   
	   
	   RestTemplate restTemplate = new RestTemplate();
	 
	   @Autowired(required = false) ApplicationInstanceInfo instanceInfo;
	   
	@RequestMapping(value="/v1/create-organization" ,method = RequestMethod.POST)   
	public ResponseEntity<String> Create_Organization(Model model, @RequestBody String json) throws FileNotFoundException, IOException{
		model.addAttribute("instanceInfo", instanceInfo);
	
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> requestParams = mapper.readValue(json, Map.class);
		 System.out.println(requestParams);
	    String uaatoken =  restTemplate.getForObject(uaaUrl, String.class);
	    headers.add("Authorization", uaatoken);
	    headers.add("Content-Type", "application/x-www-form-urlencoded");
	    headers.add("Host", "api.sys.eu.cfdev.canopy-cloud.com");
	    
	    System.out.println(headers);
		
	    HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestParams, headers);
	    System.out.println(httpEntity);
	    //HttpEntity<String> requestEntity = new HttpEntity<>("Headers", headers);
		return restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
		
	}
}
