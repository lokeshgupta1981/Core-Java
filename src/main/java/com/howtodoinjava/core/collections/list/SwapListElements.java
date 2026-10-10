package com.howtodoinjava.core.collections.list;

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
 * Examples for the tutorial "Swap Two Elements in an ArrayList With Collections.swap()".
 * https://howtodoinjava.com/java/collections/arraylist/swap-two-elements-arraylist/
 */
public class SwapListElements {
    static boolean swapValues(List<String> list, String first, String second) {
        int i = list.indexOf(first);
        int j = list.indexOf(second);
        if (i < 0 || j < 0) {
            return false;
        }
        Collections.swap(list, i, j);
        return true;
    }
    static boolean trySwap(List<?> list, int i, int j) {
        if (i < 0 || j < 0 || i >= list.size() || j >= list.size()) {
            return false;
        }
        Collections.swap(list, i, j);
        return true;
    }
    static List<String> moveUp(List<String> tasks, int index) {
        if (index > 0 && index < tasks.size()) {
            Collections.swap(tasks, index, index - 1);
        }
        return tasks;
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> chores = new ArrayList<>(List.of("dishes", "laundry", "vacuum", "trash"));
            Collections.swap(chores, 0, 2);
            List<String> swapped = chores;                             // [vacuum, laundry, dishes, trash]
            show("swapped", swapped);
            String old = chores.set(1, chores.set(3, chores.get(1)));  // "laundry", swaps 1 and 3
            show("old", old);
            List<String> manual = chores;                              // [vacuum, trash, dishes, laundry]
            show("manual", manual);
            try { Collections.swap(chores, 0, 4);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> chores = new ArrayList<>(List.of("dishes", "laundry", "vacuum"));
            Collections.swap(chores, 1, 1);
            List<String> same = chores;                                // [dishes, laundry, vacuum]
            show("same", same);
            try { Collections.swap(chores, -1, 2);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> chores = new ArrayList<>(List.of("dishes", "laundry", "vacuum"));
            String temp = chores.get(0);
            chores.set(0, chores.get(2));
            chores.set(2, temp);
            List<String> swapped = chores;                             // [vacuum, laundry, dishes]
            show("swapped", swapped);
        }
        {
            List<String> chores = new ArrayList<>(List.of("dishes", "laundry", "trash"));
            boolean done = swapValues(chores, "trash", "dishes");      // true
            show("done", done);
            List<String> result = chores;                              // [trash, laundry, dishes]
            show("result", result);
            boolean missing = swapValues(chores, "trash", "garden");   // false
            show("missing", missing);
        }
        {
            List<String> fixed = List.of("dishes", "laundry");
            try { Collections.swap(fixed, 0, 1);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            List<String> copy = new ArrayList<>(fixed);
            Collections.swap(copy, 0, 1);
            List<String> swapped = copy;                               // [laundry, dishes]
            show("swapped", swapped);
        }
        {
            List<String> chores = new ArrayList<>(List.of("dishes", "laundry"));
            boolean ok = trySwap(chores, 0, 1);                        // true
            show("ok", ok);
            boolean outside = trySwap(chores, 1, 2);                   // false
            show("outside", outside);
        }
        {
            int[] scores = {10, 20, 30};
            int temp = scores[0];
            scores[0] = scores[2];
            scores[2] = temp;
            int[] swappedScores = scores;                              // [30, 20, 10]
            show("swappedScores", swappedScores);

            String[] days = {"mon", "tue", "wed"};
            Collections.swap(Arrays.asList(days), 0, 1);
            String[] swappedDays = days;                               // [tue, mon, wed]
            show("swappedDays", swappedDays);
        }
        {
            List<String> tasks = new ArrayList<>(List.of("email", "standup", "review", "lunch"));
            List<String> afterClick = moveUp(tasks, 2);                // [email, review, standup, lunch]
            show("afterClick", afterClick);
            List<String> topRow = moveUp(tasks, 0);                    // [email, review, standup, lunch]
            show("topRow", topRow);
        }
        {
            List<String> tasks = new ArrayList<>(List.of("email", "standup", "review", "lunch"));
            Collections.rotate(tasks.subList(0, 4), 1);
            List<String> lunchFirst = tasks;                           // [lunch, email, standup, review]
            show("lunchFirst", lunchFirst);
            List<String> other = new ArrayList<>(List.of("email", "standup", "review", "lunch"));
            other.add(0, other.remove(3));
            List<String> sameResult = other;                           // [lunch, email, standup, review]
            show("sameResult", sameResult);
        }
        {
            List<String> chores = new ArrayList<>(List.of("dishes", "laundry", "trash"));
            Collections.swap(chores, 0, chores.size() - 1);
            List<String> ends = chores;                                // [trash, laundry, dishes]
            show("ends", ends);
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
