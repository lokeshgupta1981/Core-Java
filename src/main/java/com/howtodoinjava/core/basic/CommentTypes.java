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
 * Examples for the tutorial "Java Comments: Single-Line, Multi-Line and Javadoc".
 * https://howtodoinjava.com/java/basics/java-comments/
 */
public class CommentTypes {
    static public class Cart {

        private double total;

        /**
         * Adds an item price to the cart total.
         *
         * @param price the item price, must not be negative
         * @return the new cart total
         * @throws IllegalArgumentException if {@code price} is negative
         */
        public double add(double price) {
            if (price < 0) {
                throw new IllegalArgumentException("price < 0");
            }
            total += price;
            return total;
        }
    }
    static public class Wishlist {

        private final List<String> items = new ArrayList<>();

        /// Adds an item to the wishlist.
        ///
        /// @param item the item name, must not be `null`
        public void add(String item) {
            items.add(Objects.requireNonNull(item));
        }

        /// Returns the number of items, or `0` for an empty wishlist.
        ///
        /// See [#add(String)] to add items.
        ///
        /// @return the item count
        public int size() {
            return items.size();
        }
    }
    static int countItems() {
        int total = 1;
        // reset the counter \u000a total = 0;
        return total;
    }
    public static void main(String[] args) throws Exception {
        {
            // a single-line comment on its own line
            int retries = 3;                // 3, followed by an end-of-line comment
            show("retries", retries);
            /* a multi-line comment
            that spans two lines */
            int timeoutSeconds = 30;        // 30
            show("timeoutSeconds", timeoutSeconds);
        }
        {
            // VAT rate for Germany, update when the law changes
            double vatRate = 0.19;                       // 0.19
            show("vatRate", vatRate);
            double gross = 100 * (1 + vatRate);          // 119.0
            show("gross", gross);
        }
        {
            /*
             * Prices from the old catalog are stored in cents,
             * so we convert them to euros before display.
             */
            long priceInCents = 1999;
            double priceInEuros = priceInCents / 100.0;    // 19.99
            show("priceInEuros", priceInEuros);
        }
        {
            /**
             * A shopping cart that keeps a running total of item prices.
             */
        }
        {
            Cart cart = new Cart();
            double afterBook = cart.add(12.5);         // 12.5
            show("afterBook", afterBook);
            double afterPen = cart.add(2.0);           // 14.5
            show("afterPen", afterPen);
            try { double refund = cart.add(-3); show("refund", refund); } catch (Throwable _t) { System.out.println("refund -> " + _t); }
        }
        {
            int counted = countItems();     // 0, the "comment" reset total
            show("counted", counted);
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
