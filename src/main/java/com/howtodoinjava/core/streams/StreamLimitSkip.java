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

import java.time.format.*;

/**
 * Examples for the tutorial "Java Stream limit() and skip() With Pagination Examples".
 * https://howtodoinjava.com/java8/java-stream-limit-method-example/
 */
public class StreamLimitSkip {
    static <T> List<T> page(List<T> items, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("pageNumber and pageSize must be positive");
        }
        return items.stream()
                .skip((long) (pageNumber - 1) * pageSize)
                .limit(pageSize)
                .toList();
    }
    static record Recipe(String name, int minutes) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> recipes = List.of("Soup", "Salad", "Pasta", "Curry", "Tacos", "Pie", "Stew");
            List<String> firstThree = recipes.stream().limit(3).toList();          // [Soup, Salad, Pasta]
            show("firstThree", firstThree);
            List<String> afterThree = recipes.stream().skip(3).toList();           // [Curry, Tacos, Pie, Stew]
            show("afterThree", afterThree);
            List<String> pageTwo = recipes.stream().skip(3).limit(3).toList();     // [Curry, Tacos, Pie]
            show("pageTwo", pageTwo);
        }
        {
            List<String> recipes = List.of("Soup", "Salad", "Pasta", "Curry", "Tacos", "Pie", "Stew");
            List<String> two = recipes.stream().limit(2).toList();           // [Soup, Salad]
            show("two", two);
            List<String> all = recipes.stream().limit(100).toList();         // [Soup, Salad, Pasta, Curry, Tacos, Pie, Stew]
            show("all", all);
            List<String> none = recipes.stream().limit(0).toList();          // []
            show("none", none);
            try { List<String> negative = recipes.stream().limit(-1).toList(); show("negative", negative); } catch (Throwable _t) { System.out.println("negative -> " + _t); }
        }
        {
            List<Integer> firstTenEven = Stream.iterate(0, n -> n + 2).limit(10).toList();   // [0, 2, 4, 6, 8, 10, 12, 14, 16, 18]
            show("firstTenEven", firstTenEven);
        }
        {
            AtomicInteger mapped = new AtomicInteger();
            List<Integer> squares = Stream.of(1, 2, 3, 4, 5).map(n -> { mapped.incrementAndGet(); return n * n; }).limit(2).toList();   // [1, 4]
            show("squares", squares);
            int calls = mapped.get();                                                                                                   // 2
            show("calls", calls);
        }
        {
            List<String> recipes = List.of("Soup", "Salad", "Pasta", "Curry", "Tacos", "Pie", "Stew");
            List<String> limitThenFilter = recipes.stream().limit(3).filter(r -> r.length() <= 4).toList();   // [Soup]
            show("limitThenFilter", limitThenFilter);
            List<String> filterThenLimit = recipes.stream().filter(r -> r.length() <= 4).limit(3).toList();   // [Soup, Pie, Stew]
            show("filterThenLimit", filterThenLimit);
            List<String> topTwoAlphabetical = recipes.stream().sorted().limit(2).toList();                    // [Curry, Pasta]
            show("topTwoAlphabetical", topTwoAlphabetical);
        }
        {
            List<String> recipes = List.of("Soup", "Salad", "Pasta", "Curry", "Tacos", "Pie", "Stew");
            List<String> lastTwo = recipes.stream().skip(5).toList();        // [Pie, Stew]
            show("lastTwo", lastTwo);
            List<String> nothing = recipes.stream().skip(10).toList();       // []
            show("nothing", nothing);
            List<String> same = recipes.stream().skip(0).toList();           // [Soup, Salad, Pasta, Curry, Tacos, Pie, Stew]
            show("same", same);
            try { List<String> broken = recipes.stream().skip(-2).toList(); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            String csv = """
            name,minutes
            Soup,25
            Salad,10
            """;
            List<String> rows = csv.lines().skip(1).toList();   // [Soup,25, Salad,10]
            show("rows", rows);
        }
        {
            List<String> recipes = List.of("Soup", "Salad", "Pasta", "Curry", "Tacos", "Pie", "Stew");
            List<String> page1 = page(recipes, 1, 3);                 // [Soup, Salad, Pasta]
            show("page1", page1);
            List<String> page3 = page(recipes, 3, 3);                 // [Stew]
            show("page3", page3);
            List<String> page4 = page(recipes, 4, 3);                 // []
            show("page4", page4);
            int totalPages = Math.ceilDiv(recipes.size(), 3);         // 3
            show("totalPages", totalPages);
            try { List<String> page0 = page(recipes, 0, 3); show("page0", page0); } catch (Throwable _t) { System.out.println("page0 -> " + _t); }
        }
        {
            List<String> recipes = List.of("Soup", "Salad", "Pasta", "Curry", "Tacos", "Pie", "Stew");
            int from = Math.min(3, recipes.size());
            int to = Math.min(from + 3, recipes.size());
            List<String> view = recipes.subList(from, to);            // [Curry, Tacos, Pie]
            show("view", view);
        }
        {
            List<Integer> sizes = List.of(2, 4, 6, 7, 8);
            List<Integer> taken = sizes.stream().takeWhile(n -> n % 2 == 0).toList();     // [2, 4, 6]
            show("taken", taken);
            List<Integer> dropped = sizes.stream().dropWhile(n -> n % 2 == 0).toList();   // [7, 8]
            show("dropped", dropped);
            List<Integer> filtered = sizes.stream().filter(n -> n % 2 == 0).toList();     // [2, 4, 6, 8]
            show("filtered", filtered);
        }
        {
            String csv = """
            name,minutes
            Soup,25
            Salad,10
            Pasta,20
            Curry,45
            Tacos,15
            """;
            List<Recipe> quick = csv.lines()
                    .skip(1)
                    .map(line -> line.split(","))
                    .map(parts -> new Recipe(parts[0].strip(), Integer.parseInt(parts[1].strip())))
                    .filter(r -> r.minutes() <= 25)
                    .sorted(Comparator.comparingInt(Recipe::minutes))
                    .toList();
            List<String> firstPage = page(quick, 1, 2).stream().map(Recipe::name).toList();    // [Salad, Tacos]
            show("firstPage", firstPage);
            List<String> secondPage = page(quick, 2, 2).stream().map(Recipe::name).toList();   // [Pasta, Soup]
            show("secondPage", secondPage);
            int pages = Math.ceilDiv(quick.size(), 2);                                          // 2
            show("pages", pages);
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
