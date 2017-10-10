package io.shiftleft.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true, exclude = { "routingNumber", "accountNumber" })
@Entity
@Table(name = "account")
public class Account {

	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Setter
	@Getter
	private String type;

	@Setter
	@Getter
	private long routingNumber;

	@Setter
	@Getter
	private long accountNumber;

	@Setter
	@Getter
	private double balance;

	@Setter
	@Getter
	private double interest;

	public Account() {
		balance = 0;
		interest = 0;
	}

	public Account(long accountNumber, long routingNumber, String type, double initialBalance, double initialInterest) {
		this.accountNumber = accountNumber;
		this.routingNumber = routingNumber;
		this.type = type;
		this.balance = initialBalance;
		this.interest = initialInterest;
	}

	public void deposit(double amount) {
		balance = balance + amount;
	}

	public void withdraw(double amount) {
		balance = balance - amount;
	}

	public void addInterest() {
		balance = balance + balance * interest;
	}

	public String toString() {
		return "" + this.accountNumber + ", " + this.routingNumber;
	}
}
