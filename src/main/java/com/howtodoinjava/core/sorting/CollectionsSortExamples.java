package com.howtodoinjava.core.sorting;

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
 * Examples for the tutorial "Java Collections.sort(): Natural Order, Comparator, Stability".
 * https://howtodoinjava.com/java/sort/collections-sort/
 */
public class CollectionsSortExamples {
    static record Player(String name, int score) implements Comparable<Player> {

        @Override
        public int compareTo(Player other) {
            return name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return name + "=" + score;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> cities = new ArrayList<>(List.of("Pune", "Delhi", "Agra", "Mumbai"));
            Collections.sort(cities);
            List<String> ascending = List.copyOf(cities);                       // [Agra, Delhi, Mumbai, Pune]
            show("ascending", ascending);
            Collections.sort(cities, Collections.reverseOrder());
            List<String> descending = List.copyOf(cities);                      // [Pune, Mumbai, Delhi, Agra]
            show("descending", descending);
            Collections.sort(cities, Comparator.comparing(String::length));
            List<String> byLength = List.copyOf(cities);                        // [Pune, Agra, Delhi, Mumbai]
            show("byLength", byLength);

            try { Collections.sort(List.of("Pune", "Agra"));  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<Integer> scores = new ArrayList<>(List.of(70, 95, 40));
            Collections.sort(scores);
            List<Integer> sortedScores = List.copyOf(scores);         // [40, 70, 95]
            show("sortedScores", sortedScores);

            List<LocalDate> dates = new ArrayList<>(List.of(LocalDate.of(2026, 5, 1), LocalDate.of(2025, 12, 24)));
            Collections.sort(dates);
            List<LocalDate> sortedDates = List.copyOf(dates);         // [2025-12-24, 2026-05-01]
            show("sortedDates", sortedDates);
        }
        {
            List<Player> players = new ArrayList<>(List.of(new Player("mia", 80), new Player("alex", 95), new Player("lee", 80)));
            Collections.sort(players);
            List<Player> byName = List.copyOf(players);               // [alex=95, lee=80, mia=80]
            show("byName", byName);
        }
        {
            List<Player> players = new ArrayList<>(List.of(new Player("mia", 80), new Player("alex", 95), new Player("lee", 80)));
            Comparator<Player> leaderboard = Comparator.comparingInt(Player::score).reversed().thenComparing(Player::name);
            Collections.sort(players, leaderboard);
            List<Player> ranking = List.copyOf(players);              // [alex=95, lee=80, mia=80]
            show("ranking", ranking);
        }
        {
            List<Player> players = new ArrayList<>(List.of(new Player("mia", 80), new Player("alex", 95), new Player("lee", 80)));
            Collections.sort(players, Collections.reverseOrder());
            List<Player> nameDescending = List.copyOf(players);       // [mia=80, lee=80, alex=95]
            show("nameDescending", nameDescending);
            Collections.sort(players, Collections.reverseOrder(Comparator.comparingInt(Player::score)));
            List<Player> scoreDescending = List.copyOf(players);      // [alex=95, mia=80, lee=80]
            show("scoreDescending", scoreDescending);
        }
        {
            List<Player> players = new ArrayList<>(List.of(new Player("mia", 80), new Player("alex", 95), new Player("lee", 80)));
            Collections.sort(players);
            Collections.sort(players, Comparator.comparingInt(Player::score));
            List<Player> byScoreThenName = List.copyOf(players);      // [lee=80, mia=80, alex=95]
            show("byScoreThenName", byScoreThenName);
        }
        {
            String[] array = {"Pune", "Agra", "Delhi"};
            List<String> view = Arrays.asList(array);
            Collections.sort(view);
            String firstInArray = array[0];                           // "Agra"
            show("firstInArray", firstInArray);

            List<String> fromStream = Stream.of("Pune", "Agra").toList();
            try { Collections.sort(fromStream);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            List<String> copy = new ArrayList<>(fromStream);
            Collections.sort(copy);
            List<String> sortedCopy = copy;                           // [Agra, Pune]
            show("sortedCopy", sortedCopy);
        }
        {
            List<Integer> laps = new ArrayList<>(List.of(9, 7, 5, 3, 1));
            Collections.sort(laps.subList(1, 4));
            List<Integer> middleSorted = List.copyOf(laps);           // [9, 3, 5, 7, 1]
            show("middleSorted", middleSorted);
        }
        {
            List<String> cities = new ArrayList<>(List.of("Pune", "Agra", "Delhi"));
            List<String> sortedCopy = cities.stream().sorted().toList();   // [Agra, Delhi, Pune]
            show("sortedCopy", sortedCopy);
            List<String> unchanged = List.copyOf(cities);                  // [Pune, Agra, Delhi]
            show("unchanged", unchanged);
            cities.sort(null);
            List<String> sortedInPlace = cities;                           // [Agra, Delhi, Pune]
            show("sortedInPlace", sortedInPlace);
        }
        {
            List<String> cities = new ArrayList<>(List.of("Pune", "Delhi", "Agra", "Mumbai"));
            Collections.sort(cities);
            int found = Collections.binarySearch(cities, "Mumbai");   // 2
            show("found", found);
            int missing = Collections.binarySearch(cities, "Goa");    // -3
            show("missing", missing);
        }
        {
            Object[] mixed = {"Agra", 42};
            try { Arrays.sort(mixed);  } catch (Throwable _t) { System.out.println("-> " + _t); }
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
