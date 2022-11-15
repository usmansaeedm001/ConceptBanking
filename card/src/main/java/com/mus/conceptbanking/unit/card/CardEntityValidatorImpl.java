package com.mus.conceptbanking.unit.card;

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
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
@ValidationComponent
public class CardEntityValidatorImpl implements CardEntityValidator {
	@Autowired private CardRepository repository;

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto) authentication.getPrincipal()).getUuid();
	}

	@Override
	public Boolean validateDto(CardDto dto, TrackCode trackCode) throws BusinessValidationException {
		Boolean exists = true;
		exists = exists && Optional.ofNullable(dto)
			.map(CardDto::getCardSerialNo)
			.map(s -> repository.exists(Example.of(Card.builder().cardSerialNo(s).build())))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
				trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "Card already exists with specified CardSerialNo."));

		return exists;

	}

	@Override
	public Boolean validate(Card entity, TrackCode trackCode) throws BusinessValidationException {
		return true;

	}

	@Override
	public Boolean validate(List<Card> entityList, TrackCode trackCode) throws BusinessValidationException {
		return true;
	}

	@Override
	public Boolean validate(CardDto dto, Card entity, TrackCode trackCode) throws BusinessValidationException {
		Boolean exists = true;
		exists = exists && Optional.ofNullable(dto)
			.filter(cardDto -> StringUtils.hasLength(cardDto.getCardSerialNo()))
			.filter(cardDto -> Objects.nonNull(entity))
			.map(cardDto -> repository.existsByCardSerialNoAndUuidNot(cardDto.getCardSerialNo(), entity.getUuid()))
			.filter(aBoolean -> !aBoolean)
			.orElseThrow(() -> new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
				trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "Card already exists with specified CardSerialNo."));

		return exists;
	}

	@Override
	public Boolean validatePartialUpdate(CardDto dto, Card entity, TrackCode trackCode) throws BusinessValidationException {
		boolean exists = true;
		if (dto != null && StringUtils.hasLength(dto.getCardSerialNo())) {
			if (repository.existsByCardSerialNoAndUuidNot(dto.getCardSerialNo(), entity.getUuid())) {
				throw new BusinessValidationException(new EnumerationWrapper<>(ErrorCode.ALREADY_EXISTS),
					trackCode.setLayerCode(LayerType.ENTITY_VALIDATION_LAYER), "Card already exists with specified CardSerialNo.");
			}
		}

		return exists;
	}
}
