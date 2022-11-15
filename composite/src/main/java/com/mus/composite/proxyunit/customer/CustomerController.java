package com.mus.composite.proxyunit.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Usman
 * @created 10/30/2022 - 7:22 PM
 * @project MyConceptBanking
 */
@RestController
@RequestMapping(path = "/api/customer")
public class CustomerController {
	@Autowired private CustomerIntegrationService service;

	@GetMapping
	public Flux<CustomerDto> getAllCustomer() {
		return service.getAll();
	}

	@GetMapping(path = "/{uuid}")
	public Mono<CustomerDto> getCustomer(@PathVariable String uuid) {
		return service.get(uuid);
	}

	@PostMapping(path = "/search/{page-no}/{page-size}")
	public Flux<CustomerDto> searchCustomer(@PathVariable("page-no") int pageNo, @PathVariable("page-size") int pageSize,
											@RequestBody CustomerDto dto) {
		return  service.search(dto, pageNo, pageSize);
	}

	@PostMapping
	public Mono<CustomerDto> saveCustomer(@RequestBody CustomerDto dto) {
		return service.save(dto);
	}

	@PutMapping(path = "/{uuid}")
	public Mono<CustomerDto> updateCustomer(@PathVariable String uuid, @RequestBody CustomerDto dto) {
		return  service.update(uuid, dto);
	}

	@PatchMapping
	public Mono<CustomerDto> partialUpdateCustomer(@RequestBody CustomerDto partialDto) {
		return service.partialUpdate(partialDto);
	}

	@DeleteMapping(path = "/{uuid}")
	public void deleteCustomer(@PathVariable String uuid) {
		service.delete(uuid);
	}

}
