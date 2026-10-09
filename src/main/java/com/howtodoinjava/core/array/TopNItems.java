package com.howtodoinjava.core.array;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import com.google.common.collect.Comparators;
import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Find Top N Elements in an Array in Java (Sort, Stream, Heap)".
 * https://howtodoinjava.com/java/array/finding-top-n-items/
 */
public class TopNItems {
    static int[] topNBySort(int[] values, int n) {
        int[] copy = values.clone();
        Arrays.sort(copy);
        int count = Math.min(n, copy.length);
        int[] top = new int[count];
        for (int i = 0; i < count; i++) {
            top[i] = copy[copy.length - 1 - i];
        }
        return top;
    }
    static int[] topN(int[] values, int n) {
        if (values == null || n <= 0) {
            return new int[0];
        }
        PriorityQueue<Integer> heap = new PriorityQueue<>();   // min-heap
        for (int v : values) {
            if (heap.size() < n) {
                heap.offer(v);
            } else if (v > heap.peek()) {
                heap.poll();                                   // drop the smallest winner
                heap.offer(v);
            }
        }
        int[] result = new int[heap.size()];
        for (int i = result.length - 1; i >= 0; i--) {
            result[i] = heap.poll();                           // smallest goes last
        }
        return result;
    }
    static record Player(String name, int points) {}
    static <T> List<T> topN(T[] items, int n, Comparator<? super T> order) {
        PriorityQueue<T> heap = new PriorityQueue<>(order);   // smallest winner at the head
        for (T item : items) {
            heap.offer(item);
            if (heap.size() > n) {
                heap.poll();
            }
        }
        List<T> result = new ArrayList<>(heap);
        result.sort(order.reversed());
        return result;
    }
    public static void main(String[] args) throws Exception {
        {
            int[] scores = {72, 95, 60, 88, 95, 41, 79};
            List<Integer> top3 = Arrays.stream(scores).boxed().sorted(Comparator.reverseOrder()).limit(3).toList();   // [95, 95, 88]
            show("top3", top3);
            int[] best = topN(scores, 3);                                                                               // [95, 95, 88]
            show("best", best);
        }
        {
            int[] scores = {72, 95, 60, 88, 95, 41, 79};
            int[] top3 = topNBySort(scores, 3);     // [95, 95, 88]
            show("top3", top3);
            int[] all = topNBySort(scores, 10);     // [95, 95, 88, 79, 72, 60, 41]
            show("all", all);
            int first = scores[0];                  // 72, input unchanged
            show("first", first);
        }
        {
            Integer[] bids = {120, 340, 95, 410, 260};
            Integer[] sorted = bids.clone();
            Arrays.sort(sorted, Collections.reverseOrder());
            Integer[] top2 = Arrays.copyOfRange(sorted, 0, Math.min(2, sorted.length));   // [410, 340]
            show("top2", top2);
        }
        {
            int[] scores = {72, 95, 60, 88, 95, 41, 79};
            List<Integer> top3 = Arrays.stream(scores).boxed().sorted(Comparator.reverseOrder()).limit(3).toList();   // [95, 95, 88]
            show("top3", top3);
            List<Integer> distinct3 = Arrays.stream(scores).boxed().distinct().sorted(Comparator.reverseOrder()).limit(3).toList();   // [95, 88, 79]
            show("distinct3", distinct3);
            int[] lowest2 = Arrays.stream(scores).sorted().limit(2).toArray();                                           // [41, 60]
            show("lowest2", lowest2);
        }
        {
            int[] scores = {72, 95, 60, 88, 95, 41, 79};
            int[] top3 = topN(scores, 3);           // [95, 95, 88]
            show("top3", top3);
            int[] top10 = topN(scores, 10);         // [95, 95, 88, 79, 72, 60, 41]
            show("top10", top10);
            int[] none = topN(new int[0], 3);       // []
            show("none", none);
        }
        {
            PriorityQueue<Integer> heap = new PriorityQueue<>(List.of(100, 76, 90));
            String inside = heap.toString();        // "[76, 100, 90]"
            show("inside", inside);
        }
        {
            Player[] players = {new Player("ana", 310), new Player("raj", 450), new Player("li", 280), new Player("tom", 450)};
            List<String> podium = Arrays.stream(players).sorted(Comparator.comparingInt(Player::points).reversed()).limit(3).map(Player::name).toList();   // [raj, tom, ana]
            show("podium", podium);
        }
        {
            Player[] players = {new Player("ana", 310), new Player("raj", 450), new Player("li", 280), new Player("tom", 400)};
            List<Player> top2 = topN(players, 2, Comparator.comparingInt(Player::points));   // [Player[name=raj, points=450], Player[name=tom, points=400]]
            show("top2", top2);
            List<Player> slowest = topN(players, 1, Comparator.comparingInt(Player::points).reversed());   // [Player[name=li, points=280]]
            show("slowest", slowest);
        }
        {
            int[] scores = {72, 95, 60, 88, 95, 41, 79};
            List<Integer> top3 = Arrays.stream(scores).boxed().collect(Comparators.greatest(3, Comparator.<Integer>naturalOrder()));   // [95, 95, 88]
            show("top3", top3);
            List<Integer> low2 = Arrays.stream(scores).boxed().collect(Comparators.least(2, Comparator.<Integer>naturalOrder()));      // [41, 60]
            show("low2", low2);
        }
        {
            int[] scores = {72, 95, 60, 88, 95, 41, 79};
            int second = topN(scores, 2)[1];                                                                     // 95
            show("second", second);
            int secondDistinct = Arrays.stream(scores).boxed().distinct().sorted(Comparator.reverseOrder()).skip(1).findFirst().orElseThrow();   // 88
            show("secondDistinct", secondDistinct);
        }
        {
            int[] ratings = {4, 5, 3, 5, 4, 5, 2};
            List<Integer> mostFrequent2 = Arrays.stream(ratings).boxed().collect(Collectors.groupingBy(r -> r, Collectors.counting())).entrySet().stream().sorted(Map.Entry.<Integer, Long>comparingByValue().reversed()).limit(2).map(Map.Entry::getKey).toList();   // [5, 4]
            show("mostFrequent2", mostFrequent2);
        }
        {
            int[] scores = {72, 95, 60, 88, 95, 41, 79};
            int[] idx = IntStream.range(0, scores.length).boxed().sorted(Comparator.comparingInt((Integer i) -> scores[i]).reversed()).limit(3).mapToInt(Integer::intValue).sorted().toArray();   // [1, 3, 4]
            show("idx", idx);
            int[] inOrder = Arrays.stream(idx).map(i -> scores[i]).toArray();   // [95, 88, 95]
            show("inOrder", inOrder);
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
