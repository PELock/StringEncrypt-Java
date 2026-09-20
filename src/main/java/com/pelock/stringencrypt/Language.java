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

/** Output language for generated decryptor code ({@code lang} request field). */
public enum Language {
  CPP("cpp"),
  CSHARP("csharp"),
  VBNET("vbnet"),
  DELPHI("delphi"),
  JAVA("java"),
  JAVASCRIPT("js"),
  PYTHON("python"),
  RUBY("ruby"),
  AUTOIT("autoit"),
  POWERSHELL("powershell"),
  HASKELL("haskell"),
  MASM("masm"),
  FASM("fasm"),
  GO("go"),
  RUST("rust"),
  SWIFT("swift"),
  KOTLIN("kotlin"),
  LUA("lua"),
  DART("dart"),
  PHP("php"),
  OBJC("objc"),
  NASM("nasm");

  private final String wireValue;

  Language(String wireValue) {
    this.wireValue = wireValue;
  }

  public String getWireValue() {
    return wireValue;
  }
}
