package com.howtodoinjava.misc;

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
 * Examples for the tutorial "Java Fork/Join Framework and ForkJoinPool with Examples".
 * https://howtodoinjava.com/java/forkjoin-framework-tutorial-forkjoinpool-example/
 */
public class ForkjoinpoolWorkStealing {
    static class StepTotal extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10_000;
        private final int[] steps;
        private final int from;
        private final int to;

        StepTotal(int[] steps, int from, int to) {
            this.steps = steps;
            this.from = from;
            this.to = to;
        }

        @Override
        protected Long compute() {
            if (to - from <= THRESHOLD) {
                long sum = 0;
                for (int i = from; i < to; i++) {
                    sum += steps[i];
                }
                return sum;
            }
            int mid = (from + to) >>> 1;
            StepTotal left = new StepTotal(steps, from, mid);
            StepTotal right = new StepTotal(steps, mid, to);
            left.fork();                          // 1. queue the left half
            long rightSum = right.compute();      // 2. work on the right half in this thread
            return left.join() + rightSum;        // 3. wait for the left half
        }
    }
    static class ToFahrenheit extends RecursiveAction {
        private static final int THRESHOLD = 10_000;
        private final double[] readings;
        private final int from;
        private final int to;

        ToFahrenheit(double[] readings, int from, int to) {
            this.readings = readings;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void compute() {
            if (to - from <= THRESHOLD) {
                for (int i = from; i < to; i++) {
                    readings[i] = readings[i] * 9 / 5 + 32;
                }
                return;
            }
            int mid = (from + to) >>> 1;
            invokeAll(new ToFahrenheit(readings, from, mid), new ToFahrenheit(readings, mid, to));
        }
    }
    static class LogFolderSize extends RecursiveTask<Long> {
        private final Path folder;

        LogFolderSize(Path folder) {
            this.folder = folder;
        }

        @Override
        protected Long compute() {
            List<LogFolderSize> subtasks = new ArrayList<>();
            long bytes = 0;
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(folder)) {
                for (Path entry : entries) {
                    if (Files.isDirectory(entry)) {
                        LogFolderSize task = new LogFolderSize(entry);
                        task.fork();
                        subtasks.add(task);
                    } else if (entry.toString().endsWith(".log")) {
                        bytes += Files.size(entry);
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            for (LogFolderSize task : subtasks) {
                bytes += task.join();
            }
            return bytes;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            int[] steps = new int[1_000_000];
            Arrays.setAll(steps, user -> user % 10_000);
            long total = ForkJoinPool.commonPool().invoke(new StepTotal(steps, 0, steps.length));   // 4999500000
            show("total", total);
            long check = Arrays.stream(steps).asLongStream().sum();                                 // 4999500000
            show("check", check);
        }
        {
            ForkJoinPool pool = new ForkJoinPool(4);
            int parallelism = pool.getParallelism();                                             // 4
            show("parallelism", parallelism);
            boolean oneLess = ForkJoinPool.getCommonPoolParallelism() == Runtime.getRuntime().availableProcessors() - 1;   // true
            show("oneLess", oneLess);
            boolean daemon = pool.submit(() -> Thread.currentThread().isDaemon()).get();         // true
            show("daemon", daemon);
            pool.close();
        }
        {
            int[] dailySteps = new int[1_000_000];
            Arrays.setAll(dailySteps, user -> user % 10_000);
            ForkJoinPool reportPool = new ForkJoinPool(4);
            long allSteps = reportPool.invoke(new StepTotal(dailySteps, 0, dailySteps.length));   // 4999500000
            show("allSteps", allSteps);
            long firstHalf = reportPool.invoke(new StepTotal(dailySteps, 0, 500_000));           // 2499750000
            show("firstHalf", firstHalf);
            reportPool.close();
        }
        {
            double[] readings = new double[200_000];
            Arrays.fill(readings, 25.0);
            ForkJoinPool.commonPool().invoke(new ToFahrenheit(readings, 0, readings.length));
            double first = readings[0];                                // 77.0
            show("first", first);
            double last = readings[readings.length - 1];               // 77.0
            show("last", last);
        }
        {
            Path root = Files.createTempDirectory("logs");
            Files.createDirectories(root.resolve("orders/2026"));
            Files.writeString(root.resolve("app.log"), "a".repeat(100));
            Files.writeString(root.resolve("orders/orders.log"), "b".repeat(200));
            Files.writeString(root.resolve("orders/2026/january.log"), "c".repeat(300));
            Files.writeString(root.resolve("orders/readme.txt"), "not a log");
            long logBytes = ForkJoinPool.commonPool().invoke(new LogFolderSize(root));      // 600
            show("logBytes", logBytes);
        }
        {
            ForkJoinTask<Integer> parse = ForkJoinTask.adapt(() -> Integer.parseInt("abc"));
            try { int parsed = ForkJoinPool.commonPool().invoke(parse); show("parsed", parsed); } catch (Throwable _t) { System.out.println("parsed -> " + _t); }
            boolean failed = parse.isCompletedAbnormally();           // true
            show("failed", failed);
            boolean ok = parse.isCompletedNormally();                 // false
            show("ok", ok);
            boolean cancelled = parse.isCancelled();                  // false
            show("cancelled", cancelled);
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
