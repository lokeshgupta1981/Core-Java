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
 * Examples for the tutorial "Method Overloading vs Overriding in Java: Rules and Examples".
 * https://howtodoinjava.com/java/oops/method-overloading-overriding/
 */
public class MethodOverloadingVsOverriding {
    static class Fares {
        static double price(int tickets) {
            return tickets * 12.0;
        }

        static double price(int tickets, int discountPercent) {
            return price(tickets) * (100 - discountPercent) / 100;
        }

        static double price(String coupon, int tickets) {
            return coupon.equals("HALF") ? price(tickets, 50) : price(tickets);
        }
    }
    static String kind(long value) {
        return "long";
    }

    static String kind(Integer value) {
        return "Integer";
    }

    static String kind(int... values) {
        return "varargs";
    }

    static String kind(Object value) {
        return "Object";
    }
    static class Ticket {
        double basePrice() {
            return 12.0;
        }

        String label() {
            return getClass().getSimpleName() + " " + basePrice();
        }
    }
    static class StudentTicket extends Ticket {
        @Override
        double basePrice() {
            return super.basePrice() * 0.5;
        }
    }
    static class Booking {
        Ticket ticket() { return new Ticket(); }
    }

    static class StudentBooking extends Booking {
        @Override
        StudentTicket ticket() { return new StudentTicket(); }
    }
    static class SeatMap {
        String load(String hall) throws IOException { return hall + " loaded"; }
    }

    static class CachedSeatMap extends SeatMap {
        @Override
        String load(String hall) { return hall + " from cache"; }  // throws nothing
    }
    static class Seat {
        final String code;
        Seat(String code) { this.code = code; }

        public boolean equals(Seat other) {         // overload, not override
            return other != null && code.equals(other.code);
        }
    }
    static class Printer {
        String describe(Object item) { return "Printer prints an object"; }
        String describe(String item) { return "Printer prints text"; }
    }

    static class LabelPrinter extends Printer {
        @Override
        String describe(Object item) { return "LabelPrinter prints an object"; }
    }
    public static void main(String[] args) throws Exception {
        {
            String fromInt = String.valueOf(42);                    // "42"
            show("fromInt", fromInt);
            String fromChars = String.valueOf(new char[] {'h', 'i'}); // "hi"
            show("fromChars", fromChars);
            Object drinks = List.of("tea", "milk");
            String text = drinks.toString();                        // "[tea, milk]"
            show("text", text);
        }
        {
            double two = Fares.price(2);               // 24.0
            show("two", two);
            double reduced = Fares.price(2, 25);       // 18.0
            show("reduced", reduced);
            double coupon = Fares.price("HALF", 4);    // 24.0
            show("coupon", coupon);
        }
        {
            String widened = kind(5);                  // "long"
            show("widened", widened);
            String boxed = kind(Integer.valueOf(5));   // "Integer"
            show("boxed", boxed);
            String text = kind("five");                // "Object"
            show("text", text);
            String none = kind();                      // "varargs"
            show("none", none);
        }
        {
            List<Integer> seats = new ArrayList<>(List.of(10, 20, 30));
            Integer removedAt = seats.remove(1);               // 20, removes index 1
            show("removedAt", removedAt);
            boolean removed = seats.remove(Integer.valueOf(10)); // true, removes the value 10
            show("removed", removed);
            List<Integer> left = seats;                        // [30]
            show("left", left);
        }
        {
            Ticket regular = new Ticket();
            Ticket student = new StudentTicket();
            double full = regular.basePrice();         // 12.0
            show("full", full);
            double half = student.basePrice();         // 6.0
            show("half", half);
            String tag = student.label();              // "StudentTicket 6.0"
            show("tag", tag);
        }
        {
            StudentTicket cheap = new StudentBooking().ticket();  // a StudentTicket, no cast
            show("cheap", cheap);
            double cheapPrice = cheap.basePrice();                // 6.0
            show("cheapPrice", cheapPrice);
        }
        {
            SeatMap map = new CachedSeatMap();
            String loaded = map.load("A");             // "A from cache"
            show("loaded", loaded);
        }
        {
            Seat seat = new Seat("A1");
            boolean typed = seat.equals(new Seat("A1"));            // true
            show("typed", typed);
            Object asObject = new Seat("A1");
            boolean viaObject = seat.equals(asObject);              // false
            show("viaObject", viaObject);
            boolean inList = List.of(new Seat("A1")).contains(seat); // false
            show("inList", inList);
        }
        {
            Printer printer = new LabelPrinter();
            Object item = "ticket";
            String first = printer.describe(item);     // "LabelPrinter prints an object"
            show("first", first);
            String second = printer.describe("ticket"); // "Printer prints text"
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
