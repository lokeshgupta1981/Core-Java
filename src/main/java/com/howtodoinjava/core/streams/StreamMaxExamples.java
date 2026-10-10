package com.howtodoinjava.core.streams;

import java.util.concurrent.locks.*;
import java.lang.management.*;
import java.time.format.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
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

import java.time.temporal.*;

/**
 * Examples for the tutorial "Java Stream max(): Find the Largest Element With a Comparator".
 * https://howtodoinjava.com/java8/java-stream-max/
 */
public class StreamMaxExamples {
    static record Ride(String rider, String team, int km, LocalDate date) {}
    static Optional<Ride> rideOfTheWeek(List<Ride> rides, LocalDate weekStart) {
        LocalDate weekEnd = weekStart.plusDays(7);
        return rides.stream()
                .filter(r -> !r.date().isBefore(weekStart) && r.date().isBefore(weekEnd))
                .max(Comparator.comparingInt(Ride::km)
                .thenComparing(Ride::date, Comparator.reverseOrder()));
    }
    public static void main(String[] args) throws Exception {
        {
            List<Integer> distances = List.of(42, 65, 30);
            Optional<Integer> longest = distances.stream().max(Comparator.naturalOrder());     // Optional[65]
            show("longest", longest);
            int longestKm = distances.stream().max(Integer::compare).orElseThrow();             // 65
            show("longestKm", longestKm);
            List<Ride> rides = List.of(new Ride("Asha", "red", 42, LocalDate.of(2026, 6, 1)), new Ride("Ben", "blue", 65, LocalDate.of(2026, 6, 2)), new Ride("Chen", "red", 65, LocalDate.of(2026, 6, 3)), new Ride("Dina", "blue", 30, LocalDate.of(2026, 6, 3)), new Ride("Eli", "green", 55, LocalDate.of(2026, 6, 4)));
            String topRider = rides.stream().max(Comparator.comparingInt(Ride::km)).map(Ride::rider).orElse("nobody");   // "Ben"
            show("topRider", topRider);
            int maxPrimitive = IntStream.of(42, 65, 30).max().orElse(0);                        // 65
            show("maxPrimitive", maxPrimitive);
            Optional<Integer> none = Stream.<Integer>empty().max(Comparator.naturalOrder());   // Optional.empty
            show("none", none);
        }
        {
            Integer highest = Stream.of(42, 65, 30).max(Comparator.naturalOrder()).orElseThrow();            // 65
            show("highest", highest);
            String lastName = Stream.of("Dina", "Asha", "Ben").max(Comparator.naturalOrder()).orElseThrow();    // "Dina"
            show("lastName", lastName);
            String longestName = Stream.of("Eli", "Chen", "Ben").max(Comparator.comparingInt(String::length)).orElseThrow();   // "Chen"
            show("longestName", longestName);
        }
        {
            Integer overflow = Stream.of(-2_000_000_000, 2_000_000_000).max((a, b) -> a - b).orElseThrow();    // -2000000000
            show("overflow", overflow);
            Integer notMax = Stream.of(3, 7, 5).max(Integer::max).orElseThrow();                                 // 3
            show("notMax", notMax);
            Integer smallest = Stream.of(3, 7, 5).max(Comparator.reverseOrder()).orElseThrow();                  // 3
            show("smallest", smallest);
            Integer correct = Stream.of(-2_000_000_000, 2_000_000_000).max(Integer::compare).orElseThrow();      // 2000000000
            show("correct", correct);
        }
        {
            List<Ride> rides = List.of(new Ride("Asha", "red", 42, LocalDate.of(2026, 6, 1)), new Ride("Ben", "blue", 65, LocalDate.of(2026, 6, 2)), new Ride("Chen", "red", 65, LocalDate.of(2026, 6, 3)), new Ride("Dina", "blue", 30, LocalDate.of(2026, 6, 3)), new Ride("Eli", "green", 55, LocalDate.of(2026, 6, 4)));
            Ride longestRide = rides.stream().max(Comparator.comparingInt(Ride::km)).orElseThrow();
            String longestBy = longestRide.rider();                                                     // "Ben"
            show("longestBy", longestBy);
            int longestDistance = rides.stream().mapToInt(Ride::km).max().orElse(0);                    // 65
            show("longestDistance", longestDistance);
            String latestRider = rides.stream().max(Comparator.comparing(Ride::date)).map(Ride::rider).orElse("none");   // "Eli"
            show("latestRider", latestRider);
        }
        {
            List<Ride> rides = List.of(new Ride("Asha", "red", 42, LocalDate.of(2026, 6, 1)), new Ride("Ben", "blue", 65, LocalDate.of(2026, 6, 2)), new Ride("Chen", "red", 65, LocalDate.of(2026, 6, 3)), new Ride("Dina", "blue", 30, LocalDate.of(2026, 6, 3)), new Ride("Eli", "green", 55, LocalDate.of(2026, 6, 4)));
            String firstOfTie = rides.stream().max(Comparator.comparingInt(Ride::km)).map(Ride::rider).orElseThrow();                                       // "Ben"
            show("firstOfTie", firstOfTie);
            String byNameToo = rides.stream().max(Comparator.comparingInt(Ride::km).thenComparing(Ride::rider)).map(Ride::rider).orElseThrow();             // "Chen"
            show("byNameToo", byNameToo);
            String earliest = rides.stream().max(Comparator.comparingInt(Ride::km).thenComparing(Ride::date, Comparator.reverseOrder())).map(Ride::rider).orElseThrow();   // "Ben"
            show("earliest", earliest);
        }
        {
            List<Ride> rides = List.of(new Ride("Asha", "red", 42, LocalDate.of(2026, 6, 1)), new Ride("Ben", "blue", 65, LocalDate.of(2026, 6, 2)), new Ride("Chen", "red", 65, LocalDate.of(2026, 6, 3)), new Ride("Dina", "blue", 30, LocalDate.of(2026, 6, 3)), new Ride("Eli", "green", 55, LocalDate.of(2026, 6, 4)));
            List<String> allLongest = rides.stream().collect(Collectors.groupingBy(Ride::km, TreeMap::new, Collectors.mapping(Ride::rider, Collectors.toList()))).lastEntry().getValue();   // [Ben, Chen]
            show("allLongest", allLongest);
        }
        {
            List<Integer> noRides = List.of();
            Optional<Integer> result = noRides.stream().max(Integer::compare);                                   // Optional.empty
            show("result", result);
            Integer orZero = noRides.stream().max(Integer::compare).orElse(0);                                    // 0
            show("orZero", orZero);
            try { Integer withMessage = noRides.stream().max(Integer::compare).orElseThrow(() -> new IllegalStateException("no rides logged")); show("withMessage", withMessage); } catch (Throwable _t) { System.out.println("withMessage -> " + _t); }
            String label = noRides.stream().max(Integer::compare).map(km -> km + " km").orElse("no rides yet");   // "no rides yet"
            show("label", label);
            try { Integer unsafe = noRides.stream().max(Integer::compare).get(); show("unsafe", unsafe); } catch (Throwable _t) { System.out.println("unsafe -> " + _t); }
        }
        {
            List<Integer> withGaps = Arrays.asList(42, null, 65);
            try { Optional<Integer> crash = withGaps.stream().max(Comparator.naturalOrder()); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
            Integer safeMax = withGaps.stream().filter(Objects::nonNull).max(Comparator.naturalOrder()).orElse(0);   // 65
            show("safeMax", safeMax);
            try { Optional<Integer> nullWins = withGaps.stream().max(Comparator.nullsLast(Comparator.naturalOrder())); show("nullWins", nullWins); } catch (Throwable _t) { System.out.println("nullWins -> " + _t); }
        }
        {
            List<Ride> rides = List.of(new Ride("Asha", "red", 42, LocalDate.of(2026, 6, 1)), new Ride("Ben", "blue", 65, LocalDate.of(2026, 6, 2)), new Ride("Chen", "red", 65, LocalDate.of(2026, 6, 3)), new Ride("Dina", "blue", 30, LocalDate.of(2026, 6, 3)), new Ride("Eli", "green", 55, LocalDate.of(2026, 6, 4)));
            Map<String, String> bestPerTeam = rides.stream().collect(Collectors.groupingBy(Ride::team, TreeMap::new, Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(Ride::km)), best -> best.orElseThrow().rider())));   // {blue=Ben, green=Eli, red=Chen}
            show("bestPerTeam", bestPerTeam);
            Map<String, Integer> maxKmPerTeam = rides.stream().collect(Collectors.toMap(Ride::team, Ride::km, Math::max, TreeMap::new));   // {blue=65, green=55, red=65}
            show("maxKmPerTeam", maxKmPerTeam);
        }
        {
            OptionalInt maxInt = IntStream.of(42, 65, 30).max();                                  // OptionalInt[65]
            show("maxInt", maxInt);
            long maxLong = LongStream.of(3_000_000_000L, 12L).max().orElseThrow();                  // 3000000000
            show("maxLong", maxLong);
            char maxChar = (char) "rides".chars().max().orElseThrow();                              // 's'
            show("maxChar", maxChar);
            OptionalDouble withNaN = DoubleStream.of(21.5, Double.NaN, 18.0).max();                 // OptionalDouble[NaN]
            show("withNaN", withNaN);
            double cleanMax = DoubleStream.of(21.5, Double.NaN, 18.0).filter(d -> !Double.isNaN(d)).max().orElse(0);   // 21.5
            show("cleanMax", cleanMax);
        }
        {
            List<Integer> distances = List.of(42, 65, 30);
            Integer viaCollections = Collections.max(distances);                                           // 65
            show("viaCollections", viaCollections);
            Optional<Integer> viaReduce = distances.stream().reduce(Integer::max);                         // Optional[65]
            show("viaReduce", viaReduce);
            Optional<Integer> viaSort = distances.stream().sorted(Comparator.reverseOrder()).findFirst();  // Optional[65]
            show("viaSort", viaSort);
        }
        {
            List<Ride> rides = List.of(new Ride("Asha", "red", 42, LocalDate.of(2026, 6, 1)), new Ride("Ben", "blue", 65, LocalDate.of(2026, 6, 2)), new Ride("Chen", "red", 65, LocalDate.of(2026, 6, 3)), new Ride("Dina", "blue", 30, LocalDate.of(2026, 6, 3)), new Ride("Eli", "green", 55, LocalDate.of(2026, 6, 4)));
            String badge = rideOfTheWeek(rides, LocalDate.of(2026, 6, 1)).map(r -> r.rider() + ", " + r.km() + " km").orElse("No rides yet");   // "Ben, 65 km"
            show("badge", badge);
            String quietWeek = rideOfTheWeek(rides, LocalDate.of(2026, 7, 1)).map(r -> r.rider() + ", " + r.km() + " km").orElse("No rides yet");   // "No rides yet"
            show("quietWeek", quietWeek);
        }
        {
            List<Integer> scores = List.of(12, 48, 31);
            Integer top = Collections.max(scores);                                    // 48
            show("top", top);
            int topOfEven = scores.stream().filter(n -> n % 2 == 0).mapToInt(Integer::intValue).max().orElse(0);   // 48
            show("topOfEven", topOfEven);
        }
        {
            List<Ride> rides = List.of(new Ride("Asha", "red", 42, LocalDate.of(2026, 6, 1)), new Ride("Ben", "blue", 65, LocalDate.of(2026, 6, 2)), new Ride("Chen", "red", 65, LocalDate.of(2026, 6, 3)), new Ride("Dina", "blue", 30, LocalDate.of(2026, 6, 3)), new Ride("Eli", "green", 55, LocalDate.of(2026, 6, 4)));
            LocalDate lastRide = rides.stream().map(Ride::date).max(Comparator.naturalOrder()).orElseThrow();   // 2026-06-04
            show("lastRide", lastRide);
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
