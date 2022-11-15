package com.mus.conceptbanking.unit.customer;

import com.mus.conceptbanking.enums.EntityType;
import com.mus.framework.enums.ApiType;
import com.mus.framework.enums.LayerType;
import com.mus.framework.enums.RequestType;
import com.mus.framework.handler.TrackCode;
import com.mus.framework.face.DataService;

/**
 * @author Usman
 * @created 10/16/2022 - 12:38 AM
 * @project OpenBanking
 */
public interface CustomerDataService extends DataService<CustomerDto> {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT).with(requestType).with(LayerType.DATA_SERVICE_LAYER).with(EntityType.CUSTOMER.toString()).build();
	}
	boolean existsBy(CustomerDto by);

}