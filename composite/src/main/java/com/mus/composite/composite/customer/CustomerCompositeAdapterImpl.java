package com.mus.composite.composite.customer;

import com.mus.composite.enums.EntityType;
import com.mus.composite.enums.ErrorCode;
import com.mus.composite.proxyunit.account.AccountIntegrationService;
import com.mus.composite.proxyunit.card.CardIntegrationService;
import com.mus.composite.proxyunit.customer.CustomerIntegrationService;
import com.mus.framework.annotation.AggregateDataService;
import com.mus.framework.dto.EnumerationWrapper;
import com.mus.framework.enums.ApiType;
import com.mus.framework.enums.LayerType;
import com.mus.framework.enums.RequestType;
import com.mus.framework.enums.ServiceType;
import com.mus.framework.exception.BusinessValidationException;
import com.mus.framework.handler.TrackCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author Usman
 * @created 10/31/2022 - 1:43 AM
 * @project MyConceptBanking
 */
@AggregateDataService
@Slf4j
public class CustomerCompositeAdapterImpl implements CustomerCompositeAdapter {
	@Autowired private CustomerCompositeMapper aggregateMapper;
	@Autowired private CustomerIntegrationService customerIntegrationService;
	@Autowired private AccountIntegrationService accountIntegrationService;
	@Autowired private CardIntegrationService cardIntegrationService;

	@Override
	public Mono<CustomerCompositeDto> get(String uuid) {
		log.debug("Going to get Customer, Account, card for customer uuid [{}]", uuid);
		TrackCode trackCode = TrackCode.with(ServiceType.NOOP).with(ApiType.COMPOSITE).with(RequestType.GET).with(LayerType.COMPOSITE_ADAPTER_LAYER)
			.with(EntityType.CUSTOMER).build();
		return Mono.zip(customerIntegrationService.get(uuid), accountIntegrationService.getAllByCustomerUuid(uuid).collectList(),
				cardIntegrationService.getAllByCustomerUuid(uuid).collectList())
			.map(objects -> new CustomerComposite(objects.getT1(), objects.getT2(), objects.getT3()))
			.map(composite -> aggregateMapper.toAggregateDto(composite)).doOnError(throwable -> {
				log.error("Error occurred while aggregating data against customer against uuid [{}]", uuid);
				throw new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.INTEGRATION_FAILED), trackCode, throwable.getMessage());
			}).log(log.getName(), Level.FINE);

	}

	@Override
	public ReactiveHealthContributor getHealthRegistry() {

		final Map<String, ReactiveHealthIndicator> registry = new LinkedHashMap<>();

		registry.put("customer", () -> customerIntegrationService.getHealth());
		registry.put("account", () -> accountIntegrationService.getHealth());
		registry.put("card", () -> cardIntegrationService.getHealth());

		return CompositeReactiveHealthContributor.fromMap(registry);
	}


}
