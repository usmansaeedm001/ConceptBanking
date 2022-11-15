package com.mus.conceptbanking.unit.customer;

import com.mus.framework.annotation.ValidationComponent;
import com.mus.framework.exception.BusinessValidationException;
import com.mus.conceptbanking.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Usman
 * @created 10/16/2022 - 12:38 AM
 * @project OpenBanking
 */
@ValidationComponent
public class CustomerDtoValidatorImpl implements CustomerDtoValidator {
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
	public Boolean validateCreateDto(CustomerCreateDto dto) throws BusinessValidationException {
		/** todo: validate if there is anything unique other tha uuid*/
		return true;
	}

	/**
	 * @param dto
	 * @return Boolean
	 * @throws BusinessValidationException
	 */
	@Override
	public Boolean validateSearchDto(CustomerSearchDto dto) throws BusinessValidationException {
		return true;
	}

	/**
	 * @param uuid
	 * @param dto
	 * @return Boolean
	 * @throws BusinessValidationException
	 */
	@Override
	public Boolean validateUpdateDto(String uuid, CustomerUpdateDto dto) throws BusinessValidationException {
		/** todo:  validate if there is anything unique other tha uuid*/
		return true;
	}

	/**
	 * @param dto
	 * @return Boolean
	 * @throws BusinessValidationException
	 */
	@Override
	public Boolean validatePartialUpdateDto(CustomerPartialUpdateDto dto) throws BusinessValidationException {
		/** todo:  validate if there is anything unique other tha uuid*/
		return true;
	}

}
