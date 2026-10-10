package com.howtodoinjava.core.collections.list;

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
 * Examples for the tutorial "Remove Duplicates from ArrayList in Java and Keep the Order".
 * https://howtodoinjava.com/java/collections/arraylist/remove-duplicate-elements/
 */
public class RemoveDuplicatesFromList {
    static class Seat {
        final String code;
        Seat(String code) { this.code = code; }
    }
    static record Room(String code) {}
    static record Subscriber(String email, String name) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> tags = new ArrayList<>(List.of("java", "spring", "java", "sql", "spring"));
            List<String> unique = tags.stream().distinct().toList();               // [java, spring, sql]
            show("unique", unique);
            List<String> viaSet = new ArrayList<>(new LinkedHashSet<>(tags));      // [java, spring, sql]
            show("viaSet", viaSet);
            Set<String> seen = new HashSet<>();
            tags.removeIf(tag -> !seen.add(tag));                                  // tags = [java, spring, sql]
        }
        {
            List<Integer> ids = List.of(3, 1, 3, 2, 1);
            List<Integer> uniqueIds = ids.stream().distinct().toList();          // [3, 1, 2]
            show("uniqueIds", uniqueIds);
            ArrayList<Integer> editable = ids.stream()
                    .distinct()
                    .collect(Collectors.toCollection(ArrayList::new));
            editable.add(9);                                                     // editable = [3, 1, 2, 9]
        }
        {
            List<String> tags = List.of("spring", "java", "spring", "sql");
            List<String> ordered = new ArrayList<>(new LinkedHashSet<>(tags));   // [spring, java, sql]
            show("ordered", ordered);
            List<String> sorted = new ArrayList<>(new TreeSet<>(tags));          // [java, spring, sql]
            show("sorted", sorted);
            List<String> anyOrder = new ArrayList<>(new HashSet<>(tags));        // java, spring and sql in hash order
            show("anyOrder", anyOrder);
        }
        {
            List<String> shared = new ArrayList<>(List.of("a", "b", "a"));
            Set<String> distinctSet = new LinkedHashSet<>(shared);
            shared.clear();
            shared.addAll(distinctSet);                                          // shared = [a, b]
        }
        {
            List<Integer> readings = new ArrayList<>(List.of(5, 7, 5, 9, 7, 7));
            Set<Integer> seen = new HashSet<>(readings.size());
            boolean changed = readings.removeIf(r -> !seen.add(r));              // true
            show("changed", changed);
            List<Integer> result = readings;                                     // [5, 7, 9]
            show("result", result);
        }
        {
            List<Seat> seats = List.of(new Seat("A1"), new Seat("A1"));
            long distinctSeats = seats.stream().distinct().count();              // 2
            show("distinctSeats", distinctSeats);
            List<Room> rooms = List.of(new Room("B2"), new Room("B2"));
            long distinctRooms = rooms.stream().distinct().count();              // 1
            show("distinctRooms", distinctRooms);
        }
        {
            List<Subscriber> signups = List.of(
            new Subscriber("ana@mail.com", "Ana"),
            new Subscriber("bo@mail.com", "Bo"),
            new Subscriber("ana@mail.com", "Ana B."));
            Set<String> seenEmails = new HashSet<>();
            List<Subscriber> firstPerEmail = signups.stream().filter(s -> seenEmails.add(s.email())).toList();
            List<String> firstNames = firstPerEmail.stream().map(Subscriber::name).toList();    // [Ana, Bo]
            show("firstNames", firstNames);
        }
        {
            List<Subscriber> signups = List.of(
            new Subscriber("ana@mail.com", "Ana"),
            new Subscriber("bo@mail.com", "Bo"),
            new Subscriber("ana@mail.com", "Ana B."));
            Map<String, Subscriber> byEmail = signups.stream()
                    .collect(Collectors.toMap(Subscriber::email, s -> s, (older, newer) -> newer, LinkedHashMap::new));
            List<String> latestNames = byEmail.values().stream().map(Subscriber::name).toList();    // [Ana B., Bo]
            show("latestNames", latestNames);
        }
        {
            List<String> input = List.of("Java", "spring", "java", "SQL", "Spring");
            Set<String> seenKeys = new HashSet<>();
            List<String> firstSpelling = input.stream()
                    .filter(s -> seenKeys.add(s.toLowerCase(Locale.ROOT)))
                    .toList();
            String kept = firstSpelling.toString();                              // "[Java, spring, SQL]"
            show("kept", kept);
            Set<String> ignoreCase = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
            ignoreCase.addAll(input);
            String sortedKept = ignoreCase.toString();                           // "[Java, spring, SQL]"
            show("sortedKept", sortedKept);
        }
        {
            List<String> tags = List.of("java", "sql", "java");
            List<String> result = new ArrayList<>();
            for (String tag : tags) {
                if (!result.contains(tag)) {
                    result.add(tag);
                }
            }
            String noSet = result.toString();                // "[java, sql]"
            show("noSet", noSet);
        }
        {
            List<String> tags = List.of("java", "sql", "java", "go", "sql");
            Set<String> seen = new HashSet<>();
            Set<String> dupes = tags.stream().filter(t -> !seen.add(t)).collect(Collectors.toCollection(LinkedHashSet::new));
            String found = dupes.toString();                 // "[java, sql]"
            show("found", found);
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
