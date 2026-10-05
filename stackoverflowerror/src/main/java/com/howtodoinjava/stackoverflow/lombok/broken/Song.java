package com.howtodoinjava.stackoverflow.lombok.broken;

import lombok.Data;

// The back-reference to playlist is part of toString(), equals() and hashCode()
@Data
public class Song {

  private String title;
  private Playlist playlist;
}
