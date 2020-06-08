package com.ernestas.zedgebackend.request;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.ernestas.zedgebackend.ITBase;
import com.ernestas.zedgebackend.exception.BadRequestException;
import com.ernestas.zedgebackend.persistence.request.RequestRepository;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "zedge.backend.maximum.requests:1")
public class RequestServiceTests extends ITBase {

  @Autowired
  private RequestService requestService;

  @MockBean
  private RequestRepository requestRepository;

  @Test
  public void requestsExceededThrowsError() {

    Mockito.when(requestRepository.getRequestCount(Mockito.any(), Mockito.any())).thenReturn(100);

    assertThatThrownBy(() -> requestService.checkIfRequestAmountExceeded()).isInstanceOf(
        BadRequestException.class);
  }






}
