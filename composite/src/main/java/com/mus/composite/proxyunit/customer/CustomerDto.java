package com.mus.composite.proxyunit.customer;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import lombok.*;

import javax.persistence.Column;

/**
 * @author Usman
 * @created 10/30/2022 - 7:22 PM
 * @project MyConceptBanking
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
/*@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)*/
public class CustomerDto {
	@JsonProperty("uuid") private String uuid;
	@Column(name = "status") private String status;
	@Column(name = "mobileNo") private String mobileNo;
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