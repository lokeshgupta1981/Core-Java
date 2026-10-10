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
 * Examples for the tutorial "Java Stream contains(), containsAny() and containsAll()".
 * https://howtodoinjava.com/java/stream/contains-containsany-containsall/
 */
public class StreamContainsChecks {
    static <T> boolean contains(Stream<? extends T> stream, T element) {
        return stream.anyMatch(Predicate.isEqual(element));
    }
    static <T> boolean containsAny(Stream<? extends T> stream, Collection<? extends T> candidates) {
        Set<T> wanted = new HashSet<>(candidates);
        return stream.anyMatch(wanted::contains);
    }
    @SafeVarargs
    static <T> boolean containsAny(Stream<? extends T> stream, T... candidates) {
        return containsAny(stream, Arrays.asList(candidates));
    }
    static <T> boolean containsAll(Stream<? extends T> stream, Collection<? extends T> candidates) {
        Set<T> present = stream.collect(Collectors.toSet());
        return present.containsAll(candidates);
    }
    @SafeVarargs
    static <T> boolean containsAll(Stream<? extends T> stream, T... candidates) {
        return containsAll(stream, Arrays.asList(candidates));
    }
    static record Topping(String name, boolean vegetarian) {}
    static Stream<String> roles(String header) {
        return Pattern.compile(",").splitAsStream(header == null ? "" : header)
                .map(String::strip)
                .filter(r -> !r.isEmpty());
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> toppings = List.of("cheese", "olive", "basil", "ham");

            boolean hasOlive = toppings.stream().anyMatch("olive"::equals);                     // true
            show("hasOlive", hasOlive);

            Set<String> meats = Set.of("ham", "salami");
            boolean anyMeat = toppings.stream().anyMatch(meats::contains);                      // true
            show("anyMeat", anyMeat);

            Set<String> required = Set.of("cheese", "basil");
            boolean hasAll = toppings.stream().collect(Collectors.toSet()).containsAll(required);   // true
            show("hasAll", hasAll);
        }
        {
            boolean olive = contains(Stream.of("cheese", "olive", "basil"), "olive");    // true
            show("olive", olive);
            boolean corn = contains(Stream.of("cheese", "olive", "basil"), "corn");      // false
            show("corn", corn);
            boolean infinite = contains(Stream.iterate(1, n -> n + 2), 7);               // true
            show("infinite", infinite);
        }
        {
            boolean meat = containsAny(Stream.of("cheese", "olive", "ham"), List.of("ham", "salami"));   // true
            show("meat", meat);
            boolean fish = containsAny(Stream.of("cheese", "olive", "ham"), "tuna", "anchovy");          // false
            show("fish", fish);
            boolean none = containsAny(Stream.of("cheese", "olive"), List.of());                         // false
            show("none", none);
        }
        {
            boolean margherita = containsAll(Stream.of("cheese", "tomato", "basil"), "cheese", "basil");   // true
            show("margherita", margherita);
            boolean hawaii = containsAll(Stream.of("cheese", "tomato", "basil"), "ham", "cheese");         // false
            show("hawaii", hawaii);
            boolean nothing = containsAll(Stream.of("cheese"), List.of());                                // true
            show("nothing", nothing);
        }
        {
            List<Topping> pizza = List.of(new Topping("cheese", true), new Topping("ham", false), new Topping("basil", true));

            boolean hasMeat = pizza.stream().anyMatch(t -> !t.vegetarian());                    // true
            show("hasMeat", hasMeat);
            boolean allVeg = pizza.stream().allMatch(Topping::vegetarian);                      // false
            show("allVeg", allVeg);
            boolean noTuna = pizza.stream().noneMatch(t -> t.name().equals("tuna"));            // true
            show("noTuna", noTuna);
            boolean byName = containsAll(pizza.stream().map(Topping::name), "ham", "basil");    // true
            show("byName", byName);
        }
        {
            boolean anyEmpty = Stream.<String>empty().anyMatch(s -> true);           // false
            show("anyEmpty", anyEmpty);
            boolean allEmpty = Stream.<String>empty().allMatch(s -> false);         // true
            show("allEmpty", allEmpty);
            boolean nullFound = contains(Stream.of("cheese", null), null);          // true
            show("nullFound", nullFound);
            boolean nullAny = containsAny(Stream.of("cheese", null), "corn", null); // true
            show("nullAny", nullAny);
        }
        {
            Stream<String> toppings = Stream.of("cheese", "olive", "basil");
            boolean olive = toppings.anyMatch("olive"::equals);                  // true
            show("olive", olive);
            try { boolean basil = toppings.anyMatch("basil"::equals); show("basil", basil); } catch (Throwable _t) { System.out.println("basil -> " + _t); }
        }
        {
            Set<String> onPizza = Stream.of("cheese", "olive", "basil").collect(Collectors.toSet());
            boolean olive = onPizza.contains("olive");                           // true
            show("olive", olive);
            boolean both = onPizza.containsAll(List.of("cheese", "basil"));      // true
            show("both", both);
        }
        {
            boolean canPublish = containsAny(roles("viewer, editor"), "editor", "admin");       // true
            show("canPublish", canPublish);
            boolean canDelete = containsAll(roles("admin, editor"), "admin", "auditor");           // false
            show("canDelete", canDelete);
            boolean anonymous = containsAny(roles(null), "editor", "admin");                       // false
            show("anonymous", anonymous);
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
