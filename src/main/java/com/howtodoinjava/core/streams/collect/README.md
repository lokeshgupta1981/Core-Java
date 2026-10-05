# Stream collect examples

| File | Article |
|---|---|
| `CollectStreamItemsToMap.java` | [Java Stream to Map with Collectors.toMap()](https://howtodoinjava.com/java8/collect-stream-to-map/) |
| `GroupingByExamples.java` | [Java Stream groupingBy()](https://howtodoinjava.com/java/stream/collectors-groupingby/) |
| `StreamCollectToImmutableCollections.java` | [Collect Stream to Immutable Collections](https://howtodoinjava.com/java/collections/collect-stream-into-immutable-collection/) |

`CollectStreamItemsToMap.java` runs every snippet from the toMap() article: the three `Collectors.toMap()` overloads, the `IllegalStateException` on duplicate keys and the merge functions that fix it, the `NullPointerException` on null values and the `collect(HashMap::new, ...)` workaround, `LinkedHashMap` and `TreeMap` suppliers and `toUnmodifiableMap()`.

`GroupingByExamples.java` groups a list of `Employee` records by department and prints every map that the groupingBy() article shows: lists per group, `mapping()`, `counting()`, `summingInt()`, `averagingDouble()`, `maxBy()` with `collectingAndThen()`, nested `groupingBy()`, a record as the composite key, `TreeMap` and `LinkedHashMap` factories, sorting the result by value, `partitioningBy()`, `filtering()` and `groupingByConcurrent()`.

## Versions

- JDK 25 (tested with Temurin 25.0.4.1), no external dependencies

## Run

```bash
javac -d out src/main/java/com/howtodoinjava/core/streams/collect/CollectStreamItemsToMap.java
java -cp out com.howtodoinjava.core.streams.collect.CollectStreamItemsToMap
```
