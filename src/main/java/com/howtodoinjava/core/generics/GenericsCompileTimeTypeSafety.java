package com.howtodoinjava.core.generics;

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
 * Examples for the tutorial "Java Generics Tutorial: Classes, Methods, Bounds, Wildcards".
 * https://howtodoinjava.com/java/generics/complete-java-generics-tutorial/
 */
public class GenericsCompileTimeTypeSafety {
    static public record Page<T>(List<T> items, int number, int totalItems) {

        public Page {
            items = List.copyOf(items);
        }

        public boolean isEmpty() {
            return items.isEmpty();
        }
    }
    static public interface Repository<T, ID> {

        Optional<T> findById(ID id);

        void save(T item);
    }
    static public record Book(int id, String title) {
    }
    static public class BookRepository implements Repository<Book, Integer> {

        private final Map<Integer, Book> books = new HashMap<>();

        @Override
        public Optional<Book> findById(Integer id) {
            return Optional.ofNullable(books.get(id));
        }

        @Override
        public void save(Book book) {
            books.put(book.id(), book);
        }
    }
    static <T> T firstOrDefault(List<T> items, T fallback) {
        return items.isEmpty() ? fallback : items.get(0);
    }
    static public class Price {

        private final BigDecimal amount;

        public <N extends Number> Price(N value) {
            this.amount = new BigDecimal(value.toString());
        }

        public BigDecimal getAmount() {
            return amount;
        }
    }
    static <T extends Comparable<? super T>> T max(List<T> items) {
        T best = items.get(0);
        for (T item : items) {
            if (item.compareTo(best) > 0) {
                best = item;
            }
        }
        return best;
    }
    static public class Ranges {

        public static <T extends Number & Comparable<T>> T clamp(T value, T min, T max) {
            if (value.compareTo(min) < 0) {
                return min;
            }
            return value.compareTo(max) > 0 ? max : value;
        }
    }
    static int countAll(Collection<?> items) {
        return items.size();
    }
    static double sum(List<? extends Number> numbers) {
        double total = 0;
        for (Number number : numbers) {
            total += number.doubleValue();
        }
        return total;
    }
    static void addDefaults(List<? super Integer> target) {
        target.add(5);
        target.add(3);
    }
    static public class FruitList extends ArrayList<String> {
    }
    static public class Pool<T> {

        private final Supplier<T> factory;
        private final List<T> created = new ArrayList<>();

        public Pool(Supplier<T> factory) {
            this.factory = factory;
        }

        public T create() {
            T item = factory.get();
            created.add(item);
            return item;
        }

        public int size() {
            return created.size();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> fruits = new ArrayList<>();
            fruits.add("apple");
            fruits.add("banana");
            String first = fruits.get(0);                            // "apple"
            show("first", first);
            Page<String> page = new Page<>(fruits, 1, 2);            // Page[items=[apple, banana], number=1, totalItems=2]
            show("page", page);
            String top = firstOrDefault(page.items(), "none");       // "apple"
            show("top", top);
            String empty = firstOrDefault(List.of(), "none");        // "none"
            show("empty", empty);
        }
        {
            List rawNames = new ArrayList();
            rawNames.add("apple");
            rawNames.add(42);
            try { String second = (String) rawNames.get(1); show("second", second); } catch (Throwable _t) { System.out.println("second -> " + _t); }
        }
        {
            Page<String> fruitPage = new Page<>(List.of("apple", "banana"), 1, 2);
            String firstFruit = fruitPage.items().get(0);                        // "apple"
            show("firstFruit", firstFruit);
            var numberPage = new Page<Integer>(List.of(5, 3), 2, 4);
            int firstNumber = numberPage.items().get(0);                         // 5
            show("firstNumber", firstNumber);
            boolean emptyPage = new Page<String>(List.of(), 3, 4).isEmpty();     // true
            show("emptyPage", emptyPage);
        }
        {
            BookRepository repository = new BookRepository();
            repository.save(new Book(1, "Dune"));
            String title = repository.findById(1).map(Book::title).orElse("not found");    // "Dune"
            show("title", title);
            String missing = repository.findById(9).map(Book::title).orElse("not found");  // "not found"
            show("missing", missing);
        }
        {
            String firstName = firstOrDefault(List.of("apple", "banana"), "none");   // "apple"
            show("firstName", firstName);
            Integer firstCount = firstOrDefault(List.of(5, 3), 0);                    // 5
            show("firstCount", firstCount);
            List<String> noNames = Collections.<String>emptyList();                   // []
            show("noNames", noNames);
        }
        {
            BigDecimal fromInt = new Price(5).getAmount();          // 5
            show("fromInt", fromInt);
            BigDecimal fromDouble = new Price(2.5).getAmount();     // 2.5
            show("fromDouble", fromDouble);
        }
        {
            int highScore = max(List.of(5, 3, 9));                                    // 9
            show("highScore", highScore);
            String lastName = max(List.of("apple", "banana"));                        // "banana"
            show("lastName", lastName);
            LocalDate latest = max(List.of(LocalDate.of(2026, 1, 5), LocalDate.of(2026, 3, 1)));   // 2026-03-01
            show("latest", latest);
        }
        {
            int volume = Ranges.clamp(15, 0, 10);           // 10
            show("volume", volume);
            double ratio = Ranges.clamp(0.5, 0.0, 1.0);     // 0.5
            show("ratio", ratio);
        }
        {
            Object[] items = new String[2];
            try { items[0] = 42;  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            int fruitCount = countAll(Set.of("apple", "banana"));      // 2
            show("fruitCount", fruitCount);
            double intTotal = sum(List.of(5, 3));                      // 8.0
            show("intTotal", intTotal);
            double mixedTotal = sum(List.of(2.5, 1.5));                // 4.0
            show("mixedTotal", mixedTotal);
            List<Number> target = new ArrayList<>();
            addDefaults(target);
            int added = target.size();                                 // 2
            show("added", added);
        }
        {
            List<String> names = new ArrayList<>();
            List<Integer> counts = new ArrayList<>();
            boolean sameClass = names.getClass() == counts.getClass();      // true
            show("sameClass", sameClass);
            String className = names.getClass().getName();                  // "java.util.ArrayList"
            show("className", className);
        }
        {
            Object value = List.of("apple", "banana");
            boolean isList = value instanceof List<?>;                                   // true
            show("isList", isList);
            int size = value instanceof List<?> list ? list.size() : 0;                  // 2
            show("size", size);
        }
        {
            String superType = FruitList.class.getGenericSuperclass().getTypeName();     // "java.util.ArrayList<java.lang.String>"
            show("superType", superType);
        }
        {
            List<Integer> ids = new ArrayList<>(List.of(5, 3));
            int firstId = ids.get(0);                                       // 5
            show("firstId", firstId);
            int idSum = ids.stream().mapToInt(Integer::intValue).sum();     // 8
            show("idSum", idSum);
        }
        {
            Pool<StringBuilder> builders = new Pool<>(StringBuilder::new);
            StringBuilder builder = builders.create().append("apple");      // apple
            show("builder", builder);
            int poolSize = builders.size();                                 // 1
            show("poolSize", poolSize);
            String[] fruitArray = List.of("apple", "banana").toArray(String[]::new);   // [apple, banana]
            show("fruitArray", fruitArray);
        }
        {
            List<String> checked = Collections.checkedList(new ArrayList<>(), String.class);
            List raw = checked;
            try { raw.add(42);  } catch (Throwable _t) { System.out.println("-> " + _t); }
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
