package com.howtodoinjava.core.gc;

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
import java.lang.management.*;

/**
 * Examples for the tutorial "Java Memory Model: Heap, Stack, Metaspace and Code Cache".
 * https://howtodoinjava.com/java/garbage-collection/java-memory-model/
 */
public class JvmMemoryAreasHeapStackMetaspace {
    static public class PriceCalculator {

        public double total(double price, int quantity) {
            double net = price * quantity;
            List<String> lines = new ArrayList<>();
            lines.add("net=" + net);
            return net * 1.18;
        }
    }
    static public class JobControl {

        private volatile boolean stopped;

        public void stop() {
            stopped = true;
        }

        public boolean isStopped() {
            return stopped;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int count = 3;                                            // 3, a local variable in the stack frame
            show("count", count);
            String city = "Pune";                                     // "Pune", reference on the stack, String object in the heap
            show("city", city);
            List<String> cities = new ArrayList<>(List.of(city));     // [Pune], the ArrayList object is on the heap
            show("cities", cities);
            long maxHeap = Runtime.getRuntime().maxMemory();          // 1568669696 bytes here, a quarter of the RAM the JVM sees
            show("maxHeap", maxHeap);
        }
        {
            List<String> heapPools = ManagementFactory.getMemoryPoolMXBeans().stream().filter(pool -> pool.getType() == MemoryType.HEAP).map(MemoryPoolMXBean::getName).toList();          // [G1 Eden Space, G1 Old Gen, G1 Survivor Space]
            show("heapPools", heapPools);
            List<String> nonHeapPools = ManagementFactory.getMemoryPoolMXBeans().stream().filter(pool -> pool.getType() == MemoryType.NON_HEAP).map(MemoryPoolMXBean::getName).toList();   // [CodeHeap 'non-nmethods', Metaspace, CodeHeap 'profiled nmethods', Compressed Class Space, CodeHeap 'non-profiled nmethods']
            show("nonHeapPools", nonHeapPools);
        }
        {
            double total = new PriceCalculator().total(100.0, 2);   // 236.0
            show("total", total);
        }
        {
            Thread parser = Thread.ofPlatform().name("parser").stackSize(4L * 1024 * 1024).unstarted(() -> {});   // unstarted thread "parser" that requests a 4 MB stack
            show("parser", parser);
        }
        {
            JobControl control = new JobControl();
            control.stop();
            boolean stopped = control.isStopped();     // true
            show("stopped", stopped);
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
