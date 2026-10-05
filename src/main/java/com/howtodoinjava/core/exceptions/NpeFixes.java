package com.howtodoinjava.core.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

/**
 * Ways to avoid a NullPointerException: null checks, Objects.requireNonNull (fail fast), Optional,
 * "literal".equals(var), default values, empty collections, null-safe operators and switch case null.
 * Run: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.core.exceptions.NpeFixes
 */
public class NpeFixes {

  static int maxSongs = 100;

  record Artist(String name) {}

  record Song(String title, Artist artist) {

    Song {
      Objects.requireNonNull(title, "title must not be null");
    }

    Optional<Artist> findArtist() {
      return Optional.ofNullable(artist);
    }
  }

  static List<Song> songsBy(String artistName, List<Song> library) {
    if (artistName == null) {
      return List.of();   // empty list instead of null
    }
    return library.stream()
        .filter(s -> s.artist() != null && artistName.equals(s.artist().name()))
        .toList();
  }

  static Optional<String> findGenre(String title) {
    return "Hey Jude".equals(title) ? Optional.of("pop") : Optional.empty();
  }

  static String describe(Object value) {
    return switch (value) {
      case null -> "no value";
      case String s -> "text of length " + s.length();
      case Integer i -> "number " + i;
      default -> "something else";
    };
  }

  static String dayType(String day) {
    return switch (day) {
      case null -> "unknown";
      case "SAT", "SUN" -> "weekend";
      default -> "weekday";
    };
  }

  static String greet(String name) {
    Objects.requireNonNull(name, "name must not be null");
    return "Hello " + name;
  }

  static int lengthOf(String name) {
    return name.length();
  }

  public static void main(String[] args) {
    String name = null;
    Map<String, Integer> ages = new HashMap<>();
    ages.put("Lokesh", 37);

    // 1. Quick fixes
    int len = 0;
    if (name != null) {
      len = name.length();
    }
    System.out.println("1. if check: " + len);                                         // 0
    System.out.println("2. ternary: " + ((name != null) ? name.length() : 0));          // 0
    System.out.println("3. Optional: " + Optional.ofNullable(name).map(String::length).orElse(0)); // 0
    System.out.println("4. literal equals: " + "Lokesh".equals(name));                 // false
    System.out.println("5. Objects.equals: " + Objects.equals(name, null));            // true
    System.out.println("6. requireNonNullElse: " + Objects.requireNonNullElse(name, "guest")); // guest
    System.out.println("7. requireNonNullElseGet: "
        + Objects.requireNonNullElseGet(name, () -> "guest-" + ages.size()));          // guest-1
    System.out.println("8. getOrDefault: " + ages.getOrDefault("Alex", 0));            // 0
    System.out.println("9. getOrDefault hit: " + ages.getOrDefault("Lokesh", 0));      // 37
    try {
      greet(name);
    } catch (NullPointerException e) {
      System.out.println("10. requireNonNull: " + e);
    }
    try {
      lengthOf(name);
    } catch (NullPointerException e) {
      System.out.println("11. deep NPE: " + e);
    }

    // 2. Records validate in the compact constructor
    try {
      new Song(null, new Artist("Queen"));
    } catch (NullPointerException e) {
      System.out.println("12. record: " + e);
    }

    // 3. Optional return values
    Song imagine = new Song("Imagine", null);
    System.out.println("13. findArtist: " + imagine.findArtist());                     // Optional.empty
    System.out.println("14. artist name: "
        + imagine.findArtist().map(Artist::name).orElse("unknown artist"));            // unknown artist
    System.out.println("15. genre: " + findGenre("Imagine").orElse("none"));           // none
    System.out.println("16. genre hit: " + findGenre("Hey Jude").orElse("none"));      // pop
    try {
      Optional.of(name);
    } catch (NullPointerException e) {
      System.out.println("17. Optional.of(null): " + e);
    }
    try {
      findGenre("Imagine").get();
    } catch (RuntimeException e) {
      System.out.println("18. get() on empty: " + e);
    }

    Song noSong = null;
    System.out.println("18b. Optional chain: " + Optional.ofNullable(noSong)
        .map(Song::artist).map(Artist::name).orElse("unknown"));                       // unknown

    // 4. Empty collections instead of null
    List<Song> library = List.of(new Song("Hey Jude", new Artist("The Beatles")), imagine);
    System.out.println("19. songsBy(null): " + songsBy(null, library).size());          // 0
    System.out.println("20. songsBy(Beatles): " + songsBy("The Beatles", library).size()); // 1

    // 5. Null-safe operators and methods
    Object data = null;
    System.out.println("21. instanceof: " + (data instanceof String));                 // false
    System.out.println("22. String.valueOf(Object): " + String.valueOf(data));         // null
    System.out.println("23. concatenation: " + ("Hi " + name));                        // Hi null
    System.out.println("24. Objects.toString: " + Objects.toString(name, "-"));        // -
    System.out.println("25. Objects.hash: " + Objects.hashCode(name));                 // 0
    System.out.println("26. StringUtils.isEmpty: " + StringUtils.isEmpty(name));       // true
    System.out.println("27. StringUtils.length: " + StringUtils.length(name));         // 0

    System.out.println("27a. StringUtils.isNotEmpty: " + StringUtils.isNotEmpty(name));  // false
    System.out.println("27c. StringUtils.equals: " + StringUtils.equals(null, "a"));    // false
    NpeFixes fixes = null;
    System.out.println("27b. static via null: " + fixes.maxSongs);                    // 100

    // 6. switch with case null (Java 21)
    System.out.println("28. describe(null): " + describe(null));                       // no value
    System.out.println("29. describe(\"Lokesh\"): " + describe("Lokesh"));
    System.out.println("30. dayType(null): " + dayType(null));                         // unknown
    System.out.println("31. dayType(SUN): " + dayType("SUN"));                         // weekend
  }
}
