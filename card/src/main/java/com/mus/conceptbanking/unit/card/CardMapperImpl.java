package com.mus.conceptbanking.unit.card;

import com.mus.framework.annotation.Mapper;
import com.mus.conceptbanking.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Usman
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
@Mapper
public class CardMapperImpl implements CardMapper {
	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto) authentication.getPrincipal()).getUuid();
	}

	@Override
	public Card toEntity(CardDto dto) {
	    Card card = Card.builder()
	    		.uuid(dto.getUuid())
	    		.customerUuid(dto.getCustomerUuid())
	    		.accountUuid(dto.getAccountUuid())
	    		.status(dto.getStatus())
	    		.cardSerialNo(dto.getCardSerialNo())
	    		.isActive(dto.getIsActive())
	    		.build();
	    return card;

	}

	@Override
	public CardDto toDto(Card card) {
		return CardDto.builder()
				.uuid(card.getUuid())
				.customerUuid(card.getCustomerUuid())
				.accountUuid(card.getAccountUuid())
				.status(card.getStatus())
				.cardSerialNo(card.getCardSerialNo())
				.isActive(card.getIsActive())
				.build();

	}

	@Override
	public Card update(CardDto dto, Card card) {
	    card.setStatus(dto.getStatus());
	    card.setCardSerialNo(dto.getCardSerialNo());
	    card.setIsActive(dto.getIsActive());
	    return card;

	}

	@Override
	public Card partialUpdate(CardDto dto, Card card) {
	    if(isNotEmpty(dto.getStatus())){card.setStatus(dto.getStatus());}
	    if(isNotEmpty(dto.getCardSerialNo())){card.setCardSerialNo(dto.getCardSerialNo());}
	    if(isNotEmpty(dto.getIsActive())){card.setIsActive(dto.getIsActive());}
	    return card;

	}
}
