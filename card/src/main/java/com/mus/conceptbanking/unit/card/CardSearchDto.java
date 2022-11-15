package com.mus.conceptbanking.unit.card;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import lombok.*;

/**
 * @author Usman
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class CardSearchDto {
	@JsonProperty("uuid") private String uuid;
	@JsonProperty("customerUuid") private String customerUuid;
	@JsonProperty("accountUuid") private String accountUuid;
	@JsonProperty("status") private String status;
	@JsonProperty("cardSerialNo") private String cardSerialNo;
	@JsonProperty("active") private Boolean isActive;

	@Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (o == null || getClass() != o.getClass()) {return false;}
		CardDto that = (CardDto) o;
		return getUuid().equals(that.getUuid());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUuid());
	}
}