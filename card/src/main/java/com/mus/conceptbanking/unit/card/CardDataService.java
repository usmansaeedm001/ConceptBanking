package com.mus.conceptbanking.unit.card;

import com.mus.conceptbanking.enums.EntityType;
import com.mus.framework.enums.ApiType;
import com.mus.framework.enums.LayerType;
import com.mus.framework.enums.RequestType;
import com.mus.framework.handler.TrackCode;
import com.mus.framework.face.DataService;
import com.mus.framework.exception.ApplicationUncheckException;

import java.util.List;

/**
 * @author Usman
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
public interface CardDataService extends DataService<CardDto> {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT).with(requestType).with(LayerType.DATA_SERVICE_LAYER).with(EntityType.CARD.toString()).build();
	}
	boolean existsBy(CardDto by);
	List<CardDto> getAllByCustomerUuid(String uuid);
	void deleteAllByCustomerUuid(String uuid) throws ApplicationUncheckException;
	List<CardDto> getAllByAccountUuid(String uuid);
	void deleteAllByAccountUuid(String uuid) throws ApplicationUncheckException;

}