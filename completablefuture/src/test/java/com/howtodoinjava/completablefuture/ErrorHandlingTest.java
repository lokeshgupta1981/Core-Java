package com.howtodoinjava.completablefuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ErrorHandlingTest {

  private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
  private final FruitShop shop = new FruitShop(executor);

  @AfterEach
  void close() {
    executor.close();
  }

  @Test
  void articleSnippet() {
    CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> shop.priceOf("kiwi"), executor);

    CompletableFuture<Integer> safe = price.thenApply(p -> p * 2)
        .exceptionally(ex -> 0);                                            // 0

    CompletableFuture<String> message = price.handle((p, ex) ->
        ex == null ? "Price: " + p : "Error: " + ex.getCause().getMessage());   // "Error: Unknown fruit: kiwi"

    CompletableFuture<Integer> logged = price.whenComplete((p, ex) -> {
      if (ex != null) {
        System.out.println("Lookup failed: " + ex.getCause().getMessage()); // prints Lookup failed: Unknown fruit: kiwi
      }
    });                                                                     // still fails

    assertThat(safe.join()).isEqualTo(0);
    assertThat(message.join()).isEqualTo("Error: Unknown fruit: kiwi");
    assertThatThrownBy(logged::join)
        .isInstanceOf(CompletionException.class)
        .hasCauseInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void exceptionallySkipsFailedStages() {
    AtomicBoolean thenApplyRan = new AtomicBoolean();

    CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> shop.priceOf("kiwi"), executor);
    CompletableFuture<Integer> total = price.thenApply(p -> {
      thenApplyRan.set(true);
      return p * 2;
    });                                                                 // skipped
    CompletableFuture<Integer> safe = total.exceptionally(ex -> 0);     // 0

    assertThat(safe.join()).isEqualTo(0);
    assertThat(thenApplyRan).isFalse();
  }

  @Test
  void exceptionReceivedIsCompletionException() {
    CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> shop.priceOf("kiwi"), executor);
    CompletableFuture<String> error = price.thenApply(p -> p * 2)
        .exceptionally(ex -> -1)
        .thenApply(String::valueOf);
    CompletableFuture<String> type = price.thenApply(p -> "ok")
        .exceptionally(ex -> ex.getClass().getSimpleName() + " / " + ex.getCause().getMessage());

    assertThat(error.join()).isEqualTo("-1");
    assertThat(type.join()).isEqualTo("CompletionException / Unknown fruit: kiwi");
  }

  @Test
  void handleSeesResultOrException() {
    CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> shop.priceOf("kiwi"), executor);
    CompletableFuture<String> message = price.handle((p, ex) ->
        ex == null ? "Price: " + p : "Error: " + ex.getCause().getMessage());   // "Error: Unknown fruit: kiwi"

    CompletableFuture<Integer> apple = CompletableFuture.supplyAsync(() -> shop.priceOf("apple"), executor);
    CompletableFuture<String> ok = apple.handle((p, ex) ->
        ex == null ? "Price: " + p : "Error: " + ex.getCause().getMessage());   // "Price: 5"

    assertThat(message.join()).isEqualTo("Error: Unknown fruit: kiwi");
    assertThat(ok.join()).isEqualTo("Price: 5");
  }

  @Test
  void whenCompleteDoesNotChangeResult() {
    List<String> log = new CopyOnWriteArrayList<>();

    CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> shop.priceOf("kiwi"), executor);
    CompletableFuture<Integer> logged = price.whenComplete((p, ex) -> {
      if (ex != null) {
        log.add("Lookup failed: " + ex.getCause().getMessage());
      }
    });                                                   // still fails

    assertThatThrownBy(logged::join)
        .isInstanceOf(CompletionException.class)
        .hasCauseInstanceOf(IllegalArgumentException.class);
    assertThat(log).containsExactly("Lookup failed: Unknown fruit: kiwi");
  }
}
