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

import org.apache.commons.lang3.ArrayUtils;
import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Remove an Element from an Array in Java: Index, Value, Filter".
 * https://howtodoinjava.com/java/array/removing-items-from-array/
 */
public class RemoveArrayItems {
    static int[] removeAt(int[] array, int index) {
        Objects.checkIndex(index, array.length);
        int[] result = new int[array.length - 1];
        System.arraycopy(array, 0, result, 0, index);
        System.arraycopy(array, index + 1, result, index, array.length - index - 1);
        return result;
    }
    static <T> T[] removeAt(T[] array, int index) {
        Objects.checkIndex(index, array.length);
        T[] result = Arrays.copyOf(array, array.length - 1);
        System.arraycopy(array, index + 1, result, index, array.length - index - 1);
        return result;
    }
    static int[] removeFirst(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return removeAt(array, i);
            }
        }
        return array;
    }
    static int removeInPlace(int[] array, int size, int index) {
        Objects.checkIndex(index, size);
        System.arraycopy(array, index + 1, array, index, size - index - 1);
        array[size - 1] = 0;          // clear the freed slot
        return size - 1;
    }
    public static void main(String[] args) throws Exception {
        {
            int[] slots = {10, 20, 30, 40, 50};
            int[] noIndex2 = removeAt(slots, 2);                                // [10, 20, 40, 50]
            show("noIndex2", noIndex2);
            int[] no40 = Arrays.stream(slots).filter(v -> v != 40).toArray();   // [10, 20, 30, 50]
            show("no40", no40);
            int[] noFirst = ArrayUtils.remove(slots, 0);                        // [20, 30, 40, 50]
            show("noFirst", noFirst);
            int length = slots.length;                                          // 5, the original is unchanged
            show("length", length);
        }
        {
            int[] readings = {7, 3, 9, 4};
            int[] withoutFirst = removeAt(readings, 0);   // [3, 9, 4]
            show("withoutFirst", withoutFirst);
            int[] withoutLast = removeAt(readings, 3);    // [7, 3, 9]
            show("withoutLast", withoutLast);
            try { int[] invalid = removeAt(readings, 4); show("invalid", invalid); } catch (Throwable _t) { System.out.println("invalid -> " + _t); }
        }
        {
            String[] tasks = {"email", "lunch", "gym", "call"};
            String[] done = removeAt(tasks, 1);   // [email, gym, call]
            show("done", done);
        }
        {
            int[] votes = {3, 1, 3, 2};
            int[] oneLess = removeFirst(votes, 3);    // [1, 3, 2]
            show("oneLess", oneLess);
            int[] unchanged = removeFirst(votes, 9);  // [3, 1, 3, 2]
            show("unchanged", unchanged);
        }
        {
            int[] marks = {3, 1, 3, 2};
            int[] noThrees = Arrays.stream(marks).filter(m -> m != 3).toArray();   // [1, 2]
            show("noThrees", noThrees);
            String[] tags = {"java", null, "draft", "draft"};
            String[] published = Arrays.stream(tags).filter(t -> !Objects.equals(t, "draft")).toArray(String[]::new);   // [java, null]
            show("published", published);
            String[] noNulls = Arrays.stream(tags).filter(Objects::nonNull).toArray(String[]::new);                   // [java, draft, draft]
            show("noNulls", noNulls);
        }
        {
            int[] queue = {10, 20, 30, 40, 50};
            int size = removeInPlace(queue, queue.length, 1);   // 4
            show("size", size);
            String raw = Arrays.toString(queue);                // "[10, 30, 40, 50, 0]"
            show("raw", raw);
            int[] used = Arrays.copyOf(queue, size);            // [10, 30, 40, 50]
            show("used", used);
        }
        {
            String[] items = {"pen", "ink", "cap", "box"};
            List<String> fixed = Arrays.asList(items);
            try { boolean failed = fixed.remove("ink"); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
        }
        {
            String[] items = {"pen", "ink", "cap", "box"};
            List<String> list = new ArrayList<>(Arrays.asList(items));
            list.remove("ink");
            list.removeIf(s -> s.startsWith("c"));
            String[] left = list.toArray(String[]::new);   // [pen, box]
            show("left", left);
        }
        {
            List<Integer> codes = new ArrayList<>(List.of(5, 7, 1));
            Integer byIndex = codes.remove(1);                       // 7, removed position 1
            show("byIndex", byIndex);
            boolean byValue = codes.remove(Integer.valueOf(1));      // true, removed value 1
            show("byValue", byValue);
            List<Integer> rest = codes;                              // [5]
            show("rest", rest);
        }
        {
            int[] nums = {1, 1, 2, 2, 3, 3, 3};
            int[] r1 = ArrayUtils.remove(nums, 6);                     // [1, 1, 2, 2, 3, 3]
            show("r1", r1);
            int[] r2 = ArrayUtils.removeAll(nums, 0, 2, 4);            // [1, 2, 3, 3]
            show("r2", r2);
            int[] r3 = ArrayUtils.removeElement(nums, 2);              // [1, 1, 2, 3, 3, 3]
            show("r3", r3);
            int[] r4 = ArrayUtils.removeElements(nums, 1, 3, 3);       // [1, 2, 2, 3]
            show("r4", r4);
            int[] r5 = ArrayUtils.removeAllOccurrences(nums, 3);       // [1, 1, 2, 2]
            show("r5", r5);
            try { int[] r6 = ArrayUtils.remove(nums, 7); show("r6", r6); } catch (Throwable _t) { System.out.println("r6 -> " + _t); }
        }
        {
            int[] bookings = {501, 502, 503, 504, 505, 506};
            Set<Integer> cancelled = Set.of(502, 505, 999);
            int[] active = Arrays.stream(bookings).filter(id -> !cancelled.contains(id)).toArray();   // [501, 503, 504, 506]
            show("active", active);
            int removed = bookings.length - active.length;                                           // 2
            show("removed", removed);
        }
        {
            int[] stack = {4, 8, 15};
            int[] popped = stack.length > 0 ? Arrays.copyOf(stack, stack.length - 1) : stack;   // [4, 8]
            show("popped", popped);
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
