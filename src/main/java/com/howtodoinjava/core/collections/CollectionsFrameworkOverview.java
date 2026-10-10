package com.howtodoinjava.core.collections;

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
 * Examples for the tutorial "Java Collections Framework: Hierarchy, Interfaces and Classes".
 * https://howtodoinjava.com/java/collections/java-collections/
 */
public class CollectionsFrameworkOverview {
    static record Book(String title, int year) {}
    public static void main(String[] args) throws Exception {
        {
            List<String> fruits = new ArrayList<>(List.of("apple", "banana"));
            boolean added = fruits.add("cherry");                        // true
            show("added", added);
            String first = fruits.getFirst();                            // "apple"
            show("first", first);
            List<String> backwards = fruits.reversed();                  // [cherry, banana, apple]
            show("backwards", backwards);
            Set<String> unique = new TreeSet<>(List.of("kiwi", "apple", "kiwi"));  // [apple, kiwi]
            show("unique", unique);
            SequencedMap<String, Integer> stock = new LinkedHashMap<>();
            Integer previous = stock.put("apple", 5);                    // null
            show("previous", previous);
            Integer bananas = stock.merge("banana", 3, Integer::sum);    // 3
            show("bananas", bananas);
            Map.Entry<String, Integer> lastEntry = stock.lastEntry();    // banana=3
            show("lastEntry", lastEntry);
            Deque<String> orders = new ArrayDeque<>(List.of("pizza", "pasta"));
            String nextOrder = orders.pollFirst();                       // "pizza"
            show("nextOrder", nextOrder);
            List<String> longNames = fruits.stream().filter(f -> f.length() > 5).toList();  // [banana, cherry]
            show("longNames", longNames);
        }
        {
            Collection<String> tags = new ArrayList<>(List.of("java", "sql", "jpa"));
            int count = tags.size();                                     // 3
            show("count", count);
            boolean hasSql = tags.contains("sql");                       // true
            show("hasSql", hasSql);
            boolean removed = tags.removeIf(t -> t.startsWith("j"));     // true
            show("removed", removed);
            Collection<String> left = tags;                              // [sql]
            show("left", left);
        }
        {
            Map<String, Integer> ages = new TreeMap<>(Map.of("Lokesh", 37, "Alex", 29));
            Set<String> names = ages.keySet();                           // [Alex, Lokesh]
            show("names", names);
            Collection<Integer> years = ages.values();                   // [29, 37]
            show("years", years);
            boolean adult = ages.values().stream().allMatch(a -> a >= 18);  // true
            show("adult", adult);
        }
        {
            SequencedCollection<String> playlist = new ArrayList<>(List.of("intro", "verse"));
            playlist.addFirst("count-in");
            playlist.addLast("outro");
            String opener = playlist.getFirst();                         // "count-in"
            show("opener", opener);
            String closer = playlist.getLast();                          // "outro"
            show("closer", closer);
            SequencedCollection<String> backwards = playlist.reversed(); // [outro, verse, intro, count-in]
            show("backwards", backwards);
            SequencedMap<String, Integer> scores = new LinkedHashMap<>();
            scores.put("ana", 90);
            scores.put("ben", 75);
            Map.Entry<String, Integer> firstScore = scores.firstEntry(); // ana=90
            show("firstScore", firstScore);
            Map.Entry<String, Integer> removedScore = scores.pollLastEntry();  // ben=75
            show("removedScore", removedScore);
        }
        {
            List<String> fixed = List.of("a", "b");
            String a = fixed.getFirst();                                 // "a"
            show("a", a);
            try { fixed.addFirst("z");  } catch (Throwable _t) { System.out.println("-> " + _t); }
            try { String none = new ArrayList<String>().getFirst(); show("none", none); } catch (Throwable _t) { System.out.println("none -> " + _t); }
        }
        {
            Set<String> visited = new HashSet<>();
            boolean firstVisit = visited.add("home");                    // true
            show("firstVisit", firstVisit);
            boolean secondVisit = visited.add("home");                   // false
            show("secondVisit", secondVisit);
            Set<String> ordered = new LinkedHashSet<>(List.of("cart", "home", "cart"));  // [cart, home]
            show("ordered", ordered);
            NavigableSet<Integer> prices = new TreeSet<>(List.of(30, 10, 20));
            Integer cheapestOver15 = prices.ceiling(15);                 // 20
            show("cheapestOver15", cheapestOver15);
        }
        {
            Map<String, Integer> wordCount = new HashMap<>();
            for (String word : List.of("to", "be", "or", "not", "to", "be")) {
                wordCount.merge(word, 1, Integer::sum);
            }
            Integer toCount = wordCount.get("to");                       // 2
            show("toCount", toCount);
            Integer missing = wordCount.getOrDefault("is", 0);           // 0
            show("missing", missing);
            Map<Character, List<String>> byLetter = new TreeMap<>();
            byLetter.computeIfAbsent('a', k -> new ArrayList<>()).add("apple");
            List<String> aWords = byLetter.get('a');                     // [apple]
            show("aWords", aWords);
        }
        {
            Deque<String> undo = new ArrayDeque<>();
            undo.push("type A");
            undo.push("type B");
            String lastAction = undo.pop();                              // "type B"
            show("lastAction", lastAction);
            Queue<Integer> tickets = new PriorityQueue<>(List.of(5, 1, 3));
            Integer mostUrgent = tickets.poll();                         // 1
            show("mostUrgent", mostUrgent);
        }
        {
            List<String> days = List.of("mon", "tue");
            List<String> editable = new ArrayList<>(days);
            boolean addedWed = editable.add("wed");                      // true
            show("addedWed", addedWed);
            List<String> snapshot = List.copyOf(editable);               // [mon, tue, wed]
            show("snapshot", snapshot);
            List<String> upper = editable.stream().map(String::toUpperCase).toList();  // [MON, TUE, WED]
            show("upper", upper);
            try { Set<String> broken = Set.of("a", "a"); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
            try { List<String> withNull = List.of("a", null); show("withNull", withNull); } catch (Throwable _t) { System.out.println("withNull -> " + _t); }
        }
        {
            List<Book> books = new ArrayList<>(List.of(new Book("Dune", 1965), new Book("Emma", 1815)));
            books.sort(Comparator.comparingInt(Book::year));
            String oldest = books.getFirst().title();                    // "Emma"
            show("oldest", oldest);
        }
        {
            ConcurrentMap<String, Integer> hits = new ConcurrentHashMap<>();
            try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
                for (int i = 0; i < 1000; i++) {
                    pool.submit(() -> hits.merge("home", 1, Integer::sum));
                }
            }
            Integer homeHits = hits.get("home");                         // 1000
            show("homeHits", homeHits);
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
