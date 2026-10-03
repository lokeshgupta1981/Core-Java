package com.howtodoinjava.core.sorting;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Runs every sorting example of the article "How to Sort an Array, List, Map or Stream
 * in Java": arrays of primitives and objects, range and parallel sorts, lists, streams,
 * Comparable vs Comparator, comparator chains, null handling, maps, TreeMap/TreeSet,
 * stable sorting, case-insensitive and locale-aware String sorting, and common mistakes.
 * Each line prints the result that the article shows as a comment.
 */
public class SortingGuide {

  /** A song in a playlist. The natural order (Comparable) is by title. */
  record Song(String title, String artist, int year, int plays) implements Comparable<Song> {

    @Override
    public int compareTo(Song other) {
      return this.title.compareTo(other.title);
    }

    @Override
    public String toString() {
      return title;
    }
  }

  static List<Song> playlist() {
    return new ArrayList<>(List.of(
        new Song("Rain", "Ben", 2021, 300),
        new Song("Blue", "Ava", 2019, 120),
        new Song("Echo", "Ava", 2023, 450),
        new Song("Sun", "Ben", 2019, 80)));
  }

  public static void main(String[] args) {
    quickReference();
    comparableVsComparator();
    nullValues();
    arrays();
    lists();
    maps();
    sortedCollections();
    strings();
    stableSort();
    mistakes();
    faqs();
  }

  static void quickReference() {
    System.out.println("== Quick reference");
    // 1. Arrays
    int[] numbers = {5, 3, 9, 1};
    Arrays.sort(numbers);
    System.out.println("int[] sort        " + Arrays.toString(numbers));

    String[] fruits = {"banana", "apple", "cherry"};
    Arrays.sort(fruits, Comparator.reverseOrder());
    System.out.println("String[] reverse  " + Arrays.toString(fruits));

    // 2. Lists
    List<String> names = new ArrayList<>(List.of("Lokesh", "Alex", "John"));
    Collections.sort(names);
    System.out.println("Collections.sort  " + names);
    names.sort(Comparator.reverseOrder());
    System.out.println("list.sort reverse " + names);

    // 3. Streams
    List<Integer> sorted = Stream.of(5, 3, 9).sorted().toList();
    System.out.println("stream sorted     " + sorted);

    // 4. Objects
    List<Song> songs = playlist();
    songs.sort(Comparator.comparing(Song::artist).thenComparing(Song::year));
    System.out.println("artist, year      " + songs);
    songs.sort(Comparator.comparing(Song::plays).reversed());
    System.out.println("plays desc        " + songs);

    // 5. nulls
    List<String> withNull = Arrays.asList("pear", null, "fig");
    withNull.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
    System.out.println("nullsFirst        " + withNull);
  }

  static void comparableVsComparator() {
    System.out.println("== Comparable vs Comparator");
    List<Song> songs = playlist();
    Collections.sort(songs);
    System.out.println("natural (title)   " + songs);

    songs.sort(Comparator.comparingInt(Song::plays));
    System.out.println("plays asc         " + songs);

    songs.sort(Comparator.comparing(Song::artist)
        .thenComparing(Song::plays, Comparator.reverseOrder()));
    System.out.println("artist, plays desc " + songs);

    songs.sort(Comparator.comparing(Song::artist).thenComparing(Song::year).reversed());
    System.out.println("whole chain reversed " + songs);

    songs.sort(Comparator.comparing(Song::year).reversed().thenComparing(Song::title));
    System.out.println("year desc, title  " + songs);

    Comparator<Song> byArtist = (a, b) -> a.artist().compareTo(b.artist());
    songs.sort(byArtist);
    System.out.println("lambda byArtist   " + songs);
  }

  static void nullValues() {
    System.out.println("== Nulls");
    List<String> withNull = Arrays.asList("pear", null, "fig");
    withNull.sort(Comparator.nullsLast(Comparator.naturalOrder()));
    System.out.println("nullsLast         " + withNull);
    try {
      Arrays.asList("pear", null, "fig").sort(Comparator.naturalOrder());
    } catch (NullPointerException e) {
      System.out.println("natural with null " + e.getClass().getSimpleName());
    }
  }

