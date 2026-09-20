/******************************************************************************
 * StringEncrypt WebApi interface usage example.
 *
 * Version        : v1.0.1
 * Language       : Java
 * Author         : Bartosz Wójcik
 * Web page       : https://www.stringencrypt.com
 *
 *****************************************************************************/

import com.fasterxml.jackson.databind.JsonNode;
import com.pelock.stringencrypt.ErrorCode;
import com.pelock.stringencrypt.Language;
import com.pelock.stringencrypt.StringEncrypt;

public class EncryptSimple {

  public static void main(String[] args) throws Exception {
    StringEncrypt client = new StringEncrypt("YOUR-API-KEY-HERE");
    client.setLanguage(Language.JAVA);

    JsonNode result = client.encryptString("Hello!", "$label");
    if (result == null) {
      System.out.println("Cannot connect to the API.");
      System.exit(1);
    }
    if (result.path("error").asInt(-1) != ErrorCode.SUCCESS) {
      System.out.println("API error: " + result.path("error").asInt());
      System.exit(1);
    }
    System.out.println(result.path("source").asText());
  }
}
