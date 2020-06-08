package com.ernestas.zedgebackend.request;

import com.ernestas.zedgebackend.exception.BadRequestException;
import com.ernestas.zedgebackend.persistence.request.ApiRequest;
import com.ernestas.zedgebackend.persistence.request.RequestRepository;
import com.ernestas.zedgebackend.persistence.request.RequestType;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RequestService {


  private final RequestRepository requestRepository;

  @Value("${zedge.backend.maximum.requests:100}")
  private Integer maxRequests;

  public RequestService(
      RequestRepository requestRepository) {
    this.requestRepository = requestRepository;
  }

  public void checkIfRequestAmountExceeded() {
    if(requestRepository.getRequestCount(ZonedDateTime.now(ZoneId.of("UTC")),
        ZonedDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.HOURS)) >= maxRequests) {
      throw new BadRequestException("Request amount exceeded");
    }
  }

  public void saveRequest(RequestType requestType) {
    requestRepository.saveAndFlush(
        ApiRequest.builder()
            .requestTime(ZonedDateTime.now(ZoneId.of("UTC")))
            .type(requestType)
            .build());
  }


}
