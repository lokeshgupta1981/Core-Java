package com.howtodoinjava.core.datetime;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;

public class StopWatchExamples {

  public static void main(String[] args) throws InterruptedException {

    // 1- Custom Stopwatch

    Stopwatch stopwatch = new Stopwatch();
    stopwatch.start();
    Thread.sleep(2000L);
    stopwatch.stop();
    System.out.println(STR."Elapsed Time : \{stopwatch.getElapsedTime()}");
    System.out.println(STR."Elapsed Time : \{stopwatch.getElapsedTime(TimeUnit.NANOSECONDS)}");

    // 2 - Apache Commons

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    Thread.sleep(2000L);
    stopWatch.stop();
    System.out.println(STR."Elapsed Time : \{stopWatch.getTime()}");
    System.out.println(STR."Elapsed Time in Nano: \{stopWatch.getTime(TimeUnit.NANOSECONDS)}");
  }

  // elapsed time in milliseconds
  public long elapsedTimeToMillis(long nanotime) {

    return TimeUnit.MILLISECONDS.convert(
        nanotime, TimeUnit.NANOSECONDS);
  }

  // elapsed time in seconds
  public long elapsedTimeToSeconds(long nanotime) {

    return TimeUnit.SECONDS.convert(
        nanotime, TimeUnit.NANOSECONDS);
  }
}

final class Stopwatch {

  private Instant startTime;
  private Instant endTime;
  private boolean running;

  public void start() {
    if (!running) {
      startTime = Instant.now();
      running = true;
    } else {
      System.out.println("Stopwatch is already running. Use stop() before starting again.");
    }
  }

  public void stop() {
    if (running) {
      endTime = Instant.now();
      running = false;
    } else {
      System.out.println("Stopwatch is not running. Use start() before stopping.");
    }
  }

  public Duration getElapsedDuration() {
    if (running) {
      return Duration.between(startTime, Instant.now());
    } else {
      return Duration.between(startTime, endTime);
    }
  }

  public long getElapsedTime() {
    Duration duration = getElapsedDuration();
    return duration.toMillis();
  }

  public long getElapsedTime(TimeUnit timeUnit) {
    return timeUnit.convert(getElapsedTime(), TimeUnit.MILLISECONDS);
  }
}
