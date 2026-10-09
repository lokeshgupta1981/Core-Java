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
import com.google.common.primitives.Ints;
import com.google.common.collect.ObjectArrays;
import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Concatenate Two Arrays in Java: arraycopy, Streams, Libraries".
 * https://howtodoinjava.com/java/array/concatenate-arrays/
 */
public class ConcatArrays {
    static int[] concat(int[] first, int[] second) {
        int[] result = new int[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
    static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
    static int[] concatAll(int[]... arrays) {
        int total = 0;
        for (int[] array : arrays) {
            total += array.length;
        }
        int[] result = new int[total];
        int offset = 0;
        for (int[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
    static String[] buildCommand(String[] defaults, String[] userArgs) {
        String[] extra = Objects.requireNonNullElse(userArgs, new String[0]);
        String[] command = Arrays.copyOf(defaults, defaults.length + extra.length);
        System.arraycopy(extra, 0, command, defaults.length, extra.length);
        return command;
    }
    public static void main(String[] args) throws Exception {
        {
            int[] first = {1, 2, 3};
            int[] second = {4, 5};
            int[] merged = Arrays.copyOf(first, first.length + second.length);   // [1, 2, 3, 0, 0]
            show("merged", merged);
            System.arraycopy(second, 0, merged, first.length, second.length);
            String result = Arrays.toString(merged);                              // "[1, 2, 3, 4, 5]"
            show("result", result);

            String[] fruits = {"apple", "kiwi"};
            String[] veggies = {"pea"};
            String[] basket = Stream.concat(Arrays.stream(fruits), Arrays.stream(veggies)).toArray(String[]::new);   // [apple, kiwi, pea]
            show("basket", basket);
        }
        {
            int[] scores = concat(new int[]{7, 9}, new int[]{4});   // [7, 9, 4]
            show("scores", scores);
            int[] same = concat(new int[]{7, 9}, new int[0]);       // [7, 9]
            show("same", same);
        }
        {
            String[] team = concat(new String[]{"Lokesh", "Alex"}, new String[]{"Maria"});   // [Lokesh, Alex, Maria]
            show("team", team);
            Integer[] boxed = concat(new Integer[]{1}, new Integer[]{2, 3});                  // [1, 2, 3]
            show("boxed", boxed);
        }
        {
            try { Object[] mixed = concat(new String[]{"a"}, new Object[]{1}); show("mixed", mixed); } catch (Throwable _t) { System.out.println("mixed -> " + _t); }
            Object[] safe = concat(new Object[]{"a"}, new Object[]{1});    // [a, 1]
            show("safe", safe);
        }
        {
            int[] week = concatAll(new int[]{1, 2}, new int[]{3}, new int[]{4, 5, 6});   // [1, 2, 3, 4, 5, 6]
            show("week", week);
            int[] nothing = concatAll();                                               // []
            show("nothing", nothing);
        }
        {
            String[] morning = {"tea", "toast"};
            String[] evening = {"soup"};
            String[] meals = Stream.concat(Arrays.stream(morning), Arrays.stream(evening)).toArray(String[]::new);   // [tea, toast, soup]
            show("meals", meals);
        }
        {
            int[] a = {1, 2};
            int[] b = {3, 4};
            int[] ints = IntStream.concat(Arrays.stream(a), Arrays.stream(b)).toArray();            // [1, 2, 3, 4]
            show("ints", ints);
            double[] prices = DoubleStream.concat(DoubleStream.of(1.5), DoubleStream.of(2.0)).toArray();   // [1.5, 2.0]
            show("prices", prices);
        }
        {
            String[] a1 = {"x"};
            String[] a2 = {"y", "z"};
            String[] a3 = {"w"};
            String[] all = Stream.of(a1, a2, a3).flatMap(Arrays::stream).toArray(String[]::new);   // [x, y, z, w]
            show("all", all);
            int[] nums = Stream.of(new int[]{1}, new int[]{2, 3}).flatMapToInt(Arrays::stream).toArray();   // [1, 2, 3]
            show("nums", nums);
        }
        {
            String[] left = {"red", null, "blue"};
            String[] right = {"green"};
            String[] colors = new String[left.length + right.length];
            int count = 0;
            for (String c : left) {
                if (c != null) {
                    colors[count++] = c;
                }
            }
            for (String c : right) {
                colors[count++] = c;
            }
            String[] trimmed = Arrays.copyOf(colors, count);   // [red, blue, green]
            show("trimmed", trimmed);
        }
        {
            int[] joined = ArrayUtils.addAll(new int[]{1, 2}, 3, 4);                  // [1, 2, 3, 4]
            show("joined", joined);
            String[] names = ArrayUtils.addAll(new String[]{"Ann"}, "Bob", "Cid");    // [Ann, Bob, Cid]
            show("names", names);
            int[] missing = null;
            int[] fromNull = ArrayUtils.addAll(missing, new int[]{5, 6});              // [5, 6]
            show("fromNull", fromNull);
        }
        {
            int[] gInts = Ints.concat(new int[]{1}, new int[]{2}, new int[]{3});                  // [1, 2, 3]
            show("gInts", gInts);
            String[] gWords = ObjectArrays.concat(new String[]{"a"}, new String[]{"b"}, String.class);   // [a, b]
            show("gWords", gWords);
        }
        {
            String[] defaults = {"java", "-Xmx512m"};
            String[] userArgs = {"-jar", "report.jar"};
            List<String> command = new ProcessBuilder(buildCommand(defaults, userArgs)).command();   // [java, -Xmx512m, -jar, report.jar]
            show("command", command);
            String[] onlyDefaults = buildCommand(defaults, null);                                     // [java, -Xmx512m]
            show("onlyDefaults", onlyDefaults);
        }
        {
            int[] x1 = {1, 2, 3};
            int[] x2 = {3, 4, 1};
            int[] union = IntStream.concat(Arrays.stream(x1), Arrays.stream(x2)).distinct().toArray();   // [1, 2, 3, 4]
            show("union", union);
        }
        {
            int[] base = {1, 2};
            int[] longer = Arrays.copyOf(base, base.length + 1);
            longer[base.length] = 3;
            String appended = Arrays.toString(longer);   // "[1, 2, 3]"
            show("appended", appended);
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
