package com.howtodoinjava.stackoverflow.jackson.fixed;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class Song {

  private String title;

  @JsonBackReference
  private Playlist playlist;

  public Song() {
  }

  public Song(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Playlist getPlaylist() {
    return playlist;
  }

  public void setPlaylist(Playlist playlist) {
    this.playlist = playlist;
  }
}
