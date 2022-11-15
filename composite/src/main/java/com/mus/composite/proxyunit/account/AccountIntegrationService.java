package com.mus.composite.proxyunit.account;

import com.mus.framework.face.IntegrationService;
import org.springframework.boot.actuate.health.Health;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Usman
 * @created 10/31/2022 - 1:39 AM
 * @project MyConceptBanking
 */
public interface AccountIntegrationService extends IntegrationService<AccountDto> {
	Flux<AccountDto> getAllByCustomerUuid(String uuid);
	void deleteAllByCustomerUuid(String uuid);
	Mono<Health> getHealth();
}