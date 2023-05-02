package com.mus.conceptbanking.unit.account;

import com.mus.conceptbanking.enums.EntityType;
import com.mus.conceptbanking.enums.ErrorCode;
import com.mus.framework.dto.ApiResponse;
import com.mus.framework.dto.EnumerationWrapper;
import com.mus.framework.enums.RequestType;
import com.mus.framework.exception.ApplicationUncheckException;
import com.mus.framework.face.RequestValidationAdviser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Usman
 * @created 10/16/2022 - 12:39 AM
 * @project OpenBanking
 */
@RestController
@RequestMapping(path = "/api/account")
public class AccountController implements RequestValidationAdviser {
	@Autowired private AccountService service;

	@GetMapping(path = "/{uuid}")
//	@PreAuthorize(
//		"hasAnyAuthority({'USER','ADMIN','READ_ACCOUNT','ADMIN_READ_ACCOUNT' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<AccountDto>> getAccount(@PathVariable String uuid) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.get(uuid)), HttpStatus.OK);
	}

	@PostMapping(path = "/search/{page-no}/{page-size}")
//	@PreAuthorize(
//		"hasAnyAuthority({'USER','ADMIN','READ_ACCOUNT','ADMIN_READ_ACCOUNT' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<Page<AccountDto>>> searchAccount(@PathVariable("page-no") int pageNo, @PathVariable("page-size") int pageSize,
																	   @RequestBody AccountSearchDto dto) {
		//todo: AccountUpdateDto must include only those fields for which search is allowed.
		return new ResponseEntity<>(new ApiResponse<>(null, service.search(dto, pageNo, pageSize)), HttpStatus.OK);
	}

	@PostMapping
//	@PreAuthorize(
//		"hasAnyAuthority({'USER','ADMIN','WRITE_ACCOUNT','ADMIN_WRITE_ACCOUNT' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', " +
//			"'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<AccountDto>> saveAccount(@Valid @RequestBody AccountCreateDto dto,
															   BindingResult bindingResult) throws ApplicationUncheckException {
		//todo: AccountUpdateDto must include only those fields for which create is allowed.
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add an instance in EntityType with name ACCOUNT
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.POST, new EnumerationWrapper<>(EntityType.ACCOUNT), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.save(dto)), HttpStatus.CREATED);
	}

	@PutMapping(path = "/{uuid}")
//	@PreAuthorize(
//		"hasAnyAuthority({'USER','ADMIN','UPDATE_ACCOUNT','ADMIN_UPDATE_ACCOUNT' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', " +
//			"'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<AccountDto>> updateAccount(@PathVariable String uuid, @Valid @RequestBody AccountUpdateDto dto,
																 BindingResult bindingResult) throws ApplicationUncheckException {

		//todo: AccountUpdateDto must include only those fields for which update is allowed.
		//todo: All the field in AccountUpdateDto atleast annotated with javax.validation.constraints.NotNull
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add an instance in EntityType with name ACCOUNT
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.PUT, new EnumerationWrapper<>(EntityType.ACCOUNT), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.update(uuid, dto)), HttpStatus.ACCEPTED);
	}

	@PatchMapping
//	@PreAuthorize(
//		"hasAnyAuthority({'USER','ADMIN','UPDATE_ACCOUNT','ADMIN_UPDATE_ACCOUNT' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', " +
//			"'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<AccountDto>> partialUpdateAccount(@Valid @RequestBody AccountPartialUpdateDto partialDto,
																		BindingResult bindingResult) throws ApplicationUncheckException {
		//todo: AccountUpdateDto must include only those fields for which partialUpdate is allowed.
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add an instance in EntityType with name ACCOUNT
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.PATCH, new EnumerationWrapper<>(EntityType.ACCOUNT), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.partialUpdate(partialDto)), HttpStatus.ACCEPTED);
	}

	@DeleteMapping(path = "/{uuid}")
//	@PreAuthorize("hasAnyAuthority({'ADMIN','ADMIN_DELETE_ACCOUNT' })" + "|| hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<AccountDto>> deleteAccount(@PathVariable String uuid) throws ApplicationUncheckException {
		service.delete(uuid);
		return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
	}

	@GetMapping(path = "/customer/{uuid}")
	//	@PreAuthorize(
	//		"hasAnyAuthority({'USER','ADMIN','READ_ACCOUNT','ADMIN_READ_ACCOUNT' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<List<AccountDto>>> getAccountByCustomer(@PathVariable String uuid) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.getAllByCustomerUuid(uuid)), HttpStatus.OK);
	}
}

