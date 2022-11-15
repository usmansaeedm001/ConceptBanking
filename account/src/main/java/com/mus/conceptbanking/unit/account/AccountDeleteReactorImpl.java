package com.mus.conceptbanking.unit.account;

import com.mus.framework.annotation.DeleteReactor;
import com.mus.framework.enums.LayerType;
import com.mus.framework.exception.ApplicationUncheckException;
import com.mus.framework.handler.TrackCode;

/**
 * @author Usman
 * @created 10/16/2022 - 12:39 AM
 * @project OpenBanking
 */
@DeleteReactor
public class AccountDeleteReactorImpl implements AccountDeleteReactor {
	@Override
	public Boolean react(Account entity, TrackCode trackCode) throws ApplicationUncheckException {
		trackCode = trackCode.setLayerCode(LayerType.DELETE_REACTOR_LAYER);
		return true;
	}
}
