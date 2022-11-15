package com.mus.conceptbanking.unit.customer;

import com.mus.framework.annotation.Mapper;
import com.mus.conceptbanking.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Usman
 * @created 10/16/2022 - 12:38 AM
 * @project OpenBanking
 */
@Mapper
public class CustomerMapperImpl implements CustomerMapper {
	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto) authentication.getPrincipal()).getUuid();
	}

	@Override
	public Customer toEntity(CustomerDto dto) {
	    Customer customer = Customer.builder()
	    		.uuid(dto.getUuid())
	    		.status(dto.getStatus())
	    		.mobileNo(dto.getMobileNo())
	    		.isActive(dto.getIsActive())
	    		.build();
	    return customer;

	}

	@Override
	public CustomerDto toDto(Customer customer) {
	    CustomerDto customerDto = CustomerDto.builder()
	    		.uuid(customer.getUuid())
	    		.status(customer.getStatus())
	    		.mobileNo(customer.getMobileNo())
	    		.isActive(customer.getIsActive())
	    		.build();
	    return customerDto;

	}

	@Override
	public Customer update(CustomerDto dto, Customer customer) {
	    customer.setStatus(dto.getStatus());
	    customer.setMobileNo(dto.getMobileNo());
	    customer.setIsActive(dto.getIsActive());
	    return customer;

	}

	@Override
	public Customer partialUpdate(CustomerDto dto, Customer customer) {
	    if(isNotEmpty(dto.getStatus())){customer.setStatus(dto.getStatus());}
	    if(isNotEmpty(dto.getMobileNo())){customer.setMobileNo(dto.getMobileNo());}
	    if(isNotEmpty(dto.getIsActive())){customer.setIsActive(dto.getIsActive());}
	    return customer;

	}
}
