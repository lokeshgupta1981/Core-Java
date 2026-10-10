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
 * Examples for the tutorial "Java LinkedList Class: Methods, Deque Usage and Performance".
 * https://howtodoinjava.com/java/collections/java-linkedlist-class/
 */
public class LinkedListExamples {
    static void record(LinkedList<String> history, String action, int limit) {
        history.push(action);
        if (history.size() > limit) {
            history.removeLast();
        }
    }
    static <E> String ends(SequencedCollection<E> items) {
        return items.getFirst() + ".." + items.getLast();
    }
    public static void main(String[] args) throws Exception {
        {
            LinkedList<String> tasks = new LinkedList<>(List.of("write", "test"));
            tasks.addFirst("plan");                              // at the head, O(1)
            tasks.addLast("deploy");                             // at the tail, O(1)
            String first = tasks.getFirst();                     // "plan"
            show("first", first);
            String last = tasks.getLast();                       // "deploy"
            show("last", last);
            String third = tasks.get(2);                         // "test", walks the nodes
            show("third", third);
            String done = tasks.removeFirst();                   // "plan"
            show("done", done);
            LinkedList<String> backwards = tasks.reversed();     // [deploy, test, write]
            show("backwards", backwards);
            String next = new LinkedList<String>().poll();       // null, empty list
            show("next", next);
        }
        {
            LinkedList<String> stations = new LinkedList<>(List.of("A", "C", "D"));
            stations.add(1, "B");                                // insert at index 1
            String old = stations.set(3, "E");                   // "D", replaced
            show("old", old);
            int pos = stations.indexOf("C");                     // 2
            show("pos", pos);
            boolean has = stations.contains("Z");                // false
            show("has", has);
            List<String> all = stations;                         // [A, B, C, E]
            show("all", all);
        }
        {
            LinkedList<Integer> codes = new LinkedList<>(List.of(10, 20, 30));
            Integer byIndex = codes.remove(1);                   // 20, removed index 1
            show("byIndex", byIndex);
            boolean byValue = codes.remove(Integer.valueOf(10)); // true, removed value 10
            show("byValue", byValue);
            List<Integer> left = codes;                          // [30]
            show("left", left);
        }
        {
            LinkedList<String> route = new LinkedList<>(List.of("home", "office", "gym"));
            ListIterator<String> stops = route.listIterator();
            while (stops.hasNext()) {
                if (stops.next().equals("office")) {
                    stops.add("cafe");
                }
            }
            List<String> withCafe = route;                       // [home, office, cafe, gym]
            show("withCafe", withCafe);
        }
        {
            Queue<String> printJobs = new LinkedList<>();
            printJobs.offer("report.pdf");
            printJobs.offer("invoice.pdf");
            String printing = printJobs.poll();                  // "report.pdf", first in, first out
            show("printing", printing);
            String waiting = printJobs.peek();                   // "invoice.pdf", still queued
            show("waiting", waiting);
            Deque<String> stack = new LinkedList<>();
            stack.push("page1");
            stack.push("page2");
            String top = stack.pop();                            // "page2", last in, first out
            show("top", top);
            try { String empty = new LinkedList<String>().pop(); show("empty", empty); } catch (Throwable _t) { System.out.println("empty -> " + _t); }
        }
        {
            LinkedList<String> history = new LinkedList<>();
            for (String action : List.of("type a", "type b", "bold", "delete")) {
                record(history, action, 3);
            }
            String undone = history.pop();                       // "delete"
            show("undone", undone);
            List<String> stillUndoable = history;                // [bold, type b]
            show("stillUndoable", stillUndoable);
        }
        {
            LinkedList<Integer> versions = new LinkedList<>(List.of(17, 21, 25));
            LinkedList<Integer> newestFirst = versions.reversed();   // [25, 21, 17]
            show("newestFirst", newestFirst);
            newestFirst.addFirst(26);                                // adds at the tail of versions
            List<Integer> current = versions;                        // [17, 21, 25, 26]
            show("current", current);
            Integer latest = versions.getLast();                     // 26
            show("latest", latest);
        }
        {
            String fromList = ends(new LinkedList<>(List.of("a", "b", "c")));     // "a..c"
            show("fromList", fromList);
            String fromSet = ends(new LinkedHashSet<>(List.of("x", "y")));         // "x..y"
            show("fromSet", fromSet);
        }
        {
            LinkedList<String> letters = new LinkedList<>(List.of("a", "b", "c"));
            Collections.reverse(letters);
            List<String> flipped = letters;                      // [c, b, a]
            show("flipped", flipped);
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
