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

/**
 * Examples for the tutorial "Java Constructors: Default, Parameterized, Chaining, Records".
 * https://howtodoinjava.com/java/oops/java-constructors/
 */
public class ConstructorExecutionOrder {
    static class Reservation {
        private final String guest;
        private final int partySize;

        Reservation(String guest, int partySize) {              // parameterized constructor
            if (partySize < 1) {
                throw new IllegalArgumentException("partySize must be positive");
            }
            this.guest = guest;
            this.partySize = partySize;
        }

        Reservation(String guest) {
            this(guest, 2);                                     // chains to the constructor above
        }

        String guest() { return guest; }
        int partySize() { return partySize; }
    }
    static class DiningTable {
        int seats = 4;

        void DiningTable() {        // a method, not a constructor
            seats = 8;
        }
    }
    static class Menu {
        String title = "Dinner";
    }
    static class TableBooking {
        final List<String> steps = new ArrayList<>();

        TableBooking(String guest) {
            steps.add("TableBooking(guest)");
        }
    }
    static class EventBooking extends TableBooking {
        EventBooking(String guest, String occasion) {
            super(guest);
            steps.add("EventBooking(guest, occasion)");
        }

        EventBooking(String guest) {
            this(guest, "birthday");
            steps.add("EventBooking(guest)");
        }
    }
    static class Notice {
        final String text;

        Notice() {
            text = describe();
        }

        String describe() { return "table booked"; }
    }
    static record GuestName(String value) {
        GuestName {
            value = value.strip();
        }
    }
    static class TablePlan {
        private final List<String> guests;

        TablePlan(List<String> guests) {
            this.guests = new ArrayList<>(guests);
        }

        TablePlan(TablePlan other) {                // copy constructor
            this(other.guests);
        }

        void add(String guest) { guests.add(guest); }
        int size() { return guests.size(); }
    }
    static final class TipCalculator {
        private TipCalculator() { }                 // no instances

        static double tip(double bill, int percent) {
            return bill * percent / 100;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int seats = new DiningTable().seats;       // 4, the "constructor" never ran
            show("seats", seats);
        }
        {
            Menu menu = new Menu();
            String title = menu.title;                  // "Dinner"
            show("title", title);
        }
        {
            Reservation dinner = new Reservation("Lokesh", 4);
            int dinnerSize = dinner.partySize();        // 4
            show("dinnerSize", dinnerSize);
            Reservation couple = new Reservation("Alex");
            int coupleSize = couple.partySize();        // 2
            show("coupleSize", coupleSize);
            try { Reservation empty = new Reservation("Sam", 0); show("empty", empty); } catch (Throwable _t) { System.out.println("empty -> " + _t); }
        }
        {
            List<String> order = new EventBooking("Mia").steps;  // [TableBooking(guest), EventBooking(guest, occasion), EventBooking(guest)]
            show("order", order);
        }
        {
            String cleaned = new GuestName("  Lokesh ").value();   // "Lokesh"
            show("cleaned", cleaned);
        }
        {
            TablePlan original = new TablePlan(List.of("Ana", "Ben"));
            TablePlan copy = new TablePlan(original);
            copy.add("Cy");
            int originalSize = original.size();         // 2
            show("originalSize", originalSize);
            int copySize = copy.size();                 // 3
            show("copySize", copySize);
        }
        {
            double tip = TipCalculator.tip(80.0, 15);   // 12.0
            show("tip", tip);
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
