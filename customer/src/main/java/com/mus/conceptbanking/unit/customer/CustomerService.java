package com.mus.conceptbanking.unit.customer;

import com.mus.conceptbanking.enums.EntityType;
import com.mus.framework.enums.ApiType;
import com.mus.framework.enums.LayerType;
import com.mus.framework.enums.RequestType;
import com.mus.framework.handler.TrackCode;
import com.mus.framework.face.IService;
import reactor.core.publisher.Mono;

/**
 * @author Usman
 * @created 10/16/2022 - 12:38 AM
 * @project OpenBanking
 */
public interface CustomerService extends IService<CustomerDto, CustomerCreateDto, CustomerSearchDto, CustomerUpdateDto, CustomerPartialUpdateDto> {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT).with(requestType).with(LayerType.SERVICE_UNIT_LAYER)
			//todo: create EntityType enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and instance with name CUSTOMER
			.with(EntityType.CUSTOMER.toString()).build();
	}
	Mono<CustomerDto> getMono(String uuid);
}