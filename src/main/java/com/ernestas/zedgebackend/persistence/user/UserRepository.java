package com.ernestas.zedgebackend.persistence.user;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {


  @Query(value = "SELECT DISTINCT artist_artist_id FROM t_user", nativeQuery=true)
  List<Long> findAllUniqueArtistIds();

}
