package com.mus.conceptbanking.unit.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Usman
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
public interface CardRepository extends JpaRepository<Card, Long> {
	/******
	 *
	 * Query By Uuid
	 *
	 **********/
	boolean existsByUuid(String uuid);
	boolean existsByUuidAndIsActiveTrue(String uuid);
	Optional<Card> findByUuid(String uuid);
	Optional<Card> findByUuidAndIsActiveTrue(String uuid);
	List<Card> findAllByUuidIn(List<String> uuidList);
	List<Card> findAllByUuidInAndIsActiveTrue(List<String> uuidList);
	void deleteAllByUuidIn(List<String> uuids);
	void deleteAllByUuidInAndIsActiveTrue(List<String> uuids);
	/******
	 *
	 * Query By CustomerUuid
	 *
	 **********/
	boolean existsByCustomerUuid(String uuid);
	boolean existsByCustomerUuidAndIsActiveTrue(String uuid);
	Optional<Card> findByUuidAndCustomerUuid(String uuid, String parentUuid);
	Optional<Card> findByUuidAndCustomerUuidAndIsActiveTrue(String uuid, String parentUuid);
	Optional<Card> findByCustomerUuid(String uuid);
	Optional<Card> findByCustomerUuidAndIsActiveTrue(String uuid);
	List<Card> findAllByCustomerUuid(String uuid);
	List<Card> findAllByCustomerUuidAndIsActiveTrue(String uuid);
	Boolean existsByCustomerUuidAndUuidNot(String customerUuid, String uuid);
	Boolean existsByCustomerUuidAndUuidNotAndIsActiveTrue(String customerUuid, String uuid);
	/******
	 *
	 * Query By AccountUuid
	 *
	 **********/
	boolean existsByAccountUuid(String uuid);
	boolean existsByAccountUuidAndIsActiveTrue(String uuid);
	Optional<Card> findByUuidAndAccountUuid(String uuid, String parentUuid);
	Optional<Card> findByUuidAndAccountUuidAndIsActiveTrue(String uuid, String parentUuid);
	Optional<Card> findByAccountUuid(String uuid);
	Optional<Card> findByAccountUuidAndIsActiveTrue(String uuid);
	List<Card> findAllByAccountUuid(String uuid);
	List<Card> findAllByAccountUuidAndIsActiveTrue(String uuid);
	Boolean existsByAccountUuidAndUuidNot(String accountUuid, String uuid);
	Boolean existsByAccountUuidAndUuidNotAndIsActiveTrue(String accountUuid, String uuid);
	/******
	 *
	 * Query By unique CardSerialNo
	 *
	 **********/
	Optional<Card> findByCardSerialNo(String unique);
	Optional<Card> findByCardSerialNoAndIsActiveTrue(String unique);
	boolean existsByCardSerialNoAndUuidNot(String cardSerialNo, String uuid);
	boolean existsByCardSerialNoAndUuidNotAndIsActiveTrue(String cardSerialNo, String uuid);

}