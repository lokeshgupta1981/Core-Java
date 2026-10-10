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
 * Examples for the tutorial "Java HashSet: How It Works, Examples and Performance".
 * https://howtodoinjava.com/java/collections/java-hashset/
 */
public class HashSetExamples {
    static record Message(long id, String body) {}
    static List<String> handleOnce(List<Message> batch, Set<Long> seen) {
        List<String> handled = new ArrayList<>();
        for (Message m : batch) {
            if (seen.add(m.id())) {
                handled.add(m.body());
            }
        }
        return handled;
    }
    static class TagClass {
        final String name;
        TagClass(String name) { this.name = name; }
    }

    static record Tag(String name) {}
    public static void main(String[] args) throws Exception {
        {
            Set<String> tags = new HashSet<>(List.of("java", "spring"));
            boolean added = tags.add("docker");                  // true
            show("added", added);
            boolean duplicate = tags.add("java");                // false, already present
            show("duplicate", duplicate);
            boolean found = tags.contains("spring");             // true
            show("found", found);
            boolean removed = tags.remove("docker");             // true
            show("removed", removed);
            boolean nullAdded = tags.add(null);                  // true, one null is allowed
            show("nullAdded", nullAdded);
            int size = tags.size();                              // 3
            show("size", size);
        }
        {
            Set<String> empty = new HashSet<>();                              // []
            show("empty", empty);
            Set<String> fromList = new HashSet<>(List.of("a", "b", "a"));     // 2 elements, duplicate dropped
            show("fromList", fromList);
            Set<String> sized = HashSet.newHashSet(1000);                     // room for 1000 elements
            show("sized", sized);
            Set<String> fromStream = Stream.of("x", "y", "x")
                    .collect(Collectors.toCollection(HashSet::new));          // 2 elements
        }
        {
            Set<String> fixed = Set.of("red", "green");
            boolean ok = fixed.contains("red");                  // true
            show("ok", ok);
            try { fixed.add("blue");  } catch (Throwable _t) { System.out.println("-> " + _t); }
            try { Set<String> twice = Set.of("red", "red"); show("twice", twice); } catch (Throwable _t) { System.out.println("twice -> " + _t); }
        }
        {
            Set<Integer> processed = new HashSet<>();
            boolean first = processed.add(101);                  // true, new ID
            show("first", first);
            boolean again = processed.add(101);                  // false, seen before
            show("again", again);
            boolean known = processed.contains(101);             // true
            show("known", known);
            processed.addAll(List.of(102, 103, 104));
            boolean anyRemoved = processed.removeIf(id -> id % 2 == 0); // true, removes 102 and 104
            show("anyRemoved", anyRemoved);
            int left = processed.size();                         // 2
            show("left", left);
            processed.clear();
            boolean isEmpty = processed.isEmpty();               // true
            show("isEmpty", isEmpty);
        }
        {
            Set<Long> seen = new HashSet<>();
            List<Message> batch = List.of(new Message(1, "pay"), new Message(2, "ship"), new Message(1, "pay"));
            List<String> handled = handleOnce(batch, seen);      // [pay, ship]
            show("handled", handled);
        }
        {
            Set<String> skills = new HashSet<>(List.of("sql", "java", "go"));
            List<String> sorted = skills.stream().sorted().toList();   // [go, java, sql]
            show("sorted", sorted);
            StringBuilder out = new StringBuilder();
            skills.forEach(s -> out.append(s.length()));
            int totalChars = out.length();                             // 3, one digit per skill
            show("totalChars", totalChars);
        }
        {
            Set<String> langs = new HashSet<>(List.of("java", "kotlin"));
            Iterator<String> it = langs.iterator();
            String one = it.next();                              // either element, no fixed order
            show("one", one);
            langs.add("scala");
            try { String two = it.next(); show("two", two); } catch (Throwable _t) { System.out.println("two -> " + _t); }
        }
        {
            Set<Integer> a = Set.of(1, 2, 3);
            Set<Integer> b = Set.of(3, 4);
            Set<Integer> union = new HashSet<>(a);
            union.addAll(b);
            Set<Integer> common = new HashSet<>(a);
            common.retainAll(b);
            Set<Integer> onlyA = new HashSet<>(a);
            onlyA.removeAll(b);
            boolean isUnion = union.equals(Set.of(1, 2, 3, 4));  // true
            show("isUnion", isUnion);
            boolean isCommon = common.equals(Set.of(3));         // true
            show("isCommon", isCommon);
            boolean isOnlyA = onlyA.equals(Set.of(1, 2));        // true
            show("isOnlyA", isOnlyA);
            boolean subset = a.containsAll(b);                   // false
            show("subset", subset);
        }
        {
            Set<TagClass> classTags = new HashSet<>();
            classTags.add(new TagClass("java"));
            classTags.add(new TagClass("java"));
            int classCount = classTags.size();                   // 2, identity comparison
            show("classCount", classCount);
            Set<Tag> recordTags = new HashSet<>();
            recordTags.add(new Tag("java"));
            recordTags.add(new Tag("java"));
            int recordCount = recordTags.size();                 // 1, records compare fields
            show("recordCount", recordCount);
        }
        {
            List<String> cart = new ArrayList<>(List.of("apple"));
            Set<List<String>> carts = new HashSet<>();
            carts.add(cart);
            cart.add("bread");                                   // changes cart.hashCode()
            boolean stillThere = carts.contains(cart);           // false, wrong bucket
            show("stillThere", stillThere);
            int cartCount = carts.size();                        // 1, the element is still inside
            show("cartCount", cartCount);
        }
        {
            Set<String> shared = ConcurrentHashMap.newKeySet();
            boolean added = shared.add("job-1");                 // true, safe from any thread
            show("added", added);
        }
        {
            Set<String> unique = new HashSet<>(List.of("b", "a"));
            List<String> copy = new ArrayList<>(unique);         // 2 elements, any order
            show("copy", copy);
            List<String> sortedList = unique.stream().sorted().toList(); // [a, b]
            show("sortedList", sortedList);
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
