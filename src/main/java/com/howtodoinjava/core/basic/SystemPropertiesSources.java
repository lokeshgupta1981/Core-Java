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
 * Examples for the tutorial "Java System Properties: getProperty, -D and the Key List".
 * https://howtodoinjava.com/java/basics/java-system-properties/
 */
public class SystemPropertiesSources {

    public static void main(String[] args) throws Exception {
        {
            String version = System.getProperty("java.version");          // "25.0.4.1"
            show("version", version);
            String region = System.getProperty("app.region", "us-east");  // "eu-west" with -Dapp.region=eu-west, else "us-east"
            show("region", region);
            String previous = System.setProperty("app.theme", "dark");    // null, there was no value before
            show("previous", previous);
            String theme = System.getProperty("app.theme");                // "dark"
            show("theme", theme);
        }
        {
            String home = System.getProperty("user.home");                  // the account's home folder
            show("home", home);
            String missing = System.getProperty("app.unit");                // null
            show("missing", missing);
            String unit = System.getProperty("app.unit", "metric");         // "metric"
            show("unit", unit);
            try { String empty = System.getProperty(""); show("empty", empty); } catch (Throwable _t) { System.out.println("empty -> " + _t); }
        }
        {
            String set = System.setProperty("app.retries", "5");           // null
            show("set", set);
            Integer retries = Integer.getInteger("app.retries", 3);       // 5
            show("retries", retries);
            String bad = System.setProperty("app.timeout", "abc");        // null
            show("bad", bad);
            Integer timeout = Integer.getInteger("app.timeout", 30);      // 30, "abc" is not a number
            show("timeout", timeout);
            boolean debug = Boolean.getBoolean("app.debug");              // false, the property is not set
            show("debug", debug);
        }
        {
            Properties all = System.getProperties();
            Set<String> keys = all.stringPropertyNames();
            List<String> osKeys = keys.stream().filter(key -> key.startsWith("os.")).sorted().toList(); // [os.arch, os.name, os.version]
            show("osKeys", osKeys);
        }
        {
            String before = System.setProperty("app.season", "summer");   // null
            show("before", before);
            String replaced = System.setProperty("app.season", "winter"); // "summer"
            show("replaced", replaced);
            String removed = System.clearProperty("app.season");          // "winter"
            show("removed", removed);
            String after = System.getProperty("app.season");              // null
            show("after", after);
        }
        {
            String start = new File("notes.txt").getAbsolutePath();
            String oldDir = System.setProperty("user.dir", "/tmp");
            String later = new File("notes.txt").getAbsolutePath();
            boolean unchanged = start.equals(later);                      // true, File cached user.dir at startup
            show("unchanged", unchanged);
        }
        {
            Map<String, String> env = System.getenv();
            String path = System.getenv("PATH");                          // the PATH of the process, null when not set
            show("path", path);
            try { String added = env.put("APP_REGION", "eu-west"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            Properties fromFile = new Properties();
            fromFile.load(new StringReader("app.region=eu-west\napp.retries=4"));
            fromFile.forEach((key, value) -> System.setProperty((String) key, (String) value));
            Integer loaded = Integer.getInteger("app.retries", 3);        // 4
            show("loaded", loaded);
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
