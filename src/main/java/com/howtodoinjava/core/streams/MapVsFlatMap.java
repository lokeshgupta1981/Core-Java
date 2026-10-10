package com.howtodoinjava.core.streams;

import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
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
 * Examples for the tutorial "map() vs flatMap() in Java Streams: Difference with Examples".
 * https://howtodoinjava.com/java8/stream-map-vs-flatmap/
 */
public class MapVsFlatMap {
    static record Trip(String name, List<String> cities) {}
    static Optional<Trip> findTrip(List<Trip> trips, String name) {
        return trips.stream().filter(t -> t.name().equals(name)).findFirst();
    }
    static Optional<String> firstCity(Trip trip) {
        return trip.cities().stream().findFirst();
    }
    public static void main(String[] args) throws Exception {
        {
            List<List<String>> trips = List.of(List.of("Rome", "Paris"), List.of("Oslo"));

            List<Integer> stops = trips.stream().map(List::size).toList();                // [2, 1]
            show("stops", stops);
            List<Stream<String>> streams = trips.stream().map(List::stream).toList();     // two Stream objects, not cities
            show("streams", streams);
            List<String> cities = trips.stream().flatMap(List::stream).toList();          // [Rome, Paris, Oslo]
            show("cities", cities);
        }
        {
            List<Trip> trips = List.of(
            new Trip("Spring break", List.of("Rome", "Paris")),
            new Trip("Fjords", List.of("Oslo", "Bergen", "Oslo")),
            new Trip("Staycation", List.of()));

            List<String> names = trips.stream().map(Trip::name).toList();                  // [Spring break, Fjords, Staycation]
            show("names", names);
            List<Integer> counts = trips.stream().map(t -> t.cities().size()).toList();    // [2, 3, 0]
            show("counts", counts);
        }
        {
            List<Trip> trips = List.of(
            new Trip("Spring break", List.of("Rome", "Paris")),
            new Trip("Fjords", List.of("Oslo", "Bergen", "Oslo")),
            new Trip("Staycation", List.of()));

            List<String> visited = trips.stream().flatMap(t -> t.cities().stream()).toList();               // [Rome, Paris, Oslo, Bergen, Oslo]
            show("visited", visited);
            List<String> unique = trips.stream().flatMap(t -> t.cities().stream()).distinct().toList();     // [Rome, Paris, Oslo, Bergen]
            show("unique", unique);
        }
        {
            List<Trip> trips = List.of(new Trip("Spring break", List.of("Rome", "Paris")), new Trip("City trip", List.of("Rome")));

            List<List<String>> nested = trips.stream().map(Trip::cities).toList();                        // [[Rome, Paris], [Rome]]
            show("nested", nested);
            boolean hasRome = trips.stream().map(Trip::cities).anyMatch(c -> c.equals("Rome"));            // false
            show("hasRome", hasRome);
            boolean hasRomeFlat = trips.stream().flatMap(t -> t.cities().stream()).anyMatch(c -> c.equals("Rome"));   // true
            show("hasRomeFlat", hasRomeFlat);
        }
        {
            List<Trip> trips = List.of(new Trip("Fjords", List.of("Oslo", "Bergen")), new Trip("Staycation", List.of()));

            Optional<Optional<String>> nested = findTrip(trips, "Fjords").map(t -> firstCity(t));            // Optional[Optional[Oslo]]
            show("nested", nested);
            Optional<String> city = findTrip(trips, "Fjords").flatMap(t -> firstCity(t));                    // Optional[Oslo]
            show("city", city);
            String none = findTrip(trips, "Staycation").flatMap(t -> firstCity(t)).orElse("no cities");      // "no cities"
            show("none", none);
        }
        {
            List<Integer> evensTwice = Stream.of(1, 2, 3, 4)
                    .<Integer>mapMulti((n, out) -> {
                if (n % 2 == 0) {
                    out.accept(n);
                    out.accept(n);
                }
            })
                    .toList();                              // [2, 2, 4, 4]
        }
        {
            List<Trip> trips = List.of(
            new Trip("Spring break", List.of("Rome", "Paris")),
            new Trip("Fjords", List.of("Oslo", "Bergen", "Oslo")),
            new Trip("City trip", List.of("Rome", "Milan")));

            List<String> labels = trips.stream()
                    .map(t -> t.name() + " (" + t.cities().size() + " stops)")
                    .toList();                              // [Spring break (2 stops), Fjords (3 stops), City trip (2 stops)]
            long citiesVisited = trips.stream()
                    .flatMap(t -> t.cities().stream())
                    .distinct()
                    .count();                               // 5
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
