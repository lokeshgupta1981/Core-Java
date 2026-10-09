package com.howtodoinjava.core.datetime;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.time.format.*;
import java.time.temporal.*;
import java.time.chrono.*;
import java.time.zone.*;
import java.text.*;
import org.openjdk.jmh.annotations.*;
import java.lang.management.*;

/**
 * Examples for the tutorial "Measure Elapsed Time in Java: nanoTime, Duration and JMH".
 * https://howtodoinjava.com/java/date-time/execution-elapsed-time/
 */
public class ExecutionElapsedTime {
    static long sumOfSquares(int n) {
        long sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += (long) i * i;
        }
        return sum;
    }
    static record Timed<T>(T value, Duration took) {}
    static <T> Timed<T> timed(Supplier<T> task) {
        long start = System.nanoTime();
        T value = task.get();
        return new Timed<>(value, Duration.ofNanos(System.nanoTime() - start));
    }
    @State(Scope.Benchmark)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    static public class SumBenchmark {
        @Param({"1000", "100000"})
        int size;

        @Benchmark
        public long sumOfSquares() {
            long sum = 0;
            for (int i = 1; i <= size; i++) {
                sum += (long) i * i;
            }
            return sum;    // returned values are not removed by the JIT
        }
    }
    public static void main(String[] args) throws Exception {
        {
            long start = System.nanoTime();
            long total = sumOfSquares(1_000_000);                    // 333333833333500000
            show("total", total);
            long elapsedNanos = System.nanoTime() - start;           // elapsed nanoseconds, differs per run
            show("elapsedNanos", elapsedNanos);
            Duration elapsed = Duration.ofNanos(elapsedNanos);       // the same time as a Duration
            show("elapsed", elapsed);
            long elapsedMillis = elapsed.toMillis();                 // elapsed whole milliseconds
            show("elapsedMillis", elapsedMillis);
        }
        {
            long timeoutNanos = TimeUnit.SECONDS.toNanos(2);
            long deadline = System.nanoTime() + timeoutNanos;
            boolean timedOut = System.nanoTime() - deadline >= 0;    // false, the 2 seconds have not passed
            show("timedOut", timedOut);
        }
        {
            long elapsedNanos = 3_250_400_000L;
            Duration elapsed = Duration.ofNanos(elapsedNanos);                     // PT3.2504S
            show("elapsed", elapsed);
            long millis = elapsed.toMillis();                                      // 3250
            show("millis", millis);
            long seconds = elapsed.toSeconds();                                    // 3
            show("seconds", seconds);
            int millisPart = elapsed.toMillisPart();                               // 250
            show("millisPart", millisPart);
            long millisWithTimeUnit = TimeUnit.NANOSECONDS.toMillis(elapsedNanos); // 3250
            show("millisWithTimeUnit", millisWithTimeUnit);
            double secondsDecimal = elapsedNanos / 1_000_000_000.0;                // 3.2504
            show("secondsDecimal", secondsDecimal);
        }
        {
            Instant jobStart = Instant.parse("2026-10-10T02:00:00Z");
            Instant jobEnd = Instant.parse("2026-10-10T02:47:13.500Z");
            Duration took = Duration.between(jobStart, jobEnd);           // PT47M13.5S
            show("took", took);
            long tookSeconds = took.toSeconds();                          // 2833
            show("tookSeconds", tookSeconds);
            boolean slow = took.compareTo(Duration.ofMinutes(30)) > 0;    // true
            show("slow", slow);
            long minutesWithChronoUnit = ChronoUnit.MINUTES.between(jobStart, jobEnd);   // 47
            show("minutesWithChronoUnit", minutesWithChronoUnit);
        }
        {
            Timed<Long> report = timed(() -> sumOfSquares(1_000_000));
            long reportValue = report.value();                                     // 333333833333500000
            show("reportValue", reportValue);
            Duration reportTime = report.took();                                   // elapsed time of the call, differs per run
            show("reportTime", reportTime);
            boolean warn = report.took().compareTo(Duration.ofMillis(500)) > 0;    // false on a normal machine
            show("warn", warn);
        }
        {
            ThreadMXBean threads = ManagementFactory.getThreadMXBean();
            boolean supported = threads.isCurrentThreadCpuTimeSupported();          // true when the JVM can measure CPU time
            show("supported", supported);
            long cpuStart = threads.getCurrentThreadCpuTime();
            long cpuWork = sumOfSquares(2_000_000);
            long cpuNanos = threads.getCurrentThreadCpuTime() - cpuStart;         // CPU nanoseconds of this thread, differs per run
            show("cpuNanos", cpuNanos);
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
