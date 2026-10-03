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
