package com.howtodoinjava.java25;

import java.time.Duration;

/** Simulates a slow call (a remote lookup) in the examples. */
public final class Waits {

  private Waits() {
  }

  public static void pause(Duration duration) throws InterruptedException {
    Thread.sleep(duration);
  }
}
