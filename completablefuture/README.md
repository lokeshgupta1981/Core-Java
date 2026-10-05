Source code for the article https://howtodoinjava.com/?p=44238

# Java CompletableFuture Tutorial with Examples

Runnable examples for creating, chaining, combining and timing out `CompletableFuture` tasks, handling their errors, and the common mistakes (blocking inside a chain, lost exceptions, blocking calls in the common pool). Every snippet in the article has a JUnit test.

## Versions

- JDK 25 (tested with Temurin 25.0.4.1)
- Maven 3.9.16
- JUnit 6.1.3, AssertJ 3.27.7
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0

## Run

```bash
# all tests
mvn test

# print which thread runs a task: common pool, fixed pool, virtual thread
mvn compile
java -cp target/classes com.howtodoinjava.completablefuture.ThreadNamesDemo
```

## Files

| File | What it shows |
|---|---|
| `FruitShop` | Slow lookups used by all examples: `priceOf("apple")` returns 5, `stockOf("apple")` returns 12, `discountAsync(10)` returns 9 |
| `Delays` | `simulateDelay(Duration)` helper that makes a call take time |
| `ThreadNamesDemo` | Thread names for `supplyAsync()` with and without an executor |
| `QuickReferenceTest` | The quick-reference snippet from the intro |
| `CreateFutureTest` | `supplyAsync()`, `runAsync()`, own thread pool, virtual threads, `complete()` |
| `ChainTest` | `thenApply()`, `thenAccept()`, `thenApply()` vs `thenCompose()`, `thenApplyAsync()` |
| `CombineTest` | `thenCombine()`, `allOf()`, `anyOf()` |
| `ErrorHandlingTest` | `exceptionally()`, `handle()`, `whenComplete()` |
| `TimeoutTest` | `orTimeout()`, `completeOnTimeout()`, the task keeps running after a timeout |
| `JoinVsGetTest` | `join()` vs `get()` exceptions, safe `get()` with a timeout |
| `CommonMistakesTest` | Blocking `join()` inside a chain, lost exceptions, common pool size |
