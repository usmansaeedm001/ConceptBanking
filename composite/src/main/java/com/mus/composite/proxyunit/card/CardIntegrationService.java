package com.mus.composite.proxyunit.card;

import com.mus.framework.face.IntegrationService;
import org.springframework.boot.actuate.health.Health;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Usman
 * @created 10/31/2022 - 1:10 AM
 * @project MyConceptBanking
 */
public interface CardIntegrationService extends IntegrationService<CardDto> {
	Flux<CardDto> getAllByCustomerUuid(String uuid);
	void deleteAllByCustomerUuid(String uuid);
	Mono<Health> getHealth();
}