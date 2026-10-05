package com.howtodoinjava.java25.libraries;

import static com.howtodoinjava.java25.Waits.pause;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;

/** JEP 505 (fifth preview): structured concurrency. */
public class BookLookup {

  public record Offer(String title, int price, int copies) {
  }

  private final Duration stockDelay;
  private final boolean stockServiceDown;

  public BookLookup(Duration stockDelay, boolean stockServiceDown) {
    this.stockDelay = stockDelay;
    this.stockServiceDown = stockServiceDown;
  }

  public Offer offer(String title) throws InterruptedException {
    try (var scope = StructuredTaskScope.open()) {
      Subtask<Integer> price = scope.fork(() -> findPrice(title));
      Subtask<Integer> copies = scope.fork(() -> findCopies(title));
      scope.join();
      return new Offer(title, price.get(), copies.get());
    }
  }

  public Offer offerWithTimeout(String title, Duration timeout) throws InterruptedException {
    try (var scope = StructuredTaskScope.open(Joiner.<Integer>awaitAllSuccessfulOrThrow(),
        cf -> cf.withTimeout(timeout))) {
      Subtask<Integer> price = scope.fork(() -> findPrice(title));
      Subtask<Integer> copies = scope.fork(() -> findCopies(title));
      scope.join();
      return new Offer(title, price.get(), copies.get());
    }
  }

  public String memberSeenBySubtask() throws InterruptedException {
    return ScopedValue.where(LibraryContext.MEMBER, "Lokesh").call(() -> {
      try (var scope = StructuredTaskScope.<String>open()) {
        Subtask<String> task = scope.fork(() -> LibraryContext.MEMBER.get());
        scope.join();
        return task.get();
      }
    });
  }

  private int findPrice(String title) throws InterruptedException {
    pause(Duration.ofMillis(100));
    return title.length() * 3;
  }

  private int findCopies(String title) throws InterruptedException {
    pause(stockDelay);
    if (stockServiceDown) {
      throw new IllegalStateException("stock service unavailable");
    }
    return 2;
  }
}
