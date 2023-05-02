package com.mus.composite.composite.customer;

import com.mus.composite.proxyunit.account.AccountDto;
import com.mus.composite.proxyunit.card.CardDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.*;

import javax.persistence.Column;

/**
 * @author Usman
 * @created 10/31/2022 - 1:43 AM
 * @project MyConceptBanking
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class CustomerCompositeDto {
	@JsonProperty("uuid") private String uuid;
	@JsonProperty("active") private Boolean isActive;
	@JsonProperty("status") private String status;
	@JsonProperty("mobileNo") private String mobileNo;
	//todo: add Customerdto required fields
	private List<AccountDto> accountDtoList;
	private List<CardDto> cardDtoList;
}

