package com.howtodoinjava.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChainTest {

  private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
  private final FruitShop shop = new FruitShop(executor);

  @AfterEach
  void close() {
    executor.close();
  }

  @Test
  void thenApplyAndThenAccept() {
    CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> shop.priceOf("apple"), executor); // 5
    CompletableFuture<Integer> total = price.thenApply(p -> p * 2);              // 10
    CompletableFuture<String> label = total.thenApply(t -> "Total: " + t);       // "Total: 10"
    CompletableFuture<Void> shown = label.thenAccept(System.out::println);       // prints Total: 10

    shown.join();
    assertThat(total.join()).isEqualTo(10);
    assertThat(label.join()).isEqualTo("Total: 10");
  }

  @Test
  void thenApplyVsThenCompose() {
    CompletableFuture<Integer> total = CompletableFuture.supplyAsync(() -> shop.priceOf("apple") * 2, executor); // 10

    CompletableFuture<CompletableFuture<Integer>> nested = total.thenApply(t -> shop.discountAsync(t));
    CompletableFuture<Integer> discounted = total.thenCompose(t -> shop.discountAsync(t));   // 9

    assertThat(nested.join().join()).isEqualTo(9);
    assertThat(discounted.join()).isEqualTo(9);
  }

  @Test
  void asyncVariantRunsOnGivenExecutor() {
    CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> shop.priceOf("apple"), executor);
    CompletableFuture<Integer> doubled = price.thenApplyAsync(p -> p * 2, executor);   // 10
    assertThat(doubled.join()).isEqualTo(10);

    CompletableFuture<Boolean> onVirtual = price.thenApplyAsync(p -> Thread.currentThread().isVirtual(), executor);
    assertThat(onVirtual.join()).isTrue();
  }
}
