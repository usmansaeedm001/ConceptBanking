package com.mus.conceptbanking.unit.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CustomerUpdateDto {
	@JsonProperty("status") private String status;
	@JsonProperty("mobileNo") private String mobileNo;
	@JsonProperty("active") private Boolean isActive;
}