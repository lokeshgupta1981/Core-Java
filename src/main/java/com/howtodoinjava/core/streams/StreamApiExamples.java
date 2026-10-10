package com.howtodoinjava.core.streams;

import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

/**
 * Examples for the tutorial "Java Stream API Examples: Create, Filter, Collect (Java 25)".
 * https://howtodoinjava.com/java/stream/java-streams-by-examples/
 */
public class StreamApiExamples {
    static record Song(String title, String artist, int seconds) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> songs = List.of("Help", "Imagine", "Yesterday", "Hey Jude", "Let It Be");

            List<String> picked = songs.stream()
                    .filter(s -> s.length() > 4)
                    .map(String::toUpperCase)
                    .sorted()
                    .toList();                          // [HEY JUDE, IMAGINE, LET IT BE, YESTERDAY]
            long withSpace = songs.stream().filter(s -> s.contains(" ")).count();   // 2
            show("withSpace", withSpace);
            boolean anyH = songs.stream().anyMatch(s -> s.startsWith("H"));         // true
            show("anyH", anyH);
            int letters = songs.stream().mapToInt(String::length).sum();            // 37
            show("letters", letters);
            String first = songs.stream().filter(s -> s.startsWith("Y")).findFirst().orElse("none");   // "Yesterday"
            show("first", first);
            Map<Integer, List<String>> byLength = songs.stream()
                    .collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.toList()));
            String grouped = byLength.toString();   // "{4=[Help], 7=[Imagine], 8=[Hey Jude], 9=[Yesterday, Let It Be]}"
            show("grouped", grouped);
        }
        {
            List<String> songs = List.of("Help", "Imagine", "Yesterday");
            long count = songs.stream().count();                     // 3
            show("count", count);

            Map<String, Integer> plays = new TreeMap<>(Map.of("Help", 12, "Imagine", 30));
            List<String> titles = plays.keySet().stream().toList();                  // [Help, Imagine]
            show("titles", titles);
            int totalPlays = plays.values().stream().mapToInt(Integer::intValue).sum();   // 42
            show("totalPlays", totalPlays);
            List<String> lines = plays.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .toList();                                       // [Help=12, Imagine=30]
        }
        {
            Stream<String> genres = Stream.of("rock", "jazz", "pop");
            String[] titles = {"Help", "Imagine", "Yesterday"};
            List<String> fromArray = Arrays.stream(titles).toList();       // [Help, Imagine, Yesterday]
            show("fromArray", fromArray);
            List<String> middle = Arrays.stream(titles, 1, 3).toList();    // [Imagine, Yesterday]
            show("middle", middle);
            int[] ratings = {4, 5, 3};
            int best = Arrays.stream(ratings).max().orElse(0);             // 5
            show("best", best);
            long wrong = Stream.of(ratings).count();                       // 1, the array itself
            show("wrong", wrong);
            long none = Stream.empty().count();                            // 0
            show("none", none);
        }
        {
            Map<String, String> albums = Map.of("Help", "Help!", "Imagine", "Imagine");
            List<String> found = Stream.of("Help", "Yesterday", "Imagine")
                    .flatMap(t -> Stream.ofNullable(albums.get(t)))
                    .toList();                                       // [Help!, Imagine]
        }
        {
            boolean addBonus = true;
            Stream.Builder<String> builder = Stream.builder();
            builder.add("Help").add("Imagine");
            if (addBonus) {
                builder.add("Bonus Track");
            }
            List<String> tracks = builder.build().toList();          // [Help, Imagine, Bonus Track]
            show("tracks", tracks);
        }
        {
            List<Integer> evens = Stream.iterate(0, n -> n + 2).limit(5).toList();   // [0, 2, 4, 6, 8]
            show("evens", evens);
            List<Integer> doubling = Stream.iterate(1, n -> n <= 100, n -> n * 2).toList();   // [1, 2, 4, 8, 16, 32, 64]
            show("doubling", doubling);
            List<String> beats = Stream.generate(() -> "tick").limit(3).toList();   // [tick, tick, tick]
            show("beats", beats);
            List<Integer> dice = new Random(42).ints(3, 1, 7).boxed().toList();      // [3, 4, 1]
            show("dice", dice);
        }
        {
            long vowels = "Yesterday".chars().filter(c -> "aeiou".indexOf(c) >= 0).count();   // 3
            show("vowels", vowels);
            List<String> rows = "Help\nImagine".lines().toList();                         // [Help, Imagine]
            show("rows", rows);
            List<String> tags = Pattern.compile(",\\s*").splitAsStream("rock, pop,jazz").toList();   // [rock, pop, jazz]
            show("tags", tags);
        }
        {
            Path file = Files.createTempFile("playlist", ".txt");
            Files.writeString(file, "Help\nImagine\nYesterday\n");
            List<String> longTitles;
            try (Stream<String> lines = Files.lines(file)) {
                longTitles = lines.filter(line -> line.length() > 4).toList();
            }
            List<String> loaded = longTitles;                        // [Imagine, Yesterday]
            show("loaded", loaded);
        }
        {
            List<List<String>> albums = List.of(
            List.of("Help", "Yesterday"),
            List.of("Imagine", "Jealous Guy"),
            List.of("Help", "Let It Be"));

            List<String> all = albums.stream().flatMap(List::stream).toList();   // [Help, Yesterday, Imagine, Jealous Guy, Help, Let It Be]
            show("all", all);
            List<String> unique = all.stream().distinct().toList();              // [Help, Yesterday, Imagine, Jealous Guy, Let It Be]
            show("unique", unique);
            List<Integer> lengths = unique.stream().map(String::length).toList();   // [4, 9, 7, 11, 9]
            show("lengths", lengths);
            List<String> byLength = unique.stream()
                    .sorted(Comparator.comparing(String::length).thenComparing(Comparator.naturalOrder()))
                    .toList();                                       // [Help, Imagine, Let It Be, Yesterday, Jealous Guy]
            List<String> page2 = unique.stream().skip(2).limit(2).toList();      // [Imagine, Jealous Guy]
            show("page2", page2);
        }
        {
            List<Integer> seconds = List.of(120, 150, 200, 240, 180);
            List<Integer> taken = seconds.stream().takeWhile(s -> s < 210).toList();     // [120, 150, 200]
            show("taken", taken);
            List<Integer> dropped = seconds.stream().dropWhile(s -> s < 210).toList();   // [240, 180]
            show("dropped", dropped);
            List<Integer> filtered = seconds.stream().filter(s -> s < 210).toList();     // [120, 150, 200, 180]
            show("filtered", filtered);
        }
        {
            List<String> songs = List.of("Help", "Imagine", "Yesterday", "Hey Jude");

            long count = songs.stream().filter(s -> s.startsWith("H")).count();   // 2
            show("count", count);
            boolean all = songs.stream().allMatch(s -> s.length() > 3);          // true
            show("all", all);
            boolean none = songs.stream().noneMatch(String::isBlank);            // true
            show("none", none);
            Optional<String> longest = songs.stream().max(Comparator.comparing(String::length));   // Optional[Yesterday]
            show("longest", longest);
            String shortest = songs.stream().min(Comparator.comparing(String::length)).orElseThrow();   // "Help"
            show("shortest", shortest);
            String firstJ = songs.stream().filter(s -> s.startsWith("J")).findFirst().orElse("none");   // "none"
            show("firstJ", firstJ);
            String joined = songs.stream().reduce((a, b) -> a + " | " + b).orElse("");   // "Help | Imagine | Yesterday | Hey Jude"
            show("joined", joined);
            int totalChars = songs.stream().map(String::length).reduce(0, Integer::sum);   // 28
            show("totalChars", totalChars);
            String[] array = songs.stream().toArray(String[]::new);
            String printed = Arrays.toString(array);                             // "[Help, Imagine, Yesterday, Hey Jude]"
            show("printed", printed);
        }
        {
            try { String missing = Stream.of("Help").filter(s -> s.startsWith("Z")).findFirst().get(); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
        }
        {
            List<Song> playlist = List.of(
            new Song("Help", "Beatles", 139),
            new Song("Imagine", "Lennon", 183),
            new Song("Yesterday", "Beatles", 125),
            new Song("Jealous Guy", "Lennon", 254));

            Set<String> artists = playlist.stream().map(Song::artist).collect(Collectors.toCollection(TreeSet::new));   // [Beatles, Lennon]
            show("artists", artists);
            Map<String, Integer> length = playlist.stream()
                    .collect(Collectors.toMap(Song::title, Song::seconds, (a, b) -> a, TreeMap::new));   // {Help=139, Imagine=183, Jealous Guy=254, Yesterday=125}
            String titles = playlist.stream().map(Song::title).collect(Collectors.joining(", ", "[", "]"));   // "[Help, Imagine, Yesterday, Jealous Guy]"
            show("titles", titles);
            Map<String, Long> songsPerArtist = playlist.stream()
                    .collect(Collectors.groupingBy(Song::artist, TreeMap::new, Collectors.counting()));   // {Beatles=2, Lennon=2}
            Map<Boolean, List<String>> longOnes = playlist.stream()
                    .collect(Collectors.partitioningBy(s -> s.seconds() > 180,
            Collectors.mapping(Song::title, Collectors.toList())));   // {false=[Help, Yesterday], true=[Imagine, Jealous Guy]}
        }
        {
            List<String> catalog = List.of("help", "imagine", "yesterday", "hey jude", "let it be");
            AtomicInteger calls = new AtomicInteger();

            Stream<String> pipeline = catalog.stream()
                    .map(s -> { calls.incrementAndGet(); return s.toUpperCase(); })
                    .filter(s -> s.startsWith("I"));
            int beforeTerminal = calls.get();                        // 0
            show("beforeTerminal", beforeTerminal);
            String match = pipeline.findFirst().orElse("none");      // "IMAGINE"
            show("match", match);
            int afterTerminal = calls.get();                         // 2
            show("afterTerminal", afterTerminal);
        }
        {
            List<Song> history = List.of(
            new Song("Help", "Beatles", 139),
            new Song("Imagine", "Lennon", 183),
            new Song("Help", "Beatles", 139),
            new Song("Yesterday", "Beatles", 125),
            new Song("Jealous Guy", "Lennon", 254),
            new Song("Respect", "Franklin", 147));

            int totalSeconds = history.stream().mapToInt(Song::seconds).sum();   // 987
            show("totalSeconds", totalSeconds);
            String totalTime = (totalSeconds / 60) + " min " + (totalSeconds % 60) + " s";   // "16 min 27 s"
            show("totalTime", totalTime);
            String topArtist = history.stream()
                    .collect(Collectors.groupingBy(Song::artist, Collectors.summingInt(Song::seconds)))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("nobody");                               // "Lennon"
            List<String> longest = history.stream()
                    .distinct()
                    .sorted(Comparator.comparingInt(Song::seconds).reversed())
                    .limit(3)
                    .map(Song::title)
                    .toList();                                       // [Jealous Guy, Imagine, Respect]
            Map<String, Long> playsPerSong = history.stream()
                    .collect(Collectors.groupingBy(Song::title, TreeMap::new, Collectors.counting()));   // {Help=2, Imagine=1, Jealous Guy=1, Respect=1, Yesterday=1}
        }
        {
            List<Integer> ids = IntStream.rangeClosed(1, 1_000).boxed().toList();
            long evenCount = ids.parallelStream().filter(n -> n % 2 == 0).count();   // 500
            show("evenCount", evenCount);
            List<Integer> firstFive = ids.parallelStream().map(n -> n * 10).limit(5).toList();   // [10, 20, 30, 40, 50]
            show("firstFive", firstFive);
            boolean parallel = ids.parallelStream().isParallel();    // true
            show("parallel", parallel);
        }
        {
            Stream<String> once = Stream.of("Help", "Imagine");
            long size = once.count();                                // 2
            show("size", size);
            try { long again = once.count(); show("again", again); } catch (Throwable _t) { System.out.println("again -> " + _t); }
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
