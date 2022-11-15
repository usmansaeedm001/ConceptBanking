package com.mus.composite.composite.customer;

import com.mus.composite.proxyunit.customer.CustomerDto;
import com.mus.framework.annotation.Mapper;

/**
 * @author Usman
 * @created 10/31/2022 - 1:43 AM
 * @project MyConceptBanking
 */
@Mapper
public class CustomerCompositeMapperImpl implements CustomerCompositeMapper {
	@Override
	public CustomerCompositeDto toAggregateDto(CustomerComposite composite) {
		CustomerCompositeDto customerCompositeDto = null;
		if (composite != null) {
			customerCompositeDto = new CustomerCompositeDto();
			if (composite.getCustomerDto() != null) {
				customerCompositeDto.setUuid(composite.getCustomerDto().getUuid());
				customerCompositeDto.setStatus(composite.getCustomerDto().getStatus());
				customerCompositeDto.setMobileNo(composite.getCustomerDto().getMobileNo());
				customerCompositeDto.setIsActive(composite.getCustomerDto().getIsActive());
			}
			customerCompositeDto.setAccountDtoList(composite.getAccountDtoList());
			customerCompositeDto.setCardDtoList(composite.getCardDtoList());
		}
		return customerCompositeDto;

	}

	@Override
	public CustomerComposite toAggregate(CustomerCompositeDto compositeDto) {
		CustomerComposite customerComposite = new CustomerComposite();
		customerComposite.setCustomerDto(CustomerDto.builder().uuid(compositeDto.getUuid()).isActive(compositeDto.getIsActive()).build());
		customerComposite.setAccountDtoList(compositeDto.getAccountDtoList());
		customerComposite.setCardDtoList(compositeDto.getCardDtoList());
		return customerComposite;

	}
}
