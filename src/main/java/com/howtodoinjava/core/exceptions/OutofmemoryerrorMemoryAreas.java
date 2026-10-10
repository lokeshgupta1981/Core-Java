package com.howtodoinjava.core.exceptions;

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
 * Examples for the tutorial "Java OutOfMemoryError: Causes, Fixes and Heap Dumps".
 * https://howtodoinjava.com/java/exception-handling/java-outofmemoryerror/
 */
public class OutofmemoryerrorMemoryAreas {
    static final class PreviewService {

        // Wrong: grows with every product and is never cleared
        private static final Map<String, byte[]> PREVIEWS = new HashMap<>();

        byte[] preview(String productId) {
            return PREVIEWS.computeIfAbsent(productId, id -> new byte[200_000]);
        }
    }
    static final class PreviewCache<K, V> extends LinkedHashMap<K, V> {

        private final int maxEntries;

        PreviewCache(int maxEntries) {
            super(16, 0.75f, true);                    // access order
            this.maxEntries = maxEntries;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxEntries;
        }
    }
    static Optional<long[]> tryAllocate(int size) {
        try {
            return Optional.of(new long[size]);
        } catch (OutOfMemoryError e) {
            return Optional.empty();                   // fall back to a smaller buffer
        }
    }
    public static void main(String[] args) throws Exception {
        {
            long maxHeap = Runtime.getRuntime().maxMemory();   // the -Xmx limit in bytes
            show("maxHeap", maxHeap);
            try { int[] tooBig = new int[Integer.MAX_VALUE]; show("tooBig", tooBig); } catch (Throwable _t) { System.out.println("tooBig -> " + _t); }
            try { long[] huge = new long[Integer.MAX_VALUE - 8]; show("huge", huge); } catch (Throwable _t) { System.out.println("huge -> " + _t); }
        }
        {
            Path csv = Files.writeString(Files.createTempFile("orders", ".csv"), "id,cents\n1,250\n2,400\n3,150\n");
            long total;
            try (Stream<String> lines = Files.lines(csv)) {
                total = lines.skip(1).mapToLong(line -> Long.parseLong(line.split(",")[1])).sum();
            }
            long sum = total;                                  // 800
            show("sum", sum);
        }
        {
            PreviewCache<String, byte[]> previews = new PreviewCache<>(3);
            for (String id : List.of("a", "b", "c", "d", "e")) {
                previews.put(id, new byte[1024]);
            }
            int cached = previews.size();                      // 3
            show("cached", cached);
            Set<String> kept = previews.keySet();              // [c, d, e]
            show("kept", kept);
        }
        {
            int wrapped = 100_000 * 100_000;                   // 1410065408
            show("wrapped", wrapped);
            try { int checked = Math.multiplyExact(100_000, 100_000); show("checked", checked); } catch (Throwable _t) { System.out.println("checked -> " + _t); }
        }
        {
            int done;
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                List<Future<Integer>> futures = IntStream.range(0, 10_000).mapToObj(i -> executor.submit(() -> i)).toList();
                done = futures.size();
            }
            int tasks = done;                                  // 10000
            show("tasks", tasks);
        }
        {
            boolean bigBuffer = tryAllocate(Integer.MAX_VALUE).isPresent();       // false
            show("bigBuffer", bigBuffer);
            int smallBuffer = tryAllocate(1_000).map(b -> b.length).orElse(0);     // 1000
            show("smallBuffer", smallBuffer);
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
