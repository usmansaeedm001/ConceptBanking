package com.mus.conceptbanking.unit.card;

import com.mus.framework.annotation.ValidationComponent;
import com.mus.framework.exception.BusinessValidationException;
import com.mus.conceptbanking.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Usman
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
@ValidationComponent
public class CardDtoValidatorImpl implements CardDtoValidator {
	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto) authentication.getPrincipal()).getUuid();
	}

	/**
	 * @param dto
	 * @return Boolean
	 * @throws BusinessValidationException
	 */
	@Override
	public Boolean validateCreateDto(CardCreateDto dto) throws BusinessValidationException {
		/** todo: validate if there is anything unique other tha uuid*/
		return true;
	}

	/**
	 * @param dto
	 * @return Boolean
	 * @throws BusinessValidationException
	 */
	@Override
	public Boolean validateSearchDto(CardSearchDto dto) throws BusinessValidationException {
		return true;
	}

	/**
	 * @param uuid
	 * @param dto
	 * @return Boolean
	 * @throws BusinessValidationException
	 */
	@Override
	public Boolean validateUpdateDto(String uuid, CardUpdateDto dto) throws BusinessValidationException {
		/** todo:  validate if there is anything unique other tha uuid*/
		return true;
	}

	/**
	 * @param dto
	 * @return Boolean
	 * @throws BusinessValidationException
	 */
	@Override
	public Boolean validatePartialUpdateDto(CardPartialUpdateDto dto) throws BusinessValidationException {
		/** todo:  validate if there is anything unique other tha uuid*/
		return true;
	}

}
