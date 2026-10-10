package com.howtodoinjava.core.streams;

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
 * Examples for the tutorial "How to Debug Java Streams: peek() and IntelliJ Stream Trace".
 * https://howtodoinjava.com/java/stream/debugging-java-streams/
 */
public class DebugStreams {
    static record Recipe(String name, int minutes, boolean vegetarian) {}
    static List<Recipe> sampleRecipes() {
        return List.of(
        new Recipe("Pancakes", 20, true),
        new Recipe("Chili", 45, false),
        new Recipe("Salad", 10, true),
        new Recipe("Omelette", 30, true),
        new Recipe("Tacos", 25, false));
    }
    static boolean isQuick(Recipe recipe, int maxMinutes) {
        return recipe.minutes() <= maxMinutes;
    }
    static List<String> quickVegetarian(List<Recipe> recipes, int maxMinutes) {
        return recipes.stream()
                .filter(Recipe::vegetarian)
                .filter(r -> isQuick(r, maxMinutes))
                .map(Recipe::name)
                .sorted()
                .toList();
    }
    public static void main(String[] args) throws Exception {
        {
            List<Recipe> recipes = sampleRecipes();
            List<String> quick = recipes.stream()
                    .filter(Recipe::vegetarian)
                    .peek(r -> System.out.println("vegetarian: " + r.name()))
                    .filter(r -> r.minutes() < 30)
                    .peek(r -> System.out.println("quick: " + r.name()))
                    .map(Recipe::name)
                    .sorted()
                    .toList();                     // [Pancakes, Salad]
        }
        {
            List<String> order = new ArrayList<>();
            List<String> sortedNames = Stream.of("Tacos", "Chili")
                    .peek(n -> order.add("before " + n))
                    .sorted()
                    .peek(n -> order.add("after " + n))
                    .toList();                     // [Chili, Tacos]
            List<String> steps = order;            // [before Tacos, before Chili, after Chili, after Tacos]
            show("steps", steps);
        }
        {
            List<Recipe> recipes = sampleRecipes();
            List<String> seen = new ArrayList<>();
            long total = recipes.stream().peek(r -> seen.add(r.name())).count();     // 5
            show("total", total);
            List<String> peeked = seen;                                               // [], count() never ran the pipeline
            show("peeked", peeked);
            Optional<Recipe> first = recipes.stream().peek(r -> seen.add(r.name())).filter(r -> r.minutes() < 15).findFirst();   // Optional[Recipe[name=Salad, minutes=10, vegetarian=true]]
            show("first", first);
            List<String> visited = seen;                                              // [Pancakes, Chili, Salad]
            show("visited", visited);
        }
        {
            List<Recipe> recipes = sampleRecipes();
            List<String> fixed = quickVegetarian(recipes, 30);           // [Omelette, Pancakes, Salad]
            show("fixed", fixed);
            boolean edgeCase = isQuick(new Recipe("Soup", 30, true), 30);  // true
            show("edgeCase", edgeCase);
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
