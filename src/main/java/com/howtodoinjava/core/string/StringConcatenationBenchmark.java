package com.howtodoinjava.core.string;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
public class StringConcatenationBenchmark {

  @Param({"10", "100", "1000"})
  private int count;

  private String baseString;

  @Setup
  public void setup() {
    baseString = "SampleString";
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public String testStringConcatenation() {
    String result = "";
    for (int i = 0; i < count; i++) {
      result += baseString;
    }
    return result;
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public String testStringBuilder() {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < count; i++) {
      builder.append(baseString);
    }
    return builder.toString();
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public String testStringBuffer() {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < count; i++) {
      buffer.append(baseString);
    }
    return buffer.toString();
  }

  public static void main(String[] args) throws Exception {

    var name = "Alex";
    var time = LocalTime.now();

    String greeting = "Hello " + name + ", how are you?\nThe current time is " + time + " now!";
    System.out.println(greeting);

    String greetingTemplate = STR."Hello \{name}, how are you?\nThe current time is \{time} now!";
    System.out.println(greetingTemplate);

    Options opt = new OptionsBuilder()
        .include(StringConcatenationBenchmark.class.getSimpleName())
        .forks(1)
        .build();

    new Runner(opt).run();
  }
}

