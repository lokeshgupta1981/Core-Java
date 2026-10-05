package com.howtodoinjava.stackoverflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.howtodoinjava.stackoverflow.cycle.broken.Playlist;
import com.howtodoinjava.stackoverflow.cycle.broken.Song;
import org.junit.jupiter.api.Test;

class CycleTest {

  @Test
  void toStringCycleOverflows() {
    Playlist playlist = new Playlist("Road Trip");
    playlist.addSong(new Song("Song A"));
    assertThrows(StackOverflowError.class, playlist::toString);
  }

  @Test
  void toStringPrintsOnlyTheParentName() {
    var playlist = new com.howtodoinjava.stackoverflow.cycle.fixed.Playlist("Road Trip");
    playlist.addSong(new com.howtodoinjava.stackoverflow.cycle.fixed.Song("Song A"));
    assertEquals("Playlist[name=Road Trip, songs=[Song[title=Song A, playlist=Road Trip]]]",
        playlist.toString());
  }
}
