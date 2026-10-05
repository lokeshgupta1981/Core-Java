package com.howtodoinjava.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateFutureTest {

  private final FruitShop shop = new FruitShop(Runnable::run);

  @Test
  void plainFutureOnlyBlocks() throws Exception {
    try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
      Future<Integer> future = pool.submit(() -> shop.priceOf("apple"));
      int price = future.get();   // 5
      assertThat(price).isEqualTo(5);
    }
  }

  @Test
  void supplyAsyncAndRunAsyncOnDefaultExecutor() {
    CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> shop.priceOf("apple"));    // 5
    CompletableFuture<Void> opened = CompletableFuture.runAsync(() -> System.out.println("Shop opened")); // null, prints Shop opened

    assertThat(price.join()).isEqualTo(5);
    assertThat(opened.join()).isNull();
  }

  @Test
  void supplyAsyncWithOwnPool() {
    try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
      CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> shop.priceOf("apple"), pool);
      CompletableFuture<String> thread = CompletableFuture.supplyAsync(() -> Thread.currentThread().getName(), pool);
      assertThat(price.join()).isEqualTo(5);
      assertThat(thread.join()).startsWith("pool-");
    }
  }

  @Test
  void supplyAsyncWithVirtualThreads() {
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> shop.priceOf("apple"), executor);  // 5
      CompletableFuture<Integer> stock = CompletableFuture.supplyAsync(() -> shop.stockOf("apple"), executor);  // 12
      CompletableFuture<Boolean> virtual = CompletableFuture.supplyAsync(() -> Thread.currentThread().isVirtual(), executor);

      assertThat(price.join()).isEqualTo(5);
      assertThat(stock.join()).isEqualTo(12);
      assertThat(virtual.join()).isTrue();
    }
  }

  @Test
  void manualCompletion() {
    CompletableFuture<Integer> price = new CompletableFuture<>();
    boolean done = price.complete(5);   // true
    assertThat(done).isTrue();
    assertThat(price.join()).isEqualTo(5);
  }
}
