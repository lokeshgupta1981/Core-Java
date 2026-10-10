package com.howtodoinjava.core.collections.set;

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
 * Examples for the tutorial "Java TreeSet: Sorted Set, Comparators and Navigation".
 * https://howtodoinjava.com/java/collections/java-treeset-class/
 */
public class TreeSetExamples {
    static record Player(String name, int score) {}
    static Optional<LocalTime> book(TreeSet<LocalTime> free, LocalTime wanted) {
        LocalTime slot = free.ceiling(wanted);
        if (slot != null) {
            free.remove(slot);
        }
        return Optional.ofNullable(slot);
    }
    public static void main(String[] args) throws Exception {
        {
            TreeSet<Integer> scores = new TreeSet<>(List.of(70, 45, 90, 45, 82));
            Set<Integer> sorted = scores;                        // [45, 70, 82, 90], duplicate dropped
            show("sorted", sorted);
            Integer lowest = scores.first();                     // 45
            show("lowest", lowest);
            Integer highest = scores.last();                     // 90
            show("highest", highest);
            Integer atOrBelow = scores.floor(80);                // 70
            show("atOrBelow", atOrBelow);
            Integer atOrAbove = scores.ceiling(80);              // 82
            show("atOrAbove", atOrAbove);
            Integer above = scores.higher(90);                   // null, nothing above 90
            show("above", above);
            SortedSet<Integer> under80 = scores.headSet(80);     // [45, 70]
            show("under80", under80);
            NavigableSet<Integer> desc = scores.descendingSet(); // [90, 82, 70, 45]
            show("desc", desc);
        }
        {
            TreeSet<String> names = new TreeSet<>(List.of("raj", "Ana", "li"));
            Set<String> natural = names;                         // [Ana, li, raj], uppercase sorts first
            show("natural", natural);
            TreeSet<Player> broken = new TreeSet<>();
            try { broken.add(new Player("ana", 50));  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            TreeSet<String> reversed = new TreeSet<>(Comparator.reverseOrder());
            reversed.addAll(List.of("b", "c", "a"));
            Set<String> zToA = reversed;                         // [c, b, a]
            show("zToA", zToA);
            TreeSet<String> ignoreCase = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
            ignoreCase.addAll(List.of("raj", "Ana", "li"));
            Set<String> alpha = ignoreCase;                      // [Ana, li, raj]
            show("alpha", alpha);
            TreeSet<Player> board = new TreeSet<>(Comparator.comparingInt(Player::score).reversed()
                    .thenComparing(Player::name));
            board.addAll(List.of(new Player("ana", 50), new Player("raj", 80), new Player("li", 50)));
            String leader = board.first().name();                // "raj"
            show("leader", leader);
            String lastPlace = board.last().name();              // "li"
            show("lastPlace", lastPlace);
        }
        {
            TreeSet<String> byLength = new TreeSet<>(Comparator.comparingInt(String::length));
            boolean addedCat = byLength.add("cat");              // true
            show("addedCat", addedCat);
            boolean addedDog = byLength.add("dog");              // false, same length as cat
            show("addedDog", addedDog);
            TreeSet<Player> byScore = new TreeSet<>(Comparator.comparingInt(Player::score));
            byScore.add(new Player("ana", 50));
            byScore.add(new Player("li", 50));
            int players = byScore.size();                        // 1, li was dropped
            show("players", players);
        }
        {
            TreeSet<LocalTime> free = new TreeSet<>(List.of(
            LocalTime.of(9, 0), LocalTime.of(10, 30), LocalTime.of(11, 0)));
            Optional<LocalTime> booked = book(free, LocalTime.of(10, 20));   // Optional[10:30]
            show("booked", booked);
            Optional<LocalTime> late = book(free, LocalTime.of(11, 30));     // Optional.empty
            show("late", late);
            Set<LocalTime> remaining = free;                                 // [09:00, 11:00]
            show("remaining", remaining);
        }
        {
            TreeSet<Integer> ages = new TreeSet<>(List.of(12, 18, 25, 33, 41, 67));
            SortedSet<Integer> minors = ages.headSet(18);                    // [12]
            show("minors", minors);
            SortedSet<Integer> adults = ages.tailSet(18);                    // [18, 25, 33, 41, 67]
            show("adults", adults);
            SortedSet<Integer> twenties = ages.subSet(20, 30);               // [25]
            show("twenties", twenties);
            NavigableSet<Integer> upTo41 = ages.headSet(41, true);           // [12, 18, 25, 33, 41]
            show("upTo41", upTo41);
            NavigableSet<Integer> working = ages.subSet(18, true, 67, false); // [18, 25, 33, 41]
            show("working", working);
        }
        {
            TreeSet<Integer> levels = new TreeSet<>(List.of(1, 5, 9));
            SortedSet<Integer> low = levels.headSet(5);
            levels.add(3);
            SortedSet<Integer> lowNow = low;                                 // [1, 3]
            show("lowNow", lowNow);
            try { low.add(7);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            TreeSet<String> versions = new TreeSet<>(List.of("1.2", "1.10", "1.0"));
            String oldest = versions.getFirst();                 // "1.0"
            show("oldest", oldest);
            String newest = versions.getLast();                  // "1.2", string order, not numeric
            show("newest", newest);
            SequencedSet<String> desc = versions.reversed();     // [1.2, 1.10, 1.0]
            show("desc", desc);
            try { versions.addFirst("0.9");  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            TreeSet<String> plain = new TreeSet<>();
            try { plain.add(null);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            TreeSet<String> nullable = new TreeSet<>(Comparator.nullsFirst(Comparator.naturalOrder()));
            nullable.addAll(Arrays.asList("b", null, "a"));
            String firstValue = nullable.first();                // null
            show("firstValue", firstValue);
            Set<String> withNull = nullable;                     // [null, a, b]
            show("withNull", withNull);
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
