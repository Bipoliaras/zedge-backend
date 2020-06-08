package com.ernestas.zedgebackend.helper;

import com.ernestas.zedgebackend.persistence.artist.Artist;
import com.ernestas.zedgebackend.persistence.artist.ArtistRepository;
import com.ernestas.zedgebackend.persistence.user.User;
import com.ernestas.zedgebackend.persistence.user.UserRepository;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitialDataCreator {

  @Autowired
  private ArtistRepository artistRepository;

  @Autowired
  private UserRepository userRepository;

  @PostConstruct
  public void setupData() {
    Artist artist = artistRepository.saveAndFlush(Artist.builder().artistId(909253L).build());
    userRepository.saveAndFlush(User.builder().artist(artist).build());
    userRepository.saveAndFlush(User.builder().build());
  }
}
