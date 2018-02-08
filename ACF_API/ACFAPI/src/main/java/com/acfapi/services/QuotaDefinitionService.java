package com.acfapi.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.acfapi.entity.QuotaDefinition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class QuotaDefinitionService {
	@Autowired
	Environment env;
	
	public ResponseEntity<String> createQuotaDefinition(QuotaDefinition quotaDefinition, String clientId){
		RestTemplate restTemplate = new RestTemplate();
		String url;
		MultiValueMap<String, String> headers;
		String quotaDefJson;
		url = getURL(clientId);
		headers = getHeader();
		
		Gson gson = new GsonBuilder().create();
		quotaDefJson = gson.toJson(quotaDefinition);
		HttpEntity<String> requestEntity = new HttpEntity<String>(quotaDefJson, headers);
		
		ResponseEntity<String> response = restTemplate.exchange(url,
				HttpMethod.PUT, requestEntity, String.class);
		return response;
		
		
	}
	
	private MultiValueMap<String, String> getHeader(){
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		
		headers.add("Authorization", getAuthToken());
		headers.add("Content-Type", env.getProperty("Content-Type-json"));
		headers.add("Accept", env.getProperty("Host"));
		
		return headers;
	}
	
	private String getAuthToken(){
		
		return "bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiI4ZDM1MGJmZTQ5OTM0OWI3ODVlYWM0NTEyYzE2NjQ5MiIsInN1YiI6ImViZDg0MTFhLTU0NzktNDUwOC05NWE3LWM1ZmNhNDkzMTU0OCIsInNjb3BlIjpbInJvdXRpbmcucm91dGVyX2dyb3Vwcy5yZWFkIiwiY2xvdWRfY29udHJvbGxlci5yZWFkIiwicGFzc3dvcmQud3JpdGUiLCJjbG91ZF9jb250cm9sbGVyLndyaXRlIiwib3BlbmlkIiwicm91dGluZy5yb3V0ZXJfZ3JvdXBzLndyaXRlIiwiZG9wcGxlci5maXJlaG9zZSIsInNjaW0ud3JpdGUiLCJzY2ltLnJlYWQiLCJjbG91ZF9jb250cm9sbGVyLmFkbWluIiwidWFhLnVzZXIiXSwiY2xpZW50X2lkIjoiY2YiLCJjaWQiOiJjZiIsImF6cCI6ImNmIiwiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwidXNlcl9pZCI6ImViZDg0MTFhLTU0NzktNDUwOC05NWE3LWM1ZmNhNDkzMTU0OCIsIm9yaWdpbiI6InVhYSIsInVzZXJfbmFtZSI6ImFkbWluIiwiZW1haWwiOiJhZG1pbiIsInJldl9zaWciOiJjMzFkM2EwMyIsImlhdCI6MTUxNTY1Mzg0OCwiZXhwIjoxNTE1NjU1NjQ4LCJpc3MiOiJodHRwczovL3VhYS5zeXMuZXUuY2ZkZXYuY2Fub3B5LWNsb3VkLmNvbS9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJjbG91ZF9jb250cm9sbGVyIiwic2NpbSIsInBhc3N3b3JkIiwiY2YiLCJ1YWEiLCJvcGVuaWQiLCJkb3BwbGVyIiwicm91dGluZy5yb3V0ZXJfZ3JvdXBzIl19.O76r7GegKbtwwMWYUDPixq9MicZ92cyTzP9BKUhG7ZWqRcW8QaiuWJMDQqUNHiDbRE2bowZEEPKzoYaYoQwDrVL1zkBsyxhyD6BS0s4Y1MYB7_6mck4nDronUNWco7pcp5G72h0WSERihh7OxSMRkkTwwCgtN5EqO-EnR6yIYoOELOCXY8shrwuWOGA1RO6MptG1b3rF6kxCN9h3qCWKM0iea0qFtO7UNTx0PQZa6-lwNzBBDSufxgYgonjwoJBdTXbamGhU20ViElU04QTFQj_wK4K0jmNqgDA5tI59QpvpJo3a0DomOM5Gq_LB20vpvDhuvV9aNtHkJQDSxMy-Q_x__hXst1hrl3f6EuGW0DsTPHGfL7Z2HbcwZjLMVV5aqM6rfyf_kjtDZ0pfZK1QQhvcUYmQqalt15cZl7vOb7SxnvQdn1e-2Sj8fjOoAM0j0LTnAiyuI1bm4wASpOQataZu5r4WnCLiqtx7kquuusjcp_VXyFsnfcQQYZwZnyOrOe2Yyy7zLOc98Nt_aRvu9mIbu3_G41Ymxr6YNAFcbDdS_07dsoQSMLyt0xM1WpTN3NBrNmIbSwFwqgPsDLePaeaAeVSX71ONNTZBsFtTwzYMQ5gbozgMpOYTir0x9Xn5WUsLzqu7PCZK1NsFU8R-BBWYdlVQKEE9go5I08-BOEg";
		
	
	}
	private String getURL(String clientId){
		return env.getProperty("url-" + clientId);
	}
	
	

}
