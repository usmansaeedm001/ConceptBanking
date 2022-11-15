package com.mus.conceptbanking.unit.account;

import com.mus.framework.annotation.Mapper;
import com.mus.conceptbanking.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Usman
 * @created 10/16/2022 - 12:39 AM
 * @project OpenBanking
 */
@Mapper
public class AccountMapperImpl implements AccountMapper {
	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto) authentication.getPrincipal()).getUuid();
	}

	@Override
	public Account toEntity(AccountDto dto) {
	    Account account = Account.builder()
	    		.uuid(dto.getUuid())
	    		.customerUuid(dto.getCustomerUuid())
	    		.status(dto.getStatus())
	    		.accountNo(dto.getAccountNo())
	    		.isActive(dto.getIsActive())
	    		.build();
	    return account;

	}

	@Override
	public AccountDto toDto(Account account) {
		return AccountDto.builder()
				.uuid(account.getUuid())
				.customerUuid(account.getCustomerUuid())
				.status(account.getStatus())
				.accountNo(account.getAccountNo())
				.isActive(account.getIsActive())
				.build();

	}

	@Override
	public Account update(AccountDto dto, Account account) {
	    account.setStatus(dto.getStatus());
	    account.setAccountNo(dto.getAccountNo());
	    account.setIsActive(dto.getIsActive());
	    return account;

	}

	@Override
	public Account partialUpdate(AccountDto dto, Account account) {
	    if(isNotEmpty(dto.getStatus())){account.setStatus(dto.getStatus());}
	    if(isNotEmpty(dto.getAccountNo())){account.setAccountNo(dto.getAccountNo());}
	    if(isNotEmpty(dto.getIsActive())){account.setIsActive(dto.getIsActive());}
	    return account;

	}
}
