package com.ernestas.zedgebackend.persistence.album;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "t_album")
public class Album {

  @Id
  private Long collectionId;
  private Long amgArtistId;
  private String collectionName;
  private String country;
  private String currency;
  private Date releaseDate;
  private String copyright;
  private Long trackCount;
  private Long artistId;

}
