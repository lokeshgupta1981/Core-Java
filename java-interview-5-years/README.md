Source code for the article https://howtodoinjava.com/?p=44112

# Java Interview Questions for 5 Years Experience

Runnable answers for the article: core Java, Java 17 to 25 features, collections internals,
concurrency, JVM memory, streams, design patterns, Spring `@Transactional` and testing.

## Versions

- Java 25 (tested on Temurin 25.0.4.1)
- Maven 3.9+
- Spring Framework 7.0.9 (spring-context, spring-tx; no Spring Boot needed)
- JUnit 6.1.3, AssertJ 3.27.7, Mockito 5.24.0

## Run

```bash
# all answers as tests (24 tests)
mvn test

# @Transactional proxy and rollback demo
mvn compile exec:java

# memory leak until OutOfMemoryError, with a heap dump in target/
java -Xmx64m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=target \
     -cp target/classes com.howtodoinjava.interview.jvm.LeakDemo
```

## Where each answer lives

| Topic | Test or class |
|---|---|
| Immutable class, Integer cache, string pool, try-with-resources | `CoreJavaTest` |
| Records, sealed types, pattern matching, virtual threads, scoped values, sequenced collections | `ModernJavaTest` |
| HashMap mutable key, ConcurrentHashMap merge, fail-fast iterators | `CollectionsTest` |
| volatile vs AtomicInteger, ReentrantLock, ExecutorService, CompletableFuture | `ConcurrencyTest` |
| map/flatMap, laziness, toMap duplicates | `StreamsTest` |
| Singleton, LRU cache, token bucket rate limiter | `DesignTest` |
| @Transactional self-invocation and rollback rules | `SpringTransactionalTest`, `TransactionalDemo` |
| Mockito unit test | `PriceServiceTest` |
| Memory leak and OutOfMemoryError | `jvm/LeakDemo` |
