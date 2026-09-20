/******************************************************************************
 * StringEncrypt WebApi interface
 *
 * Version        : v1.0.1
 * Language       : Java
 * Author         : Bartosz Wójcik
 * Web page       : https://www.stringencrypt.com
 *
 *****************************************************************************/

package com.pelock.stringencrypt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

/**
 * Stateful HTTP client: pass the activation key to the constructor, configure other options via
 * setters, then {@link #send()}.
 *
 * <p>POST field {@code code} (activation key) is the value given at construction (empty string =
 * demo).
 *
 * <p>For {@link Command#ENCRYPT} with {@link #setCompression(boolean)} {@code true}, the API
 * returns {@code source} as base64-encoded zlib of the decryptor text. {@link #send()} decodes that
 * automatically so {@code source} is always plain text on success (unless {@link
 * #setDecompressEncryptSource(boolean)} {@code false}).
 */
public class StringEncrypt {

  public static final String DEFAULT_API_URL = "https://www.stringencrypt.com/api.php";

  private static final ObjectMapper JSON = new ObjectMapper();

  private final String apiKey;
  private String apiUrl = DEFAULT_API_URL;

  private boolean decompressEncryptSource = true;
  private Command command = null;
  private String label = "Label";
  private String inputString = null;
  private byte[] inputBytes = null;
  private boolean compression = false;
  private Language language = Language.PHP;
  private Object highlight = Boolean.FALSE;
  private int cmdMin = 1;
  private int cmdMax = 3;
  private boolean local = false;
  private boolean unicode = true;
  private String langLocale = "en_US.utf8";
  private String ansiEncoding = "WINDOWS-1250";
  private NewLine newLines = NewLine.LF;
  private String template = null;
  private boolean returnTemplate = false;
  private boolean includeTags = false;
  private boolean includeExample = false;
  private boolean includeDebugComments = false;

  public StringEncrypt() {
    this("");
  }

  public StringEncrypt(String apiKey) {
    this.apiKey = apiKey == null ? "" : apiKey;
  }

  public void setApiUrl(String apiUrl) {
    this.apiUrl = Objects.requireNonNull(apiUrl, "apiUrl");
  }

  public String getApiUrl() {
    return apiUrl;
  }

  public JsonNode isDemo() throws IOException {
    Command previous = this.command;
    setCommand(Command.IS_DEMO);
    JsonNode result = send();
    this.command = previous;
    return result;
  }

  public JsonNode encryptFileContents(String filePath, String label) throws IOException {
    return encryptFileContents(filePath == null ? null : Path.of(filePath), label);
  }

  public JsonNode encryptFileContents(Path filePath, String label) throws IOException {
    if (filePath == null || !Files.isRegularFile(filePath)) {
      return null;
    }
    byte[] raw = Files.readAllBytes(filePath);
    if (raw.length == 0) {
      return null;
    }

    Command savedCommand = this.command;
    String savedInputString = this.inputString;
    byte[] savedInputBytes = this.inputBytes;
    String savedLabel = this.label;

    setCommand(Command.ENCRYPT).setBytes(raw).setLabel(label);
    JsonNode result = send();

    this.command = savedCommand;
    this.inputString = savedInputString;
    this.inputBytes = savedInputBytes;
    this.label = savedLabel;
    return result;
  }

  public JsonNode encryptString(String string, String label) throws IOException {
    Command savedCommand = this.command;
    String savedInputString = this.inputString;
    byte[] savedInputBytes = this.inputBytes;
    String savedLabel = this.label;

    setCommand(Command.ENCRYPT).setString(string).setLabel(label);
    JsonNode result = send();

    this.command = savedCommand;
    this.inputString = savedInputString;
    this.inputBytes = savedInputBytes;
    this.label = savedLabel;
    return result;
  }

  public boolean getDecompressEncryptSource() {
    return decompressEncryptSource;
  }

  public StringEncrypt setDecompressEncryptSource(boolean decompressEncryptSource) {
    this.decompressEncryptSource = decompressEncryptSource;
    return this;
  }

  public Command getCommand() {
    return command;
  }

  public StringEncrypt setCommand(Command command) {
    this.command = command;
    return this;
  }

  public String getLabel() {
    return label;
  }

  public StringEncrypt setLabel(String label) {
    this.label = label;
    return this;
  }

