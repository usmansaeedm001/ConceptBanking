package com.mus.conceptbanking.unit.account;

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
 * @created 10/16/2022 - 12:39 AM
 * @project OpenBanking
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
	@Autowired private ApplicationConfig appConfig;
	@Autowired private AccountDataServiceImpl dataService;
	@Autowired private AccountDtoValidator validator;
	@Autowired private AccountDtoMapper dtoMapper;

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
	public AccountDto get(String uuid) {
		return dataService.getByUuid(uuid).orElse(null);
	}

	@Override
	public Page<AccountDto> search(AccountSearchDto searchDto, int pageNo, int pageSize) {
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
	public AccountDto save(AccountCreateDto createDto) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.POST);
		return Optional.ofNullable(createDto)
			.filter(dto -> validator.validateCreateDto(dto))
			.map(dto -> dtoMapper.fromCreateDto(dto))
			.flatMap(Rethrow.rethrowFunction(dto -> dataService.save(dto)))
			.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	public AccountDto update(String uuid, AccountUpdateDto updateDto) throws ApplicationUncheckException {
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
	public AccountDto partialUpdate(AccountPartialUpdateDto partialUpdateDto) throws ApplicationUncheckException {
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
	public List<AccountDto> getAllByCustomerUuid(String uuid) {
		log.debug("Going to fetch account against customer uuid : " + uuid);
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

}