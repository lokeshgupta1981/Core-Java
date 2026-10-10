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
 * Examples for the tutorial "Java Stream anyMatch(), allMatch() and noneMatch() Examples".
 * https://howtodoinjava.com/java8/stream-anymatch-example/
 */
public class StreamMatchExamples {
    static record Book(String title, int pages, boolean available) {}
    static record Rule(String message, Predicate<String> check) {}
    public static void main(String[] args) throws Exception {
        {
            List<Integer> scores = List.of(72, 88, 95, 64);
            boolean anyFailed = scores.stream().anyMatch(s -> s < 65);     // true
            show("anyFailed", anyFailed);
            boolean allPassed = scores.stream().allMatch(s -> s >= 65);    // false
            show("allPassed", allPassed);
            boolean noneZero = scores.stream().noneMatch(s -> s == 0);     // true
            show("noneZero", noneZero);
        }
        {
            List<Book> books = List.of(new Book("Dune", 412, true), new Book("Emma", 474, false), new Book("Ulysses", 730, true));
            boolean anyLong = books.stream().anyMatch(b -> b.pages() > 700);                // true
            show("anyLong", anyLong);
            boolean anyOnLoan = books.stream().anyMatch(Predicate.not(Book::available));    // true
            show("anyOnLoan", anyOnLoan);
            boolean anyByTitle = books.stream().anyMatch(b -> b.title().equals("Emma"));    // true
            show("anyByTitle", anyByTitle);
            boolean anyHuge = books.stream().anyMatch(b -> b.pages() > 1000);               // false
            show("anyHuge", anyHuge);
        }
        {
            List<Book> books = List.of(new Book("Dune", 412, true), new Book("Emma", 474, false), new Book("Ulysses", 730, true));
            Predicate<Book> longBook = b -> b.pages() > 450;
            Predicate<Book> onShelf = Book::available;
            boolean longAndOnShelf = books.stream().anyMatch(longBook.and(onShelf));             // true
            show("longAndOnShelf", longAndOnShelf);
            boolean shortAndOnLoan = books.stream().anyMatch(longBook.negate().and(onShelf.negate()));   // false
            show("shortAndOnLoan", shortAndOnLoan);
        }
        {
            List<Book> books = List.of(new Book("Dune", 412, true), new Book("Emma", 474, false), new Book("Ulysses", 730, true));
            boolean allOver400 = books.stream().allMatch(b -> b.pages() > 400);       // true
            show("allOver400", allOver400);
            boolean allAvailable = books.stream().allMatch(Book::available);          // false
            show("allAvailable", allAvailable);
            boolean allTitled = books.stream().allMatch(b -> !b.title().isBlank());   // true
            show("allTitled", allTitled);
        }
        {
            List<Book> books = List.of(new Book("Dune", 412, true), new Book("Emma", 474, false), new Book("Ulysses", 730, true));
            boolean noneHuge = books.stream().noneMatch(b -> b.pages() > 1000);           // true
            show("noneHuge", noneHuge);
            boolean noneOnLoan = books.stream().noneMatch(Predicate.not(Book::available));  // false
            show("noneOnLoan", noneOnLoan);
        }
        {
            List<Integer> scores = List.of(72, 88, 95, 64);
            Predicate<Integer> failed = s -> s < 65;
            boolean viaNone = scores.stream().noneMatch(failed);                  // false
            show("viaNone", viaNone);
            boolean viaAny = !scores.stream().anyMatch(failed);                   // false
            show("viaAny", viaAny);
            boolean viaAll = scores.stream().allMatch(Predicate.not(failed));     // false
            show("viaAll", viaAll);
        }
        {
            List<String> codes = List.of("one", "two", "a1");
            boolean wrongCheck = codes.stream().noneMatch(s -> s.contains("\\d+"));                  // true, contains() is not a regex
            show("wrongCheck", wrongCheck);
            boolean noDigits = codes.stream().noneMatch(s -> s.chars().anyMatch(Character::isDigit));  // false
            show("noDigits", noDigits);
            boolean noDigitsRegex = codes.stream().noneMatch(s -> s.matches(".*\\d.*"));              // false
            show("noDigitsRegex", noDigitsRegex);
        }
        {
            List<Integer> uploadSizes = List.of();
            boolean anyLarge = uploadSizes.stream().anyMatch(s -> s > 5_000);                                  // false
            show("anyLarge", anyLarge);
            boolean allSmall = uploadSizes.stream().allMatch(s -> s < 5_000);                                  // true
            show("allSmall", allSmall);
            boolean noneLarge = uploadSizes.stream().noneMatch(s -> s > 5_000);                                // true
            show("noneLarge", noneLarge);
            boolean validUpload = !uploadSizes.isEmpty() && uploadSizes.stream().allMatch(s -> s < 5_000);     // false
            show("validUpload", validUpload);
        }
        {
            List<Integer> tested = new ArrayList<>();
            boolean found = Stream.of(72, 88, 95, 64).peek(tested::add).anyMatch(s -> s > 80);   // true
            show("found", found);
            List<Integer> visited = List.copyOf(tested);                                         // [72, 88]
            show("visited", visited);
        }
        {
            boolean hasBig = Stream.iterate(1, n -> n * 2).anyMatch(n -> n > 1000);     // true, stops at 1024
            show("hasBig", hasBig);
            boolean allSmall = Stream.iterate(1, n -> n * 2).allMatch(n -> n < 1000);   // false, stops at 1024
            show("allSmall", allSmall);
        }
        {
            List<String> tags = List.of("java", "spring", "sql");
            boolean hasJava = tags.contains("java");                                                 // true
            show("hasJava", hasJava);
            boolean hasUpper = tags.contains("JAVA");                                                // false
            show("hasUpper", hasUpper);
            boolean hasIgnoreCase = tags.stream().anyMatch("JAVA"::equalsIgnoreCase);                // true
            show("hasIgnoreCase", hasIgnoreCase);
            boolean viaFilter = tags.stream().filter(t -> t.startsWith("s")).findFirst().isPresent();  // true
            show("viaFilter", viaFilter);
        }
        {
            int[] ratings = {4, 5, 3, 5};
            boolean allValid = Arrays.stream(ratings).allMatch(r -> r >= 1 && r <= 5);    // true
            show("allValid", allValid);
            boolean anyPerfect = IntStream.of(ratings).anyMatch(r -> r == 5);             // true
            show("anyPerfect", anyPerfect);
            Map<String, Integer> stock = Map.of("pens", 40, "notebooks", 0, "folders", 12);
            boolean anyEmpty = stock.values().stream().anyMatch(q -> q == 0);             // true
            show("anyEmpty", anyEmpty);
            boolean noneNegative = stock.values().stream().noneMatch(q -> q < 0);         // true
            show("noneNegative", noneNegative);
        }
        {
            List<Rule> rules = List.of(
            new Rule("at least 12 characters", p -> p.length() >= 12),
            new Rule("a digit", p -> p.chars().anyMatch(Character::isDigit)),
            new Rule("an uppercase letter", p -> p.chars().anyMatch(Character::isUpperCase)));
            List<String> blockedWords = List.of("password", "qwerty");
            String password = "riverside7";
            boolean strong = rules.stream().allMatch(r -> r.check().test(password));                        // false
            show("strong", strong);
            List<String> missing = rules.stream().filter(r -> !r.check().test(password)).map(Rule::message).toList();   // [at least 12 characters, an uppercase letter]
            show("missing", missing);
            boolean noBlockedWord = blockedWords.stream().noneMatch(w -> password.toLowerCase(Locale.ROOT).contains(w));   // true
            show("noBlockedWord", noBlockedWord);
        }
        {
            Stream<String> titles = Stream.of("Dune", "Emma");
            boolean first = titles.anyMatch(t -> t.startsWith("D"));     // true
            show("first", first);
            try { boolean second = titles.anyMatch(t -> t.startsWith("E")); show("second", second); } catch (Throwable _t) { System.out.println("second -> " + _t); }
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
