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
 * Examples for the tutorial "Convert String Array to int Array in Java (Safe Parsing)".
 * https://howtodoinjava.com/java/array/convert-string-array-to-integer-array/
 */
public class ConvertStringArrayToIntArray {
    static OptionalInt tryParseInt(String text) {
        if (text == null) {
            return OptionalInt.empty();
        }
        try {
            return OptionalInt.of(Integer.parseInt(text.strip()));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String[] quantities = {"3", "12", "7"};
            int[] ints = Arrays.stream(quantities).mapToInt(Integer::parseInt).toArray();               // [3, 12, 7]
            show("ints", ints);
            Integer[] boxed = Arrays.stream(quantities).map(Integer::valueOf).toArray(Integer[]::new);   // [3, 12, 7]
            show("boxed", boxed);
        }
        {
            String[] quantities = {"3", "12", "7"};
            int[] result = new int[quantities.length];
            for (int i = 0; i < quantities.length; i++) {
                result[i] = Integer.parseInt(quantities[i]);
            }
            String shown = Arrays.toString(result);   // "[3, 12, 7]"
            show("shown", shown);
        }
        {
            String[] quantities = {"3", "12", "7"};
            Integer[] boxed = Arrays.stream(quantities).map(Integer::valueOf).toArray(Integer[]::new);   // [3, 12, 7]
            show("boxed", boxed);
            List<Integer> list = Arrays.stream(quantities).map(Integer::valueOf).toList();               // [3, 12, 7]
            show("list", list);
        }
        {
            int plus = Integer.parseInt("+5");           // 5
            show("plus", plus);
            try { int padded = Integer.parseInt(" 5"); show("padded", padded); } catch (Throwable _t) { System.out.println("padded -> " + _t); }
            try { int empty = Integer.parseInt(""); show("empty", empty); } catch (Throwable _t) { System.out.println("empty -> " + _t); }
            try { int missing = Integer.parseInt(null); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
            try { int big = Integer.parseInt("3000000000"); show("big", big); } catch (Throwable _t) { System.out.println("big -> " + _t); }
        }
        {
            String[] cells = {"3", " 12", "", "seven", "7"};
            int[] valid = Arrays.stream(cells).map(c -> tryParseInt(c)).flatMapToInt(OptionalInt::stream).toArray();   // [3, 12, 7]
            show("valid", valid);
            int[] withDefault = Arrays.stream(cells).mapToInt(c -> tryParseInt(c).orElse(0)).toArray();             // [3, 12, 0, 0, 7]
            show("withDefault", withDefault);
        }
        {
            String[] cells = {"3", " 12", "", "seven", "7"};
            List<String> rejected = Arrays.stream(cells).filter(c -> tryParseInt(c).isEmpty()).toList();   // [, seven]
            show("rejected", rejected);
        }
        {
            String param = "3, 12,7";
            int[] ids = Arrays.stream(param.split("\\s*,\\s*")).mapToInt(Integer::parseInt).toArray();   // [3, 12, 7]
            show("ids", ids);
            int[] ids2 = Pattern.compile(",").splitAsStream(param).map(String::strip).mapToInt(Integer::parseInt).toArray();   // [3, 12, 7]
            show("ids2", ids2);
        }
        {
            String[] fileSizes = {"3000000000", "512"};
            long[] sizes = Arrays.stream(fileSizes).mapToLong(Long::parseLong).toArray();   // [3000000000, 512]
            show("sizes", sizes);
            String[] prices = {"9.99", "4.5"};
            double[] amounts = Arrays.stream(prices).mapToDouble(Double::parseDouble).toArray();   // [9.99, 4.5]
            show("amounts", amounts);
        }
        {
            String withComma = "1,000";
            int thousand = Integer.parseInt(withComma.replace(",", ""));   // 1000
            show("thousand", thousand);
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
