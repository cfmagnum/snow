package com.snow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class DeleteBuildpackApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeleteBuildpackApplication.class, args);
		ApiController ob = new ApiController();
		ob.getBuildpackGuid("test");
	}
}
