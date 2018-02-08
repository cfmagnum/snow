package com.acfapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.acfapi.entity.QuotaDefinition;
import com.acfapi.services.QuotaDefinitionService;

@RestController
@RequestMapping(value="/quota")
public class QuotaDefinitionMgmt {
	
	@Autowired
	private QuotaDefinitionService quotaDefinitionService;

	@RequestMapping(value = "/create_quota_definition", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	public void createQuotaDefinition(@RequestHeader(value="cliendId") String clientId, @RequestBody QuotaDefinition quotaDefinition){
		Boolean validQuotaName;
		Boolean validTotalServices;
		ResponseEntity<String> response;
		
		validQuotaName = validateQuotaName(quotaDefinition.getName());
		validTotalServices = validateTotalServices(quotaDefinition.getTotal_services());
		
		if(validQuotaName && validTotalServices){
		
			response = quotaDefinitionService.createQuotaDefinition(quotaDefinition,clientId);
		    System.out.println(response);
		}
		
		
	}

	private Boolean validateTotalServices(int total_services) {
		
		return true;
	}

	private boolean validateQuotaName(String name) {
	   
		return true;
	}
	
	
	
}
