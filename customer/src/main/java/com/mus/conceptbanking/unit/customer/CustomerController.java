package com.mus.conceptbanking.unit.customer;

import com.mus.conceptbanking.enums.EntityType;
import com.mus.conceptbanking.enums.ErrorCode;
import com.mus.framework.dto.ApiResponse;
import com.mus.framework.dto.EnumerationWrapper;
import com.mus.framework.enums.RequestType;
import com.mus.framework.exception.ApplicationUncheckException;
import com.mus.framework.face.RequestValidationAdviser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Usman
 * @created 10/16/2022 - 12:38 AM
 * @project OpenBanking
 */
@Slf4j
@RestController
@RequestMapping(path = "/api/customer")
public class CustomerController implements RequestValidationAdviser {
	@Autowired private CustomerService service;

	@GetMapping(path = "/{uuid}")
//	@PreAuthorize(
//		"hasAnyAuthority({'USER','ADMIN','READ_CUSTOMER','ADMIN_READ_CUSTOMER' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', " +
//			"'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<CustomerDto>> getCustomer(@PathVariable String uuid) {
		log.info("Getting customer for uuid [{}]", uuid);
		return new ResponseEntity<>(new ApiResponse<>(null, service.get(uuid)), HttpStatus.OK);
	}

	@GetMapping(path = "/mono/{uuid}")
	public ResponseEntity<ApiResponse<CustomerDto>> getMonoCustomer(@PathVariable String uuid) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.getMono(uuid).block()), HttpStatus.OK);
	}

	@PostMapping(path = "/search/{page-no}/{page-size}")
//	@PreAuthorize(
//		"hasAnyAuthority({'USER','ADMIN','READ_CUSTOMER','ADMIN_READ_CUSTOMER' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', " +
//			"'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<Page<CustomerDto>>> searchCustomer(@PathVariable("page-no") int pageNo, @PathVariable("page-size") int pageSize,
																		 @RequestBody CustomerSearchDto dto) {
		log.info("Searching customer with pageNo [{}], pageSize [{}], and search parameters [{}]", pageNo,pageSize, dto);
		//todo: CustomerUpdateDto must include only those fields for which search is allowed.

		return new ResponseEntity<>(new ApiResponse<>(null, service.search(dto, pageNo, pageSize)), HttpStatus.OK);
	}

	@PostMapping
//	@PreAuthorize(
//		"hasAnyAuthority({'USER','ADMIN','WRITE_CUSTOMER','ADMIN_WRITE_CUSTOMER' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', " +
//			"'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<CustomerDto>> saveCustomer(@Valid @RequestBody CustomerCreateDto dto,
																 BindingResult bindingResult) throws ApplicationUncheckException {
		//todo: CustomerUpdateDto must include only those fields for which create is allowed.
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add an instance in EntityType with name CUSTOMER
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.POST, new EnumerationWrapper<>(EntityType.CUSTOMER), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.save(dto)), HttpStatus.CREATED);
	}

	@PutMapping(path = "/{uuid}")
//	@PreAuthorize(
//		"hasAnyAuthority({'USER','ADMIN','UPDATE_CUSTOMER','ADMIN_UPDATE_CUSTOMER' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', " +
//			"'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<CustomerDto>> updateCustomer(@PathVariable String uuid, @Valid @RequestBody CustomerUpdateDto dto,
																   BindingResult bindingResult) throws ApplicationUncheckException {

		//todo: CustomerUpdateDto must include only those fields for which update is allowed.
		//todo: All the field in CustomerUpdateDto atleast annotated with javax.validation.constraints.NotNull
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add an instance in EntityType with name CUSTOMER
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.PUT, new EnumerationWrapper<>(EntityType.CUSTOMER), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.update(uuid, dto)), HttpStatus.ACCEPTED);
	}

	@PatchMapping
//	@PreAuthorize(
//		"hasAnyAuthority({'USER','ADMIN','UPDATE_CUSTOMER','ADMIN_UPDATE_CUSTOMER' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', " +
//			"'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<CustomerDto>> partialUpdateCustomer(@Valid @RequestBody CustomerPartialUpdateDto partialDto,
																		  BindingResult bindingResult) throws ApplicationUncheckException {
		//todo: CustomerUpdateDto must include only those fields for which partialUpdate is allowed.
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add an instance in EntityType with name CUSTOMER
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.PATCH, new EnumerationWrapper<>(EntityType.CUSTOMER), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.partialUpdate(partialDto)), HttpStatus.ACCEPTED);
	}

	@DeleteMapping(path = "/{uuid}")
//	@PreAuthorize("hasAnyAuthority({'ADMIN','ADMIN_DELETE_CUSTOMER' })" + "|| hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<CustomerDto>> deleteCustomer(@PathVariable String uuid) throws ApplicationUncheckException {
		service.delete(uuid);
		return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
	}

}

