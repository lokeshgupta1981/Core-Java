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
import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Convert Primitive Array to List in Java (int[] to List)".
 * https://howtodoinjava.com/java/array/convert-primitive-array-to-list/
 */
public class ConvertPrimitiveArrayToList {

    public static void main(String[] args) throws Exception {
        {
            int[] steps = {4200, 8100, 6500};
            List<Integer> stepList = IntStream.of(steps).boxed().toList();   // [4200, 8100, 6500]
            show("stepList", stepList);
            int wrongSize = Arrays.asList(steps).size();                    // 1, the list holds the int[] itself
            show("wrongSize", wrongSize);
        }
        {
            int[] steps = {4200, 8100, 6500};
            List<Integer> ints = Arrays.stream(steps).boxed().toList();         // [4200, 8100, 6500]
            show("ints", ints);
            long[] fileSizes = {2048L, 512L};
            List<Long> longs = LongStream.of(fileSizes).boxed().toList();      // [2048, 512]
            show("longs", longs);
            double[] prices = {9.99, 4.5};
            List<Double> doubles = DoubleStream.of(prices).boxed().toList();   // [9.99, 4.5]
            show("doubles", doubles);
        }
        {
            int[] steps = {4200, 8100, 6500};
            List<Integer> fixed = IntStream.of(steps).boxed().toList();
            try { boolean added = fixed.add(7000); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
            ArrayList<Integer> editable = IntStream.of(steps).boxed().collect(Collectors.toCollection(ArrayList::new));
            boolean addedOk = editable.add(7000);                              // true
            show("addedOk", addedOk);
        }
        {
            float[] temps = {21.5f, 23.0f};
            List<Float> tempList = new ArrayList<>(temps.length);
            for (float t : temps) {
                tempList.add(t);
            }
            String shown = tempList.toString();   // "[21.5, 23.0]"
            show("shown", shown);
        }
        {
            boolean[] seats = {true, false, true};
            List<Boolean> seatList = new ArrayList<>(seats.length);
            for (boolean taken : seats) {
                seatList.add(taken);
            }
            int seatCount = seatList.size();   // 3
            show("seatCount", seatCount);
        }
        {
            char[] grades = {'A', 'C', 'B'};
            List<Character> gradeList = new String(grades).chars().mapToObj(c -> (char) c).toList();   // [A, C, B]
            show("gradeList", gradeList);
        }
        {
            int[] steps = {4200, 8100, 6500};
            List<Integer> view = Ints.asList(steps);
            Integer old = view.set(0, 5000);       // 4200
            show("old", old);
            int first = steps[0];                  // 5000, the array changed too
            show("first", first);
            try { boolean added = view.add(7000); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            float[] temps = {21.5f, 23.0f};
            List<Float> tempList = Arrays.asList(ArrayUtils.toObject(temps));   // [21.5, 23.0]
            show("tempList", tempList);
            boolean[] seats = {true, false};
            List<Boolean> seatList = List.of(ArrayUtils.toObject(seats));        // [true, false]
            show("seatList", seatList);
        }
        {
            int[] week = {4200, 8100, 6500, 9900, 3000, 7200, 5600};
            List<Integer> bars = IntStream.of(week).boxed().toList();   // [4200, 8100, 6500, 9900, 3000, 7200, 5600]
            show("bars", bars);
            int best = Collections.max(bars);                          // 9900
            show("best", best);
            int bestDay = bars.indexOf(best);                          // 3
            show("bestDay", bestDay);
        }
        {
            int[] steps = {4200, 8100, 6500};
            ArrayList<Integer> list = IntStream.of(steps).boxed().collect(Collectors.toCollection(ArrayList::new));   // [4200, 8100, 6500]
            show("list", list);
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
