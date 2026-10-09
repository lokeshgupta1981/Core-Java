package com.howtodoinjava.core.flowcontrol;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Labeled break and continue in Java (Nested Loops)".
 * https://howtodoinjava.com/java/flow-control/labeled-statements-in-java/
 */
public class LabeledStatements {
    static boolean allInStock(List<String> items, Map<String, Integer> stock) {
        for (String item : items) {
            if (stock.getOrDefault(item, 0) == 0) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) throws Exception {
        {
            boolean[][] booked = {
                {true, true, false},
                {false, false, true}
            };
            int row = -1;
            int seat = -1;
            search:
            for (int r = 0; r < booked.length; r++) {
                for (int s = 0; s < booked[r].length; s++) {
                    if (!booked[r][s]) {
                        row = r;
                        seat = s;
                        break search;               // ends both loops
                    }
                }
            }
            int freeRow = row;                      // 0
            show("freeRow", freeRow);
            int freeSeat = seat;                    // 2
            show("freeSeat", freeSeat);
        }
        {
            boolean[][] booked = {
                {true, false, true, false},
                {false, false, true, true},
                {false, false, false, true}
            };
            String pair = "none";
            for (int r = 0; r < booked.length; r++) {
                for (int s = 0; s + 1 < booked[r].length; s++) {
                    if (!booked[r][s] && !booked[r][s + 1]) {
                        pair = r + ":" + s;
                        break;                      // ends only the seat loop
                    }
                }
            }
            String chosen = pair;                   // "2:0", row 2 overwrote "1:0"
            show("chosen", chosen);
        }
        {
            boolean[][] booked = {
                {true, false, true, false},
                {false, false, true, true},
                {false, false, false, true}
            };
            String pair = "none";
            rows:
            for (int r = 0; r < booked.length; r++) {
                for (int s = 0; s + 1 < booked[r].length; s++) {
                    if (!booked[r][s] && !booked[r][s + 1]) {
                        pair = r + ":" + s;
                        break rows;                 // ends the row loop too
                    }
                }
            }
            String chosen = pair;                   // "1:0"
            show("chosen", chosen);
        }
        {
            Map<String, Integer> stock = Map.of("pen", 5, "ink", 0, "pad", 2);
            List<List<String>> orders = List.of(
            List.of("pen", "pad"),
            List.of("pen", "ink"),
            List.of("pad"));
            List<Integer> shippable = new ArrayList<>();
            orderLoop:
            for (int o = 0; o < orders.size(); o++) {
                for (String item : orders.get(o)) {
                    if (stock.getOrDefault(item, 0) == 0) {
                        continue orderLoop;         // skip this order
                    }
                }
                shippable.add(o);                   // runs only if no item was missing
            }
            String ready = shippable.toString();    // "[0, 2]"
            show("ready", ready);
        }
        {
            String email = "ann@example.com";
            String result = "unchecked";
            validation:
            {
                if (email.isBlank()) {
                    result = "empty";
                    break validation;
                }
                if (!email.contains("@")) {
                    result = "missing @";
                    break validation;
                }
                result = "ok";
            }
            String status = result;                 // "ok"
            show("status", status);
        }
        {
            int visits = 0;
            scan:
            for (int i = 0; i < 3; i++) {
                visits++;
                if (i == 1) {
                    break scan;
                }
            }
            scan:
            for (int j = 0; j < 2; j++) {
                visits++;                           // this label is never used
            }
            int total = visits;                     // 4
            show("total", total);
        }
        {
            Map<String, Integer> stock = Map.of("pen", 5, "ink", 0, "pad", 2);
            boolean firstOk = allInStock(List.of("pen", "pad"), stock);    // true
            show("firstOk", firstOk);
            boolean secondOk = allInStock(List.of("pen", "ink"), stock);   // false
            show("secondOk", secondOk);
            boolean viaStream = List.of("pen", "ink").stream().allMatch(i -> stock.getOrDefault(i, 0) > 0);   // false
            show("viaStream", viaStream);
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
