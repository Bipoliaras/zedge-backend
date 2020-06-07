package com.ernestas.zedgebackend.itunes;

import com.ernestas.zedgebackend.persistence.album.Album;
import com.ernestas.zedgebackend.persistence.artist.Artist;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ItunesGateway {

  private final RestTemplate restTemplate = new RestTemplate();

  private final ObjectMapper objectMapper =
      new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  private static final Logger logger = LoggerFactory.getLogger(ItunesGateway.class);

  public List<Album> getAlbums(Long artistId) {
    try {

      String response = restTemplate.getForObject(
          "https://itunes.apple.com/lookup?id=" + artistId + "&entity=album&limit=5",
          String.class);
      return StreamSupport.stream(objectMapper.readTree(response)
          .get("results")
          .spliterator(), false)
          .filter(jsonNode -> jsonNode.get("wrapperType").toString().contains("collection"))
          .map(this::getAlbum)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());

    } catch (Exception ex) {
      logger.error(ex.toString());
    }
    return Collections.emptyList();
  }

  public List<Artist> searchArtists(String artistName) {
    String response = restTemplate
        .getForObject("https://itunes.apple.com/search?entity=allArtist&term=" + artistName,
            String.class);
    try {
      return StreamSupport
          .stream(objectMapper.readTree(response).get("results").spliterator(), false)
          .map(this::getArtist)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());

    } catch (Exception ex) {
      logger.error(ex.toString());
    }

    return Collections.emptyList();
  }

  private Artist getArtist(JsonNode jsonNode) {
    try {
      return objectMapper.readValue(jsonNode.toString(), Artist.class);
    } catch (JsonProcessingException ex) {
      logger.error(ex.toString());
    }
    return null;
  }

  private Album getAlbum(JsonNode jsonNode) {
    try {
      return objectMapper.readValue(jsonNode.toString(), Album.class);
    } catch (JsonProcessingException ex) {
      logger.error(ex.toString());
    }
    return null;
  }

}
