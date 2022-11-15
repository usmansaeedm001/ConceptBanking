package com.mus.composite.composite.customer;

import com.mus.framework.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author Usman
 * @created 10/31/2022 - 1:43 AM
 * @project MyConceptBanking
 */
@Slf4j
@RestController
@RequestMapping(path = "/api/composite/customer")
public class CustomerCompositeController {
	@Autowired private CustomerCompositeService service;

	@GetMapping(path = "/{uuid}", produces = "application/stream+json")
	public Mono<CustomerCompositeDto> getCustomer(@PathVariable String uuid) {
		log.info("Fetching composite customer for customer uuid [{}]", uuid);
		return service.get(uuid);
	}

	/*@PostMapping(path = "/search/{page-no}/{page-size}")
	public ResponseEntity<ApiResponse<Flux<CustomerCompositeDto>>> searchCustomer(@PathVariable("page-no") int pageNo, @PathVariable("page-size") int pageSize,
																				  @RequestBody CustomerCompositeSearchDto dto) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.search(dto, pageNo, pageSize)), HttpStatus.CREATED);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Mono<CustomerCompositeDto>>> saveCustomer(@RequestBody CustomerCompositeDto dto) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.save(dto)), HttpStatus.CREATED);
	}

	@PutMapping(path = "/{uuid}")
	public ResponseEntity<ApiResponse<Mono<CustomerCompositeDto>>> updateCustomer(@PathVariable String uuid, @RequestBody CustomerCompositeDto dto) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.update(uuid, dto)), HttpStatus.ACCEPTED);
	}

	@PatchMapping
	public ResponseEntity<ApiResponse<Mono<CustomerCompositeDto>>> partialUpdateCustomer(@RequestBody CleanCustomerDto partialDto) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.partialUpdate(partialDto)), HttpStatus.ACCEPTED);
	}

	@DeleteMapping(path = "/{uuid}")
	public ResponseEntity<ApiResponse<Mono<CustomerCompositeDto>>> deleteCustomer(@PathVariable String uuid) {
		service.delete(uuid);
		return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
	}*/

}
