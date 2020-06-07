package com.ernestas.zedgebackend.persistence.artist;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "t_artist")
public class Artist {

  @Id
  @NotNull
  private Long artistId;
  private String artistName;
  private String artistLinkUrl;
  private String amgArtistId;
  private String primaryGenreName;
  private String primaryGenreId;
}
