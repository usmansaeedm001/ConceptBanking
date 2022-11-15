package com.mus.conceptbanking.unit.card;

import com.mus.framework.face.DtoMapper;
import com.mus.framework.enums.ApiType;
import com.mus.framework.enums.LayerType;
import com.mus.framework.enums.RequestType;
import com.mus.framework.handler.TrackCode;
import com.mus.conceptbanking.enums.EntityType;

/**
 * @author Usman
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
public interface CardDtoMapper extends DtoMapper<CardDto, CardCreateDto, CardSearchDto, CardUpdateDto, CardPartialUpdateDto> {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT).with(requestType).with(LayerType.DTO_VALIDATION_LAYER).with(EntityType.CARD.toString()).build();
	}

}