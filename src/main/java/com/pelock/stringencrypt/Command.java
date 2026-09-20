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

/** Web API {@code command} parameter. */
public enum Command {
  ENCRYPT("encrypt"),
  IS_DEMO("is_demo"),
  INFO("info");

  private final String wireValue;

  Command(String wireValue) {
    this.wireValue = wireValue;
  }

  public String getWireValue() {
    return wireValue;
  }
}
