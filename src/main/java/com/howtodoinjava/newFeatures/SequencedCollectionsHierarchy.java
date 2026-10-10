package com.howtodoinjava.newFeatures;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;

/**
 * Examples for the tutorial "Java Sequenced Collections: getFirst, getLast and reversed()".
 * https://howtodoinjava.com/java/sequenced-collections/
 */
public class SequencedCollectionsHierarchy {
    static abstract class TrackQueue<E> extends AbstractList<E> implements Deque<E> {
        @Override public abstract TrackQueue<E> reversed();
        @Override public abstract void addFirst(E e);
        @Override public abstract void addLast(E e);
        @Override public abstract E getFirst();
        @Override public abstract E getLast();
        @Override public abstract E removeFirst();
        @Override public abstract E removeLast();
    }
    static void remember(LinkedHashSet<String> recent, String term, int limit) {
        recent.addFirst(term);
        if (recent.size() > limit) {
            recent.removeLast();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> fruits = new ArrayList<>(List.of("apple", "banana", "cherry"));
            String first = fruits.getFirst();                                  // "apple"
            show("first", first);
            String last = fruits.getLast();                                    // "cherry"
            show("last", last);
            List<String> backwards = fruits.reversed();                        // [cherry, banana, apple]
            show("backwards", backwards);
            SequencedMap<String, Integer> stock = new LinkedHashMap<>();
            stock.put("apple", 5);
            stock.put("banana", 3);
            Map.Entry<String, Integer> lastEntry = stock.lastEntry();          // banana=3
            show("lastEntry", lastEntry);
        }
        {
            List<Integer> list = List.of(1, 2, 3);
            Integer oldFirst = list.get(0);                                    // 1
            show("oldFirst", oldFirst);
            Integer oldLast = list.get(list.size() - 1);                       // 3
            show("oldLast", oldLast);
            LinkedHashSet<Integer> set = new LinkedHashSet<>(list);
            Integer oldSetFirst = set.iterator().next();                       // 1
            show("oldSetFirst", oldSetFirst);
            Integer oldSetLast = set.stream().reduce((a, b) -> b).orElseThrow(); // 3
            show("oldSetLast", oldSetLast);
        }
        {
            List<Integer> numbers = List.of(1, 2, 3);
            LinkedHashSet<Integer> ordered = new LinkedHashSet<>(numbers);
            Integer listFirst = numbers.getFirst();                            // 1
            show("listFirst", listFirst);
            Integer listLast = numbers.getLast();                              // 3
            show("listLast", listLast);
            Integer setLast = ordered.getLast();                               // 3
            show("setLast", setLast);
        }
        {
            ArrayList<Integer> arrayList = new ArrayList<>();
            arrayList.add(1);
            arrayList.addFirst(0);
            arrayList.addLast(2);
            String content = arrayList.toString();                             // "[0, 1, 2]"
            show("content", content);
            Integer firstElement = arrayList.getFirst();                       // 0
            show("firstElement", firstElement);
            Integer lastElement = arrayList.getLast();                         // 2
            show("lastElement", lastElement);
            List<Integer> reversed = arrayList.reversed();                     // [2, 1, 0]
            show("reversed", reversed);
            arrayList.add(3);
            String reversedAfterAdd = reversed.toString();                     // "[3, 2, 1, 0]"
            show("reversedAfterAdd", reversedAfterAdd);
            reversed.addFirst(9);
            String original = arrayList.toString();                            // "[0, 1, 2, 3, 9]"
            show("original", original);
        }
        {
            LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet<>(List.of(1, 2, 3));
            Integer firstInSet = linkedHashSet.getFirst();                     // 1
            show("firstInSet", firstInSet);
            Integer lastInSet = linkedHashSet.getLast();                       // 3
            show("lastInSet", lastInSet);
            linkedHashSet.addFirst(0);
            linkedHashSet.addLast(4);
            String setContent = linkedHashSet.toString();                      // "[0, 1, 2, 3, 4]"
            show("setContent", setContent);
            SequencedSet<Integer> reversedSet = linkedHashSet.reversed();      // [4, 3, 2, 1, 0]
            show("reversedSet", reversedSet);
            linkedHashSet.addFirst(3);
            String moved = linkedHashSet.toString();                           // "[3, 0, 1, 2, 4]"
            show("moved", moved);
        }
        {
            LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
            map.put(1, "One");
            map.put(2, "Two");
            map.put(3, "Three");
            Map.Entry<Integer, String> firstMapEntry = map.firstEntry();       // 1=One
            show("firstMapEntry", firstMapEntry);
            Map.Entry<Integer, String> lastMapEntry = map.lastEntry();         // 3=Three
            show("lastMapEntry", lastMapEntry);
            Map.Entry<Integer, String> polledFirst = map.pollFirstEntry();     // 1=One
            show("polledFirst", polledFirst);
            Map.Entry<Integer, String> polledLast = map.pollLastEntry();       // 3=Three
            show("polledLast", polledLast);
            String afterPoll = map.toString();                                 // "{2=Two}"
            show("afterPoll", afterPoll);
            map.putFirst(1, "One");
            map.putLast(3, "Three");
            String restored = map.toString();                                  // "{1=One, 2=Two, 3=Three}"
            show("restored", restored);
            SequencedMap<Integer, String> reversedMap = map.reversed();        // {3=Three, 2=Two, 1=One}
            show("reversedMap", reversedMap);
            SequencedSet<Integer> keysBackwards = map.sequencedKeySet().reversed(); // [3, 2, 1]
            show("keysBackwards", keysBackwards);
        }
        {
            LinkedHashMap<String, Integer> scores = new LinkedHashMap<>();
            scores.put("anna", 7);
            SequencedMap<String, Integer> readOnly = Collections.unmodifiableSequencedMap(scores);
            scores.put("lokesh", 9);
            Map.Entry<String, Integer> newest = readOnly.lastEntry();          // lokesh=9
            show("newest", newest);
            try { Integer rejected = readOnly.putFirst("bob", 1); show("rejected", rejected); } catch (Throwable _t) { System.out.println("rejected -> " + _t); }
        }
        {
            List<Integer> fixed = List.of(1, 2, 3);
            List<Integer> fixedReversed = fixed.reversed();                    // [3, 2, 1]
            show("fixedReversed", fixedReversed);
            try { fixed.addLast(4);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            TreeSet<Integer> sorted = new TreeSet<>(List.of(1, 2, 3));
            Integer smallest = sorted.getFirst();                              // 1
            show("smallest", smallest);
            try { sorted.addFirst(4);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<Integer> empty = new ArrayList<>();
            try { Integer missing = empty.getFirst(); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
            Integer safe = empty.isEmpty() ? null : empty.getFirst();          // null
            show("safe", safe);
            Optional<Integer> firstOrEmpty = empty.stream().findFirst();       // Optional.empty
            show("firstOrEmpty", firstOrEmpty);
        }
        {
            LinkedHashSet<String> recent = new LinkedHashSet<>();
            remember(recent, "shoes", 3);
            remember(recent, "socks", 3);
            remember(recent, "hat", 3);
            remember(recent, "shoes", 3);
            String afterRepeat = recent.toString();                            // "[shoes, hat, socks]"
            show("afterRepeat", afterRepeat);
            remember(recent, "belt", 3);
            String afterLimit = recent.toString();                             // "[belt, shoes, hat]"
            show("afterLimit", afterLimit);
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
