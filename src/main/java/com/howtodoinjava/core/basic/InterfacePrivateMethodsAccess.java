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
 * Examples for the tutorial "Private Methods in Interfaces in Java (with Examples)".
 * https://howtodoinjava.com/java9/java9-private-interface-methods/
 */
public class InterfacePrivateMethodsAccess {
    static interface Greeter {
        default String morning(String name) {
            return greet("Good morning", name);
        }

        default String evening(String name) {
            return greet("Good evening", name);
        }

        private String greet(String prefix, String name) {
            return prefix + ", " + name.strip() + "!";
        }
    }
    static interface CustomCalculator {
        default int addEvenNumbers(int... nums) {
            return add(n -> n % 2 == 0, nums);
        }

        default int addOddNumbers(int... nums) {
            return add(n -> n % 2 != 0, nums);
        }

        private int add(IntPredicate predicate, int... nums) {
            return IntStream.of(nums).filter(predicate).sum();
        }
    }
    static interface Temperature {
        static double toFahrenheit(double celsius) {
            return round(celsius * 9 / 5 + 32);
        }

        static double toCelsius(double fahrenheit) {
            return round((fahrenheit - 32) * 5 / 9);
        }

        private static double round(double value) {
            return Math.round(value * 10) / 10.0;
        }
    }
    static interface OrderNotifier {
        String send(String customer, String text);

        default String shipped(String customer, int orderNo) {
            return send(checked(customer), "Order " + format(orderNo) + " has shipped");
        }

        default String delivered(String customer, int orderNo) {
            return send(checked(customer), "Order " + format(orderNo) + " was delivered");
        }

        private String checked(String customer) {
            if (customer == null || customer.isBlank()) {
                throw new IllegalArgumentException("customer is required");
            }
            return customer.strip();
        }

        private static String format(int orderNo) {
            return "#" + String.format("%06d", orderNo);
        }
    }
    static class SmsNotifier implements OrderNotifier {
        public String send(String customer, String text) {
            return "SMS to " + customer + ": " + text;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Greeter greeter = new Greeter() {};
            String morning = greeter.morning(" Ana ");     // "Good morning, Ana!"
            show("morning", morning);
            String evening = greeter.evening("Ben");       // "Good evening, Ben!"
            show("evening", evening);
        }
        {
            CustomCalculator calc = new CustomCalculator() {};
            int evens = calc.addEvenNumbers(1, 2, 3, 4, 5, 6, 7, 8, 9);   // 20
            show("evens", evens);
            int odds = calc.addOddNumbers(1, 2, 3, 4, 5, 6, 7, 8, 9);     // 25
            show("odds", odds);
        }
        {
            double warm = Temperature.toFahrenheit(21.5);    // 70.7
            show("warm", warm);
            double cold = Temperature.toCelsius(40);          // 4.4
            show("cold", cold);
        }
        {
            OrderNotifier sms = new SmsNotifier();
            String shipped = sms.shipped("Ana", 4217);       // "SMS to Ana: Order #004217 has shipped"
            show("shipped", shipped);
            try { String missing = sms.delivered(" ", 4217); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
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
