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

/**
 * API error codes returned in encrypt responses ({@code error} field).
 *
 * @see <a href="https://www.stringencrypt.com/api/">StringEncrypt API</a>
 */
public final class ErrorCode {

  public static final int SUCCESS = 0;
  public static final int EMPTY_LABEL = 1;
  public static final int LENGTH_LABEL = 2;
  public static final int EMPTY_STRING = 3;
  public static final int EMPTY_BYTES = 4;
  public static final int EMPTY_INPUT = 5;
  public static final int LENGTH_STRING = 6;
  public static final int INVALID_LANG = 7;
  public static final int INVALID_LOCALE = 8;
  public static final int CMD_MIN = 9;
  public static final int CMD_MAX = 10;
  public static final int LENGTH_BYTES = 11;
  public static final int DEMO = 100;

  private ErrorCode() {}
}
