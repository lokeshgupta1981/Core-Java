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
 * Examples for the tutorial "Java assert Keyword".
 * https://howtodoinjava.com/java/keywords/java-assert/
 */
public class AssertKeyword {
    static int discountedPrice(int price, int percent) {
        int discounted = price - price * percent / 100;
        assert discounted >= 0 && discounted <= price : "bad discount " + percent + "% on " + price;
        return discounted;
    }
    static String quarterName(int month) {
        return switch ((month - 1) / 3) {
            case 0 -> "Q1";
            case 1 -> "Q2";
            case 2 -> "Q3";
            case 3 -> "Q4";
            default -> throw new AssertionError("month out of range: " + month);
        };
    }
    public static void main(String[] args) throws Exception {
        {
            int sale = discountedPrice(200, 25);    // 150
            show("sale", sale);
        }
        {
            List<String> cart = new ArrayList<>(List.of("pen", "ink"));
            String item = "pen";

            // wrong: the item is removed only when assertions are enabled
            assert cart.remove(item);

            // right: the work happens always, only the check is optional
            boolean removed = cart.remove(item);
            assert removed : "item was not in the cart: " + item;
        }
        {
            String q = quarterName(8);              // "Q3"
            show("q", q);
        }
        {
            String outcome;
            try {
                throw new AssertionError("broken invariant");
            } catch (Exception e) {
                outcome = "caught as Exception";
            } catch (AssertionError e) {
                outcome = "caught as AssertionError";
            }
            try { String caught = outcome; show("caught", caught); } catch (Throwable _t) { System.out.println("caught -> " + _t); }
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
