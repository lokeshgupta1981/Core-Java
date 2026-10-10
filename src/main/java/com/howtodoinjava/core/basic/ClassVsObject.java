package com.howtodoinjava.core.basic;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;

/**
 * Examples for the tutorial "Java Class and Object: Differences, Creation and Examples".
 * https://howtodoinjava.com/java/basics/java-classes-objects/
 */
public class ClassVsObject {
    static class Book {
        private final String title;
        private final int pages;
        private boolean borrowed;

        Book(String title, int pages) {
            if (pages <= 0) {
                throw new IllegalArgumentException("pages must be positive");
            }
            this.title = title;
            this.pages = pages;
        }

        void borrow() {
            if (borrowed) {
                throw new IllegalStateException(title + " is already borrowed");
            }
            borrowed = true;
        }

        void giveBack() {
            borrowed = false;
        }

        boolean isBorrowed() {
            return borrowed;
        }

        int pages() {
            return pages;
        }

        String title() {
            return title;
        }
    }
    static class Shelf {
        int count;
        boolean full;
        String label;
    }
    static class Visit {
        static final List<String> LOG = new ArrayList<>();

        static {
            LOG.add("static block");
        }

        private final String guest;

        {
            LOG.add("instance block");
        }

        Visit(String guest) {
            this.guest = guest;
            LOG.add("constructor " + guest);
        }
    }
    static enum Genre { NOVEL, POETRY }
    static record Author(String name, Genre genre) {}
    public static void main(String[] args) throws Exception {
        {
            Book first = new Book("Dune", 412);
            Book second = new Book("Emma", 320);
            first.borrow();
            boolean firstOut = first.isBorrowed();      // true
            show("firstOut", firstOut);
            boolean secondOut = second.isBorrowed();    // false
            show("secondOut", secondOut);
            int pages = second.pages();                 // 320
            show("pages", pages);
        }
        {
            Book atlas = new Book("Atlas", 96);
            atlas.borrow();
            try { atlas.borrow();  } catch (Throwable _t) { System.out.println("-> " + _t); }
            try { Book empty = new Book("Blank", 0); show("empty", empty); } catch (Throwable _t) { System.out.println("empty -> " + _t); }
        }
        {
            Shelf shelf = new Shelf();
            int countValue = shelf.count;            // 0
            show("countValue", countValue);
            boolean fullValue = shelf.full;          // false
            show("fullValue", fullValue);
            String labelValue = shelf.label;         // null
            show("labelValue", labelValue);
        }
        {
            Book original = new Book("Ulysses", 730);
            Book alias = original;
            alias.borrow();
            boolean seenByOriginal = original.isBorrowed();    // true
            show("seenByOriginal", seenByOriginal);
            boolean sameObject = alias == original;            // true
            show("sameObject", sameObject);
        }
        {
            Book missing = null;
            try { int missingPages = missing.pages(); show("missingPages", missingPages); } catch (Throwable _t) { System.out.println("missingPages -> " + _t); }
            int safePages = missing == null ? 0 : missing.pages();   // 0
            show("safePages", safePages);
        }
        {
            Book viaReflection = Book.class.getDeclaredConstructor(String.class, int.class).newInstance("Ode", 12);
            int reflectedPages = viaReflection.pages();         // 12
            show("reflectedPages", reflectedPages);
            LocalDate date = LocalDate.of(2026, 10, 10);        // 2026-10-10
            show("date", date);
        }
        {
            Visit morning = new Visit("Asha");
            Visit evening = new Visit("Ben");
            String order = Visit.LOG.toString();   // "[static block, instance block, constructor Asha, instance block, constructor Ben]"
            show("order", order);
        }
        {
            Author poet = new Author("Rumi", Genre.POETRY);
            String shown = poet.toString();               // "Author[name=Rumi, genre=POETRY]"
            show("shown", shown);
            Genre[] genres = Genre.values();              // [NOVEL, POETRY]
            show("genres", genres);
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
