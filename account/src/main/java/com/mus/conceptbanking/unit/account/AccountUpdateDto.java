package com.mus.conceptbanking.unit.account;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class AccountUpdateDto {
	@JsonProperty("customerUuid") private String customerUuid;
	@JsonProperty("status") private String status;
	@JsonProperty("accountNo") private String accountNo;
	@JsonProperty("active") private Boolean isActive;
}