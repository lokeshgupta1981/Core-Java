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
 * Examples for the tutorial "Convert Set to List in Java (HashSet to ArrayList and Back)".
 * https://howtodoinjava.com/java/collections/arraylist/hashset-to-arraylist/
 */
public class SetToListConversion {

    public static void main(String[] args) throws Exception {
        {
            Set<String> fruits = new LinkedHashSet<>(List.of("banana", "apple", "cherry"));
            List<String> list = new ArrayList<>(fruits);                     // [banana, apple, cherry]
            show("list", list);
            List<String> readOnly = List.copyOf(fruits);                     // [banana, apple, cherry]
            show("readOnly", readOnly);
            List<String> sorted = fruits.stream().sorted().toList();         // [apple, banana, cherry]
            show("sorted", sorted);
            Set<String> unique = new LinkedHashSet<>(List.of("kiwi", "fig", "kiwi"));    // [kiwi, fig]
            show("unique", unique);
        }
        {
            Set<Integer> ids = new TreeSet<>(Set.of(30, 10, 20));
            List<Integer> idList = new ArrayList<>(ids);                     // [10, 20, 30]
            show("idList", idList);
            idList.add(40);                                                  // idList = [10, 20, 30, 40]
            int first = idList.get(0);                                       // 10
            show("first", first);
        }
        {
            Set<String> extras = new LinkedHashSet<>(List.of("nuts", "honey"));
            List<String> toppings = new ArrayList<>(List.of("cream"));
            toppings.addAll(extras);                                         // toppings = [cream, nuts, honey]
        }
        {
            List<String> input = List.of("banana", "apple", "cherry");
            List<String> fromLinked = new ArrayList<>(new LinkedHashSet<>(input));    // [banana, apple, cherry]
            show("fromLinked", fromLinked);
            List<String> fromTree = new ArrayList<>(new TreeSet<>(input));            // [apple, banana, cherry]
            show("fromTree", fromTree);
            List<String> fromHash = new ArrayList<>(new HashSet<>(input));            // all three, order not defined
            show("fromHash", fromHash);
        }
        {
            SequencedSet<String> recent = new LinkedHashSet<>(List.of("home", "cart", "checkout"));
            String firstPage = recent.getFirst();                            // "home"
            show("firstPage", firstPage);
            List<String> newestFirst = new ArrayList<>(recent.reversed());   // [checkout, cart, home]
            show("newestFirst", newestFirst);
        }
        {
            Set<String> sizes = new LinkedHashSet<>(Arrays.asList("S", null, "M"));
            List<String> viaStream = sizes.stream().toList();                 // [S, null, M]
            show("viaStream", viaStream);
            try { List<String> viaCopyOf = List.copyOf(sizes); show("viaCopyOf", viaCopyOf); } catch (Throwable _t) { System.out.println("viaCopyOf -> " + _t); }
        }
        {
            Set<String> tags = new HashSet<>(Set.of("sale", "new", "eco", "kids"));
            List<String> filterTags = tags.stream().sorted().toList();                         // [eco, kids, new, sale]
            show("filterTags", filterTags);
            List<String> byLength = new ArrayList<>(tags);
            byLength.sort(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
            String result = byLength.toString();                                               // "[eco, new, kids, sale]"
            show("result", result);
        }
        {
            List<Integer> votes = List.of(3, 1, 3, 2, 1);
            Set<Integer> hashed = new HashSet<>(votes);                       // 1, 2 and 3, order not defined
            show("hashed", hashed);
            Set<Integer> inOrder = new LinkedHashSet<>(votes);                // [3, 1, 2]
            show("inOrder", inOrder);
            Set<Integer> ascending = new TreeSet<>(votes);                    // [1, 2, 3]
            show("ascending", ascending);
            Set<Integer> frozen = Set.copyOf(votes);                          // 1, 2 and 3, unmodifiable
            show("frozen", frozen);
            int count = hashed.size();                                        // 3
            show("count", count);
        }
        {
            Set<String> fromCopy = Set.copyOf(List.of("a", "a"));               // [a]
            show("fromCopy", fromCopy);
            try { Set<String> fromOf = Set.of("a", "a"); show("fromOf", fromOf); } catch (Throwable _t) { System.out.println("fromOf -> " + _t); }
            Set<String> fromStream = Stream.of("b", "a", "b").collect(Collectors.toCollection(LinkedHashSet::new));    // [b, a]
            show("fromStream", fromStream);
        }
        {
            Set<String> colors = new TreeSet<>(Set.of("red", "blue"));
            String[] array = colors.toArray(String[]::new);                   // [blue, red]
            show("array", array);
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
