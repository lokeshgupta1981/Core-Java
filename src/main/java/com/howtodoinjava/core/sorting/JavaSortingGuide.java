package com.howtodoinjava.core.sorting;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;
import java.text.Collator;

/**
 * Examples for the tutorial "Sorting in Java: Arrays, Lists, Maps, Streams, Comparators".
 * https://howtodoinjava.com/java/sort/java-sorting/
 */
public class JavaSortingGuide {
    static record Song(String title, String artist, int year, int plays) implements Comparable<Song> {

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
    public static void main(String[] args) throws Exception {
        {
            // 1. Arrays
            int[] numbers = {5, 3, 9, 1};
            Arrays.sort(numbers);                                   // [1, 3, 5, 9]
            String[] fruits = {"banana", "apple", "cherry"};
            Arrays.sort(fruits, Comparator.reverseOrder());         // [cherry, banana, apple]

            // 2. Lists
            List<String> names = new ArrayList<>(List.of("Lokesh", "Alex", "John"));
            Collections.sort(names);                                // [Alex, John, Lokesh]
            names.sort(Comparator.reverseOrder());                  // [Lokesh, John, Alex]

            // 3. Streams (a new list; the source stays unchanged)
            List<Integer> sorted = Stream.of(5, 3, 9).sorted().toList();       // [3, 5, 9]
            show("sorted", sorted);

            // 4. Objects: by artist, then by year; by plays, highest first
            List<Song> songs = playlist();
            songs.sort(Comparator.comparing(Song::artist)
                    .thenComparing(Song::year));                        // [Blue, Echo, Sun, Rain]
            songs.sort(Comparator.comparing(Song::plays).reversed());          // [Echo, Rain, Blue, Sun]

            // 5. Lists that contain null
            List<String> withNull = Arrays.asList("pear", null, "fig");
            withNull.sort(Comparator.nullsFirst(Comparator.naturalOrder()));   // [null, fig, pear]
        }
        {
            List<Song> songs = playlist();
            Collections.sort(songs);                     // [Blue, Echo, Rain, Sun]
            songs.sort(null);                            // [Blue, Echo, Rain, Sun], null means natural order
            List<Song> copy = playlist().stream().sorted().toList();   // [Blue, Echo, Rain, Sun]
            show("copy", copy);
        }
        {
            List<Song> songs = playlist();
            songs.sort(Comparator.comparingInt(Song::plays));                 // [Sun, Blue, Rain, Echo]
            songs.sort(Comparator.comparing(Song::plays).reversed());         // [Echo, Rain, Blue, Sun]

            songs.sort(Comparator.comparing(Song::artist)
                    .thenComparing(Song::plays, Comparator.reverseOrder()));      // [Echo, Blue, Rain, Sun]

            songs.sort(Comparator.comparing(Song::year).reversed()
                    .thenComparing(Song::title));                                 // [Echo, Rain, Blue, Sun]

            Comparator<Song> byArtist = (a, b) -> a.artist().compareTo(b.artist());   // lambda form
            show("byArtist", byArtist);
        }
        {
            List<String> withNull = Arrays.asList("pear", null, "fig");
            try { withNull.sort(Comparator.naturalOrder());  } catch (Throwable _t) { System.out.println("-> " + _t); }
            withNull.sort(Comparator.nullsFirst(Comparator.naturalOrder()));  // [null, fig, pear]
            withNull.sort(Comparator.nullsLast(Comparator.naturalOrder()));   // [fig, pear, null]

            // null fields inside objects
            List<Song> songs = playlist();
            songs.add(new Song("Mix", null, 2024, 10));
            songs.sort(Comparator.comparing(Song::artist, Comparator.nullsLast(Comparator.naturalOrder())));   // [Blue, Echo, Rain, Sun, Mix]
        }
        {
            // Option 1: box, sort with a Comparator, unbox
            int[] desc = IntStream.of(5, 3, 9, 1)
                    .boxed()
                    .sorted(Comparator.reverseOrder())
                    .mapToInt(Integer::intValue)
                    .toArray();                                    // [9, 5, 3, 1]

            // Option 2: sort ascending, swap from both ends (no boxing)
            int[] numbers = {5, 3, 9, 1};
            Arrays.sort(numbers);                              // [1, 3, 5, 9]
            for (int i = 0, j = numbers.length - 1; i < j; i++, j--) {
                int tmp = numbers[i];
                numbers[i] = numbers[j];
                numbers[j] = tmp;
            }
            String reversed = Arrays.toString(numbers);        // [9, 5, 3, 1]
            show("reversed", reversed);

            // Object arrays take a Comparator
            Integer[] boxed = {5, 3, 9, 1};
            Arrays.sort(boxed, Comparator.reverseOrder());     // [9, 5, 3, 1]
            Song[] songArray = playlist().toArray(new Song[0]);
            Arrays.sort(songArray, Comparator.comparingInt(Song::plays));   // [Sun, Blue, Rain, Echo]
        }
        {
            int[] range = {9, 7, 5, 3, 1, 0};
            Arrays.sort(range, 1, 4);                          // [9, 3, 5, 7, 1, 0], indexes 1 to 3 sorted

            String[] words = {"fig", "pear", "apple", "kiwi"};
            Arrays.sort(words, 0, 2, Comparator.reverseOrder());   // [pear, fig, apple, kiwi]

            try { Arrays.sort(range, 4, 2);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            try { Arrays.sort(range, 0, 9);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            int[] big = new Random(42).ints(1_000_000, 0, 1000).toArray();
            int[] copy = big.clone();
            Arrays.parallelSort(big);
            Arrays.sort(copy);
            boolean same = Arrays.equals(big, copy);                     // true
            show("same", same);

            String[] words = {"pear", "fig", "apple"};
            Arrays.parallelSort(words, Comparator.reverseOrder());       // [pear, fig, apple]
        }
        {
            List<String> names = new ArrayList<>(List.of("Lokesh", "Alex", "John"));

            // in place
            Collections.sort(names);                             // [Alex, John, Lokesh]
            names.sort(Comparator.reverseOrder());               // [Lokesh, John, Alex]
            names.sort(null);                                    // [Alex, John, Lokesh], natural order

            // new list, the source keeps its order
            List<String> source = new ArrayList<>(List.of("Lokesh", "Alex", "John"));
            List<String> copy = source.stream().sorted().toList();   // [Alex, John, Lokesh]
            show("copy", copy);
            String unchanged = source.toString();                    // [Lokesh, Alex, John]
            show("unchanged", unchanged);
        }
        {
            try { List.of("b", "a").sort(null);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            try { Stream.of("b", "a").toList().sort(null);  } catch (Throwable _t) { System.out.println("-> " + _t); }

            List<String> modifiable = new ArrayList<>(List.of("b", "a"));
            modifiable.sort(null);                               // [a, b]

            List<String> fixedSize = Arrays.asList("b", "a");
            fixedSize.sort(null);                                // [a, b]
            try { fixedSize.add("c");  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Map<String, Integer> ages = Map.of("Lokesh", 37, "John", 40, "Alex", 25);

            // 1. By key: TreeMap sorts the keys
            Map<String, Integer> byKey = new TreeMap<>(ages);              // {Alex=25, John=40, Lokesh=37}
            show("byKey", byKey);

            Map<String, Integer> byKeyDesc = new TreeMap<>(Comparator.reverseOrder());
            byKeyDesc.putAll(ages);                                        // {Lokesh=37, John=40, Alex=25}

            // 2. By value: sort the entries, collect into a LinkedHashMap
            Map<String, Integer> byValue = ages.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (a, b) -> a, LinkedHashMap::new));                     // {Alex=25, Lokesh=37, John=40}

            Map<String, Integer> byValueDesc = ages.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (a, b) -> a, LinkedHashMap::new));                     // {John=40, Lokesh=37, Alex=25}

            // 3. Wrong: the two-argument toMap() returns a HashMap
            Map<String, Integer> lost = ages.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));   // {Alex=25, John=40, Lokesh=37}
        }
        {
            TreeSet<String> tags = new TreeSet<>(List.of("rock", "jazz", "pop", "jazz"));   // [jazz, pop, rock], the duplicate jazz is dropped
            show("tags", tags);
            String first = tags.first();                       // jazz
            show("first", first);
            String last = tags.last();                         // rock
            show("last", last);
            SortedSet<String> before = tags.headSet("pop");    // [jazz], elements before "pop"
            show("before", before);
        }
        {
            List<Song> songs = playlist();
            TreeSet<Song> byArtist = new TreeSet<>(Comparator.comparing(Song::artist));
            byArtist.addAll(songs);                // [Blue, Rain], Echo and Sun were dropped

            TreeSet<Song> byArtistTitle = new TreeSet<>(
            Comparator.comparing(Song::artist).thenComparing(Song::title));
            byArtistTitle.addAll(songs);           // [Blue, Echo, Rain, Sun]
        }
        {
            List<String> names = new ArrayList<>(List.of("bob", "Alice", "alex", "Bea"));
            names.sort(null);                                  // [Alice, Bea, alex, bob]
            names.sort(String.CASE_INSENSITIVE_ORDER);         // [alex, Alice, Bea, bob]
            names.sort(Comparator.comparing(String::length)
                    .thenComparing(String.CASE_INSENSITIVE_ORDER)); // [Bea, bob, alex, Alice]
        }
        {
            List<String> words = new ArrayList<>(List.of("zebra", "\u00e9clair", "eclair", "Apple"));
            words.sort(null);                                  // [Apple, eclair, zebra, \u00e9clair]
            words.sort(Collator.getInstance(Locale.FRENCH));   // [Apple, eclair, \u00e9clair, zebra]

            Collator collator = Collator.getInstance(Locale.ENGLISH);
            collator.setStrength(Collator.PRIMARY);            // ignore case and accents
            int primary = collator.compare("eclair", "\u00c9clair");   // 0, equal
            show("primary", primary);
            int natural = "eclair".compareTo("\u00c9clair");          // -100
            show("natural", natural);
        }
        {
            List<Song> songs = playlist();
            songs.sort(Comparator.comparingInt(Song::plays).reversed());   // [Echo, Rain, Blue, Sun]
            songs.sort(Comparator.comparing(Song::artist));                // [Echo, Blue, Rain, Sun]
        }
        {
            List<Integer> values = new ArrayList<>(List.of(-2_000_000_000, 2_000_000_000, 0));
            values.sort((a, b) -> a - b);           // [0, 2000000000, -2000000000], wrong
            int diff = -2_000_000_000 - 2_000_000_000;   // 294967296, positive instead of negative
            show("diff", diff);
            values.sort(Integer::compare);          // [-2000000000, 0, 2000000000], correct
        }
        {
            List<Song> songs = playlist();
            songs.sort(Comparator.comparing(Song::artist).thenComparing(Song::year)
                    .reversed());                                                   // [Rain, Sun, Echo, Blue], artists Z to A
            songs.sort(Comparator.comparing(Song::artist)
                    .thenComparing(Song::year, Comparator.reverseOrder()));         // [Echo, Blue, Rain, Sun], only years descending
        }
        {
            List<Integer> many = new Random(42).ints(10_000, 0, 100).boxed().collect(Collectors.toCollection(ArrayList::new));
            try { many.sort((a, b) -> a < b ? -1 : 1);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> names = new ArrayList<>(List.of("Lokesh", "Alex", "John"));
            names.sort(Comparator.reverseOrder());                         // [Lokesh, John, Alex]
            List<Song> songs = playlist();
            songs.sort(Comparator.comparingInt(Song::plays).reversed());   // [Echo, Rain, Blue, Sun]
            Integer[] boxed = {5, 3, 9, 1};
            Arrays.sort(boxed, Collections.reverseOrder());                // [9, 5, 3, 1]
        }
        {
            Set<String> tags = new HashSet<>(Set.of("rock", "jazz", "pop"));
            TreeSet<String> sortedSet = new TreeSet<>(tags);              // [jazz, pop, rock]
            show("sortedSet", sortedSet);
            List<String> sortedList = tags.stream().sorted().toList();    // [jazz, pop, rock]
            show("sortedList", sortedList);
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
