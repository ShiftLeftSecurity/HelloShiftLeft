package io.shiftleft.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Patient {
  public Patient() {
  }

  public Patient(int patientId, String patientFirstName, String patientLastName, Date dateOfBirth, int patientWeight,
      int patientHeight, String medications, int body_temperature_deg_c, int heartRate, int pulseRate,
      int bpDiastolic) {
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
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private int patientId;

  private String patientFirstName;

  private String patientLastName;

  private Date dateOfBirth;

  private int patientWeight;

  private int patientHeight;

  private String medications;

  private int body_temp_deg_c;

  private int heartRate;

  private int pulse_rate;

  private int bpDiastolic;

  public long getId() {
    return id;
  }

  public int getPatientId() {
    return patientId;
  }

  public String getPatientFirstName() {
    return patientFirstName;
  }

  public String getPatientLastName() {
    return patientLastName;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public int getPatientWeight() {
    return patientWeight;
  }

  public int getPatientHeight() {
    return patientHeight;
  }

  public String getMedications() {
    return medications;
  }

  public int getBody_temp_deg_c() {
    return body_temp_deg_c;
  }

  public int getHeartRate() {
    return heartRate;
  }

  public int getPulse_rate() {
    return pulse_rate;
  }

  public int getBpDiastolic() {
    return bpDiastolic;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  public void setPatientFirstName(String patientFirstName) {
    this.patientFirstName = patientFirstName;
  }

  public void setPatientLastName(String patientLastName) {
    this.patientLastName = patientLastName;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public void setPatientWeight(int patientWeight) {
    this.patientWeight = patientWeight;
  }

  public void setPatientHeight(int patientHeight) {
    this.patientHeight = patientHeight;
  }

  public void setMedications(String medications) {
    this.medications = medications;
  }

  public void setBody_temp_deg_c(int body_temp_deg_c) {
    this.body_temp_deg_c = body_temp_deg_c;
  }

  public void setHeartRate(int heartRate) {
    this.heartRate = heartRate;
  }

  public void setPulse_rate(int pulse_rate) {
    this.pulse_rate = pulse_rate;
  }

  public void setBpDiastolic(int bpDiastolic) {
    this.bpDiastolic = bpDiastolic;
  }

  @Override
  public String toString() {
    return "Patient [id=" + id + ", patientId=" + patientId + ", patientFirstName=" + patientFirstName
        + ", patientLastName=" + patientLastName + ", dateOfBirth=" + dateOfBirth + ", patientWeight=" + patientWeight
        + ", patientHeight=" + patientHeight + ", medications=" + medications + ", body_temp_deg_c=" + body_temp_deg_c
        + ", heartRate=" + heartRate + ", pulse_rate=" + pulse_rate + ", bpDiastolic=" + bpDiastolic + "]";
  }

}
