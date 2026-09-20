# StringEncrypt — Java SDK

Java client for the [StringEncrypt](https://www.stringencrypt.com) Web API at `https://www.stringencrypt.com/api.php`. Encrypt strings and files into polymorphic decryptor source in the selected language.

API docs: https://www.stringencrypt.com/api/

## Maven

```xml
<dependency>
  <groupId>com.pelock</groupId>
  <artifactId>stringencrypt</artifactId>
  <version>1.0.1</version>
</dependency>
```

This package is not published to Maven Central. Install locally with `mvn install`.

## Usage

Pass the activation key to the constructor (empty string = demo). Configure options with setters, then `encryptString` / `encryptFileContents` / `isDemo` / `send`.

```java
import com.pelock.stringencrypt.ErrorCode;
import com.pelock.stringencrypt.Language;
import com.pelock.stringencrypt.StringEncrypt;
import com.fasterxml.jackson.databind.JsonNode;

StringEncrypt client = new StringEncrypt("YOUR-API-KEY-HERE");
client.setLanguage(Language.JAVA);
JsonNode result = client.encryptString("Hello!", "label");

if (result != null && result.path("error").asInt(-1) == ErrorCode.SUCCESS) {
    System.out.println(result.path("source").asText());
}
```

See `examples/`. Apache-2.0. Copyright Bartosz Wójcik / PELock.
