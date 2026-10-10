package com.howtodoinjava.core.collections.map;

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
 * Examples for the tutorial "Java ConcurrentSkipListMap: Sorted Thread-Safe Map Guide".
 * https://howtodoinjava.com/java/collections/concurrentskiplistmap/
 */
public class SkipListMapExamples {

    public static void main(String[] args) throws Exception {
        {
            ConcurrentSkipListMap<Integer, String> scores = new ConcurrentSkipListMap<>();
            scores.put(70, "bob");
            scores.put(95, "lokesh");
            scores.put(82, "alex");

            String all = scores.toString();                                    // "{70=bob, 82=alex, 95=lokesh}"
            show("all", all);
            Integer atLeast80 = scores.ceilingKey(80);                         // 82
            show("atLeast80", atLeast80);
            String top = scores.lastEntry().getValue();                        // "lokesh"
            show("top", top);
            NavigableMap<Integer, String> passed = scores.tailMap(80, true);   // {82=alex, 95=lokesh}
            show("passed", passed);
            Map.Entry<Integer, String> lowest = scores.pollFirstEntry();       // 70=bob, also removed
            show("lowest", lowest);
            int left = scores.size();                                          // 2
            show("left", left);
        }
        {
            ConcurrentSkipListMap<Integer, String> natural = new ConcurrentSkipListMap<>();                         // natural order
            show("natural", natural);
            ConcurrentSkipListMap<Integer, String> highFirst = new ConcurrentSkipListMap<>(Comparator.reverseOrder()); // reverse order
            show("highFirst", highFirst);
            ConcurrentSkipListMap<Integer, String> copied = new ConcurrentSkipListMap<>(Map.of(2, "b", 1, "a"));      // {1=a, 2=b}
            show("copied", copied);
            ConcurrentSkipListMap<String, Integer> sameOrder = new ConcurrentSkipListMap<>(new TreeMap<>(String.CASE_INSENSITIVE_ORDER));   // keeps the comparator
            show("sameOrder", sameOrder);
        }
        {
            ConcurrentSkipListMap<Integer, String> leaderboard = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
            leaderboard.put(70, "bob");
            leaderboard.put(95, "lokesh");
            leaderboard.put(82, "alex");
            String ranking = leaderboard.toString();                   // "{95=lokesh, 82=alex, 70=bob}"
            show("ranking", ranking);
        }
        {
            ConcurrentSkipListMap<Integer, String> map = new ConcurrentSkipListMap<>(Map.of(1, "a", 2, "b", 4, "d", 5, "e"));
            Integer ceil3 = map.ceilingKey(3);                       // 4
            show("ceil3", ceil3);
            Integer higher3 = map.higherKey(3);                      // 4
            show("higher3", higher3);
            Integer ceil4 = map.ceilingKey(4);                       // 4
            show("ceil4", ceil4);
            Integer higher4 = map.higherKey(4);                      // 5
            show("higher4", higher4);
            Integer floor0 = map.floorKey(0);                        // null, no smaller key
            show("floor0", floor0);
            Integer firstKey = map.firstKey();                       // 1
            show("firstKey", firstKey);
            Integer lastKey = map.lastKey();                         // 5
            show("lastKey", lastKey);
        }
        {
            ConcurrentSkipListMap<Integer, String> grades = new ConcurrentSkipListMap<>(Map.of(1, "a", 2, "b", 3, "c", 4, "d", 5, "e"));
            ConcurrentNavigableMap<Integer, String> head = grades.headMap(3);          // {1=a, 2=b}
            show("head", head);
            ConcurrentNavigableMap<Integer, String> tail = grades.tailMap(3);          // {3=c, 4=d, 5=e}
            show("tail", tail);
            ConcurrentNavigableMap<Integer, String> middle = grades.subMap(2, true, 4, true);   // {2=b, 3=c, 4=d}
            show("middle", middle);

            grades.put(0, "z");
            String headNow = head.toString();                        // "{0=z, 1=a, 2=b}", the view is live
            show("headNow", headNow);
            try { String outOfRange = tail.put(1, "x"); show("outOfRange", outOfRange); } catch (Throwable _t) { System.out.println("outOfRange -> " + _t); }
        }
        {
            ConcurrentSkipListMap<Integer, String> jobs = new ConcurrentSkipListMap<>(Map.of(30, "report", 10, "backup", 20, "email"));
            Map.Entry<Integer, String> next = jobs.pollFirstEntry();                // 10=backup
            show("next", next);
            Map.Entry<Integer, String> last = jobs.pollLastEntry();                 // 30=report
            show("last", last);
            String remaining = jobs.toString();                                     // "{20=email}"
            show("remaining", remaining);
        }
        {
            ConcurrentSkipListMap<Integer, String> ranks = new ConcurrentSkipListMap<>(Map.of(1, "a", 2, "b", 3, "c"));
            NavigableMap<Integer, String> desc = ranks.descendingMap();             // {3=c, 2=b, 1=a}
            show("desc", desc);
            SequencedMap<Integer, String> reversed = ranks.reversed();              // {3=c, 2=b, 1=a}
            show("reversed", reversed);
            Map.Entry<Integer, String> firstEntry = ranks.firstEntry();             // 1=a
            show("firstEntry", firstEntry);
            try { String front = ranks.putFirst(0, "z"); show("front", front); } catch (Throwable _t) { System.out.println("front -> " + _t); }
            try { String changed = firstEntry.setValue("x"); show("changed", changed); } catch (Throwable _t) { System.out.println("changed -> " + _t); }
        }
        {
            ConcurrentSkipListMap<Integer, Integer> asks = new ConcurrentSkipListMap<>();
            try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
                pool.submit(() -> asks.merge(10_150, 200, Integer::sum));
                pool.submit(() -> asks.merge(10_120, 50, Integer::sum));
                pool.submit(() -> asks.merge(10_150, 100, Integer::sum));
                pool.submit(() -> asks.merge(10_200, 500, Integer::sum));
            }
            int bestPrice = asks.firstKey();                          // 10120
            show("bestPrice", bestPrice);
            int qtyAt10150 = asks.get(10_150);                        // 300
            show("qtyAt10150", qtyAt10150);
            NavigableMap<Integer, Integer> upTo10150 = asks.headMap(10_150, true);   // {10120=50, 10150=300}
            show("upTo10150", upTo10150);
            asks.computeIfPresent(10_120, (price, qty) -> qty - 50 == 0 ? null : qty - 50);
            int newBest = asks.firstKey();                            // 10150
            show("newBest", newBest);
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
