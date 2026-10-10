package com.howtodoinjava.core.string;

import java.util.concurrent.locks.*;
import java.lang.management.*;
import java.time.format.*;
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

import java.time.temporal.*;
import java.lang.reflect.*;

/**
 * Examples for the tutorial "Why Strings Are Immutable in Java: Reasons and Examples".
 * https://howtodoinjava.com/java/string/java-interview-question-why-strings-are-immutable/
 */
public class StringImmutability {
    static class Settings {
        private volatile Map<String, String> values = Map.of("payment.mode", "sandbox");

        String get(String key) {
            return values.get(key);
        }

        void reload(Map<String, String> fresh) {
            values = Map.copyOf(fresh);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String env = "dev";
            String upper = env.toUpperCase(Locale.ROOT);          // "DEV"
            show("upper", upper);
            String original = env;                                // "dev"
            show("original", original);
            boolean sameObject = upper == env;                    // false
            show("sameObject", sameObject);
        }
        {
            String name = "dev";
            name.concat("-eu");                                   // result is discarded
            String unchanged = name;                              // "dev"
            show("unchanged", unchanged);
            name = name.concat("-eu");                            // name now points to a new object
            String reassigned = name;                             // "dev-eu"
            show("reassigned", reassigned);
        }
        {
            String first = "value";
            String second = "value";
            String built = new StringBuilder("val").append("ue").toString();
            boolean pooled = first == second;                     // true
            show("pooled", pooled);
            boolean runtime = first == built;                     // false
            show("runtime", runtime);
            boolean interned = first == built.intern();           // true
            show("interned", interned);
        }
        {
            char[] letters = {'d', 'e', 'v'};
            String fromArray = new String(letters);
            letters[0] = 'r';
            String stillDev = fromArray;                          // "dev"
            show("stillDev", stillDev);
            char[] copy = fromArray.toCharArray();
            copy[0] = 'r';
            String alsoDev = fromArray;                           // "dev"
            show("alsoDev", alsoDev);
        }
        {
            Field field = String.class.getDeclaredField("value");
            try { field.setAccessible(true);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            StringBuilder mutableHost = new StringBuilder("api.example.com");
            String checkedHost = mutableHost.toString();
            boolean allowed = Set.of("api.example.com").contains(checkedHost);   // true
            show("allowed", allowed);
            mutableHost.replace(0, 3, "dev");
            String builderNow = mutableHost.toString();           // "dev.example.com"
            show("builderNow", builderNow);
            String stillChecked = checkedHost;                    // "api.example.com"
            show("stillChecked", stillChecked);
        }
        {
            AtomicReference<String> status = new AtomicReference<>("idle");
            boolean changed = status.compareAndSet("idle", "running");   // true
            show("changed", changed);
            String current = status.get();                        // "running"
            show("current", current);
        }
        {
            Map<List<String>, Integer> byList = new HashMap<>();
            List<String> listKey = new ArrayList<>(List.of("apple"));
            byList.put(listKey, 5);
            listKey.add("kiwi");
            Integer lost = byList.get(listKey);                   // null
            show("lost", lost);
            Map<String, Integer> byName = new HashMap<>();
            byName.put("apple", 5);
            Integer found = byName.get("apple");                  // 5
            show("found", found);
        }
        {
            StringBuilder csv = new StringBuilder();
            for (String fruit : List.of("apple", "kiwi", "fig")) {
                if (!csv.isEmpty()) {
                    csv.append(',');
                }
                csv.append(fruit);
            }
            String line = csv.toString();                         // "apple,kiwi,fig"
            show("line", line);
        }
        {
            char[] password = {'s', 'e', 'c', 'r', 'e', 't'};
            Arrays.fill(password, '*');
            String wiped = new String(password);                  // "******"
            show("wiped", wiped);
        }
        {
            Settings settings = new Settings();
            String before = settings.get("payment.mode");         // "sandbox"
            show("before", before);
            settings.reload(Map.of("payment.mode", "live"));
            String after = settings.get("payment.mode");          // "live"
            show("after", after);
            String oldValue = before;                             // "sandbox"
            show("oldValue", oldValue);
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
