package com.mus.conceptbanking.unit.card;

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
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
@Slf4j
@DataService
public class CardDataServiceImpl implements CardDataService {
	@Autowired private CardRepository repository;
	@Autowired private CardMapper mapper;
	@Autowired private CardEntityValidator validator;
	@Autowired private CardChangeReactor changeReactor;
	@Autowired private CardDeleteReactor deleteReactor;

	@Override
	public String getPrincipalUuid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((UserDto) authentication.getPrincipal()).getUuid();
	}

	private List<CardDto> processList(List<CardDto> list, TrackCode trackCode, Function<CardDto, Card> function) throws ApplicationUncheckException {
		return Optional.ofNullable(list)
			.filter(dtoList -> !dtoList.isEmpty())
			.map(Rethrow.rethrowFunction(dtoList -> dtoList.stream().map(function).collect(Collectors.toList())))
			.map(entityList -> repository.saveAll(entityList))
			.map(entityList -> mapper.toDtoList(entityList))
			.orElseThrow(
				() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	public boolean existsBy(CardDto by) {
		return Optional.ofNullable(by).map(dto -> mapper.toEntity(dto)).map(Example::of).map(example -> repository.exists(example)).orElse(false);
	}

	@Override
	public boolean existsByUuid(String uuid) {
		return Optional.ofNullable(uuid).filter(StringUtils::hasLength).map(s -> repository.existsByUuid(uuid)).orElse(false);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<CardDto> getByUuid(String uuid) {
		TrackCode trackCode = trackCode(RequestType.GET);
		return Optional.ofNullable(uuid)
			.filter(StringUtils::hasLength)
			.flatMap(s -> repository.findByUuidAndIsActiveTrue(s))
			.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity, trackCode)))
			.map(entity -> mapper.toDto(entity));
	}

	@Override
	public Page<CardDto> search(CardDto dto, PageRequest pageRequest) {
		TrackCode trackCode = trackCode(RequestType.SEARCH);
		return Optional.ofNullable(dto)
			.filter(dto1 -> Objects.nonNull(pageRequest))
			.map(aDto -> {
				Card entity = mapper.toEntity(aDto);
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
	public Optional<CardDto> save(CardDto dto) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.POST);
		return Optional.ofNullable(dto)
			.map(Rethrow.rethrowFunction(aDto -> prepareEntityToSave(aDto, trackCode)))
			.map(entity -> repository.save(entity))
			.map(entity -> mapper.toDto(entity));
	}

	private Card prepareEntityToSave(CardDto dto, TrackCode trackCode) throws ApplicationUncheckException {
		if (dto == null) {
			throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Optional<Card> optionalEntity = Optional.of(dto)
			.map(CardDto::getUuid)
			.filter(StringUtils::hasLength)
			.flatMap(dtoUuid -> repository.findByUuid(dtoUuid))
			.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity, trackCode)));
		if (optionalEntity.isPresent()) {
			throw new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST), trackCode, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		changeReactor.react(dto, new Card(), trackCode);
		return mapper.toEntity(dto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CardDto> save(List<CardDto> list) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.POST_ALL);
		return processList(list, trackCode, Rethrow.rethrowFunction(aDto -> prepareEntityToSave(aDto, trackCode)));
	}

	@Override
	public Optional<CardDto> update(CardDto dto) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.PUT);
		return Optional.ofNullable(dto)
			.map(Rethrow.rethrowFunction(aDto -> prepareEntityToUpdate(aDto, trackCode)))
			.map(entity -> repository.save(entity))
			.map(entity -> mapper.toDto(entity));
	}

	private Card prepareEntityToUpdate(CardDto dto, TrackCode trackCode) throws ApplicationUncheckException {
		return Optional.of(dto)
			.map(CardDto::getUuid)
			.filter(StringUtils::hasLength)
			.flatMap(dtoUuid -> repository.findByUuidAndIsActiveTrue(dtoUuid))
			.filter(Rethrow.rethrowPredicate(entity -> validator.validate(dto, entity, trackCode)))
			.filter(Rethrow.rethrowPredicate(entity -> changeReactor.react(dto, entity, trackCode)))
			.map(entity -> mapper.update(dto, entity))
			.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CardDto> update(List<CardDto> list) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.PUT_ALL);
		return processList(list, trackCode, Rethrow.rethrowFunction(aDto -> prepareEntityToUpdate(aDto, trackCode)));
	}

	@Override
	public Optional<CardDto> partialUpdate(CardDto dto) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.PATCH);
		return Optional.ofNullable(dto)
			.map(Rethrow.rethrowFunction(aDto -> prepareEntityToPartialUpdate(aDto, trackCode)))
			.map(entity -> repository.save(entity))
			.map(entity -> mapper.toDto(entity));
	}

	private Card prepareEntityToPartialUpdate(CardDto dto, TrackCode trackCode) throws ApplicationUncheckException {
		return Optional.ofNullable(dto)
			.map(CardDto::getUuid)
			.filter(StringUtils::hasLength)
			.flatMap(dtoUuid -> repository.findByUuidAndIsActiveTrue(dtoUuid))
			.filter(Rethrow.rethrowPredicate(entity -> validator.validatePartialUpdate(dto, entity, trackCode)))
			.filter(Rethrow.rethrowPredicate(entity -> changeReactor.react(dto, entity, trackCode)))
			.map(entity -> mapper.partialUpdate(dto, entity))
			.orElseThrow(() -> new ApplicationUncheckException(new EnumerationWrapper<>(ErrorCode.NOT_FOUND), trackCode, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CardDto> partialUpdate(List<CardDto> list) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.PATCH_ALL);
		return processList(list, trackCode, Rethrow.rethrowFunction(aDto -> prepareEntityToPartialUpdate(aDto, trackCode)));
	}

	@Override
	public void delete(String uuid) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.DELETE);
		Optional<Card> optionalEntity = Optional.ofNullable(uuid)
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
	public void delete(CardDto dto) throws ApplicationUncheckException {
		if (dto != null && StringUtils.hasLength(dto.getUuid())) {
			delete(dto.getUuid());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<CardDto> getAllByCustomerUuid(String uuid) {
		TrackCode trackCode = trackCode(RequestType.GET_ALL);
		log.debug("Fetching cards against customer uuid : " + uuid);
		List<CardDto> cardDtoList = Stream.ofNullable(uuid)
			.map(s -> repository.findAllByCustomerUuidAndIsActiveTrue(s))
			.flatMap(Collection::stream)
			.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity, trackCode)))
			.map(entity -> mapper.toDto(entity))
			.collect(Collectors.toList());
		log.debug("Fetched cards with details : " + cardDtoList);
		return cardDtoList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAllByCustomerUuid(String uuid) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.DELETE_ALL);
		List<Card> list = Stream.ofNullable(uuid)
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

	@Override
	@Transactional(readOnly = true)
	public List<CardDto> getAllByAccountUuid(String uuid) {
		TrackCode trackCode = trackCode(RequestType.GET_ALL);
		return Stream.ofNullable(uuid)
			.map(s -> repository.findAllByAccountUuidAndIsActiveTrue(s))
			.flatMap(Collection::stream)
			.filter(Rethrow.rethrowPredicate(entity -> validator.validate(entity, trackCode)))
			.map(entity -> mapper.toDto(entity))
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAllByAccountUuid(String uuid) throws ApplicationUncheckException {
		TrackCode trackCode = trackCode(RequestType.DELETE_ALL);
		List<Card> list = Stream.ofNullable(uuid)
			.map(s -> new ArrayList<>(repository.findAllByAccountUuidAndIsActiveTrue(uuid)))
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

