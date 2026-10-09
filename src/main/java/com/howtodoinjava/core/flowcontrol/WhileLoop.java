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
 * Examples for the tutorial "Java while Loop: Syntax, Examples and Infinite Loops".
 * https://howtodoinjava.com/java/flow-control/while-loop-in-java/
 */
public class WhileLoop {
    static int sumWithoutUpdate(int[] values) {
        int i = 0;
        int sum = 0;
        while (i < values.length) {
            sum += values[i];               // i never changes, runs forever
        }
        return sum;
    }
    static int sumWithStraySemicolon(int[] values) {
        int i = 0;
        int sum = 0;
        while (i < values.length);          // empty body, runs forever
        {
            sum += values[i];
            i++;
        }
        return sum;
    }
    static int sumFor(int n) {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += i;
        }
        return sum;
    }
    static int sumWhile(int n) {
        int sum = 0;
        int i = 0;
        while (i < n) {
            sum += i;
            i++;
        }
        return sum;
    }
    static boolean chargeWithRetry(IntPredicate gateway, int maxAttempts) {
        int attempt = 1;
        boolean charged = gateway.test(attempt);
        while (!charged && attempt < maxAttempts) {
            attempt++;
            charged = gateway.test(attempt);
        }
        return charged;
    }
    public static void main(String[] args) throws Exception {
        {
            double balance = 1000.0;
            int years = 0;
            while (balance < 2000.0) {
                balance = balance * 1.07;           // 7% interest per year
                years++;
            }
            int yearsToDouble = years;              // 11
            show("yearsToDouble", yearsToDouble);
        }
        {
            int countdown = 3;
            StringBuilder launch = new StringBuilder();
            while (countdown > 0) {
                launch.append(countdown).append(' ');
                countdown--;
            }
            String sequence = launch.toString();    // "3 2 1 "
            show("sequence", sequence);
        }
        {
            int stock = 0;
            int sold = 0;
            while (stock > 0) {
                stock--;
                sold++;
            }
            int soldCount = sold;                   // 0, the body never ran
            show("soldCount", soldCount);
        }
        {
            String csv = "pen,2\nbook,5\nlamp,1";
            int totalItems = 0;
            try (BufferedReader reader = new BufferedReader(new StringReader(csv))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    totalItems += Integer.parseInt(line.split(",")[1].strip());
                }
            }
            int items = totalItems;                 // 8
            show("items", items);
        }
        {
            List<String> cart = new ArrayList<>(List.of("pen", "", "book", ""));
            Iterator<String> it = cart.iterator();
            while (it.hasNext()) {
                if (it.next().isEmpty()) {
                    it.remove();
                }
            }
            String cleaned = cart.toString();       // "[pen, book]"
            show("cleaned", cleaned);
        }
        {
            Queue<String> jobs = new ArrayDeque<>(List.of("invoice", "label", "receipt"));
            List<String> printed = new ArrayList<>();
            String job;
            while ((job = jobs.poll()) != null) {
                printed.add(job);
            }
            int printedCount = printed.size();      // 3
            show("printedCount", printedCount);
            boolean empty = jobs.isEmpty();         // true
            show("empty", empty);
        }
        {
            int[] readings = {12, 15, 0, 18};
            int index = 0;
            int sum = 0;
            while (true) {
                int value = readings[index++];
                if (value == 0) {
                    break;                          // 0 marks the end of the data
                }
                sum += value;
            }
            int total = sum;                        // 27
            show("total", total);
        }
        {
            int viaFor = sumFor(5);                 // 10
            show("viaFor", viaFor);
            int viaWhile = sumWhile(5);             // 10
            show("viaWhile", viaWhile);
        }
        {
            IntPredicate failsTwice = attempt -> attempt >= 3;
            boolean paid = chargeWithRetry(failsTwice, 5);         // true, on attempt 3
            show("paid", paid);
            boolean declined = chargeWithRetry(attempt -> false, 3);   // false, after 3 attempts
            show("declined", declined);
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
