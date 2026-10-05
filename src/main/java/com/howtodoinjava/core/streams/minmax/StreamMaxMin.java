package com.howtodoinjava.core.streams.minmax;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Finds the max and min elements of a stream with Stream.max(), Stream.min(), IntStream.max(),
 * Collectors.maxBy()/minBy(), groupingBy() and toMap() per group, summaryStatistics(), teeing(),
 * reduce() and Collections.max(), and shows how ties, empty streams and null elements behave.
 */
public class StreamMaxMin {

  /** A recipe with a cuisine, a cooking time in minutes and a number of likes. */
  record Recipe(String name, String cuisine, int minutes, int likes) {
    @Override
    public String toString() {
      return name;
    }
  }

  static final List<Recipe> RECIPES = List.of(
      new Recipe("pasta", "italian", 20, 120),
      new Recipe("pizza", "italian", 45, 150),
      new Recipe("risotto", "italian", 35, 150),
      new Recipe("tacos", "mexican", 15, 90),
      new Recipe("burrito", "mexican", 25, 110),
      new Recipe("curry", "indian", 40, 150),
      new Recipe("dal", "indian", 30, 80));

  public static void main(final String[] args) {
    List<Recipe> recipes = RECIPES;
    List<Integer> numbers = List.of(4, 8, 15, 16, 23, 42);

    // 0. Quick reference
    Integer max = numbers.stream().max(Comparator.naturalOrder()).orElseThrow();
    Integer min = numbers.stream().min(Comparator.naturalOrder()).orElseThrow();
    int maxInt = numbers.stream().mapToInt(Integer::intValue).max().orElseThrow();
    Recipe mostLiked = recipes.stream().max(Comparator.comparing(Recipe::likes)).orElseThrow();
    Recipe quickest = recipes.stream().min(Comparator.comparing(Recipe::minutes)).orElseThrow();
    Optional<Recipe> top = recipes.stream().collect(Collectors.maxBy(Comparator.comparing(Recipe::likes)));
    Optional<Integer> none = Stream.<Integer>empty().max(Comparator.naturalOrder());
    print("max", max);
    print("min", min);
    print("maxInt", maxInt);
    print("mostLiked", mostLiked);
    print("quickest", quickest);
    print("top", top);
    print("none", none);

    // 2. Dates
    List<LocalDate> dates = List.of(
        LocalDate.of(2026, 3, 15), LocalDate.of(2025, 12, 1), LocalDate.of(2026, 7, 4));
    LocalDate latest = dates.stream().max(Comparator.naturalOrder()).orElseThrow();
    LocalDate earliest = dates.stream().min(Comparator.naturalOrder()).orElseThrow();
    LocalDate latestOld = dates.stream().max(Comparator.comparing(LocalDate::toEpochDay)).orElseThrow();
    print("latest", latest);
    print("earliest", earliest);
    print("latestOld (toEpochDay)", latestOld);

    // 3. Numbers
    Integer maxNumber = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).max(Integer::compare).orElseThrow();
    Integer minNumber = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).min(Integer::compare).orElseThrow();
    Integer reversedMax = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).max(Comparator.reverseOrder()).orElseThrow();
    print("maxNumber", maxNumber);
    print("minNumber", minNumber);
    print("max with reverseOrder()", reversedMax);

    // 4. Chars and Strings
    String maxChar = Stream.of("H", "T", "D", "I", "J").max(Comparator.naturalOrder()).orElseThrow();
    String minChar = Stream.of("H", "T", "D", "I", "J").min(Comparator.naturalOrder()).orElseThrow();
    String longest = Stream.of("kiwi", "banana", "cherry", "fig").max(Comparator.comparing(String::length)).orElseThrow();
    String shortest = Stream.of("kiwi", "banana", "cherry", "fig").min(Comparator.comparing(String::length)).orElseThrow();
    char maxLetter = (char) "streams".chars().max().orElseThrow();
    print("maxChar", maxChar);
    print("minChar", minChar);
    print("longest", longest);
    print("shortest", shortest);
    print("maxLetter", maxLetter);

    // 5. Objects by field
    Comparator<Recipe> byLikes = Comparator.comparingInt(Recipe::likes);
    Recipe leastLiked = recipes.stream().min(byLikes).orElseThrow();
    Recipe slowest = recipes.stream().max(Comparator.comparingInt(Recipe::minutes)).orElseThrow();
    int maxLikes = recipes.stream().mapToInt(Recipe::likes).max().orElseThrow();
    print("leastLiked", leastLiked);
    print("slowest", slowest);
    print("maxLikes", maxLikes);

    // 6. Ties and multiple fields
    Recipe firstOfTies = recipes.stream().max(byLikes).orElseThrow();
    Recipe minFirstOfTies = Stream.of(new Recipe("a", "x", 10, 1), new Recipe("b", "x", 10, 1))
        .min(Comparator.comparingInt(Recipe::minutes)).orElseThrow();
    Recipe parallelTie = recipes.parallelStream().max(byLikes).orElseThrow();
    Recipe collectionsTie = Collections.max(recipes, byLikes);
    Recipe likedThenQuick = recipes.stream()
        .max(Comparator.comparingInt(Recipe::likes)
            .thenComparing(Recipe::minutes, Comparator.reverseOrder()))
        .orElseThrow();
    List<Recipe> allTop = recipes.stream().filter(r -> r.likes() == maxLikes).toList();
    print("firstOfTies (max)", firstOfTies);
    print("minFirstOfTies (min)", minFirstOfTies);
    print("parallelTie", parallelTie);
    print("collectionsTie", collectionsTie);
    print("likedThenQuick", likedThenQuick);
    print("allTop", allTop);
    // last of ties: reduce keeping the right operand on equal
    Recipe lastOfTies = recipes.stream()
        .reduce((a, b) -> byLikes.compare(b, a) >= 0 ? b : a).orElseThrow();
    print("lastOfTies", lastOfTies);

    // 7. Max per group
    Map<String, Optional<Recipe>> topPerCuisine = recipes.stream()
        .collect(Collectors.groupingBy(Recipe::cuisine, TreeMap::new, Collectors.maxBy(byLikes)));
    Map<String, Recipe> topPerCuisine2 = recipes.stream()
        .collect(Collectors.groupingBy(Recipe::cuisine, TreeMap::new,
            Collectors.collectingAndThen(Collectors.maxBy(byLikes), Optional::get)));
    Map<String, Recipe> topPerCuisine3 = recipes.stream()
        .collect(Collectors.toMap(Recipe::cuisine, Function.identity(),
            BinaryOperator.maxBy(byLikes), TreeMap::new));
    Map<String, Recipe> quickestPerCuisine = recipes.stream()
        .collect(Collectors.toMap(Recipe::cuisine, Function.identity(),
            BinaryOperator.minBy(Comparator.comparingInt(Recipe::minutes)), TreeMap::new));
    Map<String, Integer> maxLikesPerCuisine = recipes.stream()
        .collect(Collectors.groupingBy(Recipe::cuisine, TreeMap::new,
            Collectors.collectingAndThen(Collectors.summarizingInt(Recipe::likes), IntSummaryStatistics::getMax)));
    print("topPerCuisine", topPerCuisine);
    print("topPerCuisine2", topPerCuisine2);
    print("topPerCuisine3", topPerCuisine3);
    print("quickestPerCuisine", quickestPerCuisine);
    print("maxLikesPerCuisine", maxLikesPerCuisine);

    // 8. Primitive streams and summaryStatistics
    OptionalInt maxOpt = IntStream.of(4, 8, 15, 16, 23, 42).max();
    int minPrim = IntStream.of(4, 8, 15, 16, 23, 42).min().getAsInt();
    double maxDouble = numbers.stream().mapToDouble(Integer::doubleValue).max().orElseThrow();
    IntSummaryStatistics stats = numbers.stream().mapToInt(Integer::intValue).summaryStatistics();
    IntSummaryStatistics emptyStats = IntStream.empty().summaryStatistics();
    print("maxOpt", maxOpt);
    print("minPrim", minPrim);
    print("maxDouble", maxDouble);
    print("stats", stats);
    print("stats.getMin()", stats.getMin());
    print("stats.getMax()", stats.getMax());
    print("emptyStats", emptyStats);
    print("emptyStats.getMax()", emptyStats.getMax());
    IntSummaryStatistics likeStats = recipes.stream().collect(Collectors.summarizingInt(Recipe::likes));
    print("likeStats", likeStats);

    // min and max in one pass with teeing (Java 12+)
    List<Integer> minAndMax = numbers.stream().collect(Collectors.teeing(
        Collectors.minBy(Comparator.<Integer>naturalOrder()),
        Collectors.maxBy(Comparator.<Integer>naturalOrder()),
        (lo, hi) -> List.of(lo.orElseThrow(), hi.orElseThrow())));
    print("minAndMax (teeing)", minAndMax);

    // reduce
    Optional<Integer> reduced = numbers.stream().reduce(Integer::max);
    print("reduce(Integer::max)", reduced);
    print("max(naturalOrder())", numbers.stream().max(Comparator.naturalOrder()));
    print("collect(maxBy(naturalOrder()))", numbers.stream().collect(Collectors.maxBy(Comparator.naturalOrder())));

    // 9. Collections.max / min
    Integer collMax = Collections.max(numbers);
    Integer collMin = Collections.min(numbers);
    Recipe collQuickest = Collections.min(recipes, Comparator.comparingInt(Recipe::minutes));
    print("Collections.max", collMax);
    print("Collections.min", collMin);
    print("Collections.min(recipes, byMinutes)", collQuickest);
    try {
      Collections.max(List.<Integer>of());
    } catch (NoSuchElementException e) {
      print("Collections.max(empty)", e);
    }

    // 10. Empty streams
    List<Integer> empty = List.of();
    Optional<Integer> emptyMax = empty.stream().max(Comparator.naturalOrder());
    print("emptyMax", emptyMax);
    try {
      empty.stream().max(Comparator.naturalOrder()).get();
    } catch (NoSuchElementException e) {
      print("get() on empty", e);
    }
    try {
      empty.stream().max(Comparator.naturalOrder()).orElseThrow();
    } catch (NoSuchElementException e) {
      print("orElseThrow() on empty", e);
    }
    Integer withDefault = empty.stream().max(Comparator.naturalOrder()).orElse(0);
    print("orElse(0)", withDefault);
    try {
      empty.stream().max(Comparator.naturalOrder())
          .orElseThrow(() -> new IllegalStateException("no numbers"));
    } catch (IllegalStateException e) {
      print("orElseThrow(supplier)", e);
    }
    empty.stream().max(Comparator.naturalOrder()).ifPresent(System.out::println);
    print("ifPresent on empty", "(nothing printed above)");
    try {
      IntStream.empty().max().getAsInt();
    } catch (NoSuchElementException e) {
      print("IntStream.empty().max().getAsInt()", e);
    }

    // 11. Nulls
    List<Integer> withNulls = Arrays.asList(4, null, 15);
    try {
      withNulls.stream().max(Comparator.naturalOrder());
    } catch (NullPointerException e) {
      print("max with null element", e);
    }
    Integer nonNullMax = withNulls.stream().filter(Objects::nonNull).max(Comparator.naturalOrder()).orElseThrow();
    Integer nullsFirstMax = withNulls.stream().max(Comparator.nullsFirst(Comparator.naturalOrder())).orElseThrow();
    print("filter(Objects::nonNull).max", nonNullMax);
    print("max(nullsFirst)", nullsFirstMax);
    try {
      withNulls.stream().max(Comparator.nullsLast(Comparator.naturalOrder()));
    } catch (NullPointerException e) {
      print("max(nullsLast)", e);
    }
    Integer nullsLastMin = withNulls.stream().min(Comparator.nullsLast(Comparator.naturalOrder())).orElseThrow();
    print("min(nullsLast)", nullsLastMin);
    try {
      Collections.max(withNulls);
    } catch (NullPointerException e) {
      print("Collections.max with null", e);
    }
    List<Recipe> list = List.of(new Recipe("pasta", null, 20, 120), new Recipe("tacos", "mexican", 15, 90));
    Recipe byCuisine = list.stream()
        .max(Comparator.comparing(Recipe::cuisine, Comparator.nullsFirst(Comparator.naturalOrder())))
        .orElseThrow();
    print("max by nullable field", byCuisine);

    // 12. Map entry with max value
    Map<String, Integer> ages = new LinkedHashMap<>();
    ages.put("Lokesh", 37);
    ages.put("John", 40);
    ages.put("Alex", 29);
    Map.Entry<String, Integer> oldest = ages.entrySet().stream()
        .max(Map.Entry.comparingByValue()).orElseThrow();
    String youngestName = Collections.min(ages.entrySet(), Map.Entry.comparingByValue()).getKey();
    print("oldest entry", oldest);
    print("youngest key", youngestName);
  }

  private static void print(String label, Object value) {
    System.out.println(label + " = " + value);
  }
}