  static void arrays() {
    System.out.println("== Arrays");
    int[] numbers = {5, 3, 9, 1};
    Arrays.sort(numbers);
    // Arrays.sort(numbers, Comparator.reverseOrder()); // does not compile: int[] is not T[]

    int[] desc = IntStream.of(5, 3, 9, 1)
        .boxed()
        .sorted(Comparator.reverseOrder())
        .mapToInt(Integer::intValue)
        .toArray();
    System.out.println("int[] desc stream " + Arrays.toString(desc));

    int[] reversed = {5, 3, 9, 1};
    Arrays.sort(reversed);
    for (int i = 0, j = reversed.length - 1; i < j; i++, j--) {
      int tmp = reversed[i];
      reversed[i] = reversed[j];
      reversed[j] = tmp;
    }
    System.out.println("int[] desc swap   " + Arrays.toString(reversed));

    Integer[] boxed = {5, 3, 9, 1};
    Arrays.sort(boxed, Comparator.reverseOrder());
    System.out.println("Integer[] desc    " + Arrays.toString(boxed));

    Song[] songs = playlist().toArray(new Song[0]);
    Arrays.sort(songs, Comparator.comparingInt(Song::plays));
    System.out.println("Song[] by plays   " + Arrays.toString(songs));

    int[] range = {9, 7, 5, 3, 1, 0};
    Arrays.sort(range, 1, 4);
    System.out.println("range 1..4        " + Arrays.toString(range));

    int[] big = new java.util.Random(42).ints(1_000_000, 0, 1000).toArray();
    int[] copy = big.clone();
    Arrays.parallelSort(big);
    Arrays.sort(copy);
    System.out.println("parallel == sort  " + Arrays.equals(big, copy));

    String[] words = {"pear", "fig", "apple"};
    Arrays.parallelSort(words, Comparator.reverseOrder());
    System.out.println("parallel reverse  " + Arrays.toString(words));
  }

  static void lists() {
    System.out.println("== Lists");
    List<String> names = new ArrayList<>(List.of("Lokesh", "Alex", "John"));
    List<String> copy = names.stream().sorted().toList();
    System.out.println("stream copy       " + copy + " source " + names);

    names.sort(null);
    System.out.println("sort(null)        " + names);

    try {
      List.of("b", "a").sort(null);
    } catch (UnsupportedOperationException e) {
      System.out.println("List.of sort      " + e.getClass().getSimpleName());
    }
    try {
      Stream.of("b", "a").toList().sort(null);
    } catch (UnsupportedOperationException e) {
      System.out.println("toList() sort     " + e.getClass().getSimpleName());
    }
  }

