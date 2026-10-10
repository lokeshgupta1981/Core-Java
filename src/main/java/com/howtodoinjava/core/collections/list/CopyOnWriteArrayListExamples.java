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
 * Examples for the tutorial "Java CopyOnWriteArrayList: Snapshot Iterators and Use Cases".
 * https://howtodoinjava.com/java/collections/java-copyonwritearraylist/
 */
public class CopyOnWriteArrayListExamples {
    static record OrderPlaced(long id, double total) {}
    static class OrderEvents {
        private final CopyOnWriteArrayList<Consumer<OrderPlaced>> listeners = new CopyOnWriteArrayList<>();

        void register(Consumer<OrderPlaced> listener) {
            listeners.addIfAbsent(listener);
        }

        void publish(OrderPlaced event) {
            for (Consumer<OrderPlaced> listener : listeners) {
                listener.accept(event);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            CopyOnWriteArrayList<String> channels = new CopyOnWriteArrayList<>(List.of("email", "sms"));
            Iterator<String> snapshot = channels.iterator();
            boolean added = channels.add("push");                  // true, copies the array
            show("added", added);
            boolean addedTwice = channels.addIfAbsent("sms");      // false, already present
            show("addedTwice", addedTwice);
            String first = channels.getFirst();                    // "email"
            show("first", first);
            int size = channels.size();                            // 3
            show("size", size);
            List<String> seen = new ArrayList<>();
            snapshot.forEachRemaining(seen::add);
            List<String> fromSnapshot = seen;                      // [email, sms]
            show("fromSnapshot", fromSnapshot);
        }
        {
            List<Integer> plain = new ArrayList<>(List.of(1, 2, 3));
            Iterator<Integer> plainIt = plain.iterator();
            Integer one = plainIt.next();                          // 1
            show("one", one);
            boolean grew = plain.add(4);                           // true
            show("grew", grew);
            try { Integer two = plainIt.next(); show("two", two); } catch (Throwable _t) { System.out.println("two -> " + _t); }
        }
        {
            CopyOnWriteArrayList<Integer> numbers = new CopyOnWriteArrayList<>(List.of(1, 2, 3));
            Iterator<Integer> itr1 = numbers.iterator();
            boolean added = numbers.add(4);                        // true
            show("added", added);
            Iterator<Integer> itr2 = numbers.iterator();
            List<Integer> seenBy1 = new ArrayList<>();
            itr1.forEachRemaining(seenBy1::add);
            List<Integer> seenBy2 = new ArrayList<>();
            itr2.forEachRemaining(seenBy2::add);
            List<Integer> first = seenBy1;                         // [1, 2, 3]
            show("first", first);
            List<Integer> second = seenBy2;                        // [1, 2, 3, 4]
            show("second", second);
        }
        {
            CopyOnWriteArrayList<String> queue = new CopyOnWriteArrayList<>(List.of("a", "b"));
            for (String item : queue) {
                queue.add(item + "-retry");
            }
            List<String> result = queue;                           // [a, b, a-retry, b-retry]
            show("result", result);
        }
        {
            CopyOnWriteArrayList<String> tags = new CopyOnWriteArrayList<>(List.of("java", "old", "spring"));
            Iterator<String> tagIt = tags.iterator();
            String firstTag = tagIt.next();                        // "java"
            show("firstTag", firstTag);
            try { tagIt.remove();  } catch (Throwable _t) { System.out.println("-> " + _t); }
            boolean removed = tags.removeIf(t -> t.equals("old")); // true
            show("removed", removed);
            List<String> left = tags;                              // [java, spring]
            show("left", left);
        }
        {
            CopyOnWriteArrayList<String> empty = new CopyOnWriteArrayList<>();                 // []
            show("empty", empty);
            CopyOnWriteArrayList<String> fromList = new CopyOnWriteArrayList<>(List.of("a", "b"));  // [a, b]
            show("fromList", fromList);
            CopyOnWriteArrayList<String> fromArray = new CopyOnWriteArrayList<>(new String[] {"x"}); // [x]
            show("fromArray", fromArray);
        }
        {
            CopyOnWriteArrayList<String> users = new CopyOnWriteArrayList<>(List.of("alex"));
            boolean addedSam = users.addIfAbsent("sam");           // true
            show("addedSam", addedSam);
            boolean addedAlex = users.addIfAbsent("alex");         // false
            show("addedAlex", addedAlex);
            int count = users.addAllAbsent(List.of("sam", "kim", "lee")); // 2
            show("count", count);
            List<String> all = users;                              // [alex, sam, kim, lee]
            show("all", all);
        }
        {
            CopyOnWriteArrayList<Integer> scores = new CopyOnWriteArrayList<>(List.of(10, 20, 30));
            Integer lowest = scores.getFirst();                    // 10
            show("lowest", lowest);
            Integer latest = scores.getLast();                     // 30
            show("latest", latest);
            scores.addFirst(5);
            List<Integer> reversed = scores.reversed();            // [30, 20, 10, 5]
            show("reversed", reversed);
            Integer removedLast = scores.removeLast();             // 30
            show("removedLast", removedLast);
            List<Integer> current = scores;                        // [5, 10, 20]
            show("current", current);
        }
        {
            CopyOnWriteArrayList<String> empty = new CopyOnWriteArrayList<>();
            try { String none = empty.getFirst(); show("none", none); } catch (Throwable _t) { System.out.println("none -> " + _t); }
        }
        {
            List<String> log = new CopyOnWriteArrayList<>();
            Consumer<OrderPlaced> audit = e -> log.add("audit " + e.id());
            Consumer<OrderPlaced> mail = e -> log.add("mail " + e.id());
            OrderEvents events = new OrderEvents();
            events.register(audit);
            events.register(mail);
            events.register(audit);
            events.publish(new OrderPlaced(7, 49.9));
            List<String> calls = log;                              // [audit 7, mail 7]
            show("calls", calls);
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
