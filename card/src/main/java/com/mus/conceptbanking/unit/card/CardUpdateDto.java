package com.mus.conceptbanking.unit.card;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CardUpdateDto {
	@JsonProperty("customerUuid") private String customerUuid;
	@JsonProperty("accountUuid") private String accountUuid;
	@JsonProperty("status") private String status;
	@JsonProperty("cardSerialNo") private String cardSerialNo;
	@JsonProperty("active") private Boolean isActive;
}