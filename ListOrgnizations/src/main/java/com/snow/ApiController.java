package com.snow;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiController {
	@Autowired
	Environment env;
	   private String url ="http://api.sys.eu.cfdev.canopy-cloud.com/v2/organizations";  
	   private MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	  
	   RestTemplate restTemplate = new RestTemplate();
	   @Autowired(required = false) ApplicationInstanceInfo instanceInfo;
	   
	@RequestMapping("/v1/list-Organizations")   
	public ResponseEntity<String> getOrgs(Model model){
		model.addAttribute("instanceInfo", instanceInfo);
	    String uaatoken =  restTemplate.getForObject(env.getProperty("uaaUrl"), String.class);
	    headers.add("Authorization", uaatoken);
	    headers.add("Content-Type", env.getProperty("Content-Type-json"));
	    HttpEntity<String> requestEntity = new HttpEntity<>("Headers", headers);
	    return restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		
	}
}
