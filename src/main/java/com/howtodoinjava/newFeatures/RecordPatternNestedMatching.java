package com.howtodoinjava.newFeatures;

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
 * Examples for the tutorial "Java Record Patterns: instanceof, switch and Nested Patterns".
 * https://howtodoinjava.com/java/record-patterns-and-pattern-matching/
 */
public class RecordPatternNestedMatching {
    static record Money(double amount, String currency) {}
    static String currencyOf(Object value) {
        if (!(value instanceof Money(var amount, var currency))) {
            return "none";
        }
        return currency;
    }
    static record Address(String city, String zip) {}
    static sealed interface OrderEvent permits Placed, Paid, Delivered, Cancelled {}
    static record Placed(String orderId, Address address) implements OrderEvent {}
    static record Paid(Money total, String method) implements OrderEvent {}
    static record Delivered(String orderId, int minutes) implements OrderEvent {}
    static record Cancelled(String orderId, String reason) implements OrderEvent {}
    static record Pair<A, B>(A first, B second) {}
    static String stockLine(Pair<String, Integer> pair) {
        return switch (pair) {
            case Pair(var item, var count) when count == 0 -> item + " sold out";
            case Pair(var item, var count) -> item + " x" + count;
        };
    }
    static String describe(OrderEvent event) {
        return switch (event) {
            case Placed(var id, Address(var city, var zip)) -> id + " placed for " + city;
            case Paid(Money(var amount, var currency), var method) when amount > 100 ->
            "large " + method + " payment " + amount + " " + currency;
            case Paid(Money(var amount, var currency), var method) -> method + " payment " + amount;
            case Delivered(var id, var minutes) when minutes > 45 -> id + " late by " + (minutes - 45) + " min";
            case Delivered(var id, var minutes) -> id + " delivered";
            case Cancelled(var id, var reason) -> id + " cancelled: " + reason;
        };
    }
    static record Reading(double celsius) {
        public double celsius() {
            if (Double.isNaN(celsius)) {
                throw new IllegalStateException("sensor offline");
            }
            return celsius;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Object value = new Money(120.0, "EUR");
            String text = "";
            if (value instanceof Money(double amount, String currency)) {
                text = amount + " " + currency;
            }
            String formatted = text;                                           // "120.0 EUR"
            show("formatted", formatted);
        }
        {
            Object payment = new Money(80.0, "USD");
            String before = "";
            if (payment instanceof Money m) {
                before = m.amount() + " " + m.currency();
            }
            String withTypePattern = before;                                   // "80.0 USD"
            show("withTypePattern", withTypePattern);
        }
        {
            Object paid = new Money(80.0, "USD");
            String after = "";
            if (paid instanceof Money(double amount, String currency) && amount > 50) {
                after = "large payment in " + currency;
            }
            String withRecordPattern = after;                                  // "large payment in USD"
            show("withRecordPattern", withRecordPattern);
        }
        {
            String euro = currencyOf(new Money(5.0, "EUR"));                  // "EUR"
            show("euro", euro);
            String none = currencyOf("five euros");                            // "none"
            show("none", none);
        }
        {
            Object event = new Paid(new Money(120.0, "EUR"), "card");
            String result = "";
            if (event instanceof Paid(Money(var amount, var currency), String method)) {
                result = method + " payment of " + amount + " " + currency;
            }
            String nested = result;                                            // "card payment of 120.0 EUR"
            show("nested", nested);
        }
        {
            String soldOut = stockLine(new Pair<>("soup", 0));                // "soup sold out"
            show("soldOut", soldOut);
            String inStock = stockLine(new Pair<>("cake", 4));                 // "cake x4"
            show("inStock", inStock);
        }
        {
            String placed = describe(new Placed("A-1", new Address("Pune", "411001"))); // "A-1 placed for Pune"
            show("placed", placed);
            String large = describe(new Paid(new Money(120.0, "EUR"), "card"));          // "large card payment 120.0 EUR"
            show("large", large);
            String small = describe(new Paid(new Money(20.0, "EUR"), "cash"));           // "cash payment 20.0"
            show("small", small);
            String late = describe(new Delivered("A-1", 60));                            // "A-1 late by 15 min"
            show("late", late);
            String cancelled = describe(new Cancelled("A-2", "no driver"));              // "A-2 cancelled: no driver"
            show("cancelled", cancelled);
        }
        {
            Object nothing = null;
            boolean matchesNull = nothing instanceof Money(var amount, var currency); // false
            show("matchesNull", matchesNull);
            try { String npe = describe(null); show("npe", npe); } catch (Throwable _t) { System.out.println("npe -> " + _t); }
        }
        {
            Paid unpaid = new Paid(null, "card");
            boolean nestedMatch = unpaid instanceof Paid(Money(var amount, var currency), var method); // false
            show("nestedMatch", nestedMatch);
            boolean typeMatch = unpaid instanceof Paid(Money total, var method);                       // true
            show("typeMatch", typeMatch);
            try { String broken = describe(unpaid); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            Object reading = new Reading(Double.NaN);
            try { boolean warm = reading instanceof Reading(double c) && c > 20; show("warm", warm); } catch (Throwable _t) { System.out.println("warm -> " + _t); }
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
