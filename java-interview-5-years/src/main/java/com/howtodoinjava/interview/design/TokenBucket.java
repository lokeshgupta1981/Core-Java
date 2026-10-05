package com.howtodoinjava.interview.design;

import java.util.function.LongSupplier;

/** Token bucket rate limiter: refills tokens at a fixed rate up to a capacity. */
public class TokenBucket {

  private final long capacity;
  private final double tokensPerNano;
  private final LongSupplier clock;
  private double tokens;
  private long lastRefill;

  public TokenBucket(long capacity, long tokensPerSecond, LongSupplier nanoClock) {
    this.capacity = capacity;
    this.tokensPerNano = tokensPerSecond / 1_000_000_000.0;
    this.clock = nanoClock;
    this.tokens = capacity;
    this.lastRefill = nanoClock.getAsLong();
  }

  public synchronized boolean tryAcquire() {
    long now = clock.getAsLong();
    tokens = Math.min(capacity, tokens + (now - lastRefill) * tokensPerNano);
    lastRefill = now;
    if (tokens >= 1) {
      tokens -= 1;
      return true;
    }
    return false;
  }
}
