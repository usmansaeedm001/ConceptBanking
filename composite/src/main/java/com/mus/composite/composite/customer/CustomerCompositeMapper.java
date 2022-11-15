package com.mus.composite.composite.customer;

import com.mus.composite.face.CompositeMapper;

/**
 * @author Usman
 * @created 10/31/2022 - 1:43 AM
 * @project MyConceptBanking
 */
public interface CustomerCompositeMapper extends CompositeMapper<CustomerCompositeDto, CustomerComposite> {
	CustomerCompositeDto toAggregateDto(CustomerComposite composite);
	CustomerComposite toAggregate(CustomerCompositeDto compositeDto);
}

