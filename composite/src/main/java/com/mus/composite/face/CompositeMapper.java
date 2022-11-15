package com.mus.composite.face;

/**
 * @author Usman
 * @created 11/7/2022 - 12:59 AM
 * @project MyConceptBanking
 */
public interface CompositeMapper<D, C> {
	D toAggregateDto(C composite);
	C toAggregate(D compositeDto);
}
