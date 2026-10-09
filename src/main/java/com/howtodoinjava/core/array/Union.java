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

import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Union of Two Arrays in Java: Set, Stream and Sorted Merge".
 * https://howtodoinjava.com/java/array/union-between-two-arrays/
 */
public class Union {
    static int[] union(int[]... arrays) {
        return Arrays.stream(arrays)
                .flatMapToInt(Arrays::stream)
                .distinct()
                .toArray();
    }
    static int[] unionOfSorted(int[] a, int[] b) {
        int[] out = new int[a.length + b.length];
        int i = 0, j = 0, k = 0;
        while (i < a.length || j < b.length) {
            int next;
            if (j == b.length || (i < a.length && a[i] < b[j])) {
                next = a[i++];
            } else if (i == a.length || b[j] < a[i]) {
                next = b[j++];
            } else {                          // equal values, take one
                next = a[i++];
                j++;
            }
            if (k == 0 || out[k - 1] != next) {
                out[k++] = next;              // skip repeats
            }
        }
        return Arrays.copyOf(out, k);
    }
    static record Tag(String name) {}
    public static void main(String[] args) throws Exception {
        {
            int[] a = {1, 3, 5, 3};
            int[] b = {3, 4, 5};
            int[] union = IntStream.concat(Arrays.stream(a), Arrays.stream(b)).distinct().toArray();   // [1, 3, 5, 4]
            show("union", union);
        }
        {
            int[] first = {1, 3, 5, 3};
            int[] second = {3, 4, 5};
            int[] joined = IntStream.concat(Arrays.stream(first), Arrays.stream(second)).toArray();   // [1, 3, 5, 3, 3, 4, 5]
            show("joined", joined);
            int[] union = IntStream.concat(Arrays.stream(first), Arrays.stream(second)).distinct().toArray();   // [1, 3, 5, 4]
            show("union", union);
        }
        {
            String[] morning = {"ana", "raj", "li"};
            String[] evening = {"li", "tom", "ana"};
            Set<String> attendees = new LinkedHashSet<>(Arrays.asList(morning));
            boolean changed = attendees.addAll(Arrays.asList(evening));   // true, "tom" was added
            show("changed", changed);
            String[] union = attendees.toArray(String[]::new);            // [ana, raj, li, tom]
            show("union", union);
            Set<String> sorted = new TreeSet<>(attendees);                 // [ana, li, raj, tom]
            show("sorted", sorted);
        }
        {
            int[] two = union(new int[]{4, 2, 4}, new int[]{2, 9});          // [4, 2, 9]
            show("two", two);
            int[] three = union(new int[]{1}, new int[]{2, 1}, new int[]{3});  // [1, 2, 3]
            show("three", three);
            int[] sortedUnion = IntStream.of(union(new int[]{8, 2}, new int[]{5, 2})).sorted().toArray();   // [2, 5, 8]
            show("sortedUnion", sortedUnion);
            int[] none = union();                                              // []
            show("none", none);
        }
        {
            int[] merged = unionOfSorted(new int[]{1, 2, 2, 7}, new int[]{2, 3, 9});   // [1, 2, 3, 7, 9]
            show("merged", merged);
            int[] oneEmpty = unionOfSorted(new int[]{}, new int[]{4, 4, 6});           // [4, 6]
            show("oneEmpty", oneEmpty);
        }
        {
            String[] editor = {"read", "write", "publish"};
            String[] reviewer = {"read", "comment"};
            String[] perms = Stream.of(editor, reviewer).flatMap(Arrays::stream).distinct().toArray(String[]::new);   // [read, write, publish, comment]
            show("perms", perms);
            boolean canComment = Arrays.asList(perms).contains("comment");   // true
            show("canComment", canComment);
        }
        {
            Tag[] postA = {new Tag("java"), new Tag("arrays")};
            Tag[] postB = {new Tag("arrays"), new Tag("streams")};
            Tag[] tags = Stream.concat(Arrays.stream(postA), Arrays.stream(postB)).distinct().toArray(Tag[]::new);   // [Tag[name=java], Tag[name=arrays], Tag[name=streams]]
            show("tags", tags);
        }
        {
            Set<String> labels = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
            Collections.addAll(labels, "Java", "Spring");
            Collections.addAll(labels, "java", "JUnit");
            String[] caseless = labels.toArray(String[]::new);   // [Java, JUnit, Spring]
            show("caseless", caseless);
        }
        {
            Set<Integer> onlyOne = new LinkedHashSet<>(List.of(1, 2, 3));
            onlyOne.addAll(List.of(2, 3, 4));
            boolean removed = onlyOne.removeAll(Set.of(2, 3));   // true
            show("removed", removed);
            String result = onlyOne.toString();                  // "[1, 4]"
            show("result", result);
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
