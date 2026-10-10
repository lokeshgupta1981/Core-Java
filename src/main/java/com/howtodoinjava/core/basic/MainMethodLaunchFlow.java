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
 * Examples for the tutorial "Java main() Method: Syntax, Java 25 Rules and Arguments".
 * https://howtodoinjava.com/java/basics/main-method/
 */
public class MainMethodLaunchFlow {
    static public class Invoice {
        public static void main(String[] args) {
            String customer = args.length > 0 ? args[0] : "guest";
            System.out.println("Invoice for " + customer);   // java Invoice.java Lokesh  ->  Invoice for Lokesh
        }
    }
    static public class ShowArgs {
        public static void main(String[] args) {
            System.out.println("env = " + System.getProperty("env"));
            for (int i = 0; i < args.length; i++) {
                System.out.println(i + ": [" + args[i] + "]");
            }
        }
    }
    static int parseDays(String[] args, int defaultDays) {
        if (args.length < 2) {
            return defaultDays;
        }
        try {
            return Integer.parseInt(args[1].strip());
        } catch (NumberFormatException e) {
            System.err.println("Not a number: " + args[1] + ", using " + defaultDays);
            return defaultDays;
        }
    }
    static Map<String, String> parseOptions(String[] args) {
        Map<String, String> options = new TreeMap<>();
        for (String arg : args) {
            int eq = arg.indexOf('=');
            if (arg.startsWith("--") && eq > 2) {
                options.put(arg.substring(2, eq), arg.substring(eq + 1));
            }
        }
        return options;
    }
    static public class Cleanup {
        public static void main(String[] args) {
            if (args.length == 0) {
                System.err.println("Usage: java Cleanup <folder>");
                System.exit(2);
            }
            System.out.println("Cleaning " + args[0]);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String[] noArgs = {};
            try { String first = noArgs[0]; show("first", first); } catch (Throwable _t) { System.out.println("first -> " + _t); }
            try { int days = Integer.parseInt("seven"); show("days", days); } catch (Throwable _t) { System.out.println("days -> " + _t); }
        }
        {
            int given = parseDays(new String[] {"daily", " 7"}, 1);         // 7
            show("given", given);
            int missing = parseDays(new String[] {"daily"}, 1);             // 1
            show("missing", missing);
            int invalid = parseDays(new String[] {"daily", "seven"}, 1);    // 1
            show("invalid", invalid);
        }
        {
            Map<String, String> opts = parseOptions(new String[] {"--env=prod", "--days=7", "extra"});   // {days=7, env=prod}
            show("opts", opts);
            String env = opts.getOrDefault("env", "dev");                                                   // "prod"
            show("env", env);
            String region = opts.getOrDefault("region", "eu");                                              // "eu"
            show("region", region);
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
