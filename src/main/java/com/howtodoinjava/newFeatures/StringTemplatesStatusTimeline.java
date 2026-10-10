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
import java.text.MessageFormat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Examples for the tutorial "Java String Templates: Withdrawn in JDK 23, What to Use Now".
 * https://howtodoinjava.com/java/java-string-templates/
 */
public class StringTemplatesStatusTimeline {
    static List<String> findDishes(Connection connection, String customer) throws SQLException {
        String sql = "SELECT dish FROM orders WHERE customer = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer);
            try (ResultSet rows = statement.executeQuery()) {
                List<String> found = new ArrayList<>();
                while (rows.next()) {
                    found.add(rows.getString("dish"));
                }
                return found;
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String name = "Alex";
            int items = 3;
            String formatted = "Hi %s, you have %d items".formatted(name, items);   // "Hi Alex, you have 3 items"
            show("formatted", formatted);
            String joined = "Hi " + name + ", you have " + items + " items";        // "Hi Alex, you have 3 items"
            show("joined", joined);
            String message = MessageFormat.format("Hi {0}, you have {1} items", name, items); // "Hi Alex, you have 3 items"
            show("message", message);
        }
        {
            String dish = "soup";
            double price = 4.5;
            String line = "%-8s|%6.2f|".formatted(dish, price);            // "soup    |  4.50|"
            show("line", line);
            String us = String.format(Locale.US, "%,.2f", 1234.5);          // "1,234.50"
            show("us", us);
            String de = String.format(Locale.GERMANY, "%,.2f", 1234.5);     // "1.234,50"
            show("de", de);
            try { String bad = "%d items".formatted("three"); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            List<String> dishes = List.of("soup", "salad", "bread");
            StringBuilder receipt = new StringBuilder("Order:");
            for (String item : dishes) {
                receipt.append(' ').append(item);
            }
            String text = receipt.toString();                                // "Order: soup salad bread"
            show("text", text);
            String list = String.join(", ", dishes);                        // "soup, salad, bread"
            show("list", list);
        }
        {
            String en = MessageFormat.format("{0} ordered {1} dishes", "Alex", 2);   // "Alex ordered 2 dishes"
            show("en", en);
            String fr = MessageFormat.format("{1} plats pour {0}", "Alex", 2);       // "2 plats pour Alex"
            show("fr", fr);
            String broken = MessageFormat.format("It's {0}", "Alex");               // "Its {0}"
            show("broken", broken);
            String fixed = MessageFormat.format("It''s {0}", "Alex");               // "It's Alex"
            show("fixed", fixed);
        }
        {
            String customer = "Alex";
            List<String> order = List.of("soup", "salad", "bread");
            String email = """
            Hi %s,
            your order of %d dishes (%s) is on its way.
            Total %.2f EUR
            """.formatted(customer, order.size(), String.join(", ", order), 13.5);
            long emailLines = email.lines().count();                          // 3
            show("emailLines", emailLines);
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
