package com.mus.conceptbanking.unit.account;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import lombok.*;

/**
 * @author Usman
 * @created 10/16/2022 - 12:39 AM
 * @project OpenBanking
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class AccountSearchDto {
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