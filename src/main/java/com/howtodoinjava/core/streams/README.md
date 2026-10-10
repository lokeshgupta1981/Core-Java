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
* [Java Streams Tutorial: Every Stream Guide Grouped by Task](https://howtodoinjava.com/java/stream/java-streams-guide/)
* [Java Stream API Examples: Create, Filter, Collect (Java 25)](https://howtodoinjava.com/java/stream/java-streams-by-examples/)
* [Functional Interface in Java: Rules, JDK Types and Generics](https://howtodoinjava.com/java/stream/functional-interface-tutorial/)
* [Primitive Type Streams in Java: IntStream, LongStream and More](https://howtodoinjava.com/java/stream/primitive-type-streams/)
* [Filter a Map by Keys and Values Using Java Streams](https://howtodoinjava.com/java/stream/filter-map-keys-values-both/)
* [Java Stream contains(), containsAny() and containsAll()](https://howtodoinjava.com/java/stream/contains-containsany-containsall/)
* [Filter Nested Collections with Java Streams (flatMap, anyMatch)](https://howtodoinjava.com/java/stream/filter-nested-collections/)
* [Java Stream mapMulti() with Examples and flatMap Comparison](https://howtodoinjava.com/java/stream/stream-mapmulti-example/)
* [Stream Has Already Been Operated Upon or Closed: Causes and Fix](https://howtodoinjava.com/java/stream/stream-has-already-been-operated-upon-or-closed/)
* [Java Stream With Index: Iterate Over a Stream With Indices](https://howtodoinjava.com/java/stream/iterate-over-stream-with-indices/)
* [How to Debug Java Streams: peek() and IntelliJ Stream Trace](https://howtodoinjava.com/java/stream/debugging-java-streams/)
* [Java Stream map(): Transform Elements with Examples](https://howtodoinjava.com/java8/stream-map-example/)
* [Java Stream flatMap(): Flatten Nested Lists with Examples](https://howtodoinjava.com/java8/stream-flatmap-example/)
* [map() vs flatMap() in Java Streams: Difference with Examples](https://howtodoinjava.com/java8/stream-map-vs-flatmap/)
* [Java Stream filter(): Keep Matching Elements with Examples](https://howtodoinjava.com/java8/java-stream-filter-example/)
* [Java Stream Filter with Multiple Conditions: 3 Ways](https://howtodoinjava.com/java8/stream-multiple-filters-example/)
* [Java Stream sorted(): How to Sort a Stream in Java](https://howtodoinjava.com/java8/stream-sorted-method/)
* [Java Stream: Sort by Multiple Fields With thenComparing()](https://howtodoinjava.com/java8/sort-stream-multiple-fields/)
* [Java Stream distinct(): Remove Duplicates, Keep Order](https://howtodoinjava.com/java8/java-stream-distinct-examples/)
* [How to Find Duplicates in a Java Stream (Count and Remove)](https://howtodoinjava.com/java8/stream-find-remove-duplicates/)
* [Java Stream count(): Count Elements, Matches and Groups](https://howtodoinjava.com/java8/stream-count-elements-example/)
* [Java Stream anyMatch(), allMatch() and noneMatch() Examples](https://howtodoinjava.com/java8/stream-anymatch-example/)
* [Java Stream findFirst() vs findAny(): Differences and Examples](https://howtodoinjava.com/java8/stream-findfirst-findany/)
* [Get the Last Element of a Stream in Java: reduce() and getLast()](https://howtodoinjava.com/java8/java-stream-get-last-element/)
* [Java Stream: Get Object With Max Date From a List](https://howtodoinjava.com/java8/stream-get-object-with-max-date/)
* [Java Stream limit() and skip() With Pagination Examples](https://howtodoinjava.com/java8/java-stream-limit-method-example/)
* [If-Else Logic in Java Streams: filter, map and partitioningBy](https://howtodoinjava.com/java8/stream-if-else-logic/)
* [Java Stream peek(): How It Works and When It Is Skipped](https://howtodoinjava.com/java8/java-stream-peek-example/)
* [Join a Stream of Strings in Java: joining() and String.join()](https://howtodoinjava.com/java8/join-stream-of-strings/)
* [Java Stream concat(): Merge Streams, Append and Prepend Items](https://howtodoinjava.com/java8/stream-concat-example/)
* [Java Stream toArray(): Convert a Stream to an Array](https://howtodoinjava.com/java8/convert-stream-to-array/)
* [Convert Iterable or Iterator to Stream in Java (StreamSupport)](https://howtodoinjava.com/java8/iterable-iterator-to-stream/)
* [Java IntStream Guide: range(), iterate(), sum() and More](https://howtodoinjava.com/java8/intstream-examples/)
* [Java Boxed Stream: IntStream boxed() to List, Set and Array](https://howtodoinjava.com/java8/java8-boxed-intstream/)
