package com.howtodoinjava.ids;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

public final class UuidV7 {

  private static final SecureRandom RANDOM = new SecureRandom();

  private UuidV7() {
  }

  public static UUID next() {
    return of(System.currentTimeMillis());
  }

  public static UUID of(long epochMillis) {
    long msb = (epochMillis << 16) | 0x7000L | (RANDOM.nextLong() & 0x0FFFL);
    long lsb = (RANDOM.nextLong() & 0x3FFFFFFFFFFFFFFFL) | 0x8000000000000000L;
    return new UUID(msb, lsb);
  }

  public static Instant createdAt(UUID uuid) {
    return Instant.ofEpochMilli(uuid.getMostSignificantBits() >>> 16);
  }
}
