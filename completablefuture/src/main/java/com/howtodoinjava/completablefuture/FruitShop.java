package com.howtodoinjava.completablefuture;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static com.howtodoinjava.completablefuture.Delays.simulateDelay;

/**
 * A small shop with slow lookups. Each method waits 100 ms to look like a remote call.
 */
public class FruitShop {

  private static final Duration CALL_TIME = Duration.ofMillis(100);

  private final Map<String, Integer> prices = Map.of("apple", 5, "banana", 3, "mango", 7);
  private final Map<String, Integer> stock = Map.of("apple", 12, "banana", 20, "mango", 0);
  private final Executor executor;

  public FruitShop(Executor executor) {
    this.executor = executor;
  }

  /** Blocking lookup: returns the price or throws IllegalArgumentException. */
  public int priceOf(String fruit) {
    simulateDelay(CALL_TIME);
    Integer price = prices.get(fruit);
    if (price == null) {
      throw new IllegalArgumentException("Unknown fruit: " + fruit);
    }
    return price;
  }

  /** Blocking lookup: returns the number of items in stock. */
  public int stockOf(String fruit) {
    simulateDelay(CALL_TIME);
    return stock.getOrDefault(fruit, 0);
  }

  /** Async call to a coupon service: returns the amount with 10% off. */
  public CompletableFuture<Integer> discountAsync(int amount) {
    return CompletableFuture.supplyAsync(() -> {
      simulateDelay(CALL_TIME);
      return amount * 90 / 100;
    }, executor);
  }

  /** Async price lookup that takes the given time. */
  public CompletableFuture<Integer> priceAsync(String fruit, Duration callTime) {
    return CompletableFuture.supplyAsync(() -> {
      simulateDelay(callTime);
      return prices.getOrDefault(fruit, 0);
    }, executor);
  }
}
