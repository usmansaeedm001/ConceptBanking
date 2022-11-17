package com.mus.composite;

import com.mus.composite.composite.customer.CustomerCompositeAdapterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@ComponentScan("com.mus")
@EnableEurekaClient
@RefreshScope
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
