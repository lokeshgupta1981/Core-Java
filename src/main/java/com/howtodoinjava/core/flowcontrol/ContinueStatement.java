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
 * Examples for the tutorial "Java continue Statement with Examples and Pitfalls".
 * https://howtodoinjava.com/java/flow-control/continue-keyword-statement-in-java/
 */
public class ContinueStatement {
    static record Order(String status, double total) {}
    static double billableNested(List<Order> orders) {
        double sum = 0;
        for (Order order : orders) {
            if (!order.status().equals("cancelled")) {
                if (order.total() > 0) {
                    sum += order.total();
                }
            }
        }
        return sum;
    }
    static double billable(List<Order> orders) {
        double sum = 0;
        for (Order order : orders) {
            if (order.status().equals("cancelled")) {
                continue;
            }
            if (order.total() <= 0) {
                continue;
            }
            sum += order.total();
        }
        return sum;
    }
    static int revenueHangs(int[] amounts) {
        int total = 0;
        int i = 0;
        while (i < amounts.length) {
            if (amounts[i] <= 0) {
                continue;                   // i++ below is skipped, i never changes
            }
            total += amounts[i];
            i++;
        }
        return total;
    }
    static int importStock(List<String> rows, Map<String, Integer> stock) {
        int skipped = 0;
        for (String row : rows) {
            if (row.isBlank() || row.startsWith("#")) {
                continue;                   // not data, not counted
            }
            String[] parts = row.split(",");
            if (parts.length != 2) {
                skipped++;
                continue;
            }
            try {
                stock.put(parts[0].strip(), Integer.parseInt(parts[1].strip()));
            } catch (NumberFormatException e) {
                skipped++;
            }
        }
        return skipped;
    }
    public static void main(String[] args) throws Exception {
        {
            String[] rows = {"apple,3", "", "# fruit stock", "kiwi,5"};
            int imported = 0;
            for (String row : rows) {
                if (row.isBlank() || row.startsWith("#")) {
                    continue;                       // skip blank and comment rows
                }
                imported++;
            }
            int importedRows = imported;            // 2
            show("importedRows", importedRows);
        }
        {
            int[] amounts = {40, -10, 25, 0, 15};
            int total = 0;
            for (int i = 0; i < amounts.length; i++) {
                if (amounts[i] <= 0) {
                    continue;                       // i++ still runs
                }
                total += amounts[i];
            }
            int revenue = total;                    // 80
            show("revenue", revenue);
        }
        {
            List<Order> orders = List.of(new Order("paid", 20.0), new Order("cancelled", 50.0),
            new Order("paid", 0.0), new Order("paid", 12.5));
            double nested = billableNested(orders);  // 32.5
            show("nested", nested);
            double guarded = billable(orders);       // 32.5
            show("guarded", guarded);
        }
        {
            int[] amounts = {40, -10, 25, 0, 15};
            int total = 0;
            int i = 0;
            while (i < amounts.length) {
                int amount = amounts[i];
                i++;                                // update before any continue
                if (amount <= 0) {
                    continue;
                }
                total += amount;
            }
            int revenue = total;                    // 80
            show("revenue", revenue);
        }
        {
            int[][] hours = {{8, 7, 9}, {6, -1, 8}, {5, 5, 5}};
            int counted = 0;
            for (int[] week : hours) {
                for (int h : week) {
                    if (h < 0) {
                        continue;                   // skips only the -1 entry
                    }
                    counted += h;
                }
            }
            int totalHours = counted;               // 53
            show("totalHours", totalHours);
        }
        {
            int[][] hours = {{8, 7, 9}, {6, -1, 8}, {5, 5, 5}};
            int counted = 0;
            rows:
            for (int[] week : hours) {
                int weekSum = 0;
                for (int h : week) {
                    if (h < 0) {
                        continue rows;              // skips the whole timesheet
                    }
                    weekSum += h;
                }
                counted += weekSum;
            }
            int totalHours = counted;               // 39
            show("totalHours", totalHours);
        }
        {
            String[] events = {"click", "noise", "buy", "click"};
            int tracked = 0;
            for (String event : events) {
                switch (event) {
                    case "noise":
                    continue;                   // next event, tracked++ is skipped
                    default:
                    break;                      // leaves only the switch
                }
                tracked++;
            }
            int trackedEvents = tracked;            // 3
            show("trackedEvents", trackedEvents);
        }
        {
            List<String> tags = List.of("java", "", "loops");
            List<String> kept = new ArrayList<>();
            tags.forEach(tag -> {
                if (tag.isEmpty()) {
                    return;                         // works like continue
                }
                kept.add(tag);
            });
            String result = kept.toString();        // "[java, loops]"
            show("result", result);
        }
        {
            List<String> rows = List.of("# name,qty", "apple, 3", "", "pear", "kiwi,five", "plum,7");
            Map<String, Integer> stock = new LinkedHashMap<>();
            int skipped = importStock(rows, stock);  // 2
            show("skipped", skipped);
            String loaded = stock.toString();        // "{apple=3, plum=7}"
            show("loaded", loaded);
        }
        {
            List<String> rows = List.of("# name,qty", "apple, 3", "", "plum,7");
            long dataRows = rows.stream().filter(r -> !r.isBlank() && !r.startsWith("#")).count();   // 2
            show("dataRows", dataRows);
        }
        {
            int[] values = {1, 2, 3, 4};
            int sumBreak = 0;
            for (int v : values) {
                if (v == 3) {
                    break;
                }
                sumBreak += v;
            }
            int sumContinue = 0;
            for (int v : values) {
                if (v == 3) {
                    continue;
                }
                sumContinue += v;
            }
            int withBreak = sumBreak;               // 3, stops before 3
            show("withBreak", withBreak);
            int withContinue = sumContinue;         // 7, skips only 3
            show("withContinue", withContinue);
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
