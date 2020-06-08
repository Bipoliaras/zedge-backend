package com.ernestas.zedgebackend.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.ernestas.zedgebackend.ITBase;
import com.ernestas.zedgebackend.persistence.request.ApiRequest;
import com.ernestas.zedgebackend.persistence.request.RequestRepository;
import com.ernestas.zedgebackend.persistence.request.RequestType;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class RequestRepositoryTests extends ITBase {

  @Autowired
  private RequestRepository requestRepository;

  @Test
  public void saveRequestOk() {
    ZonedDateTime time = ZonedDateTime.now();
    ApiRequest apiRequest = requestRepository.saveAndFlush(
        ApiRequest.builder().type(RequestType.ALBUM_SEARCH).requestTime(time).build());
    assertThat(requestRepository.findById(apiRequest.getId()).isPresent()).isEqualTo(true);
  }

  @Test
  public void getRequestCountOk() {
    ZonedDateTime time = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC"));
    requestRepository.saveAndFlush(
        ApiRequest.builder().type(RequestType.ALBUM_SEARCH).requestTime(time.plusMinutes(1)).build());
    requestRepository.saveAndFlush(
        ApiRequest.builder().type(RequestType.ALBUM_SEARCH).requestTime(time.plusMinutes(2)).build());
    requestRepository.saveAndFlush(
        ApiRequest.builder().type(RequestType.ALBUM_SEARCH).requestTime(time.plusMinutes(3)).build());
    requestRepository.saveAndFlush(
        ApiRequest.builder().type(RequestType.ALBUM_SEARCH).requestTime(time.plusMinutes(4)).build());
    requestRepository.saveAndFlush(
        ApiRequest.builder().type(RequestType.ALBUM_SEARCH).requestTime(time.plusMinutes(5)).build());
    requestRepository.saveAndFlush(
        ApiRequest.builder().type(RequestType.ALBUM_SEARCH).requestTime(time.plusMinutes(6)).build());

    assertThat(requestRepository.getRequestCount(time.plusMinutes(30), time)).isEqualTo(6);
  }




}
