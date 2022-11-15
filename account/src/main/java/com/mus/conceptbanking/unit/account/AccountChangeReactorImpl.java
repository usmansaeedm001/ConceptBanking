package com.mus.conceptbanking.unit.account;

import com.mus.framework.annotation.ChangeReactor;
import com.mus.framework.enums.LayerType;
import com.mus.framework.exception.ApplicationUncheckException;
import com.mus.framework.handler.TrackCode;

/**
 * @author Usman
 * @created 10/16/2022 - 12:39 AM
 * @project OpenBanking
 */
@ChangeReactor
public class AccountChangeReactorImpl implements AccountChangeReactor {
	@Override
	public Boolean react(AccountDto dto, Account entity, TrackCode trackCode) throws ApplicationUncheckException {
		trackCode = trackCode.setLayerCode(LayerType.CHANGE_REACTOR_LAYER);
		return continueReaction(null, null, trackCode);
	}

	private Boolean continueReaction(AccountStatus currentStatus, AccountStatus previousStatus, TrackCode trackCode) throws ApplicationUncheckException {
		return true;
	}
}
