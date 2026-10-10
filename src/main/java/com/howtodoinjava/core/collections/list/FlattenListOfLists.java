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

import org.eclipse.collections.api.list.*;
import org.eclipse.collections.impl.list.mutable.*;

/**
 * Examples for the tutorial "Flatten a List of Lists in Java: flatMap, Loops and Libraries".
 * https://howtodoinjava.com/java/collections/arraylist/flatten-nested-lists/
 */
public class FlattenListOfLists {
    static Stream<Object> flatten(Object item) {
        return item instanceof List<?> list
        ? list.stream().flatMap(element -> flatten(element))
        : Stream.of(item);
    }
    public static void main(String[] args) throws Exception {
        {
            List<List<String>> recipes = List.of(
            List.of("flour", "eggs"),
            List.of("rice"),
            List.of("eggs", "milk"));
            List<String> shopping = recipes.stream().flatMap(List::stream).toList();    // [flour, eggs, rice, eggs, milk]
            show("shopping", shopping);
        }
        {
            List<List<String>> recipes = List.of(List.of("flour", "eggs"), List.of("rice"), List.of("eggs", "milk"));
            List<String> all = recipes.stream().flatMap(List::stream).toList();                       // [flour, eggs, rice, eggs, milk]
            show("all", all);
            List<String> unique = recipes.stream().flatMap(List::stream).distinct().sorted().toList(); // [eggs, flour, milk, rice]
            show("unique", unique);
            long count = recipes.stream().mapToLong(List::size).sum();                                 // 5
            show("count", count);
        }
        {
            List<List<String>> recipes = List.of(List.of("flour", "eggs"), List.of("rice"));
            List<String> editable = recipes.stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toCollection(ArrayList::new));
            editable.add("salt");                                    // editable = [flour, eggs, rice, salt]
        }
        {
            List<List<String>> recipes = List.of(List.of("flour", "eggs"), List.of("rice"), List.of("eggs", "milk"));
            int total = recipes.stream().mapToInt(List::size).sum();     // 5
            show("total", total);
            List<String> flat = new ArrayList<>(total);
            recipes.forEach(flat::addAll);                               // flat = [flour, eggs, rice, eggs, milk]
        }
        {
            List<List<String>> recipes = List.of(List.of("flour", "water"), List.of("rice", "water"), List.of("milk"));
            List<String> toBuy = recipes.stream()
                    .<String>mapMulti((recipe, sink) -> {
                for (String item : recipe) {
                    if (!item.equals("water")) {
                        sink.accept(item);
                    }
                }
            })
                    .toList();
            String result = toBuy.toString();                          // "[flour, rice, milk]"
            show("result", result);
        }
        {
            // avoid: mutates the identity value of reduce()
            List<List<String>> recipes = List.of(List.of("flour", "eggs"), List.of("rice"));
            List<String> risky = recipes.stream().reduce(new ArrayList<>(), (acc, recipe) -> {
                acc.addAll(recipe);
                return acc;
            });
        }
        {
            List<List<String>> recipes = List.of(List.of("flour", "eggs"), List.of("rice"));
            ArrayList<String> merged = recipes.stream().collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);    // [flour, eggs, rice]
            show("merged", merged);
        }
        {
            List<Object> nested = List.of(1, List.of(2, List.of(3, 4)), 5);
            List<Object> deep = flatten(nested).toList();                 // [1, 2, 3, 4, 5]
            show("deep", deep);
            List<Object> mixed = flatten(List.of("a", List.of("b"), "c")).toList();    // [a, b, c]
            show("mixed", mixed);
        }
        {
            List<List<String>> withGaps = Arrays.asList(List.of("flour"), null, List.of("milk"));
            List<String> safe = withGaps.stream().filter(Objects::nonNull).flatMap(List::stream).toList();    // [flour, milk]
            show("safe", safe);
            try { List<String> broken = withGaps.stream().flatMap(List::stream).toList(); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            Map<String, List<String>> tagsByPost = new LinkedHashMap<>();
            tagsByPost.put("intro", List.of("java", "lists"));
            tagsByPost.put("streams", List.of("java", "streams"));
            List<String> allTags = tagsByPost.values().stream().flatMap(List::stream).distinct().toList();    // [java, lists, streams]
            show("allTags", allTags);
        }
        {
            List<List<String>> recipes = List.of(List.of("flour", "eggs"), List.of("rice"));
            MutableList<String> ecList = ListAdapter.adapt(recipes).flatCollect(recipe -> recipe);    // [flour, eggs, rice]
            show("ecList", ecList);
        }
        {
            List<List<Integer>> rows = List.of(List.of(1, 2), List.of(3));
            int[] numbers = rows.stream().flatMap(List::stream).mapToInt(Integer::intValue).toArray();    // [1, 2, 3]
            show("numbers", numbers);
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
