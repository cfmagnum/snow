package com.snow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.core.env.Environment;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@RestController
public class ApiController {
	@Autowired
	Environment env;

	

	private RestTemplate restTemplate = new RestTemplate();
	private Gson gson = new Gson();

	@Autowired(required = false)
	ApplicationInstanceInfo instanceInfo;

	@RequestMapping(value = "/v1/create-quota-definition", method = RequestMethod.POST)
	public ResponseEntity<String> Create_Quota_Definition(Model model,
			@RequestBody String data) throws JsonParseException,
			JsonMappingException, IOException {
		model.addAttribute("instanceInfo", instanceInfo);

		String url="";
	    String clientName="";
	    String authToken="";
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> requestParams = mapper.readValue(data, Map.class);
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		Map<String, Object> params = new HashMap<String, Object>();
		authToken=(String) requestParams.get("authToken");
		clientName =(String) requestParams.get("clientName");
        url=env.getProperty("url-" +clientName);

		

		requestParams = mapper.readValue(data, Map.class);
		headers.add("Authorization", authToken);
		headers.add("Content-Type", env.getProperty("Content-Type-json"));
		headers.add("Accept", env.getProperty("Host"));
		
		for (String key : requestParams.keySet()) {
			if (key !="authToken"&& key!="clientName") {
				params.put(key, requestParams.get(key));
			}
		}
		String jsonData = gson.toJson(params);
		HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers);
		
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);

	}
}
