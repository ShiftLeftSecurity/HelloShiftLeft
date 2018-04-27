package controllers;

import java.io.File;
import models.AuthToken;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import play.mvc.Controller;
import play.mvc.Http.Cookie;
import play.mvc.Result;

/**
 * Admin checks login
 */
public class AdminController extends Controller {
  private String fail = "/";

  // helper
  private boolean isAdmin(String auth)
  {
    try {
      ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(auth));
      ObjectInputStream objectInputStream = new ObjectInputStream(bis);
      Object authToken = objectInputStream.readObject();
      return ((AuthToken) authToken).isAdmin();
    } catch (Exception ex) {
      System.out.println(" cookie cannot be deserialized: "+ex.getMessage());
      return false;
    }
  }

  // post /admin/printSecrets
  public Result doPostPrintSecrets() {
    return redirect(fail);
  }


  // get /admin/printSecrets
  public Result doGetPrintSecrets() throws Exception {
    Cookie auth = request().cookie("auth");
    if (auth == null) {
      return redirect(fail);
    }

    String authToken = auth.value();
    if(!isAdmin(authToken)) {
      return redirect(fail);
    }

    return ok(new File("./static/calculations.csv"));
  }

  /**
   * Handle login attempt
   * @return redirect to company numbers
   * @throws Exception
   */
 // post /admin/login
  public Result doPostLogin() throws Exception {
    String succ = "/admin/printSecrets";

    try {
      // no cookie no fun
      String auth = request().cookie("auth").value();
      if (!auth.equals("notset")) {
        if (isAdmin(auth)) {
          session().put("auth", auth);
          return redirect(succ);
        }
      }
    } catch (NullPointerException npe) {

    }

    try {
      String pass = request().body()
          .asFormUrlEncoded().get("password")[0];

      // compare pass
      if(pass != null && pass.length()>0 && pass.equals("shiftleftsecret"))
      {
        AuthToken authToken = new AuthToken(AuthToken.ADMIN);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(authToken);
        String cookieValue = new String(Base64.getEncoder().encode(bos.toByteArray()));
        response().setCookie(Cookie.builder("auth", cookieValue).build());

        // cookie is lost after redirection
        session().put("auth", cookieValue);

        return redirect(succ);
      }
      return redirect(fail);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      // no succ == fail
      return redirect(fail);
    }
  }

  /**
   * Same as POST but just a redirect
   * @return redirect
   */
  // get /admin/login
  public Result doGetLogin() {
    return redirect(fail);
  }
}
