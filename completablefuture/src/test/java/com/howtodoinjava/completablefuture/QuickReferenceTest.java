package com.howtodoinjava.completablefuture;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** The quick-reference snippet from the intro of the article. */
class QuickReferenceTest {

  @Test
  void quickReference() {
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {   // close() waits for all tasks
      FruitShop shop = new FruitShop(executor);

      CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> shop.priceOf("apple"), executor); // 5
      CompletableFuture<Integer> stock = CompletableFuture.supplyAsync(() -> shop.stockOf("apple"), executor); // 12

      CompletableFuture<Integer> total = price.thenApply(p -> p * 2);                  // 10
      CompletableFuture<Integer> discounted = total.thenCompose(shop::discountAsync);  // 9
      CompletableFuture<Integer> value = price.thenCombine(stock, (p, s) -> p * s);    // 60

      CompletableFuture<Integer> unknown = CompletableFuture.supplyAsync(() -> shop.priceOf("kiwi"), executor);
      CompletableFuture<Integer> safe = unknown.exceptionally(ex -> 0);                // 0

      CompletableFuture<Integer> slow = shop.priceAsync("mango", Duration.ofSeconds(2));
      CompletableFuture<Integer> limited = slow.completeOnTimeout(-1, 500, TimeUnit.MILLISECONDS);  // -1

      int result = discounted.join();                                                  // 9

      assertThat(price.join()).isEqualTo(5);
      assertThat(stock.join()).isEqualTo(12);
      assertThat(total.join()).isEqualTo(10);
      assertThat(value.join()).isEqualTo(60);
      assertThat(safe.join()).isEqualTo(0);
      assertThat(limited.join()).isEqualTo(-1);
      assertThat(result).isEqualTo(9);
    }
  }
}
