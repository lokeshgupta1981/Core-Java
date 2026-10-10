package com.howtodoinjava.core.predicate;

import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.time.format.*;

/**
 * Examples for the tutorial "Java Predicate: test(), and(), or(), not() and IntPredicate".
 * https://howtodoinjava.com/java8/how-to-use-predicate-in-java-8/
 */
public class PredicateGuide {
    static <T> List<T> select(List<T> items, Predicate<? super T> rule) {
        return items.stream().filter(rule).toList();
    }
    static boolean isPrime(int n) {
        return n > 1 && IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(d -> n % d == 0);
    }
    static record Apartment(String city, int rent, boolean petsAllowed) {}
    static Predicate<Apartment> searchFilter(Optional<String> city, OptionalInt maxRent, boolean petsRequired) {
        Predicate<Apartment> filter = apartment -> true;
        if (city.isPresent()) {
            filter = filter.and(apartment -> apartment.city().equalsIgnoreCase(city.get()));
        }
        if (maxRent.isPresent()) {
            filter = filter.and(apartment -> apartment.rent() <= maxRent.getAsInt());
        }
        if (petsRequired) {
            filter = filter.and(Apartment::petsAllowed);
        }
        return filter;
    }
    public static void main(String[] args) throws Exception {
        {
            Predicate<String> isLong = s -> s.length() > 5;
            Predicate<String> startsWithB = s -> s.startsWith("b");
            boolean longWord = isLong.test("banana");                    // true
            show("longWord", longWord);
            boolean both = isLong.and(startsWithB).test("banana");       // true
            show("both", both);
            boolean either = isLong.or(startsWithB).test("kiwi");        // false
            show("either", either);
            boolean notLong = isLong.negate().test("kiwi");              // true
            show("notLong", notLong);
            List<String> shortWords = Stream.of("apple", "banana", "kiwi").filter(Predicate.not(isLong)).toList();   // [apple, kiwi]
            show("shortWords", shortWords);
            boolean same = Predicate.isEqual("kiwi").test("kiwi");       // true
            show("same", same);
        }
        {
            Predicate<Integer> isEven = n -> n % 2 == 0;
            Predicate<String> hasDigit = s -> s.chars().anyMatch(Character::isDigit);
            boolean even = isEven.test(10);                // true
            show("even", even);
            boolean digit = hasDigit.test("room42");       // true
            show("digit", digit);
        }
        {
            Predicate<String> isBlank = String::isBlank;
            Predicate<Object> isNull = Objects::isNull;
            Predicate<String> inStock = Set.of("apple", "kiwi")::contains;
            boolean blank = isBlank.test("   ");           // true
            show("blank", blank);
            boolean missing = isNull.test(null);           // true
            show("missing", missing);
            boolean available = inStock.test("kiwi");      // true
            show("available", available);
        }
        {
            List<Integer> numbers = List.of(3, 8, 12, 5);
            List<Integer> big = select(numbers, n -> n > 6);                  // [8, 12]
            show("big", big);
            List<Integer> notNull = select(numbers, Objects::nonNull);        // [3, 8, 12, 5]
            show("notNull", notNull);
        }
        {
            Predicate<String> notNull = Objects::nonNull;
            Predicate<String> notEmpty = s -> !s.isEmpty();
            boolean nullOk = notNull.and(notEmpty).test(null);       // false, isEmpty() never runs
            show("nullOk", nullOk);
            boolean textOk = notNull.and(notEmpty).test("hi");       // true
            show("textOk", textOk);
        }
        {
            Predicate<String> notEmpty = s -> !s.isEmpty();
            Predicate<String> notNull = Objects::nonNull;
            try { boolean crash = notEmpty.and(notNull).test(null); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
        }
        {
            Predicate<Integer> small = n -> n < 10;
            Predicate<Integer> even = n -> n % 2 == 0;
            Predicate<Integer> positive = n -> n > 0;
            boolean chained = small.or(even).and(positive).test(-4);     // false, (small || even) && positive
            show("chained", chained);
            boolean nested = small.or(even.and(positive)).test(-4);      // true, small || (even && positive)
            show("nested", nested);
        }
        {
            List<Predicate<String>> rules = List.of(s -> s.length() >= 4, s -> s.startsWith("p"), s -> !s.contains(" "));
            Predicate<String> allRules = rules.stream().reduce(s -> true, Predicate::and);
            Predicate<String> anyRule = rules.stream().reduce(s -> false, Predicate::or);
            boolean pear = allRules.test("pear");          // true
            show("pear", pear);
            boolean fig = allRules.test("fig");            // false
            show("fig", fig);
            boolean figAny = anyRule.test("fig");          // true
            show("figAny", figAny);
        }
        {
            Predicate<Integer> isEven = n -> n % 2 == 0;
            Predicate<Integer> isOdd = isEven.negate();
            Predicate<Integer> alsoOdd = Predicate.not(isEven);
            boolean odd = isOdd.test(7);                   // true
            show("odd", odd);
            boolean odd2 = alsoOdd.test(7);                // true
            show("odd2", odd2);
        }
        {
            List<String> lines = List.of("milk", " ", "", "eggs");
            List<String> filled = lines.stream().filter(Predicate.not(String::isBlank)).toList();    // [milk, eggs]
            show("filled", filled);
            List<String> filled2 = lines.stream().filter(s -> !s.isBlank()).toList();                // [milk, eggs]
            show("filled2", filled2);
        }
        {
            Predicate<Integer> isExpensive = cents -> cents > 1000;
            List<Integer> prices = List.of(450, 1200, 999, 2500);
            List<Integer> expensive = prices.stream().filter(isExpensive).toList();                             // [1200, 2500]
            show("expensive", expensive);
            boolean anyExpensive = prices.stream().anyMatch(isExpensive);                                       // true
            show("anyExpensive", anyExpensive);
            Map<Boolean, List<Integer>> split = prices.stream().collect(Collectors.partitioningBy(isExpensive)); // {false=[450, 999], true=[1200, 2500]}
            show("split", split);
            List<Integer> cheapStart = prices.stream().takeWhile(isExpensive.negate()).toList();                // [450]
            show("cheapStart", cheapStart);
            Optional<Integer> checked = Optional.of(999).filter(isExpensive);                                   // Optional.empty
            show("checked", checked);
            List<Integer> cart = new ArrayList<>(prices);
            boolean removed = cart.removeIf(isExpensive);                                                       // true
            show("removed", removed);
            List<Integer> left = cart;                                                                          // [450, 999]
            show("left", left);
        }
        {
            BiPredicate<String, Integer> fitsIn = (word, max) -> word.length() <= max;
            boolean fits = fitsIn.test("kiwi", 5);         // true
            show("fits", fits);
            boolean tooLong = fitsIn.test("banana", 5);    // false
            show("tooLong", tooLong);
        }
        {
            IntPredicate isOdd = n -> n % 2 != 0;
            IntPredicate isOddPrime = isOdd.and(n -> isPrime(n));
            int[] oddPrimes = IntStream.range(1, 20).filter(isOddPrime).toArray();     // [3, 5, 7, 11, 13, 17, 19]
            show("oddPrimes", oddPrimes);
            boolean negativeOdd = isOdd.test(-3);                                      // true
            show("negativeOdd", negativeOdd);
            IntPredicate wrongOdd = n -> n % 2 == 1;
            boolean missed = wrongOdd.test(-3);                                        // false, -3 % 2 is -1
            show("missed", missed);
        }
        {
            Pattern digits = Pattern.compile("\\d+");
            boolean found = digits.asPredicate().test("room 42");          // true
            show("found", found);
            boolean whole = digits.asMatchPredicate().test("room 42");     // false
            show("whole", whole);
            boolean onlyDigits = digits.asMatchPredicate().test("42");     // true
            show("onlyDigits", onlyDigits);
        }
        {
            Predicate<String> ourDomain = Pattern.compile("[\\w.+-]+@example\\.com").asMatchPredicate();
            List<String> emails = List.of("alex@example.com", "bob@mail.com", "eve@example.com.evil.io", "dana@example.com");
            List<String> ours = emails.stream().filter(ourDomain).toList();                          // [alex@example.com, dana@example.com]
            show("ours", ours);
            boolean looseDot = Pattern.compile("^(.+)@example.com$").asPredicate().test("alex@exampleXcom");   // true, the dot matches X
            show("looseDot", looseDot);
        }
        {
            List<Apartment> listings = List.of(new Apartment("Berlin", 1200, true), new Apartment("Berlin", 1800, false), new Apartment("Munich", 1500, true));
            Predicate<Apartment> visitorFilter = searchFilter(Optional.of("berlin"), OptionalInt.of(1500), false);
            List<Apartment> hits = listings.stream().filter(visitorFilter).toList();       // [Apartment[city=Berlin, rent=1200, petsAllowed=true]]
            show("hits", hits);
            long petFriendly = listings.stream().filter(searchFilter(Optional.empty(), OptionalInt.empty(), true)).count();   // 2
            show("petFriendly", petFriendly);
        }
        {
            Predicate<String> isNullValue = Predicate.isEqual(null);
            boolean nullMatch = isNullValue.test(null);        // true
            show("nullMatch", nullMatch);
            boolean textMatch = isNullValue.test("kiwi");      // false
            show("textMatch", textMatch);
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
