package com.ernestas.zedgebackend.api;

import static com.ernestas.zedgebackend.json.ResponseExamples.albumsJsonExample;
import static com.ernestas.zedgebackend.json.ResponseExamples.artistsJsonExample;

import com.ernestas.zedgebackend.ITBase;
import com.ernestas.zedgebackend.persistence.artist.Artist;
import com.ernestas.zedgebackend.persistence.artist.ArtistCreate;
import com.ernestas.zedgebackend.persistence.artist.ArtistRepository;
import com.ernestas.zedgebackend.persistence.user.User;
import com.ernestas.zedgebackend.persistence.user.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

public class ArtistControllerTests extends ITBase {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ArtistRepository artistRepository;

  @MockBean
  private RestTemplate restTemplate;

  @Test
  public void getAlbumsOk() {

    Artist artist = artistRepository.saveAndFlush(Artist.builder().artistId(368183298L).build());

    Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any()))
        .thenReturn(albumsJsonExample);

    User user = userRepository
        .saveAndFlush(User.builder().artist(artist).build());

    RestAssured.given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .queryParam("userId", user.getId())
        .get("/artists/albums")
        .then()
        .log().all()
        .statusCode(200);
  }

  @Test
  public void getArtistsOk() {

    Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any()))
        .thenReturn(artistsJsonExample);

    RestAssured.given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .post("/artists/search/abba")
        .then()
        .log().all()
        .statusCode(200);

  }

  @Test
  public void saveFavoriteArtistOk() {

    User user = userRepository
        .saveAndFlush(User.builder().build());

    RestAssured.given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .queryParam("userId", user.getId())
        .body(
            ArtistCreate.builder().artistId(10L).artistName("Kendrick").build()
        )
        .post("/artists")
        .then()
        .statusCode(200);
  }

}
