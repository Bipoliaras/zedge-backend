package com.ernestas.zedgebackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends WebException{

  public BadRequestException(String message) {
    super(message);
    this.code="BadRequest";
  }
}
