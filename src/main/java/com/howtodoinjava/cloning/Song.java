package com.howtodoinjava.cloning;

import java.io.Serial;
import java.io.Serializable;

/**
 * A mutable song that can be copied in three ways: clone(), a copy constructor and a static factory method.
 */
public class Song implements Cloneable, Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String title;
  private int plays;

  Song() {
    // used by Jackson
  }

  public Song(String title, int plays) {
    this.title = title;
    this.plays = plays;
  }

  // Copy constructor
  public Song(Song other) {
    this(other.title, other.plays);
  }

  // Static factory method
  public static Song copyOf(Song other) {
    return new Song(other.title, other.plays);
  }

  // Public clone() with a covariant return type and no checked exception
  @Override
  public Song clone() {
    try {
      return (Song) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError("Song implements Cloneable", e);
    }
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getPlays() {
    return plays;
  }

  public void setPlays(int plays) {
    this.plays = plays;
  }

  @Override
  public String toString() {
    return "Song[title=" + title + ", plays=" + plays + "]";
  }
}
