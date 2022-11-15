package com.mus.conceptbanking.unit.customer;

import com.mus.framework.annotation.DtoMapper;
import com.mus.conceptbanking.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/**
 * @author Usman
 * @created 10/16/2022 - 12:38 AM
 * @project OpenBanking
 */
@DtoMapper
public class CustomerDtoMapperImpl implements CustomerDtoMapper {
	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto) authentication.getPrincipal()).getUuid();
	}

	/**
	 * @param dto
	 * @return CustomerDto
	 */
	@Override
	public CustomerDto fromCreateDto(CustomerCreateDto dto) {
		return CustomerDto.builder()
				.uuid(UUID.randomUUID().toString())
				.status(dto.getStatus())
				.mobileNo(dto.getMobileNo())
				.isActive(dto.getIsActive())
				.build();

	}

	/**
	 * @param dto
	 * @return CustomerDto
	 */
	@Override
	public CustomerDto fromSearchDto(CustomerSearchDto dto) {
	    CustomerDto customerDto = CustomerDto.builder()
	    		.uuid(dto.getUuid())
	    		.status(dto.getStatus())
	    		.mobileNo(dto.getMobileNo())
	    		.isActive(dto.getIsActive())
	    		.build();
	    return customerDto;


	}

	/**
	 * @param dto
	 * @return CustomerDto
	 */
	@Override
	public CustomerDto fromUpdateDto(CustomerUpdateDto dto) {
	    CustomerDto customerDto = CustomerDto.builder()
	    		.status(dto.getStatus())
	    		.mobileNo(dto.getMobileNo())
	    		.isActive(dto.getIsActive())
	    		.build();
	    return customerDto;


	}

	/**
	 * @param dto
	 * @return CustomerDto
	 */
	@Override
	public CustomerDto fromPartialUpdateDto(CustomerPartialUpdateDto dto) {
	    CustomerDto customerDto = CustomerDto.builder()
	    		.uuid(dto.getUuid())
	    		.status(dto.getStatus())
	    		.mobileNo(dto.getMobileNo())
	    		.isActive(dto.getIsActive())
	    		.build();
	    return customerDto;


	}
}
