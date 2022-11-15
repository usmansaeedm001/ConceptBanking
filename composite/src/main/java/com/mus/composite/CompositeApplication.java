package com.mus.composite;

import com.mus.composite.composite.customer.CustomerCompositeAdapterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootApplication
@ComponentScan("com.mus")
public class CompositeApplication {
	public static void main(String[] args) {
		SpringApplication.run(CompositeApplication.class, args);
	}


	@Autowired CustomerCompositeAdapterImpl customerCompositeAdapter;

		@Bean
		ReactiveHealthContributor coreServices() {
			return customerCompositeAdapter.getHealthRegistry();
		}
}
