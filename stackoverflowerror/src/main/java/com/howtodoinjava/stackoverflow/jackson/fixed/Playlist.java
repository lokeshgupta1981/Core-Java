package com.howtodoinjava.stackoverflow.jackson.fixed;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;

public class Playlist {

  private String name;

  @JsonManagedReference
  private List<Song> songs = new ArrayList<>();

  public Playlist() {
  }

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

  public void setName(String name) {
    this.name = name;
  }

  public List<Song> getSongs() {
    return songs;
  }

  public void setSongs(List<Song> songs) {
    this.songs = songs;
  }
}
