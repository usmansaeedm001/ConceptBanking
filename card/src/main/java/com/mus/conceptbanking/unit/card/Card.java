package com.mus.conceptbanking.unit.card;

import javax.persistence.*;

import com.mus.framework.config.AbstractAuditing;
import lombok.*;

import java.util.Objects;

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
@Entity
@Table(name = "card")
public class Card extends AbstractAuditing {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
	@Column(name = "uuid") private String uuid;
	@Column(name = "customer_Uuid") private String customerUuid;
	@Column(name = "account_Uuid") private String accountUuid;
	@Column(name = "status") private String status;
	@Column(name = "cardSerialNo") private String cardSerialNo;
	@Column(name = "active") private Boolean isActive = true;

	@Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (o == null || getClass() != o.getClass()) {return false;}
		Card that = (Card) o;
		return getUuid().equals(that.getUuid());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUuid());
	}

}
