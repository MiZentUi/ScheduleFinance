package com.mizentui.schedulefinance;

public class MonthTransactions {

	private final int month;
	private final int year;
	private final int transactionsQuantity;

	public MonthTransactions(int month, int year) {
		this.month = month;
		this.year = year;
		transactionsQuantity = 0;
	}

	public MonthTransactions(int month, int year, int transactionsQuantity) {
		this.month = month;
		this.year = year;
		this.transactionsQuantity = transactionsQuantity;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	public int getTransactionsQuantity() {
		return transactionsQuantity;
	}
}
