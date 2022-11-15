package com.mus.conceptbanking.unit.account;

import com.mus.conceptbanking.enums.EntityType;
import com.mus.framework.enums.ApiType;
import com.mus.framework.enums.LayerType;
import com.mus.framework.enums.RequestType;
import com.mus.framework.handler.TrackCode;
import com.mus.framework.face.EntityValidator;

/**
 * @author Usman
 * @created 10/16/2022 - 12:39 AM
 * @project OpenBanking
 */
public interface AccountEntityValidator extends EntityValidator<AccountDto, Account> {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT).with(requestType).with(LayerType.ENTITY_VALIDATION_LAYER).with(EntityType.ACCOUNT.toString()).build();
	}
}