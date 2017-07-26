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

	@RequestMapping(value = "/v1/list-spaces", method = RequestMethod.POST)
	public ResponseEntity<String> associateUserWithOrg(Model model,
			@RequestBody String data) throws JsonParseException,
			JsonMappingException, IOException {
		model.addAttribute("instanceInfo", instanceInfo);
	
		String url="";
	    String clientName="";
	    String authToken="";
	    String orgName ="";
	    String orgGuid="";
	    String host ="";
	    ObjectMapper mapper = new ObjectMapper();
	    MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	    Map<String, Object> requestParams = mapper.readValue(data, Map.class);
	    authToken=(String) requestParams.get("authToken");
		clientName =(String) requestParams.get("clientName");
		host=env.getProperty("Host-"+clientName);
		

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(
				requestParams, headers);

		orgName = (String) requestParams.get("organizationName");
		orgGuid = getOrgGuid(orgName,authToken,host,clientName);
		

		url = env.getProperty("url-" +clientName) + "/" + orgGuid + "/spaces";
		headers.add("Authorization", authToken);
		headers.add("Content-Type", env.getProperty("Content-Type-json"));
		headers.add("Accept", host);

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

		ResponseEntity<String> response = restTemplate.exchange(url,
				HttpMethod.GET, requestEntity, String.class);
		return response;
	}

	public String getUaaToken() {
		String token = restTemplate.getForObject(env.getProperty("uaaUrl"),
				String.class);
		return token;
	}

	public String getOrgGuid(String orgName,String authToken,String host, String clientName) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		String urlForId = env.getProperty("url-" +clientName)+ "?q=name:" + orgName;
		
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
