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

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ApiController {
	@Autowired
	private Environment env;
	private RestTemplate restTemplate = new RestTemplate();
	private ObjectMapper mapper = new ObjectMapper();
	@Autowired(required = false)
	ApplicationInstanceInfo instanceInfo;

	/**
	 * @param model
	 *            -to read vcap parameters to conncet with CF
	 * @param data
	 *            - parameters for post request
	 * @return -String
	 * @throws VaultException 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/v1/get-vault-secret", method = RequestMethod.GET)
	public String getVaultToken(Model model, @RequestBody String data) throws VaultException{
			
		model.addAttribute("instanceInfo", instanceInfo);

		final VaultConfig config = new VaultConfig().address("https://10.4.2.16:8200").token("e97c6601-2986-2acc-83ef-e5e134d6fe7e").build();
        
        final Map<String, String> secrets = new HashMap<String, String>();
        final Vault vault = new Vault(config);

       // System.out.println("connection done");
      
        // secrets.put("value", "world");
       // secrets.put("other_value", "You can store multiple name/value pairs under a single key");
          
        // Write operation
        //final LogicalResponse writeResponse = vault.logical().write("secret/hello", secrets);
                                               
        // Read operation
        final String value = vault.logical() .read("secret/aws-clients/acf-devtest/shared/automation/ui").getData().get("value");
        return value;

	}

	/**
	 * @param ConnectionURL
	 * @throws Exception
	 */
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
