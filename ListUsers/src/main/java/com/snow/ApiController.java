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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ApiController {
	@Autowired
	Environment env;

	 

	private RestTemplate restTemplate = new RestTemplate();
	@Autowired(required = false)
	ApplicationInstanceInfo instanceInfo;

	@RequestMapping(value = "/v1/list-users", method = RequestMethod.POST)
	public ResponseEntity<String> getUsers(Model model,@RequestBody String data)
			throws FileNotFoundException, IOException {
		model.addAttribute("instanceInfo", instanceInfo);
        
		String url="";
		String authToken="";
		String clientName="";
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> requestParams;
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		requestParams = mapper.readValue(data, Map.class);
		authToken= (String) requestParams.get("authToken");
		clientName=(String) requestParams.get("clientName");
		
		url=env.getProperty("url-" +clientName);
		System.out.println(url);
		headers.add("Authorization", authToken);
		headers.add("Accept", env.getProperty("Content-Type-json"));
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
