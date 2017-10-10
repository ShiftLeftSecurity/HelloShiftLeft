package io.shiftleft.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@ToString(callSuper = true)
@Entity
public class Patient {
	public Patient() {
	}

	public Patient(int patientId, String patientFirstName, String patientLastName, Date dateOfBirth,
                   int patientWeight, int patientHeight, String medications,  int body_temperature_deg_c, int heartRate,
                   int pulseRate, int bpDiastolic) {
		super();
		this.patientId = patientId;
		this.patientFirstName = patientFirstName;
		this.patientLastName = patientLastName;
		this.dateOfBirth = dateOfBirth;
		this.patientWeight = patientWeight;
		this.patientHeight = patientHeight;
		this.medications = medications;
		this.body_temp_deg_c = body_temperature_deg_c;
		this.heartRate = heartRate;
		this.pulse_rate = pulseRate;
		this.bpDiastolic = bpDiastolic;
	}

	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Setter
	@Getter
	private int patientId;

	@Setter
	@Getter
	private String patientFirstName;

	@Setter
	@Getter
	private String patientLastName;

	@Setter
	@Getter
	private Date dateOfBirth;

	@Setter
	@Getter
	private int patientWeight;

	@Setter
	@Getter
	private int patientHeight;

	@Setter
	@Getter
	private String medications;

	@Setter
	@Getter
	private int body_temp_deg_c;

	@Setter
	@Getter
	private int heartRate;

	@Setter
	@Getter
	private int pulse_rate;

	@Setter
	@Getter
	private int bpDiastolic;

}
