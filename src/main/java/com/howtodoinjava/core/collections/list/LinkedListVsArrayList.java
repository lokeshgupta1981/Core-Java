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
 * Examples for the tutorial "LinkedList vs ArrayList in Java: Differences and When to Use".
 * https://howtodoinjava.com/java/collections/arraylist/linkedlist-vs-arraylist/
 */
public class LinkedListVsArrayList {
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;
    }
    static int totalLength(List<String> names) {
        int total = 0;
        for (String name : names) {        // iterator, O(n) for both classes
            total += name.length();
        }
        return total;
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> jobs = new ArrayList<>(List.of("report", "invoice", "photo"));
            LinkedList<String> queue = new LinkedList<>(jobs);
            String third = jobs.get(2);                                  // "photo", reads one array slot
            show("third", third);
            String thirdToo = queue.get(2);                              // "photo", walks the nodes
            show("thirdToo", thirdToo);
            queue.addFirst("urgent");                                    // links one node, no shifting
            jobs.add(0, "urgent");                                       // shifts 3 elements to the right
            String next = queue.pollFirst();                             // "urgent", LinkedList is also a Deque
            show("next", next);
            ArrayList<String> converted = new ArrayList<>(queue);        // [report, invoice, photo]
            show("converted", converted);
        }
        {
            int fast = totalLength(new LinkedList<>(List.of("report", "invoice")));   // 13
            show("fast", fast);
        }
        {
            LinkedList<String> pending = new LinkedList<>(List.of("report", "draft", "invoice", "draft"));
            Iterator<String> it = pending.iterator();
            while (it.hasNext()) {
                if (it.next().equals("draft")) {
                    it.remove();                                         // unlinks one node, O(1)
                }
            }
            String left = pending.toString();                            // "[report, invoice]"
            show("left", left);
        }
        {
            Deque<String> printer = new ArrayDeque<>();
            printer.offerLast("report");
            printer.offerLast("photo");
            printer.offerFirst("urgent");
            String printing = printer.pollFirst();                       // "urgent"
            show("printing", printing);
            String after = printer.peekFirst();                          // "report"
            show("after", after);
        }
        {
            LinkedList<String> linked = new LinkedList<>(List.of("report", "invoice", "photo"));
            ArrayList<String> array = new ArrayList<>(linked);                         // [report, invoice, photo]
            show("array", array);
            ArrayList<String> filled = new ArrayList<>();
            boolean copied = filled.addAll(linked);                                    // true, [report, invoice, photo]
            show("copied", copied);
            ArrayList<String> upper = linked.stream().map(String::toUpperCase).collect(Collectors.toCollection(ArrayList::new));   // [REPORT, INVOICE, PHOTO]
            show("upper", upper);
            List<String> readOnly = List.copyOf(linked);                               // [report, invoice, photo], unmodifiable
            show("readOnly", readOnly);
        }
        {
            ArrayList<String> source = new ArrayList<>(List.of("report", "invoice"));
            LinkedList<String> back = new LinkedList<>(source);                        // [report, invoice]
            show("back", back);
            LinkedList<String> reversedCopy = new LinkedList<>(source.reversed());     // [invoice, report], Java 21
            show("reversedCopy", reversedCopy);
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
