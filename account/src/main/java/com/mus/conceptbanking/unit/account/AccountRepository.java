package com.mus.conceptbanking.unit.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Usman
 * @created 10/16/2022 - 12:39 AM
 * @project OpenBanking
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
	/******
	 *
	 * Query By Uuid
	 *
	 **********/
	boolean existsByUuid(String uuid);
	boolean existsByUuidAndIsActiveTrue(String uuid);
	Optional<Account> findByUuid(String uuid);
	Optional<Account> findByUuidAndIsActiveTrue(String uuid);
	List<Account> findAllByUuidIn(List<String> uuidList);
	List<Account> findAllByUuidInAndIsActiveTrue(List<String> uuidList);
	void deleteAllByUuidIn(List<String> uuids);
	void deleteAllByUuidInAndIsActiveTrue(List<String> uuids);
	/******
	 *
	 * Query By CustomerUuid
	 *
	 **********/
	boolean existsByCustomerUuid(String uuid);
	boolean existsByCustomerUuidAndIsActiveTrue(String uuid);
	Optional<Account> findByUuidAndCustomerUuid(String uuid, String parentUuid);
	Optional<Account> findByUuidAndCustomerUuidAndIsActiveTrue(String uuid, String parentUuid);
	Optional<Account> findByCustomerUuid(String uuid);
	Optional<Account> findByCustomerUuidAndIsActiveTrue(String uuid);
	List<Account> findAllByCustomerUuid(String uuid);
	List<Account> findAllByCustomerUuidAndIsActiveTrue(String uuid);
	Boolean existsByCustomerUuidAndUuidNot(String customerUuid, String uuid);
	Boolean existsByCustomerUuidAndUuidNotAndIsActiveTrue(String customerUuid, String uuid);
	/******
	 *
	 * Query By unique AccountNo
	 *
	 **********/
	Optional<Account> findByAccountNo(String unique);
	Optional<Account> findByAccountNoAndIsActiveTrue(String unique);
	boolean existsByAccountNoAndUuidNot(String accountNo, String uuid);
	boolean existsByAccountNoAndUuidNotAndIsActiveTrue(String accountNo, String uuid);

}