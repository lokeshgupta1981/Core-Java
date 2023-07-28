package com.howtodoinjava.core.collections.map;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3, time = 10, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.MILLISECONDS)
public class PerformanceCompareForIterations {

  static Map<String, Integer> map = new HashMap();

  static {
    for (int i = 0; i < 10_00_000; i++) {
      map.put(String.valueOf(i), i);
    }
  }

  public static void main(String[] args) throws IOException {

    org.openjdk.jmh.Main.main(args);
  }

  @Benchmark
  public void usingForEach(Blackhole blackhole) {
    map.forEach((key, value) -> {
      blackhole.consume(key);
      blackhole.consume(value);
    });
  }

  @Benchmark
  public void usingEntrySetWithForLoop(Blackhole blackhole) {
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      blackhole.consume(entry.getKey());
      blackhole.consume(entry.getValue());
    }
  }

  @Benchmark
  public void usingKeySetAndAccessValues(Blackhole blackhole) {
    for (String key : map.keySet()) {
      blackhole.consume(key);
      blackhole.consume(map.get(key));
    }
  }

  @Benchmark
  public void usingEntrySetWithForIterator(Blackhole blackhole) {
    Iterator<Entry<String, Integer>> entryIterator = map.entrySet().iterator();
    while (entryIterator.hasNext()) {
      Map.Entry<String, Integer> entry = entryIterator.next();
      blackhole.consume(entry.getKey());
      blackhole.consume(entry.getValue());
    }
  }
}
