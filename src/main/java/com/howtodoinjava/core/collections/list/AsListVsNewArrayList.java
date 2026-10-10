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
 * Examples for the tutorial "Arrays.asList() vs new ArrayList() in Java: Key Differences".
 * https://howtodoinjava.com/java/collections/arraylist/arrays-aslist-vs-new-arraylist/
 */
public class AsListVsNewArrayList {

    public static void main(String[] args) throws Exception {
        {
            String[] colors = {"red", "green", "blue"};
            List<String> view = Arrays.asList(colors);                     // [red, green, blue]
            show("view", view);
            List<String> copy = new ArrayList<>(Arrays.asList(colors));    // [red, green, blue]
            show("copy", copy);
            view.set(0, "pink");                                           // colors = [pink, green, blue]
            boolean added = copy.add("black");                             // true
            show("added", added);
            try { boolean failed = view.add("black"); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
        }
        {
            String[] sizes = {"S", "M", "L"};
            List<String> fixed = Arrays.asList(sizes);                    // [S, M, L]
            show("fixed", fixed);
            ArrayList<String> growable = new ArrayList<>(Arrays.asList(sizes));    // [S, M, L]
            show("growable", growable);
        }
        {
            String[] sizes = {"S", "M", "L"};
            String viewType = Arrays.asList(sizes).getClass().getName();                    // "java.util.Arrays$ArrayList"
            show("viewType", viewType);
            String copyType = new ArrayList<>(Arrays.asList(sizes)).getClass().getName();   // "java.util.ArrayList"
            show("copyType", copyType);
        }
        {
            List<String> view = Arrays.asList("S", "M");
            try { ArrayList<String> cast = (ArrayList<String>) view; show("cast", cast); } catch (Throwable _t) { System.out.println("cast -> " + _t); }
        }
        {
            String[] days = {"tue", "mon", "wed"};
            List<String> view = Arrays.asList(days);
            view.sort(null);                                   // view = [mon, tue, wed]
            view.replaceAll(String::toUpperCase);              // view = [MON, TUE, WED]
            try { boolean removed = view.remove("MON"); show("removed", removed); } catch (Throwable _t) { System.out.println("removed -> " + _t); }
        }
        {
            String[] days = {"mon", "tue"};
            List<String> copy = new ArrayList<>(Arrays.asList(days));
            copy.add("wed");                                   // copy = [mon, tue, wed]
            copy.remove("mon");                                // copy = [tue, wed]
        }
        {
            String[] fruits = {"apple", "banana"};
            List<String> view = Arrays.asList(fruits);
            List<String> copy = new ArrayList<>(Arrays.asList(fruits));
            fruits[1] = "cherry";                              // view = [apple, cherry]
            copy.set(0, "mango");                              // fruits = [apple, cherry]
            String fromCopy = copy.toString();                 // "[mango, banana]"
            show("fromCopy", fromCopy);
        }
        {
            StringBuilder[] notes = {new StringBuilder("buy")};
            List<StringBuilder> copy = new ArrayList<>(Arrays.asList(notes));
            notes[0].append(" milk");
            String shared = copy.get(0).toString();            // "buy milk"
            show("shared", shared);
        }
        {
            String[] tags = {"java", "lists"};
            List<String> immutable = List.of(tags);
            tags[0] = "kotlin";                                // immutable = [java, lists]
            List<String> withNull = Arrays.asList("a", null);  // [a, null]
            show("withNull", withNull);
            try { List<String> noNull = List.of("a", null); show("noNull", noNull); } catch (Throwable _t) { System.out.println("noNull -> " + _t); }
        }
        {
            int[] scores = {90, 75, 60};
            List<int[]> wrong = Arrays.asList(scores);
            int size = wrong.size();                                          // 1
            show("size", size);
            List<Integer> boxed = Arrays.stream(scores).boxed().toList();     // [90, 75, 60]
            show("boxed", boxed);
            List<Integer> editable = new ArrayList<>(boxed);                  // [90, 75, 60]
            show("editable", editable);
        }
        {
            String[] cities = {"Paris", "Rome"};
            ArrayList<String> list = new ArrayList<>(Arrays.asList(cities));    // [Paris, Rome]
            show("list", list);
            list.add("Oslo");                                                    // list = [Paris, Rome, Oslo]
        }
        {
            String[] cities = {"Paris", "Rome", "Oslo"};
            List<String> target = new ArrayList<>(List.of("Lima"));
            Collections.addAll(target, cities);                      // target = [Lima, Paris, Rome, Oslo]
            ArrayList<String> shortNames = Arrays.stream(cities)
                    .filter(c -> c.length() == 4)
                    .collect(Collectors.toCollection(ArrayList::new));
            String result = shortNames.toString();                   // "[Rome, Oslo]"
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
