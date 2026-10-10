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
 * Examples for the tutorial "Java Stream filter(): Keep Matching Elements with Examples".
 * https://howtodoinjava.com/java8/java-stream-filter-example/
 */
public class StreamFilterExamples {
    static record Task(String title, boolean done, int priority) {
        boolean isUrgent() {
            return !done && priority >= 3;
        }
    }
    static boolean hasContent(Path file) {
        try {
            return Files.size(file) > 0;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    static record Todo(String title, LocalDate due, boolean done, int priority) {}
    static List<String> today(List<Todo> todos, LocalDate date) {
        return todos.stream()
                .filter(t -> !t.done())
                .filter(t -> !t.due().isAfter(date))
                .sorted(Comparator.comparingInt(Todo::priority).reversed())
                .map(Todo::title)
                .toList();
    }
    public static void main(String[] args) throws Exception {
        {
            List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);

            List<Integer> evens = numbers.stream().filter(n -> n % 2 == 0).toList();                     // [2, 4, 6]
            show("evens", evens);
            long bigOnes = numbers.stream().filter(n -> n > 4).count();                                  // 2
            show("bigOnes", bigOnes);
            Optional<Integer> firstOver3 = numbers.stream().filter(n -> n > 3).findFirst();               // Optional[4]
            show("firstOver3", firstOver3);
            List<String> words = Stream.of("tea", "", "jam").filter(Predicate.not(String::isEmpty)).toList();   // [tea, jam]
            show("words", words);
        }
        {
            Predicate<String> isLong = s -> s.length() > 3;
            List<String> fruits = List.of("fig", "apple", "", "banana");

            List<String> withLambda = fruits.stream().filter(s -> s.length() > 3).toList();          // [apple, banana]
            show("withLambda", withLambda);
            List<String> withVariable = fruits.stream().filter(isLong).toList();                     // [apple, banana]
            show("withVariable", withVariable);
            List<String> withReference = fruits.stream().filter(String::isEmpty).toList();           // []
            show("withReference", withReference);
            List<String> withNot = fruits.stream().filter(Predicate.not(String::isEmpty)).toList();  // [fig, apple, banana]
            show("withNot", withNot);
        }
        {
            List<String> trace = new ArrayList<>();
            Optional<Integer> first = Stream.of(1, 2, 3, 4)
                    .filter(n -> {
                trace.add("filter " + n);
                return n % 2 == 0;
            })
                    .map(n -> {
                trace.add("map " + n);
                return n * 10;
            })
                    .findFirst();
            Optional<Integer> result = first;              // Optional[20]
            show("result", result);
            String steps = String.join(", ", trace);       // "filter 1, filter 2, map 2"
            show("steps", steps);
        }
        {
            List<Task> tasks = List.of(
            new Task("Pay rent", false, 3),
            new Task("Buy milk", true, 1),
            new Task("Call mom", false, 2),
            new Task("Fix bike", false, 3));

            List<String> open = tasks.stream().filter(t -> !t.done()).map(Task::title).toList();      // [Pay rent, Call mom, Fix bike]
            show("open", open);
            List<String> urgent = tasks.stream().filter(Task::isUrgent).map(Task::title).toList();    // [Pay rent, Fix bike]
            show("urgent", urgent);
            long doneCount = tasks.stream().filter(Task::done).count();                              // 1
            show("doneCount", doneCount);
        }
        {
            List<Task> tasks = List.of(new Task("Pay rent", false, 3), new Task("Buy milk", true, 3), new Task("Call mom", false, 2));

            List<String> oneFilter = tasks.stream().filter(t -> !t.done() && t.priority() == 3).map(Task::title).toList();        // [Pay rent]
            show("oneFilter", oneFilter);
            List<String> twoFilters = tasks.stream().filter(t -> !t.done()).filter(t -> t.priority() == 3).map(Task::title).toList();   // [Pay rent]
            show("twoFilters", twoFilters);
        }
        {
            List<Integer> scores = List.of(72, 95, 38, 88, 95);

            List<Integer> passed = scores.stream().filter(s -> s >= 50).toList();                   // [72, 95, 88, 95]
            show("passed", passed);
            Set<Integer> distinctTop = scores.stream().filter(s -> s > 90).collect(Collectors.toSet());   // [95]
            show("distinctTop", distinctTop);
            int passedTotal = scores.stream().filter(s -> s >= 50).mapToInt(Integer::intValue).sum();     // 350
            show("passedTotal", passedTotal);
            boolean anyFailed = scores.stream().anyMatch(s -> s < 50);                              // true
            show("anyFailed", anyFailed);
        }
        {
            List<String> emails = Arrays.asList("ana@mail.com", null, "  ", "raj@mail.com", "");

            List<String> clean = emails.stream()
                    .filter(Objects::nonNull)
                    .filter(Predicate.not(String::isBlank))
                    .map(String::strip)
                    .toList();                              // [ana@mail.com, raj@mail.com]
        }
        {
            Map<String, Integer> stock = new TreeMap<>(Map.of("apple", 5, "banana", 0, "kiwi", 12));

            Map<String, Integer> available = stock.entrySet().stream()
                    .filter(e -> e.getValue() > 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, TreeMap::new));   // {apple=5, kiwi=12}
        }
        {
            List<Integer> readings = new ArrayList<>(List.of(1, 2, 7, 3, 9));

            List<Integer> small = readings.stream().filter(n -> n < 5).toList();         // [1, 2, 3]
            show("small", small);
            List<Integer> prefix = readings.stream().takeWhile(n -> n < 5).toList();     // [1, 2]
            show("prefix", prefix);
            boolean changed = readings.removeIf(n -> n >= 5);                             // true
            show("changed", changed);
            List<Integer> afterRemove = readings;                                         // [1, 2, 3]
            show("afterRemove", afterRemove);
        }
        {
            Path dir = Files.createTempDirectory("uploads");
            Path empty = Files.createFile(dir.resolve("empty.txt"));
            Path notes = Files.writeString(dir.resolve("notes.txt"), "milk");
            List<String> nonEmpty = Stream.of(empty, notes).filter(p -> hasContent(p)).map(p -> p.getFileName().toString()).toList();   // [notes.txt]
            show("nonEmpty", nonEmpty);
        }
        {
            LocalDate day = LocalDate.of(2026, 10, 10);
            List<Todo> todos = List.of(
            new Todo("Pay rent", LocalDate.of(2026, 10, 1), false, 3),
            new Todo("Buy milk", day, true, 1),
            new Todo("Call mom", day, false, 2),
            new Todo("Book flight", LocalDate.of(2026, 10, 20), false, 3));

            List<String> screen = today(todos, day);       // [Pay rent, Call mom]
            show("screen", screen);
        }
        {
            Set<String> blocked = new HashSet<>(List.of("spam@mail.com"));
            List<String> allowed = Stream.of("ana@mail.com", "spam@mail.com").filter(e -> !blocked.contains(e)).toList();   // [ana@mail.com]
            show("allowed", allowed);
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
