package com.mus.conceptbanking.unit.account;

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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Usman
 * @created 10/16/2022 - 12:39 AM
 * @project OpenBanking
 */
@Slf4j
@DataService
public class AccountDataServiceImpl implements AccountDataService {
	@Autowired private AccountRepository repository;
	@Autowired private AccountMapper mapper;
	@Autowired private AccountEntityValidator validator;
	@Autowired private AccountChangeReactor changeReactor;
	@Autowired private AccountDeleteReactor deleteReactor;

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto) authentication.getPrincipal()).getUuid();
	}

	private List<AccountDto> processList(List<AccountDto> list, TrackCode trackCode,
										 Function<AccountDto, Account> function) throws ApplicationUncheckException {
		return Optional.ofNullable(list)
			.filter(dtoList -> !dtoList.isEmpty())
			.map(Rethrow.rethrowFunction(dtoList -> dtoList.stream().map(function).collect(Collectors.toList())))
			.map(entityList -> repository.saveAll(entityList))
			.map(entityList -> mapper.toDtoList(entityList))
			.orElseThrow(
				() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	public boolean existsBy(AccountDto by) {
		return Optional.ofNullable(by).map(dto -> mapper.toEntity(dto)).map(Example::of).map(example -> repository.exists(example)).orElse(false);
	}

	@Override
	public boolean existsByUuid(String uuid) {
		return Optional.ofNullable(uuid).filter(StringUtils::hasLength).map(s -> repository.existsByUuid(uuid)).orElse(false);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<AccountDto> getByUuid(String uuid) {
		TrackCode trackCode = trackCode(RequestType.GET);
		return Optional.ofNullable(uuid)
			.filter(StringUtils::hasLength)
			.flatMap(s -> repository.findByUuidAndIsActiveTrue(s))
			.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity, trackCode)))
			.map(entity -> mapper.toDto(entity));
	}

	@Override
	public Page<AccountDto> search(AccountDto dto, PageRequest pageRequest) {
		TrackCode trackCode = trackCode(RequestType.SEARCH);
		return Optional.ofNullable(dto)
			.filter(dto1 -> Objects.nonNull(pageRequest))
			.map(aDto -> {
				Account entity = mapper.toEntity(aDto);
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
	public Optional<AccountDto> save(AccountDto dto) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.POST);
		return Optional.ofNullable(dto)
			.map(Rethrow.rethrowFunction(aDto -> prepareEntityToSave(aDto, trackCode)))
			.map(entity -> repository.save(entity))
			.map(entity -> mapper.toDto(entity));
	}

	private Account prepareEntityToSave(AccountDto dto, TrackCode trackCode) throws ApplicationUncheckException {
		if (dto == null) {
			throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Optional<Account> optionalEntity = Optional.of(dto)
			.map(AccountDto::getUuid)
			.filter(StringUtils::hasLength)
			.flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
			.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity, trackCode)));
		if (optionalEntity.isPresent()) {
			throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		changeReactor.react(dto, new Account(), trackCode);
		return mapper.toEntity(dto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AccountDto> save(List<AccountDto> list) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.POST_ALL);
		return processList(list, trackCode, Rethrow.rethrowFunction(aDto -> prepareEntityToSave(aDto, trackCode)));
	}

	@Override
	public Optional<AccountDto> update(AccountDto dto) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.PUT);
		return Optional.ofNullable(dto)
			.map(Rethrow.rethrowFunction(aDto -> prepareEntityToUpdate(aDto, trackCode)))
			.map(entity -> repository.save(entity))
			.map(entity -> mapper.toDto(entity));
	}

	private Account prepareEntityToUpdate(AccountDto dto, TrackCode trackCode) throws ApplicationUncheckException {
		return Optional.of(dto)
			.map(AccountDto::getUuid)
			.filter(StringUtils::hasLength)
			.flatMap(dtoUuid -> repository.findByUuidAndIsActiveTrue(dtoUuid))
			.filter(Rethrow.rethrowPredicate(entity -> validator.validate(dto, entity, trackCode)))
			.filter(Rethrow.rethrowPredicate(entity -> changeReactor.react(dto, entity, trackCode)))
			.map(entity -> mapper.update(dto, entity))
			.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AccountDto> update(List<AccountDto> list) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.PUT_ALL);
		return processList(list, trackCode, Rethrow.rethrowFunction(aDto -> prepareEntityToUpdate(aDto, trackCode)));
	}

	@Override
	public Optional<AccountDto> partialUpdate(AccountDto dto) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.PATCH);
		return Optional.ofNullable(dto)
			.map(Rethrow.rethrowFunction(aDto -> prepareEntityToPartialUpdate(aDto, trackCode)))
			.map(entity -> repository.save(entity))
			.map(entity -> mapper.toDto(entity));
	}

	private Account prepareEntityToPartialUpdate(AccountDto dto, TrackCode trackCode) throws ApplicationUncheckException {
		return Optional.ofNullable(dto)
			.map(AccountDto::getUuid)
			.filter(StringUtils::hasLength)
			.flatMap(dtoUuid -> repository.findByUuidAndIsActiveTrue(dtoUuid))
			.filter(Rethrow.rethrowPredicate(entity -> validator.validatePartialUpdate(dto, entity, trackCode)))
			.filter(Rethrow.rethrowPredicate(entity -> changeReactor.react(dto, entity, trackCode)))
			.map(entity -> mapper.partialUpdate(dto, entity))
			.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AccountDto> partialUpdate(List<AccountDto> list) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.PATCH_ALL);
		return processList(list, trackCode, Rethrow.rethrowFunction(aDto -> prepareEntityToPartialUpdate(aDto, trackCode)));
	}

	@Override
	public void delete(String uuid) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.DELETE);
		Optional<Account> optionalEntity = Optional.ofNullable(uuid)
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
	public void delete(AccountDto dto) throws ApplicationUncheckException {
		if (dto != null && StringUtils.hasLength(dto.getUuid())) {
			delete(dto.getUuid());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<AccountDto> getAllByCustomerUuid(String uuid) {
		TrackCode trackCode = trackCode(RequestType.GET_ALL);
		log.debug("Going to fetch account against customer uuid : " + uuid);
		List<AccountDto> accountDtoList = Stream.ofNullable(uuid)
			.map(s -> repository.findAllByCustomerUuidAndIsActiveTrue(s))
			.flatMap(Collection::stream)
			.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity, trackCode)))
			.map(entity -> mapper.toDto(entity))
			.collect(Collectors.toList());
		log.debug("Fetched account dto list : " + accountDtoList);
		return accountDtoList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAllByCustomerUuid(String uuid) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.DELETE_ALL);
		List<Account> list = Stream.ofNullable(uuid)
			.map(s -> new ArrayList<>(repository.findAllByCustomerUuidAndIsActiveTrue(uuid)))
			.flatMap(Collection::stream)
			.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity, trackCode)))
			.filter(Rethrow.rethrowPredicate(entity -> deleteReactor.react(entity, trackCode)))
			.collect(Collectors.toList());
		if (!list.isEmpty()) {
			repository.deleteAll(list);
		} else {
			throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}

