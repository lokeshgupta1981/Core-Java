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
 * Examples for the tutorial "Polymorphism in Java: Overloading, Overriding, Examples".
 * https://howtodoinjava.com/java/oops/what-is-polymorphism-in-java/
 */
public class PolymorphismDynamicDispatch {
    static class Ride {
        int fare(int km) {
            return 200 + km * 100;                    // cents
        }
    }

    static class CarRide extends Ride {
        @Override
        int fare(int km) {
            return 300 + km * 150;
        }
    }

    static class BikeRide extends Ride {
        @Override
        int fare(int km) {
            return 100 + km * 50;
        }
    }
    static String receipt(int cents) {
        return receipt(cents, "USD");
    }

    static String receipt(int cents, String currency) {
        return currency + " " + cents / 100 + "." + String.format("%02d", cents % 100);
    }

    static String receipt(List<Integer> items) {
        int total = 0;
        for (int item : items) {
            total += item;
        }
        return receipt(total);
    }
    static String kind(long value) {
        return "long";
    }

    static String kind(Integer value) {
        return "Integer";
    }

    static String kind(Object value) {
        return "Object";
    }
    static class ScooterRide extends Ride {
        @Override
        int fare(int km) {
            return 150 + km * 80;
        }
    }
    static interface PriceRule {
        int apply(int cents);
    }
    static class Fleet {
        String name = "fleet";

        static String category() {
            return "general";
        }

        String label() {
            return "fleet label";
        }
    }

    static class TaxiFleet extends Fleet {
        String name = "taxi";

        static String category() {
            return "taxi";
        }

        @Override
        String label() {
            return "taxi label";
        }
    }
    static sealed interface Discount permits PercentOff, FlatOff, NoDiscount {
    }

    static record PercentOff(int percent) implements Discount {
    }

    static record FlatOff(int cents) implements Discount {
    }

    static record NoDiscount() implements Discount {
    }
    static int discounted(int cents, Discount discount) {
        return switch (discount) {
            case PercentOff p -> cents - cents * p.percent() / 100;
            case FlatOff f -> Math.max(0, cents - f.cents());
            case NoDiscount n -> cents;
        };
    }
    public static void main(String[] args) throws Exception {
        {
            Ride first = new CarRide();
            Ride second = new BikeRide();
            int carFare = first.fare(10);                     // 1800
            show("carFare", carFare);
            int bikeFare = second.fare(10);                   // 600
            show("bikeFare", bikeFare);
        }
        {
            String single = receipt(1850);                    // "USD 18.50"
            show("single", single);
            String euros = receipt(600, "EUR");               // "EUR 6.00"
            show("euros", euros);
            String trip = receipt(List.of(1800, 250));        // "USD 20.50"
            show("trip", trip);
        }
        {
            String widened = kind(7);                         // "long"
            show("widened", widened);
            String boxed = kind(Integer.valueOf(7));          // "Integer"
            show("boxed", boxed);
            Object declaredAsObject = 7;
            String byStaticType = kind(declaredAsObject);     // "Object"
            show("byStaticType", byStaticType);
        }
        {
            List<Ride> rides = List.of(new CarRide(), new BikeRide(), new ScooterRide(), new Ride());
            int total = rides.stream().mapToInt(ride -> ride.fare(10)).sum();  // 4550
            show("total", total);
        }
        {
            PriceRule surge = cents -> cents * 3 / 2;
            PriceRule airportFee = cents -> cents + 500;
            int night = surge.apply(1800);                    // 2700
            show("night", night);
            int airport = airportFee.apply(1800);             // 2300
            show("airport", airport);
        }
        {
            Fleet fleet = new TaxiFleet();
            String fieldValue = fleet.name;                   // "fleet"
            show("fieldValue", fieldValue);
            String staticValue = fleet.category();            // "general"
            show("staticValue", staticValue);
            String methodValue = fleet.label();               // "taxi label"
            show("methodValue", methodValue);
        }
        {
            int tenPercent = discounted(1800, new PercentOff(10));  // 1620
            show("tenPercent", tenPercent);
            int flat = discounted(1800, new FlatOff(500));          // 1300
            show("flat", flat);
            int capped = discounted(1800, new FlatOff(5000));       // 0
            show("capped", capped);
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
