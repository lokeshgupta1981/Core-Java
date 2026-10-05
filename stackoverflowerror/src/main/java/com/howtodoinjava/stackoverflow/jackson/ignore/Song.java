package com.howtodoinjava.stackoverflow.jackson.ignore;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Song {

  private String title;

  @JsonIgnore
  private Playlist playlist;

  public Song(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public Playlist getPlaylist() {
    return playlist;
  }

  void setPlaylist(Playlist playlist) {
    this.playlist = playlist;
  }
}
