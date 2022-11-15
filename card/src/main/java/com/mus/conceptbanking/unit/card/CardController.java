package com.mus.conceptbanking.unit.card;

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

/**
 * @author Usman
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
@RestController
@RequestMapping(path = "api/card")
public class CardController implements RequestValidationAdviser {
	@Autowired private CardService service;

	@GetMapping(path = "/{uuid}")
//	@PreAuthorize(
//		"hasAnyAuthority({'USER','ADMIN','READ_CARD','ADMIN_READ_CARD' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<CardDto>> getCard(@PathVariable String uuid) {
		return new ResponseEntity<>(new ApiResponse<>(null, service.get(uuid)), HttpStatus.OK);
	}

	@PostMapping(path = "/search/{page-no}/{page-size}")
//	@PreAuthorize(
//		"hasAnyAuthority({'USER','ADMIN','READ_CARD','ADMIN_READ_CARD' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<Page<CardDto>>> searchCard(@PathVariable("page-no") int pageNo, @PathVariable("page-size") int pageSize,
																 @RequestBody CardSearchDto dto) {
		//todo: CardUpdateDto must include only those fields for which search is allowed.
		return new ResponseEntity<>(new ApiResponse<>(null, service.search(dto, pageNo, pageSize)), HttpStatus.OK);
	}

	@PostMapping
//	@PreAuthorize(
//		"hasAnyAuthority({'USER','ADMIN','WRITE_CARD','ADMIN_WRITE_CARD' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<CardDto>> saveCard(@Valid @RequestBody CardCreateDto dto,
														 BindingResult bindingResult) throws ApplicationUncheckException {
		//todo: CardUpdateDto must include only those fields for which create is allowed.
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add an instance in EntityType with name CARD
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.POST, new EnumerationWrapper<>(EntityType.CARD), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.save(dto)), HttpStatus.CREATED);
	}

	@PutMapping(path = "/{uuid}")
//	@PreAuthorize(
//		"hasAnyAuthority({'USER','ADMIN','UPDATE_CARD','ADMIN_UPDATE_CARD' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<CardDto>> updateCard(@PathVariable String uuid, @Valid @RequestBody CardUpdateDto dto,
														   BindingResult bindingResult) throws ApplicationUncheckException {

		//todo: CardUpdateDto must include only those fields for which update is allowed.
		//todo: All the field in CardUpdateDto atleast annotated with javax.validation.constraints.NotNull
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add an instance in EntityType with name CARD
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.PUT, new EnumerationWrapper<>(EntityType.CARD), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.update(uuid, dto)), HttpStatus.ACCEPTED);
	}

	@PatchMapping
//	@PreAuthorize(
//		"hasAnyAuthority({'USER','ADMIN','UPDATE_CARD','ADMIN_UPDATE_CARD' })" + "|| hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<CardDto>> partialUpdateCard(@Valid @RequestBody CardPartialUpdateDto partialDto,
																  BindingResult bindingResult) throws ApplicationUncheckException {
		//todo: CardUpdateDto must include only those fields for which partialUpdate is allowed.
		//todo: create EntityType enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add an instance in EntityType with name CARD
		//todo: create ErrorCode enum and inherit it with EnumerationFace interface under com.mus.openbanking.enums and
		//todo: add instance in ErrorCode "INVALID_REQUEST("001", "Invalid request")"
		handlingErrors(bindingResult, RequestType.PATCH, new EnumerationWrapper<>(EntityType.CARD), new EnumerationWrapper<>(ErrorCode.INVALID_REQUEST));
		return new ResponseEntity<>(new ApiResponse<>(null, service.partialUpdate(partialDto)), HttpStatus.ACCEPTED);
	}

	@DeleteMapping(path = "/{uuid}")
//	@PreAuthorize("hasAnyAuthority({'ADMIN','ADMIN_DELETE_CARD' })" + "|| hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN', 'ROLE_OWNER')")
	public ResponseEntity<ApiResponse<CardDto>> deleteCard(@PathVariable String uuid) throws ApplicationUncheckException {
		service.delete(uuid);
		return new ResponseEntity<>(new ApiResponse<>(null, null), HttpStatus.NO_CONTENT);
	}

}

