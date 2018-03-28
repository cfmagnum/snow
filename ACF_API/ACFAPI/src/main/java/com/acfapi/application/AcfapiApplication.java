package com.acfapi.application;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class AcfapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcfapiApplication.class, args);
	}
}