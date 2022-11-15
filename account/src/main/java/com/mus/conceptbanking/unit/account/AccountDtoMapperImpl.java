package com.mus.conceptbanking.unit.account;

import com.mus.framework.annotation.DtoMapper;
import com.mus.conceptbanking.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/**
 * @author Usman
 * @created 10/16/2022 - 12:39 AM
 * @project OpenBanking
 */
@DtoMapper
public class AccountDtoMapperImpl implements AccountDtoMapper {
	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto) authentication.getPrincipal()).getUuid();
	}

	/**
	 * @param dto
	 * @return AccountDto
	 */
	@Override
	public AccountDto fromCreateDto(AccountCreateDto dto) {
	    AccountDto accountDto = AccountDto.builder()
	    		.uuid(UUID.randomUUID().toString())
	    		.customerUuid(dto.getCustomerUuid())
	    		.status(dto.getStatus())
	    		.accountNo(dto.getAccountNo())
	    		.isActive(dto.getIsActive())
	    		.build();
	    return accountDto;


	}

	/**
	 * @param dto
	 * @return AccountDto
	 */
	@Override
	public AccountDto fromSearchDto(AccountSearchDto dto) {
	    AccountDto accountDto = AccountDto.builder()
	    		.uuid(dto.getUuid())
	    		.customerUuid(dto.getCustomerUuid())
	    		.status(dto.getStatus())
	    		.accountNo(dto.getAccountNo())
	    		.isActive(dto.getIsActive())
	    		.build();
	    return accountDto;


	}

	/**
	 * @param dto
	 * @return AccountDto
	 */
	@Override
	public AccountDto fromUpdateDto(AccountUpdateDto dto) {
	    AccountDto accountDto = AccountDto.builder()
	    		.customerUuid(dto.getCustomerUuid())
	    		.status(dto.getStatus())
	    		.accountNo(dto.getAccountNo())
	    		.isActive(dto.getIsActive())
	    		.build();
	    return accountDto;


	}

	/**
	 * @param dto
	 * @return AccountDto
	 */
	@Override
	public AccountDto fromPartialUpdateDto(AccountPartialUpdateDto dto) {
	    AccountDto accountDto = AccountDto.builder()
	    		.uuid(dto.getUuid())
	    		.customerUuid(dto.getCustomerUuid())
	    		.status(dto.getStatus())
	    		.accountNo(dto.getAccountNo())
	    		.isActive(dto.getIsActive())
	    		.build();
	    return accountDto;


	}
}
