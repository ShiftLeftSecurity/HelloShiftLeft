import com.typesafe.config.Config;
import data.DataBuilder;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import javax.inject.*;
import lombok.extern.slf4j.Slf4j;
import models.Customer;
import models.Patient;
import play.inject.ApplicationLifecycle;
import play.Environment;

@Singleton
@Slf4j
public class ApplicationStart {
  private static EbeanServer db = Ebean.getDefaultServer();

  public ApplicationStart() {
    log.debug("Loading test data...");

    DataBuilder builder = new DataBuilder();

    for(Customer customer : builder.createCustomers()) {
      db.save(customer);
    }
    for (Patient patient : builder.createPatients()) {
      db.save(patient);
    }
    log.debug("Test data loaded...");
  }

  private boolean connectToAws() {
    Config config = play.api.Play.current().asJava().config();

    log.info("Start Loading AWS Properties");
    log.info("AWS AccessKey is {} and SecretKey is {}", config.getString("aws.accesskey"),
        config.getString("aws.secretkey"));
    log.info("AWS Bucket is {}", config.getString("aws.bucket"));
    log.info("End Loading AWS Properties");

    // Connect to AWS resources and do something

    return true;
  }
}