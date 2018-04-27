package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.typesafe.config.Config;
import exception.CustomerNotFoundException;
import exception.InvalidCustomerRequestException;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Query;
import io.ebean.RawSql;
import io.ebean.RawSqlBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import models.Account;
import models.Address;
import models.Customer;
import org.apache.commons.codec.digest.DigestUtils;

import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.*;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.twirl.api.Html;
import play.twirl.api.HtmlFormat;

/**
 * Customer Controller exposes a series of RESTful endpoints
 */
@Slf4j
public class CustomerController extends Controller {
	private WSClient ws;
  private FormFactory formFactory;

	@Inject
	CustomerController(WSClient ws, FormFactory formFactory) {
    this.ws = ws;
    this.formFactory = formFactory;
  }

  private static EbeanServer db = Ebean.getDefaultServer();

  public void init() {
    Config config = play.api.Play.current().asJava().config();
		log.info("Start Loading SalesForce Properties");
		log.info("Url is {}", config.getString("sfdc.url"));
		log.info("UserName is {}", config.getString("sfdc.username"));
		log.info("Password is {}", config.getString("sfdc.password"));
		log.info("End Loading SalesForce Properties");
	}

	private void dispatchEventToSalesForce(String event) throws Exception {
    Config config = play.api.Play.current().asJava().config();
    CompletionStage<? extends WSResponse> promise = ws.url(config.getString("sfdc.url"))
        .setBody(event)
        .setAuth(config.getString("sfdc.username"), config.getString("sfdc.password"),
            WSAuthScheme.BASIC).execute();

    try {
      WSResponse res = promise.toCompletableFuture().join();
      log.info("Response from SFDC is {}", res.getStatus());
    } catch (CompletionException exc) {
      throw new IOException(exc.getCause());
    }
	}

	/**
	 * Get customer using id. Returns HTTP 404 if customer not found
	 *
	 */
	// get /customers/{customerId}
	public Result getCustomer(Long customerId) {

		/* validate customer Id parameter */
    if (null == customerId) {
      throw new InvalidCustomerRequestException();
    }

    Customer customer = Customer.db().find(Customer.class, customerId);
		if (null == customer) {
		  throw new CustomerNotFoundException();
	  }

	  Account account = new Account(4242L, 1234, "savings", 1, 0);
	  log.info("Account Data is {}", account);
	  log.info("Customer Data is {}", customer);

    try {
      dispatchEventToSalesForce(String.format(" Customer %s Logged into SalesForce", customer));
    } catch (Exception e) {
      log.error("Failed to Dispatch Event to SalesForce . Details {} ", e.getLocalizedMessage());

    }

      return ok(Json.toJson(customer));
  }

  /**
   * Get customer using id and raw sql. Returns HTTP 404 if customer not found
   *
   */
  // get /rawcustomers/{customerId}
  public Result getRawCustomer(String customerId) {

		/* validate customer Id parameter */
    if (null == customerId) {
      throw new InvalidCustomerRequestException();
    }

    String sqlQuery = "SELECT first_name, last_name FROM customer WHERE id = " + customerId;

    RawSql rawSql = RawSqlBuilder.parse(sqlQuery).create();

    Query<Customer> query = db.find(Customer.class);
    query.setRawSql(rawSql);

    Customer customer = query.findOne();

    if (null == customer) {
      throw new CustomerNotFoundException();
    }

    return ok(Json.toJson(customer));
  }

    /**
     * Get customer using id and raw sql. Returns HTTP 404 if customer not found
     *
     */
    // get /rawcustomersbyname/{firstName}
    public Result getRawCustomerByName(String firstName) {

        /* validate customer Id parameter */
        if (null == firstName) {
            throw new InvalidCustomerRequestException();
        }

        String sqlQuery = "SELECT first_name, last_name FROM customer WHERE first_name = '" + firstName + "'";

        RawSql rawSql = RawSqlBuilder.parse(sqlQuery).create();

        Query<Customer> query = db.find(Customer.class);
        query.setRawSql(rawSql);

        Customer customer = query.findOne();

        if (null == customer) {
            throw new CustomerNotFoundException();
        }

        return ok(Json.toJson(customer));
    }

  /**
   * Handler for / loads the index.scala.html
   */
  // get /
  public Result index() throws IOException {
    return ok(views.html.index.render());
  }

  /**
   * Check if settings= is present in cookie
   * @param request
   * @return
   */
  private boolean checkCookie(Request request) throws Exception {
    try {
      return request.getHeaders().get("Cookie").get().startsWith("settings=");
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }

    return false;
  }

  /**
   * restores the preferences on the filesystem
   *
   * @throws Exception
   */
  // get /loadSettings
  public Result loadSettings() throws Exception {
    // get cookie values
    if (!checkCookie(request())) {
      return badRequest("cookie is incorrect");
    }
    String md5sum = request().getHeaders().get("Cookie").get().substring("settings=".length(), 41);
    File folder = new File("./static/");
    File[] listOfFiles = folder.listFiles();
    String filecontent = new String();
    for (File f : listOfFiles) {
      // not efficient, i know
      filecontent = new String();
      byte[] encoded = Files.readAllBytes(f.toPath());
      filecontent = new String(encoded, StandardCharsets.UTF_8);
      if (filecontent.contains(md5sum)) {
        // this will send me to the developer hell (if exists)

        // encode the file settings, md5sum is removed
        String s = new String(Base64.getEncoder().encode(filecontent.replace(md5sum, "").getBytes()));
        // setting the new cookie
        response().setHeader("Cookie", "settings=" + s + "," + md5sum);
        return ok();
      }
    }

    return badRequest();
  }

