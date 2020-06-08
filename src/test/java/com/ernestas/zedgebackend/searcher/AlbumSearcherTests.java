package com.ernestas.zedgebackend.searcher;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.ernestas.zedgebackend.ITBase;
import com.ernestas.zedgebackend.itunes.ItunesGateway;
import com.ernestas.zedgebackend.persistence.album.Album;
import com.ernestas.zedgebackend.persistence.album.AlbumRepository;
import com.ernestas.zedgebackend.persistence.artist.Artist;
import com.ernestas.zedgebackend.persistence.artist.ArtistRepository;
import com.ernestas.zedgebackend.persistence.user.User;
import com.ernestas.zedgebackend.persistence.user.UserRepository;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"zedge.backend.maximum.requests=5"})
public class AlbumSearcherTests extends ITBase {


  @Autowired
  private AlbumSearcher albumSearcher;

  @MockBean
  private ItunesGateway itunesGateway;

  @Autowired
  private AlbumRepository albumRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ArtistRepository artistRepository;

  @Test
  public void givenAlbumsExistInDb_whenSearchAlbums_ok() {

    Artist artist = artistRepository.saveAndFlush(
        Artist.builder().artistId(1L).build());

    userRepository
        .saveAndFlush(User.builder().artist(artist).build());

    albumRepository.saveAll( List.of(
        Album.builder().collectionId(1L).artistId(1L).collectionName("GOOD KIID MAAD CITY").build(),
        Album.builder().collectionId(2L).artistId(1L).collectionName("TO PIMP A BUTTERFLY").build(),
        Album.builder().collectionId(3L).artistId(1L).collectionName("SEASONS PART 2").build(),
        Album.builder().collectionId(4L).artistId(1L).collectionName("BIG DINGUS ADVENTURE")
            .build(),
        Album.builder().collectionId(5L).artistId(1L).collectionName("THREE BEANS PLEASE").build()
    ));

    albumSearcher.searchAlbums();

  }

  @Test
  public void givenNoAlbumsExistInDb_whenSearchAlbums_ok() {
    Artist artist = artistRepository.saveAndFlush(
        Artist.builder().artistId(1L).build());

    userRepository
        .saveAndFlush(User.builder().artist(artist).build());

    when(itunesGateway.getAlbumsByArtistId(Mockito.any())).thenReturn(Collections.emptyList());

    albumSearcher.searchAlbums();

    Mockito.verify(itunesGateway,Mockito.times(1)).getAlbumsByArtistId(artist.getArtistId());

    assertThat(albumRepository.findAll()).isEqualTo(Collections.emptyList());
  }




}
