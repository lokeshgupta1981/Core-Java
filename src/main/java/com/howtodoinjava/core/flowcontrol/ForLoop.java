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
 * Examples for the tutorial "Java for Loop: Syntax, Examples and Common Mistakes".
 * https://howtodoinjava.com/java/flow-control/for-loop-in-java/
 */
public class ForLoop {
    static boolean isPalindrome(String text) {
        for (int left = 0, right = text.length() - 1; left < right; left++, right--) {
            if (text.charAt(left) != text.charAt(right)) {
                return false;
            }
        }
        return true;
    }
    static List<Integer> batchSizes(List<String> emails, int batchSize) {
        List<Integer> sizes = new ArrayList<>();
        for (int start = 0; start < emails.size(); start += batchSize) {
            int end = Math.min(start + batchSize, emails.size());
            List<String> batch = emails.subList(start, end);
            sizes.add(batch.size());                       // send the batch here
        }
        return sizes;
    }
    static int firstGoalDay(int[] steps, int goal) {
        int found = -1;
        for (int i = 0; i < steps.length; i++) {
            if (steps[i] >= goal) {
                found = i;
                break;                     // stop at the first match
            }
        }
        return found;                      // i is out of scope here
    }
    static record Category(String name, Category parent) {}
    static int sumWrong(int[] values) {
        int sum = 0;
        for (int i = 0; i <= values.length; i++) {   // should be i < values.length
            sum += values[i];
        }
        return sum;
    }
    public static void main(String[] args) throws Exception {
        {
            int[] steps = {8000, 10500, 6200, 9300};
            int sum = 0;
            for (int i = 0; i < steps.length; i++) {
                sum += steps[i];
            }
            double average = sum / (double) steps.length;   // 8500.0
            show("average", average);
        }
        {
            StringBuilder countdown = new StringBuilder();
            for (int i = 3; i > 0; i--) {
                countdown.append(i).append(' ');
            }
            String launch = countdown.toString().strip();      // "3 2 1"
            show("launch", launch);
            StringBuilder evens = new StringBuilder();
            for (var i = 0; i <= 8; i += 2) {
                evens.append(i);
            }
            String evenDigits = evens.toString();              // "02468"
            show("evenDigits", evenDigits);
        }
        {
            boolean level = isPalindrome("level");   // true
            show("level", level);
            boolean java = isPalindrome("java");     // false
            show("java", java);
        }
        {
            List<String> subscribers = Collections.nCopies(250, "user@example.com");
            List<Integer> requests = batchSizes(subscribers, 100);   // [100, 100, 50]
            show("requests", requests);
        }
        {
            int day = firstGoalDay(new int[] {8000, 10500, 6200, 12000}, 10000);   // 1
            show("day", day);
            int none = firstGoalDay(new int[] {8000, 6200}, 10000);                // -1
            show("none", none);
        }
        {
            Category root = new Category("Home", null);
            Category shoes = new Category("Shoes", new Category("Men", root));
            List<String> path = new ArrayList<>();
            for (Category c = shoes; c != null; c = c.parent()) {
                path.addFirst(c.name());
            }
            String breadcrumb = String.join(" > ", path);   // "Home > Men > Shoes"
            show("breadcrumb", breadcrumb);
        }
        {
            boolean[][] booked = {
                {true, true, false},
                {false, false, false},
                {true, false, true}
            };
            int free = 0;
            for (int row = 0; row < booked.length; row++) {
                for (int seat = 0; seat < booked[row].length; seat++) {
                    if (!booked[row][seat]) {
                        free++;
                    }
                }
            }
            int freeSeats = free;                       // 5
            show("freeSeats", freeSeats);
        }
        {
            try { int broken = sumWrong(new int[] {8000, 10500, 6200}); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            List<Integer> daily = new ArrayList<>(List.of(12000, 13000, 4000, 15000));
            for (int i = 0; i < daily.size(); i++) {
                if (daily.get(i) > 10000) {
                    daily.remove(i);
                }
            }
            List<Integer> skipped = daily;                     // [13000, 4000], 13000 was never checked
            show("skipped", skipped);
            List<Integer> safe = new ArrayList<>(List.of(12000, 13000, 4000, 15000));
            for (int i = safe.size() - 1; i >= 0; i--) {
                if (safe.get(i) > 10000) {
                    safe.remove(i);
                }
            }
            List<Integer> fixed = safe;                        // [4000]
            show("fixed", fixed);
        }
        {
            double total = 0.0;
            for (int i = 0; i < 10; i++) {
                total += 0.1;
            }
            boolean exact = total == 1.0;                       // false, total is 0.9999999999999999
            show("exact", exact);
        }
        {
            int count = 0;
            for (int i = 0; i < 3; i++);                        // empty body
            {
                count++;
            }
            int runs = count;                                  // 1, not 3
            show("runs", runs);
        }
        {
            int sumOfFirst = IntStream.range(0, 4).sum();          // 6, same as 0 + 1 + 2 + 3
            show("sumOfFirst", sumOfFirst);
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
