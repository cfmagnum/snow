package com.snow;

import java.io.IOException;
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

@RestController
public class ApiController {
	@Autowired
	Environment env;
	private String url = "http://api.sys.eu.cfdev.canopy-cloud.com/v2/quota_definitions";
	private MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();

	RestTemplate restTemplate = new RestTemplate();

	@Autowired(required = false)
	ApplicationInstanceInfo instanceInfo;

	@RequestMapping(value = "/v1/create-quota-definition", method = RequestMethod.POST)
	public ResponseEntity<String> Create_Quota_Definition(Model model,
			@RequestBody String json) throws JsonParseException,
			JsonMappingException, IOException {
		model.addAttribute("instanceInfo", instanceInfo);

		String uaatoken = restTemplate.getForObject(env.getProperty("uaaUrl"),
				String.class);
		headers.add("Authorization", uaatoken);
		headers.add("Content-Type", env.getProperty("Content-Type-json"));
		headers.add("Accept", env.getProperty("Host"));
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> requestParams = mapper.readValue(json, Map.class);
		System.out.println();
		System.out.println(headers);
		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(
				requestParams, headers);
		System.out.println(httpEntity);
		return restTemplate.exchange(url, HttpMethod.POST, httpEntity,
				String.class);

	}
}
