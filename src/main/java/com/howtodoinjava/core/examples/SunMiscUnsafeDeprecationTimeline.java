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
import java.lang.reflect.Field;
import java.lang.invoke.VarHandle;
import java.lang.invoke.MethodHandles;

/**
 * Examples for the tutorial "sun.misc.Unsafe in Java: Deprecation, VarHandle and FFM API".
 * https://howtodoinjava.com/java-examples/usage-of-class-sun-misc-unsafe/
 */
public class SunMiscUnsafeDeprecationTimeline {
    static class PageStats {
        private static final VarHandle VIEWS;
        static {
            try {
                VIEWS = MethodHandles.lookup().findVarHandle(PageStats.class, "views", int.class);
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        private volatile int views;

        void recordView() {
            VIEWS.getAndAdd(this, 1);
        }

        boolean resetIf(int expected) {
            return VIEWS.compareAndSet(this, expected, 0);
        }

        int views() {
            return views;
        }
    }
    static class Ticket {
        private final String event;
        Ticket(String event) {
            this.event = Objects.requireNonNull(event);
        }
        String event() {
            return event;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            PageStats stats = new PageStats();
            IntStream.range(0, 10_000).parallel().forEach(i -> stats.recordView());
            int counted = stats.views();                                   // 10000
            show("counted", counted);
            boolean staleReset = stats.resetIf(9_999);                     // false
            show("staleReset", staleReset);
            boolean reset = stats.resetIf(10_000);                         // true
            show("reset", reset);
            int afterReset = stats.views();                                // 0
            show("afterReset", afterReset);
        }
        {
            VarHandle slots = MethodHandles.arrayElementVarHandle(int[].class);
            int[] seats = new int[4];
            boolean booked = slots.compareAndSet(seats, 2, 0, 1);           // true
            show("booked", booked);
            boolean bookedTwice = slots.compareAndSet(seats, 2, 0, 1);      // false
            show("bookedTwice", bookedTwice);
            try { int outside = (int) slots.getVolatile(seats, 4); show("outside", outside); } catch (Throwable _t) { System.out.println("outside -> " + _t); }
        }
        {
            char[] password = {'s', 'e', 'c', 'r', 'e', 't'};
            Arrays.fill(password, '\0');
            int nonZero = (int) new String(password).chars().filter(c -> c != 0).count(); // 0
            show("nonZero", nonZero);
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
