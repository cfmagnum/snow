package com.snow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class AddAppInstanceDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(AddAppInstanceDataApplication.class, args);
	}
}
