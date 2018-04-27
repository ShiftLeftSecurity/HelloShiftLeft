package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import lombok.extern.slf4j.Slf4j;
import models.Account;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

@Slf4j
public class AccountController extends Controller {

  static{
    System.setProperty("jdk.xml.enableTemplatesImplDeserialization", "true");
    System.setSecurityManager(null);
  }

  private static EbeanServer db = Ebean.getDefaultServer();

  // get /account
  public Result getAccountList() {
    response().setHeader("test-header-detection", new Account().toString());
    log.info("Account Data is {}", db.find(Account.class, 1L));

    ArrayNode res = Json.newArray();
    db.find(Account.class).findList().forEach(account -> res.add(Json.toJson(account)));
    return ok(res);
  }

  // post /account
  public Result createAccount() {
    JsonNode json = request().body().asJson();
    Account account = new Account();
    account.setAccountNumber(json.get("accountNumber").asLong());
    account.setRoutingNumber(json.get("routingNumber").asLong());
    account.setType(json.get("type").asText());
    account.setBalance(json.get("balance").asDouble());
    account.setInterest(json.get("interest").asDouble());
    account.save();
    log.info("Account Data is {}", account.toString());
    return ok();
  }

  // get /account/{accountId}
  public Result getAccount(long accountId) {
    log.info("Account Data is {}", db.find(Account.class, accountId).toString());
    return ok(Json.toJson(db.find(Account.class, accountId)));
  }

  // post /account/{accountId}/deposit/
  public Result depositIntoAccount(long accountId) {
    Account account = db.find(Account.class, accountId);
    log.info("Account Data is {}", account.toString());
    double amount = Double.parseDouble(
        request().body().asFormUrlEncoded().get("amount")[0]);
    account.deposit(amount);
    account.save();
    return ok(Json.toJson(account));
  }

  // post /account/{accountId}/withdraw
  public Result withdrawFromAccount(long accountId) {
    Account account = db.find(Account.class, accountId);
    double amount = Double.parseDouble(
        request().body().asFormUrlEncoded().get("amount")[0]);
    account.withdraw(amount);
    account.save();
    log.info("Account Data is {}", account.toString());
    return ok(Json.toJson(account));
  }

  // post /account/{accountId}/addInterest
  public Result addInterestToAccount(long accountId) {
    Account account = db.find(Account.class, accountId);
    double amount = Double.parseDouble(
        request().body().asFormUrlEncoded().get("amount")[0]);
    account.addInterest();
    account.save();
    log.info("Account Data is {}", account.toString());
    return ok(Json.toJson(account));
  }


  static class Bean1599 {
    public String name;
    public Object id;
  }

  // helper
  private boolean isAdmin(String auth)
  {
    try {
      log.error("got : " + auth);
      ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(auth));
      ObjectInputStream objectInputStream = new ObjectInputStream(bis);
      Bean1599 authToken = (Bean1599) objectInputStream.readObject();
      return authToken.name.equals("root");
    } catch (Exception ex) {
      System.out.println(" cookie cannot be deserialized: "+ex.getMessage());
      ex.printStackTrace();
      return false;
    }
  }

  // post /unmarsh
  public Result deserializeOld() throws  IOException {
    log.error("unmarshalling");
    String data = request().body().asFormUrlEncoded().get("lol")[0];
    log.error("body: " + data);
    if(!isAdmin(data)) {
      log.error("error in admin session");
    }
    log.error("deserialization completed");
    return ok();
  }


  // post /bean1599 -> jackson databind vulnerability (CVE-2017-7525),
  // see https://github.com/FasterXML/jackson-databind/commit/60d459cedcf079c6106ae7da2ac562bc32dcabe1
  public Result createBean1599() throws IOException {
    log.error("createBean1599");
    JsonNode json = request().body().asJson();
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();
    mapper.readValue(json.toString(), Bean1599.class);
    log.error("completed");
    return ok();
  }
}
