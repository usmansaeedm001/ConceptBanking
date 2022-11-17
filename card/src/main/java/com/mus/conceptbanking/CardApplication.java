package com.mus.conceptbanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.mus")
@EnableEurekaClient
public class CardApplication {
	public static void main(String[] args) {
		SpringApplication.run(CardApplication.class, args);
	}

}
