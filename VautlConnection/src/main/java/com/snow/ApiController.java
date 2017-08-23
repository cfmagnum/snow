package com.snow;

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
import org.springframework.boot.json.JacksonJsonParser;
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

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.google.gson.JsonObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

@RestController
public class ApiController {
	@Autowired
	private Environment env;
	private RestTemplate restTemplate = new RestTemplate();
	private ObjectMapper mapper = new ObjectMapper();
	@Autowired(required = false)
	ApplicationInstanceInfo instanceInfo;
	
	@RequestMapping(value = "/v1/get-vault-secret", method = RequestMethod.POST)
	public JsonObject getVaultSecret(Model model, @RequestBody String data) throws JsonParseException, JsonMappingException, IOException
		 {
		model.addAttribute("instanceInfo", instanceInfo);

		String url = "";
		String vaultToken=""; 
		Gson gson = new GsonBuilder().create();
		JsonObject jsonResponse = new JsonObject();
		JsonObject resources = new JsonObject();
		JsonObject passwordData = new JsonObject();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> requestParams = mapper.readValue(data, Map.class);
		String clientName = (String) requestParams.get("clientName");
		vaultToken = env.getProperty("vault-token");
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		url = env.getProperty("vault-url-" + clientName);
		
		headers.add("X-Vault-Token", vaultToken);
		
		HttpEntity<String> requestEntity = new HttpEntity<>("Headers", headers);

		try {
			skipSslValidation(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				String.class).getBody();
		try {
			if(response !=null){
				jsonResponse = gson.fromJson(response, JsonObject.class);
			}
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (jsonResponse != null) {
			if (jsonResponse.getAsJsonObject("data") != null) {
				passwordData = jsonResponse.getAsJsonObject("data");
				//passwordData = jsonResponse.getAsJsonArray("data").get(0)
					//	.getAsJsonObject();
			}
		}	
		//String secret = passwordData.get("password").getAsString();
			
		return passwordData;

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
