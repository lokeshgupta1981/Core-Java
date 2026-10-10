# Sorting in Java

Source code for the article [How to Sort an Array, List, Map or Stream in Java](https://howtodoinjava.com/java/sort/java-sorting/).

- `SortingGuide.java`: every example of the article (arrays, ranges, `parallelSort()`, lists, streams,
  `Comparable` vs `Comparator`, comparator chains, `nullsFirst()`/`nullsLast()`, maps, `TreeMap`/`TreeSet`,
  case-insensitive and `Collator` sorting, stable sort, comparator mistakes). Each line prints the result shown in the article.
- `ComparableExamples.java`, `ComparatorExamples.java`, `FirstNameSorter.java`, `User.java`: earlier
  `Comparable` and `Comparator` examples on a `User` record.

Requirements: Java 21 or later (tested on JDK 25).

`SortingGuide` is a standalone class with no dependencies. Run it from the repository root:

```bash
javac -d target/classes src/main/java/com/howtodoinjava/core/sorting/SortingGuide.java
java -cp target/classes com.howtodoinjava.core.sorting.SortingGuide
```
* [Java Stream Sort With Null Values: nullsFirst and nullsLast](https://howtodoinjava.com/java/sort/stream-sort-with-null-values/)
* [Sort a String Alphabetically in Java (Arrays.sort, Streams)](https://howtodoinjava.com/java/sort/sort-string-chars-alphabetically/)
* [Java Collections.sort(): Natural Order, Comparator, Stability](https://howtodoinjava.com/java/sort/collections-sort/)
* [Sort Array in Java: Arrays.sort(), parallelSort() and Ranges](https://howtodoinjava.com/java/sort/java-array-sorting/)
* [Sort a Map by Values in Java (Ascending, Descending, Top N)](https://howtodoinjava.com/java/sort/java-sort-map-by-values/)
* [Sort a Map by Keys in Java: TreeMap and Streams](https://howtodoinjava.com/java/sort/java-sort-map-by-key/)
