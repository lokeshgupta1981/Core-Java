package com.howtodoinjava.core.collections.cursors;

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
 * Examples for the tutorial "Iterator vs ListIterator vs Spliterator in Java (+ Enumeration)".
 * https://howtodoinjava.com/java/collections/iterator-listiterator-vs-spliterator/
 */
public class CursorComparison {

    public static void main(String[] args) throws Exception {
        {
            List<String> colors = new ArrayList<>(List.of("red", "green", "blue"));

            Iterator<String> it = colors.iterator();
            String first = it.next();                         // "red"
            show("first", first);
            it.remove();                                      // Iterator removes "red"

            ListIterator<String> li = colors.listIterator(colors.size());
            String last = li.previous();                      // "blue", moving backward
            show("last", last);
            li.set("navy");                                   // ListIterator replaces "blue"

            Spliterator<String> sp = colors.spliterator();
            Spliterator<String> half = sp.trySplit();
            long splitOff = half.estimateSize();              // 1, Spliterator hands "green" to a second part
            show("splitOff", splitOff);
            long remaining = sp.estimateSize();               // 1
            show("remaining", remaining);
            String result = String.join(",", colors);         // "green,navy"
            show("result", result);
        }
        {
            Vector<Integer> vector = new Vector<>(List.of(1, 2, 3));
            Enumeration<Integer> enumeration = vector.elements();
            StringJoiner seen = new StringJoiner(" ");
            while (enumeration.hasMoreElements()) {
                seen.add(String.valueOf(enumeration.nextElement()));
            }
            String printed = seen.toString();                 // "1 2 3"
            show("printed", printed);
            Iterator<Integer> modern = vector.elements().asIterator();
            Integer firstValue = modern.next();               // 1
            show("firstValue", firstValue);
        }
        {
            Set<Integer> scores = new TreeSet<>(Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
            Iterator<Integer> iterator = scores.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() % 5 == 0) {
                    iterator.remove();
                }
            }
            String left = scores.toString();                  // "[1, 2, 3, 4, 6, 7, 8, 9]"
            show("left", left);
        }
        {
            List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
            ListIterator<Integer> iterator = numbers.listIterator();
            while (iterator.hasNext()) {
                int num = iterator.next();
                if (num == 1) {
                    iterator.remove();       // removes 1
                } else if (num == 5) {
                    iterator.set(50);        // changes 5 to 50
                }
            }
            iterator.add(6);                 // adds 6 at the end
            String result = numbers.toString();               // "[2, 3, 4, 50, 6]"
            show("result", result);
        }
        {
            List<String> bigList = Stream.generate(() -> "Hello").limit(30_000).toList();
            Spliterator<String> split = bigList.spliterator();
            long before = split.estimateSize();               // 30000
            show("before", before);
            Spliterator<String> split1 = split.trySplit();
            long secondHalf = split.estimateSize();           // 15000
            show("secondHalf", secondHalf);
            long firstHalf = split1.estimateSize();           // 15000
            show("firstHalf", firstHalf);
            LongAdder processed = new LongAdder();
            try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
                pool.submit(() -> split.forEachRemaining(s -> processed.increment()));
                pool.submit(() -> split1.forEachRemaining(s -> processed.increment()));
            }
            long done = processed.sum();                      // 30000
            show("done", done);
        }
        {
            List<String> names = List.of("red", "green");
            StringJoiner joined = new StringJoiner("+");
            for (String name : names) {
                joined.add(name);
            }
            String both = joined.toString();                  // "red+green"
            show("both", both);
        }
        {
            List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
            ListIterator<Integer> iterator = numbers.listIterator();
            Integer a = iterator.next();                      // 1
            show("a", a);
            Integer b = iterator.next();                      // 2
            show("b", b);
            int index = iterator.previousIndex();             // 1
            show("index", index);
            Integer c = iterator.previous();                  // 2
            show("c", c);
            Integer d = iterator.next();                      // 2
            show("d", d);
        }
        {
            List<String> list = Stream.generate(() -> "Hello").limit(10).toList();
            Spliterator<String> split = list.spliterator();
            boolean r1 = split.tryAdvance(s -> {});           // true
            show("r1", r1);
            boolean r2 = split.tryAdvance(s -> {});           // true
            show("r2", r2);
            boolean r3 = split.tryAdvance(s -> {});           // true
            show("r3", r3);
            long left = split.estimateSize();                 // 7
            show("left", left);
        }
        {
            List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
            ListIterator<Integer> iterator = numbers.listIterator();
            Integer firstNumber = iterator.next();            // 1
            show("firstNumber", firstNumber);
            numbers.add(0, 10);                               // changes the list outside the iterator
            try { Integer second = iterator.next(); show("second", second); } catch (Throwable _t) { System.out.println("second -> " + _t); }
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
