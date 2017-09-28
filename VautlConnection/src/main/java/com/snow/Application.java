package com.snow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bettercloud.vault.VaultException;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) throws VaultException {
		SpringApplication.run(Application.class, args);
	
	}
}
