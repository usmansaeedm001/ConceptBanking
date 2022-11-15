package com.mus.composite.proxyunit.account;

import com.mus.framework.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Usman
 * @created 10/31/2022 - 1:39 AM
 * @project MyConceptBanking
 */
@RestController
@RequestMapping(path = "/api/account")
public class AccountController {
	@Autowired private AccountIntegrationService service;

	@GetMapping
	public ResponseEntity<ApiResponse<Flux<AccountDto>>> getAllAccount() {
		return new ResponseEntity<>(new ApiResponse<>(null, service.getAll()), HttpStatus.OK);
	}

	@GetMapping(path = "/customer/{uuid}")
	public ResponseEntity<ApiResponse<Flux<AccountDto>>> getAllAccountByCustomerUuid(@PathVariable String uuid) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.getAllByCustomerUuid(uuid)), HttpStatus.OK);
	}

	@GetMapping(path = "/{uuid}")
	public Mono<AccountDto> getAccount(@PathVariable String uuid) {
		return service.get(uuid);
	}

	@PostMapping(path = "/search/{page-no}/{page-size}")
	@ResponseStatus(HttpStatus.OK)
	public Flux<AccountDto> searchCustomer(@PathVariable("page-no") int pageNo, @PathVariable("page-size") int pageSize,
										   @RequestBody AccountDto dto) {
		return service.search(dto, pageNo, pageSize);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Mono<AccountDto>>> saveAccount(@RequestBody AccountDto dto) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.save(dto)), HttpStatus.CREATED);
	}

	@PutMapping(path = "/{uuid}")
	public ResponseEntity<ApiResponse<Mono<AccountDto>>> updateAccount(@PathVariable String uuid, @RequestBody AccountDto dto) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.update(uuid, dto)), HttpStatus.ACCEPTED);
	}

	@PatchMapping
	public ResponseEntity<ApiResponse<Mono<AccountDto>>> partialUpdateAccount(@RequestBody AccountDto partialDto) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.partialUpdate(partialDto)), HttpStatus.ACCEPTED);
	}

	@DeleteMapping(path = "/{uuid}")
	public ResponseEntity<ApiResponse<Mono<AccountDto>>> deleteAccount(@PathVariable String uuid) {
		service.delete(uuid);
		return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
	}

	@DeleteMapping(path = "/customer/{uuid}")
	public ResponseEntity<ApiResponse<Mono<AccountDto>>> deleteAllByCustomerUuid(@PathVariable String uuid) {
		service.deleteAllByCustomerUuid(uuid);
		return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
	}
}
