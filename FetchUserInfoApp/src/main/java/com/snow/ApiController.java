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

@RestController
public class ApiController {
	@Autowired
	Environment env;
	
	RestTemplate restTemplate = new RestTemplate();
	@Autowired(required = false)
	ApplicationInstanceInfo instanceInfo;

	@RequestMapping(value = "/v1/fetch-user-information" , method = RequestMethod.POST)
	public ResponseEntity<String> getuserInfo(Model model,@RequestBody String data) throws JsonParseException, JsonMappingException, IOException {
		model.addAttribute("instanceInfo", instanceInfo);
		String url="";
	    String clientName="";
	    String authToken="";
	    ObjectMapper mapper = new ObjectMapper();
	    MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	    Map<String, Object> requestParams = mapper.readValue(data, Map.class);
	    authToken=(String) requestParams.get("authToken");
		clientName =(String) requestParams.get("clientName");
        url=env.getProperty("url-" +clientName);
		headers.add("Authorization", authToken);

		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			protected boolean hasError(HttpStatus statusCode) {
				return false;
			}
		});

		HttpEntity<String> requestEntity = new HttpEntity<>("Headers", headers);
		try {
			skipSslValidation(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				String.class);
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
