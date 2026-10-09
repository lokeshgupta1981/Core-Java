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
 * Examples for the tutorial "Java break Statement: Loops, switch and Nested Loops".
 * https://howtodoinjava.com/java/flow-control/break-keyword/
 */
public class BreakStatement {
    static int findShelf(String[][] shelves, String wanted) {
        for (int s = 0; s < shelves.length; s++) {
            for (String item : shelves[s]) {
                if (item.equals(wanted)) {
                    return s;
                }
            }
        }
        return -1;
    }
    public static void main(String[] args) throws Exception {
        {
            int[] temps = {21, 23, 38, 22, 40};
            int firstHot = -1;
            for (int i = 0; i < temps.length; i++) {
                if (temps[i] > 35) {
                    firstHot = i;
                    break;                          // 22 and 40 are never checked
                }
            }
            int index = firstHot;                   // 2
            show("index", index);
        }
        {
            int balance = 100;
            int withdrawals = 0;
            while (true) {
                if (balance < 30) {
                    break;                          // not enough money left
                }
                balance -= 30;
                withdrawals++;
            }
            int done = withdrawals;                 // 3
            show("done", done);
            int left = balance;                     // 10
            show("left", left);
        }
        {
            int volume = 7;
            do {
                volume--;
                if (volume == 4) {
                    break;                          // stop at the comfort level
                }
            } while (volume > 0);
            int finalVolume = volume;               // 4
            show("finalVolume", finalVolume);
        }
        {
            String plan = "pro";
            List<String> features = new ArrayList<>();
            switch (plan) {
                case "pro":
                features.add("reports");        // no break, falls through
                case "basic":
                features.add("email");
                break;
                default:
                features.add("none");
            }
            String granted = features.toString();   // "[reports, email]"
            show("granted", granted);
        }
        {
            String tier = "basic";
            int seats = switch (tier) {
                case "pro" -> 10;
                case "basic" -> 3;
                default -> 1;
            };
            int seatCount = seats;                  // 3
            show("seatCount", seatCount);
        }
        {
            String[] commands = {"add", "stop", "add"};
            int added = 0;
            for (String command : commands) {
                switch (command) {
                    case "add":
                    added++;
                    break;
                    case "stop":
                    break;                      // leaves the switch, not the loop
                }
            }
            int addedCount = added;                 // 2
            show("addedCount", addedCount);
        }
        {
            String[] commands = {"add", "stop", "add"};
            int added = 0;
            loop:
            for (String command : commands) {
                switch (command) {
                    case "add" -> added++;
                    case "stop" -> {
                        break loop;
                    }
                    default -> { }
                }
            }
            int addedCount = added;                 // 1
            show("addedCount", addedCount);
        }
        {
            String[][] shelves = {{"bolt", "nut"}, {"screw", "washer"}, {"hinge", "nail"}};
            int foundShelf = -1;
            int checks = 0;
            for (int s = 0; s < shelves.length; s++) {
                for (String item : shelves[s]) {
                    checks++;
                    if (item.equals("screw")) {
                        foundShelf = s;
                        break;                      // ends only the inner loop
                    }
                }
            }
            int shelf = foundShelf;                 // 1
            show("shelf", shelf);
            int comparisons = checks;               // 5, shelf 2 was searched too
            show("comparisons", comparisons);
        }
        {
            String[][] shelves = {{"bolt", "nut"}, {"screw", "washer"}, {"hinge", "nail"}};
            int foundShelf = -1;
            int checks = 0;
            search:
            for (int s = 0; s < shelves.length; s++) {
                for (String item : shelves[s]) {
                    checks++;
                    if (item.equals("screw")) {
                        foundShelf = s;
                        break search;               // ends both loops
                    }
                }
            }
            int shelf = foundShelf;                 // 1
            show("shelf", shelf);
            int comparisons = checks;               // 3
            show("comparisons", comparisons);
        }
        {
            String[][] shelves = {{"bolt", "nut"}, {"screw", "washer"}, {"hinge", "nail"}};
            int screwShelf = findShelf(shelves, "screw");   // 1
            show("screwShelf", screwShelf);
            int glueShelf = findShelf(shelves, "glue");     // -1
            show("glueShelf", glueShelf);
        }
        {
            List<String> steps = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                try {
                    if (i == 1) {
                        break;
                    }
                    steps.add("work " + i);
                } finally {
                    steps.add("cleanup " + i);
                }
            }
            String trace = steps.toString();        // "[work 0, cleanup 0, cleanup 1]"
            show("trace", trace);
        }
        {
            List<String> log = List.of("INFO start", "WARN slow query", "ERROR db down", "INFO retry");
            String firstError = null;
            int read = 0;
            for (String line : log) {
                read++;
                if (line.startsWith("ERROR")) {
                    firstError = line;
                    break;
                }
            }
            String error = firstError;              // "ERROR db down"
            show("error", error);
            int linesRead = read;                   // 3
            show("linesRead", linesRead);
        }
        {
            List<String> log = List.of("INFO start", "WARN slow query", "ERROR db down", "INFO retry");
            Optional<String> first = log.stream().filter(l -> l.startsWith("ERROR")).findFirst();   // Optional[ERROR db down]
            show("first", first);
            boolean hasError = log.stream().anyMatch(l -> l.startsWith("ERROR"));                 // true
            show("hasError", hasError);
            List<String> beforeError = log.stream().takeWhile(l -> !l.startsWith("ERROR")).toList();   // [INFO start, WARN slow query]
            show("beforeError", beforeError);
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
