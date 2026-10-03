package com.howtodoinjava.cloning;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A playlist with a deep clone(): the copy gets a new list with a clone of every song.
 */
public class Playlist implements Cloneable, Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String name;
  private List<Song> songs = new ArrayList<>();

  Playlist() {
    // used by Jackson
  }

  public Playlist(String name) {
    this.name = name;
  }

  // Deep copy constructor
  public Playlist(Playlist other) {
    this.name = other.name;
    this.songs = new ArrayList<>();
    for (Song song : other.songs) {
      this.songs.add(new Song(song));
    }
  }

  @Override
  public Playlist clone() {
    try {
      Playlist copy = (Playlist) super.clone();   // 1. field-by-field copy
      copy.songs = new ArrayList<>();             // 2. a new list
      for (Song song : songs) {
        copy.songs.add(song.clone());             // 3. a clone of every song
      }
      return copy;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError(e);
    }
  }

  public void add(Song song) {
    songs.add(song);
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
