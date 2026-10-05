package com.howtodoinjava.completablefuture;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CombineTest {

  private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
  private final FruitShop shop = new FruitShop(executor);

  @AfterEach
  void close() {
    executor.close();
  }

  @Test
  void thenCombine() {
    CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> shop.priceOf("apple"), executor); // 5
    CompletableFuture<Integer> stock = CompletableFuture.supplyAsync(() -> shop.stockOf("apple"), executor); // 12
    CompletableFuture<Integer> value = price.thenCombine(stock, (p, s) -> p * s);                           // 60

    assertThat(value.join()).isEqualTo(60);
  }

  @Test
  void allOfCollectsAllResults() {
    long start = System.nanoTime();

    List<String> fruits = List.of("apple", "banana", "mango");
    List<CompletableFuture<Integer>> futures = fruits.stream()
        .map(fruit -> CompletableFuture.supplyAsync(() -> shop.priceOf(fruit), executor))
        .toList();

    CompletableFuture<Void> all = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    CompletableFuture<List<Integer>> prices = all.thenApply(v -> futures.stream()
        .map(CompletableFuture::join)
        .toList());                                                            // [5, 3, 7]

    List<Integer> result = prices.join();
    long millis = Duration.ofNanos(System.nanoTime() - start).toMillis();
    System.out.println("allOf prices " + result + " in " + millis + " ms");

    assertThat(result).containsExactly(5, 3, 7);
    assertThat(millis).isLessThan(250);   // three 100 ms calls in parallel, not 300 ms
  }

  @Test
  void allOfFailsWhenOneFails() {
    List<CompletableFuture<Integer>> futures = List.of("apple", "kiwi").stream()
        .map(fruit -> CompletableFuture.supplyAsync(() -> shop.priceOf(fruit), executor))
        .toList();
    CompletableFuture<Void> all = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

    all.handle((v, ex) -> null).join();
    assertThat(all.isCompletedExceptionally()).isTrue();
    assertThat(futures.get(0).join()).isEqualTo(5);
  }

  @Test
  void anyOfReturnsFirst() {
    CompletableFuture<Integer> slow = shop.priceAsync("apple", Duration.ofMillis(500));   // 5 after 500 ms
    CompletableFuture<Integer> fast = shop.priceAsync("banana", Duration.ofMillis(50));   // 3 after 50 ms
    CompletableFuture<Object> first = CompletableFuture.anyOf(slow, fast);                // 3

    assertThat(first.join()).isEqualTo(3);
  }
}
