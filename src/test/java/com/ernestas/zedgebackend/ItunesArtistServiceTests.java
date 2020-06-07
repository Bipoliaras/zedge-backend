package com.ernestas.zedgebackend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ernestas.zedgebackend.exception.NotFoundException;
import com.ernestas.zedgebackend.itunes.ItunesArtistService;
import com.ernestas.zedgebackend.itunes.ItunesGateway;
import com.ernestas.zedgebackend.persistence.album.Album;
import com.ernestas.zedgebackend.persistence.album.AlbumRepository;
import com.ernestas.zedgebackend.persistence.artist.Artist;
import com.ernestas.zedgebackend.persistence.artist.ArtistRepository;
import com.ernestas.zedgebackend.persistence.user.User;
import com.ernestas.zedgebackend.persistence.user.UserRepository;
import java.util.List;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ItunesArtistServiceTests extends ITBase {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ArtistRepository artistRepository;

  @MockBean
  private ItunesGateway itunesGateway;

  @Autowired
  private ItunesArtistService itunesArtistService;

  @Autowired
  private AlbumRepository albumRepository;

  @Test
  public void getAlbumsOk() {

    List<Album> expectedAlbums = List.of(
        Album.builder().collectionId(1L).build(),
        Album.builder().collectionId(2L).build());

    Mockito.when(itunesGateway.getAlbums(1L))
        .thenReturn(List.of(
            Album.builder().collectionId(1L).build(),
            Album.builder().collectionId(2L).build()
        ));

    Artist artist = artistRepository.saveAndFlush(
        Artist.builder().artistId(1L).build());

    User user = userRepository
        .saveAndFlush(User.builder().artist(artist).build());

    List<Album> albums = itunesArtistService.getAlbums(user.getId());

    assertThat(albums).isEqualTo(expectedAlbums);

    assertThat(albumRepository.findAll().size()).isEqualTo(2);
  }

  @Test
  public void whenGetAlbumsWithNonExistingUser_throwsNotFoundException() {
    assertThatThrownBy(() -> itunesArtistService.getAlbums(30L))
        .isInstanceOf(NotFoundException.class)
        .hasMessageContaining("User does not exist");
  }

  @Test
  public void whenGetAlbumsWithUserWithNoFavoriteAlbum_throwsNotFoundException() {
    User user = userRepository
        .saveAndFlush(User.builder().build());

    assertThatThrownBy(() -> itunesArtistService.getAlbums(user.getId()))
        .isInstanceOf(NotFoundException.class)
        .hasMessageContaining("User has no favorite artist");
  }

  @Test
  public void whenGetAlbumsAndAlbumsExistInRepositoryOk() {

    Artist artist = artistRepository.saveAndFlush(
        Artist.builder().artistId(1L).build());

    User user = userRepository
        .saveAndFlush(User.builder().artist(artist).build());

    List<Album> expectedAlbums = List.of(
        Album.builder().collectionId(1L).artistId(1L).collectionName("GOOD KIID MAAD CITY").build(),
        Album.builder().collectionId(2L).artistId(1L).collectionName("TO PIMP A BUTTERFLY").build(),
        Album.builder().collectionId(3L).artistId(1L).collectionName("SEASONS PART 2").build(),
        Album.builder().collectionId(4L).artistId(1L).collectionName("BIG DINGUS ADVENTURE")
            .build(),
        Album.builder().collectionId(5L).artistId(1L).collectionName("THREE BEANS PLEASE").build()
    );

    albumRepository.saveAll(expectedAlbums);

    assertThat(itunesArtistService.getAlbums(user.getId())).isEqualTo(expectedAlbums);
  }

  @Test
  public void whenSaveArtistAndUserDoesNotExist_throwsError() {

    assertThatThrownBy(() -> itunesArtistService.getAlbums(30L)).isInstanceOf(
        NotFoundException.class
    ).hasMessageContaining("User does not exist");
  }

  @Test
  public void saveArtistOk() {
    User user = userRepository
        .saveAndFlush(User.builder().build());

    Artist expected = Artist.builder().artistId(50L).build();

    Artist actual = itunesArtistService
        .saveArtist(user.getId(), Artist.builder().artistId(50L).build());

    assertThat(actual).isEqualTo(expected);
    assertThat(artistRepository.findAll().size()).isEqualTo(1);

  }

  @Test
  public void getArtistsOk() {
    List<Artist> expectedList = List.of(
        Artist.builder().artistName("BEAVER").artistId(1L).build(),
        Artist.builder().artistName("DOG").artistId(2L).build(),
        Artist.builder().artistName("RAT").artistId(3L).build(),
        Artist.builder().artistName("CAT").artistId(4L).build(),
        Artist.builder().artistName("BAT").artistId(5L).build()
    );

    Mockito.when(itunesGateway.searchArtists(Mockito.any())).thenReturn(
        expectedList
    );

    assertThat(itunesArtistService.getArtists("Animal"))
        .isEqualTo(expectedList);
  }

  @Test
  public void saveArtistWhenArtistAlreadyExistsOk() {

    User user = userRepository
        .saveAndFlush(User.builder().build());

    Artist savedArtist = artistRepository.saveAndFlush(Artist.builder().artistId(50L).build());

    Artist actual = itunesArtistService
        .saveArtist(user.getId(), Artist.builder().artistId(50L).build());

    assertThat(actual).isEqualTo(savedArtist);

    assertThat(artistRepository.findAll().size()).isEqualTo(1);
  }


}
