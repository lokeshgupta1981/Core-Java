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
 * Examples for the tutorial "Convert List to Array in Java and an Array Back to a List".
 * https://howtodoinjava.com/java/array/convert-between-list-and-array/
 */
public class ConvertBetweenArrayAndList {

    public static void main(String[] args) throws Exception {
        {
            List<String> names = List.of("ana", "li", "raj");
            String[] array = names.toArray(String[]::new);               // [ana, li, raj]
            show("array", array);
            List<String> back = new ArrayList<>(Arrays.asList(array));   // [ana, li, raj]
            show("back", back);
            boolean added = back.add("tom");                             // true
            show("added", added);
        }
        {
            List<String> cities = new ArrayList<>(List.of("Pune", "Oslo"));
            Object[] objects = cities.toArray();                       // [Pune, Oslo]
            show("objects", objects);
            String[] typed = cities.toArray(String[]::new);            // [Pune, Oslo]
            show("typed", typed);
            String[] typed2 = cities.toArray(new String[0]);           // [Pune, Oslo]
            show("typed2", typed2);
            try { String[] broken = (String[]) cities.toArray(); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            List<String> trio = List.of("ana", "li", "raj");
            String[] big = {"x", "x", "x", "x", "x"};
            String[] filled = trio.toArray(big);           // [ana, li, raj, null, x]
            show("filled", filled);
            boolean sameArray = filled == big;             // true
            show("sameArray", sameArray);
            String[] fresh = trio.toArray(new String[0]);  // [ana, li, raj]
            show("fresh", fresh);
        }
        {
            List<Integer> seats = List.of(12, 7, 30);
            int[] seatArray = seats.stream().mapToInt(Integer::intValue).toArray();   // [12, 7, 30]
            show("seatArray", seatArray);
            List<Long> ids = List.of(5L, 9L);
            long[] idArray = ids.stream().mapToLong(Long::longValue).toArray();      // [5, 9]
            show("idArray", idArray);
        }
        {
            List<Integer> withGap = Arrays.asList(4, null, 8);
            try { int[] crash = withGap.stream().mapToInt(Integer::intValue).toArray(); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
            int[] clean = withGap.stream().filter(Objects::nonNull).mapToInt(Integer::intValue).toArray();   // [4, 8]
            show("clean", clean);
        }
        {
            String[] colors = {"red", "green"};
            List<String> view = Arrays.asList(colors);
            String old = view.set(0, "blue");          // "red"
            show("old", old);
            String first = colors[0];                  // "blue", the array changed
            show("first", first);
            try { view.add("pink");  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            String[] tags = {"java", null};
            try { List<String> rejected = List.of(tags); show("rejected", rejected); } catch (Throwable _t) { System.out.println("rejected -> " + _t); }
            List<String> growable = new ArrayList<>(Arrays.asList(tags));
            boolean ok = growable.add("arrays");                // true
            show("ok", ok);
            String text = growable.toString();                  // "[java, null, arrays]"
            show("text", text);
        }
        {
            int[] scores = {70, 85, 90};
            List<int[]> wrong = Arrays.asList(scores);
            int wrongSize = wrong.size();                                  // 1
            show("wrongSize", wrongSize);
            List<Integer> right = Arrays.stream(scores).boxed().toList();  // [70, 85, 90]
            show("right", right);
            List<Integer> mutable = IntStream.of(scores).boxed().collect(Collectors.toCollection(ArrayList::new));   // [70, 85, 90]
            show("mutable", mutable);
        }
        {
            List<String> files = List.of("a.txt", "b.png", "c.txt", "d.txt");
            String[] texts = files.stream().filter(f -> f.endsWith(".txt")).toArray(String[]::new);           // [a.txt, c.txt, d.txt]
            show("texts", texts);
            String[] inParallel = files.parallelStream().filter(f -> f.endsWith(".txt")).toArray(String[]::new);   // [a.txt, c.txt, d.txt]
            show("inParallel", inParallel);
        }
        {
            List<String> parts = new ArrayList<>(List.of("backups", "2026", "10"));
            parts.add("db.sql");
            Path target = Path.of(parts.getFirst(), parts.subList(1, parts.size()).toArray(String[]::new));
            String pathText = target.toString();   // "backups/2026/10/db.sql" on Linux and macOS
            show("pathText", pathText);
        }
        {
            List<int[]> rows = List.of(new int[]{1, 2}, new int[]{3, 4});
            int[][] grid = rows.toArray(int[][]::new);   // [[1, 2], [3, 4]]
            show("grid", grid);
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
