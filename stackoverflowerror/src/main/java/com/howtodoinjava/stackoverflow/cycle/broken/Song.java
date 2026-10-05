package com.howtodoinjava.stackoverflow.cycle.broken;

public class Song {

  private final String title;
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

  @Override
  public String toString() {
    return "Song[title=" + title + ", playlist=" + playlist + "]";
  }
}
