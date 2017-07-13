package com.snow;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
	// private String orgurl
	// ="https://api.sys.eu.cfdev.canopy-cloud.com/v2/organizations";
	private String uaaUrl = "http://uaatokengenerator.apps.eu.cfdev.canopy-cloud.com/v1/get-UAA-token";
	RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/v1/ResetPassword", method = RequestMethod.POST)
	public ResponseEntity<String> ResetPassword(Model model,
			@RequestBody String json) throws FileNotFoundException, IOException {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();

		System.getProperties().put("http.proxyHost",
				"proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("http.proxyPort", "84");
		System.getProperties().put("https.proxyHost",
				"proxy-in.glb.my-it-solutions.net");
		System.getProperties().put("https.proxyPort", "84");

		String uaatoken = getUaaToken();

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> requestParams = mapper.readValue(json, Map.class);

		String userEmailId = (String) requestParams.get("userEmailId");

		String UaaId = getUserUaaId(userEmailId);
		System.out.println(UaaId);

		String oldPassword = (String) requestParams.get("oldPassword");
		String password = (String) requestParams.get("password");
		System.out.println(oldPassword + "\n" + password);

		String url = "https://uaa.sys.eu.cfdev.canopy-cloud.com/Users/" + UaaId
				+ "/password";
		headers.add("Authorization", uaatoken);

		headers.add("Content-Type", "application/json");
		// headers.add("Accept", "application/json");
		// headers.add("If-Match", "0");

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

		HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
		ResponseEntity<String> response = restTemplate.exchange(url,
				HttpMethod.POST, requestEntity, String.class);

		// ResponseEntity<String> response = restTemplate.exchange(url,
		// HttpMethod.PUT, requestEntity, String.class).getStatusCode();
		// return response;
		return restTemplate.exchange(url, HttpMethod.PUT, requestEntity,
				String.class);
	}

	public String getUaaToken() {
		String token = restTemplate.getForObject(uaaUrl, String.class);
		return token;
	}

	public String getUserUaaId(String userEmailId) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		String url = "https://uaa.sys.eu.cfdev.canopy-cloud.com/Users?filter=emails.value eq '"
				+ userEmailId + "'";

		String uaatoken = getUaaToken();
		String UaaId = "";
		JsonObject resources = new JsonObject();
		Gson gson = new GsonBuilder().create();
		JsonObject job = new JsonObject();
		headers.add("Authorization", uaatoken);
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
