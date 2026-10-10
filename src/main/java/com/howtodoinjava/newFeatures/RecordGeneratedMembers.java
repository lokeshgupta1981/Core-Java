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
import java.lang.reflect.*;

/**
 * Examples for the tutorial "Java Record Type: Syntax, Constructors and Examples".
 * https://howtodoinjava.com/java/java-record-type/
 */
public class RecordGeneratedMembers {
    static record Ingredient(String name, int grams) {}
    static public record Product(long id, String title, double price) {}
    static record Temperature(double celsius, String sensor) {
        Temperature(double celsius, String sensor) {
            if (celsius < -273.15) {
                throw new IllegalArgumentException("Below absolute zero: " + celsius);
            }
            this.celsius = celsius;
            this.sensor = Objects.requireNonNull(sensor);
        }
    }
    static record Email(String address) {
        Email {
            Objects.requireNonNull(address, "address");
            address = address.strip().toLowerCase();
            if (!address.contains("@")) {
                throw new IllegalArgumentException("Not an email: " + address);
            }
        }
    }
    static record Portion(String food, int grams) {
        Portion(String food) {
            this(food, 100);
        }
    }
    static record Money(BigDecimal amount, String currency) {
        static final Money ZERO_EUR = new Money(BigDecimal.ZERO, "EUR");

        static Money eur(String amount) {
            return new Money(new BigDecimal(amount), "EUR");
        }

        Money plus(Money other) {
            if (!currency.equals(other.currency)) {
                throw new IllegalArgumentException("Currency mismatch");
            }
            return new Money(amount.add(other.amount), currency);
        }
    }
    static record ApiResponse<T>(int status, T body) {}
    static record Score(String player, int points) implements Comparable<Score> {
        @Override
        public int compareTo(Score other) {
            return Integer.compare(points, other.points);
        }
    }
    static List<String> topItems(Map<String, Integer> sales) {
        record Sale(String item, int count) {}
        return sales.entrySet().stream()
                .map(e -> new Sale(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingInt(Sale::count).reversed())
                .limit(2)
                .map(Sale::item)
                .toList();
    }
    static record RecipeV1(String title, List<String> steps) {}
    static record Recipe(String title, List<String> steps) {
        Recipe {
            steps = List.copyOf(steps);
        }
    }
    static record Pixels(int[] values) {}
    static record Serving(String dish, int grams) implements Serializable {
        Serving {
            if (grams <= 0) {
                throw new IllegalArgumentException("grams must be positive");
            }
        }
    }
    static byte[] serialize(Object value) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(value);
        }
        return bytes.toByteArray();
    }
    static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return in.readObject();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Ingredient flour = new Ingredient("flour", 200);
            String name = flour.name();                                        // "flour"
            show("name", name);
            int grams = flour.grams();                                         // 200
            show("grams", grams);
            String text = flour.toString();                                    // "Ingredient[name=flour, grams=200]"
            show("text", text);
            boolean same = flour.equals(new Ingredient("flour", 200));         // true
            show("same", same);
            boolean sameHash = flour.hashCode() == new Ingredient("flour", 200).hashCode(); // true
            show("sameHash", sameHash);
        }
        {
            Product book = new Product(1L, "Java Basics", 29.99);
            Product copy = new Product(1L, "Java Basics", 29.99);
            Product other = new Product(2L, "Java Basics", 29.99);
            boolean equalProducts = book.equals(copy);                         // true
            show("equalProducts", equalProducts);
            boolean differentId = book.equals(other);                          // false
            show("differentId", differentId);
            String bookText = book.toString();                                 // "Product[id=1, title=Java Basics, price=29.99]"
            show("bookText", bookText);
        }
        {
            Email email = new Email("  Lokesh@Example.com ");
            String normalized = email.address();                               // "lokesh@example.com"
            show("normalized", normalized);
            try { Email broken = new Email("lokesh"); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            Portion rice = new Portion("rice");
            int defaultGrams = rice.grams();                                   // 100
            show("defaultGrams", defaultGrams);
        }
        {
            Money total = Money.eur("10.50").plus(Money.eur("4.25"));
            BigDecimal sum = total.amount();                                   // 14.75
            show("sum", sum);
            boolean zeroIsEur = Money.ZERO_EUR.currency().equals("EUR");       // true
            show("zeroIsEur", zeroIsEur);
        }
        {
            ApiResponse<String> greeting = new ApiResponse<>(200, "hello");
            ApiResponse<List<Integer>> ids = new ApiResponse<>(200, List.of(4, 8));
            String body = greeting.body();                                     // "hello"
            show("body", body);
            Integer firstId = ids.body().getFirst();                           // 4
            show("firstId", firstId);
        }
        {
            List<Score> scores = new ArrayList<>(List.of(new Score("anna", 7), new Score("bob", 3)));
            Collections.sort(scores);
            String lowest = scores.getFirst().player();                        // "bob"
            show("lowest", lowest);
        }
        {
            List<String> best = topItems(Map.of("tea", 5, "cake", 9, "soup", 2)); // [cake, tea]
            show("best", best);
        }
        {
            List<String> steps = new ArrayList<>(List.of("mix", "bake"));
            RecipeV1 cake = new RecipeV1("cake", steps);
            steps.add("burn");
            int stepCount = cake.steps().size();                               // 3
            show("stepCount", stepCount);
        }
        {
            List<String> input = new ArrayList<>(List.of("mix", "bake"));
            Recipe bread = new Recipe("bread", input);
            input.add("burn");
            int breadSteps = bread.steps().size();                             // 2
            show("breadSteps", breadSteps);
            try { boolean added = bread.steps().add("eat"); show("added", added); } catch (Throwable _t) { System.out.println("added -> " + _t); }
        }
        {
            boolean equalArrays = new Pixels(new int[]{1, 2}).equals(new Pixels(new int[]{1, 2})); // false
            show("equalArrays", equalArrays);
        }
        {
            Serving soup = new Serving("soup", 300);
            Object restored = deserialize(serialize(soup));
            boolean equalAfterRoundTrip = soup.equals(restored);               // true
            show("equalAfterRoundTrip", equalAfterRoundTrip);
            long uid = ObjectStreamClass.lookup(Serving.class).getSerialVersionUID(); // 0
            show("uid", uid);
        }
        {
            boolean isRecord = Ingredient.class.isRecord();                   // true
            show("isRecord", isRecord);
            RecordComponent[] parts = Ingredient.class.getRecordComponents();
            String firstComponent = parts[0].getName();                        // "name"
            show("firstComponent", firstComponent);
            Class<?> secondType = parts[1].getType();                          // int
            show("secondType", secondType);
        }
        {
            Object item = new Ingredient("sugar", 50);
            String label = "";
            if (item instanceof Ingredient(String what, int weight)) {
                label = weight + "g " + what;
            }
            String matched = label;                                            // "50g sugar"
            show("matched", matched);
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
