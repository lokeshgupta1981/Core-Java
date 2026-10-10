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
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.results.RunResult;

/**
 * Examples for the tutorial "JMH Error: Unable to find the resource: /META-INF/BenchmarkList".
 * https://howtodoinjava.com/java/exception-handling/jmh-error-unable-to-find-the-resource-meta-inf-benchmarklist/
 */
public class JmhBenchmarklistAnnotationProcessor {
    @State(Scope.Benchmark)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    static public class StringJoinBenchmark {

        String[] words = {"apple", "banana", "cherry"};

        @Benchmark
        public String concat() {
            String result = "";
            for (String word : words) {
                result += word;
            }
            return result;
        }

        @Benchmark
        public String builder() {
            StringBuilder sb = new StringBuilder();
            for (String word : words) {
                sb.append(word);
            }
            return sb.toString();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Options options = new OptionsBuilder()
                    .include(StringJoinBenchmark.class.getSimpleName())
                    .forks(1)
                    .build();
            try { Collection<RunResult> results = new Runner(options).run(); show("results", results); } catch (Throwable _t) { System.out.println("results -> " + _t); }
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
