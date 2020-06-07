package com.ernestas.zedgebackend.api;

import com.ernestas.zedgebackend.itunes.ItunesArtistService;
import com.ernestas.zedgebackend.persistence.album.Album;
import com.ernestas.zedgebackend.persistence.artist.Artist;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/artists")
public class ArtistController {

  private final ItunesArtistService itunesArtistService;

  public ArtistController(
      ItunesArtistService itunesArtistService) {
    this.itunesArtistService = itunesArtistService;
  }

  @GetMapping("/albums")
  public List<Album> getAlbums(@RequestParam("userId") Long userId) {
    return itunesArtistService.getAlbums(userId);
  }

  @PostMapping("/search/{artistName}")
  public List<Artist> searchArtists(@PathVariable("artistName") String artistName) {
    return itunesArtistService.getArtists(artistName);
  }

  @PostMapping
  public Artist saveFavoriteArtist(@RequestParam("userId") Long userId, @Valid @RequestBody Artist artist) {
    return itunesArtistService.saveArtist(userId, artist);
  }


}
