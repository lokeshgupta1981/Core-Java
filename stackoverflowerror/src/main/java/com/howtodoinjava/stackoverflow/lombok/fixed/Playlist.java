package com.howtodoinjava.stackoverflow.lombok.fixed;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

// @Data generates toString(), equals() and hashCode() over all fields, including songs
@Data
public class Playlist {

  private String name;
  private List<Song> songs = new ArrayList<>();
}
