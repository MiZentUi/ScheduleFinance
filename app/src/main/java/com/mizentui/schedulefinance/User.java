package com.mizentui.schedulefinance;

import java.util.LinkedHashMap;

public class User {

	private final String username;
	private final LinkedHashMap<Long, Long> transactions;

	public User(String username, LinkedHashMap<Long, Long> transactions) {
		this.username = username;
		this.transactions = transactions;
	}

	public String getUsername() {
		return username;
	}

	public LinkedHashMap<Long, Long> getTransactions() {
		return transactions;
	}
}
