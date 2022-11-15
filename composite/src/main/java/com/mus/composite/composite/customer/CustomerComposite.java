package com.mus.composite.composite.customer;

import com.mus.composite.proxyunit.customer.CustomerDto;
import com.mus.composite.proxyunit.account.AccountDto;
import com.mus.composite.proxyunit.card.CardDto;

import java.util.List;

import lombok.*;

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
public class CustomerComposite {
	private CustomerDto customerDto;
	private List<AccountDto> accountDtoList;
	private List<CardDto> cardDtoList;
}

