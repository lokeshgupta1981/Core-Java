package com.howtodoinjava.stackoverflow.cycle.fixed;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

  private final String name;
  private final List<Song> songs = new ArrayList<>();

  public Playlist(String name) {
    this.name = name;
  }

  public void addSong(Song song) {
    songs.add(song);
    song.setPlaylist(this);
  }

  public String getName() {
    return name;
  }

  public List<Song> getSongs() {
    return songs;
  }

  @Override
  public String toString() {
    return "Playlist[name=" + name + ", songs=" + songs + "]";
  }
}
