package com.howtodoinjava.core.streams;

import java.util.regex.*;
import java.net.*;
import java.text.*;
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
 * Examples for the tutorial "Functional Interface in Java: Rules, JDK Types and Generics".
 * https://howtodoinjava.com/java/stream/functional-interface-tutorial/
 */
public class FunctionalInterfaceExamples {
    @FunctionalInterface
    static interface Discount {
        int apply(int price);

        default Discount then(Discount next) {
            return price -> next.apply(apply(price));
        }

        static Discount none() {
            return price -> price;
        }
    }
    @FunctionalInterface
    static interface Combiner<T> {
        T combine(T first, T second);
    }
    @FunctionalInterface
    static interface NumberCombiner<T extends Number> {
        T combine(T first, T second);
    }
    @FunctionalInterface
    static interface IntCombiner extends Combiner<Integer> {
    }
    static interface Identity {
        <T> T same(T value);
    }
    @FunctionalInterface
    static interface ThrowingFunction<T, R, E extends Exception> {
        R apply(T t) throws E;
    }
    static <T, R> Function<T, R> unchecked(ThrowingFunction<T, R, ?> f) {
        return t -> {
            try {
                return f.apply(t);
            } catch (Exception e) {
                throw new IllegalArgumentException("Cannot convert " + t, e);
            }
        };
    }
    static record Order(int subtotal, boolean member) {}
    public static void main(String[] args) throws Exception {
        {
            Function<Integer, Integer> square = x -> x * x;
            int squared = square.apply(5);                          // 25
            show("squared", squared);
            Predicate<String> isBlank = String::isBlank;
            boolean blank = isBlank.test("  ");                     // true
            show("blank", blank);
            Supplier<List<String>> newCart = ArrayList::new;
            List<String> cart = newCart.get();                      // []
            show("cart", cart);
            Consumer<String> addToCart = cart::add;
            addToCart.accept("pizza");
            int items = cart.size();                                // 1
            show("items", items);
            BinaryOperator<Integer> add = Integer::sum;
            int total = add.apply(120, 80);                         // 200
            show("total", total);
        }
        {
            Discount tenOff = price -> price - 10;
            Discount half = price -> price / 2;
            int once = tenOff.apply(250);                           // 240
            show("once", once);
            int chained = tenOff.then(half).apply(250);             // 120
            show("chained", chained);
            int unchanged = Discount.none().apply(250);             // 250
            show("unchanged", unchanged);
        }
        {
            List<String> dishes = new ArrayList<>(List.of("pizza", "tea", "noodles"));
            Comparator<String> byLength = (a, b) -> Integer.compare(a.length(), b.length());
            int cmp = byLength.compare("tea", "pizza");             // -1
            show("cmp", cmp);
            dishes.sort(byLength.thenComparing(Comparator.reverseOrder()));
            List<String> sorted = dishes;                           // [tea, pizza, noodles]
            show("sorted", sorted);
        }
        {
            List<String> cart = new ArrayList<>();
            BiConsumer<List<String>, String> addTo = List::add;
            addTo.accept(cart, "pizza");
            addTo.accept(cart, "salad");
            List<String> items = cart;                              // [pizza, salad]
            show("items", items);
            BiPredicate<String, Integer> longerThan = (s, n) -> s.length() > n;
            boolean longName = longerThan.test("pizza", 3);         // true
            show("longName", longName);
            Function<String, Integer> nameLength = String::length;
            int length = nameLength.apply("salad");                 // 5
            show("length", length);
            BiFunction<Integer, Integer, Integer> lineTotal = (price, qty) -> price * qty;
            int line = lineTotal.apply(120, 3);                     // 360
            show("line", line);
            UnaryOperator<String> shout = String::toUpperCase;
            String upper = shout.apply("pizza");                    // "PIZZA"
            show("upper", upper);
            BinaryOperator<Integer> cheaper = BinaryOperator.minBy(Comparator.naturalOrder());
            int cheapest = cheaper.apply(120, 90);                  // 90
            show("cheapest", cheapest);
        }
        {
            IntPredicate isEven = n -> n % 2 == 0;
            boolean even = isEven.test(4);                          // true
            show("even", even);
            ToIntFunction<String> letters = String::length;
            int count = letters.applyAsInt("noodles");              // 7
            show("count", count);
            IntBinaryOperator plus = Integer::sum;
            int sum = plus.applyAsInt(2, 3);                        // 5
            show("sum", sum);
            IntFunction<String> label = n -> "Table " + n;
            String table = label.apply(4);                          // "Table 4"
            show("table", table);
        }
        {
            Function<Integer, Integer> addDelivery = p -> p + 40;
            Function<Integer, Integer> halfPrice = p -> p / 2;
            int deliveryFirst = addDelivery.andThen(halfPrice).apply(200);   // 120
            show("deliveryFirst", deliveryFirst);
            int halfFirst = addDelivery.compose(halfPrice).apply(200);       // 140
            show("halfFirst", halfFirst);

            Predicate<String> isVeg = dish -> dish.startsWith("veg");
            Predicate<String> isSpicy = dish -> dish.contains("spicy");
            boolean mildVeg = isVeg.and(isSpicy.negate()).test("veg curry");     // true
            show("mildVeg", mildVeg);
            boolean either = isVeg.or(isSpicy).test("spicy wings");              // true
            show("either", either);
            boolean notVeg = Predicate.not(isVeg).test("chicken");               // true
            show("notVeg", notVeg);
        }
        {
            Combiner<Integer> multiply = (a, b) -> a * b;
            int product = multiply.combine(2, 3);                   // 6
            show("product", product);
            Combiner<String> join = (a, b) -> a + " " + b;
            String text = join.combine("Hello", "World");           // "Hello World"
            show("text", text);
        }
        {
            NumberCombiner<Double> times = (a, b) -> a * b;
            double area = times.combine(4.0, 6.0);                  // 24.0
            show("area", area);
            NumberCombiner<Integer> larger = (a, b) -> a.doubleValue() >= b.doubleValue() ? a : b;
            int max = larger.combine(7, 3);                         // 7
            show("max", max);
        }
        {
            IntCombiner addInts = Integer::sum;
            int nine = addInts.combine(4, 5);                       // 9
            show("nine", nine);
        }
        {
            Identity ref = Objects::requireNonNull;
            String tea = ref.same("tea");                           // "tea"
            show("tea", tea);
        }
        {
            Function<String, URI> toUri = unchecked(URI::new);
            URI menu = toUri.apply("https://example.com/menu");    // https://example.com/menu
            show("menu", menu);
            try { URI bad = toUri.apply("not a uri"); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            Order order = new Order(600, true);
            Predicate<Order> isMember = Order::member;
            Predicate<Order> bigOrder = o -> o.subtotal() >= 500;
            boolean memberDeal = isMember.and(bigOrder).test(order);   // true
            show("memberDeal", memberDeal);

            UnaryOperator<Integer> coupon = p -> p - 50;
            UnaryOperator<Integer> memberOff = p -> p * 90 / 100;
            UnaryOperator<Integer> deliveryFee = p -> p < 500 ? p + 40 : p;
            Function<Integer, Integer> pricing = coupon.andThen(memberOff).andThen(deliveryFee);
            int toPay = pricing.apply(order.subtotal());            // 535
            show("toPay", toPay);
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
