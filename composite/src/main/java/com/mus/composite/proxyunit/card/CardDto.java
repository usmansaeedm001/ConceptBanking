package com.mus.composite.proxyunit.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

import lombok.*;

/**
 * @author Usman
 * @created 10/31/2022 - 1:10 AM
 * @project MyConceptBanking
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class CardDto implements Serializable {
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