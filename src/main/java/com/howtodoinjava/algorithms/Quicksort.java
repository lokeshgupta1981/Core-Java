package com.howtodoinjava.algorithms;

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
 * Examples for the tutorial "Quicksort in Java".
 * https://howtodoinjava.com/algorithm/quicksort-java-example/
 */
public class Quicksort {
    static void quickSort(int[] a, int low, int high) {
        if (low >= high) {
            return;                         // 0 or 1 element
        }
        int p = partition(a, low, high);
        quickSort(a, low, p - 1);
        quickSort(a, p + 1, high);
    }

    static int partition(int[] a, int low, int high) {
        int pivot = a[high];
        int i = low - 1;                    // end of the "<= pivot" part
        for (int j = low; j < high; j++) {
            if (a[j] <= pivot) {
                i++;
                swap(a, i, j);
            }
        }
        swap(a, i + 1, high);               // pivot to its final place
        return i + 1;
    }

    static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
    static void sort(int[] a) {
        if (a == null || a.length < 2) {
            return;
        }
        quickSort(a, 0, a.length - 1);
    }
    static void quickSortHoare(int[] a, int low, int high) {
        if (low >= high) {
            return;
        }
        int p = hoarePartition(a, low, high);
        quickSortHoare(a, low, p);          // p stays in the left part
        quickSortHoare(a, p + 1, high);
    }

    static int hoarePartition(int[] a, int low, int high) {
        int pivot = a[low + (high - low) / 2];
        int i = low - 1;
        int j = high + 1;
        while (true) {
            do { i++; } while (a[i] < pivot);
            do { j--; } while (a[j] > pivot);
            if (i >= j) {
                return j;
            }
            swap(a, i, j);
        }
    }
    static record Song(String title, int seconds) {}
    static <T> void quickSort(T[] a, Comparator<? super T> cmp, int low, int high) {
        if (low >= high) {
            return;
        }
        T pivot = a[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (cmp.compare(a[j], pivot) <= 0) {
                i++;
                T tmp = a[i]; a[i] = a[j]; a[j] = tmp;
            }
        }
        T tmp = a[i + 1]; a[i + 1] = a[high]; a[high] = tmp;
        quickSort(a, cmp, low, i);
        quickSort(a, cmp, i + 2, high);
    }
    static void quickSortSafe(int[] a, int low, int high) {
        while (low < high) {
            int r = ThreadLocalRandom.current().nextInt(low, high + 1);
            swap(a, r, high);               // random pivot
            int p = partition(a, low, high);
            if (p - low < high - p) {
                quickSortSafe(a, low, p - 1);   // smaller part
                low = p + 1;
            } else {
                quickSortSafe(a, p + 1, high);
                high = p - 1;
            }
        }
    }
    static record Player(String name, int score) {}
    public static void main(String[] args) throws Exception {
        {
            int[] scores = {6, 4, 1, 8, 9, 2, 5};
            quickSort(scores, 0, scores.length - 1);
            int[] sorted = scores;                  // [1, 2, 4, 5, 6, 8, 9]
            show("sorted", sorted);
        }
        {
            int[] empty = {};
            sort(empty);
            int[] stillEmpty = empty;               // []
            show("stillEmpty", stillEmpty);
            sort(null);                             // returns, no exception
            int[] dupes = {3, 1, 3, 1, 2};
            sort(dupes);
            int[] sortedDupes = dupes;              // [1, 1, 2, 3, 3]
            show("sortedDupes", sortedDupes);
        }
        {
            int[] marks = {6, 4, 1, 8, 9, 2, 5};
            quickSortHoare(marks, 0, marks.length - 1);
            int[] hoareSorted = marks;              // [1, 2, 4, 5, 6, 8, 9]
            show("hoareSorted", hoareSorted);
        }
        {
            Song[] playlist = {
                new Song("Intro", 95), new Song("Rain", 240), new Song("Coffee", 180), new Song("Outro", 60)
            };
            quickSort(playlist, Comparator.comparingInt(Song::seconds), 0, playlist.length - 1);
            String first = playlist[0].title();     // "Outro"
            show("first", first);
            String last = playlist[3].title();      // "Rain"
            show("last", last);
        }
        {
            int[] alreadySorted = IntStream.range(0, 20_000).toArray();
            try { sort(alreadySorted);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            int[] bigSorted = IntStream.range(0, 1_000_000).toArray();
            quickSortSafe(bigSorted, 0, bigSorted.length - 1);
            boolean ok = bigSorted[999_999] == 999_999;   // true
            show("ok", ok);
        }
        {
            Random random = new Random(42);
            boolean allMatch = true;
            for (int run = 0; run < 1_000; run++) {
                int[] input = random.ints(random.nextInt(50), -20, 20).toArray();
                int[] expected = input.clone();
                Arrays.sort(expected);
                int[] actual = input.clone();
                quickSortSafe(actual, 0, actual.length - 1);
                allMatch &= Arrays.equals(expected, actual);
            }
            boolean passed = allMatch;              // true
            show("passed", passed);
        }
        {
            Player[] players = {new Player("Bob", 7), new Player("Ann", 9), new Player("Cid", 7), new Player("Dan", 5)};
            quickSort(players, Comparator.comparingInt(Player::score), 0, players.length - 1);
            String order = Arrays.stream(players).map(Player::name).collect(Collectors.joining(", "));   // "Dan, Cid, Bob, Ann"
            show("order", order);
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
