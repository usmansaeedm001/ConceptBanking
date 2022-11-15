package com.mus.conceptbanking.unit.card;

import com.mus.framework.annotation.ChangeReactor;
import com.mus.framework.enums.LayerType;
import com.mus.framework.exception.ApplicationUncheckException;
import com.mus.framework.handler.TrackCode;

/**
 * @author Usman
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
@ChangeReactor
public class CardChangeReactorImpl implements CardChangeReactor {
	@Override
	public Boolean react(CardDto dto, Card entity, TrackCode trackCode) throws ApplicationUncheckException {
		trackCode = trackCode.setLayerCode(LayerType.CHANGE_REACTOR_LAYER);
		return continueReaction(null, null, trackCode);
	}

	private Boolean continueReaction(CardStatus currentStatus, CardStatus previousStatus, TrackCode trackCode) throws ApplicationUncheckException {
		return true;
	}
}
