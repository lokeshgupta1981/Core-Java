package com.howtodoinjava.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommonMistakesTest {

  @Test
  void blockingJoinInsideChainCanHang() {
    // Daemon thread: join() ignores interrupts, so the stuck thread must not keep the JVM alive
    ExecutorService single = Executors.newFixedThreadPool(1, Thread.ofPlatform().daemon().factory());
    FruitShop shop = new FruitShop(single);
    CompletableFuture<Integer> total = CompletableFuture.supplyAsync(() -> shop.priceOf("apple") * 2, single);

    // Wrong: the only pool thread waits for a task that needs the same thread
    CompletableFuture<Integer> blocked = total.thenApplyAsync(t -> shop.discountAsync(t).join(), single);
    assertThatThrownBy(() -> blocked.get(1, TimeUnit.SECONDS)).isInstanceOf(TimeoutException.class);
    single.shutdownNow();
  }

  @Test
  void thenComposeDoesNotBlock() {
    try (ExecutorService single = Executors.newFixedThreadPool(1)) {
      FruitShop shop = new FruitShop(single);
      CompletableFuture<Integer> total = CompletableFuture.supplyAsync(() -> shop.priceOf("apple") * 2, single);
      CompletableFuture<Integer> discounted = total.thenCompose(shop::discountAsync);   // 9
      assertThat(discounted.join()).isEqualTo(9);
    }
  }

  @Test
  void exceptionIsLostWithoutHandler() {
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      FruitShop shop = new FruitShop(executor);
      // Wrong: nothing is printed when the lookup fails
      CompletableFuture<Void> lost = CompletableFuture.runAsync(() -> shop.priceOf("kiwi"), executor);
      lost.handle((v, ex) -> null).join();
      assertThat(lost.isCompletedExceptionally()).isTrue();
    }
  }

  @Test
  void exceptionIsLoggedWithWhenComplete() {
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      FruitShop shop = new FruitShop(executor);
      CompletableFuture<Void> refresh = CompletableFuture.runAsync(() -> shop.priceOf("kiwi"), executor)
          .whenComplete((v, ex) -> {
            if (ex != null) {
              System.out.println("Price refresh failed: " + ex.getCause().getMessage()); // Price refresh failed: Unknown fruit: kiwi
            }
          });
      refresh.handle((v, ex) -> null).join();
      assertThat(refresh.isCompletedExceptionally()).isTrue();
    }
  }

  @Test
  void commonPoolSize() {
    int parallelism = ForkJoinPool.getCommonPoolParallelism();
    int cores = Runtime.getRuntime().availableProcessors();
    System.out.println("cores=" + cores + " commonPoolParallelism=" + parallelism);
    assertThat(parallelism).isEqualTo(Math.max(1, cores - 1));
  }
}
