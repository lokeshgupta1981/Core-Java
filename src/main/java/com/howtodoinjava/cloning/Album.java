package com.howtodoinjava.cloning;

import java.util.ArrayList;
import java.util.List;

/**
 * An album with a final mutable field. clone() cannot give the copy a new list, so the class uses a copy
 * constructor instead.
 */
public class Album {

  private final String title;
  private final List<Song> tracks;

  public Album(String title, List<Song> tracks) {
    this.title = title;
    this.tracks = new ArrayList<>(tracks);
  }

  // Deep copy constructor: assigns the final field once, like any constructor
  public Album(Album other) {
    this.title = other.title;
    this.tracks = new ArrayList<>();
    for (Song song : other.tracks) {
      this.tracks.add(new Song(song));
    }
  }

  public String getTitle() {
    return title;
  }

  public List<Song> getTracks() {
    return tracks;
  }
}
