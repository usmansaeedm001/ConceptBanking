package com.mus.conceptbanking.unit.account;

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
 * @created 10/16/2022 - 12:39 AM
 * @project OpenBanking
 */
@ValidationComponent
public class AccountEntityValidatorImpl implements AccountEntityValidator {
	@Autowired private AccountRepository repository;

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto) authentication.getPrincipal()).getUuid();
	}

	@Override
	public Boolean validateDto(AccountDto dto, TrackCode trackCode) throws BusinessValidationException {
		Boolean exists = true;
		exists = exists && Optional.ofNullable(dto)
			.map(AccountDto::getAccountNo)
			.map(s -> repository.exists(Example.of(Account.builder().accountNo(s).build())))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
				trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "Account already exists with specified AccountNo."));
		return exists;

	}

	@Override
	public Boolean validate(Account entity, TrackCode trackCode) throws BusinessValidationException {
		return true;

	}

	@Override
	public Boolean validate(List<Account> entityList, TrackCode trackCode) throws BusinessValidationException {
		return true;
	}

	@Override
	public Boolean validate(AccountDto dto, Account entity, TrackCode trackCode) throws BusinessValidationException {
		Boolean exists = true;
		exists = exists && Optional.ofNullable(dto)
			.filter(accountDto -> StringUtils.hasLength(accountDto.getAccountNo()))
			.filter(accountDto -> Objects.nonNull(entity))
			.map(accountDto -> repository.existsByAccountNoAndUuidNot(accountDto.getAccountNo(), entity.getUuid()))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
				trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "Account already exists with specified AccountNo."));

		return exists;
	}

	@Override
	public Boolean validatePartialUpdate(AccountDto dto, Account entity, TrackCode trackCode) throws BusinessValidationException {
		boolean exists = true;
		if (dto != null && StringUtils.hasLength(dto.getAccountNo())) {
			if (repository.existsByAccountNoAndUuidNot(dto.getAccountNo(), entity.getUuid())) {
				throw new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
					trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "Account already exists with specified AccountNo.");
			}
		}

		return exists;
	}
}
