package com.mus.conceptbanking.unit.card;

import com.mus.conceptbanking.enums.EntityType;
import com.mus.framework.enums.ApiType;
import com.mus.framework.enums.LayerType;
import com.mus.framework.enums.RequestType;
import com.mus.framework.exception.ApplicationUncheckException;
import com.mus.framework.handler.TrackCode;
import com.mus.framework.face.IService;

import java.util.List;

/**
 * @author Usman
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
public interface CardService extends IService<CardDto, CardCreateDto, CardSearchDto, CardUpdateDto, CardPartialUpdateDto> {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT).with(requestType).with(LayerType.SERVICE_UNIT_LAYER)
			//todo: create EntityType enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and instance with name CARD
			.with(EntityType.CARD.toString()).build();
	}
	List<CardDto> getAllByCustomerUuid(String uuid);
	void deleteAllByCustomerUuid(String uuid) throws ApplicationUncheckException;
	List<CardDto> getAllByAccountUuid(String uuid);
	void deleteAllByAccountUuid(String uuid) throws ApplicationUncheckException;

}