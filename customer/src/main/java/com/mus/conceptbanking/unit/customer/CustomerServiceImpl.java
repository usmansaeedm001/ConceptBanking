package com.mus.conceptbanking.unit.customer;

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
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Usman
 * @created 10/16/2022 - 12:38 AM
 * @project OpenBanking
 */
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired private ApplicationConfig appConfig;
	@Autowired private CustomerDataServiceImpl dataService;
	@Autowired private CustomerDtoValidator validator;
	@Autowired private CustomerDtoMapper dtoMapper;

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
	public CustomerDto get(String uuid) {
		log.debug("Fetching customer for uuid [{}]", uuid);
		return dataService.getByUuid(uuid).orElse(null);
	}

	@Override
	public Mono<CustomerDto> getMono(String uuid){
		return dataService.getMonoByUuid(uuid);
	}

	@Override
	public Page<CustomerDto> search(CustomerSearchDto searchDto, int pageNo, int pageSize) {
		log.debug("Searching customer page with pageNo [{}], pageSize [{}] and search dto [{}]", pageNo, pageSize, searchDto);
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
	public CustomerDto save(CustomerCreateDto createDto) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.POST);
		return Optional.ofNullable(createDto)
			.filter(dto -> validator.validateCreateDto(dto))
			.map(dto -> dtoMapper.fromCreateDto(dto))
			.flatMap(Rethrow.rethrowFunction(dto -> dataService.save(dto)))
			.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	public CustomerDto update(String uuid, CustomerUpdateDto updateDto) throws ApplicationUncheckException {
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
	public CustomerDto partialUpdate(CustomerPartialUpdateDto partialUpdateDto) throws ApplicationUncheckException {
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

}