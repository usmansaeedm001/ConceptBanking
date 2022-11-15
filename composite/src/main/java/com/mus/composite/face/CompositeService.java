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
import org.springframework.data.domain.Page;

/**
 * @author Usman
 * @created 11/7/2022 - 12:56 AM
 * @project MyConceptBanking
 */
public interface CompositeService<D> {
	default <S> Page<D> search(S searchDto, int pageNo, int pageSize) {
		TrackCode trackCode = TrackCode.with(ServiceType.COMPOSITE_SERVICE).with(ApiType.COMPOSITE).with(RequestType.SEARCH)
			.with(LayerType.COMPOSITE_SERVICE_LAYER).with(EntityType.CUSTOMER).build();
		throw new ApplicationException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, "Not Implemented yet.");
	}
	default D get(String uuid) {
		TrackCode trackCode = TrackCode.with(ServiceType.COMPOSITE_SERVICE).with(ApiType.COMPOSITE).with(RequestType.GET)
			.with(LayerType.COMPOSITE_SERVICE_LAYER).with(EntityType.CUSTOMER).build();
		throw new ApplicationException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, "Not Implemented yet.");
	}
	default <C> D save(C createDto) throws ApplicationUncheckException {
		TrackCode trackCode = TrackCode.with(ServiceType.COMPOSITE_SERVICE).with(ApiType.COMPOSITE).with(RequestType.POST)
			.with(LayerType.COMPOSITE_SERVICE_LAYER).with(EntityType.CUSTOMER).build();
		throw new ApplicationException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, "Not Implemented yet.");
	}
	default <U> D update(String uuid, U updateDto) throws ApplicationUncheckException {
		TrackCode trackCode = TrackCode.with(ServiceType.COMPOSITE_SERVICE).with(ApiType.COMPOSITE).with(RequestType.PUT)
			.with(LayerType.COMPOSITE_SERVICE_LAYER).with(EntityType.CUSTOMER).build();
		throw new ApplicationException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, "Not Implemented yet.");
	}
	default <P> D partialUpdate(P partialUpdateDto) throws ApplicationUncheckException {
		TrackCode trackCode = TrackCode.with(ServiceType.COMPOSITE_SERVICE).with(ApiType.COMPOSITE).with(RequestType.PATCH)
			.with(LayerType.COMPOSITE_SERVICE_LAYER).with(EntityType.CUSTOMER).build();
		throw new ApplicationException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, "Not Implemented yet.");
	}
	default void delete(String uuid) throws ApplicationUncheckException {
		TrackCode trackCode = TrackCode.with(ServiceType.COMPOSITE_SERVICE).with(ApiType.COMPOSITE).with(RequestType.DELETE)
			.with(LayerType.COMPOSITE_SERVICE_LAYER).with(EntityType.CUSTOMER).build();
		throw new ApplicationException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, "Not Implemented yet.");
	}
}
