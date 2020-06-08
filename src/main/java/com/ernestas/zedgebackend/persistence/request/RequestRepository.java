package com.ernestas.zedgebackend.persistence.request;

import java.time.ZonedDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RequestRepository extends JpaRepository<ApiRequest, Long> {

  @Query(value = "SELECT COUNT(*) FROM t_request WHERE request_time <= :currentTime AND request_time >= :startTime", nativeQuery=true)
  Integer getRequestCount(ZonedDateTime currentTime, ZonedDateTime startTime);

}
