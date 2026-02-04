package com.wemade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WemadeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WemadeApplication.class, args);
	}

}
