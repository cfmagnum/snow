package com.snow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class FetchUserInfoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FetchUserInfoAppApplication.class, args);
	}
}
