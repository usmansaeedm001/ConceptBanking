package com.mus.composite.proxyunit.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import lombok.*;

/**
 * @author Usman
 * @created 10/31/2022 - 1:39 AM
 * @project MyConceptBanking
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto {
	@JsonProperty("uuid") private String uuid;
	@JsonProperty("customerUuid") private String customerUuid;
	@JsonProperty("status") private String status;
	@JsonProperty("accountNo") private String accountNo;
	@JsonProperty("active") private Boolean isActive;

	@Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (o == null || getClass() != o.getClass()) {return false;}
		AccountDto that = (AccountDto) o;
		return getUuid().equals(that.getUuid());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUuid());
	}
}