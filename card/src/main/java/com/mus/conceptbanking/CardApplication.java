package com.mus.conceptbanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.mus")
public class CardApplication {
	public static void main(String[] args) {
		SpringApplication.run(CardApplication.class, args);
	}

}
