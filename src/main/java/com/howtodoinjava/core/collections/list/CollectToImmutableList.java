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
 * Examples for the tutorial "Collect Stream to Immutable List: toList vs toUnmodifiableList".
 * https://howtodoinjava.com/java/collections/collect-stream-into-immutable-collection/
 */
public class CollectToImmutableList {
    static record CorsSettings(List<String> allowedOrigins) {

        CorsSettings {
            allowedOrigins = List.copyOf(allowedOrigins);
        }

        static CorsSettings fromProperty(String value) {
            return new CorsSettings(Arrays.stream(value.split(",")).map(String::strip).filter(s -> !s.isEmpty()).toList());
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> cities = Stream.of("Pune", "Oslo", "Lima").toList();                    // [Pune, Oslo, Lima]
            show("cities", cities);
            List<String> strict = Stream.of("Pune", "Oslo").collect(Collectors.toUnmodifiableList());  // [Pune, Oslo]
            show("strict", strict);
            List<String> withNull = Stream.of("Pune", null).toList();                             // [Pune, null]
            show("withNull", withNull);
            try { cities.add("Rome");  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> tags = Stream.of("java", "spring", "java").distinct().toList();   // [java, spring]
            show("tags", tags);
            String first = tags.getFirst();                                               // "java"
            show("first", first);
            boolean hasNull = tags.contains(null);                                        // false
            show("hasNull", hasNull);
            try { tags.set(0, "kotlin");  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<String> origins = Stream.of("a.com", "b.com").collect(Collectors.toUnmodifiableList());   // [a.com, b.com]
            show("origins", origins);
            Set<String> roles = Stream.of("admin", "user", "admin").collect(Collectors.toUnmodifiableSet());   // admin and user, order not defined
            show("roles", roles);
            int roleCount = roles.size();                                                                       // 2
            show("roleCount", roleCount);
            try { List<String> bad = Stream.of("a.com", null).collect(Collectors.toUnmodifiableList()); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            Map<String, Integer> lengths = Stream.of("kiwi", "fig").collect(Collectors.toUnmodifiableMap(s -> s, String::length));   // {kiwi=4, fig=3} in any order
            show("lengths", lengths);
            Integer kiwi = lengths.get("kiwi");                                                                                      // 4
            show("kiwi", kiwi);
            try { Map<Integer, String> byLength = Stream.of("kiwi", "pear", "fig").collect(Collectors.toUnmodifiableMap(String::length, s -> s)); show("byLength", byLength); } catch (Throwable _t) { System.out.println("byLength -> " + _t); }
            Map<Integer, String> merged = Stream.of("kiwi", "pear", "fig").collect(Collectors.toUnmodifiableMap(String::length, s -> s, (a, b) -> a + "," + b));
            String four = merged.get(4);                                                                                             // "kiwi,pear"
            show("four", four);
        }
        {
            List<String> editable = Stream.of("b", "a").collect(Collectors.toCollection(ArrayList::new));
            editable.add("c");
            editable.sort(null);
            List<String> sorted = editable;                                      // [a, b, c]
            show("sorted", sorted);
        }
        {
            List<String> wrapped = Stream.of("Pune", "Oslo").collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));   // [Pune, Oslo]
            show("wrapped", wrapped);
            List<String> keepsNull = Stream.of("Pune", null).collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));   // [Pune, null]
            show("keepsNull", keepsNull);
        }
        {
            List<String> source = new ArrayList<>(List.of("Pune"));
            List<String> view = Collections.unmodifiableList(source);
            List<String> copy = List.copyOf(source);
            source.add("Oslo");
            List<String> viewNow = view;                                         // [Pune, Oslo]
            show("viewNow", viewNow);
            List<String> copyNow = copy;                                         // [Pune]
            show("copyNow", copyNow);
        }
        {
            CorsSettings settings = CorsSettings.fromProperty(" https://a.com, https://b.com ,");
            List<String> origins = settings.allowedOrigins();                    // [https://a.com, https://b.com]
            show("origins", origins);
            try { settings.allowedOrigins().add("https://evil.com");  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<StringBuilder> drafts = Stream.of(new StringBuilder("draft")).toList();
            drafts.getFirst().append(" v2");
            String text = drafts.getFirst().toString();                         // "draft v2"
            show("text", text);
        }
        {
            List<String> fromToList = Stream.of("a").toList();
            boolean safe = fromToList.contains(null);                            // false
            show("safe", safe);
            List<String> fromCollector = Stream.of("a").collect(Collectors.toUnmodifiableList());
            try { boolean strict = fromCollector.contains(null); show("strict", strict); } catch (Throwable _t) { System.out.println("strict -> " + _t); }
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
