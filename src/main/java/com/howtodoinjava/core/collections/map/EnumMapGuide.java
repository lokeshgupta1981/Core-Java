package com.howtodoinjava.core.collections.map;

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
 * Examples for the tutorial "Java EnumMap with Examples: Usage, Streams and vs HashMap".
 * https://howtodoinjava.com/java/collections/java-enummap/
 */
public class EnumMapGuide {
    static enum Priority { LOW, MEDIUM, HIGH, CRITICAL }
    static record Ticket(String title, Priority priority) {}
    static Duration responseTime(Ticket ticket) {
        return RESPONSE_TIME.getOrDefault(ticket.priority(), Duration.ofDays(1));
    }

    static final EnumMap<Priority, Duration> RESPONSE_TIME = new EnumMap<>(Map.of(
    Priority.CRITICAL, Duration.ofMinutes(15),
    Priority.HIGH, Duration.ofHours(4),
    Priority.LOW, Duration.ofDays(3)));
    public static void main(String[] args) throws Exception {
        {
            EnumMap<Priority, String> owners = new EnumMap<>(Priority.class);
            owners.put(Priority.HIGH, "alex");
            owners.put(Priority.LOW, "bob");
            owners.put(Priority.CRITICAL, "lokesh");

            String all = owners.toString();                          // "{LOW=bob, HIGH=alex, CRITICAL=lokesh}"
            show("all", all);
            String high = owners.get(Priority.HIGH);                 // "alex"
            show("high", high);
            boolean hasMedium = owners.containsKey(Priority.MEDIUM); // false
            show("hasMedium", hasMedium);
            String removed = owners.remove(Priority.LOW);            // "bob"
            show("removed", removed);
            int size = owners.size();                                // 2
            show("size", size);
        }
        {
            EnumMap<Priority, Integer> empty = new EnumMap<>(Priority.class);                   // {}
            show("empty", empty);
            EnumMap<Priority, Integer> copyOfEnumMap = new EnumMap<>(empty);                    // same key type, can be empty
            show("copyOfEnumMap", copyOfEnumMap);
            EnumMap<Priority, Integer> fromMap = new EnumMap<>(Map.of(Priority.HIGH, 4));       // {HIGH=4}
            show("fromMap", fromMap);
            try { EnumMap<Priority, Integer> fromEmpty = new EnumMap<>(new HashMap<Priority, Integer>()); show("fromEmpty", fromEmpty); } catch (Throwable _t) { System.out.println("fromEmpty -> " + _t); }
        }
        {
            EnumMap<Priority, Integer> openTickets = new EnumMap<>(Priority.class);
            openTickets.merge(Priority.HIGH, 1, Integer::sum);
            openTickets.merge(Priority.LOW, 1, Integer::sum);
            openTickets.merge(Priority.HIGH, 1, Integer::sum);

            int highCount = openTickets.get(Priority.HIGH);                         // 2
            show("highCount", highCount);
            int criticalCount = openTickets.getOrDefault(Priority.CRITICAL, 0);     // 0
            show("criticalCount", criticalCount);
            Set<Priority> keys = openTickets.keySet();                              // [LOW, HIGH]
            show("keys", keys);
            Collection<Integer> counts = openTickets.values();                      // [1, 2]
            show("counts", counts);
            boolean hasTwo = openTickets.containsValue(2);                          // true
            show("hasTwo", hasTwo);
            openTickets.replaceAll((p, n) -> n * 10);
            String scaled = openTickets.toString();                                 // "{LOW=10, HIGH=20}"
            show("scaled", scaled);
        }
        {
            EnumMap<Priority, Integer> zeroed = new EnumMap<>(Priority.class);
            for (Priority p : Priority.values()) {
                zeroed.put(p, 0);
            }
            String allZero = zeroed.toString();                                     // "{LOW=0, MEDIUM=0, HIGH=0, CRITICAL=0}"
            show("allZero", allZero);
        }
        {
            Map<Priority, String> enumBased = new EnumMap<>(Priority.class);
            Map<Priority, String> hashBased = new HashMap<>();
        }
        {
            EnumMap<Priority, String> teams = new EnumMap<>(Priority.class);
            teams.put(Priority.LOW, null);                           // null values are allowed
            String none = teams.get(null);                           // null
            show("none", none);
            boolean hasNull = teams.containsKey(null);               // false
            show("hasNull", hasNull);
            try { String bad = teams.put(null, "alex"); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            EnumMap<Priority, String> ordered = new EnumMap<>(Priority.class);
            ordered.put(Priority.CRITICAL, "lokesh");
            ordered.put(Priority.LOW, "bob");
            Priority firstKey = ordered.keySet().iterator().next();   // LOW
            show("firstKey", firstKey);
        }
        {
            List<Ticket> tickets = List.of(
            new Ticket("Login fails", Priority.CRITICAL),
            new Ticket("Typo on page", Priority.LOW),
            new Ticket("Slow search", Priority.HIGH),
            new Ticket("Broken link", Priority.LOW));

            Map<Priority, Long> perPriority = tickets.stream()
                    .collect(Collectors.groupingBy(Ticket::priority,
            () -> new EnumMap<>(Priority.class), Collectors.counting()));
            String grouped = perPriority.toString();                 // "{LOW=2, HIGH=1, CRITICAL=1}"
            show("grouped", grouped);

            Map<Priority, String> firstTitle = tickets.stream()
                    .collect(Collectors.toMap(Ticket::priority, Ticket::title, (a, b) -> a,
            () -> new EnumMap<>(Priority.class)));
            String titles = firstTitle.toString();                   // "{LOW=Typo on page, HIGH=Slow search, CRITICAL=Login fails}"
            show("titles", titles);
        }
        {
            Duration urgent = responseTime(new Ticket("Login fails", Priority.CRITICAL));   // PT15M
            show("urgent", urgent);
            Duration normal = responseTime(new Ticket("Old browser", Priority.MEDIUM));      // PT24H
            show("normal", normal);
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
