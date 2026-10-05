package com.howtodoinjava.core.streams.distinct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Removes duplicate objects from a stream by one or more fields: a distinctByKey() predicate with a
 * composite key (List.of(), a record key, a varargs key list), Collectors.toMap() to keep the first,
 * last or most played element, a TreeSet with a Comparator, plain distinct() with record equals(),
 * and the pitfalls of reusing the predicate, null fields and parallel streams.
 */
public class DistinctByMultipleFields {

  /** A song in a playlist; the same title and artist can appear more than once. */
  record Song(String title, String artist, int plays) {
    @Override
    public String toString() {
      return title + "/" + artist + "/" + plays;
    }
  }

  /** A composite key made of the fields that decide if two songs are duplicates. */
  record SongKey(String title, String artist) {
    SongKey(Song song) {
      this(song.title(), song.artist());
    }

    @Override
    public String toString() {
      return title + "/" + artist;
    }
  }

  static final List<Song> SONGS = List.of(
      new Song("rain", "anna", 120),
      new Song("home", "ben", 90),
      new Song("rain", "anna", 75),
      new Song("home", "carl", 60),
      new Song("home", "ben", 40),
      new Song("rain", "anna", 75));

  /** Returns a stateful predicate that passes only the first element seen for each key. */
  public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }

  /** Same as distinctByKey(), but builds the key from several key extractors. */
  @SafeVarargs
  public static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
    List<Function<? super T, ?>> extractors = List.of(keyExtractors);
    Set<List<?>> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(extractors.stream().map(ke -> ke.apply(t)).toList());
  }

  public static void main(String[] args) {
    // 1. Plain distinct() uses the record's equals() and hashCode() on all fields
    System.out.println("distinct(): " + SONGS.stream().distinct().toList());

    // 2. Distinct by title and artist with a List key
    List<Song> byList = SONGS.stream()
        .filter(distinctByKey(s -> List.of(s.title(), s.artist())))
        .toList();
    System.out.println("List key: " + byList);

    // 3. Distinct by title and artist with a record key
    List<Song> byRecord = SONGS.stream().filter(distinctByKey(SongKey::new)).toList();
    System.out.println("record key: " + byRecord);

    // 4. Distinct with varargs key extractors
    List<Song> byVarargs = SONGS.stream()
        .filter(distinctByKeys(Song::title, Song::artist))
        .toList();
    System.out.println("varargs keys: " + byVarargs);

    // 5. Distinct by one field
    List<Song> byTitle = SONGS.stream().filter(distinctByKey(Song::title)).toList();
    System.out.println("by title: " + byTitle);

    // 6. toMap(): keep the first occurrence, in encounter order
    List<Song> keepFirst = SONGS.stream()
        .collect(Collectors.collectingAndThen(
            Collectors.toMap(SongKey::new, Function.identity(), (a, b) -> a, LinkedHashMap::new),
            m -> new ArrayList<>(m.values())));
    System.out.println("toMap keep first: " + keepFirst);

    // 7. toMap(): keep the last occurrence
    List<Song> keepLast = SONGS.stream()
        .collect(Collectors.collectingAndThen(
            Collectors.toMap(SongKey::new, Function.identity(), (a, b) -> b, LinkedHashMap::new),
            m -> new ArrayList<>(m.values())));
    System.out.println("toMap keep last: " + keepLast);

    // 8. toMap(): keep the most played song per key
    List<Song> keepMostPlayed = SONGS.stream()
        .collect(Collectors.collectingAndThen(
            Collectors.toMap(SongKey::new, Function.identity(),
                BinaryOperator.maxBy(Comparator.comparingInt(Song::plays)), LinkedHashMap::new),
            m -> new ArrayList<>(m.values())));
    System.out.println("toMap keep most played: " + keepMostPlayed);

    // 9. Without LinkedHashMap, toMap() returns a HashMap with no fixed order
    Map<SongKey, Song> hashMap = SONGS.stream()
        .collect(Collectors.toMap(SongKey::new, Function.identity(), (a, b) -> a));
    System.out.println("toMap HashMap class: " + hashMap.getClass().getSimpleName());

    // 10. TreeSet with a Comparator: distinct and sorted by the compared fields
    Comparator<Song> byTitleAndArtist =
        Comparator.comparing(Song::title).thenComparing(Song::artist);
    List<Song> sorted = SONGS.stream()
        .collect(Collectors.collectingAndThen(
            Collectors.toCollection(() -> new TreeSet<>(byTitleAndArtist)),
            ArrayList::new));
    System.out.println("TreeSet: " + sorted);

    // 11. Count distinct elements and count duplicates per key
    long distinctCount = SONGS.stream().filter(distinctByKey(SongKey::new)).count();
    System.out.println("distinct count: " + distinctCount);
    Map<SongKey, Long> countPerKey = SONGS.stream()
        .collect(Collectors.groupingBy(SongKey::new, LinkedHashMap::new, Collectors.counting()));
    System.out.println("count per key: " + countPerKey);

    // 12. Distinct keys only (no Song objects)
    List<SongKey> keys = SONGS.stream().map(SongKey::new).distinct().toList();
    System.out.println("distinct keys: " + keys);

    // 13. A predicate instance remembers its keys: reusing it filters everything out
    Predicate<Song> once = distinctByKey(SongKey::new);
    System.out.println("first use: " + SONGS.stream().filter(once).count());
    System.out.println("second use: " + SONGS.stream().filter(once).count());

    // 14. List.of() rejects null fields; Arrays.asList() and a record key accept them
    List<Song> withNull = List.of(new Song("rain", null, 10), new Song("rain", null, 20));
    try {
      withNull.stream().filter(distinctByKey(s -> List.of(s.title(), s.artist()))).toList();
    } catch (NullPointerException e) {
      System.out.println("List.of key with null: NullPointerException");
    }
    System.out.println("Arrays.asList key with null: " + withNull.stream()
        .filter(distinctByKey(s -> Arrays.asList(s.title(), s.artist()))).toList());
    System.out.println("record key with null: " + withNull.stream()
        .filter(distinctByKey(SongKey::new)).toList());

    // 15. String concatenation as a key can merge different songs
    List<Song> tricky = List.of(new Song("ab", "c", 1), new Song("a", "bc", 2));
    System.out.println("concat key: " + tricky.stream()
        .filter(distinctByKey(s -> s.title() + s.artist())).toList());
    System.out.println("record key: " + tricky.stream()
        .filter(distinctByKey(SongKey::new)).toList());

    // 16. Parallel streams: distinct() and toMap() keep the first element; the predicate may not
    List<Song> big = new ArrayList<>();
    for (int i = 0; i < 100_000; i++) {
      big.add(new Song("song" + (i % 100), "anna", i));
    }
    List<Song> expected = big.stream().filter(distinctByKey(SongKey::new)).toList();
    int runs = 50;
    int predicateDiffers = 0;
    int toMapDiffers = 0;
    for (int run = 0; run < runs; run++) {
      List<Song> viaPredicate = big.parallelStream().filter(distinctByKey(SongKey::new)).toList();
      if (!viaPredicate.equals(expected)) {
        predicateDiffers++;
      }
      List<Song> viaToMap = big.parallelStream()
          .collect(Collectors.collectingAndThen(
              Collectors.toMap(SongKey::new, Function.identity(), (a, b) -> a, LinkedHashMap::new),
              m -> new ArrayList<>(m.values())));
      if (!viaToMap.equals(expected)) {
        toMapDiffers++;
      }
    }
    System.out.println("sequential first 3: " + expected.subList(0, 3));
    System.out.println("parallel first 3: " + big.parallelStream()
        .filter(distinctByKey(SongKey::new)).toList().subList(0, 3));
    System.out.println("parallel predicate differs from sequential: " + predicateDiffers + "/" + runs);
    System.out.println("parallel toMap differs from sequential: " + toMapDiffers + "/" + runs);

    // 17. Group duplicates by key to see what was removed
    Map<SongKey, List<Integer>> playsPerKey = SONGS.stream()
        .collect(Collectors.groupingBy(SongKey::new, LinkedHashMap::new,
            Collectors.mapping(Song::plays, Collectors.toList())));
    System.out.println("plays per key: " + playsPerKey);
  }
}
