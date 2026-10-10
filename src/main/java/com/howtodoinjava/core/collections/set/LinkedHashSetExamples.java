package com.howtodoinjava.core.collections.set;

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
 * Examples for the tutorial "Java LinkedHashSet: Insertion Order and Java 21 SequencedSet".
 * https://howtodoinjava.com/java/collections/java-linkedhashset/
 */
public class LinkedHashSetExamples {
    static void recordView(LinkedHashSet<String> recent, String product, int limit) {
        recent.addFirst(product);
        if (recent.size() > limit) {
            recent.removeLast();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            LinkedHashSet<String> songs = new LinkedHashSet<>(List.of("intro", "verse", "chorus"));
            boolean added = songs.add("verse");                    // false, keeps its position
            show("added", added);
            songs.add("outro");
            String first = songs.getFirst();                       // "intro"
            show("first", first);
            String last = songs.getLast();                         // "outro"
            show("last", last);
            songs.addFirst("chorus");                              // moves chorus to the front
            Set<String> order = songs;                             // [chorus, intro, verse, outro]
            show("order", order);
            SequencedSet<String> backwards = songs.reversed();     // [outro, verse, intro, chorus]
            show("backwards", backwards);
        }
        {
            LinkedHashSet<Integer> ids = new LinkedHashSet<>(List.of(3, 1, 2));
            boolean again = ids.add(1);                            // false
            show("again", again);
            Set<Integer> same = ids;                               // [3, 1, 2]
            show("same", same);
            ids.remove(3);
            ids.add(3);
            Set<Integer> moved = ids;                              // [1, 2, 3]
            show("moved", moved);
        }
        {
            List<String> emails = List.of("ana@x.io", "raj@x.io", "ana@x.io", "li@x.io");
            List<String> unique = new ArrayList<>(new LinkedHashSet<>(emails));  // [ana@x.io, raj@x.io, li@x.io]
            show("unique", unique);
            List<String> viaStream = emails.stream().distinct().toList();        // [ana@x.io, raj@x.io, li@x.io]
            show("viaStream", viaStream);
            Set<String> asSet = emails.stream()
                    .collect(Collectors.toCollection(LinkedHashSet::new));       // [ana@x.io, raj@x.io, li@x.io]
        }
        {
            LinkedHashSet<String> steps = new LinkedHashSet<>(List.of("build", "test", "deploy"));
            String oldest = steps.getFirst();                      // "build"
            show("oldest", oldest);
            steps.addLast("build");                                // moves build to the end
            Set<String> afterMove = steps;                         // [test, deploy, build]
            show("afterMove", afterMove);
            String removed = steps.removeFirst();                  // "test"
            show("removed", removed);
            SequencedSet<String> view = steps.reversed();
            view.addFirst("notify");                               // adds at the end of steps
            Set<String> result = steps;                            // [deploy, build, notify]
            show("result", result);
            try { String none = new LinkedHashSet<String>().getFirst(); show("none", none); } catch (Throwable _t) { System.out.println("none -> " + _t); }
        }
        {
            LinkedHashSet<String> recent = new LinkedHashSet<>();
            for (String p : List.of("phone", "case", "charger", "phone", "cable")) {
                recordView(recent, p, 3);
            }
            Set<String> shown = recent;                            // [cable, phone, charger]
            show("shown", shown);
        }
        {
            LinkedHashSet<String> empty = new LinkedHashSet<>();                       // []
            show("empty", empty);
            LinkedHashSet<String> buckets = new LinkedHashSet<>(64);                   // 64 buckets
            show("buckets", buckets);
            LinkedHashSet<String> tuned = new LinkedHashSet<>(64, 0.75f);              // 64 buckets, load factor 0.75
            show("tuned", tuned);
            LinkedHashSet<String> copied = new LinkedHashSet<>(List.of("b", "a", "b")); // [b, a]
            show("copied", copied);
            LinkedHashSet<String> sized = LinkedHashSet.newLinkedHashSet(500);         // room for 500 elements
            show("sized", sized);
        }
        {
            LinkedHashSet<String> stages = new LinkedHashSet<>(List.of("plan", "code", "ship"));
            String second = stages.stream().skip(1).findFirst().orElseThrow(); // "code"
            show("second", second);
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
