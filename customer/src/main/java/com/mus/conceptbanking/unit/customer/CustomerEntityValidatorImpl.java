package com.mus.conceptbanking.unit.customer;

import com.mus.conceptbanking.enums.ErrorCode;
import com.mus.framework.handler.TrackCode;
import com.mus.framework.enums.LayerType;
import com.mus.framework.annotation.ValidationComponent;
import com.mus.framework.dto.EnumerationWrapper;
import com.mus.framework.exception.BusinessValidationException;
import com.mus.conceptbanking.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Usman
 * @created 10/16/2022 - 12:38 AM
 * @project OpenBanking
 */
@ValidationComponent
public class CustomerEntityValidatorImpl implements CustomerEntityValidator {
	@Autowired private CustomerRepository repository;

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto) authentication.getPrincipal()).getUuid();
	}

	@Override
	public Boolean validateDto(CustomerDto dto, TrackCode trackCode) throws BusinessValidationException {
		Boolean exists = true;
		exists = exists && Optional.ofNullable(dto)
			.map(CustomerDto::getMobileNo)
			.map(s -> repository.exists(Example.of(Customer.builder().mobileNo(s).build())))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
				trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "Customer already exists with specified MobileNo."));

		return exists;

	}

	@Override
	public Boolean validate(Customer entity, TrackCode trackCode) throws BusinessValidationException {
		return true;

	}

	@Override
	public Boolean validate(List<Customer> entityList, TrackCode trackCode) throws BusinessValidationException {
		return true;
	}

	@Override
	public Boolean validate(CustomerDto dto, Customer entity, TrackCode trackCode) throws BusinessValidationException {
		Boolean exists = true;
		exists = exists && Optional.ofNullable(dto)
			.filter(customerDto -> StringUtils.hasLength(customerDto.getMobileNo()))
			.filter(customerDto -> Objects.nonNull(entity))
			.map(customerDto -> repository.existsByMobileNoAndUuidNot(customerDto.getMobileNo(), entity.getUuid()))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
				trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "Customer already exists with specified MobileNo."));

		return exists;
	}

	@Override
	public Boolean validatePartialUpdate(CustomerDto dto, Customer entity, TrackCode trackCode) throws BusinessValidationException {
		boolean exists = true;
		if (dto != null && StringUtils.hasLength(dto.getMobileNo())) {
			if (repository.existsByMobileNoAndUuidNot(dto.getMobileNo(), entity.getUuid())) {
				throw new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
					trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "Customer already exists with specified MobileNo.");
			}
		}

		return exists;
	}
}
