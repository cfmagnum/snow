package com.snow;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
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
	private Gson gson = new Gson();
	@Autowired(required = false)
	ApplicationInstanceInfo instanceInfo;

	@RequestMapping(value = "/v1/update-service-broker", method = RequestMethod.POST)
	public ResponseEntity<String> updateServiceBroker(Model model,
			@RequestBody String json) throws FileNotFoundException, IOException {
		model.addAttribute("instanceInfo", instanceInfo);
		System.getProperties().put("http.proxyHost",
				"proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("http.proxyPort", "84");
		System.getProperties().put("https.proxyHost",
				"proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("https.proxyPort", "84");

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		// Map<String, Object> uriVariables = new Hashmap<String, Object>();
		String name = "";
		String uaatoken = "";
		String guid = "";
		String url = "";
		String broker_url = "";
		String auth_username = "";
		String auth_password = "";
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> requestParams = mapper.readValue(json, Map.class);
		uaatoken = getUaaToken();
		name = (String) requestParams.get("name");
		broker_url = (String) requestParams.get("broker_url");
		auth_username = (String) requestParams.get("auth_username");
		auth_password = (String) requestParams.get("auth_password");
		guid = getServiceBrokerGuid(name);

		url = "https://api.sys.eu.cfdev.canopy-cloud.com/v2/service_brokers/"
				+ guid;
		headers.add("Authorization", uaatoken);
		headers.add("Content-Type", env.getProperty("Content-Type-json"));
		headers.add("Accept", env.getProperty("Host"));

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

		for (String key : requestParams.keySet()) {
			if (key != "name") {
				params.put(key, requestParams.get(key));
			}
		}
		System.out.println(params);
		String jsonData = gson.toJson(params);
		HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers);

		System.out.println(params);
		ResponseEntity<String> response = restTemplate.exchange(url,
				HttpMethod.PUT, requestEntity, String.class);
		return response;
	}

	public String getUaaToken() {
		String token = restTemplate.getForObject(env.getProperty("uaaUrl"),
				String.class);
		return token;
	}

	public String getServiceBrokerGuid(String name) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		String url = "https://api.sys.eu.cfdev.canopy-cloud.com/v2/service_brokers?q=name:"
				+ name;
		System.out.println(url);
		String uaatoken = getUaaToken();
		String guid = "";
		JsonObject resources = new JsonObject();
		Gson gson = new GsonBuilder().create();
		JsonObject job = new JsonObject();
		headers.add("Authorization", uaatoken);
		headers.add("Host", "api.sys.eu.cfdev.canopy-cloud.com");
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
			System.out.println(metadata);
			guid = metadata.get("guid").getAsString();
		}
		System.out.println(guid);
		return guid;
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