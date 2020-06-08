package com.ernestas.zedgebackend.persistence.artist;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistCreate {

  @NotNull
  private Long artistId;
  @NotNull
  private String artistName;
  private String artistLinkUrl;
  private String amgArtistId;
  private String primaryGenreName;
  private String primaryGenreId;
}
