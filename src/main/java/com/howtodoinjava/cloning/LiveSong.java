package com.howtodoinjava.cloning;

import java.io.Serial;

/**
 * A subclass of Song, used to show that a copy constructor of the parent loses the subclass type.
 */
public class LiveSong extends Song {

  @Serial
  private static final long serialVersionUID = 1L;

  private String venue;

  public LiveSong(String title, int plays, String venue) {
    super(title, plays);
    this.venue = venue;
  }

  // Copy constructor of the subclass calls the copy constructor of the parent
  public LiveSong(LiveSong other) {
    super(other);
    this.venue = other.venue;
  }

  @Override
  public LiveSong clone() {
    return (LiveSong) super.clone();   // Song.clone() calls Object.clone(), which keeps the runtime class
  }

  public String getVenue() {
    return venue;
  }

  @Override
  public String toString() {
    return "LiveSong[title=" + getTitle() + ", plays=" + getPlays() + ", venue=" + venue + "]";
  }
}
