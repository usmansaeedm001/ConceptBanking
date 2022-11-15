package com.mus.conceptbanking.unit.account;

import com.mus.conceptbanking.enums.EntityType;
import com.mus.framework.enums.ApiType;
import com.mus.framework.enums.LayerType;
import com.mus.framework.enums.RequestType;
import com.mus.framework.handler.TrackCode;
import com.mus.framework.face.DataService;
import com.mus.framework.exception.ApplicationUncheckException;

import java.util.List;

/**
 * @author Usman
 * @created 10/16/2022 - 12:39 AM
 * @project OpenBanking
 */
public interface AccountDataService extends DataService<AccountDto> {
	default TrackCode trackCode(RequestType requestType) {
		return TrackCode.with(ApiType.UNIT).with(requestType).with(LayerType.DATA_SERVICE_LAYER).with(EntityType.ACCOUNT.toString()).build();
	}
	boolean existsBy(AccountDto by);
	List<AccountDto> getAllByCustomerUuid(String uuid);
	void deleteAllByCustomerUuid(String uuid) throws ApplicationUncheckException;

}