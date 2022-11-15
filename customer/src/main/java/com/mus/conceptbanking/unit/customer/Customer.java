package com.mus.conceptbanking.unit.customer;

import javax.persistence.*;

import com.mus.framework.config.AbstractAuditing;
import lombok.*;

import java.util.Objects;

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
@Entity
@Table(name = "cusotmer")
public class Customer extends AbstractAuditing {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
	@Column(name = "uuid") private String uuid;
	@Column(name = "status") private String status;
	@Column(name = "mobileNo") private String mobileNo;
	@Column(name = "active") private Boolean isActive = true;

	@Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (o == null || getClass() != o.getClass()) {return false;}
		Customer that = (Customer) o;
		return getUuid().equals(that.getUuid());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUuid());
	}

}
