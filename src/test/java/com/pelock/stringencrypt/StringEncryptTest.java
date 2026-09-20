package com.pelock.stringencrypt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.Test;

class StringEncryptTest {

  @Test
  void toRequestArrayRequiresCommand() {
    StringEncrypt client = new StringEncrypt("KEY");
    assertThrows(IllegalArgumentException.class, client::toRequestArray);
  }

  @Test
  void infoParams() {
    StringEncrypt client = new StringEncrypt("KEY-1");
    client.setCommand(Command.INFO);
    Map<String, String> p = client.toRequestArray();
    assertEquals("info", p.get("command"));
    assertEquals("KEY-1", p.get("code"));
  }

  @Test
  void isDemoParams() {
    StringEncrypt client = new StringEncrypt("");
    client.setCommand(Command.IS_DEMO);
    Map<String, String> p = client.toRequestArray();
    assertEquals("is_demo", p.get("command"));
    assertEquals("", p.get("code"));
  }

  @Test
  void encryptParamsIncludeSetters() {
    StringEncrypt client = new StringEncrypt("KEY");
    client
        .setCommand(Command.ENCRYPT)
        .setString("Hello!")
        .setLabel("wszLabel")
        .setLanguage(Language.JAVA)
        .setCompression(true)
        .setCmdMin(2)
        .setCmdMax(5)
        .setUnicode(false)
        .setNewLines(NewLine.CRLF)
        .setIncludeTags(true);

    Map<String, String> p = client.toRequestArray();
    assertEquals("encrypt", p.get("command"));
    assertEquals("KEY", p.get("code"));
    assertEquals("Hello!", p.get("string"));
    assertEquals("wszLabel", p.get("label"));
    assertEquals("java", p.get("lang"));
    assertEquals("1", p.get("compression"));
    assertEquals("2", p.get("cmd_min"));
    assertEquals("5", p.get("cmd_max"));
    assertEquals("0", p.get("unicode"));
    assertEquals("crlf", p.get("new_lines"));
    assertEquals("1", p.get("include_tags"));
    assertFalse(p.containsKey("highlight"));
    assertFalse(p.containsKey("bytes"));
  }

  @Test
  void resetRestoresDefaultsIncludingDollarLabel() {
    StringEncrypt client = new StringEncrypt("KEY");
    client.setCommand(Command.ENCRYPT).setLabel("x").setCompression(true);
    client.reset();
    assertEquals("$label", client.getLabel());
    assertFalse(client.getCompression());
    assertEquals(Language.PHP, client.getLanguage());
    assertThrows(IllegalArgumentException.class, client::toRequestArray);
  }

  @Test
  void languageWireValues() {
    assertEquals("js", Language.JAVASCRIPT.getWireValue());
    assertEquals("csharp", Language.CSHARP.getWireValue());
    assertTrue(Language.values().length >= 22);
  }
}
