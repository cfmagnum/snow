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
	private String url = "";
	private MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	private Map<String, Object> token = new HashMap<String, Object>();
	private RestTemplate restTemplate = new RestTemplate(); 
	private ObjectMapper mapper = new ObjectMapper();
	private Map<String, Object> requestParams;
	private JacksonJsonParser parser = new JacksonJsonParser();
    
	@Autowired(required = false)
	ApplicationInstanceInfo instanceInfo;

	@RequestMapping(value ="/v1/get-UAA-token", method = RequestMethod.POST)
	public String getAuthorizationToken(Model model,@RequestBody String data) throws JsonParseException, JsonMappingException, IOException {

		model.addAttribute("instanceInfo", instanceInfo);
		String json = "";
		String userName = "";
		String password = "";
		String clientName="";
		String authorizationKey ="";
		
		requestParams = mapper.readValue(data, Map.class);
		userName = (String) requestParams.get("userName");
		password = (String) requestParams.get("password");
		clientName = (String) requestParams.get("clientName");
		json= "grant_type=password" + "&username=" + userName +"&password="+ password;
		url= env.getProperty(clientName);
		headers.add("Authorization", env.getProperty("Authorization"));
		headers.add("cache-control", env.getProperty("cache-control"));
		headers.add("Content-Type", env.getProperty("Content-Type"));
		headers.add("x-uaa-endpoint", env.getProperty("UAA"));
		headers.add("Accept", env.getProperty("Accept"));
		headers.add("charset", env.getProperty("charset"));
		
		
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			protected boolean hasError(HttpStatus statusCode) {
				return false;
			}
		});

		HttpEntity<String> httpEntity = new HttpEntity<>(json, headers);
		System.out.println(httpEntity);
		try {
			skipSslValidation(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	
		token = parser.parseMap(restTemplate.exchange(url, HttpMethod.POST,
				httpEntity, String.class).getBody());

		authorizationKey = token.get("token_type") + " "
				+ token.get("access_token");

		return authorizationKey;

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
