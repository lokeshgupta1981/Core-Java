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
import com.google.common.base.Ticker;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.time.StopWatch;

/**
 * Examples for the tutorial "Java StopWatch: Guava, Apache Commons, Spring and java.time".
 * https://howtodoinjava.com/java/date-time/stopwatch-example/
 */
public class StopWatchExamples {
    static long sumOfSquares(int n) {
        long sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += (long) i * i;
        }
        return sum;
    }
    static class FakeTicker extends Ticker {
        private long nanos;

        void advance(Duration amount) {
            nanos += amount.toNanos();
        }

        @Override
        public long read() {
            return nanos;
        }
    }
    static final class SimpleStopwatch {
        private final LongSupplier ticker;
        private final Map<String, Duration> laps = new LinkedHashMap<>();
        private long startNanos;
        private long lastLapNanos;
        private long stoppedNanos;
        private boolean running;

        SimpleStopwatch(LongSupplier ticker) {
            this.ticker = ticker;
        }

        static SimpleStopwatch createStarted() {
            SimpleStopwatch watch = new SimpleStopwatch(System::nanoTime);
            watch.start();
            return watch;
        }

        void start() {
            if (running) {
                throw new IllegalStateException("Stopwatch is already running");
            }
            startNanos = ticker.getAsLong();
            lastLapNanos = startNanos;
            running = true;
        }

        void stop() {
            if (!running) {
                throw new IllegalStateException("Stopwatch is not running");
            }
            stoppedNanos += ticker.getAsLong() - startNanos;
            running = false;
        }

        Duration lap(String name) {
            if (!running) {
                throw new IllegalStateException("Stopwatch is not running");
            }
            long now = ticker.getAsLong();
            Duration lap = Duration.ofNanos(now - lastLapNanos);
            lastLapNanos = now;
            laps.put(name, lap);
            return lap;
        }

        Duration elapsed() {
            long current = running ? ticker.getAsLong() - startNanos : 0;
            return Duration.ofNanos(stoppedNanos + current);
        }

        Map<String, Duration> laps() {
            return Collections.unmodifiableMap(laps);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Stopwatch guava = Stopwatch.createStarted();
            long total = sumOfSquares(1_000_000);              // 333333833333500000
            show("total", total);
            Duration guavaTime = guava.stop().elapsed();       // elapsed time of the loop, from Guava
            show("guavaTime", guavaTime);
            StopWatch commons = StopWatch.createStarted();
            long again = sumOfSquares(1_000_000);              // 333333833333500000
            show("again", again);
            commons.stop();
            Duration commonsTime = commons.getDuration();      // elapsed time of the loop, from Commons Lang
            show("commonsTime", commonsTime);
        }
        {
            FakeTicker ticker = new FakeTicker();
            Stopwatch watch = Stopwatch.createStarted(ticker);
            ticker.advance(Duration.ofMillis(1250));
            Duration elapsed = watch.elapsed();                       // PT1.25S
            show("elapsed", elapsed);
            long millis = watch.elapsed(TimeUnit.MILLISECONDS);       // 1250
            show("millis", millis);
            String text = watch.toString();                           // "1.250 s"
            show("text", text);
            boolean running = watch.stop().isRunning();               // false
            show("running", running);
            try { Stopwatch twice = watch.stop(); show("twice", twice); } catch (Throwable _t) { System.out.println("twice -> " + _t); }
        }
        {
            StopWatch importWatch = StopWatch.createStarted();
            long readStep = sumOfSquares(500_000);                     // 41666791666750000
            show("readStep", readStep);
            importWatch.split("read file");
            importWatch.suspend();                                     // waiting for the user does not count
            importWatch.resume();
            long saveStep = sumOfSquares(500_000);                     // 41666791666750000
            show("saveStep", saveStep);
            importWatch.split("save rows");
            importWatch.stop();
            int splitCount = importWatch.getSplits().size();          // 2
            show("splitCount", splitCount);
            String firstLabel = importWatch.getSplits().get(0).getLabel();   // "read file"
            show("firstLabel", firstLabel);
            Duration total = importWatch.getDuration();               // total time without the suspended part
            show("total", total);
            String formatted = importWatch.formatTime();              // total time as HH:mm:ss.SSS text
            show("formatted", formatted);
        }
        {
            StopWatch fresh = new StopWatch();
            try { fresh.stop();  } catch (Throwable _t) { System.out.println("-> " + _t); }
            StopWatch timedCall = new StopWatch();
            long result = timedCall.get(() -> sumOfSquares(1_000));   // 333833500
            show("result", result);
            boolean suspended = timedCall.isSuspended();              // true
            show("suspended", suspended);
            Duration callTime = timedCall.getDuration();             // time spent inside the call
            show("callTime", callTime);
        }
        {
            AtomicLong fakeNanos = new AtomicLong();
            SimpleStopwatch checkout = new SimpleStopwatch(fakeNanos::get);
            checkout.start();
            fakeNanos.set(120_000_000L);
            Duration validate = checkout.lap("validate cart");        // PT0.12S
            show("validate", validate);
            fakeNanos.set(970_000_000L);
            Duration payment = checkout.lap("charge payment");        // PT0.85S
            show("payment", payment);
            fakeNanos.set(1_000_000_000L);
            Duration email = checkout.lap("send email");              // PT0.03S
            show("email", email);
            checkout.stop();
            Duration checkoutTime = checkout.elapsed();               // PT1S
            show("checkoutTime", checkoutTime);
            List<String> slowSteps = checkout.laps().entrySet().stream().filter(e -> e.getValue().toMillis() > 500).map(Map.Entry::getKey).toList();   // [charge payment]
            show("slowSteps", slowSteps);
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
