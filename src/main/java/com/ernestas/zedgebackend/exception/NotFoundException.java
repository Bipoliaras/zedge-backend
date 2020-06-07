package com.ernestas.zedgebackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends WebException {

  public NotFoundException(String message) {
    super(message);
    this.code="NotFound";
  }
}
