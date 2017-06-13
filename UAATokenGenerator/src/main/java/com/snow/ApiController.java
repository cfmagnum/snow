package com.snow;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;


@RestController
public class ApiController {
   
   private String url = "https://login.sys.eu.cfdev.canopy-cloud.com/oauth/token";
   private MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
   private Map<String, Object> token = new HashMap<String, Object>();
   private String json ="grant_type=password&username=admin&password=JioovG*jCadGect37mkyuur9kyat$"; 
   RestTemplate restTemplate = new RestTemplate();
   
   @Autowired(required = false) ApplicationInstanceInfo instanceInfo;
   
   @RequestMapping("/v1/get-UAA-token")
   public String getAuthorizationToken(Model model) {
	   model.addAttribute("instanceInfo", instanceInfo);
	    headers.add("Authorization", "Basic Y2Y6");
	    headers.add("cache-control", "no-cache");
	    headers.add("Content-Type", "application/x-www-form-urlencoded");
	    headers.add("x-uaa-endpoint", "uaa.sys.eu.cfdev.canopy-cloud.com");
	    headers.add("accept", "application/json");
	    headers.add("charset", "utf-8");
	    restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			protected boolean hasError(HttpStatus statusCode) {
				return false;
			}
		});	
	  
	   /* HttpTransportProperties.ProxyProperties proxyProperties = new HttpTransportProperties.ProxyProperties();
	    proxyProperties.setDomain("ww930");
	    proxyProperties.setProxyName("proxy-de.glb.my-it-solutions.net");
	    proxyProperties.setProxyPort(84);
	    configurationContext.setProperty(HTTPConstants.PROXY, proxyProperties);
	    */
	    
	    HttpEntity<String> httpEntity = new HttpEntity<>(json, headers);
	    try {
			skipSslValidation(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	   
	    JacksonJsonParser parser = new JacksonJsonParser();
		token = parser.parseMap(restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class).getBody());
		
		String authorizationKey = token.get("token_type") + " " + token.get("access_token");
		
		return authorizationKey;
		
	    
    }
   public void skipSslValidation(String ConnectionURL) throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
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
