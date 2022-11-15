package com.mus.composite.face;

import com.mus.composite.enums.EntityType;
import com.mus.composite.enums.ErrorCode;
import com.mus.framework.dto.EnumerationWrapper;
import com.mus.framework.enums.ApiType;
import com.mus.framework.enums.LayerType;
import com.mus.framework.enums.RequestType;
import com.mus.framework.enums.ServiceType;
import com.mus.framework.exception.ApplicationException;
import com.mus.framework.exception.ApplicationUncheckException;
import com.mus.framework.handler.TrackCode;

/**
 * @author Usman
 * @created 11/7/2022 - 12:56 AM
 * @project MyConceptBanking
 */
public interface DeleteService {
	default void delete(String uuid) throws ApplicationUncheckException {
		TrackCode trackCode = TrackCode.with(ServiceType.COMPOSITE_SERVICE).with(ApiType.COMPOSITE).with(RequestType.DELETE)
			.with(LayerType.COMPOSITE_SERVICE_LAYER).with(EntityType.CUSTOMER).build();
		throw new ApplicationException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, "Not Implemented yet.");
	}
}
