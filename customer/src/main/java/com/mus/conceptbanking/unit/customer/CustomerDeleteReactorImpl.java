package com.mus.conceptbanking.unit.customer;

import com.mus.framework.annotation.DeleteReactor;
import com.mus.framework.enums.LayerType;
import com.mus.framework.exception.ApplicationUncheckException;
import com.mus.framework.handler.TrackCode;

/**
 * @author Usman
 * @created 10/16/2022 - 12:38 AM
 * @project OpenBanking
 */
@DeleteReactor
public class CustomerDeleteReactorImpl implements CustomerDeleteReactor {
	@Override
	public Boolean react(Customer entity, TrackCode trackCode) throws ApplicationUncheckException {
		trackCode = trackCode.setLayerCode(LayerType.DELETE_REACTOR_LAYER);
		return true;
	}
}
