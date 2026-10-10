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

/**
 * Examples for the tutorial "Difference Between Interface and Abstract Class in Java".
 * https://howtodoinjava.com/java/oops/exploring-interfaces-and-abstract-classes-in-java/
 */
public class InterfaceVsAbstractClass {
    static interface Storage {                                     // the contract
        void put(String key, String value);
        String get(String key);
    }

    static abstract class BaseStorage implements Storage {         // a partial implementation
        protected final Map<String, String> data = new HashMap<>();  // instance state
        private final String name;
        protected BaseStorage(String name) { this.name = name; }     // constructor
        public String get(String key) { return data.get(key); }       // shared code
        public String name() { return name; }
    }

    static class MemoryStorage extends BaseStorage {
        MemoryStorage() { super("memory"); }
        public void put(String key, String value) { data.put(key, value); }
    }
    static interface Expiring {
        long MAX_TTL = 86_400;                      // constant, public static final

        long ttlSeconds();                          // abstract method

        default boolean isShortLived() {            // Java 8, default method
            return ttlSeconds() < minutes(5);
        }

        static Expiring of(long seconds) {          // Java 8, static method
            return () -> Math.min(seconds, MAX_TTL);
        }

        private long minutes(long count) {          // Java 9, private method
            return count * 60;
        }
    }
    static class Weekdays extends AbstractList<String> {
        private final String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri"};

        @Override
        public String get(int index) { return days[index]; }

        @Override
        public int size() { return days.length; }
    }
    static class SettingsService {
        private final Storage storage;

        SettingsService(Storage storage) { this.storage = storage; }

        String theme() {
            String value = storage.get("theme");
            return value != null ? value : "light";
        }
    }
    public static void main(String[] args) throws Exception {
        {
            BaseStorage prefs = new MemoryStorage();
            prefs.put("theme", "dark");
            String theme = prefs.get("theme");          // "dark"
            show("theme", theme);
            String storageName = prefs.name();          // "memory"
            show("storageName", storageName);
        }
        {
            BaseStorage readOnly = new BaseStorage("read-only") {
                public void put(String key, String value) {
                    throw new UnsupportedOperationException("read-only storage");
                }
            };
            String readOnlyName = readOnly.name();      // "read-only"
            show("readOnlyName", readOnlyName);
        }
        {
            Expiring session = Expiring.of(120);
            boolean shortLived = session.isShortLived();        // true
            show("shortLived", shortLived);
            long capped = Expiring.of(999_999).ttlSeconds();    // 86400
            show("capped", capped);
        }
        {
            List<String> weekdays = new Weekdays();
            boolean hasWed = weekdays.contains("Wed");  // true
            show("hasWed", hasWed);
            int thu = weekdays.indexOf("Thu");          // 3
            show("thu", thu);
            List<String> middle = weekdays.subList(1, 3);   // [Tue, Wed]
            show("middle", middle);
            try { boolean added = weekdays.add("Sat"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            SettingsService settings = new SettingsService(new MemoryStorage());
            String defaultTheme = settings.theme();     // "light"
            show("defaultTheme", defaultTheme);
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
