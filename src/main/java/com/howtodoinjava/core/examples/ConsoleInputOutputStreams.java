package com.howtodoinjava.core.examples;

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
import java.util.Scanner;
import java.util.Arrays;

/**
 * Examples for the tutorial "Java Console Input and Output: System.out, Console and IO".
 * https://howtodoinjava.com/java-examples/console-input-output/
 */
public class ConsoleInputOutputStreams {
    static void askName(Console console) {
        String name = console.readLine("Your name: ");
        if (name == null) {                 // end of input, e.g. Ctrl+D
            return;
        }
        console.printf("Hello %s%n", name.strip()).flush();
    }
    static char[] readToken() {
        Console console = System.console();
        if (console != null) {
            char[] token = console.readPassword("API token: ");
            return token != null ? token : new char[0];
        }
        String fromEnv = System.getenv("EXPENSES_TOKEN");    // set by the build server
        return fromEnv != null ? fromEnv.toCharArray() : new char[0];
    }
    public static void main(String[] args) throws Exception {
        {
            String item = "Coffee";
            double amount = 3.5;
            String line = String.format("%-8s %7.2f", item, amount);    // "Coffee      3.50"
            show("line", line);
            String total = "Total: %.2f".formatted(12.75);             // "Total: 12.75"
            show("total", total);
        }
        {
            String note = "50% off";
            try { String bad = String.format(note); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
            String safe = String.format("Note: %s", note); // "Note: 50% off"
            show("safe", safe);
        }
        {
            Scanner scanner = new Scanner("abc\n12.50\n");       // new Scanner(System.in) in a real program
            show("scanner", scanner);
            boolean validFirst = scanner.hasNextDouble();          // false, "abc" is not a number
            show("validFirst", validFirst);
            String skipped = scanner.nextLine();                   // "abc"
            show("skipped", skipped);
            double amount = scanner.nextDouble();                  // 12.5
            show("amount", amount);
        }
        {
            char[] token = readToken();
            try {
                // 1. Use the token, e.g. in an Authorization header
            } finally {
                Arrays.fill(token, ' ');                              // 2. Overwrite the secret
            }
            int length = token.length;                                // 0 when no token was given
            show("length", length);
        }
        {
            String red = "\u001B[31m" + "Over budget" + "\u001B[0m";   // shows red text in a terminal
            show("red", red);
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
