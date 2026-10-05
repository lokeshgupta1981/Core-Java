package com.howtodoinjava.stackoverflow.lombok.fixed;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class Song {

  private String title;

  // The back-reference is left out of the generated methods
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Playlist playlist;
}
