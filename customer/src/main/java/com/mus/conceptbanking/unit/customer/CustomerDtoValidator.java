package com.mus.conceptbanking.unit.customer;

import com.mus.framework.enums.ApiType;
import com.mus.framework.enums.LayerType;
import com.mus.framework.enums.RequestType;
import com.mus.framework.handler.TrackCode;
import com.mus.conceptbanking.enums.EntityType;
import com.mus.framework.face.DtoValidator;

/**
 * @author Usman
 * @created 10/16/2022 - 12:38 AM
 * @project OpenBanking
 */
public interface CustomerDtoValidator extends DtoValidator<CustomerCreateDto, CustomerSearchDto, CustomerUpdateDto, CustomerPartialUpdateDto> {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT).with(requestType).with(LayerType.DTO_VALIDATION_LAYER).with(EntityType.CUSTOMER.toString()).build();
	}

}