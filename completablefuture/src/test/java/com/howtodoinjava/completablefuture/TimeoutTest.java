package com.howtodoinjava.completablefuture;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeoutTest {

  private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
  private final FruitShop shop = new FruitShop(executor);

  @AfterEach
  void close() {
    executor.shutdownNow();
  }

  @Test
  void orTimeoutFailsWithTimeoutException() {
    CompletableFuture<Integer> slow = shop.priceAsync("mango", Duration.ofSeconds(2));
    CompletableFuture<Integer> fallback = slow.orTimeout(500, TimeUnit.MILLISECONDS)   // TimeoutException after 500 ms
        .exceptionally(ex -> -1);                                                     // -1

    assertThat(fallback.join()).isEqualTo(-1);
    assertThatThrownBy(slow::join)
        .isInstanceOf(CompletionException.class)
        .hasCauseInstanceOf(TimeoutException.class);
  }

  @Test
  void completeOnTimeoutUsesDefault() {
    CompletableFuture<Integer> late = shop.priceAsync("mango", Duration.ofSeconds(2));
    CompletableFuture<Integer> withDefault = late.completeOnTimeout(0, 500, TimeUnit.MILLISECONDS);  // 0

    CompletableFuture<Integer> quick = shop.priceAsync("mango", Duration.ofMillis(50));
    CompletableFuture<Integer> real = quick.completeOnTimeout(0, 500, TimeUnit.MILLISECONDS);  // 7

    assertThat(withDefault.join()).isEqualTo(0);
    assertThat(real.join()).isEqualTo(7);
  }

  @Test
  void timeoutDoesNotStopTheTask() {
    AtomicBoolean finished = new AtomicBoolean();
    CompletableFuture<Integer> slow = CompletableFuture.supplyAsync(() -> {
      Delays.simulateDelay(Duration.ofMillis(300));
      finished.set(true);
      return 7;
    }, executor);
    slow.orTimeout(50, TimeUnit.MILLISECONDS).exceptionally(ex -> -1).join();
    assertThat(finished).isFalse();

    Delays.simulateDelay(Duration.ofMillis(400));
    assertThat(finished).isTrue();   // the supplier kept running after the timeout
  }
}
