package com.howtodoinjava.core.streams;

import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.time.format.*;

/**
 * Examples for the tutorial "Java Stream: Get Object With Max Date From a List".
 * https://howtodoinjava.com/java8/stream-get-object-with-max-date/
 */
public class ObjectWithMaxDate {
    static record Release(String product, String version, LocalDate date) {}
    public static void main(String[] args) throws Exception {
        {
            List<Release> releases = List.of(
            new Release("app", "1.0", LocalDate.of(2026, 1, 15)),
            new Release("app", "1.2", LocalDate.of(2026, 6, 3)),
            new Release("api", "2.0", LocalDate.of(2026, 4, 10)),
            new Release("app", "1.1", LocalDate.of(2026, 3, 20)));
            Optional<Release> latest = releases.stream().max(Comparator.comparing(Release::date));                  // Optional[Release[product=app, version=1.2, date=2026-06-03]]
            show("latest", latest);
            String latestVersion = latest.map(Release::version).orElse("none");                                    // "1.2"
            show("latestVersion", latestVersion);
            String earliestVersion = releases.stream().min(Comparator.comparing(Release::date)).map(Release::version).orElse("none");   // "1.0"
            show("earliestVersion", earliestVersion);
        }
        {
            List<Release> releases = List.of(
            new Release("app", "1.0", LocalDate.of(2026, 1, 15)),
            new Release("app", "1.2", LocalDate.of(2026, 6, 3)),
            new Release("api", "2.0", LocalDate.of(2026, 4, 10)),
            new Release("app", "1.1", LocalDate.of(2026, 3, 20)));
            Comparator<Release> byDate = Comparator.comparing(Release::date);
            String newest = releases.stream().max(byDate).map(Release::version).orElse("none");              // "1.2"
            show("newest", newest);
            String oldest = releases.stream().min(byDate).map(Release::version).orElse("none");              // "1.0"
            show("oldest", oldest);
            String oldestReversed = releases.stream().max(byDate.reversed()).map(Release::version).orElse("none");   // "1.0"
            show("oldestReversed", oldestReversed);
        }
        {
            List<Release> none = List.of();
            Optional<Release> nothing = none.stream().max(Comparator.comparing(Release::date));          // Optional.empty
            show("nothing", nothing);
            String label = nothing.map(Release::version).orElse("no releases yet");                      // "no releases yet"
            show("label", label);
            try { Release mustExist = nothing.orElseThrow(); show("mustExist", mustExist); } catch (Throwable _t) { System.out.println("mustExist -> " + _t); }
        }
        {
            List<Release> releases = List.of(
            new Release("app", "1.0", LocalDate.of(2026, 1, 15)),
            new Release("app", "1.2", LocalDate.of(2026, 6, 3)),
            new Release("api", "2.0", LocalDate.of(2026, 4, 10)),
            new Release("app", "1.1", LocalDate.of(2026, 3, 20)));
            Optional<LocalDate> lastDate = releases.stream().map(Release::date).max(Comparator.naturalOrder());     // Optional[2026-06-03]
            show("lastDate", lastDate);
            Optional<LocalDate> firstDate = releases.stream().map(Release::date).min(Comparator.naturalOrder());    // Optional[2026-01-15]
            show("firstDate", firstDate);
        }
        {
            List<Release> withPlanned = List.of(
            new Release("app", "1.3", null),
            new Release("app", "1.2", LocalDate.of(2026, 6, 3)));
            try { Optional<Release> crash = withPlanned.stream().max(Comparator.comparing(Release::date)); show("crash", crash); } catch (Throwable _t) { System.out.println("crash -> " + _t); }
            String nullsFirst = withPlanned.stream().max(Comparator.comparing(Release::date, Comparator.nullsFirst(Comparator.naturalOrder()))).map(Release::version).orElse("none");   // "1.2"
            show("nullsFirst", nullsFirst);
            String filtered = withPlanned.stream().filter(r -> r.date() != null).max(Comparator.comparing(Release::date)).map(Release::version).orElse("none");   // "1.2"
            show("filtered", filtered);
        }
        {
            List<Release> releases = List.of(
            new Release("app", "1.0", LocalDate.of(2026, 1, 15)),
            new Release("app", "1.2", LocalDate.of(2026, 6, 3)),
            new Release("api", "2.0", LocalDate.of(2026, 4, 10)),
            new Release("app", "1.1", LocalDate.of(2026, 3, 20)),
            new Release("api", "2.1", LocalDate.of(2026, 6, 3)));
            Comparator<Release> byDateThenVersion = Comparator.comparing(Release::date).thenComparing(Release::version);
            String winner = releases.stream().max(byDateThenVersion).map(Release::version).orElse("none");    // "2.1"
            show("winner", winner);
            LocalDate maxDate = releases.stream().map(Release::date).max(Comparator.naturalOrder()).orElseThrow();   // 2026-06-03
            show("maxDate", maxDate);
            List<String> sameDay = releases.stream().filter(r -> r.date().equals(maxDate)).map(Release::version).toList();   // [1.2, 2.1]
            show("sameDay", sameDay);
        }
        {
            List<Release> releases = List.of(
            new Release("app", "1.0", LocalDate.of(2026, 1, 15)),
            new Release("app", "1.2", LocalDate.of(2026, 6, 3)),
            new Release("api", "2.0", LocalDate.of(2026, 4, 10)),
            new Release("app", "1.1", LocalDate.of(2026, 3, 20)));
            Map<String, Optional<Release>> latestPerProduct = releases.stream()
                    .collect(Collectors.groupingBy(Release::product, TreeMap::new, Collectors.maxBy(Comparator.comparing(Release::date))));
            String appViaGrouping = latestPerProduct.get("app").map(Release::version).orElse("none");   // "1.2"
            show("appViaGrouping", appViaGrouping);
            Map<String, Release> latestByProduct = releases.stream()
                    .collect(Collectors.toMap(Release::product, r -> r, BinaryOperator.maxBy(Comparator.comparing(Release::date)), TreeMap::new));
            String appLatest = latestByProduct.get("app").version();                         // "1.2"
            show("appLatest", appLatest);
            String apiLatest = latestByProduct.get("api").version();                         // "2.0"
            show("apiLatest", apiLatest);
            Set<String> products = latestByProduct.keySet();                                 // [api, app]
            show("products", products);
        }
        {
            List<Release> releases = List.of(
            new Release("app", "1.0", LocalDate.of(2026, 1, 15)),
            new Release("app", "1.2", LocalDate.of(2026, 6, 3)),
            new Release("api", "2.0", LocalDate.of(2026, 4, 10)),
            new Release("app", "1.1", LocalDate.of(2026, 3, 20)));
            Release viaCollections = Collections.max(releases, Comparator.comparing(Release::date));    // Release[product=app, version=1.2, date=2026-06-03]
            show("viaCollections", viaCollections);
            try { Release fromEmpty = Collections.max(new ArrayList<Release>(), Comparator.comparing(Release::date)); show("fromEmpty", fromEmpty); } catch (Throwable _t) { System.out.println("fromEmpty -> " + _t); }
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
