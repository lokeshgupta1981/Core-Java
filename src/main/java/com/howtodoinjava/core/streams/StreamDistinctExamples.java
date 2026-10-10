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

/**
 * Examples for the tutorial "Java Stream distinct(): Remove Duplicates, Keep Order".
 * https://howtodoinjava.com/java8/java-stream-distinct-examples/
 */
public class StreamDistinctExamples {
    static record Subscriber(String email, String plan) {}
    static record Member(int id, String name) {
        @Override
        public boolean equals(Object o) {
            return o instanceof Member other && id == other.id;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(id);
        }
    }
    static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> tags = List.of("java", "spring", "java", "docker", "spring");
            List<String> unique = tags.stream().distinct().toList();                         // [java, spring, docker]
            show("unique", unique);
            long uniqueCount = tags.stream().distinct().count();                             // 3
            show("uniqueCount", uniqueCount);
            List<Integer> numbers = IntStream.of(3, 1, 3, 2, 1).distinct().boxed().toList();  // [3, 1, 2]
            show("numbers", numbers);
        }
        {
            Stream<String> codes = Stream.of("IN", "US", "IN");
            List<String> distinctCodes = codes.distinct().toList();   // [IN, US]
            show("distinctCodes", distinctCodes);
        }
        {
            List<String> letters = List.of("A", "B", "C", "D", "A", "B", "C");
            List<String> distinctLetters = letters.stream().distinct().toList();   // [A, B, C, D]
            show("distinctLetters", distinctLetters);
        }
        {
            int[] ports = IntStream.of(8080, 443, 8080, 80).distinct().toArray();                 // [8080, 443, 80]
            show("ports", ports);
            List<String> mixedCase = Stream.of("Java", "java", "JAVA").distinct().toList();         // [Java, java, JAVA]
            show("mixedCase", mixedCase);
            List<String> lowerCase = Stream.of("Java", "java", "JAVA").map(s -> s.toLowerCase(Locale.ROOT)).distinct().toList();   // [java]
            show("lowerCase", lowerCase);
        }
        {
            List<Subscriber> signups = List.of(new Subscriber("ana@mail.com", "free"), new Subscriber("raj@mail.com", "pro"), new Subscriber("ana@mail.com", "free"), new Subscriber("ana@mail.com", "pro"));
            int distinctSignups = signups.stream().distinct().toList().size();   // 3
            show("distinctSignups", distinctSignups);
        }
        {
            List<Member> members = List.of(new Member(3, "Alex"), new Member(2, "Brian"), new Member(2, "Brian K."), new Member(1, "Lokesh"), new Member(1, "Lokesh G."));
            List<String> distinctNames = members.stream().distinct().map(Member::name).toList();   // [Alex, Brian, Lokesh]
            show("distinctNames", distinctNames);
        }
        {
            List<Subscriber> signups = List.of(new Subscriber("ana@mail.com", "free"), new Subscriber("raj@mail.com", "pro"), new Subscriber("ana@mail.com", "pro"));
            List<String> oneMailEach = signups.stream().filter(distinctByKey(Subscriber::email)).map(Subscriber::plan).toList();   // [free, pro]
            show("oneMailEach", oneMailEach);
            List<String> emails = signups.stream().map(Subscriber::email).distinct().toList();   // [ana@mail.com, raj@mail.com]
            show("emails", emails);
        }
        {
            List<Integer> source = List.of(5, 3, 5, 1, 3);
            List<Integer> asList = source.stream().distinct().toList();                                        // [5, 3, 1]
            show("asList", asList);
            Set<Integer> asLinkedSet = source.stream().collect(Collectors.toCollection(LinkedHashSet::new));   // [5, 3, 1]
            show("asLinkedSet", asLinkedSet);
        }
        {
            List<Integer> readings = IntStream.range(0, 1_000).map(i -> i % 10).boxed().toList();
            List<Integer> ordered = readings.parallelStream().distinct().toList();       // [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
            show("ordered", ordered);
            long anyOrderCount = readings.parallelStream().unordered().distinct().count();   // 10
            show("anyOrderCount", anyOrderCount);
        }
        {
            List<String> formA = List.of("Ana@Mail.com", "raj@mail.com ");
            List<String> formB = List.of("ana@mail.com", "lee@mail.com");
            List<String> mailingList = Stream.concat(formA.stream(), formB.stream()).map(e -> e.strip().toLowerCase(Locale.ROOT)).distinct().toList();   // [ana@mail.com, raj@mail.com, lee@mail.com]
            show("mailingList", mailingList);
        }
        {
            long distinctCities = Stream.of("Pune", "Delhi", "Pune").distinct().count();   // 2
            show("distinctCities", distinctCities);
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
