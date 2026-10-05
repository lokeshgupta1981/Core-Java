package com.howtodoinjava.stackoverflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.howtodoinjava.stackoverflow.setter.Artist;
import org.junit.jupiter.api.Test;

class ArtistTest {

  @Test
  void selfCallingSetterOverflows() {
    Artist artist = new Artist();
    assertThrows(StackOverflowError.class, () -> artist.setName("Lokesh"));
  }

  @Test
  void setterThatAssignsTheFieldWorks() {
    Artist artist = new Artist();
    artist.setNameFixed("Lokesh");
    assertEquals("Lokesh", artist.getName());
  }
}
