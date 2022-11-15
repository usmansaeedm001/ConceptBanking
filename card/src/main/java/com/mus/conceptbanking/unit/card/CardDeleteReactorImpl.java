package com.mus.conceptbanking.unit.card;

import com.mus.framework.annotation.DeleteReactor;
import com.mus.framework.enums.LayerType;
import com.mus.framework.exception.ApplicationUncheckException;
import com.mus.framework.handler.TrackCode;

/**
 * @author Usman
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
@DeleteReactor
public class CardDeleteReactorImpl implements CardDeleteReactor {
	@Override
	public Boolean react(Card entity, TrackCode trackCode) throws ApplicationUncheckException {
		trackCode = trackCode.setLayerCode(LayerType.DELETE_REACTOR_LAYER);
		return true;
	}
}
