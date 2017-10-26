package io.shiftleft.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String address1;

  private String address2;

  private String city;

  private String state;

  private String zipCode;

  public long getId() {
    return id;
  }

  public String getAddress1() {
    return address1;
  }

  public String getAddress2() {
    return address2;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  @Override
  public String toString() {
    return "Address [id=" + id + ", address1=" + address1 + ", address2=" + address2 + ", city=" + city + ", state="
        + state + ", zipCode=" + zipCode + "]";
  }

}
