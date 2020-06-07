package com.ernestas.zedgebackend;


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
public class AlbumUpdater {

  private final AlbumRepository albumRepository;

  private final UserRepository userRepository;

  private final ItunesGateway itunesGateway;

  public AlbumUpdater(AlbumRepository albumRepository,
      UserRepository userRepository, ItunesGateway itunesGateway) {
    this.albumRepository = albumRepository;
    this.userRepository = userRepository;
    this.itunesGateway = itunesGateway;
  }

  @Scheduled(fixedDelay = 10000, initialDelay = 5000)
  @Transactional
  public void updateAlbumsForArtists() {

    userRepository.findAllUniqueArtistIds()
        .stream()
        .filter(Objects::nonNull)
        .forEach(
            artistId ->
            {
              List<Album> albums = albumRepository.findAllByArtistId(artistId);
              albumRepository.deleteAll(albums);
              albumRepository.saveAll(itunesGateway.getAlbums(artistId));
            }
        );
  }
}
