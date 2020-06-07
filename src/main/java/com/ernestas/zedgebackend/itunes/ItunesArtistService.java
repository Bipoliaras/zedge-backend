package com.ernestas.zedgebackend.itunes;

import com.ernestas.zedgebackend.exception.NotFoundException;
import com.ernestas.zedgebackend.persistence.album.Album;
import com.ernestas.zedgebackend.persistence.album.AlbumRepository;
import com.ernestas.zedgebackend.persistence.artist.Artist;
import com.ernestas.zedgebackend.persistence.artist.ArtistRepository;
import com.ernestas.zedgebackend.persistence.user.User;
import com.ernestas.zedgebackend.persistence.user.UserRepository;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class ItunesArtistService {

  private final AlbumRepository albumRepository;

  private final ArtistRepository artistRepository;

  private final UserRepository userRepository;

  private final ItunesGateway itunesGateway;

  public ItunesArtistService(ItunesGateway itunesGateway, AlbumRepository albumRepository,
      UserRepository userRepository, ArtistRepository artistRepository) {
    this.itunesGateway = itunesGateway;
    this.albumRepository = albumRepository;
    this.artistRepository = artistRepository;
    this.userRepository = userRepository;
  }

  @PostConstruct
  public void setupData() {
    Artist artist = artistRepository.saveAndFlush(Artist.builder().artistId(909253L).build());
    userRepository.saveAndFlush(User.builder().artist(artist).build());
    userRepository.saveAndFlush(User.builder().build());
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
      return albumRepository.saveAll(itunesGateway.getAlbums(user.getArtist().getArtistId()));
    }
  }

  public List<Artist> getArtists(String artistName) {
    return itunesGateway.searchArtists(artistName);
  }

  public Artist saveArtist(Long userId, Artist artist) {

    User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exist"));

    if(artistRepository.findById(artist.getArtistId()).isPresent()) {
      user.setArtist(artist);
    } else {
      user.setArtist(artistRepository.saveAndFlush(artist));
    }
    userRepository.saveAndFlush(user);
    return artist;
  }
}
