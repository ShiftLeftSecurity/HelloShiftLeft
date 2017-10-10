import io.shiftleft.model.AuthToken;
import java.io.*;
import java.util.Base64;
import java.net.*;
public class DoSerialize {

  public static void main(String[] main) throws Exception{
    AuthToken authToken = new AuthToken(0);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeObject(authToken);
    String finalToken = new String(Base64.getEncoder().encode(bos.toByteArray()));
    out.writeObject(authToken);
    out.close();

    System.out.println(finalToken);
  }
}

