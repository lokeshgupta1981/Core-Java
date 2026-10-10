package com.howtodoinjava.core.collections.cursors;

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
 * Examples for the tutorial "Java Spliterator: trySplit(), Characteristics and Examples".
 * https://howtodoinjava.com/java/collections/java-spliterator/
 */
public class SpliteratorExamples {
    static class SumTask extends RecursiveTask<Long> {
        private final Spliterator<Integer> source;

        SumTask(Spliterator<Integer> source) {
            this.source = source;
        }

        @Override
        protected Long compute() {
            if (source.estimateSize() > 1_000) {
                Spliterator<Integer> prefix = source.trySplit();
                if (prefix != null) {
                    SumTask left = new SumTask(prefix);
                    left.fork();
                    long right = new SumTask(source).compute();
                    return right + left.join();
                }
            }
            long[] sum = {0};
            source.forEachRemaining(n -> sum[0] += n);
            return sum[0];
        }
    }
    static class PageSpliterator extends Spliterators.AbstractSpliterator<String> {
        private final IntFunction<List<String>> fetchPage;
        private int page = 0;
        private Iterator<String> current = Collections.emptyIterator();

        PageSpliterator(IntFunction<List<String>> fetchPage) {
            super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
            this.fetchPage = fetchPage;
        }