  public void trivialFlowToFile(String s) throws  Exception {
      File file = new File("./notinterestingbutyouarereportingme/" + s);
      if(!file.exists()) {
          file.getParentFile().mkdirs();
      }
      FileOutputStream fos = new FileOutputStream(file, true);
      fos.write(s.getBytes());
      fos.close();
  }

  /**
   * Saves the preferences (screen resolution, language..) on the filesystem
   *
   * @throws Exception
   */
  // get /saveSettings
  public Result saveSettings() throws Exception {
    // "Settings" will be stored in a cookie
    // schema: base64(filename,value1,value2...), md5sum(base64(filename,value1,value2...))

    if (!checkCookie(request())) {
      return badRequest("cookie is incorrect");
    }

    String settingsCookie = request().getHeaders().get("Cookie").get();
    trivialFlowToFile(settingsCookie);

    String[] cookie = settingsCookie.split(",");
	  if(cookie.length<2) {
      return badRequest("Malformed cookie");
    }

    String base64txt = cookie[0].replace("settings=","");

    // Check md5sum
    String cookieMD5sum = cookie[1];
    String calcMD5Sum = DigestUtils.md5Hex(base64txt);
	  if(!cookieMD5sum.equals(calcMD5Sum)) {
      return badRequest("invalid md5");
    }

    // Now we can store on filesystem
    String[] settings = new String(Base64.getDecoder().decode(base64txt)).split(",");
	  // storage will have ClassPathResource as basepath
	  File file = new File("./static/" + settings[0]);
    if(!file.exists()) {
      file.getParentFile().mkdirs();
    }

    FileOutputStream fos = new FileOutputStream(file, true);
    // First entry is the filename -> remove it
    String[] settingsArr = Arrays.copyOfRange(settings, 1, settings.length);
    // on setting at a linez
    fos.write(String.join("\n",settingsArr).getBytes());
    fos.write(("\n"+cookie[cookie.length-1]).getBytes());
    fos.close();
    return ok("Settings Saved");
  }

  /**
   * Debug test for saving and reading a customer
   *
   * @throws IOException
   */
  // get /debug
  public Result debug() throws IOException {

    // empty for now, because we debug
    Set<Account> accounts1 = new HashSet<Account>();
    //dateofbirth example -> "1982-01-10"

    Customer customer1 = formFactory.form(Customer.class)
        .bindFromRequest().get();

    customer1.setAccounts(accounts1);
    customer1.setAddress(new Address("Debug str",
        "", "Debug city", "CA", "12345"));

    customer1.save();

    response().setHeader("Location", String.format("%s/customers/%s",
        request().host(), customer1.getId()));

    return created(new Html(customer1.toString().toLowerCase().replace("script","")));
  }

	/**
	 * Debug test for saving and reading a customer
	 *
	 */
	// get /debugEscaped
	public Result debugEscaped() throws IOException {
    Html html = HtmlFormat.escape(request().getQueryString("firstName"));
		System.out.println(html.body());
		return ok(html);
	}
	/**
	 * Gets all customers.
	 *
	 * @return the customers
	 */
	// get /customers
	public Result getCustomers() {
    ArrayNode res = Json.newArray();
    db.find(Customer.class).findList().forEach(customer -> res.add(Json.toJson(customer)));
    return ok(res);
	}

	/**
	 * Create a new customer and return in response with HTTP 201
	 *
	 * @return created customer
	 */
	// post /customers
	public Result createCustomer() {
    Customer customer1 = formFactory.form(Customer.class)
        .bindFromRequest().get();

    customer1.save();

    response().setHeader("Location", String.format("%s/customers/%s",
        request().path(), customer1.getId()));

    return created(Json.toJson(customer1));
	}

	/**
	 * Update customer with given customer id.
	 *
	 */
	// put /customers/{customerId}
	public Result updateCustomer(Long customerId) {
    Customer customer1 = formFactory.form(Customer.class)
        .bindFromRequest().get();

    Customer found = db.find(Customer.class, customerId);

    if (found != null) {
      found.setCustomerId(customer1.getCustomerId());
      found.setAddress(customer1.getAddress());
      found.setAccounts(customer1.getAccounts());
      found.setClientId(customer1.getClientId());
      found.setDateOfBirth(customer1.getDateOfBirth());
      found.setFirstName(customer1.getFirstName());
      found.setLastName(customer1.getLastName());
      found.setPhoneNumber(customer1.getPhoneNumber());
      found.setSocialInsurancenum(customer1.getSocialInsurancenum());
      found.setSsn(customer1.getSsn());
      found.setTin(customer1.getTin());
      found.update();
      return ok();
		} else {
      return badRequest();
		}
	}

	/**
	 * Deletes the customer with given customer id if it exists and returns
	 * HTTP204.
	 *
	 * @param customerId
	 *            the customer id
	 */
	// delete /customers/{customerId}
	public Result removeCustomer(Long customerId) {

    db.find(Customer.class, customerId).delete();

    return noContent();
	}
}
