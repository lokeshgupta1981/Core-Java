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
 * Examples for the tutorial "Java Nested Map: Create, Update and Iterate a Map of Maps".
 * https://howtodoinjava.com/java/collections/hashmap/java-nested-map/
 */
public class NestedMapExamples {
    static record Mark(String student, String subject, int score) {}
    static record StudentSubject(String student, String subject) {}
    public static void main(String[] args) throws Exception {
        {
            Map<String, Map<String, Integer>> marks = new TreeMap<>();
            marks.computeIfAbsent("lokesh", k -> new TreeMap<>()).put("math", 90);
            marks.computeIfAbsent("lokesh", k -> new TreeMap<>()).put("art", 75);
            marks.computeIfAbsent("alex", k -> new TreeMap<>()).put("math", 82);

            String all = marks.toString();                     // "{alex={math=82}, lokesh={art=75, math=90}}"
            show("all", all);
            int math = marks.get("lokesh").get("math");        // 90
            show("math", math);
            int subjects = marks.get("lokesh").size();         // 2
            show("subjects", subjects);
            int music = marks.getOrDefault("bob", Map.of()).getOrDefault("music", 0);   // 0
            show("music", music);
        }
        {
            Map<String, Map<String, String>> messages = Map.of(
            "en", Map.of("greeting", "Hello", "bye", "Goodbye"),
            "de", Map.of("greeting", "Hallo"));

            String german = messages.get("de").get("greeting");                                // "Hallo"
            show("german", german);
            String fallback = messages.get("de").getOrDefault("bye", messages.get("en").get("bye"));   // "Goodbye"
            show("fallback", fallback);
        }
        {
            Map<String, Integer> lokeshMarks = new TreeMap<>();
            lokeshMarks.put("math", 90);
            lokeshMarks.put("art", 75);

            Map<String, Map<String, Integer>> byPut = new TreeMap<>();
            byPut.put("lokesh", lokeshMarks);
            String created = byPut.toString();                 // "{lokesh={art=75, math=90}}"
            show("created", created);

            Map<String, Map<String, Integer>> fixed = Map.of("alex", Map.of("math", 82));
            int alexMath = fixed.get("alex").get("math");      // 82
            show("alexMath", alexMath);
        }
        {
            List<Mark> rows = List.of(
            new Mark("lokesh", "math", 90),
            new Mark("lokesh", "art", 75),
            new Mark("alex", "math", 82),
            new Mark("lokesh", "math", 95));

            Map<String, Map<String, Integer>> fromRows = rows.stream()
                    .collect(Collectors.groupingBy(Mark::student, TreeMap::new,
            Collectors.toMap(Mark::subject, Mark::score, Integer::max, TreeMap::new)));

            String grouped = fromRows.toString();              // "{alex={math=82}, lokesh={art=75, math=95}}"
            show("grouped", grouped);
        }
        {
            Map<String, Map<String, Integer>> book = new TreeMap<>();
            try { book.get("bob").put("math", 60);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Map<String, Map<String, Integer>> gradebook = new TreeMap<>();
            gradebook.computeIfAbsent("bob", k -> new TreeMap<>()).put("math", 60);
            gradebook.computeIfAbsent("bob", k -> new TreeMap<>()).merge("math", 5, Integer::sum);
            int bobMath = gradebook.get("bob").get("math");    // 65
            show("bobMath", bobMath);
        }
        {
            Map<String, Map<String, Integer>> scores = new TreeMap<>();
            scores.computeIfAbsent("alex", k -> new TreeMap<>()).put("math", 82);
            scores.computeIfAbsent("lokesh", k -> new TreeMap<>()).put("art", 75);

            scores.computeIfPresent("alex", (student, inner) -> {
                inner.remove("math");
                return inner.isEmpty() ? null : inner;
            });
            String left = scores.toString();                   // "{lokesh={art=75}}"
            show("left", left);
            Map<String, Integer> removedAll = scores.remove("lokesh");   // {art=75}
            show("removedAll", removedAll);
        }
        {
            Map<String, Map<String, Integer>> report = new TreeMap<>(Map.of(
            "alex", new TreeMap<>(Map.of("math", 82)),
            "lokesh", new TreeMap<>(Map.of("art", 75, "math", 90))));

            List<String> lines = new ArrayList<>();
            report.forEach((student, subjectMarks) ->
            subjectMarks.forEach((subject, score) -> lines.add(student + "." + subject + "=" + score)));
            String printed = lines.toString();                 // "[alex.math=82, lokesh.art=75, lokesh.math=90]"
            show("printed", printed);

            Map<String, Integer> bestPerStudent = new TreeMap<>();
            for (Map.Entry<String, Map<String, Integer>> entry : report.entrySet()) {
                bestPerStudent.put(entry.getKey(), Collections.max(entry.getValue().values()));
            }
            String bests = bestPerStudent.toString();          // "{alex=82, lokesh=90}"
            show("bests", bests);

            double average = report.values().stream()
                    .flatMap(inner -> inner.values().stream())
                    .mapToInt(Integer::intValue)
                    .average().orElse(0);
            double avg = average;                              // 82.33333333333333
            show("avg", avg);
        }
        {
            Map<String, Map<String, Integer>> data = Map.of("alex", Map.of("math", 82));
            int art = data.getOrDefault("alex", Map.of()).getOrDefault("art", 0);       // 0
            show("art", art);
            Optional<Integer> bob = Optional.ofNullable(data.get("bob")).map(m -> m.get("math"));   // Optional.empty
            show("bob", bob);

            Map<String, Object> payload = Map.of("user", Map.of("name", "lokesh", "age", 37));
            String name = payload.get("user") instanceof Map<?, ?> user
                    && user.get("name") instanceof String s ? s : "unknown";
            String userName = name;                                                    // "lokesh"
            show("userName", userName);
        }
        {
            Map<String, Map<String, Integer>> original = new TreeMap<>();
            original.put("alex", new TreeMap<>(Map.of("math", 82)));

            Map<String, Map<String, Integer>> shallow = new TreeMap<>(original);
            shallow.get("alex").put("math", 0);
            int changed = original.get("alex").get("math");    // 0, the inner map is shared
            show("changed", changed);

            Map<String, Map<String, Integer>> deep = new TreeMap<>();
            original.forEach((k, v) -> deep.put(k, new TreeMap<>(v)));
            deep.get("alex").put("math", 99);
            int unchanged = original.get("alex").get("math");  // 0, deep copy has its own inner map
            show("unchanged", unchanged);
        }
        {
            Map<String, Map<String, Integer>> source = Map.of("alex", new TreeMap<>(Map.of("math", 82)));
            Map<String, Map<String, Integer>> readOnly = source.entrySet().stream()
                    .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, e -> Map.copyOf(e.getValue())));
            try { Integer blocked = readOnly.get("alex").put("math", 1); show("blocked", blocked); } catch (Throwable _t) { System.out.println("blocked -> " + _t); }
        }
        {
            Map<String, Map<String, Integer>> nested = new TreeMap<>(Map.of(
            "alex", Map.of("math", 82),
            "lokesh", Map.of("math", 90)));

            Map<String, Integer> flat = nested.entrySet().stream()
                    .flatMap(outer -> outer.getValue().entrySet().stream()
                    .map(inner -> Map.entry(outer.getKey() + "." + inner.getKey(), inner.getValue())))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, TreeMap::new));

            String flatText = flat.toString();                 // "{alex.math=82, lokesh.math=90}"
            show("flatText", flatText);
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
