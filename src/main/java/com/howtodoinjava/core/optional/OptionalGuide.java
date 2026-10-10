package com.howtodoinjava.core.optional;

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
 * Examples for the tutorial "Java Optional Guide: Methods, Examples and Best Practices".
 * https://howtodoinjava.com/java8/java-8-optionals-complete-reference/
 */
public class OptionalGuide {
    static record Member(String name, String email) {

        Optional<String> emailAddress() {
            return Optional.ofNullable(email);
        }
    }
    static record Book(String title, Member borrower) {

        Optional<Member> currentBorrower() {
            return Optional.ofNullable(borrower);
        }
    }
    static class Catalog {

        private final Map<String, Book> books = new HashMap<>();

        void add(Book book) {
            books.put(book.title(), book);
        }

        Optional<Book> findByTitle(String title) {
            return Optional.ofNullable(books.get(title));
        }
    }
    static Catalog sampleCatalog() {
        Catalog catalog = new Catalog();
        catalog.add(new Book("Dune", new Member("Lokesh", "lokesh@mail.com")));
        catalog.add(new Book("Emma", new Member("Ana", null)));
        catalog.add(new Book("Ulysses", null));
        return catalog;
    }
    static Book requireBook(Catalog catalog, String title) {
        return catalog.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("No book titled " + title));
    }
    static String borrowerEmailWithNullChecks(Map<String, Book> books, String title) {
        Book book = books.get(title);
        if (book != null) {
            Member member = book.borrower();
            if (member != null && member.email() != null) {
                return member.email();
            }
        }
        return "no email";
    }
    static String borrowerEmail(Catalog catalog, String title) {
        return catalog.findByTitle(title)
                .flatMap(Book::currentBorrower)
                .flatMap(Member::emailAddress)
                .orElse("no email");
    }
    public static void main(String[] args) throws Exception {
        {
            Optional<String> title = Optional.of("Dune");
            Optional<String> none = Optional.empty();

            boolean present = title.isPresent();                             // true
            show("present", present);
            boolean empty = none.isEmpty();                                  // true
            show("empty", empty);
            String upper = title.map(String::toUpperCase).orElse("?");       // "DUNE"
            show("upper", upper);
            String shortOnly = title.filter(t -> t.length() < 4).orElse("long title");  // "long title"
            show("shortOnly", shortOnly);
            String fallback = none.or(() -> Optional.of("Emma")).orElseThrow();  // "Emma"
            show("fallback", fallback);
            String value = title.orElseThrow();                              // "Dune"
            show("value", value);
            long count = none.stream().count();                              // 0
            show("count", count);
        }
        {
            Optional<String> nothing = Optional.empty();                    // Optional.empty
            show("nothing", nothing);
            Optional<String> dune = Optional.of("Dune");                     // Optional[Dune]
            show("dune", dune);
            Optional<String> fromMap = Optional.ofNullable(null);            // Optional.empty
            show("fromMap", fromMap);
            try { Optional<String> broken = Optional.of(null); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            Catalog catalog = sampleCatalog();

            boolean hasDune = catalog.findByTitle("Dune").isPresent();       // true
            show("hasDune", hasDune);
            boolean noHobbit = catalog.findByTitle("Hobbit").isEmpty();      // true
            show("noHobbit", noHobbit);
        }
        {
            Catalog catalog = sampleCatalog();
            List<String> reminders = new ArrayList<>();

            catalog.findByTitle("Dune")
                    .flatMap(Book::currentBorrower)
                    .ifPresent(member -> reminders.add("Remind " + member.name()));
            catalog.findByTitle("Ulysses")
                    .flatMap(Book::currentBorrower)
                    .ifPresentOrElse(member -> reminders.add("Remind " + member.name()),
            () -> reminders.add("Ulysses is on the shelf"));
            List<String> sent = reminders;                                   // [Remind Lokesh, Ulysses is on the shelf]
            show("sent", sent);
        }
        {
            Catalog catalog = sampleCatalog();

            String found = catalog.findByTitle("Emma").map(Book::title).orElse("not in catalog");     // "Emma"
            show("found", found);
            String missing = catalog.findByTitle("Hobbit").map(Book::title).orElse("not in catalog"); // "not in catalog"
            show("missing", missing);
            Book dune = catalog.findByTitle("Dune").orElseThrow();           // Book[title=Dune, borrower=Member[name=Lokesh, email=lokesh@mail.com]]
            show("dune", dune);
            try { Book hobbit = catalog.findByTitle("Hobbit").orElseThrow(); show("hobbit", hobbit); } catch (Throwable _t) { System.out.println("hobbit -> " + _t); }
        }
        {
            Catalog catalog = sampleCatalog();

            String title = requireBook(catalog, "Dune").title();             // "Dune"
            show("title", title);
            try { Book hobbit = requireBook(catalog, "Hobbit"); show("hobbit", hobbit); } catch (Throwable _t) { System.out.println("hobbit -> " + _t); }
        }
        {
            Catalog catalog = sampleCatalog();

            String lokesh = borrowerEmail(catalog, "Dune");                  // "lokesh@mail.com"
            show("lokesh", lokesh);
            String ana = borrowerEmail(catalog, "Emma");                     // "no email", Ana has no email
            show("ana", ana);
            String shelf = borrowerEmail(catalog, "Ulysses");                // "no email", nobody borrowed it
            show("shelf", shelf);
            String unknown = borrowerEmail(catalog, "Hobbit");               // "no email", no such book
            show("unknown", unknown);
        }
        {
            Catalog catalog = sampleCatalog();

            Optional<String> viaMap = catalog.findByTitle("Emma")
                    .map(Book::borrower)
                    .map(Member::email);
            boolean noEmail = viaMap.isEmpty();                              // true, map() turned null into empty
            show("noEmail", noEmail);
            boolean borrowed = catalog.findByTitle("Ulysses")
                    .filter(b -> b.borrower() != null)
                    .isPresent();                                            // false
        }
        {
            Catalog cache = new Catalog();
            Catalog database = sampleCatalog();

            Optional<Book> book = cache.findByTitle("Emma")
                    .or(() -> database.findByTitle("Emma"));
            String title = book.map(Book::title).orElse("none");             // "Emma", found in the database
            show("title", title);
        }
        {
            Catalog catalog = sampleCatalog();

            List<String> borrowers = Stream.of("Dune", "Emma", "Ulysses", "Hobbit")
                    .map(catalog::findByTitle)
                    .flatMap(Optional::stream)
                    .flatMap(b -> b.currentBorrower().stream())
                    .map(Member::name)
                    .toList();
            List<String> names = borrowers;                                  // [Lokesh, Ana]
            show("names", names);
        }
        {
            OptionalInt longest = Stream.of("Dune", "Emma", "Ulysses")
                    .mapToInt(String::length)
                    .max();
            int length = longest.orElse(0);                                  // 7
            show("length", length);
            OptionalDouble noAverage = IntStream.empty().average();
            boolean none = noAverage.isEmpty();                              // true
            show("none", none);
        }
        {
            Optional<Book> notFound = null;
            try { boolean exists = notFound.isPresent(); show("exists", exists); } catch (Throwable _t) { System.out.println("exists -> " + _t); }
        }
        {
            Optional<Book> notFound = Optional.empty();
            boolean exists = notFound.isPresent();                           // false
            show("exists", exists);
        }
        {
            List<String> one = Optional.of("Dune").stream().toList();      // [Dune]
            show("one", one);
            List<String> zero = Optional.<String>empty().stream().toList();  // []
            show("zero", zero);
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