  /** UTF-8 text input; clears raw bytes input. */
  public StringEncrypt setString(String string) {
    this.inputString = string;
    this.inputBytes = null;
    return this;
  }

  /** Raw binary input; clears string input. */
  public StringEncrypt setBytes(byte[] bytes) {
    this.inputBytes = bytes;
    this.inputString = null;
    return this;
  }

  public boolean getCompression() {
    return compression;
  }

  public StringEncrypt setCompression(boolean compression) {
    this.compression = compression;
    return this;
  }

  public Language getLanguage() {
    return language;
  }

  public StringEncrypt setLanguage(Language language) {
    this.language = language;
    return this;
  }

  public Object getHighlight() {
    return highlight;
  }

  /** {@code false}, or a highlight mode string such as {@code geshi} / {@code js}. */
  public StringEncrypt setHighlight(Object highlight) {
    this.highlight = highlight;
    return this;
  }

  public int getCmdMin() {
    return cmdMin;
  }

  public StringEncrypt setCmdMin(int cmdMin) {
    this.cmdMin = cmdMin;
    return this;
  }

  public int getCmdMax() {
    return cmdMax;
  }

  public StringEncrypt setCmdMax(int cmdMax) {
    this.cmdMax = cmdMax;
    return this;
  }

  public boolean getLocal() {
    return local;
  }

  public StringEncrypt setLocal(boolean local) {
    this.local = local;
    return this;
  }

  public boolean getUnicode() {
    return unicode;
  }

  public StringEncrypt setUnicode(boolean unicode) {
    this.unicode = unicode;
    return this;
  }

  public String getLangLocale() {
    return langLocale;
  }

  public StringEncrypt setLangLocale(String langLocale) {
    this.langLocale = langLocale;
    return this;
  }

  public String getAnsiEncoding() {
    return ansiEncoding;
  }

  public StringEncrypt setAnsiEncoding(String ansiEncoding) {
    this.ansiEncoding = ansiEncoding;
    return this;
  }

  public NewLine getNewLines() {
    return newLines;
  }

  public StringEncrypt setNewLines(NewLine newLines) {
    this.newLines = newLines;
    return this;
  }

  public String getTemplate() {
    return template;
  }

  public StringEncrypt setTemplate(String template) {
    this.template = template;
    return this;
  }

  public boolean getReturnTemplate() {
    return returnTemplate;
  }

  public StringEncrypt setReturnTemplate(boolean returnTemplate) {
    this.returnTemplate = returnTemplate;
    return this;
  }

  public boolean getIncludeTags() {
    return includeTags;
  }

  public StringEncrypt setIncludeTags(boolean includeTags) {
    this.includeTags = includeTags;
    return this;
  }

  public boolean getIncludeExample() {
    return includeExample;
  }

  public StringEncrypt setIncludeExample(boolean includeExample) {
    this.includeExample = includeExample;
    return this;
  }

  public boolean getIncludeDebugComments() {
    return includeDebugComments;
  }

  public StringEncrypt setIncludeDebugComments(boolean includeDebugComments) {
    this.includeDebugComments = includeDebugComments;
    return this;
  }

  /** Reset request fields to defaults (reuse the same client for another call). */
  public StringEncrypt reset() {
    this.command = null;
    this.label = "$label";
    this.inputString = null;
    this.inputBytes = null;
    this.compression = false;
    this.language = Language.PHP;
    this.highlight = Boolean.FALSE;
    this.cmdMin = 1;
    this.cmdMax = 3;
    this.local = false;
    this.unicode = true;
    this.langLocale = "en_US.utf8";
    this.ansiEncoding = "WINDOWS-1250";
    this.newLines = NewLine.LF;
    this.template = null;
    this.returnTemplate = false;
    this.includeTags = false;
    this.includeExample = false;
    this.includeDebugComments = false;
    return this;
  }

  /** Build the POST body the client would send (for debugging and tests). */
  public Map<String, String> toRequestArray() {
    if (command == null) {
      throw new IllegalArgumentException("Command must be set (use setCommand()).");
    }
    switch (command) {
      case INFO:
        return buildInfoParams();
      case IS_DEMO:
        return buildIsDemoParams();
      case ENCRYPT:
        return buildEncryptParams();
      default:
        throw new IllegalArgumentException("Unknown command: " + command);
    }
  }

