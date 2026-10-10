package com.howtodoinjava.core.streams;

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

/**
 * Examples for the tutorial "Java Stream Filter with Multiple Conditions: 3 Ways".
 * https://howtodoinjava.com/java8/stream-multiple-filters-example/
 */
public class FilterMultipleConditions {
    static record Room(int number, int beds, int price, boolean available, String view) {}
    static record RoomSearch(Integer minBeds, Integer maxPrice, Boolean seaView) {
        Predicate<Room> matcher() {
            List<Predicate<Room>> conditions = new ArrayList<>();
            conditions.add(Room::available);
            if (minBeds != null) conditions.add(r -> r.beds() >= minBeds);
            if (maxPrice != null) conditions.add(r -> r.price() <= maxPrice);
            if (seaView != null) conditions.add(r -> seaView == "sea".equals(r.view()));
            return conditions.stream().reduce(r -> true, Predicate::and);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<Integer> nums = List.of(3, 8, 12, 15, 20);
            Predicate<Integer> big = n -> n > 5;
            Predicate<Integer> even = n -> n % 2 == 0;

            List<Integer> oneLambda = nums.stream().filter(n -> n > 5 && n % 2 == 0).toList();       // [8, 12, 20]
            show("oneLambda", oneLambda);
            List<Integer> chained = nums.stream().filter(big).filter(even).toList();                 // [8, 12, 20]
            show("chained", chained);
            List<Integer> combined = nums.stream().filter(big.and(even)).toList();                   // [8, 12, 20]
            show("combined", combined);
            List<Integer> smallOr20 = nums.stream().filter(big.negate().or(n -> n == 20)).toList();  // [3, 20]
            show("smallOr20", smallOr20);
        }
        {
            List<Room> rooms = List.of(
            new Room(101, 1, 80, true, "garden"),
            new Room(102, 2, 120, true, "sea"),
            new Room(201, 2, 150, false, "sea"),
            new Room(202, 3, 200, true, "city"),
            new Room(301, 3, 180, false, "sea"));

            List<Integer> twoBedsCheap = rooms.stream().filter(r -> r.available() && r.beds() >= 2 && r.price() <= 150).map(Room::number).toList();   // [102]
            show("twoBedsCheap", twoBedsCheap);
            List<Integer> withParens = rooms.stream().filter(r -> r.available() && (r.view().equals("sea") || r.beds() == 3)).map(Room::number).toList();   // [102, 202]
            show("withParens", withParens);
            List<Integer> noParens = rooms.stream().filter(r -> r.available() && r.view().equals("sea") || r.beds() == 3).map(Room::number).toList();      // [102, 202, 301]
            show("noParens", noParens);
        }
        {
            List<Room> rooms = List.of(new Room(101, 1, 80, true, "garden"), new Room(102, 2, 120, true, "sea"), new Room(201, 2, 150, false, "sea"));

            List<Integer> matches = rooms.stream()
                    .filter(Room::available)
                    .filter(r -> r.beds() >= 2)
                    .filter(r -> r.price() <= 150)
                    .map(Room::number)
                    .toList();                              // [102]
        }
        {
            Predicate<Room> free = Room::available;
            Predicate<Room> family = r -> r.beds() >= 3;
            Predicate<Room> seaView = r -> r.view().equals("sea");
            List<Room> rooms = List.of(new Room(101, 1, 80, true, "garden"), new Room(102, 2, 120, true, "sea"), new Room(201, 2, 150, false, "sea"), new Room(202, 3, 200, true, "city"));

            List<Integer> freeFamilyOrSea = rooms.stream().filter(free.and(family.or(seaView))).map(Room::number).toList();   // [102, 202]
            show("freeFamilyOrSea", freeFamilyOrSea);
            List<Integer> freeNotSea = rooms.stream().filter(free.and(Predicate.not(seaView))).map(Room::number).toList();    // [101, 202]
            show("freeNotSea", freeNotSea);
            List<Integer> booked = rooms.stream().filter(free.negate()).map(Room::number).toList();                           // [201]
            show("booked", booked);
        }
        {
            AtomicInteger calls = new AtomicInteger();
            Predicate<Integer> expensive = price -> {
                calls.incrementAndGet();
                return price % 20 == 0;
            };
            Predicate<Integer> cheap = price -> price <= 100;
            List<Integer> prices = List.of(80, 120, 140, 160, 200);

            List<Integer> cheapFirst = prices.stream().filter(cheap.and(expensive)).toList();    // [80]
            show("cheapFirst", cheapFirst);
            int callsCheapFirst = calls.getAndSet(0);                                          // 1
            show("callsCheapFirst", callsCheapFirst);
            List<Integer> expensiveFirst = prices.stream().filter(expensive.and(cheap)).toList();   // [80]
            show("expensiveFirst", expensiveFirst);
            int callsExpensiveFirst = calls.get();                                             // 5
            show("callsExpensiveFirst", callsExpensiveFirst);
        }
        {
            List<Room> rooms = Arrays.asList(new Room(101, 1, 80, true, null), new Room(102, 2, 120, true, "sea"));

            try { List<Integer> unsafe = rooms.stream().filter(r -> r.view().equals("sea")).map(Room::number).toList(); show("unsafe", unsafe); } catch (Throwable _t) { System.out.println("unsafe -> " + _t); }
            List<Integer> guarded = rooms.stream().filter(r -> r.view() != null && r.view().equals("sea")).map(Room::number).toList();       // [102]
            show("guarded", guarded);
            List<Integer> constantFirst = rooms.stream().filter(r -> "sea".equals(r.view())).map(Room::number).toList();                    // [102]
            show("constantFirst", constantFirst);
        }
        {
            List<Room> rooms = List.of(
            new Room(101, 1, 80, true, "garden"),
            new Room(102, 2, 120, true, "sea"),
            new Room(201, 2, 150, false, "sea"),
            new Room(202, 3, 200, true, "city"));

            List<Integer> anyFree = rooms.stream().filter(new RoomSearch(null, null, null).matcher()).map(Room::number).toList();    // [101, 102, 202]
            show("anyFree", anyFree);
            List<Integer> twoBeds = rooms.stream().filter(new RoomSearch(2, null, null).matcher()).map(Room::number).toList();       // [102, 202]
            show("twoBeds", twoBeds);
            List<Integer> budgetSea = rooms.stream().filter(new RoomSearch(null, 150, true).matcher()).map(Room::number).toList();   // [102]
            show("budgetSea", budgetSea);
        }
        {
            List<Predicate<String>> rules = List.of(s -> s.startsWith("tea"), s -> s.endsWith("jam"));
            List<Predicate<String>> noRules = List.of();
            List<String> items = List.of("tea bags", "apple jam", "bread");

            List<String> anyRule = items.stream().filter(rules.stream().reduce(s -> false, Predicate::or)).toList();       // [tea bags, apple jam]
            show("anyRule", anyRule);
            List<String> allRules = items.stream().filter(rules.stream().reduce(s -> true, Predicate::and)).toList();      // []
            show("allRules", allRules);
            List<String> emptyOr = items.stream().filter(noRules.stream().reduce(s -> false, Predicate::or)).toList();     // []
            show("emptyOr", emptyOr);
            List<String> emptyAnd = items.stream().filter(noRules.stream().reduce(s -> true, Predicate::and)).toList();    // [tea bags, apple jam, bread]
            show("emptyAnd", emptyAnd);
        }
        {
            Set<String> views = Set.of("sea", "garden");
            List<Integer> nice = Stream.of(new Room(101, 1, 80, true, "garden"), new Room(202, 3, 200, true, "city")).filter(r -> views.contains(r.view())).map(Room::number).toList();   // [101]
            show("nice", nice);
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
