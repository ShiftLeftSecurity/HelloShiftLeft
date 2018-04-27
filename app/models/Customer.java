package models;

import io.ebean.Model;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true, exclude = { "id", "ssn", "tin" })
@Entity
public class Customer extends Model {
	public Customer() {
	}

	public Customer(String customerId, int clientId, String firstName, String lastName, Date dateOfBirth,
					String ssn, String socialInsurancenum, String tin, String phoneNumber, Address address,
					Set<Account> accounts) {
		super();
		this.clientId = clientId;
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.ssn = ssn;
		this.socialInsurancenum = socialInsurancenum;
		this.tin = tin;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.accounts = accounts;
	}

	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Setter
	@Getter
	private String customerId;

	@Setter
	@Getter
	private int clientId;

	@Setter
	@Getter
	private String firstName;

	@Setter
	@Getter
	private String lastName;

	@Setter
	@Getter
	private Date dateOfBirth;

	@Setter
	@Getter
	private String ssn;

	@Setter
	@Getter
	private String socialInsurancenum;

	@Setter
	@Getter
	private String tin;

	@Setter
	@Getter
	private String phoneNumber;

	@Setter
	@Getter
	@OneToOne(cascade = { CascadeType.ALL })
	private Address address;

	@Setter
	@Getter
	@OneToMany(cascade = { CascadeType.ALL })
	private Set<Account> accounts;

}
