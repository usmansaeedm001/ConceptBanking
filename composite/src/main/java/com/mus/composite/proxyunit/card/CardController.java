package com.mus.composite.proxyunit.card;

import com.mus.framework.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Usman
 * @created 10/31/2022 - 1:10 AM
 * @project MyConceptBanking
 */
@RestController
@RequestMapping(path = "/api/card")
public class CardController {
	@Autowired private CardIntegrationService service;

	@GetMapping
	public ResponseEntity<ApiResponse<Flux<CardDto>>> getAllCard() {
		return new ResponseEntity<>(new ApiResponse<>(null, service.getAll()), HttpStatus.OK);
	}

	@GetMapping(path = "/customer/{uuid}")
	public ResponseEntity<ApiResponse<Flux<CardDto>>> getAllCardByCustomerUuid(@PathVariable String uuid) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.getAllByCustomerUuid(uuid)), HttpStatus.OK);
	}

	@GetMapping(path = "/{uuid}")
	public Mono<CardDto> getCard(@PathVariable String uuid) {
		return service.get(uuid);
	}

	@PostMapping(path = "/search/{page-no}/{page-size}")
	public Flux<CardDto> searchCustomer(@PathVariable("page-no") int pageNo, @PathVariable("page-size") int pageSize,
										@RequestBody CardDto dto) {
		return service.search(dto, pageNo, pageSize);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Mono<CardDto>>> saveCard(@RequestBody CardDto dto) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.save(dto)), HttpStatus.CREATED);
	}

	@PutMapping(path = "/{uuid}")
	public ResponseEntity<ApiResponse<Mono<CardDto>>> updateCard(@PathVariable String uuid, @RequestBody CardDto dto) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.update(uuid, dto)), HttpStatus.ACCEPTED);
	}

	@PatchMapping
	public ResponseEntity<ApiResponse<Mono<CardDto>>> partialUpdateCard(@RequestBody CardDto partialDto) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.partialUpdate(partialDto)), HttpStatus.ACCEPTED);
	}

	@DeleteMapping(path = "/{uuid}")
	public ResponseEntity<ApiResponse<Mono<CardDto>>> deleteCard(@PathVariable String uuid) {
		service.delete(uuid);
		return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
	}

	@DeleteMapping(path = "/customer/{uuid}")
	public ResponseEntity<ApiResponse<Mono<CardDto>>> deleteAllByCustomerUuid(@PathVariable String uuid) {
		service.deleteAllByCustomerUuid(uuid);
		return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
	}
}
