package com.snow;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bettercloud.vault.VaultException;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) throws VaultException {
		SpringApplication.run(Application.class, args);
		int num = 1;
    	int[] input = {-1,-2,1};
    	System.out.println(input);
    	Arrays.sort(input);
    	int smallest = input[0];
    	if( smallest<0){
    		num= 1;
    	}
    	if (smallest ==1){
    	  num = 1;
    	}
    	if(smallest >0){
    	 num = smallest -1;	
    	}	
  
        System.out.println(num);
	
	}
}
