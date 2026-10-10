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
 * Examples for the tutorial "Java Stream count(): Count Elements, Matches and Groups".
 * https://howtodoinjava.com/java8/stream-count-elements-example/
 */
public class StreamCountExamples {
    static record Ticket(int id, String status, int priority) {}
    static record DashboardCounts(long open, long urgentOpen, Map<Integer, Long> openByPriority) {}
    static DashboardCounts dashboard(List<Ticket> tickets) {
        List<Ticket> open = tickets.stream()
                .filter(t -> t.status().equals("open"))
                .toList();
        long urgent = open.stream().filter(t -> t.priority() == 1).count();
        Map<Integer, Long> byPriority = open.stream()
                .collect(Collectors.groupingBy(Ticket::priority, TreeMap::new, Collectors.counting()));
        return new DashboardCounts(open.size(), urgent, byPriority);
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> statuses = List.of("open", "closed", "open", "pending", "open");
            long total = statuses.stream().count();                                     // 5
            show("total", total);
            long open = statuses.stream().filter("open"::equals).count();               // 3
            show("open", open);
            long kinds = statuses.stream().distinct().count();                          // 3
            show("kinds", kinds);
            long evens = IntStream.rangeClosed(1, 10).filter(n -> n % 2 == 0).count();  // 5
            show("evens", evens);
        }
        {
            Stream<String> names = Stream.of("ana", "raj");
            long first = names.count();    // 2
            show("first", first);
            try { long second = names.count(); show("second", second); } catch (Throwable _t) { System.out.println("second -> " + _t); }
        }
        {
            List<Ticket> tickets = List.of(new Ticket(1, "open", 1), new Ticket(2, "closed", 2), new Ticket(3, "open", 3), new Ticket(4, "pending", 1), new Ticket(5, "open", 1));
            long openTickets = tickets.stream().filter(t -> t.status().equals("open")).count();                        // 3
            show("openTickets", openTickets);
            long urgentOpen = tickets.stream().filter(t -> t.status().equals("open") && t.priority() == 1).count();     // 2
            show("urgentOpen", urgentOpen);
            long notClosed = tickets.stream().map(Ticket::status).filter(Predicate.not("closed"::equals)).count();      // 4
            show("notClosed", notClosed);
        }
        {
            long vowels = "stream count".chars().filter(c -> "aeiou".indexOf(c) >= 0).count();          // 4
            show("vowels", vowels);
            long bigOrders = Arrays.stream(new int[] {120, 45, 300, 80}).filter(v -> v >= 100).count();   // 2
            show("bigOrders", bigOrders);
            Map<String, Integer> stock = Map.of("apple", 0, "banana", 7, "cherry", 0);
            long soldOut = stock.values().stream().filter(qty -> qty == 0).count();                        // 2
            show("soldOut", soldOut);
        }
        {
            List<String> seenByPeek = new ArrayList<>();
            long sized = List.of("a", "b", "c").stream().peek(seenByPeek::add).count();                     // 3
            show("sized", sized);
            int peeked = seenByPeek.size();                                                                // 0
            show("peeked", peeked);
            List<String> seenWithFilter = new ArrayList<>();
            long filtered = List.of("a", "b", "c").stream().peek(seenWithFilter::add).filter(s -> !s.isEmpty()).count();   // 3
            show("filtered", filtered);
            int peekedWithFilter = seenWithFilter.size();                                                  // 3
            show("peekedWithFilter", peekedWithFilter);
        }
        {
            List<Ticket> tickets = List.of(new Ticket(1, "open", 1), new Ticket(2, "closed", 2), new Ticket(3, "open", 3), new Ticket(4, "pending", 1), new Ticket(5, "open", 1));
            Map<String, Long> perStatus = tickets.stream().collect(Collectors.groupingBy(Ticket::status, TreeMap::new, Collectors.counting()));   // {closed=1, open=3, pending=1}
            show("perStatus", perStatus);
            int listSize = tickets.size();   // 5
            show("listSize", listSize);
        }
        {
            List<Ticket> tickets = List.of(new Ticket(1, "open", 1), new Ticket(2, "closed", 2), new Ticket(3, "open", 3), new Ticket(4, "pending", 1), new Ticket(5, "open", 1));
            DashboardCounts counts = dashboard(tickets);
            long openCount = counts.open();                           // 3
            show("openCount", openCount);
            long urgentCount = counts.urgentOpen();                   // 2
            show("urgentCount", urgentCount);
            Map<Integer, Long> breakdown = counts.openByPriority();   // {1=2, 3=1}
            show("breakdown", breakdown);
        }
        {
            long matches = Stream.of("a", "bb", "ccc").filter(s -> s.length() > 1).count();
            int asInt = Math.toIntExact(matches);   // 2
            show("asInt", asInt);
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
