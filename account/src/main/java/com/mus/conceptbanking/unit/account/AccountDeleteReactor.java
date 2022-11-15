package com.mus.conceptbanking.unit.account;

import com.mus.framework.exception.ApplicationUncheckException;
import com.mus.framework.face.DeleteReactor;
import com.mus.framework.handler.TrackCode;

/**
 * @author Usman
 * @created 10/16/2022 - 12:39 AM
 * @project OpenBanking
 */
public interface AccountDeleteReactor extends DeleteReactor<Account> {
	Boolean react(Account entity, TrackCode trackCode) throws ApplicationUncheckException;
}
