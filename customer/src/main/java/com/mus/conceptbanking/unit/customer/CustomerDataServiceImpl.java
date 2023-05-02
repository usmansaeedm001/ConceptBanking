package com.mus.conceptbanking.unit.customer;

import com.mus.framework.exception.ApplicationException;
import com.mus.conceptbanking.enums.ErrorCode;
import com.mus.framework.dto.EnumerationWrapper;
import com.mus.framework.annotation.DataService;
import com.mus.framework.enums.RequestType;
import com.mus.framework.exception.ApplicationUncheckException;
import com.mus.framework.handler.TrackCode;
import com.mus.framework.util.Rethrow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.mus.conceptbanking.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Usman
 * @created 10/16/2022 - 12:38 AM
 * @project OpenBanking
 */
@Slf4j
@DataService
public class CustomerDataServiceImpl implements CustomerDataService {
	@Autowired private CustomerRepository repository;
	@Autowired private CustomerMapper mapper;
	@Autowired private CustomerEntityValidator validator;
	@Autowired private CustomerChangeReactor changeReactor;
	@Autowired private CustomerDeleteReactor deleteReactor;

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto) authentication.getPrincipal()).getUuid();
	}

	private List<CustomerDto> processList(List<CustomerDto> list, TrackCode trackCode,
										  Function<CustomerDto, Customer> function) throws ApplicationUncheckException {
		return Optional.ofNullable(list)
			.filter(dtoList -> !dtoList.isEmpty())
			.map(Rethrow.rethrowFunction(dtoList -> dtoList.stream().map(function).collect(Collectors.toList())))
			.map(entityList -> repository.saveAll(entityList))
			.map(entityList -> mapper.toDtoList(entityList))
			.orElseThrow(
				() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	public boolean existsBy(CustomerDto by) {
		return Optional.ofNullable(by).map(dto -> mapper.toEntity(dto)).map(Example::of).map(example -> repository.exists(example)).orElse(false);
	}

	@Override
	public boolean existsByUuid(String uuid) {
		return Optional.ofNullable(uuid).filter(StringUtils::hasLength).map(s -> repository.existsByUuid(uuid)).orElse(false);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<CustomerDto> getByUuid(String uuid) {
		log.debug("Getting customer for uuid [{}]", uuid);
		TrackCode trackCode = trackCode(RequestType.GET);
		return Optional.ofNullable(uuid)
			.filter(StringUtils::hasLength)
			.flatMap(s -> repository.findByUuidAndIsActiveTrue(s))
			.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity, trackCode)))
			.map(entity -> mapper.toDto(entity));
	}

	@Transactional(readOnly = true)
	public Mono<CustomerDto> getMonoByUuid(String uuid) {
		TrackCode trackCode = trackCode(RequestType.GET);
		return Mono.just(uuid).map(Rethrow.rethrowFunction(s ->  repository.findByUuid(s).orElseThrow(() -> new ApplicationException(new EnumerationWrapper<>(ErrorCode.INVALID_UUID)
			,trackCode)))).map(customer ->  mapper.toDto(customer));
	}

	@Override
	public Page<CustomerDto> search(CustomerDto dto, PageRequest pageRequest) {
		log.debug("Searching customer with customer dto [{}] and Page Request [{}]", dto, pageRequest);
		TrackCode trackCode = trackCode(RequestType.SEARCH);
		return Optional.ofNullable(dto)
			.filter(dto1 -> Objects.nonNull(pageRequest))
			.map(aDto -> {
				Customer entity = mapper.toEntity(aDto);
				entity.setIsActive(true);
				return entity;
			})
			.map(Example::of)
			.map(example -> repository.findAll(example, pageRequest))
			.map(page -> new PageImpl<>(Optional.of(page)
				.map(Slice::getContent)
				.stream()
				.flatMap(Collection::stream)
				.filter(entity -> validator.validate(entity, trackCode))
				.map(entity -> mapper.toDto(entity))
				.collect(Collectors.toList()), pageRequest, page.getTotalElements()))
			.orElse(new PageImpl<>(new ArrayList<>()));
	}

	@Override
	public Optional<CustomerDto> save(CustomerDto dto) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.POST);
		return Optional.ofNullable(dto)
			.map(Rethrow.rethrowFunction(aDto -> prepareEntityToSave(aDto, trackCode)))
			.map(entity -> repository.save(entity))
			.map(entity -> mapper.toDto(entity));
	}

	private Customer prepareEntityToSave(CustomerDto dto, TrackCode trackCode) throws ApplicationUncheckException {
		if (dto == null) {
			throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Optional<Customer> optionalEntity = Optional.of(dto)
			.filter(customerDto -> validator.validateDto(customerDto, trackCode))
			.map(CustomerDto::getUuid)
			.filter(StringUtils::hasLength)
			.flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
			.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity, trackCode)));
		if (optionalEntity.isPresent()) {
			throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		changeReactor.react(dto, new Customer(), trackCode);
		return mapper.toEntity(dto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CustomerDto> save(List<CustomerDto> list) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.POST_ALL);
		return processList(list, trackCode, Rethrow.rethrowFunction(aDto -> prepareEntityToSave(aDto, trackCode)));
	}

	@Override
	public Optional<CustomerDto> update(CustomerDto dto) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.PUT);
		return Optional.ofNullable(dto)
			.map(Rethrow.rethrowFunction(aDto -> prepareEntityToUpdate(aDto, trackCode)))
			.map(entity -> repository.save(entity))
			.map(entity -> mapper.toDto(entity));
	}

	private Customer prepareEntityToUpdate(CustomerDto dto, TrackCode trackCode) throws ApplicationUncheckException {
		return Optional.of(dto)
			.map(CustomerDto::getUuid)
			.filter(StringUtils::hasLength)
			.flatMap(dtoUuid -> repository.findByUuidAndIsActiveTrue(dtoUuid))
			.filter(Rethrow.rethrowPredicate(entity -> validator.validate(dto, entity, trackCode)))
			.filter(Rethrow.rethrowPredicate(entity -> changeReactor.react(dto, entity, trackCode)))
			.map(entity -> mapper.update(dto, entity))
			.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CustomerDto> update(List<CustomerDto> list) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.PUT_ALL);
		return processList(list, trackCode, Rethrow.rethrowFunction(aDto -> prepareEntityToUpdate(aDto, trackCode)));
	}

	@Override
	public Optional<CustomerDto> partialUpdate(CustomerDto dto) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.PATCH);
		return Optional.ofNullable(dto)
			.map(Rethrow.rethrowFunction(aDto -> prepareEntityToPartialUpdate(aDto, trackCode)))
			.map(entity -> repository.save(entity))
			.map(entity -> mapper.toDto(entity));
	}

	private Customer prepareEntityToPartialUpdate(CustomerDto dto, TrackCode trackCode) throws ApplicationUncheckException {
		return Optional.ofNullable(dto)
			.map(CustomerDto::getUuid)
			.filter(StringUtils::hasLength)
			.flatMap(dtoUuid -> repository.findByUuidAndIsActiveTrue(dtoUuid))
			.filter(Rethrow.rethrowPredicate(entity -> validator.validatePartialUpdate(dto, entity, trackCode)))
			.filter(Rethrow.rethrowPredicate(entity -> changeReactor.react(dto, entity, trackCode)))
			.map(entity -> mapper.partialUpdate(dto, entity))
			.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CustomerDto> partialUpdate(List<CustomerDto> list) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.PATCH_ALL);
		return processList(list, trackCode, Rethrow.rethrowFunction(aDto -> prepareEntityToPartialUpdate(aDto, trackCode)));
	}

	@Override
	public void delete(String uuid) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.DELETE);
		Optional<Customer> optionalEntity = Optional.ofNullable(uuid)
			.filter(StringUtils::hasLength)
			.flatMap(s -> repository.findByUuidAndIsActiveTrue(s))
			.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity, trackCode)))
			.filter(Rethrow.rethrowPredicate(entity -> deleteReactor.react(entity, trackCode)));
		if (optionalEntity.isPresent()) {
			repository.delete(optionalEntity.get());
		} else {
			throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@Override
	public void delete(CustomerDto dto) throws ApplicationUncheckException {
		if (dto != null && StringUtils.hasLength(dto.getUuid())) {
			delete(dto.getUuid());
		}
	}

}

