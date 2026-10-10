package com.howtodoinjava.io;

import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
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
 * Examples for the tutorial "Read a File from the Classpath in Java: ClassLoader vs Class".
 * https://howtodoinjava.com/java/io/read-file-from-classpath/
 */
public class ClasspathResources {
    static class MenuService {

        static String readMenu(String name) throws IOException {
            ClassLoader loader = MenuService.class.getClassLoader();
            try (InputStream in = loader.getResourceAsStream(name)) {
                if (in == null) {
                    throw new FileNotFoundException(name + " is not on the classpath");
                }
                return new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            ClassLoader loader = MenuService.class.getClassLoader();
            URL where = loader.getResource("menu/today.txt");               // jar:file:/.../menu-data.jar!/menu/today.txt
            show("where", where);
            boolean missing = loader.getResource("menu/tomorrow.txt") == null;   // true
            show("missing", missing);

            String soup;
            try (InputStream in = Objects.requireNonNull(loader.getResourceAsStream("menu/today.txt"), "menu/today.txt")) {
                soup = new String(in.readAllBytes(), StandardCharsets.UTF_8).strip();
            }
            String today = soup;                                            // "Soup of the day: tomato"
            show("today", today);
        }
        {
            String menu = MenuService.readMenu("menu/today.txt").strip();   // "Soup of the day: tomato"
            show("menu", menu);
            try { String none = MenuService.readMenu("menu/tomorrow.txt"); show("none", none); } catch (Throwable _t) { System.out.println("none -> " + _t); }
        }
        {
            URL relative = String.class.getResource("String.class");              // jrt:/java.base/java/lang/String.class
            show("relative", relative);
            URL absolute = String.class.getResource("/java/lang/String.class");    // jrt:/java.base/java/lang/String.class
            show("absolute", absolute);
            URL viaLoader = ClassLoader.getSystemClassLoader().getResource("String.class");   // null
            show("viaLoader", viaLoader);
        }
        {
            ClassLoader own = MenuService.class.getClassLoader();
            ClassLoader context = Thread.currentThread().getContextClassLoader();
            boolean sameHere = own == context;                                      // true in a plain java -cp run
            show("sameHere", sameHere);
            boolean contextFinds = context.getResource("menu/today.txt") != null;  // true
            show("contextFinds", contextFinds);
        }
        {
            ClassLoader loader = MenuService.class.getClassLoader();
            URL first = loader.getResource("specials/weekly.txt");     // file:/.../target/classes/specials/weekly.txt
            show("first", first);

            List<String> specials = new ArrayList<>();
            try (Stream<URL> urls = loader.resources("specials/weekly.txt")) {
                for (URL url : urls.toList()) {
                    try (InputStream in = url.openStream()) {
                        specials.add(new String(in.readAllBytes(), StandardCharsets.UTF_8).strip());
                    }
                }
            }
            List<String> allSpecials = specials;                       // [Weekly special: lasagna, Weekly special: curry]
            show("allSpecials", allSpecials);
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
