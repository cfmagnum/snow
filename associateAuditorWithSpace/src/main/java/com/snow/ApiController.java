package com.snow;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

@RestController
public class ApiController {

	@Autowired
	Environment env;

	RestTemplate restTemplate = new RestTemplate();

	@Autowired(required = false)
	ApplicationInstanceInfo instanceInfo;

	@RequestMapping(value = "v1/associate-auditor-with-space", method = RequestMethod.POST)
	public ResponseEntity<String> associateUserWithSpace(Model model,
			@RequestBody String json) throws JsonParseException,
			JsonMappingException, IOException {
		model.addAttribute("instanceInfo", instanceInfo);
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> requestParams = mapper.readValue(json, Map.class);
		System.getProperties().put("http.proxyHost",
				"proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("http.proxyPort", "84");
		System.getProperties().put("https.proxyHost",
				"proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("https.proxyPort", "84");
		String orgName = "";
		String orgGuid = "";
		String userEmailId = "";
		String uaaId = "";
		String url = "";
		String authToken = "";
		String clientName = "";
		String host = "";
		String spaceGuid = "";
		String spaceName = "";

		authToken = (String) requestParams.get("authToken");
		clientName = (String) requestParams.get("clientName");
		host = env.getProperty("Host-" + clientName);
		orgName = (String) requestParams.get("organizationName");
		System.out.println(orgName);

		orgGuid = getOrgGuid(orgName, authToken, host, clientName);
		System.out.println(orgGuid);
		userEmailId = (String) requestParams.get("userEmailId");
		System.out.println(userEmailId);
		spaceName = (String) requestParams.get("spaceName");

		System.out.println(spaceName);
		spaceGuid = getSpaceGuid(spaceName, orgGuid, authToken, host,
				clientName);
		System.out.println(spaceGuid);
		uaaId = getUserUaaId(userEmailId, authToken, clientName);
		System.out.println(uaaId);

		url = env.getProperty("url-spaces-" + clientName) + "/" + spaceGuid
				+ "/auditors/" + uaaId;

		headers.add("Authorization", authToken);
		headers.add("Content-Type", env.getProperty("Content-Type-json"));
		headers.add("Host", env.getProperty("Host-" + clientName));

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
		HttpEntity<String> requestEntity = new HttpEntity<>("Headers", headers);
		System.out.println(url);
		ResponseEntity<String> response = restTemplate.exchange(url,
				HttpMethod.PUT, requestEntity, String.class);
		return response;
	}

	public String getOrgGuid(String orgName, String authToken, String host,
			String clientName) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		String urlForId = env.getProperty("url-" + clientName) + "?q=name:"
				+ orgName;

		String orgId = "";
		JsonObject resources = new JsonObject();
		Gson gson = new GsonBuilder().create();
		JsonObject job = new JsonObject();
		headers.add("Authorization", authToken);
		headers.add("Host", host);
		try {
			skipSslValidation(urlForId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpEntity<String> requestEntity = new HttpEntity<>("Headers", headers);
		String orgInfo = restTemplate.exchange(urlForId, HttpMethod.GET,
				requestEntity, String.class).getBody();
		try {
			job = gson.fromJson(orgInfo, JsonObject.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (job != null) {
			if (job.getAsJsonArray("resources") != null) {
				resources = job.getAsJsonArray("resources").get(0)
						.getAsJsonObject();
			}
			JsonObject metadata = resources.get("metadata").getAsJsonObject();
			orgId = metadata.get("guid").getAsString();
		}
		return orgId;
	}

	public String getSpaceGuid(String spaceName, String orgGuid,
			String authToken, String host, String clientName) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		String url = env.getProperty("url-" + clientName) + "/" + orgGuid
				+ "/spaces" + "?q=name:" + spaceName;

		String guid = "";
		JsonObject resources = new JsonObject();
		Gson gson = new GsonBuilder().create();
		JsonObject job = new JsonObject();
		headers.add("Authorization", authToken);
		headers.add("Host", host);
		try {
			skipSslValidation(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpEntity<String> requestEntity = new HttpEntity<>("Headers", headers);
		String orgInfo = restTemplate.exchange(url, HttpMethod.GET,
				requestEntity, String.class).getBody();
		try {
			job = gson.fromJson(orgInfo, JsonObject.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (job != null) {
			if (job.getAsJsonArray("resources") != null) {
				resources = job.getAsJsonArray("resources").get(0)
						.getAsJsonObject();
			}

			JsonObject metadata = resources.get("metadata").getAsJsonObject();
			guid = metadata.get("guid").getAsString();
		}

		return guid;
	}

	public String getUserUaaId(String userEmailId, String authToken,
			String clientName) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		String url = env.getProperty("url-users-" + clientName)
				+ "?filter=emails.value eq '" + userEmailId + "'";

		String UaaId = "";
		JsonObject resources = new JsonObject();
		Gson gson = new GsonBuilder().create();
		JsonObject job = new JsonObject();
		headers.add("Authorization", authToken);
		headers.add("content-type", "application/json");
		headers.add("Accept", "application/json");
		try {
			skipSslValidation(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpEntity<String> requestEntity = new HttpEntity<>("Headers", headers);
		String userinfo = restTemplate.exchange(url, HttpMethod.GET,
				requestEntity, String.class).getBody();
		try {
			job = gson.fromJson(userinfo, JsonObject.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (job != null) {
			if (job.getAsJsonArray("resources") != null) {
				resources = job.getAsJsonArray("resources").get(0)
						.getAsJsonObject();
				UaaId = resources.get("id").getAsString();
			}
		}
		return UaaId;
	}

	public void skipSslValidation(String ConnectionURL) throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
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
