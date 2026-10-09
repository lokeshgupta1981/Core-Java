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


/**
 * Examples for the tutorial "this vs super in Java".
 * https://howtodoinjava.com/java/keywords/this-vs-super/
 * The flexible constructor body example (Java 25) is in the java-25-features module,
 * class com.howtodoinjava.java25.keywords.FlexibleConstructorBodies.
 */
public class ThisVsSuper {
    static class Employee {
        protected final String name;

        Employee(String name) {
            this.name = name;                // this.name is the field
        }

        String describe() {
            return name;
        }
    }

    static class Manager extends Employee {
        private final int teamSize;

        Manager(String name, int teamSize) {
            super(name);                     // runs Employee(String)
            this.teamSize = teamSize;
        }

        @Override
        String describe() {
            return super.describe() + " leads " + teamSize;   // extends the parent version
        }
    }
    static class Order {
        private final List<String> items = new ArrayList<>();
        private String note = "";

        Order add(String item) {
            items.add(item);
            return this;                     // allows chained calls
        }

        Order note(String note) {
            this.note = note;                // field vs parameter
            return this;
        }

        String summary() {
            return items + " " + note;
        }
    }
    static interface Greeter {
        default String greet() {
            return "Hello";
        }
    }

    static class PoliteGreeter implements Greeter {
        @Override
        public String greet() {
            return Greeter.super.greet() + ", welcome";
        }
    }
    static class Room {
        final String name;
        final int capacity;

        Room(String name) {
            this(name, 10);                  // delegates to the full constructor
        }

        Room(String name, int capacity) {
            this.name = name;
            this.capacity = capacity;
        }
    }

    public static void main(String[] args) throws Exception {
        {
            Employee lead = new Manager("Ann", 4);
            String text = lead.describe();          // "Ann leads 4"
            show("text", text);
        }
        {
            String summary = new Order().add("tea").add("cake").note("table 5").summary();   // "[tea, cake] table 5"
            show("summary", summary);
        }
        {
            String greeting = new PoliteGreeter().greet();   // "Hello, welcome"
            show("greeting", greeting);
        }
        {
            Room small = new Room("Focus");
            int seats = small.capacity;             // 10
            show("seats", seats);
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
