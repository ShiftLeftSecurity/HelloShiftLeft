package io.shiftleft.controller;

import io.shiftleft.data.DataLoader;
import io.shiftleft.model.Patient;
import io.shiftleft.repository.PatientRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin checks login
 */

@RestController
public class PatientController {

  private static Logger log = LoggerFactory.getLogger(PatientController.class);

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
    if (pat != null) {
      log.info("First Patient is {}", pat.toString());
    }
    return patientRepository.findAll();
  }

}