        @Override
        public boolean tryAdvance(Consumer<? super String> action) {
            while (!current.hasNext()) {
                List<String> next = fetchPage.apply(page++);
                if (next.isEmpty()) {
                    return false;
                }
                current = next.iterator();
            }
            action.accept(current.next());
            return true;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> books = new ArrayList<>(List.of("Dune", "Emma", "Ulysses", "Beloved"));
            Spliterator<String> rest = books.spliterator();
            Spliterator<String> prefix = rest.trySplit();
            long prefixSize = prefix.estimateSize();                        // 2, holds Dune and Emma
            show("prefixSize", prefixSize);
            long restSize = rest.estimateSize();                            // 2, holds Ulysses and Beloved
            show("restSize", restSize);
            boolean moved = rest.tryAdvance(b -> System.out.println(b));    // true, prints Ulysses
            show("moved", moved);
            boolean sized = rest.hasCharacteristics(Spliterator.SIZED);     // true
            show("sized", sized);
            long total = StreamSupport.stream(books.spliterator(), true).count();   // 4
            show("total", total);
        }
        {
            Iterator<String> legacy = List.of("Dune", "Emma").iterator();
            Spliterator<String> wrapped = Spliterators.spliteratorUnknownSize(legacy, Spliterator.ORDERED);
            List<String> titles = StreamSupport.stream(wrapped, false).map(String::toUpperCase).toList();   // [DUNE, EMMA]
            show("titles", titles);
        }
        {
            List<String> list = new ArrayList<>(List.of("Dune"));
            Spliterator<String> spliterator = list.spliterator();
            int expected = Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
            boolean same = spliterator.characteristics() == expected;            // true
            show("same", same);
            boolean ordered = spliterator.hasCharacteristics(Spliterator.ORDERED);   // true
            show("ordered", ordered);
            boolean sorted = spliterator.hasCharacteristics(Spliterator.SORTED);     // false
            show("sorted", sorted);
            boolean distinct = new HashSet<>(list).spliterator().hasCharacteristics(Spliterator.DISTINCT);   // true
            show("distinct", distinct);
        }
        {
            Spliterator<String> fromList = new ArrayList<>(List.of("A", "B", "C", "D")).spliterator();
            long estimate = fromList.estimateSize();                  // 4
            show("estimate", estimate);
            long exact = fromList.getExactSizeIfKnown();              // 4
            show("exact", exact);
            Spliterator<Integer> fromSet = new HashSet<>(Set.of(1, 2, 3, 4, 5, 6)).spliterator();
            Spliterator<Integer> half = fromSet.trySplit();
            long guess = fromSet.estimateSize();                      // 3
            show("guess", guess);
            long unknown = fromSet.getExactSizeIfKnown();             // -1
            show("unknown", unknown);
        }
        {
            SortedSet<String> reversed = new TreeSet<>(Comparator.reverseOrder());
            reversed.addAll(List.of("A", "D", "C", "B"));
            String content = reversed.toString();                     // "[D, C, B, A]"
            show("content", content);
            boolean custom = reversed.spliterator().getComparator() != null;     // true
            show("custom", custom);
            Comparator<? super String> natural = new TreeSet<String>().spliterator().getComparator();   // null
            show("natural", natural);
            try { Comparator<? super String> none = new ArrayList<String>().spliterator().getComparator(); show("none", none); } catch (Throwable _t) { System.out.println("none -> " + _t); }
        }
        {
            List<String> letters = new ArrayList<>(List.of("A", "B", "C", "D", "E", "F"));
            Spliterator<String> spliterator1 = letters.spliterator();
            Spliterator<String> spliterator2 = spliterator1.trySplit();
            List<String> kept = new ArrayList<>();
            spliterator1.forEachRemaining(kept::add);
            List<String> splitOff = new ArrayList<>();
            spliterator2.forEachRemaining(splitOff::add);
            String original = kept.toString();                        // "[D, E, F]"
            show("original", original);
            String returned = splitOff.toString();                    // "[A, B, C]"
            show("returned", returned);
            Spliterator<String> tiny = new ArrayList<>(List.of("A")).spliterator();
            Spliterator<String> nothing = tiny.trySplit();            // null
            show("nothing", nothing);
        }
        {
            List<Integer> pageViews = new ArrayList<>(List.of(120, 80, 45, 300, 10, 75));
            Spliterator<Integer> views = pageViews.spliterator();
            List<Integer> seen = new ArrayList<>();
            boolean first = views.tryAdvance(seen::add);              // true
            show("first", first);
            boolean second = views.tryAdvance(seen::add);             // true
            show("second", second);
            views.forEachRemaining(seen::add);
            String all = seen.toString();                             // "[120, 80, 45, 300, 10, 75]"
            show("all", all);
            boolean more = views.tryAdvance(seen::add);               // false
            show("more", more);
        }
        {
            List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3));
            Spliterator<Integer> lazy = numbers.spliterator();
            boolean added = numbers.add(4);                           // true, before binding
            show("added", added);
            long boundSize = lazy.estimateSize();                     // 4
            show("boundSize", boundSize);
            boolean read = lazy.tryAdvance(n -> {});                  // true
            show("read", read);
            boolean addedLate = numbers.add(5);                       // true, after binding
            show("addedLate", addedLate);
            try { lazy.forEachRemaining(n -> {});  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            List<Integer> orderIds = IntStream.rangeClosed(1, 7).boxed().toList();
            Spliterator<Integer> source = orderIds.spliterator();
            List<List<Integer>> requests = new ArrayList<>();
            List<Integer> batch = new ArrayList<>();
            while (source.tryAdvance(batch::add)) {
                if (batch.size() == 3) {
                    requests.add(List.copyOf(batch));      // send the batch here
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                requests.add(List.copyOf(batch));
            }
            int requestCount = requests.size();                       // 3
            show("requestCount", requestCount);
            List<Integer> lastBatch = requests.getLast();             // [7]
            show("lastBatch", lastBatch);
        }
        {
            List<Integer> views = IntStream.rangeClosed(1, 10_000).boxed().toList();
            long byTask = ForkJoinPool.commonPool().invoke(new SumTask(views.spliterator()));   // 50005000
            show("byTask", byTask);
            long byStream = views.parallelStream().mapToLong(Integer::longValue).sum();          // 50005000
            show("byStream", byStream);
        }
        {
            List<List<String>> server = List.of(List.of("Dune", "Emma"), List.of("Ulysses"), List.of());
            List<String> allTitles = StreamSupport.stream(new PageSpliterator(server::get), false).toList();   // [Dune, Emma, Ulysses]
            show("allTitles", allTitles);
            Optional<String> firstTitle = StreamSupport.stream(new PageSpliterator(server::get), false).findFirst();   // Optional[Dune]
            show("firstTitle", firstTitle);
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
