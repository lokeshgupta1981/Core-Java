package com.howtodoinjava.core.collections.list;

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
 * Examples for the tutorial "ArrayList vs Vector in Java: Differences and Modern Alternatives".
 * https://howtodoinjava.com/java/collections/arraylist/arraylist-vs-vector/
 */
public class ArrayListVsVector {
    static void registerUnsafe(Vector<String> devices, String id) {
        if (!devices.contains(id)) {          // threads A and B both see false
            devices.add(id);                  // both add the same id
        }
    }
    static void registerSafe(List<String> devices, String id) {
        synchronized (devices) {
            if (!devices.contains(id)) {
                devices.add(id);
            }
        }
    }
    static void addDuringLoop(List<String> sensors) {
        Iterator<String> it = sensors.iterator();
        while (it.hasNext()) {
            if (it.next().equals("attic")) {
                sensors.add("yard");
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<Integer> readings = new ArrayList<>();                           // no locking
            show("readings", readings);
            Vector<Integer> legacy = new Vector<>();                                // every method synchronized
            show("legacy", legacy);
            for (int i = 0; i < 11; i++) {
                readings.add(i);
                legacy.add(i);
            }
            int vectorCapacity = legacy.capacity();                                 // 20, doubled from 10
            show("vectorCapacity", vectorCapacity);
            int listSize = readings.size();                                         // 11, ArrayList has no capacity() method
            show("listSize", listSize);
            List<Integer> shared = Collections.synchronizedList(new ArrayList<>()); // the modern thread-safe choice
            show("shared", shared);
        }
        {
            List<String> devices = Collections.synchronizedList(new ArrayList<>());
            registerSafe(devices, "thermostat");
            registerSafe(devices, "thermostat");
            int registered = devices.size();                                        // 1
            show("registered", registered);
            CopyOnWriteArrayList<String> listeners = new CopyOnWriteArrayList<>();
            boolean first = listeners.addIfAbsent("thermostat");                    // true
            show("first", first);
            boolean second = listeners.addIfAbsent("thermostat");                   // false, already present
            show("second", second);
        }
        {
            Vector<Integer> stepped = new Vector<>(10, 5);
            for (int i = 0; i < 11; i++) {
                stepped.add(i);
            }
            int steppedCapacity = stepped.capacity();                               // 15, grew by the fixed increment
            show("steppedCapacity", steppedCapacity);
        }
        {
            try { addDuringLoop(new ArrayList<>(List.of("hall", "attic", "porch")));  } catch (Throwable _t) { System.out.println("-> " + _t); }
            try { addDuringLoop(new Vector<>(List.of("hall", "attic", "porch")));  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Vector<String> sensors = new Vector<>(List.of("hall", "attic", "porch"));
            Enumeration<String> names = sensors.elements();
            StringBuilder seen = new StringBuilder();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                if (name.equals("attic")) {
                    sensors.add("yard");
                }
                seen.append(name).append(' ');
            }
            String visited = seen.toString().strip();                               // "hall attic porch yard", no exception
            show("visited", visited);
        }
        {
            Vector<String> old = new Vector<>(List.of("hall", "attic"));
            List<String> modern = new ArrayList<>(old);                             // [hall, attic]
            show("modern", modern);
            Vector<String> forLegacyApi = new Vector<>(modern);                     // [hall, attic]
            show("forLegacyApi", forLegacyApi);
            ArrayList<String> fromEnumeration = Collections.list(old.elements());   // [hall, attic]
            show("fromEnumeration", fromEnumeration);
            Deque<String> undo = new ArrayDeque<>();
            undo.push("type");
            undo.push("bold");
            String lastAction = undo.pop();                                         // "bold"
            show("lastAction", lastAction);
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
