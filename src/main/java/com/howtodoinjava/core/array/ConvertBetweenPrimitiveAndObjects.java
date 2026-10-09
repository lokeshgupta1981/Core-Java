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
 * Examples for the tutorial "Convert int[] to Integer[] in Java and Back to int[]".
 * https://howtodoinjava.com/java/array/convert-between-array-of-primitives-and-objects/
 */
public class ConvertBetweenPrimitiveAndObjects {

    public static void main(String[] args) throws Exception {
        {
            int[] scores = {70, 85, 90};
            Integer[] boxed = Arrays.stream(scores).boxed().toArray(Integer[]::new);    // [70, 85, 90]
            show("boxed", boxed);
            int[] unboxed = Arrays.stream(boxed).mapToInt(Integer::intValue).toArray();  // [70, 85, 90]
            show("unboxed", unboxed);
        }
        {
            int[] scores = {70, 85, 90};
            Integer[] boxed = IntStream.of(scores).boxed().toArray(Integer[]::new);   // [70, 85, 90]
            show("boxed", boxed);
            Object[] objects = IntStream.of(scores).boxed().toArray();               // [70, 85, 90], but typed Object[]
            show("objects", objects);
        }
        {
            int[] scores = {70, 85, 90};
            Integer[] viaLoop = new Integer[scores.length];
            for (int i = 0; i < scores.length; i++) {
                viaLoop[i] = scores[i];
            }
            Integer[] viaSetAll = new Integer[scores.length];
            Arrays.setAll(viaSetAll, i -> scores[i]);
            String result = Arrays.toString(viaSetAll);   // "[70, 85, 90]"
            show("result", result);
        }
        {
            Integer[] ratings = {4, 5, 3};
            int[] values = Arrays.stream(ratings).mapToInt(Integer::intValue).toArray();   // [4, 5, 3]
            show("values", values);
        }
        {
            Integer[] ratings = {4, null, 3};
            try { int[] crash = Arrays.stream(ratings).mapToInt(Integer::intValue).toArray(); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
            int[] withZero = Arrays.stream(ratings).mapToInt(r -> r == null ? 0 : r).toArray();  // [4, 0, 3]
            show("withZero", withZero);
            int[] answered = Arrays.stream(ratings).filter(Objects::nonNull).mapToInt(Integer::intValue).toArray();   // [4, 3]
            show("answered", answered);
        }
        {
            long[] sizes = {2048L, 512L};
            Long[] boxedSizes = Arrays.stream(sizes).boxed().toArray(Long[]::new);   // [2048, 512]
            show("boxedSizes", boxedSizes);
            char[] grades = {'A', 'B'};
            Character[] boxedGrades = new String(grades).chars().mapToObj(c -> (char) c).toArray(Character[]::new);   // [A, B]
            show("boxedGrades", boxedGrades);
        }
        {
            int[] scores = {70, 85, 90};
            Integer[] boxed = ArrayUtils.toObject(scores);           // [70, 85, 90]
            show("boxed", boxed);
            Integer[] ratings = {4, null, 3};
            int[] safe = ArrayUtils.toPrimitive(ratings, 0);         // [4, 0, 3]
            show("safe", safe);
            try { int[] strict = ArrayUtils.toPrimitive(ratings); show("strict", strict); } catch (Throwable _t) { System.out.println("strict -> " + _t); }
            int[] none = ArrayUtils.toPrimitive((Integer[]) null);   // null
            show("none", none);
        }
        {
            int[] scores = {70, 95, 85, 60};
            Integer[] boxed = Arrays.stream(scores).boxed().toArray(Integer[]::new);
            Arrays.sort(boxed, Comparator.reverseOrder());
            int[] ranked = Arrays.stream(boxed).mapToInt(Integer::intValue).toArray();   // [95, 85, 70, 60]
            show("ranked", ranked);
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
