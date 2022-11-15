package com.mus.conceptbanking.unit.customer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import lombok.*;

/**
 * @author Usman
 * @created 10/16/2022 - 12:38 AM
 * @project OpenBanking
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class CustomerDto {
	@JsonProperty("uuid") private String uuid;
	@JsonProperty("status") private String status;
	@JsonProperty("mobileNo") private String mobileNo;
	@JsonProperty("active") private Boolean isActive;

	@Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (o == null || getClass() != o.getClass()) {return false;}
		CustomerDto that = (CustomerDto) o;
		return getUuid().equals(that.getUuid());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUuid());
	}
}