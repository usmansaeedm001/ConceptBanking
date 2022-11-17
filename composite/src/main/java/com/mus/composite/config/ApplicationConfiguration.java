package com.mus.composite.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Usman
 * @created 11/18/2022 - 3:08 AM
 * @project MyConceptBanking
 */
@Configuration
public class ApplicationConfiguration {


	@Value("${app.refresh.scope.property:default}")
	public String refreshScopeProperty;

	@Bean
	@LoadBalanced
	public WebClient.Builder loadBalancedWebClientBuilder() {
		return WebClient.builder();
	}

}
