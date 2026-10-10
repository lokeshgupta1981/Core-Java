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
 * Examples for the tutorial "Sort Array in Java: Arrays.sort(), parallelSort() and Ranges".
 * https://howtodoinjava.com/java/sort/java-array-sorting/
 */
public class SortArrays {
    static void reverse(int[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }
    static record Book(String title, int year) {

        @Override
        public String toString() {
            return title + "(" + year + ")";
        }
    }
    static Book[] shelf() {
        return new Book[] {
            new Book("Dune", 1965), new Book("Emma", 1815),
            new Book("Beloved", 1987), new Book("Ulysses", 1922)};
    }
    public static void main(String[] args) throws Exception {
        {
            int[] scores = {70, 95, 40, 85};
            Arrays.sort(scores);
            int[] ascending = scores;                                  // [40, 70, 85, 95]
            show("ascending", ascending);

            String[] cities = {"Pune", "agra", "Delhi"};
            Arrays.sort(cities, String.CASE_INSENSITIVE_ORDER);
            String[] ignoreCase = cities;                              // [agra, Delhi, Pune]
            show("ignoreCase", ignoreCase);

            Integer[] points = {70, 95, 40};
            Arrays.sort(points, Comparator.reverseOrder());
            Integer[] descending = points;                             // [95, 70, 40]
            show("descending", descending);

            int[] laps = {9, 7, 5, 3, 1};
            Arrays.sort(laps, 1, 4);
            int[] partly = laps;                                       // [9, 3, 5, 7, 1]
            show("partly", partly);

            long[] ids = {30L, 10L, 20L};
            Arrays.parallelSort(ids);
            long[] parallel = ids;                                     // [10, 20, 30]
            show("parallel", parallel);
        }
        {
            int[] scores = {70, 95, 40, 85};
            Arrays.sort(scores);
            int[] sortedScores = scores;                               // [40, 70, 85, 95]
            show("sortedScores", sortedScores);

            char[] letters = {'d', 'B', 'a', 'C'};
            Arrays.sort(letters);
            char[] sortedLetters = letters;                            // [B, C, a, d]
            show("sortedLetters", sortedLetters);
        }
        {
            double[] temperatures = {2.5, Double.NaN, 0.0, -0.0, -1.0};
            Arrays.sort(temperatures);
            double[] sortedTemps = temperatures;                       // [-1.0, -0.0, 0.0, 2.5, NaN]
            show("sortedTemps", sortedTemps);
        }
        {
            int[] scores = {70, 95, 40, 85};
            Arrays.sort(scores);
            reverse(scores);
            int[] reversedInPlace = scores;                            // [95, 85, 70, 40]
            show("reversedInPlace", reversedInPlace);

            int[] prices = {70, 95, 40, 85};
            int[] viaStream = IntStream.of(prices).boxed().sorted(Comparator.reverseOrder()).mapToInt(Integer::intValue).toArray();   // [95, 85, 70, 40]
            show("viaStream", viaStream);

            Integer[] boxed = {70, 95, 40, 85};
            Arrays.sort(boxed, Comparator.reverseOrder());
            Integer[] boxedDescending = boxed;                         // [95, 85, 70, 40]
            show("boxedDescending", boxedDescending);
        }
        {
            Book[] books = shelf();
            Arrays.sort(books, Comparator.comparingInt(Book::year));
            Book[] oldestFirst = books;                                // [Emma(1815), Ulysses(1922), Dune(1965), Beloved(1987)]
            show("oldestFirst", oldestFirst);
            Arrays.sort(books, Comparator.comparing(Book::title).reversed());
            Book[] titleDescending = books;                            // [Ulysses(1922), Emma(1815), Dune(1965), Beloved(1987)]
            show("titleDescending", titleDescending);
            Arrays.sort(books, Comparator.comparingInt((Book b) -> b.title().length()).thenComparing(Book::title));
            Book[] byTitleLength = books;                              // [Dune(1965), Emma(1815), Beloved(1987), Ulysses(1922)]
            show("byTitleLength", byTitleLength);
        }
        {
            Book[] books = shelf();
            try { Arrays.sort(books);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            int[] scores = {99, 98, 97, 40, 85, 60};
            Arrays.sort(scores, 3, scores.length);
            int[] newScoresSorted = scores;                            // [99, 98, 97, 40, 60, 85]
            show("newScoresSorted", newScoresSorted);

            String[] words = {"pear", "fig", "apple", "kiwi"};
            Arrays.sort(words, 0, 3, Comparator.comparing(String::length));
            String[] firstThree = words;                               // [fig, pear, apple, kiwi]
            show("firstThree", firstThree);
        }
        {
            int[] scores = {70, 95, 40};
            try { Arrays.sort(scores, 2, 1);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            try { Arrays.sort(scores, 0, 5);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            int[] amounts = new Random(42).ints(1_000_000, 0, 10_000).toArray();
            int[] copy = amounts.clone();
            Arrays.parallelSort(amounts);
            Arrays.sort(copy);
            boolean sameResult = Arrays.equals(amounts, copy);         // true
            show("sameResult", sameResult);
        }
        {
            int[][] rows = {{3, 90}, {1, 75}, {2, 90}, {4, 60}};
            Arrays.sort(rows, Comparator.comparingInt((int[] row) -> row[1]).reversed().thenComparingInt(row -> row[0]));
            int[][] byScore = rows;                                    // [[2, 90], [3, 90], [1, 75], [4, 60]]
            show("byScore", byScore);
            Arrays.sort(rows, Comparator.comparingInt(row -> row[0]));
            int[][] byId = rows;                                       // [[1, 75], [2, 90], [3, 90], [4, 60]]
            show("byId", byId);
        }
        {
            int[] joinOrder = {70, 95, 40, 85};
            int[] sortedCopy = joinOrder.clone();
            Arrays.sort(sortedCopy);
            int[] original = joinOrder;                                // [70, 95, 40, 85]
            show("original", original);
            int[] copySorted = sortedCopy;                             // [40, 70, 85, 95]
            show("copySorted", copySorted);

            int[] fromStream = Arrays.stream(joinOrder).sorted().toArray();                                   // [40, 70, 85, 95]
            show("fromStream", fromStream);
            Book[] booksByYear = Stream.of(shelf()).sorted(Comparator.comparingInt(Book::year)).toArray(Book[]::new);   // [Emma(1815), Ulysses(1922), Dune(1965), Beloved(1987)]
            show("booksByYear", booksByYear);
        }
        {
            String[] names = {"pear", null, "fig"};
            Arrays.sort(names, Comparator.nullsLast(Comparator.naturalOrder()));
            String[] nullsAtEnd = names;                               // [fig, pear, null]
            show("nullsAtEnd", nullsAtEnd);
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
