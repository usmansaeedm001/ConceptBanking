package com.mus.conceptbanking.unit.customer;

import com.mus.framework.exception.ApplicationUncheckException;
import com.mus.framework.face.DeleteReactor;
import com.mus.framework.handler.TrackCode;

/**
 * @author Usman
 * @created 10/16/2022 - 12:38 AM
 * @project OpenBanking
 */
public interface CustomerDeleteReactor extends DeleteReactor<Customer> {
	Boolean react(Customer entity, TrackCode trackCode) throws ApplicationUncheckException;
}
