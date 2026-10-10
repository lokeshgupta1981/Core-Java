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
import java.lang.ref.*;
import java.lang.foreign.*;

/**
 * Examples for the tutorial "Java Cleaner API: Replace finalize() with Cleaner".
 * https://howtodoinjava.com/java/basics/java-cleaners/
 */
public class CleanerCleanupPaths {
    static final class NativeImages {
        static final AtomicInteger HANDLES = new AtomicInteger();
        static long open() { HANDLES.incrementAndGet(); return 42L; }
        static void free(long handle) { HANDLES.decrementAndGet(); }
    }
    // Before: deprecated for removal
    static class ImageCache {
        private final long handle = NativeImages.open();

        @Override
        protected void finalize() {
            NativeImages.free(handle);
        }
    }
    // After: explicit close() plus a Cleaner as the safety net
    static final class ImageCacheV2 implements AutoCloseable {

        private static final Cleaner CLEANER = Cleaner.create();

        private record FreeHandle(long handle) implements Runnable {
            @Override
            public void run() {
                NativeImages.free(handle);
            }
        }

        private final Cleaner.Cleanable cleanable;

        ImageCacheV2() {
            long handle = NativeImages.open();
            this.cleanable = CLEANER.register(this, new FreeHandle(handle));
        }

        @Override
        public void close() {
            cleanable.clean();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Cleaner cleaner = Cleaner.create();
            AtomicInteger openHandles = new AtomicInteger(1);
            Object owner = new Object();
            Cleaner.Cleanable cleanable = cleaner.register(owner, openHandles::decrementAndGet);

            cleanable.clean();                             // runs the action on this thread
            int afterClean = openHandles.get();            // 0
            show("afterClean", afterClean);
            cleanable.clean();                             // second call does nothing
            int afterSecond = openHandles.get();           // 0
            show("afterSecond", afterSecond);
        }
        {
            try (ImageCacheV2 cache = new ImageCacheV2()) {
                int inUse = NativeImages.HANDLES.get();    // 1
            }
            int afterClose = NativeImages.HANDLES.get();   // 0
            show("afterClose", afterClose);
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
