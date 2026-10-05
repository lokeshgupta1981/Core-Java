package com.howtodoinjava.cloning;

import java.util.ArrayList;
import java.util.List;

/**
 * A playlist whose clone() only calls super.clone(), so the copy shares the song list with the original.
 */
public class ShallowPlaylist implements Cloneable {

  private String name;
  private List<Song> songs = new ArrayList<>();

  public ShallowPlaylist(String name) {
    this.name = name;
  }

  @Override
  public ShallowPlaylist clone() {
    try {
      return (ShallowPlaylist) super.clone();   // copies the reference to the same list
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
}
