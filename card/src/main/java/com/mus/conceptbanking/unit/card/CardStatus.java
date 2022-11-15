package com.mus.conceptbanking.unit.card;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

import com.mus.framework.annotation.Matchable;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * @author Usman
 * @created 10/16/2022 - 12:40 AM
 * @project OpenBanking
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CardStatus implements Matchable {
	INSTANCE("INST", "instance"),
	;
	String code;
	String value;

	CardStatus(String code, String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public static CardStatus getType(String code) {
		return stream(values()).filter(type -> type.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
	}

	public static boolean isValid(String code) {
		return stream(values()).anyMatch(type -> type.getCode().equalsIgnoreCase(code));
	}

	public static List<CardStatus> getAllTypes() {
		return stream(values()).collect(toList());
	}

	@Override
	public String toMatchAbleString() {
		return this.code;
	}

	@Override
	public String toString() {
		return code;
	}
}
