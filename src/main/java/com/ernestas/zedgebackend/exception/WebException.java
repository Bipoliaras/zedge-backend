package com.ernestas.zedgebackend.exception;

public class WebException extends RuntimeException {

  protected String code;

  public String getCode() {
    return this.code;
  }

  public WebException(String message) {
    super(message);
  }

}
