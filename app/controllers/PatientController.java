package controllers;

import static play.mvc.Results.ok;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import models.Patient;
import play.libs.Json;
import play.mvc.Result;

/**
 * Admin checks login
 */
@Slf4j
public class PatientController {
  /**
   * Gets all customers.
   *
   * @return the customers
   */
  // get /patients
  public Result getPatient() {
    Patient pat = Patient.db().find(Patient.class, 1L);
    log.info("First Patient is {}", pat);

    ArrayNode res = Json.newArray();
    Patient.db().find(Patient.class)
        .findList().forEach(account -> res.add(Json.toJson(account)));

    return ok(res);
  }

}
