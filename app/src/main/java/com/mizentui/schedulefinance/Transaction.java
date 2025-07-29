package com.mizentui.schedulefinance;

public class Transaction {

	private final Long time;
	private final Long days;

	public Transaction(Long time, Long days) {
		this.time = time;
		this.days = days;
	}

	public Long getTime() {
		return time;
	}

	public Long getDays() {
		return days;
	}
}
