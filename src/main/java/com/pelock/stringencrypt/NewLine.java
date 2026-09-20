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

/** Line ending style for generated source ({@code new_lines} request field). */
public enum NewLine {
  LF("lf"),
  CRLF("crlf"),
  CR("cr");

  private final String wireValue;

  NewLine(String wireValue) {
    this.wireValue = wireValue;
  }

  public String getWireValue() {
    return wireValue;
  }
}
