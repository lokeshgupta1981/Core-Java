package com.howtodoinjava.java25.streams;

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

import java.time.format.*;

/**
 * Examples for the tutorial "Java forEach(): Iterate a List, Set or Map With Examples".
 * https://howtodoinjava.com/java8/foreach-method-example/
 */
public class ForEachOnIterableAndMapJava25 {

    public static void main(String[] args) throws Exception {
        {
            List<String> fruits = List.of("apple", "banana", "cherry");
            fruits.forEach(System.out::println);                                       // apple, banana, cherry (one per line)
            Map<String, Integer> stock = new TreeMap<>(Map.of("apple", 5, "banana", 3));
            stock.forEach((item, qty) -> System.out.println(item + "=" + qty));        // apple=5, banana=3 (one per line)
        }
        {
            List<String> names = new ArrayList<>(List.of("Lokesh", "Alex", "Brian"));
            StringBuilder greeting = new StringBuilder();
            names.forEach(name -> greeting.append("Hi ").append(name).append(". "));
            String text = greeting.toString().strip();                 // "Hi Lokesh. Hi Alex. Hi Brian."
            show("text", text);
            Consumer<String> noAction = null;
            try { names.forEach(noAction);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> steps = List.of("wash", "cut", "cook");
            List<String> backwards = new ArrayList<>();
            steps.reversed().forEach(backwards::add);
            List<String> reversedSteps = backwards;                    // [cook, cut, wash]
            show("reversedSteps", reversedSteps);
            Set<String> tags = new TreeSet<>(Set.of("java", "api", "loop"));
            List<String> sortedTags = new ArrayList<>();
            tags.forEach(sortedTags::add);
            List<String> tagOrder = sortedTags;                        // [api, java, loop]
            show("tagOrder", tagOrder);
        }
        {
            Map<String, Integer> ages = new LinkedHashMap<>();
            ages.put("Lokesh", 37);
            ages.put("Alex", 29);
            List<String> lines = new ArrayList<>();
            ages.forEach((name, age) -> lines.add(name + " is " + age));
            List<String> report = lines;                               // [Lokesh is 37, Alex is 29]
            show("report", report);
            for (Map.Entry<String, Integer> entry : ages.entrySet()) {
                lines.add(entry.getKey() + ":" + entry.getValue());
            }
            int lineCount = lines.size();                              // 4
            show("lineCount", lineCount);
        }
        {
            Map<String, Integer> cart = Map.of("apple", 3, "kiwi", 2);
            int[] total = {0};
            cart.forEach((_, qty) -> total[0] += qty);
            int items = total[0];                                      // 5
            show("items", items);
        }
        {
            List<Integer> readings = List.of(3, 8, -1, 5, 9);
            List<Integer> seen = new ArrayList<>();
            readings.forEach(r -> {
                if (r < 0) {
                    return;                                            // skips -1 only
                }
                seen.add(r);
            });
            List<Integer> withoutNegative = seen;                      // [3, 8, 5, 9]
            show("withoutNegative", withoutNegative);
            List<Integer> beforeNegative = readings.stream().takeWhile(r -> r >= 0).toList();    // [3, 8]
            show("beforeNegative", beforeNegative);
            boolean hasNegative = readings.stream().anyMatch(r -> r < 0);                        // true
            show("hasNegative", hasNegative);
        }
        {
            Path dir = Files.createTempDirectory("notes");
            List<String> notes = List.of("milk", "eggs");
            notes.forEach(note -> {
                try {
                    Files.writeString(dir.resolve(note + ".txt"), note);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
            boolean written = Files.exists(dir.resolve("eggs.txt"));   // true
            show("written", written);
        }
        {
            List<String> basket = new ArrayList<>(List.of("apple", "banana", "kiwi"));
            try { basket.forEach(item -> basket.remove("kiwi"));  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> basket = new ArrayList<>(List.of("apple", "banana", "kiwi"));
            boolean removed = basket.removeIf(item -> item.equals("kiwi"));      // true
            show("removed", removed);
            basket.replaceAll(String::toUpperCase);
            List<String> result = basket;                                        // [APPLE, BANANA]
            show("result", result);
        }
        {
            List<String> words = List.of("one", "two", "three");
            List<String> fromList = new ArrayList<>();
            words.forEach(fromList::add);
            List<String> listOrder = fromList;                                      // [one, two, three]
            show("listOrder", listOrder);
            List<String> longWords = words.stream().filter(w -> w.length() > 3).toList();   // [three]
            show("longWords", longWords);
        }
        {
            Map<String, Integer> stock = new TreeMap<>(Map.of("apple", 5, "banana", 0, "kiwi", 2));
            List<String> reorder = new ArrayList<>();
            stock.forEach((item, qty) -> {
                if (qty < 3) {
                    reorder.add(item);
                }
            });
            List<String> toOrder = reorder;                                          // [banana, kiwi]
            show("toOrder", toOrder);
            StringJoiner sheet = new StringJoiner(", ");
            stock.forEach((item, qty) -> sheet.add(item + ": " + qty));
            String printed = sheet.toString();                                       // "apple: 5, banana: 0, kiwi: 2"
            show("printed", printed);
            stock.replaceAll((item, qty) -> qty < 3 ? qty + 10 : qty);
            Map<String, Integer> afterDelivery = stock;                              // {apple=5, banana=10, kiwi=12}
            show("afterDelivery", afterDelivery);
        }
        {
            int[] scores = {7, 9, 4};
            int[] sum = {0};
            Arrays.stream(scores).forEach(s -> sum[0] += s);
            int total = sum[0];                                        // 20
            show("total", total);
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
