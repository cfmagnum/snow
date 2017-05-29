package com.snow;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class CreateQuotaDefinitionApplication {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		SpringApplication.run(CreateQuotaDefinitionApplication.class, args);
		
	}
}
