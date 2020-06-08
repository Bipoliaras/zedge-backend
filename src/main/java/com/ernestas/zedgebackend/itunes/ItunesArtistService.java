package com.ernestas.zedgebackend.itunes;

import com.ernestas.zedgebackend.exception.NotFoundException;
import com.ernestas.zedgebackend.persistence.album.Album;
import com.ernestas.zedgebackend.persistence.album.AlbumRepository;
import com.ernestas.zedgebackend.persistence.artist.Artist;
import com.ernestas.zedgebackend.persistence.artist.ArtistCreate;
import com.ernestas.zedgebackend.persistence.artist.ArtistRepository;
import com.ernestas.zedgebackend.persistence.user.User;
import com.ernestas.zedgebackend.persistence.user.UserRepository;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ItunesArtistService {

  private final AlbumRepository albumRepository;

  private final ArtistRepository artistRepository;

  private final UserRepository userRepository;

  private final ItunesGateway itunesGateway;

  public ItunesArtistService(ItunesGateway itunesGateway,
      AlbumRepository albumRepository,
      UserRepository userRepository,
      ArtistRepository artistRepository) {
    this.itunesGateway = itunesGateway;
    this.albumRepository = albumRepository;
    this.artistRepository = artistRepository;
    this.userRepository = userRepository;
  }

  public List<Album> getAlbums(Long userId) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("User does not exist"));

    if (user.getArtist() == null) {
      throw new NotFoundException("User has no favorite artist");
    }

    List<Album> albums = albumRepository.findAllByArtistId(user.getArtist().getArtistId());

    if (albums != null && !albums.isEmpty()) {
      return albums;
    } else {
      return albumRepository.saveAll(itunesGateway.getAlbumsByArtistId(user.getArtist().getArtistId()));
    }
  }

  @Cacheable(value = "artistSearchCache")
  public List<Artist> getArtists(String artistName) {
    return itunesGateway.searchArtistsByArtistName(artistName);
  }

  public Artist saveArtist(Long userId, ArtistCreate artistCreate) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("User does not exist"));

    Artist artist = Artist.builder()
        .artistId(artistCreate.getArtistId())
        .artistName(artistCreate.getArtistName())
        .amgArtistId(artistCreate.getAmgArtistId())
        .artistLinkUrl(artistCreate.getArtistLinkUrl())
        .primaryGenreId(artistCreate.getPrimaryGenreId())
        .primaryGenreName(artistCreate.getPrimaryGenreName())
        .build();

    if (artistRepository.findById(artist.getArtistId()).isPresent()) {
      user.setArtist(artist);
    } else {
      user.setArtist(artistRepository.saveAndFlush(artist));
    }
    userRepository.saveAndFlush(user);
    return artist;
  }
}
