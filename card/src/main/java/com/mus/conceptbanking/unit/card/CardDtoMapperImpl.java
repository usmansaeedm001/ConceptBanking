package com.mus.conceptbanking.unit.card;

import com.mus.framework.annotation.DtoMapper;
import com.mus.conceptbanking.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/**
 * @author Usman
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
@DtoMapper
public class CardDtoMapperImpl implements CardDtoMapper {
	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto) authentication.getPrincipal()).getUuid();
	}

	/**
	 * @param dto
	 * @return CardDto
	 */
	@Override
	public CardDto fromCreateDto(CardCreateDto dto) {
		return CardDto.builder()
				.uuid(UUID.randomUUID().toString())
				.customerUuid(dto.getCustomerUuid())
				.accountUuid(dto.getAccountUuid())
				.status(dto.getStatus())
				.cardSerialNo(dto.getCardSerialNo())
				.isActive(dto.getIsActive())
				.build();
	}

	/**
	 * @param dto
	 * @return CardDto
	 */
	@Override
	public CardDto fromSearchDto(CardSearchDto dto) {
		return CardDto.builder()
				.uuid(dto.getUuid())
				.customerUuid(dto.getCustomerUuid())
				.accountUuid(dto.getAccountUuid())
				.status(dto.getStatus())
				.cardSerialNo(dto.getCardSerialNo())
				.isActive(dto.getIsActive())
				.build();


	}

	/**
	 * @param dto
	 * @return CardDto
	 */
	@Override
	public CardDto fromUpdateDto(CardUpdateDto dto) {
	    CardDto cardDto = CardDto.builder()
	    		.customerUuid(dto.getCustomerUuid())
	    		.accountUuid(dto.getAccountUuid())
	    		.status(dto.getStatus())
	    		.cardSerialNo(dto.getCardSerialNo())
	    		.isActive(dto.getIsActive())
	    		.build();
	    return cardDto;


	}

	/**
	 * @param dto
	 * @return CardDto
	 */
	@Override
	public CardDto fromPartialUpdateDto(CardPartialUpdateDto dto) {
		return CardDto.builder()
				.uuid(dto.getUuid())
				.customerUuid(dto.getCustomerUuid())
				.accountUuid(dto.getAccountUuid())
				.status(dto.getStatus())
				.cardSerialNo(dto.getCardSerialNo())
				.isActive(dto.getIsActive())
				.build();


	}
}
