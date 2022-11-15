package com.mus.composite.proxyunit.customer;

import com.mus.framework.face.IntegrationService;
import org.springframework.boot.actuate.health.Health;
import reactor.core.publisher.Mono;

/**
 * @author Usman
 * @created 10/30/2022 - 7:22 PM
 * @project MyConceptBanking
 */
public interface CustomerIntegrationService extends IntegrationService<CustomerDto> {
	Mono<Health> getHealth();
}