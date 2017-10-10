package io.shiftleft.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
public class Address {

	public Address() {
	}

	public Address(String address1, String address2, String city, String state, String zipCode) {
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
	}

	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Setter
	@Getter
	private String address1;

	@Setter
	@Getter
	private String address2;

	@Setter
	@Getter
	private String city;

	@Setter
	@Getter
	private String state;

	@Setter
	@Getter
	private String zipCode;
}
