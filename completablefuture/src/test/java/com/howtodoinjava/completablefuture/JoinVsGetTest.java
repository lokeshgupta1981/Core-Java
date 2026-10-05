package com.howtodoinjava.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JoinVsGetTest {

  private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
  private final FruitShop shop = new FruitShop(executor);

  @AfterEach
  void close() {
    executor.close();
  }

  @Test
  void joinThrowsCompletionException() {
    assertThatThrownBy(() -> {
      int price = CompletableFuture.supplyAsync(() -> shop.priceOf("kiwi"), executor).join();
    })
        .isInstanceOf(CompletionException.class)
        .hasMessage("java.lang.IllegalArgumentException: Unknown fruit: kiwi");
  }

  @Test
  void getThrowsExecutionException() {
    CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> shop.priceOf("kiwi"), executor);
    assertThatThrownBy(future::get)
        .isInstanceOf(ExecutionException.class)
        .hasCauseInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void safeGetWithTimeout() {
    CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> shop.priceOf("kiwi"), executor);
    String outcome = priceOrMessage(future);
    assertThat(outcome).isEqualTo("Lookup failed: Unknown fruit: kiwi");

    CompletableFuture<Integer> apple = CompletableFuture.supplyAsync(() -> shop.priceOf("apple"), executor);
    assertThat(priceOrMessage(apple)).isEqualTo("Price: 5");
  }

  private static String priceOrMessage(CompletableFuture<Integer> future) {
    String outcome;
    try {
      int price = future.get(1, TimeUnit.SECONDS);
      outcome = "Price: " + price;
    } catch (ExecutionException e) {
      outcome = "Lookup failed: " + e.getCause().getMessage();
    } catch (TimeoutException e) {
      outcome = "Lookup timed out";
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      outcome = "Interrupted";
    }
    return outcome;
  }
}
