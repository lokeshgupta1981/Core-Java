package com.howtodoinjava.cloning;

import java.util.List;

/**
 * An immutable record. Instances can be shared, so a record never needs clone(); a "with" method creates a changed copy.
 */
public record Track(String title, List<String> artists) {

  public Track {
    artists = List.copyOf(artists);   // unmodifiable copy of the caller's list
  }

  public Track withTitle(String newTitle) {
    return new Track(newTitle, artists);
  }
}
