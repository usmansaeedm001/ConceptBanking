package com.mus.conceptbanking.unit.account;

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
 * @created 10/16/2022 - 12:39 AM
 * @project OpenBanking
 */
public interface AccountService extends IService<AccountDto, AccountCreateDto, AccountSearchDto, AccountUpdateDto, AccountPartialUpdateDto> {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT).with(requestType).with(LayerType.SERVICE_UNIT_LAYER)
			//todo: create EntityType enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and instance with name ACCOUNT
			.with(EntityType.ACCOUNT.toString()).build();
	}
	List<AccountDto> getAllByCustomerUuid(String uuid);
	void deleteAllByCustomerUuid(String uuid) throws ApplicationUncheckException;

}