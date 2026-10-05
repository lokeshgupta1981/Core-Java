package com.howtodoinjava.stackoverflow.cycle.fixed;

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

  // Prints only the playlist name, so the cycle stops here
  @Override
  public String toString() {
    return "Song[title=" + title + ", playlist=" + (playlist == null ? null : playlist.getName()) + "]";
  }
}
