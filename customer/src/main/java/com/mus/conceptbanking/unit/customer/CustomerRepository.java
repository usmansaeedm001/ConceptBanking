package com.mus.conceptbanking.unit.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Usman
 * @created 10/16/2022 - 12:38 AM
 * @project OpenBanking
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	/******
	 *
	 * Query By Uuid
	 *
	 **********/
	boolean existsByUuid(String uuid);
	boolean existsByUuidAndIsActiveTrue(String uuid);
	Optional<Customer> findByUuid(String uuid);
	Optional<Customer> findByUuidAndIsActiveTrue(String uuid);
	List<Customer> findAllByUuidIn(List<String> uuidList);
	List<Customer> findAllByUuidInAndIsActiveTrue(List<String> uuidList);
	void deleteAllByUuidIn(List<String> uuids);
	void deleteAllByUuidInAndIsActiveTrue(List<String> uuids);
	/******
	 *
	 * Query By unique MobileNo
	 *
	 **********/
	Optional<Customer> findByMobileNo(String unique);
	Optional<Customer> findByMobileNoAndIsActiveTrue(String unique);
	boolean existsByMobileNoAndUuidNot(String mobileNo, String uuid);
	boolean existsByMobileNoAndUuidNotAndIsActiveTrue(String mobileNo, String uuid);

}