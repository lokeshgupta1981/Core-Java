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
import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

/**
 * Examples for the tutorial "static Keyword in Java".
 * https://howtodoinjava.com/java/keywords/java-static-keyword/
 */
public class StaticKeyword {
    static class Ticket {
        static int created = 0;              // one copy for the class
        final int number;                    // one copy per object

        Ticket() {
            created++;
            number = created;
        }
    }
    static class Limits {
        static final int MAX_USERS = 500;
        static final List<String> ADMINS = List.of("root", "lokesh");
    }
    static class Temperature {
        final double celsius;

        Temperature(double celsius) {
            this.celsius = celsius;
        }

        static Temperature ofFahrenheit(double f) {   // static factory method
            return new Temperature((f - 32) * 5 / 9);
        }

        static double average(List<Temperature> readings) {
            return readings.stream().mapToDouble(t -> t.celsius).average().orElse(Double.NaN);
        }
    }
    static class CountryCodes {
        static final Map<String, String> NAMES = new HashMap<>();
        static final List<String> LOG = new ArrayList<>();

        static {
            NAMES.put("IN", "India");
            NAMES.put("DE", "Germany");
            LOG.add("initialized");
        }
    }
    static class Pizza {
        private final String size;
        private final List<String> toppings;

        private Pizza(Builder b) {
            size = b.size;
            toppings = List.copyOf(b.toppings);
        }

        String describe() {
            return size + " " + toppings;
        }

        static class Builder {
            private String size = "medium";
            private final List<String> toppings = new ArrayList<>();

            Builder size(String size) {
                this.size = size;
                return this;
            }

            Builder topping(String topping) {
                toppings.add(topping);
                return this;
            }

            Pizza build() {
                return new Pizza(this);
            }
        }
    }
    static class Animal {
        static String kind() {
            return "animal";
        }

        String name() {
            return "animal";
        }
    }

    static class Dog extends Animal {
        static String kind() {
            return "dog";
        }

        @Override
        String name() {
            return "dog";
        }
    }
    static class SafeTicket {
        static final AtomicInteger CREATED = new AtomicInteger();
        final int number = CREATED.incrementAndGet();
    }
    public static void main(String[] args) throws Exception {
        {
            Ticket first = new Ticket();
            Ticket second = new Ticket();
            int total = Ticket.created;             // 2
            show("total", total);
            int secondNumber = second.number;       // 2
            show("secondNumber", secondNumber);
        }
        {
            int max = Limits.MAX_USERS;             // 500
            show("max", max);
            boolean isAdmin = Limits.ADMINS.contains("root");   // true
            show("isAdmin", isAdmin);
        }
        {
            Temperature boiling = Temperature.ofFahrenheit(212);
            double celsius = boiling.celsius;       // 100.0
            show("celsius", celsius);
            double avg = Temperature.average(List.of(new Temperature(10), new Temperature(20)));   // 15.0
            show("avg", avg);
        }
        {
            String india = CountryCodes.NAMES.get("IN");   // "India"
            show("india", india);
            int runs = CountryCodes.LOG.size();             // 1
            show("runs", runs);
        }
        {
            String pizza = new Pizza.Builder().size("large").topping("olives").build().describe();   // "large [olives]"
            show("pizza", pizza);
        }
        {
            double circumference = 2 * PI * 3;      // 18.84955592153876
            show("circumference", circumference);
            double side = sqrt(49);                 // 7.0
            show("side", side);
        }
        {
            Animal pet = new Dog();
            String instanceCall = pet.name();       // "dog", overriding
            show("instanceCall", instanceCall);
            String staticCall = Animal.kind();      // "animal", hiding
            show("staticCall", staticCall);
        }
        {
            SafeTicket a = new SafeTicket();
            SafeTicket b = new SafeTicket();
            int last = b.number;                    // 2
            show("last", last);
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
