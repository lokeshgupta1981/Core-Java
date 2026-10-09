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
 * Examples for the tutorial "ArrayStoreException in Java: Causes and How to Fix It".
 * https://howtodoinjava.com/java/array/solving-arraystoreexception/
 */
public class ArrayStoreExceptionExample {
    static void fillBlanks(Object[] slots) {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == null) {
                slots[i] = 0;
            }
        }
    }
    static boolean tryStore(Object[] array, int index, Object value) {
        Class<?> elementType = array.getClass().getComponentType();
        if (value != null && !elementType.isInstance(value)) {
            return false;
        }
        array[index] = value;
        return true;
    }
    public static void main(String[] args) throws Exception {
        {
            Object[] labels = new String[2];      // compiles, arrays are covariant
            show("labels", labels);
            labels[0] = "draft";                  // fine, a String
            try { labels[1] = 42;  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Integer[] ids = {1, 2};
            Number[] numbers = ids;               // legal, Integer[] is a Number[]
            show("numbers", numbers);
            numbers[0] = 7;                       // fine, an Integer
            try { numbers[1] = 2.5;  } catch (Throwable _t) { System.out.println("-> " + _t); }
            Object[] names = new String[1];
            names[0] = null;                      // fine, null fits any reference type
        }
        {
            Integer[] counts = {5, null};
            fillBlanks(counts);
            Integer[] filled = counts;            // [5, 0]
            show("filled", filled);
            String[] fields = {"name", null};
            try { fillBlanks(fields);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            int[] raw = {1, 2};
            long[] wide = new long[2];
            try { System.arraycopy(raw, 0, wide, 0, 2);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            Object[] mixed = {"a", "b", 3, "d"};
            String[] dest = new String[4];
            try { System.arraycopy(mixed, 0, dest, 0, 4);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            String[] partial = dest;                       // [a, b, null, null]
            show("partial", partial);
        }
        {
            List<Object> row = new ArrayList<>(List.of("Lokesh", 37));
            try { String[] cells = row.toArray(new String[0]); show("cells", cells); } catch (Throwable _t) { System.out.println("cells -> " + _t); }
            try { String[] values = Stream.of((Object) "a", 2).toArray(String[]::new); show("values", values); } catch (Throwable _t) { System.out.println("values -> " + _t); }
        }
        {
            List<Object> row = new ArrayList<>(List.of("Lokesh", 37));
            String[] cells = row.stream().map(String::valueOf).toArray(String[]::new);   // [Lokesh, 37]
            show("cells", cells);
        }
        {
            Object[] input = {"a", 1};
            try { Object[] asStrings = Arrays.copyOf(input, 2, String[].class); show("asStrings", asStrings); } catch (Throwable _t) { System.out.println("asStrings -> " + _t); }
            Object[] ok = Arrays.copyOf(new Object[] {"a", "b"}, 2, String[].class);
            String type = ok.getClass().getSimpleName();                       // "String[]"
            show("type", type);
        }
        {
            Number[] amounts = new Integer[2];
            boolean stored = tryStore(amounts, 0, 10);       // true
            show("stored", stored);
            boolean rejected = tryStore(amounts, 1, 9.99);   // false, a Double does not fit
            show("rejected", rejected);
            Number[] result = amounts;                       // [10, null]
            show("result", result);
        }
        {
            Object value = Integer.valueOf(1);
            try { String text = (String) value; show("text", text); } catch (Throwable _t) { System.out.println("text -> " + _t); }
            int[] two = new int[2];
            try { int third = two[2]; show("third", third); } catch (Throwable _t) { System.out.println("third -> " + _t); }
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
