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
 * Examples for the tutorial "Synchronized ArrayList in Java: Thread-Safe List Options".
 * https://howtodoinjava.com/java/collections/arraylist/synchronize-arraylist/
 */
public class SynchronizedArrayList {
    static boolean addOnce(List<String> list, String value) {
        synchronized (list) {
            if (list.contains(value)) {
                return false;
            }
            return list.add(value);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> events = Collections.synchronizedList(new ArrayList<>());
            events.add("login");                                         // thread-safe, locks the wrapper
            events.add("search");
            List<String> copy;
            synchronized (events) {                                      // required for iteration and streams
                copy = List.copyOf(events);
            }
            int copied = copy.size();                                    // 2
            show("copied", copied);
            CopyOnWriteArrayList<String> listeners = new CopyOnWriteArrayList<>(List.of("audit"));
            boolean added = listeners.addIfAbsent("email");              // true, no external lock needed
            show("added", added);
        }
        {
            List<Integer> unsafe = new ArrayList<>();
            try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
                for (int t = 0; t < 4; t++) {
                    pool.submit(() -> {
                        for (int i = 0; i < 10_000; i++) {
                            unsafe.add(i);
                        }
                    });
                }
            }
            int lost = unsafe.size();                                    // varies, often below 40000
            show("lost", lost);
        }
        {
            List<Integer> safe = Collections.synchronizedList(new ArrayList<>());
            try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
                for (int t = 0; t < 4; t++) {
                    pool.submit(() -> {
                        for (int i = 0; i < 10_000; i++) {
                            safe.add(i);
                        }
                    });
                }
            }
            int all = safe.size();                                       // 40000
            show("all", all);
        }
        {
            List<String> wrong = new ArrayList<>();
            List<String> wrapped = Collections.synchronizedList(wrong);  // "wrong" still bypasses the lock
            show("wrapped", wrapped);
            List<String> right = Collections.synchronizedList(new ArrayList<>());   // only the wrapper exists
            show("right", right);
        }
        {
            List<String> visitors = Collections.synchronizedList(new ArrayList<>(List.of("ana", "raj", "lee")));
            long threeLetter;
            synchronized (visitors) {
                threeLetter = visitors.stream().filter(n -> n.length() == 3).count();
            }
            long counted = threeLetter;                                    // 3
            show("counted", counted);
        }
        {
            List<String> sessions = Collections.synchronizedList(new ArrayList<>());
            boolean firstLogin = addOnce(sessions, "ana");               // true
            show("firstLogin", firstLogin);
            boolean secondLogin = addOnce(sessions, "ana");              // false, already in the list
            show("secondLogin", secondLogin);
        }
        {
            CopyOnWriteArrayList<String> handlers = new CopyOnWriteArrayList<>(List.of("audit", "email"));
            Iterator<String> snapshot = handlers.iterator();
            handlers.add("sms");
            int seen = 0;
            while (snapshot.hasNext()) {
                snapshot.next();
                seen++;
            }
            int seenByIterator = seen;                                   // 2, created before "sms" was added
            show("seenByIterator", seenByIterator);
            int current = handlers.size();                               // 3
            show("current", current);
        }
        {
            CopyOnWriteArrayList<String> hooks = new CopyOnWriteArrayList<>(List.of("audit", "email", "sms"));
            Iterator<String> it = hooks.iterator();
            String firstHook = it.next();                                // "audit"
            show("firstHook", firstHook);
            try { it.remove();  } catch (Throwable _t) { System.out.println("-> " + _t); }
            for (String hook : hooks) {
                if (hook.equals("email")) {
                    hooks.remove(hook);                                  // allowed, no ConcurrentModificationException
                }
            }
            String remaining = hooks.toString();                         // "[audit, sms]"
            show("remaining", remaining);
        }
        {
            List<Integer> squares = IntStream.rangeClosed(1, 5).parallel().map(n -> n * n).boxed().toList();   // [1, 4, 9, 16, 25]
            show("squares", squares);
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
