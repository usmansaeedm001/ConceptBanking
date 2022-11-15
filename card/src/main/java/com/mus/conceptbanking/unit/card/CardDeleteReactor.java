package com.mus.conceptbanking.unit.card;

import com.mus.framework.exception.ApplicationUncheckException;
import com.mus.framework.face.DeleteReactor;
import com.mus.framework.handler.TrackCode;

/**
 * @author Usman
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
public interface CardDeleteReactor extends DeleteReactor<Card> {
	Boolean react(Card entity, TrackCode trackCode) throws ApplicationUncheckException;
}
