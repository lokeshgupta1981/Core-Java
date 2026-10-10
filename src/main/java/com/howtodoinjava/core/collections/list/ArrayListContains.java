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
 * Examples for the tutorial "Check if Element Exists in ArrayList: contains() and indexOf()".
 * https://howtodoinjava.com/java/collections/arraylist/arraylist-contains/
 */
public class ArrayListContains {
    static class Author {
        String name;
        Author(String name) { this.name = name; }
    }
    static record Tag(String name, int posts) {}
    static boolean addTag(List<String> tags, String input) {
        String tag = input.strip().toLowerCase();
        if (tag.isEmpty() || tags.contains(tag)) {
            return false;
        }
        return tags.add(tag);
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> tags = new ArrayList<>(List.of("java", "spring", "docker", "java"));
            boolean hasSpring = tags.contains("spring");                              // true
            show("hasSpring", hasSpring);
            boolean hasKotlin = tags.contains("kotlin");                              // false
            show("hasKotlin", hasKotlin);
            int first = tags.indexOf("java");                                         // 0
            show("first", first);
            int last = tags.lastIndexOf("java");                                      // 3
            show("last", last);
            int missing = tags.indexOf("kotlin");                                     // -1
            show("missing", missing);
            boolean both = tags.containsAll(List.of("java", "docker"));               // true
            show("both", both);
            boolean ignoreCase = tags.stream().anyMatch("Spring"::equalsIgnoreCase);  // true
            show("ignoreCase", ignoreCase);
        }
        {
            List<String> tags = new ArrayList<>(List.of("java", "spring", "docker"));
            boolean found = tags.contains("docker");      // true
            show("found", found);
            boolean upper = tags.contains("Docker");      // false, equals() is case-sensitive
            show("upper", upper);

            List<String> drafts = new ArrayList<>(Arrays.asList("java", null));
            boolean hasNull = drafts.contains(null);      // true
            show("hasNull", hasNull);
        }
        {
            List<String> tags = new ArrayList<>(List.of("java", "spring", "java", "docker"));
            int index = tags.indexOf("java");             // 0, the first of two matches
            show("index", index);
            int none = tags.indexOf("kotlin");            // -1
            show("none", none);
        }
        {
            List<String> tags = new ArrayList<>(List.of("java", "spring", "docker"));
            int pos = tags.indexOf("spring");                               // 1
            show("pos", pos);
            String old = pos >= 0 ? tags.set(pos, "spring-boot") : null;    // "spring", tags = [java, spring-boot, docker]
            show("old", old);
            try { String unsafe = tags.get(tags.indexOf("kotlin")); show("unsafe", unsafe); } catch (Throwable _t) { System.out.println("unsafe -> " + _t); }
        }
        {
            List<String> tags = new ArrayList<>(List.of("java", "spring", "java", "docker", "java"));
            int lastJava = tags.lastIndexOf("java");      // 4
            show("lastJava", lastJava);
            int onlyOne = tags.lastIndexOf("docker");     // 3, same as indexOf("docker")
            show("onlyOne", onlyOne);
            int absent = tags.lastIndexOf("kotlin");      // -1
            show("absent", absent);
        }
        {
            List<String> tags = new ArrayList<>(List.of("java", "spring", "java"));
            boolean javaTwice = tags.indexOf("java") != tags.lastIndexOf("java");       // true
            show("javaTwice", javaTwice);
            boolean springTwice = tags.indexOf("spring") != tags.lastIndexOf("spring"); // false
            show("springTwice", springTwice);
        }
        {
            List<Author> authors = new ArrayList<>(List.of(new Author("Lokesh")));
            boolean copyFound = authors.contains(new Author("Lokesh"));   // false, no equals() override
            show("copyFound", copyFound);
            boolean sameFound = authors.contains(authors.get(0));         // true, same reference
            show("sameFound", sameFound);
        }
        {
            List<Tag> tags = new ArrayList<>(List.of(new Tag("java", 120), new Tag("spring", 80)));
            boolean match = tags.contains(new Tag("java", 120));          // true
            show("match", match);
            boolean otherCount = tags.contains(new Tag("java", 99));      // false, posts differs
            show("otherCount", otherCount);
        }
        {
            List<Tag> tags = List.of(new Tag("java", 120), new Tag("spring", 80));
            boolean popular = tags.stream().anyMatch(t -> t.posts() > 100);           // true
            show("popular", popular);
            boolean hasJava = tags.stream().anyMatch(t -> t.name().equals("java"));   // true
            show("hasJava", hasJava);

            List<String> names = List.of("java", "spring");
            boolean caseless = names.stream().anyMatch("JAVA"::equalsIgnoreCase);     // true
            show("caseless", caseless);
        }
        {
            List<Tag> tags = List.of(new Tag("java", 120), new Tag("spring", 80), new Tag("docker", 40));
            int firstSmall = IntStream.range(0, tags.size()).filter(i -> tags.get(i).posts() < 100).findFirst().orElse(-1);   // 1
            show("firstSmall", firstSmall);
        }
        {
            List<String> tags = new ArrayList<>(List.of("java", "spring", "docker"));
            boolean all = tags.containsAll(List.of("java", "spring"));                // true
            show("all", all);
            boolean notAll = tags.containsAll(List.of("java", "kotlin"));             // false
            show("notAll", notAll);
            boolean any = !Collections.disjoint(tags, List.of("kotlin", "docker"));   // true
            show("any", any);
        }
        {
            List<String> tags = List.of("java", "spring", "java");
            int javaCount = Collections.frequency(tags, "java");      // 2
            show("javaCount", javaCount);
            int kotlinCount = Collections.frequency(tags, "kotlin");  // 0
            show("kotlinCount", kotlinCount);
        }
        {
            List<String> blocked = List.of("spam", "ads", "casino");
            Set<String> blockedSet = new HashSet<>(blocked);
            boolean isBlocked = blockedSet.contains("casino");        // true
            show("isBlocked", isBlocked);
        }
        {
            List<String> sorted = new ArrayList<>(List.of("docker", "java", "kotlin", "spring"));
            int at = Collections.binarySearch(sorted, "kotlin");      // 2
            show("at", at);
            int gone = Collections.binarySearch(sorted, "rust");      // -4, negative means missing
            show("gone", gone);
        }
        {
            List<String> tags = new ArrayList<>(List.of("java", "spring"));
            boolean added = addTag(tags, " Docker ");     // true, tags = [java, spring, docker]
            show("added", added);
            boolean dupe = addTag(tags, "JAVA");          // false, tags = [java, spring, docker]
            show("dupe", dupe);
            boolean blank = addTag(tags, "   ");          // false
            show("blank", blank);
        }
        {
            List<String> fixed = List.of("java", "spring");
            try { boolean nullCheck = fixed.contains(null); show("nullCheck", nullCheck); } catch (Throwable _t) { System.out.println("nullCheck -> " + _t); }
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
