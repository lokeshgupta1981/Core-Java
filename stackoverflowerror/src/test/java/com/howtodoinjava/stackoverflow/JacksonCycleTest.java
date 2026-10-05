package com.howtodoinjava.stackoverflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import tools.jackson.core.exc.StreamConstraintsException;
import tools.jackson.databind.json.JsonMapper;

class JacksonCycleTest {

  private final JsonMapper mapper = JsonMapper.builder().build();

  @Test
  void bidirectionalRelationFailsAtNestingLimit() {
    var playlist = new com.howtodoinjava.stackoverflow.cycle.broken.Playlist("Road Trip");
    playlist.addSong(new com.howtodoinjava.stackoverflow.cycle.broken.Song("Song A"));

    var e = assertThrows(StreamConstraintsException.class, () -> mapper.writeValueAsString(playlist));
    assertEquals(true, e.getMessage().startsWith("Document nesting depth (501) exceeds the maximum allowed (500"));
  }

  @Test
  void managedAndBackReferenceRoundTrip() {
    var playlist = new com.howtodoinjava.stackoverflow.jackson.fixed.Playlist("Road Trip");
    playlist.addSong(new com.howtodoinjava.stackoverflow.jackson.fixed.Song("Song A"));

    String json = mapper.writeValueAsString(playlist);
    assertEquals("{\"name\":\"Road Trip\",\"songs\":[{\"title\":\"Song A\"}]}", json);

    var read = mapper.readValue(json, com.howtodoinjava.stackoverflow.jackson.fixed.Playlist.class);
    assertSame(read, read.getSongs().getFirst().getPlaylist());
  }

  @Test
  void jsonIgnoreDropsTheBackReference() {
    var playlist = new com.howtodoinjava.stackoverflow.jackson.ignore.Playlist("Road Trip");
    playlist.addSong(new com.howtodoinjava.stackoverflow.jackson.ignore.Song("Song A"));
    assertEquals("{\"name\":\"Road Trip\",\"songs\":[{\"title\":\"Song A\"}]}",
        mapper.writeValueAsString(playlist));
  }
}
