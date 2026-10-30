package com.howtodoinjava.ids;

import java.time.Instant;
import java.util.UUID;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import com.github.f4b6a3.uuid.UuidCreator;

/**
 * Prints example IDs of each kind, as shown in the article.
 */
public class SampleIds {

  public static void main(String[] args) throws InterruptedException {
    System.out.println("UUIDv4: " + UUID.randomUUID());
    System.out.println("UUIDv4: " + UUID.randomUUID());

    UUID first = UuidCreator.getTimeOrderedEpoch();
    Thread.sleep(5);
    UUID second = UuidCreator.getTimeOrderedEpoch();
    System.out.println("UUIDv7: " + first);
    System.out.println("UUIDv7: " + second);

    long millis = first.getMostSignificantBits() >>> 16;
    System.out.println("First UUIDv7 was created at " + Instant.ofEpochMilli(millis));

    Ulid ulid = UlidCreator.getMonotonicUlid();
    System.out.println("ULID: " + ulid + " (created at " + ulid.getInstant() + ")");
    System.out.println("ULID as UUID: " + ulid.toUuid());

    for (int i = 0; i < 3; i++) {
      System.out.println("Same millisecond: " + UuidCreator.getTimeOrderedEpoch());
    }
  }
}
