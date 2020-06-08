package com.ernestas.zedgebackend.searcher;


import com.ernestas.zedgebackend.itunes.ItunesGateway;
import com.ernestas.zedgebackend.persistence.album.Album;
import com.ernestas.zedgebackend.persistence.album.AlbumRepository;
import com.ernestas.zedgebackend.persistence.user.UserRepository;
import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AlbumSearcher {

  private final AlbumRepository albumRepository;

  private final UserRepository userRepository;

  private final ItunesGateway itunesGateway;

  public AlbumSearcher(AlbumRepository albumRepository,
      UserRepository userRepository, ItunesGateway itunesGateway) {
    this.albumRepository = albumRepository;
    this.userRepository = userRepository;
    this.itunesGateway = itunesGateway;
  }

  @Scheduled(initialDelayString = "${zedge.backend.album.updater.initial.delay:60000}",
      fixedDelayString = "${zedge.backend.album.updater.fixed.delay:900000}")
  @Transactional
  public void searchAlbums() {
    userRepository.findAllUniqueArtistIds()
        .stream()
        .filter(Objects::nonNull)
        .forEach(this::saveAlbumsForArtistId);
  }

  public void saveAlbumsForArtistId(Long artistId) {
    List<Album> albums = albumRepository.findAllByArtistId(artistId);
    //only save top 5 albums if none exist
    if (albums == null || albums.isEmpty()) {
      albumRepository.saveAll(itunesGateway.getAlbumsByArtistId(artistId));
    }
  }
}
