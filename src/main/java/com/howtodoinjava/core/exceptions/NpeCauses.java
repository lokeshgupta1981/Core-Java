package com.howtodoinjava.core.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common causes of NullPointerException and the helpful message (JEP 358) the JVM prints for each.
 * Compile with debug info (Maven does this by default, or javac -g) to see local variable names in
 * the messages. Run: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.core.exceptions.NpeCauses
 */
public class NpeCauses {

  record Artist(String name) {}

  record Song(String title, Artist artist) {}

  static class Playlist {
    String owner;           // never initialized, so null
    List<Song> songs;       // never initialized, so null
  }

  static String findGenre(String title) {
    return title.startsWith("Hey") ? "pop" : null;   // returns null for unknown songs
  }

  public static void main(String[] args) {
    // 0. The classic example
    show("0. Local variable", () -> {
      String name = null;
      System.out.println(name.length());
    });

    // 1. Uninitialized field
    show("1. Uninitialized field", () -> {
      Playlist playlist = new Playlist();
      System.out.println(playlist.owner.toUpperCase());
    });
    show("1b. Uninitialized collection field", () -> {
      Playlist playlist = new Playlist();
      playlist.songs.add(new Song("Hey Jude", new Artist("The Beatles")));
    });

    // 2. Map.get() miss and unboxing a null Integer
    Map<String, Integer> ages = new HashMap<>();
    ages.put("Lokesh", 37);
    show("2. Map.get() miss + unboxing", () -> {
      int age = ages.get("Alex");
      System.out.println(age);
    });

    // 3. Method returning null
    show("3. Method returning null", () -> {
      String genre = findGenre("Yesterday");
      System.out.println(genre.toUpperCase());
    });

    // 4. Array element null and array itself null
    show("4. Array element null", () -> {
      String[] names = new String[3];
      System.out.println(names[1].length());
    });
    show("4b. Array null", () -> {
      int[] scores = null;
      System.out.println(scores.length);
    });

    // 5. Chained calls
    show("5. Chained calls", () -> {
      Song song = new Song("Imagine", null);
      System.out.println(song.artist().name().length());
    });

    // 6. synchronized on null
    show("6. synchronized on null", () -> {
      Object lock = null;
      synchronized (lock) {
        System.out.println("locked");
      }
    });

    // 7. throw null
    show("7. throw null", () -> {
      RuntimeException error = null;
      throw error;
    });

    // 8. switch on null before case null
    show("8. switch on null", () -> {
      String day = null;
      switch (day) {
        case "SAT", "SUN" -> System.out.println("weekend");
        default -> System.out.println("weekday");
      }
    });

    // 9. String.valueOf(null) picks valueOf(char[])
    show("9. String.valueOf(null)", () -> {
      System.out.println(String.valueOf(null));
    });

    // 10. List.of() and Map.of() reject null
    show("10. List.of(null)", () -> List.of("Lokesh", null));
  }

  static void show(String label, Runnable code) {
    try {
      code.run();
      System.out.println(label + " -> no exception");
    } catch (NullPointerException e) {
      System.out.println(label + " -> " + e);
    }
  }
}
