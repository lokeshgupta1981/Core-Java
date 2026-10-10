package com.howtodoinjava.core.collections.queue;

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
 * Examples for the tutorial "Java ArrayDeque Tutorial: Stack, Queue and Deque Examples".
 * https://howtodoinjava.com/java/collections/java-arraydeque/
 */
public class ArrayDequeExamples {
    static <E> E newest(SequencedCollection<E> items) {
        return items.getLast();
    }
    static void record(Deque<String> history, String action, int limit) {
        history.push(action);
        if (history.size() > limit) {
            history.removeLast();                   // drop the oldest action
        }
    }
    public static void main(String[] args) throws Exception {
        {
            ArrayDeque<String> pages = new ArrayDeque<>();
            pages.addLast("home");                          // [home]
            pages.addLast("search");                        // [home, search]
            pages.addFirst("login");                        // [login, home, search]
            String first = pages.peekFirst();               // "login"
            show("first", first);
            String last = pages.peekLast();                 // "search"
            show("last", last);
            String removedLast = pages.pollLast();          // "search"
            show("removedLast", removedLast);
            pages.push("cart");                             // push() adds at the front
            String popped = pages.pop();                    // "cart"
            show("popped", popped);
            List<String> reversed = List.copyOf(pages.reversed());   // [home, login] (Java 21)
            show("reversed", reversed);
            String fromEmpty = new ArrayDeque<String>().pollFirst(); // null
            show("fromEmpty", fromEmpty);
        }
        {
            ArrayDeque<Integer> empty = new ArrayDeque<>();
            ArrayDeque<Integer> sized = new ArrayDeque<>(1_000);
            ArrayDeque<Integer> copied = new ArrayDeque<>(List.of(1, 2, 3));
            int count = copied.size();                      // 3
            show("count", count);
            try { boolean nullAdded = empty.add(null); show("nullAdded", nullAdded); } catch (Throwable _t) { System.out.println("nullAdded -> " + _t); }
        }
        {
            ArrayDeque<String> tabs = new ArrayDeque<>(List.of("mail", "docs"));
            String front = tabs.removeFirst();              // "mail"
            show("front", front);
            String back = tabs.getLast();                   // "docs"
            show("back", back);
            tabs.clear();
            String safe = tabs.peekLast();                  // null
            show("safe", safe);
            try { String strict = tabs.removeLast(); show("strict", strict); } catch (Throwable _t) { System.out.println("strict -> " + _t); }
        }
        {
            ArrayDeque<String> visits = new ArrayDeque<>(List.of("home", "cart", "home"));
            boolean removed = visits.removeLastOccurrence("home");   // true
            show("removed", removed);
            List<String> left = List.copyOf(visits);                 // [home, cart]
            show("left", left);
            boolean missing = visits.removeFirstOccurrence("blog");  // false
            show("missing", missing);
        }
        {
            Deque<Integer> stack = new ArrayDeque<>();
            stack.push(15);
            stack.push(10);
            stack.push(5);
            String asText = stack.toString();               // "[5, 10, 15]" (top first)
            show("asText", asText);
            Integer top = stack.peek();                     // 5
            show("top", top);
            Integer popped = stack.pop();                   // 5
            show("popped", popped);
            Integer next = stack.pop();                     // 10
            show("next", next);
        }
        {
            Queue<String> downloads = new ArrayDeque<>();
            downloads.offer("a.zip");
            downloads.offer("b.zip");
            downloads.offer("c.zip");
            String nextFile = downloads.poll();             // "a.zip"
            show("nextFile", nextFile);
            String peekFile = downloads.peek();             // "b.zip"
            show("peekFile", peekFile);
            int waiting = downloads.size();                 // 2
            show("waiting", waiting);
        }
        {
            ArrayDeque<String> steps = new ArrayDeque<>(List.of("wash", "cut", "cook"));
            Deque<String> backwards = steps.reversed();
            String lastStep = backwards.getFirst();         // "cook"
            show("lastStep", lastStep);
            backwards.addFirst("serve");                    // adds at the end of steps
            List<String> forward = List.copyOf(steps);      // [wash, cut, cook, serve]
            show("forward", forward);
        }
        {
            String fromDeque = newest(new ArrayDeque<>(List.of("a", "b")));   // "b"
            show("fromDeque", fromDeque);
            String fromList = newest(List.of("x", "y", "z"));                  // "z"
            show("fromList", fromList);
        }
        {
            ArrayDeque<Integer> levels = new ArrayDeque<>(List.of(1, 2, 3));
            List<Integer> frontToBack = levels.stream().toList();          // [1, 2, 3]
            show("frontToBack", frontToBack);
            List<Integer> backToFront = new ArrayList<>();
            levels.descendingIterator().forEachRemaining(backToFront::add);
            List<Integer> viaIterator = backToFront;                       // [3, 2, 1]
            show("viaIterator", viaIterator);
            List<Integer> viaReversed = levels.reversed().stream().toList();   // [3, 2, 1]
            show("viaReversed", viaReversed);
            boolean anyRemoved = levels.removeIf(n -> n % 2 == 0);         // true, levels is [1, 3]
            show("anyRemoved", anyRemoved);
        }
        {
            Deque<String> history = new ArrayDeque<>();
            record(history, "draw line", 3);
            record(history, "fill red", 3);
            record(history, "add text", 3);
            record(history, "resize", 3);
            List<String> kept = List.copyOf(history);       // [resize, add text, fill red]
            show("kept", kept);
            String undone = history.pop();                  // "resize"
            show("undone", undone);
            String nextUndo = history.peek();               // "add text"
            show("nextUndo", nextUndo);
        }
        {
            ArrayDeque<String> recent = new ArrayDeque<>(List.of("a", "b", "c"));
            String second = List.copyOf(recent).get(1);     // "b"
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
