package com.howtodoinjava.core.streams;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;

/**
 * Examples for the tutorial "Java Stream max() and min() Examples: Find Max and Min Values".
 * https://howtodoinjava.com/java8/stream-max-min-examples/
 */
public class StreamMaxMinMethods {
    static record Recipe(String name, String cuisine, int minutes, int likes) {
        @Override
        public String toString() { return name; }
    }

    static List<Integer> numbers = List.of(4, 8, 15, 16, 23, 42);

    static List<Recipe> recipes = List.of(
    new Recipe("pasta", "italian", 20, 120),
    new Recipe("pizza", "italian", 45, 150),
    new Recipe("risotto", "italian", 35, 150),
    new Recipe("tacos", "mexican", 15, 90),
    new Recipe("burrito", "mexican", 25, 110),
    new Recipe("curry", "indian", 40, 150),
    new Recipe("dal", "indian", 30, 80));
    public static void main(String[] args) throws Exception {
        {
            List<Integer> numbers = List.of(4, 8, 15, 16, 23, 42);

            Integer max = numbers.stream().max(Comparator.naturalOrder()).orElseThrow();       // 42
            show("max", max);
            Integer min = numbers.stream().min(Comparator.naturalOrder()).orElseThrow();       // 4
            show("min", min);
            int maxInt = numbers.stream().mapToInt(Integer::intValue).max().orElseThrow();     // 42
            show("maxInt", maxInt);

            Recipe mostLiked = recipes.stream().max(Comparator.comparing(Recipe::likes)).orElseThrow();   // pizza
            show("mostLiked", mostLiked);
            Recipe quickest = recipes.stream().min(Comparator.comparing(Recipe::minutes)).orElseThrow();  // tacos
            show("quickest", quickest);
            Optional<Recipe> top = recipes.stream().collect(Collectors.maxBy(Comparator.comparing(Recipe::likes)));  // Optional[pizza]
            show("top", top);

            Optional<Integer> none = Stream.<Integer>empty().max(Comparator.naturalOrder());   // Optional.empty
            show("none", none);
            try { Integer value = none.orElseThrow(); show("value", value); } catch (Throwable _t) { System.out.println("value -> " + _t); }
        }
        {
            List<LocalDate> dates = List.of(
            LocalDate.of(2026, 3, 15), LocalDate.of(2025, 12, 1), LocalDate.of(2026, 7, 4));

            LocalDate latest = dates.stream().max(Comparator.naturalOrder()).orElseThrow();     // 2026-07-04
            show("latest", latest);
            LocalDate earliest = dates.stream().min(Comparator.naturalOrder()).orElseThrow();   // 2025-12-01
            show("earliest", earliest);
        }
        {
            Integer maxNumber = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).max(Integer::compare).orElseThrow();   // 9
            show("maxNumber", maxNumber);
            Integer minNumber = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).min(Integer::compare).orElseThrow();   // 1
            show("minNumber", minNumber);

