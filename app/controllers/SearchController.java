package controllers;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import play.mvc.Controller;
import play.mvc.Result;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Search login
 */
public class SearchController extends Controller {
  private static EbeanServer db = Ebean.getDefaultServer();

  // get /search/user
  public Result doGetSearch() {
    try {
      String q = request().getQueryString("q");
      Object message;
      ExpressionParser parser = new SpelExpressionParser();
      Expression exp = parser.parseExpression(q);
      message = exp.getValue();
      return ok(message.toString());
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
    return internalServerError();
  }
}
