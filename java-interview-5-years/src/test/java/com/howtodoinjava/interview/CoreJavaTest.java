package com.howtodoinjava.interview;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.howtodoinjava.interview.core.Playlist;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CoreJavaTest {

  @Test
  void immutableClass() {
    List<String> songs = new ArrayList<>(List.of("one", "two"));
    Playlist playlist = new Playlist("road trip", songs);

    songs.add("three");                                  // the caller changes its own list
    int size = playlist.songs().size();                  // 2, the playlist kept its copy
    Playlist longer = playlist.withSong("three");        // new object with 3 songs

    assertThat(size).isEqualTo(2);
    assertThat(longer.songs()).hasSize(3);
    assertThat(playlist.songs()).hasSize(2);
    assertThatThrownBy(() -> {
      boolean added = playlist.songs().add("four");      // UnsupportedOperationException
    })
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  @SuppressWarnings({"StringEquality", "removal", "NumberEquality"})
  void integerCacheAndStringPool() {
    Integer a = 127, b = 127;
    boolean small = a == b;                // true, both come from the Integer cache
    Integer c = 128, d = 128;
    boolean big = c == d;                  // false, two different objects
    boolean equal = c.equals(d);           // true

    String s1 = "java";
    String s2 = "java";
    String s3 = new String("java");
    boolean pooled = s1 == s2;             // true, same literal from the pool
    boolean copy = s1 == s3;               // false, new String() creates a heap object
    boolean interned = s1 == s3.intern();  // true

    assertThat(small).isTrue();
    assertThat(big).isFalse();
    assertThat(equal).isTrue();
    assertThat(pooled).isTrue();
    assertThat(copy).isFalse();
    assertThat(interned).isTrue();
  }

  @Test
  void tryWithResourcesAndSuppressed() {
    String result;
    try (BufferedReader reader = Files.newBufferedReader(Path.of("ages.txt"))) {
      result = reader.readLine();
    } catch (NoSuchFileException e) {
      result = "missing file " + e.getMessage();       // missing file ages.txt
    } catch (IOException e) {
      result = "read failed";
    }
    assertThat(result).isEqualTo("missing file ages.txt");

    String main;
    String suppressed;
    AutoCloseable resource = () -> {
      throw new IllegalStateException("close failed");
    };
    try (resource) {
      throw new RuntimeException("body failed");
    } catch (Exception e) {
      main = e.getMessage();                                // body failed
      suppressed = e.getSuppressed()[0].getMessage();       // close failed
    }
    assertThat(main).isEqualTo("body failed");
    assertThat(suppressed).isEqualTo("close failed");
  }
}
