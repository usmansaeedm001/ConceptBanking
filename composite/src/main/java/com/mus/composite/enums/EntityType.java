package com.mus.composite.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mus.framework.face.EnumerationFace;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * @author Usman
 * @created 8/29/2022 - 10:58 AM
 * @project poc
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EntityType implements EnumerationFace {
	NOOP("000", "No operation"),
	CUSTOMER("001", "Customer"),
	ACCOUNT("002", "Account"),
	BANK("003", "Bank"),
	WALLET("004", "Wallet"),
	APPLICATIONCUSTOMER("005", "Applicaition Customer"),
	BANKACCOUNT("006", "Bank Account"),
	USERCARD("007", "User card"),
	CARDPERMISSION("008", "card permission"),
	CARDPRODUCT("009", "Card product"),
	USERADDRESS("010", "User Address"),
	BIN("011", "Bin"),
	SCHEMECATEGORY("012", "Scheme Category"),
	SCHEME("013", "Schema"),
	DELIVERYADDRESS("0014", "Delivery address"),
	SHIPMENTDETAIL("0015", "Shipment details"),
	CARD("0016", "Card");
	String code;
	String value;

	EntityType(String code, String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public static EntityType getType(String code) {
		return stream(values()).filter(type -> type.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
	}

	public static boolean isValid(String code) {
		return stream(values()).anyMatch(type -> type.getCode().equalsIgnoreCase(code));
	}

	public static List<EntityType> getAllTypes() {
		return stream(values()).collect(toList());
	}

	@Override
	public String toString() {
		return code;
	}
}