            Integer reversedMax = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).max(Comparator.reverseOrder()).orElseThrow();   // 1
            show("reversedMax", reversedMax);
        }
        {
            String maxChar = Stream.of("H", "T", "D", "I", "J").max(Comparator.naturalOrder()).orElseThrow();   // "T"
            show("maxChar", maxChar);
            String minChar = Stream.of("H", "T", "D", "I", "J").min(Comparator.naturalOrder()).orElseThrow();   // "D"
            show("minChar", minChar);

            String longest = Stream.of("kiwi", "banana", "cherry", "fig")
                    .max(Comparator.comparing(String::length)).orElseThrow();      // "banana"
            String shortest = Stream.of("kiwi", "banana", "cherry", "fig")
                    .min(Comparator.comparing(String::length)).orElseThrow();      // "fig"

            char maxLetter = (char) "streams".chars().max().orElseThrow();     // t
            show("maxLetter", maxLetter);
        }
        {
            Comparator<Recipe> byLikes = Comparator.comparingInt(Recipe::likes);

            Recipe mostLiked = recipes.stream().max(byLikes).orElseThrow();      // pizza
            show("mostLiked", mostLiked);
            Recipe leastLiked = recipes.stream().min(byLikes).orElseThrow();     // dal
            show("leastLiked", leastLiked);
            Recipe slowest = recipes.stream()
                    .max(Comparator.comparingInt(Recipe::minutes)).orElseThrow();    // pizza

            int maxLikes = recipes.stream().mapToInt(Recipe::likes).max().orElseThrow();   // 150
            show("maxLikes", maxLikes);
        }
        {
            Comparator<Recipe> byLikes = Comparator.comparingInt(Recipe::likes);

            Recipe sequential = recipes.stream().max(byLikes).orElseThrow();            // pizza
            show("sequential", sequential);
            Recipe parallel = recipes.parallelStream().max(byLikes).orElseThrow();      // pizza
            show("parallel", parallel);
            Recipe fromCollections = Collections.max(recipes, byLikes);                 // pizza
            show("fromCollections", fromCollections);
        }
        {
            Recipe likedThenQuick = recipes.stream()
                    .max(Comparator.comparingInt(Recipe::likes)
                    .thenComparing(Recipe::minutes, Comparator.reverseOrder()))
                    .orElseThrow();                                     // risotto
        }
        {
            int maxLikes = recipes.stream().mapToInt(Recipe::likes).max().orElseThrow();      // 150
            show("maxLikes", maxLikes);
            List<Recipe> allTop = recipes.stream().filter(r -> r.likes() == maxLikes).toList(); // [pizza, risotto, curry]
            show("allTop", allTop);
        }
        {
            Comparator<Recipe> byLikes = Comparator.comparingInt(Recipe::likes);

            Map<String, Optional<Recipe>> topPerCuisine = recipes.stream()
                    .collect(Collectors.groupingBy(Recipe::cuisine, TreeMap::new,
            Collectors.maxBy(byLikes)));
            // {indian=Optional[curry], italian=Optional[pizza], mexican=Optional[burrito]}
        }
        {
            Comparator<Recipe> byLikes = Comparator.comparingInt(Recipe::likes);

            Map<String, Recipe> topPerCuisine = recipes.stream()
                    .collect(Collectors.groupingBy(Recipe::cuisine, TreeMap::new,
            Collectors.collectingAndThen(Collectors.maxBy(byLikes), Optional::get)));
            // {indian=curry, italian=pizza, mexican=burrito}

            Map<String, Recipe> topPerCuisine2 = recipes.stream()
                    .collect(Collectors.toMap(Recipe::cuisine, Function.identity(),
            BinaryOperator.maxBy(byLikes), TreeMap::new));
            // {indian=curry, italian=pizza, mexican=burrito}

            Map<String, Recipe> quickestPerCuisine = recipes.stream()
                    .collect(Collectors.toMap(Recipe::cuisine, Function.identity(),
            BinaryOperator.minBy(Comparator.comparingInt(Recipe::minutes)), TreeMap::new));
            // {indian=dal, italian=pasta, mexican=tacos}
        }
        {
            Map<String, Integer> maxLikesPerCuisine = recipes.stream()
                    .collect(Collectors.groupingBy(Recipe::cuisine, TreeMap::new,
            Collectors.collectingAndThen(Collectors.summarizingInt(Recipe::likes),
            IntSummaryStatistics::getMax)));
            // {indian=150, italian=150, mexican=110}
        }
        {
            OptionalInt maxOpt = IntStream.of(4, 8, 15, 16, 23, 42).max();                // OptionalInt[42]
            show("maxOpt", maxOpt);
            int minPrim = IntStream.of(4, 8, 15, 16, 23, 42).min().getAsInt();            // 4
            show("minPrim", minPrim);
            double maxDouble = numbers.stream().mapToDouble(Integer::doubleValue).max().orElseThrow();   // 42.0
            show("maxDouble", maxDouble);
        }
        {
            IntSummaryStatistics stats = numbers.stream().mapToInt(Integer::intValue).summaryStatistics();
            // IntSummaryStatistics{count=6, sum=108, min=4, average=18.000000, max=42}
            int minValue = stats.getMin();   // 4
            show("minValue", minValue);
            int maxValue = stats.getMax();   // 42
            show("maxValue", maxValue);

            IntSummaryStatistics emptyStats = IntStream.empty().summaryStatistics();
            // IntSummaryStatistics{count=0, sum=0, min=2147483647, average=0.000000, max=-2147483648}
        }
        {
            List<Integer> minAndMax = numbers.stream().collect(Collectors.teeing(
            Collectors.minBy(Comparator.<Integer>naturalOrder()),
            Collectors.maxBy(Comparator.<Integer>naturalOrder()),
            (lo, hi) -> List.of(lo.orElseThrow(), hi.orElseThrow())));     // [4, 42]
        }
        {
            Integer maxNumber = Collections.max(numbers);                                       // 42
            show("maxNumber", maxNumber);
            Integer minNumber = Collections.min(numbers);                                       // 4
            show("minNumber", minNumber);
            Recipe quickest = Collections.min(recipes, Comparator.comparingInt(Recipe::minutes));   // tacos
            show("quickest", quickest);

            try { Integer fromEmpty = Collections.max(List.<Integer>of()); show("fromEmpty", fromEmpty); } catch (Throwable _t) { System.out.println("fromEmpty -> " + _t); }
        }
        {
            List<Integer> empty = List.of();

            Optional<Integer> result = empty.stream().max(Comparator.naturalOrder());           // Optional.empty
            show("result", result);
            try { Integer viaGet = empty.stream().max(Comparator.naturalOrder()).get(); show("viaGet", viaGet); } catch (Throwable _t) { System.out.println("viaGet -> " + _t); }
            try { Integer viaThrow = empty.stream().max(Comparator.naturalOrder()).orElseThrow(); show("viaThrow", viaThrow); } catch (Throwable _t) { System.out.println("viaThrow -> " + _t); }
            Integer orZero = empty.stream().max(Comparator.naturalOrder()).orElse(0);           // 0
            show("orZero", orZero);
            Supplier<IllegalStateException> noNumbers = () -> new IllegalStateException("no numbers");
            try { Integer orCustom = empty.stream().max(Comparator.naturalOrder()).orElseThrow(noNumbers); show("orCustom", orCustom); } catch (Throwable _t) { System.out.println("orCustom -> " + _t); }
            empty.stream().max(Comparator.naturalOrder())
                    .ifPresent(System.out::println);                                                // prints nothing
        }
        {
            List<Integer> withNulls = Arrays.asList(4, null, 15);

            try { Optional<Integer> maxWithNull = withNulls.stream().max(Comparator.naturalOrder()); show("maxWithNull", maxWithNull); } catch (Throwable _t) { System.out.println("maxWithNull -> " + _t); }
            Integer maxNonNull = withNulls.stream().filter(Objects::nonNull)
                    .max(Comparator.naturalOrder()).orElseThrow();                                  // 15

            Integer nullsFirstMax = withNulls.stream().max(Comparator.nullsFirst(Comparator.naturalOrder())).orElseThrow();   // 15
            show("nullsFirstMax", nullsFirstMax);
            try { Optional<Integer> nullsLastMax = withNulls.stream().max(Comparator.nullsLast(Comparator.naturalOrder())); show("nullsLastMax", nullsLastMax); } catch (Throwable _t) { System.out.println("nullsLastMax -> " + _t); }
            Integer nullsLastMin = withNulls.stream().min(Comparator.nullsLast(Comparator.naturalOrder())).orElseThrow();    // 4
            show("nullsLastMin", nullsLastMin);
        }
        {
            List<Recipe> list = List.of(new Recipe("pasta", null, 20, 120), new Recipe("tacos", "mexican", 15, 90));

            Recipe maxByCuisine = list.stream()
                    .max(Comparator.comparing(Recipe::cuisine, Comparator.nullsFirst(Comparator.naturalOrder())))
                    .orElseThrow();                                      // tacos
        }
        {
            Optional<Integer> viaMax = numbers.stream().max(Comparator.naturalOrder());                           // Optional[42]
            show("viaMax", viaMax);
            Optional<Integer> viaReduce = numbers.stream().reduce(Integer::max);                                  // Optional[42]
            show("viaReduce", viaReduce);
            Optional<Integer> viaCollector = numbers.stream().collect(Collectors.maxBy(Comparator.naturalOrder())); // Optional[42]
            show("viaCollector", viaCollector);
        }
        {
            Comparator<Recipe> byLikes = Comparator.comparingInt(Recipe::likes);

            Recipe lastOfTies = recipes.stream()
                    .reduce((a, b) -> byLikes.compare(b, a) >= 0 ? b : a)
                    .orElseThrow();                                      // curry
        }
        {
            Map<String, Integer> ages = new LinkedHashMap<>();
            ages.put("Lokesh", 37);
            ages.put("John", 40);
            ages.put("Alex", 29);

            Map.Entry<String, Integer> oldest = ages.entrySet().stream()
                    .max(Map.Entry.comparingByValue()).orElseThrow();                              // John=40
            String youngest = Collections.min(ages.entrySet(), Map.Entry.comparingByValue()).getKey();   // "Alex"
            show("youngest", youngest);
        }
        {
            List<Integer> scores = List.of(42, 16, 42, 23, 8);

            Optional<Integer> second = scores.stream().distinct()
                    .sorted(Comparator.reverseOrder()).skip(1).findFirst();      // Optional[23]
            Optional<Integer> withoutDistinct = scores.stream()
                    .sorted(Comparator.reverseOrder()).skip(1).findFirst();      // Optional[42]
        }
        {
            int[] temperatures = {21, 25, 19, 30};
            String[] names = {"kiwi", "banana", "fig"};

            int hottest = Arrays.stream(temperatures).max().orElseThrow();                          // 30
            show("hottest", hottest);
            String longestName = Arrays.stream(names).max(Comparator.comparingInt(String::length)).orElseThrow();   // "banana"
            show("longestName", longestName);
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
