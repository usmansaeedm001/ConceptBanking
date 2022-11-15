package com.mus.conceptbanking.unit.card;

import com.mus.conceptbanking.config.ApplicationConfig;
import com.mus.conceptbanking.enums.ErrorCode;
import com.mus.framework.util.Rethrow;
import com.mus.framework.dto.EnumerationWrapper;
import com.mus.framework.enums.RequestType;
import com.mus.framework.exception.ApplicationUncheckException;
import com.mus.framework.handler.TrackCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.mus.conceptbanking.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Usman
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
@Slf4j
@Service
public class CardServiceImpl implements CardService {
	@Autowired private ApplicationConfig appConfig;
	@Autowired private CardDataServiceImpl dataService;
	@Autowired private CardDtoValidator validator;
	@Autowired private CardDtoMapper dtoMapper;

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto) authentication.getPrincipal()).getUuid();
	}

	@Override
	public boolean existsByUuid(String uuid) {
		return dataService.existsByUuid(uuid);
	}

	@Override
	public CardDto get(String uuid) {
		return dataService.getByUuid(uuid).orElse(null);
	}

	@Override
	public Page<CardDto> search(CardSearchDto searchDto, int pageNo, int pageSize) {
		pageNo = Math.max(pageNo, 0);
		pageSize = Math.max(pageSize, 1);
		pageSize = Math.min(Integer.parseInt(appConfig.getMaxPageSize()), pageSize);
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
		return Optional.ofNullable(searchDto)
			.filter(dto -> validator.validateSearchDto(dto))
			.map(dto -> dtoMapper.fromSearchDto(dto))
			.map(dto -> dataService.search(dto, pageRequest))
			.orElse(new PageImpl<>(new ArrayList<>()));
	}

	@Override
	public CardDto save(CardCreateDto createDto) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.POST);
		return Optional.ofNullable(createDto)
			.filter(dto -> validator.validateCreateDto(dto))
			.map(dto -> dtoMapper.fromCreateDto(dto))
			.flatMap(Rethrow.rethrowFunction(dto -> dataService.save(dto)))
			.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	public CardDto update(String uuid, CardUpdateDto updateDto) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.PUT);
		return Optional.ofNullable(updateDto)
			.filter(dto -> StringUtils.hasLength(uuid))
			.filter(dto -> validator.validateUpdateDto(uuid, dto))
			.map(dto -> dtoMapper.fromUpdateDto(dto))
			.map(dto -> {
				dto.setUuid(uuid);
				return dto;
			})
			.flatMap(Rethrow.rethrowFunction(dto -> dataService.update(dto)))
			.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	public CardDto partialUpdate(CardPartialUpdateDto partialUpdateDto) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.PATCH);
		return Optional.ofNullable(partialUpdateDto)
			.filter(dto -> validator.validatePartialUpdateDto(partialUpdateDto))
			.map(dto -> dtoMapper.fromPartialUpdateDto(dto))
			.flatMap(Rethrow.rethrowFunction(dto -> dataService.partialUpdate(dto)))
			.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	public void delete(String uuid) throws ApplicationUncheckException {
		if (uuid == null) {
			throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_UUID), trackCode(RequestType.DELETE));
		}
		dataService.delete(uuid);
	}

	@Override
	public List<CardDto> getAllByCustomerUuid(String uuid) {
		return Optional.ofNullable(uuid)
			.filter(StringUtils::hasLength)
			.map(Rethrow.rethrowFunction(s -> dataService.getAllByCustomerUuid(s)))
			.orElse(new ArrayList<>());
	}

	@Override
	public void deleteAllByCustomerUuid(String uuid) throws ApplicationUncheckException {
		if (StringUtils.hasLength(uuid)) {
			dataService.deleteAllByCustomerUuid(uuid);
		} else {
			throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_UUID), trackCode(RequestType.DELETE_ALL));
		}
	}

	@Override
	public List<CardDto> getAllByAccountUuid(String uuid) {
		return Optional.ofNullable(uuid)
			.filter(StringUtils::hasLength)
			.map(Rethrow.rethrowFunction(s -> dataService.getAllByAccountUuid(s)))
			.orElse(new ArrayList<>());
	}

	@Override
	public void deleteAllByAccountUuid(String uuid) throws ApplicationUncheckException {
		if (StringUtils.hasLength(uuid)) {
			dataService.deleteAllByAccountUuid(uuid);
		} else {
			throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_UUID), trackCode(RequestType.DELETE_ALL));
		}
	}

}