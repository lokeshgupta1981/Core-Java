package com.howtodoinjava.puzzles;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;
import sun.misc.Unsafe;

/**
 * Examples for the tutorial "Create an Object Without the new Keyword in Java (7 Ways)".
 * https://howtodoinjava.com/java/puzzles/how-to-create-an-instance-of-any-class-without-using-new-keyword/
 */
public class CreateObjectWithoutNew {
    static class Book implements Cloneable, Serializable {
        static int constructorCalls = 0;
        String title;

        public Book() {
            this("untitled");
        }

        public Book(String title) {
            this.title = title;
            constructorCalls++;
        }

        static Book of(String title) {
            return new Book(title);
        }

        @Override
        public Book clone() {
            try {
                return (Book) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);      // cannot happen, Book is Cloneable
            }
        }

        @Override
        public String toString() {
            return title;
        }
    }
    static Book newBookViaHandle(String title) {
        try {
            MethodHandle ctor = MethodHandles.lookup()
                    .findConstructor(Book.class, MethodType.methodType(void.class, String.class));
            return (Book) ctor.invoke(title);
        } catch (Throwable t) {
            throw new IllegalStateException("cannot create Book", t);
        }
    }
    static <T extends Serializable> T roundTrip(T object) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(object);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            @SuppressWarnings("unchecked")
            T copy = (T) in.readObject();
            return copy;
        }
    }
    static interface Exporter {
        String export(List<String> rows);
    }
    static class CsvExporter implements Exporter {
        public CsvExporter() {
        }

        public String export(List<String> rows) {
            return String.join(",", rows);
        }
    }
    static enum Config { INSTANCE }
    public static void main(String[] args) throws Exception {
        {
            Book byReflection = Book.class.getDeclaredConstructor(String.class).newInstance("Dune");   // Dune
            show("byReflection", byReflection);
            Book byClone = byReflection.clone();                                                       // Dune
            show("byClone", byClone);
            Book byFactory = Book.of("Emma");                                                          // Emma
            show("byFactory", byFactory);
            Supplier<Book> maker = Book::new;
            Book bySupplier = maker.get();                                                             // untitled
            show("bySupplier", bySupplier);
        }
        {
            Book reflected = Book.class.getDeclaredConstructor().newInstance();       // untitled
            show("reflected", reflected);
            Object list = Class.forName("java.util.ArrayList").getDeclaredConstructor().newInstance();   // []
            show("list", list);
        }
        {
            Constructor<String> hidden = String.class.getDeclaredConstructor(byte[].class, byte.class);
            try { hidden.setAccessible(true);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Book viaHandle = newBookViaHandle("Ivanhoe");                            // Ivanhoe
            show("viaHandle", viaHandle);
        }
        {
            int callsBeforeClone = Book.constructorCalls;
            Book original = Book.of("Persuasion");
            Book cloned = original.clone();
            boolean differentObject = cloned != original;                           // true
            show("differentObject", differentObject);
            int cloneCalls = Book.constructorCalls - callsBeforeClone;              // 1, only Book.of() ran a constructor
            show("cloneCalls", cloneCalls);
        }
        {
            Book saved = Book.of("Middlemarch");
            int callsBeforeRead = Book.constructorCalls;
            Book restored = roundTrip(saved);                                       // Middlemarch
            show("restored", restored);
            int readCalls = Book.constructorCalls - callsBeforeRead;                // 0, no constructor ran
            show("readCalls", readCalls);
        }
        {
            List<String> genres = List.of("drama", "poetry");                      // [drama, poetry]
            show("genres", genres);
            LocalDate release = LocalDate.of(2026, 3, 14);                           // 2026-03-14
            show("release", release);
            boolean cached = Integer.valueOf(127) == Integer.valueOf(127);          // true, same cached object
            show("cached", cached);
            Optional<Book> maybe = Optional.of(Book.of("Emma"));                    // Optional[Emma]
            show("maybe", maybe);
        }
        {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            int callsBeforeUnsafe = Book.constructorCalls;
            Book raw = (Book) unsafe.allocateInstance(Book.class);                  // null, title never set
            show("raw", raw);
            int unsafeCalls = Book.constructorCalls - callsBeforeUnsafe;            // 0
            show("unsafeCalls", unsafeCalls);
        }
        {
            String literal = "Dune";                                                 // "Dune", no new in our code
            show("literal", literal);
            Integer boxed = 42;                                                      // 42, Integer.valueOf() runs for us
            show("boxed", boxed);
            int[] pages = {120, 240};                                                // [120, 240]
            show("pages", pages);
        }
        {
            String className = CsvExporter.class.getName();          // read from the configuration file in a real app
            show("className", className);
            Exporter exporter = Class.forName(className).asSubclass(Exporter.class).getDeclaredConstructor().newInstance();
            String csv = exporter.export(List.of("id", "title"));                    // "id,title"
            show("csv", csv);
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
