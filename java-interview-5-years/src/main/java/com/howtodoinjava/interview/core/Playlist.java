package com.howtodoinjava.interview.core;

import java.util.List;
import java.util.Objects;

/** An immutable class: final, private final fields, defensive copy, no setters. */
public final class Playlist {

  private final String name;
  private final List<String> songs;

  public Playlist(String name, List<String> songs) {
    this.name = Objects.requireNonNull(name, "name");
    this.songs = List.copyOf(songs);   // defensive, unmodifiable copy
  }

  public String name() {
    return name;
  }

  public List<String> songs() {
    return songs;                      // safe to return, the list is unmodifiable
  }

  public Playlist withSong(String song) {
    List<String> copy = new java.util.ArrayList<>(songs);
    copy.add(song);
    return new Playlist(name, copy);
  }
}
