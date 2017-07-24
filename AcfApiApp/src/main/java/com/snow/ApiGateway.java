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
	public String getToken(Model model,
			@RequestBody String data)  {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-uaa-token-api");
		return restTemplate.postForObject(url,data,String.class);
	}
	
	@RequestMapping(value = "/v1/add-build-pack", method = RequestMethod.POST)
	public String addBuildpack(Model model,
			@RequestBody String data)  {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-add-buildpack");
		return restTemplate.postForObject(url,data,String.class);
	}
	
	@RequestMapping(value = "/v1/associate_auditor_with_the_org_by_username", method = RequestMethod.POST)
	public String AssociateAuditorwithOrg(Model model,
			@RequestBody String data)  {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-Associate-Auditor-with-Org");
		return restTemplate.postForObject(url,data,String.class);
	}
	
	@RequestMapping(value = "/v1/associate-developer-with-space-by-username", method = RequestMethod.POST)
	public String AssociatedeveloperWithSpace(Model model,
			@RequestBody String data)  {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-associate-devloper-with-space");
		return restTemplate.postForObject(url,data,String.class);
	}
	
	@RequestMapping(value = "/v1/add-app-instance-data", method = RequestMethod.POST)
	public String addAppInstance(Model model,
			@RequestBody String data)  {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-add-app-instance");
		return restTemplate.postForObject(url,data,String.class);
	}
	
	
}

