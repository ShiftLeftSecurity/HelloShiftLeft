package io.shiftleft.data;

import java.io.IOException;
import java.util.Properties;

import io.shiftleft.model.Patient;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Component;
import io.shiftleft.model.Customer;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import io.shiftleft.repository.CustomerRepository;
import io.shiftleft.repository.PatientRepository;

@Component
@Configuration
@EnableEncryptableProperties
@PropertySource({ "classpath:config/application-aws.properties", "classpath:config/application-mysql.properties",
    "classpath:config/application-sfdc.properties" })
public class DataLoader implements CommandLineRunner {

  private static Logger log = LoggerFactory.getLogger(DataLoader.class);

  @Autowired
  private DataBuilder dataBuilder;

  @Autowired
  Environment env;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private PatientRepository patientRepository;

  /*
   * Internal workings of @EnableEncryptableProperties annotation
   */
  private String getSecurePassword(String masterPassword) throws IOException {
    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    encryptor.setPassword(masterPassword);
    Properties props = new EncryptableProperties(encryptor);
    props.load(this.getClass().getClassLoader().getResourceAsStream("config/application-mysql.properties"));
    return props.getProperty("db.password");
  }

  private boolean connectToAws() {

    log.info("Start Loading AWS Properties");
    log.info("AWS AccessKey is {} and SecretKey is {}", env.getProperty("aws.accesskey"),
        env.getProperty("aws.secretkey"));
    log.info("AWS Bucket is {}", env.getProperty("aws.bucket"));
    log.info("End Loading AWS Properties");

    // Connect to AWS resources and do something

    return true;
  }

  private boolean connectToMySQL() {

    log.info("Start Loading MySQL Properties");
    log.info("Url is {}", env.getProperty("db.url"));
    log.info("UserName is {}", env.getProperty("db.username"));
    log.info("Password is {}", env.getProperty("db.password"));
    log.info("End Loading MySQL Properties");

    // Connect to DB MySQL resources and do something

    return true;
  }

  @Override
  public void run(String... arg0) throws Exception {

    SimpleCommandLinePropertySource ps = new SimpleCommandLinePropertySource(arg0);
    String encryptor = (String) ps.getProperty("jasypt.encryptor.password");
    log.info("JASP Master Creds is {}", encryptor);

    connectToMySQL();

    connectToAws();

    log.debug("Loading test data...");
    for (Customer customer : dataBuilder.createCustomers()) {
      customerRepository.save(customer);
    }
    for (Patient patient : dataBuilder.createPatients()) {
      patientRepository.save(patient);
    }
    log.debug("Test data loaded...");
  }
}
