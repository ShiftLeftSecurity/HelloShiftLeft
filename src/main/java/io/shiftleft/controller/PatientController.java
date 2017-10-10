package io.shiftleft.controller;

import io.shiftleft.model.Patient;
import io.shiftleft.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin checks login
 */
@Slf4j
@RestController
public class PatientController {


  @Autowired
  private PatientRepository patientRepository;

  /**
   * Gets all customers.
   *
   * @return the customers
   */
  @RequestMapping(value = "/patients", method = RequestMethod.GET)
  public Iterable<Patient> getPatient() {
    Patient pat = patientRepository.findOne(1l);
    log.info("First Patient is {}", pat.toString());
    return patientRepository.findAll();
  }

}
