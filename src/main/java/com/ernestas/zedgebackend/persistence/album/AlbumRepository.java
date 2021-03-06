package com.ernestas.zedgebackend.persistence.album;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
  List<Album> findAllByArtistId(Long artistId);
}
