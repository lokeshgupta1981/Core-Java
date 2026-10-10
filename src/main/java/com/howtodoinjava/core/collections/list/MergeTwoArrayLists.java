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
 * Examples for the tutorial "Merge Two ArrayLists in Java, With or Without Duplicates".
 * https://howtodoinjava.com/java/collections/arraylist/merge-arraylists/
 */
public class MergeTwoArrayLists {
    static <T extends Comparable<? super T>> List<T> mergeSorted(List<T> first, List<T> second) {
        List<T> result = new ArrayList<>(first.size() + second.size());
        int i = 0;
        int j = 0;
        while (i < first.size() && j < second.size()) {
            if (first.get(i).compareTo(second.get(j)) <= 0) {
                result.add(first.get(i++));
            } else {
                result.add(second.get(j++));
            }
        }
        result.addAll(first.subList(i, first.size()));
        result.addAll(second.subList(j, second.size()));
        return result;
    }
    static record Member(String name, String role) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> backend = List.of("ann", "bob", "eve");
            List<String> frontend = List.of("eve", "joe");

            List<String> everyone = new ArrayList<>(backend);
            boolean changed = everyone.addAll(frontend);                   // true
            show("changed", changed);
            List<String> merged = everyone;                                // [ann, bob, eve, eve, joe]
            show("merged", merged);
            List<String> viaStream = Stream.concat(backend.stream(), frontend.stream()).toList();   // [ann, bob, eve, eve, joe]
            show("viaStream", viaStream);
            List<String> unique = new ArrayList<>(new LinkedHashSet<>(merged));   // [ann, bob, eve, joe]
            show("unique", unique);
        }
        {
            List<String> backend = new ArrayList<>(List.of("ann", "bob"));
            List<String> frontend = List.of("joe", "kim");
            backend.addAll(frontend);
            List<String> grown = backend;                                  // [ann, bob, joe, kim]
            show("grown", grown);
            int unchanged = frontend.size();                               // 2
            show("unchanged", unchanged);
        }
        {
            List<String> backend = List.of("ann", "bob");
            List<String> frontend = List.of("joe");
            List<String> merged = new ArrayList<>(backend.size() + frontend.size());
            merged.addAll(backend);
            merged.addAll(frontend);
            List<String> result = merged;                                  // [ann, bob, joe]
            show("result", result);
            List<String> original = backend;                               // [ann, bob]
            show("original", original);
        }
        {
            List<String> feed = new ArrayList<>(List.of("post3", "post4"));
            feed.addAll(0, List.of("post1", "post2"));
            List<String> newestFirst = feed;                               // [post1, post2, post3, post4]
            show("newestFirst", newestFirst);
        }
        {
            List<String> backend = List.of("ann", "bob");
            List<String> frontend = List.of("joe");
            List<String> fixed = Stream.concat(backend.stream(), frontend.stream()).toList();
            try { fixed.add("kim");  } catch (Throwable _t) { System.out.println("-> " + _t); }
            List<String> editable = Stream.concat(backend.stream(), frontend.stream())
                    .collect(Collectors.toCollection(ArrayList::new));
            editable.add("kim");
            List<String> result = editable;                                // [ann, bob, joe, kim]
            show("result", result);
        }
        {
            List<String> backend = List.of("ann", "bob");
            List<String> frontend = List.of("joe");
            List<String> design = List.of("liz", "ann");
            List<String> all = Stream.of(backend, frontend, design)
                    .flatMap(List::stream)
                    .toList();                                             // [ann, bob, joe, liz, ann]
            int total = all.size();                                        // 5
            show("total", total);
        }
        {
            List<String> backend = List.of("ann", "bob", "eve");
            List<String> frontend = List.of("eve", "joe", "ann");
            Set<String> members = new LinkedHashSet<>(backend);
            members.addAll(frontend);
            List<String> unique = new ArrayList<>(members);                // [ann, bob, eve, joe]
            show("unique", unique);
        }
        {
            List<String> backend = List.of("ann", "bob", "bob");
            List<String> frontend = List.of("eve", "ann");
            List<String> unique = Stream.concat(backend.stream(), frontend.stream())
                    .distinct()
                    .toList();                                             // [ann, bob, eve]
        }
        {
            List<String> backend = List.of("ann", "bob", "eve");
            List<String> frontend = List.of("eve", "joe");
            List<String> newcomers = new ArrayList<>(frontend);
            newcomers.removeAll(backend);
            List<String> merged = new ArrayList<>(backend);
            merged.addAll(newcomers);
            List<String> result = merged;                                  // [ann, bob, eve, joe]
            show("result", result);
        }
        {
            List<Integer> morning = List.of(8, 10, 12);
            List<Integer> evening = List.of(9, 12, 18, 20);
            List<Integer> slots = mergeSorted(morning, evening);           // [8, 9, 10, 12, 12, 18, 20]
            show("slots", slots);
            List<Integer> sortedAgain = Stream.concat(morning.stream(), evening.stream()).sorted().toList();   // [8, 9, 10, 12, 12, 18, 20]
            show("sortedAgain", sortedAgain);
        }
        {
            List<Member> hr = List.of(new Member("ann", "dev"), new Member("bob", "dev"), new Member("joe", "qa"));
            List<Member> project = List.of(new Member("ann", "lead"), new Member("kim", "dev"));

            Map<String, Member> byName = Stream.concat(hr.stream(), project.stream())
                    .collect(Collectors.toMap(Member::name, Function.identity(),
            (older, newer) -> newer, LinkedHashMap::new));
            List<String> roster = byName.values().stream()
                    .map(m -> m.name() + "=" + m.role())
                    .toList();                                             // [ann=lead, bob=dev, joe=qa, kim=dev]
        }
        {
            List<Integer> whole = List.of(1, 2);
            List<Double> halves = List.of(0.5);
            List<Number> numbers = new ArrayList<>(whole);
            numbers.addAll(halves);
            List<Number> mixed = numbers;                                  // [1, 2, 0.5]
            show("mixed", mixed);
        }
        {
            List<String> home = List.of("h1", "h2", "h3");
            List<String> away = List.of("a1", "a2");
            List<String> schedule = IntStream.range(0, Math.max(home.size(), away.size()))
                    .boxed()
                    .flatMap(i -> Stream.of(i < home.size() ? home.get(i) : null, i < away.size() ? away.get(i) : null))
                    .filter(Objects::nonNull)
                    .toList();                                             // [h1, a1, h2, a2, h3]
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
