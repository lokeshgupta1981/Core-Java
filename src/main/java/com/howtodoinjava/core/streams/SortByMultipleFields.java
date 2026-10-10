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
 * Examples for the tutorial "Java Stream: Sort by Multiple Fields With thenComparing()".
 * https://howtodoinjava.com/java8/sort-stream-multiple-fields/
 */
public class SortByMultipleFields {
    static record Task(String title, int priority, LocalDate due) {}
    static record Ticket(int priority, LocalDate opened) implements Comparable<Ticket> {
        private static final Comparator<Ticket> ORDER =
        Comparator.comparingInt(Ticket::priority).thenComparing(Ticket::opened);

        @Override
        public int compareTo(Ticket other) {
            return ORDER.compare(this, other);
        }
    }
    static Comparator<Task> byColumns(List<String> columns) {
        Map<String, Comparator<Task>> known = Map.of(
        "priority", Comparator.comparingInt(Task::priority),
        "due", Comparator.comparing(Task::due),
        "title", Comparator.comparing(Task::title));
        Comparator<Task> result = (a, b) -> 0;
        for (String column : columns) {
            boolean descending = column.startsWith("-");
            Comparator<Task> next = known.get(descending ? column.substring(1) : column);
            if (next == null) {
                throw new IllegalArgumentException("Unknown sort column: " + column);
            }
            result = result.thenComparing(descending ? next.reversed() : next);
        }
        return result;
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> words = List.of("pear", "fig", "apple", "kiwi", "date");
            Comparator<String> byLengthThenAlpha = Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder());
            List<String> sorted = words.stream().sorted(byLengthThenAlpha).toList();   // [fig, date, kiwi, pear, apple]
            show("sorted", sorted);
        }
        {
            List<Task> tasks = List.of(
            new Task("tests", 2, LocalDate.of(2026, 10, 14)),
            new Task("login", 1, LocalDate.of(2026, 10, 12)),
            new Task("docs", 3, LocalDate.of(2026, 10, 12)),
            new Task("review", 1, LocalDate.of(2026, 10, 11)),
            new Task("deploy", 2, LocalDate.of(2026, 10, 14)));
            Comparator<Task> byPriorityThenDue = Comparator.comparingInt(Task::priority).thenComparing(Task::due);
            Comparator<Task> byAllThree = byPriorityThenDue.thenComparing(Task::title);
            List<String> twoFields = tasks.stream().sorted(byPriorityThenDue).map(Task::title).toList();   // [review, login, tests, deploy, docs]
            show("twoFields", twoFields);
            List<String> threeFields = tasks.stream().sorted(byAllThree).map(Task::title).toList();        // [review, login, deploy, tests, docs]
            show("threeFields", threeFields);
        }
        {
            List<Task> tasks = List.of(
            new Task("tests", 2, LocalDate.of(2026, 10, 14)),
            new Task("login", 1, LocalDate.of(2026, 10, 12)),
            new Task("docs", 3, LocalDate.of(2026, 10, 12)),
            new Task("review", 1, LocalDate.of(2026, 10, 11)),
            new Task("deploy", 2, LocalDate.of(2026, 10, 14)));
            Comparator<Task> allReversed = Comparator.comparingInt(Task::priority).thenComparing(Task::due).reversed();
            Comparator<Task> latestDueFirst = Comparator.comparingInt(Task::priority).thenComparing(Task::due, Comparator.reverseOrder());
            List<String> wrong = tasks.stream().sorted(allReversed).map(Task::title).toList();       // [docs, tests, deploy, login, review]
            show("wrong", wrong);
            List<String> right = tasks.stream().sorted(latestDueFirst).map(Task::title).toList();    // [login, review, tests, deploy, docs]
            show("right", right);
        }
        {
            List<Task> board = Arrays.asList(new Task("tests", 2, null), new Task("docs", 2, LocalDate.of(2026, 10, 12)), new Task("login", 1, null));
            Comparator<Task> unsafe = Comparator.comparingInt(Task::priority).thenComparing(Task::due);
            try { List<Task> crash = board.stream().sorted(unsafe).toList(); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
            Comparator<Task> safe = Comparator.comparingInt(Task::priority).thenComparing(Task::due, Comparator.nullsLast(Comparator.naturalOrder()));
            List<String> titles = board.stream().sorted(safe).map(Task::title).toList();   // [login, docs, tests]
            show("titles", titles);
        }
        {
            List<Ticket> queue = List.of(new Ticket(2, LocalDate.of(2026, 10, 1)), new Ticket(1, LocalDate.of(2026, 10, 5)), new Ticket(1, LocalDate.of(2026, 10, 2)));
            List<Ticket> ordered = queue.stream().sorted().toList();
            LocalDate firstOpened = ordered.getFirst().opened();   // 2026-10-02
            show("firstOpened", firstOpened);
        }
        {
            Comparator<Task> manual = (a, b) -> {
                int result = Integer.compare(a.priority(), b.priority());
                if (result != 0) {
                    return result;
                }
                result = a.due().compareTo(b.due());
                return result != 0 ? result : a.title().compareTo(b.title());
            };
        }
        {
            List<Task> tasks = List.of(
            new Task("tests", 2, LocalDate.of(2026, 10, 14)),
            new Task("login", 1, LocalDate.of(2026, 10, 12)),
            new Task("docs", 3, LocalDate.of(2026, 10, 12)),
            new Task("review", 1, LocalDate.of(2026, 10, 11)),
            new Task("deploy", 2, LocalDate.of(2026, 10, 14)));
            List<String> view = tasks.stream().sorted(byColumns(List.of("priority", "-due", "title"))).map(Task::title).toList();   // [login, review, deploy, tests, docs]
            show("view", view);
            try { Comparator<Task> bad = byColumns(List.of("owner")); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            List<String> names = new ArrayList<>(List.of("bob", "al", "cy", "dan"));
            names.sort(Comparator.comparingInt(String::length).thenComparing(Comparator.reverseOrder()));
            List<String> result = names;   // [cy, al, dan, bob]
            show("result", result);
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
