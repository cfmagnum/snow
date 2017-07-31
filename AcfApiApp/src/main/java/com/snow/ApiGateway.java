package com.snow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
	public String getToken(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-uaa-token-api");
		return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/add-build-pack", method = RequestMethod.POST)
	public ResponseEntity<String> addBuildpack(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-add-buildpack");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/delete-buildpack", method = RequestMethod.POST)
	public HttpStatus DeleteBuildpack(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-delete-buildpack");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class).getStatusCode();

	}

	@RequestMapping(value = "/v1/add-app-instance-data", method = RequestMethod.POST)
	public ResponseEntity<String> addAppInstance(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-add-app-instance");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/delete-app-instance-data", method = RequestMethod.POST)
	public HttpStatus DeleteApplication(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-delete-app-instance-data");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class).getStatusCode();
	}

	@RequestMapping(value = "/v1/create-space", method = RequestMethod.POST)
	public ResponseEntity<String> CreateSpace(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-create-space");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/create-organization", method = RequestMethod.POST)
	public ResponseEntity<String> CreateOrganization(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-create-organization");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/associate-user-with-org", method = RequestMethod.POST)
	public ResponseEntity<String> AssociateUserWithOrg(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-associate-user-with-org");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/v1/associate-auditor-with-org", method = RequestMethod.POST)
	public ResponseEntity<String> AssociateAuditorwithOrg(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-Associate-Auditor-with-Org");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/associate_auditor_with_the_org_by_username", method = RequestMethod.POST)
	public ResponseEntity<String> AssociateAuditorwithOrgByUserName(
			Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env
				.getProperty("url-Associate-Auditor-with-Org-by-user-name");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/associate_manager_with_the_org_by_username", method = RequestMethod.POST)
	public ResponseEntity<String> associateManagerwiththeOrgbyUsername(
			Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env
				.getProperty("url-associate-Manager-with-the-Orgby-Username");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/associate_billing_manager_with_the_organization_by_username", method = RequestMethod.POST)
	public ResponseEntity<String> associateBillingManagerwiththeOrgbyUsername(
			Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env
				.getProperty("url-associate-billing-Manager-with-the-Orgby-Username");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/delete-org", method = RequestMethod.POST)
	public HttpStatus DeleteOrg(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-delete-org");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class).getStatusCode();
	}

	@RequestMapping(value = "/v1/associate-user-with-space", method = RequestMethod.POST)
	public ResponseEntity<String> AssociateUserWithSpace(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-associate-user-with-space");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/associate-auditor-with-space", method = RequestMethod.POST)
	public ResponseEntity<String> AssociateAuditorWithSpace(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-associate-auditor-with-space");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/associate-auditor-with-space-by-username", method = RequestMethod.POST)
	public ResponseEntity<String> associateAuditorWithSpaceByUsername(
			Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env
				.getProperty("url-associate-auditor-with-space-by-username");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/associate-developer-with-space-by-username", method = RequestMethod.POST)
	public ResponseEntity<String> AssociatedeveloperWithSpace(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-associate-devloper-with-space");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/associate-manager-with-space-by-username", method = RequestMethod.POST)
	public ResponseEntity<String> AssociateManagerWithSpaceByUsername(
			Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env
				.getProperty("url-associate-manager-with-space-by-username");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/create-quota-definition", method = RequestMethod.POST)
	public ResponseEntity<String> CreateQuotaDefinition(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-create-quota-definition");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/update-quota-size-of-space", method = RequestMethod.POST)
	public ResponseEntity<String> UpdateQuotaSizeForSpace(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-update-quota-size-of-space");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/update-quota-size-of-org", method = RequestMethod.POST)
	public ResponseEntity<String> UpdateQuotaSizeForOrg(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-update-quota-size-of-org");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/create-user", method = RequestMethod.POST)
	public ResponseEntity<String> UserCreation(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-create-user");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/reset-password-of-user", method = RequestMethod.POST)
	public ResponseEntity<String> ResetPasswordOfUser(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-reset-password-of-user");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/delete-user", method = RequestMethod.POST)
	public HttpStatus DeleteUser(Model model, @RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-delete-user");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class).getStatusCode();

	}

	@RequestMapping(value = "/v1/fetch-user-information", method = RequestMethod.POST)
	public ResponseEntity<String> FetchUserInfoApp(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-fetch-user-information");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/update-service-broker", method = RequestMethod.POST)
	public ResponseEntity<String> UpdateServiceBroker(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		String url = env.getProperty("url-update-service-broker");
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/list-Organizations", method = RequestMethod.POST)
	public ResponseEntity<String> ListOrganizations(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-list-Organizations");

		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
		// return restTemplate.postForEntity(url, data, String.class);
		// return restTemplate.postForObject(url, data, String.class);
	}

	@RequestMapping(value = "/v1/list-spaces", method = RequestMethod.POST)
	public ResponseEntity<String> ListSpacesinOrgs(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-list-spaces");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	@RequestMapping(value = "/v1/list-users", method = RequestMethod.POST)
	public ResponseEntity<String> ListUsers(Model model,
			@RequestBody String data) {
		model.addAttribute("instanceInfo", instanceInfo);
		String url = env.getProperty("url-list-users");
		HttpEntity<String> requestEntity = new HttpEntity<>(data);
		return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}
}
