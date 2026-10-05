package com.howtodoinjava.stackoverflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class LombokCycleTest {

  @Test
  void dataOnBothSidesOverflows() {
    var playlist = new com.howtodoinjava.stackoverflow.lombok.broken.Playlist();
    playlist.setName("Road Trip");
    var song = new com.howtodoinjava.stackoverflow.lombok.broken.Song();
    song.setTitle("Song A");
    song.setPlaylist(playlist);
    playlist.getSongs().add(song);

    assertThrows(StackOverflowError.class, playlist::toString);
    assertThrows(StackOverflowError.class, playlist::hashCode);
  }

  @Test
  void excludedBackReferenceWorks() {
    var playlist = new com.howtodoinjava.stackoverflow.lombok.fixed.Playlist();
    playlist.setName("Road Trip");
    var song = new com.howtodoinjava.stackoverflow.lombok.fixed.Song();
    song.setTitle("Song A");
    song.setPlaylist(playlist);
    playlist.getSongs().add(song);

    assertEquals("Playlist(name=Road Trip, songs=[Song(title=Song A)])", playlist.toString());
    assertEquals(playlist.hashCode(), playlist.hashCode());
  }
}
