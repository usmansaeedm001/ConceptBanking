package com.mus.conceptbanking.unit.card;

import com.mus.framework.enums.ApiType;
import com.mus.framework.enums.LayerType;
import com.mus.framework.enums.RequestType;
import com.mus.framework.handler.TrackCode;
import com.mus.conceptbanking.enums.EntityType;
import com.mus.framework.face.EntityMapper;

/**
 * @author Usman
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
public interface CardMapper extends EntityMapper<CardDto, Card> {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT).with(requestType).with(LayerType.MAPPER_LAYER).with(EntityType.CARD.toString()).build();
	}
}