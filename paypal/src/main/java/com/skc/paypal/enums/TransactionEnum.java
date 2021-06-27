package com.skc.paypal.enums;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TransactionEnum {
	SUCCESS_URL("PAY/SUCCESS"),
	CANCEL_URL("PAY/CANCEL");
	
	@Getter
	private String value;
	
	public static List<String> getAllValues(){
		return List.of(TransactionEnum.values()).stream().map(data -> data.value).collect(Collectors.toList());
	}
}
