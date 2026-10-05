# Related Tutorials

1. [Filter a Map by List of Keys](https://howtodoinjava.com/java/stream/filter-map-by-list-of-keys/)
2. [Remove/Update Elements From List using Stream](https://howtodoinjava.com/java/stream/remove-update-stream-elements/)
3. [Java Predicate](https://howtodoinjava.com/java8/how-to-use-predicate-in-java-8/)
4. [Handle Exceptions Thrown in Java Streams](https://howtodoinjava.com/java/stream/handle-exceptions-in-stream/)
5. [Sort a Map by Keys](https://howtodoinjava.com/java/sort/java-sort-map-by-key/)
6. [Sort a Map by Values](https://howtodoinjava.com/java/sort/java-sort-map-by-values/)
7. [Java Stream to List: toList() vs Collectors.toList()](https://howtodoinjava.com/java8/convert-stream-to-list/)
8. [Java Stream max() and min(): Find Max and Min Values](https://howtodoinjava.com/java8/stream-max-min-examples/)
9. [Java Stream Distinct by Multiple Fields (with Examples)](https://howtodoinjava.com/java8/stream-distinct-by-multiple-fields/)

## Java Stream to List

Source code for the article [Java Stream to List: toList() vs Collectors.toList()](https://howtodoinjava.com/java8/convert-stream-to-list/).

- `conversions/StreamToList.java`: `Stream.toList()`, `Collectors.toList()`, `Collectors.toUnmodifiableList()`, `Collectors.toCollection()`, primitive and infinite streams, and the exceptions the unmodifiable lists throw.
- Java 21 or later (the `toList()` examples need Java 16+).

```bash
mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.core.streams.conversions.StreamToList
```

## Java Stream max() and min()

Source code for the article [Java Stream max() and min(): Find Max and Min Values](https://howtodoinjava.com/java8/stream-max-min-examples/).

- `minmax/StreamMaxMin.java`: `Stream.max()`, `Stream.min()`, `IntStream.max()`, `Collectors.maxBy()`, ties and `thenComparing()`, max per group with `groupingBy()` and `toMap()`, `summaryStatistics()`, `Collectors.teeing()`, `Collections.max()`, empty streams and `null` elements.
- Java 21 or later (`Collectors.teeing()` needs Java 12+).

```bash
mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.core.streams.minmax.StreamMaxMin
```

## Java Stream Distinct by Multiple Fields

Source code for the article [Java Stream Distinct by Multiple Fields (with Examples)](https://howtodoinjava.com/java8/stream-distinct-by-multiple-fields/).

- `distinct/DistinctByMultipleFields.java`: a `distinctByKey()` predicate with `ConcurrentHashMap.newKeySet()` and a composite key (`List.of()`, a record key, varargs key extractors), `Collectors.toMap()` with `LinkedHashMap` to keep the first, last or most played element, a `TreeSet` with a `Comparator`, `distinct()` with record `equals()`, counting duplicates, and the pitfalls of predicate reuse, `null` fields, string keys and parallel streams.
- `distinct/DistinctComplexTypes.java`: the original `distinctByKeys()` and `CustomKey` examples.
- Java 21 or later. The `Gatherer`-based `distinctBy()` shown in the article needs Java 24+ and is not part of this module.

```bash
mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.core.streams.distinct.DistinctByMultipleFields
```
