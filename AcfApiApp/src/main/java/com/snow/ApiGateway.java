package com.snow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.core.env.Environment;
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
	public String getToken(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-uaa-token-api");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/add-build-pack", method = RequestMethod.POST)
	public String addBuildpack(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-add-buildpack");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/delete-buildpack", method = RequestMethod.POST)
	public String DeleteBuildpack(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-delete-buildpack");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/add-app-instance-data", method = RequestMethod.POST)
	public String addAppInstance(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-add-app-instance");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/delete-app-instance-data", method = RequestMethod.POST)
	public String DeleteApplication(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-delete-app-instance-data");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/create-space", method = RequestMethod.POST)
	public String CreateSpace(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-create-space");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/create-organization", method = RequestMethod.POST)
	public String CreateOrganization(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-create-organization");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/associate-user-with-org", method = RequestMethod.POST)
	public String AssociateUserWithOrg(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-associate-user-with-org");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/associate-auditor-with-org", method = RequestMethod.POST)
	public String AssociateAuditorwithOrg(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-Associate-Auditor-with-Org");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/associate_auditor_with_the_org_by_username", method = RequestMethod.POST)
	public String AssociateAuditorwithOrgByUserName(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env
				.getProperty("url-Associate-Auditor-with-Org-by-user-name");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/associate_manager_with_the_org_by_username", method = RequestMethod.POST)
	public String associateManagerwiththeOrgbyUsername(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env
				.getProperty("url-associate-Manager-with-the-Orgby-Username");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/associate_billing_manager_with_the_organization_by_username", method = RequestMethod.POST)
	public String associateBillingManagerwiththeOrgbyUsername(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env
				.getProperty("url-associate-billing-Manager-with-the-Orgby-Username");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/delete-org", method = RequestMethod.POST)
	public String DeleteOrg(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-delete-org");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/associate-user-with-space", method = RequestMethod.POST)
	public String AssociateUserWithSpace(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-associate-user-with-space");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/associate-auditor-with-space", method = RequestMethod.POST)
	public String AssociateAuditorWithSpace(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-associate-auditor-with-space");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/associate-auditor-with-space-by-username", method = RequestMethod.POST)
	public String associateAuditorWithSpaceByUsername(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env
				.getProperty("url-associate-auditor-with-space-by-username");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/associate-developer-with-space-by-username", method = RequestMethod.POST)
	public String AssociatedeveloperWithSpace(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-associate-devloper-with-space");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/associate-manager-with-space-by-username", method = RequestMethod.POST)
	public String AssociateManagerWithSpaceByUsername(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env
				.getProperty("url-associate-manager-with-space-by-username");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/create-quota-definition", method = RequestMethod.POST)
	public String CreateQuotaDefinition(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-create-quota-definition");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/update-quota-size-of-space", method = RequestMethod.POST)
	public String UpdateQuotaSizeForSpace(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-update-quota-size-of-space");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/update-quota-size-of-org", method = RequestMethod.POST)
	public String UpdateQuotaSizeForOrg(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-update-quota-size-of-org");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/create-user", method = RequestMethod.POST)
	public String UserCreation(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-create-user");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/reset-password-of-user", method = RequestMethod.POST)
	public String ResetPasswordOfUser(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-reset-password-of-user");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/delete-user", method = RequestMethod.POST)
	public String DeleteUser(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-delete-user");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/fetch-user-information", method = RequestMethod.POST)
	public String FetchUserInfoApp(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-fetch-user-information");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/update-service-broker", method = RequestMethod.POST)
	public String UpdateServiceBroker(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-update-service-broker");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/list-Organizations", method = RequestMethod.POST)
	public String ListOrganizations(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-list-Organizations");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/list-spaces", method = RequestMethod.POST)
	public String ListSpacesinOrgs(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-list-spaces");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/list-users", method = RequestMethod.POST)
	public String ListUsers(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-list-users");
		return restTemplate.postForObject(url, data, String.class);
	}
}
