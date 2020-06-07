package com.ernestas.zedgebackend.persistence.user;

import com.ernestas.zedgebackend.persistence.artist.Artist;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "t_user")
public class User {
  @Id
  @GeneratedValue
  private Long id;

  @OneToOne
  private Artist artist;
}
