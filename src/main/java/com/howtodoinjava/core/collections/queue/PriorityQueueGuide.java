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
 * Examples for the tutorial "Java Priority Queue: PriorityQueue, Comparator and Max-Heap".
 * https://howtodoinjava.com/java/collections/java-priorityqueue/
 */
public class PriorityQueueGuide {
    static <T> List<T> pollAll(Queue<T> queue) {
        List<T> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            result.add(queue.poll());
        }
        return result;
    }
    static record Ticket(int id, String title, int priority) implements Comparable<Ticket> {

        @Override
        public int compareTo(Ticket other) {
            return Integer.compare(other.priority, this.priority);   // higher priority first
        }
    }
    static record QueuedTicket(Ticket ticket, long sequence) {}
    public static void main(String[] args) throws Exception {
        {
            PriorityQueue<Integer> numbers = new PriorityQueue<>(List.of(5, 1, 8, 3));
            Integer head = numbers.peek();                  // 1 (smallest first, stays in the queue)
            show("head", head);
            Integer first = numbers.poll();                 // 1 (removed)
            show("first", first);
            Integer second = numbers.poll();                // 3
            show("second", second);

            PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
            maxHeap.addAll(List.of(5, 1, 8, 3));
            Integer largest = maxHeap.poll();               // 8 (largest first)
            show("largest", largest);

            PriorityQueue<String> byLength = new PriorityQueue<>(Comparator.comparingInt(String::length));
            byLength.addAll(List.of("banana", "kiwi", "fig"));
            String shortest = byLength.poll();              // "fig"
            show("shortest", shortest);

            Integer nothing = new PriorityQueue<Integer>().poll();   // null (empty queue, no exception)
            show("nothing", nothing);
        }
        {
            PriorityQueue<Integer> queue = new PriorityQueue<>();
            queue.add(5);
            queue.add(1);
            queue.add(8);
            queue.add(3);
            queue.add(2);
            String printed = queue.toString();              // "[1, 2, 8, 5, 3]"
            show("printed", printed);
            List<Integer> polled = pollAll(queue);          // [1, 2, 3, 5, 8]
            show("polled", polled);
        }
        {
            PriorityQueue<Integer> empty = new PriorityQueue<>();
            Integer safeHead = empty.peek();                // null
            show("safeHead", safeHead);
            try { Integer strictHead = empty.element(); show("strictHead", strictHead); } catch (Throwable _t) { System.out.println("strictHead -> " + _t); }
            try { boolean added = empty.offer(null); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            PriorityQueue<Integer> ids = new PriorityQueue<>(List.of(5, 1, 8));
            boolean hasEight = ids.contains(8);                  // true
            show("hasEight", hasEight);
            boolean removed = ids.remove(Integer.valueOf(8));    // true
            show("removed", removed);
            int left = ids.size();                               // 2
            show("left", left);
        }
        {
            PriorityQueue<Ticket> tickets = new PriorityQueue<>();
            tickets.add(new Ticket(1, "Login fails", 5));
            tickets.add(new Ticket(2, "Typo on page", 1));
            tickets.add(new Ticket(3, "Site down", 10));

            List<String> workOrder = pollAll(tickets).stream().map(Ticket::title).toList();   // [Site down, Login fails, Typo on page]
            show("workOrder", workOrder);
        }
        {
            Comparator<Ticket> byIdOrder = Comparator.comparingInt(Ticket::id);
            PriorityQueue<Ticket> byId = new PriorityQueue<>(byIdOrder);
            byId.add(new Ticket(3, "Site down", 10));
            byId.add(new Ticket(1, "Login fails", 5));
            byId.add(new Ticket(2, "Typo on page", 1));

            List<Integer> reportOrder = pollAll(byId).stream().map(Ticket::id).toList();   // [1, 2, 3]
            show("reportOrder", reportOrder);
            boolean sameComparator = byId.comparator() == byIdOrder;   // true
            show("sameComparator", sameComparator);
        }
        {
            PriorityQueue<Integer> biggestFirst = new PriorityQueue<>(Comparator.reverseOrder());
            biggestFirst.addAll(List.of(5, 1, 8, 3));
            List<Integer> descending = pollAll(biggestFirst);        // [8, 5, 3, 1]
            show("descending", descending);

            Comparator<Ticket> byPriorityThenTitle = Comparator.comparingInt(Ticket::priority).reversed()
                    .thenComparing(Ticket::title);
            PriorityQueue<Ticket> sorted = new PriorityQueue<>(byPriorityThenTitle);
            sorted.add(new Ticket(4, "Slow search", 5));
            sorted.add(new Ticket(1, "Login fails", 5));
            sorted.add(new Ticket(3, "Site down", 10));
            List<String> titles = pollAll(sorted).stream().map(Ticket::title).toList();   // [Site down, Login fails, Slow search]
            show("titles", titles);
        }
        {
            Comparator<Integer> broken = (a, b) -> b - a;
            int wrongSign = broken.compare(Integer.MIN_VALUE, 1);                    // -2147483647 (says MIN_VALUE is larger)
            show("wrongSign", wrongSign);
            int rightSign = Comparator.<Integer>reverseOrder().compare(Integer.MIN_VALUE, 1);   // 1
            show("rightSign", rightSign);
        }
        {
            AtomicLong counter = new AtomicLong();
            PriorityQueue<QueuedTicket> fifo = new PriorityQueue<>(
            Comparator.comparingInt((QueuedTicket q) -> q.ticket().priority()).reversed()
                    .thenComparingLong(QueuedTicket::sequence));
            fifo.add(new QueuedTicket(new Ticket(7, "Export broken", 5), counter.getAndIncrement()));
            fifo.add(new QueuedTicket(new Ticket(8, "Import broken", 5), counter.getAndIncrement()));
            fifo.add(new QueuedTicket(new Ticket(9, "Print broken", 5), counter.getAndIncrement()));
            List<Integer> arrival = pollAll(fifo).stream().map(q -> q.ticket().id()).toList();   // [7, 8, 9]
            show("arrival", arrival);
        }
        {
            List<Integer> responseTimes = List.of(120, 45, 300, 80, 250, 60, 410);
            PriorityQueue<Integer> top = new PriorityQueue<>();
            for (int time : responseTimes) {
                top.offer(time);
                if (top.size() > 3) {
                    top.poll();                             // drops the smallest of the four
                }
            }
            List<Integer> slowest = top.stream().sorted(Comparator.reverseOrder()).toList();   // [410, 300, 250]
            show("slowest", slowest);
        }
        {
            PriorityQueue<Integer> scores = new PriorityQueue<>(List.of(5, 1, 8, 3, 2));
            List<Integer> inOrder = scores.stream().sorted().toList();       // [1, 2, 3, 5, 8]
            show("inOrder", inOrder);
            List<Integer> copyPolled = pollAll(new PriorityQueue<>(scores));  // [1, 2, 3, 5, 8]
            show("copyPolled", copyPolled);
            int size = scores.size();                                         // 5 (original unchanged)
            show("size", size);
        }
        {
            Queue<Integer> shared = new PriorityBlockingQueue<>(List.of(5, 1));
            Integer sharedHead = shared.poll();             // 1
            show("sharedHead", sharedHead);
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