  static void maps() {
    System.out.println("== Maps");
    Map<String, Integer> ages = Map.of("Lokesh", 37, "John", 40, "Alex", 25);

    Map<String, Integer> byKey = new TreeMap<>(ages);
    System.out.println("TreeMap by key    " + byKey);

    Map<String, Integer> byKeyDesc = new TreeMap<>(Comparator.reverseOrder());
    byKeyDesc.putAll(ages);
    System.out.println("TreeMap key desc  " + byKeyDesc);

    Map<String, Integer> byValue = ages.entrySet().stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (a, b) -> a, LinkedHashMap::new));
    System.out.println("by value          " + byValue);

    Map<String, Integer> byValueDesc = ages.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (a, b) -> a, LinkedHashMap::new));
    System.out.println("by value desc     " + byValueDesc);

    Map<String, Integer> toHashMap = ages.entrySet().stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    System.out.println("into HashMap      " + toHashMap + " " + toHashMap.getClass().getSimpleName());
  }

  static void sortedCollections() {
    System.out.println("== TreeSet");
    TreeSet<String> tags = new TreeSet<>(List.of("rock", "jazz", "pop", "jazz"));
    System.out.println("TreeSet           " + tags);
    System.out.println("first/last        " + tags.first() + " " + tags.last());
    System.out.println("headSet(pop)      " + tags.headSet("pop"));

    TreeSet<Song> byArtist = new TreeSet<>(Comparator.comparing(Song::artist));
    byArtist.addAll(playlist());
    System.out.println("TreeSet by artist " + byArtist + " size " + byArtist.size());

    TreeSet<Song> byArtistTitle = new TreeSet<>(
        Comparator.comparing(Song::artist).thenComparing(Song::title));
    byArtistTitle.addAll(playlist());
    System.out.println("artist, title     " + byArtistTitle + " size " + byArtistTitle.size());
  }

  static void strings() {
    System.out.println("== Strings");
    List<String> names = new ArrayList<>(List.of("bob", "Alice", "alex", "Bea"));
    names.sort(null);
    System.out.println("natural           " + names);
    names.sort(String.CASE_INSENSITIVE_ORDER);
    System.out.println("case-insensitive  " + names);
    names.sort(Comparator.comparing(String::length).thenComparing(String.CASE_INSENSITIVE_ORDER));
    System.out.println("length, then name " + names);

    List<String> words = new ArrayList<>(List.of("zebra", "\u00e9clair", "eclair", "Apple"));
    words.sort(null);
    System.out.println("natural           " + escape(words));
    words.sort(Collator.getInstance(Locale.FRENCH));
    System.out.println("Collator FRENCH   " + escape(words));

    Collator primary = Collator.getInstance(Locale.ENGLISH);
    primary.setStrength(Collator.PRIMARY);
    System.out.println("PRIMARY eclair    " + primary.compare("eclair", "\u00c9clair"));
    System.out.println("compareTo eclair  " + "eclair".compareTo("\u00c9clair"));
  }

  static void stableSort() {
    System.out.println("== Stable sort");
    List<Song> songs = playlist();
    songs.sort(Comparator.comparingInt(Song::plays).reversed());
    System.out.println("by plays desc     " + songs);
    songs.sort(Comparator.comparing(Song::artist));
    System.out.println("then by artist    " + songs);
  }

  static void mistakes() {
    System.out.println("== Mistakes");
    List<Integer> values = new ArrayList<>(List.of(-2_000_000_000, 2_000_000_000, 0));
    values.sort((a, b) -> a - b);
    System.out.println("a - b             " + values);
    System.out.println("-2e9 - 2e9        " + (-2_000_000_000 - 2_000_000_000));
    values.sort(Integer::compare);
    System.out.println("Integer::compare  " + values);
  }

  /** A class without Comparable, to show the runtime error of a natural-order sort. */
  record Playlist(String name) {
  }

  static void faqs() {
    System.out.println("== FAQs and extras");
    List<Playlist> playlists = new ArrayList<>(List.of(new Playlist("b"), new Playlist("a")));
    try {
      playlists.sort(null);
    } catch (ClassCastException e) {
      System.out.println("no Comparable      " + e.getClass().getSimpleName());
    }

    String[] words = {"fig", "pear", "apple", "kiwi"};
    Arrays.sort(words, 0, 2, Comparator.reverseOrder());
    System.out.println("range reverse     " + Arrays.toString(words));

    Integer[] boxed = {5, 3, 9, 1};
    Arrays.sort(boxed, Collections.reverseOrder());
    System.out.println("Collections.reverseOrder " + Arrays.toString(boxed));

    java.util.Set<String> tags = new java.util.HashSet<>(java.util.Set.of("rock", "jazz", "pop"));
    System.out.println("TreeSet copy      " + new TreeSet<>(tags));
    System.out.println("sorted list       " + tags.stream().sorted().toList());

    java.util.Random random = new java.util.Random(1);
    List<Integer> many = new ArrayList<>();
    for (int i = 0; i < 10_000; i++) {
      many.add(random.nextInt(100));
    }
    try {
      many.sort((x, y) -> x < y ? -1 : 1);   // never returns 0
    } catch (IllegalArgumentException e) {
      System.out.println("broken comparator " + e.getMessage());
    }
  }

  private static String escape(List<String> list) {
    StringBuilder sb = new StringBuilder();
    for (char c : list.toString().toCharArray()) {
      sb.append(c < 128 ? String.valueOf(c) : String.format("\\u%04x", (int) c));
    }
    return sb.toString();
  }
}
