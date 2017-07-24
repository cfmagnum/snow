package com.snow;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class ApiGateway {
	@Autowired
	private Environment env;
	private RestTemplate restTemplate = new RestTemplate();
	ApplicationInstanceInfo instanceInfo;
	@RequestMapping(value = "/v1/get-UAA-token", method = RequestMethod.POST)
	public String getUAAToken(Model model,
			@RequestBody String data)  {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-uaa-token-api");
		return restTemplate.postForObject(url, HttpMethod.POST, String.class, data);
	}
	
	}

