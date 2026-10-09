package com.howtodoinjava.core.keywords;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.io.Serial;

/**
 * Examples for the tutorial "Java transient Keyword".
 * https://howtodoinjava.com/java/keywords/transient-keyword-in-java-with-real-time-example/
 */
public class TransientKeyword {
    static class Session implements Serializable {
        final String user;
        transient Map<String, String> cache = new HashMap<>();

        Session(String user) {
            this.user = user;
        }
    }
    @SuppressWarnings("unchecked")
    static <T> T roundTrip(T object) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(object);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            return (T) in.readObject();
        }
    }
    static class Cart implements Serializable {
        private final List<Integer> prices = new ArrayList<>();
        private transient int total;         // derived from prices

        void add(int price) {
            prices.add(price);
            total += price;
        }

        int total() {
            return total;
        }

        @Serial
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();          // reads prices
            total = prices.stream().mapToInt(Integer::intValue).sum();
        }
    }
    static class Report implements Serializable {
        final transient String title = "Sales";              // constant, inlined by javac
        final transient List<String> lines = new ArrayList<>();   // not a constant
    }
    public static void main(String[] args) throws Exception {
        {
            Session session = new Session("lokesh");
            session.cache.put("theme", "dark");
            Session restored = roundTrip(session);
            String user = restored.user;            // "lokesh"
            show("user", user);
            Map<String, String> cache = restored.cache;   // null
            show("cache", cache);
        }
        {
            Cart cart = new Cart();
            cart.add(30);
            cart.add(12);
            int restoredTotal = roundTrip(cart).total();   // 42
            show("restoredTotal", restoredTotal);
        }
        {
            Report copy = roundTrip(new Report());
            String title = copy.title;              // "Sales"
            show("title", title);
            List<String> lines = copy.lines;        // null
            show("lines", lines);
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
