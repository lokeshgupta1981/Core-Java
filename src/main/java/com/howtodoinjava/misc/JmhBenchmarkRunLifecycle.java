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
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.results.format.ResultFormatType;

/**
 * Examples for the tutorial "JMH Java Microbenchmark Harness: Setup, Blackhole, Pitfalls".
 * https://howtodoinjava.com/java/library/jmh-java-microbenchmark-harness/
 */
public class JmhBenchmarkRunLifecycle {
    @State(Scope.Thread)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    static public class JoinBenchmark {

        String[] words = new String[1000];

        @Setup
        public void fill() {
            Arrays.setAll(words, i -> "word" + i);
        }

        @Benchmark
        public String join() {
            return String.join("", words);     // returned, so the JIT cannot remove it
        }
    }
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 5, time = 1)
    @Fork(2)
    @State(Scope.Thread)
    static public class ConcatBenchmark {

        @Param({"10", "1000"})
        int size;

        String[] words;

        @Setup
        public void setUp() {
            words = new String[size];
            for (int i = 0; i < size; i++) {
                words[i] = "word" + i;
            }
        }

        @Benchmark
        public String plusInLoop() {
            String result = "";
            for (String word : words) {
                result += word;
            }
            return result;
        }

        @Benchmark
        public String stringBuilder() {
            StringBuilder sb = new StringBuilder();
            for (String word : words) {
                sb.append(word);
            }
            return sb.toString();
        }

        @Benchmark
        public String stringJoin() {
            return String.join("", words);
        }
    }
    @State(Scope.Thread)
    static public class LogBenchmark {

        double x = Math.PI;

        @Benchmark
        public void wrong() {
            Math.log(x);                       // result unused, may be removed
        }

        @Benchmark
        public double returned() {
            return Math.log(x);
        }

        @Benchmark
        public void consumed(Blackhole bh) {
            bh.consume(Math.log(x));
            bh.consume(Math.log(x + 1));       // several results need a Blackhole
        }
    }
    static public class BenchmarkMain {

        public static void main(String[] args) throws RunnerException {
            Options options = new OptionsBuilder()
                    .include(ConcatBenchmark.class.getSimpleName())
                    .forks(1)
                    .resultFormat(ResultFormatType.JSON)
                    .result("concat.json")
                    .build();
            new Runner(options).run();
        }
    }
    public static void main(String[] args) throws Exception {

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
