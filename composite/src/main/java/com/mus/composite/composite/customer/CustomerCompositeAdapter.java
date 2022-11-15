package com.mus.composite.composite.customer;

import com.mus.composite.face.CompositeAdapter;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;

/**
 * @author Usman
 * @created 10/31/2022 - 1:43 AM
 * @project MyConceptBanking
 */
public interface CustomerCompositeAdapter extends CompositeAdapter<CustomerCompositeDto> {
	ReactiveHealthContributor getHealthRegistry();
}