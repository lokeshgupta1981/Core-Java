package com.howtodoinjava.core.sorting;

import java.util.regex.*;
import java.net.*;
import java.text.*;
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
 * Examples for the tutorial "Java Stream Sort With Null Values: nullsFirst and nullsLast".
 * https://howtodoinjava.com/java/sort/stream-sort-with-null-values/
 */
public class StreamSortNullValues {
    static record Task(String title, LocalDate dueDate) {

        @Override
        public String toString() {
            return title + "(" + dueDate + ")";
        }
    }
    static List<Task> sampleTasks() {
        return List.of(
        new Task("rent", LocalDate.of(2026, 11, 1)),
        new Task("milk", null),
        new Task("taxes", LocalDate.of(2026, 10, 15)),
        new Task("gym", null),
        new Task("passport", LocalDate.of(2027, 1, 20)));
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> fruits = Arrays.asList("banana", null, "apple", "cherry", null);

            try { List<String> plain = fruits.stream().sorted().toList(); show("plain", plain); } catch (Throwable _t) { System.out.println("plain -> " + _t); }
            List<String> nullsFirst = fruits.stream().sorted(Comparator.nullsFirst(Comparator.naturalOrder())).toList();   // [null, null, apple, banana, cherry]
            show("nullsFirst", nullsFirst);
            List<String> nullsLast = fruits.stream().sorted(Comparator.nullsLast(Comparator.naturalOrder())).toList();     // [apple, banana, cherry, null, null]
            show("nullsLast", nullsLast);
            List<String> descending = fruits.stream().sorted(Comparator.nullsLast(Comparator.reverseOrder())).toList();    // [cherry, banana, apple, null, null]
            show("descending", descending);

            List<Task> tasks = List.of(new Task("rent", LocalDate.of(2026, 11, 1)), new Task("milk", null), new Task("taxes", LocalDate.of(2026, 10, 15)));
            List<Task> byDueDate = tasks.stream().sorted(Comparator.comparing(Task::dueDate, Comparator.nullsLast(Comparator.naturalOrder()))).toList();   // [taxes(2026-10-15), rent(2026-11-01), milk(null)]
            show("byDueDate", byDueDate);
        }
        {
            Task rent = new Task("rent", LocalDate.of(2026, 11, 1));
            Task taxes = new Task("taxes", LocalDate.of(2026, 10, 15));
            List<Task> withNullTasks = Arrays.asList(rent, null, taxes, null);

            List<Task> sorted = withNullTasks.stream().sorted(Comparator.nullsFirst(Comparator.comparing(Task::dueDate))).toList();   // [null, null, taxes(2026-10-15), rent(2026-11-01)]
            show("sorted", sorted);
        }
        {
            List<Task> tasks = sampleTasks();
            try { List<Task> unsafe = tasks.stream().sorted(Comparator.comparing(Task::dueDate)).toList(); show("unsafe", unsafe); } catch (Throwable _t) { System.out.println("unsafe -> " + _t); }
        }
        {
            List<Task> tasks = sampleTasks();
            Comparator<Task> byDueDateManual = (t1, t2) -> {
                if (t1.dueDate() == null && t2.dueDate() == null) {
                    return 0;
                } else if (t1.dueDate() == null) {
                    return 1;
                } else if (t2.dueDate() == null) {
                    return -1;
                }
                return t1.dueDate().compareTo(t2.dueDate());
            };
            List<Task> sorted = tasks.stream().sorted(byDueDateManual).toList();   // [taxes(2026-10-15), rent(2026-11-01), passport(2027-01-20), milk(null), gym(null)]
            show("sorted", sorted);
        }
        {
            List<Task> tasks = sampleTasks();
            Comparator<Task> byDueDate = Comparator.comparing(Task::dueDate, Comparator.nullsLast(Comparator.naturalOrder()));
            List<Task> sorted = tasks.stream().sorted(byDueDate).toList();   // [taxes(2026-10-15), rent(2026-11-01), passport(2027-01-20), milk(null), gym(null)]
            show("sorted", sorted);
        }
        {
            Task rent = new Task("rent", LocalDate.of(2026, 11, 1));
            Task milk = new Task("milk", null);
            Task taxes = new Task("taxes", LocalDate.of(2026, 10, 15));
            List<Task> both = Arrays.asList(rent, null, milk, taxes, null);
            Comparator<Task> byDueDate = Comparator.comparing(Task::dueDate, Comparator.nullsLast(Comparator.naturalOrder()));

            List<Task> sorted = both.stream().sorted(Comparator.nullsLast(byDueDate)).toList();   // [taxes(2026-10-15), rent(2026-11-01), milk(null), null, null]
            show("sorted", sorted);
        }
        {
            List<Task> tasks = sampleTasks();
            Comparator<Task> byDueDate = Comparator.comparing(Task::dueDate, Comparator.nullsLast(Comparator.naturalOrder()));
            List<Task> sorted = tasks.stream().sorted(byDueDate.thenComparing(Task::title)).toList();   // [taxes(2026-10-15), rent(2026-11-01), passport(2027-01-20), gym(null), milk(null)]
            show("sorted", sorted);
        }
        {
            List<Integer> scores = Arrays.asList(70, null, 95, 40, null);
            List<Integer> sorted = scores.stream().sorted(Comparator.nullsLast(Comparator.naturalOrder())).toList();   // [40, 70, 95, null, null]
            show("sorted", sorted);
        }
        {
            List<Integer> scores = Arrays.asList(70, null, 95, 40, null);
            List<Integer> sorted = scores.stream().sorted(Comparator.nullsFirst(Comparator.naturalOrder())).toList();   // [null, null, 40, 70, 95]
            show("sorted", sorted);
        }
        {
            List<String> fruits = Arrays.asList("banana", null, "apple", "cherry", null);
            List<String> descending = fruits.stream().sorted(Comparator.nullsLast(Comparator.reverseOrder())).toList();   // [cherry, banana, apple, null, null]
            show("descending", descending);

            List<Task> tasks = sampleTasks();
            List<Task> latestFirst = tasks.stream().sorted(Comparator.comparing(Task::dueDate, Comparator.nullsLast(Comparator.reverseOrder()))).toList();   // [passport(2027-01-20), rent(2026-11-01), taxes(2026-10-15), milk(null), gym(null)]
            show("latestFirst", latestFirst);
        }
        {
            List<String> fruits = Arrays.asList("banana", null, "apple", "cherry", null);
            List<String> reversed = fruits.stream().sorted(Comparator.nullsLast(Comparator.<String>naturalOrder()).reversed()).toList();   // [null, null, cherry, banana, apple]
            show("reversed", reversed);

            List<Task> tasks = sampleTasks();
            Comparator<Task> byDueDate = Comparator.comparing(Task::dueDate, Comparator.nullsLast(Comparator.naturalOrder()));
            List<Task> reversedTasks = tasks.stream().sorted(byDueDate.reversed()).toList();   // [milk(null), gym(null), passport(2027-01-20), rent(2026-11-01), taxes(2026-10-15)]
            show("reversedTasks", reversedTasks);
        }
        {
            List<String> fruits = Arrays.asList("banana", null, "apple", "cherry", null);
            List<String> withoutNulls = fruits.stream().filter(Objects::nonNull).sorted().toList();   // [apple, banana, cherry]
            show("withoutNulls", withoutNulls);
        }
        {
            List<String> list = new ArrayList<>(Arrays.asList("banana", null, "apple", "cherry", null));
            list.sort(Comparator.nullsLast(Comparator.naturalOrder()));
            List<String> sortedList = list;                                         // [apple, banana, cherry, null, null]
            show("sortedList", sortedList);

            String[] array = {"banana", null, "apple"};
            Arrays.sort(array, Comparator.nullsFirst(Comparator.naturalOrder()));
            String[] sortedArray = array;                                           // [null, apple, banana]
            show("sortedArray", sortedArray);
        }
        {
            List<String> fruits = Arrays.asList("banana", null, "apple", "cherry", null);
            List<String> nullsMoved = fruits.stream().sorted(Comparator.nullsLast(null)).toList();   // [banana, apple, cherry, null, null]
            show("nullsMoved", nullsMoved);
        }
        {
            try { List<String> rejected = List.of("banana", null); show("rejected", rejected); } catch (Throwable _t) { System.out.println("rejected -> " + _t); }
            List<String> accepted = Arrays.asList("banana", null);   // [banana, null]
            show("accepted", accepted);
        }
        {
            List<String> mixedCase = Arrays.asList("banana", null, "Apple", "cherry");
            List<String> sorted = mixedCase.stream().sorted(Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)).toList();   // [Apple, banana, cherry, null]
            show("sorted", sorted);
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
