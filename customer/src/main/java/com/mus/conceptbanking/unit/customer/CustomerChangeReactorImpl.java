package com.mus.conceptbanking.unit.customer;

import com.mus.framework.annotation.ChangeReactor;
import com.mus.framework.enums.LayerType;
import com.mus.framework.exception.ApplicationUncheckException;
import com.mus.framework.handler.TrackCode;

/**
 * @author Usman
 * @created 10/16/2022 - 12:38 AM
 * @project OpenBanking
 */
@ChangeReactor
public class CustomerChangeReactorImpl implements CustomerChangeReactor {
	@Override
	public Boolean react(CustomerDto dto, Customer entity, TrackCode trackCode) throws ApplicationUncheckException {
		trackCode = trackCode.setLayerCode(LayerType.CHANGE_REACTOR_LAYER);
		return continueReaction(null, null, trackCode);
	}

	private Boolean continueReaction(CustomerStatus currentStatus, CustomerStatus previousStatus, TrackCode trackCode) throws ApplicationUncheckException {
		return true;
	}
}