  /**
   * Send the request and return decoded JSON, or {@code null} on transport / JSON failure.
   */
  public JsonNode send() throws IOException {
    Map<String, String> params = toRequestArray();

    List<NameValuePair> nvps = new ArrayList<>();
    for (Map.Entry<String, String> e : params.entrySet()) {
      nvps.add(new BasicNameValuePair(e.getKey(), e.getValue()));
    }

    HttpPost post = new HttpPost(apiUrl);
    post.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));
    post.addHeader("User-Agent", "pelock/stringencrypt (+https://www.stringencrypt.com)");
    post.addHeader("Content-Type", ContentType.APPLICATION_FORM_URLENCODED.toString());

    try (CloseableHttpClient http = HttpClients.createDefault();
        CloseableHttpResponse response = http.execute(post)) {
      String raw;
      try {
        raw =
            response.getEntity() != null
                ? EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
                : "";
      } catch (ParseException e) {
        return null;
      }
      if (raw == null || raw.isEmpty()) {
        return null;
      }
      JsonNode decoded = JSON.readTree(raw);
      if (decoded == null || !decoded.isObject()) {
        return null;
      }
      return applyDecryptorSourceDecompression(decoded);
    } catch (IOException e) {
      return null;
    }
  }

  private JsonNode applyDecryptorSourceDecompression(JsonNode response) {
    if (command != Command.ENCRYPT || !compression || !decompressEncryptSource) {
      return response;
    }
    if (response.path("error").asInt(-1) != ErrorCode.SUCCESS) {
      return response;
    }
    if (!response.hasNonNull("source") || !response.get("source").isTextual()) {
      return response;
    }
    try {
      byte[] binary = Base64.getDecoder().decode(response.get("source").asText());
      String plain = zlibDecompress(binary);
      if (response instanceof ObjectNode) {
        ((ObjectNode) response).put("source", plain);
      }
    } catch (RuntimeException | IOException ignored) {
      return response;
    }
    return response;
  }

  private Map<String, String> buildInfoParams() {
    Map<String, String> p = new LinkedHashMap<>();
    p.put("command", Command.INFO.getWireValue());
    p.put("code", apiKey);
    return p;
  }

  private Map<String, String> buildIsDemoParams() {
    Map<String, String> p = new LinkedHashMap<>();
    p.put("command", Command.IS_DEMO.getWireValue());
    p.put("code", apiKey);
    return p;
  }

  private Map<String, String> buildEncryptParams() {
    Map<String, String> p = new LinkedHashMap<>();
    p.put("command", Command.ENCRYPT.getWireValue());
    p.put("code", apiKey);
    p.put("label", label);
    p.put("compression", compression ? "1" : "0");
    p.put("lang", language.getWireValue());
    p.put("cmd_min", Integer.toString(cmdMin));
    p.put("cmd_max", Integer.toString(cmdMax));
    p.put("local", local ? "1" : "0");
    p.put("unicode", unicode ? "1" : "0");
    p.put("lang_locale", langLocale);
    p.put("ansi_encoding", ansiEncoding);
    p.put("new_lines", newLines.getWireValue());
    p.put("return_template", returnTemplate ? "1" : "0");
    p.put("include_tags", includeTags ? "1" : "0");
    p.put("include_example", includeExample ? "1" : "0");
    p.put("include_debug_comments", includeDebugComments ? "1" : "0");

    if (inputString != null) {
      p.put("string", inputString);
    } else if (inputBytes != null) {
      p.put("bytes", new String(inputBytes, StandardCharsets.ISO_8859_1));
    }

    if (highlight instanceof String) {
      p.put("highlight", (String) highlight);
    } else if (highlight instanceof Boolean && Boolean.TRUE.equals(highlight)) {
      p.put("highlight", "1");
    }

    if (template != null) {
      p.put("template", template);
    }

    return p;
  }

  static String zlibDecompress(byte[] decoded) throws IOException {
    try (InflaterInputStream iis =
            new InflaterInputStream(new ByteArrayInputStream(decoded), new Inflater(false));
        ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      byte[] buf = new byte[4096];
      int n;
      while ((n = iis.read(buf)) != -1) {
        bos.write(buf, 0, n);
      }
      return new String(bos.toByteArray(), StandardCharsets.UTF_8);
    }
  }
}
