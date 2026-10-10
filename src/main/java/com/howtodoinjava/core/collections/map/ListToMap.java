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
 * Examples for the tutorial "Convert List to Map in Java: toMap(), Duplicate Keys and Order".
 * https://howtodoinjava.com/java/collections/convert-list-to-map/
 */
public class ListToMap {
    static record Book(int id, String title, String author) {

        @Override
        public String toString() {
            return title;
        }
    }

    static List<Book> books() {
        return List.of(
        new Book(1, "Dune", "Herbert"),
        new Book(2, "Emma", "Austen"),
        new Book(3, "Persuasion", "Austen"),
        new Book(4, "Ubik", "Dick"));
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> fruits = List.of("apple", "kiwi", "banana", "pear");
            Map<String, Integer> lengths = fruits.stream().collect(Collectors.toMap(Function.identity(), String::length));
            Integer apple = lengths.get("apple");               // 5
            show("apple", apple);
            Map<Integer, List<String>> byLength = fruits.stream().collect(Collectors.groupingBy(String::length));
            List<String> fourLetters = byLength.get(4);         // [kiwi, pear]
            show("fourLetters", fourLetters);
        }
        {
            Map<Integer, Book> byId = new HashMap<>();
            for (Book book : books()) {
                byId.put(book.id(), book);
            }
            Book dune = byId.get(1);                            // Dune
            show("dune", dune);
        }
        {
            Map<String, Book> lastByAuthor = new HashMap<>();
            Map<String, Book> firstByAuthor = new HashMap<>();
            Map<String, List<Book>> allByAuthor = new HashMap<>();
            for (Book book : books()) {
                lastByAuthor.put(book.author(), book);
                firstByAuthor.putIfAbsent(book.author(), book);
                allByAuthor.computeIfAbsent(book.author(), author -> new ArrayList<>()).add(book);
            }
            Book last = lastByAuthor.get("Austen");             // Persuasion
            show("last", last);
            Book first = firstByAuthor.get("Austen");           // Emma
            show("first", first);
            List<Book> all = allByAuthor.get("Austen");         // [Emma, Persuasion]
            show("all", all);
        }
        {
            Map<Integer, Book> byId = books().stream().collect(Collectors.toMap(Book::id, Function.identity()));
            Book emma = byId.get(2);                            // Emma
            show("emma", emma);
            Map<Integer, String> titles = books().stream().collect(Collectors.toMap(Book::id, Book::title));
            String ubik = titles.get(4);                        // "Ubik"
            show("ubik", ubik);
        }
        {
            try { Map<String, Book> byAuthor = books().stream().collect(Collectors.toMap(Book::author, Function.identity())); show("byAuthor", byAuthor); } catch (Throwable _t) { System.out.println("byAuthor -> " + _t); }
        }
        {
            Map<String, Book> firstBook = books().stream().collect(Collectors.toMap(Book::author, Function.identity(), (existing, replacement) -> existing));
            Book keptFirst = firstBook.get("Austen");           // Emma
            show("keptFirst", keptFirst);
            Map<String, Book> lastBook = books().stream().collect(Collectors.toMap(Book::author, Function.identity(), (existing, replacement) -> replacement));
            Book keptLast = lastBook.get("Austen");             // Persuasion
            show("keptLast", keptLast);
            Map<String, String> joinedTitles = books().stream().collect(Collectors.toMap(Book::author, Book::title, (a, b) -> a + ", " + b));
            String austen = joinedTitles.get("Austen");         // "Emma, Persuasion"
            show("austen", austen);
            Map<String, Integer> bookCount = books().stream().collect(Collectors.toMap(Book::author, b -> 1, Integer::sum));
            Integer austenCount = bookCount.get("Austen");      // 2
            show("austenCount", austenCount);
        }
        {
            Map<String, Integer> inListOrder = books().stream().collect(Collectors.toMap(Book::title, Book::id, (a, b) -> a, LinkedHashMap::new));   // {Dune=1, Emma=2, Persuasion=3, Ubik=4}
            show("inListOrder", inListOrder);
            Map<String, Integer> sortedByAuthor = books().stream().collect(Collectors.toMap(Book::author, b -> 1, Integer::sum, TreeMap::new));   // {Austen=2, Dick=1, Herbert=1}
            show("sortedByAuthor", sortedByAuthor);
        }
        {
            List<String> names = Arrays.asList("ann", null, "bo");
            try { Map<String, Integer> broken = names.stream().collect(Collectors.toMap(n -> String.valueOf(n), n -> n == null ? null : n.length())); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
            Map<String, Integer> safe = names.stream().filter(Objects::nonNull).collect(Collectors.toMap(Function.identity(), String::length));
            Integer bo = safe.get("bo");                        // 2
            show("bo", bo);
        }
        {
            Map<Integer, String> catalog = books().stream().collect(Collectors.toUnmodifiableMap(Book::id, Book::title));
            String title = catalog.get(3);                      // "Persuasion"
            show("title", title);
            try { catalog.put(5, "Solaris");  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Map<String, List<Book>> byAuthor = books().stream().collect(Collectors.groupingBy(Book::author));
            List<Book> austenBooks = byAuthor.get("Austen");    // [Emma, Persuasion]
            show("austenBooks", austenBooks);
            Map<String, List<String>> titlesByAuthor = books().stream().collect(Collectors.groupingBy(Book::author, TreeMap::new, Collectors.mapping(Book::title, Collectors.toList())));   // {Austen=[Emma, Persuasion], Dick=[Ubik], Herbert=[Dune]}
            show("titlesByAuthor", titlesByAuthor);
            Map<String, Long> countByAuthor = books().stream().collect(Collectors.groupingBy(Book::author, Collectors.counting()));
            Long austenTotal = countByAuthor.get("Austen");     // 2
            show("austenTotal", austenTotal);
        }
        {
            List<String> steps = List.of("wash", "cut", "cook");
            Map<Integer, String> byIndex = IntStream.range(0, steps.size()).boxed().collect(Collectors.toMap(i -> i, steps::get));
            String second = byIndex.get(1);                     // "cut"
            show("second", second);
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
