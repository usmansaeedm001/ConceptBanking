package com.mus.composite.composite.customer;

import com.mus.framework.annotation.AggregateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

/**
 * @author Usman
 * @created 10/31/2022 - 1:43 AM
 * @project MyConceptBanking
 */
@Slf4j
@AggregateService
public class CustomerCompositeServiceImpl implements CustomerCompositeService {
	@Autowired private CustomerCompositeAdapter customerCompositeAdapter;

	@Override
	public Mono<CustomerCompositeDto> get(String uuid) {
		log.info("Getting customer mono for customer uuid [{}]", uuid);
		return customerCompositeAdapter.get(uuid);
	}


}
