package com.howtodoinjava.core.basic;

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

/**
 * Examples for the tutorial "Java instanceof Operator and Pattern Matching Explained".
 * https://howtodoinjava.com/java/oops/java-instanceof/
 */
public class InstanceofOperatorTypeCheck {
    static boolean isBlankText(Object input) {
        return input instanceof String text && text.isBlank();
    }
    // before Java 16
    static int oldLength(Object value) {
        if (value instanceof String) {
            String s = (String) value;
            return s.length();
        }
        return 0;
    }
    // Java 16 and later
    static int newLength(Object value) {
        if (value instanceof String s) {
            return s.length();
        }
        return 0;
    }
    static String describe(Object input) {
        if (!(input instanceof Integer number)) {
            return "not a number";
        }
        return number > 100 ? "large" : "small";   // number is in scope here
    }
    static class CartItem {
        private final String sku;
        private final int quantity;

        CartItem(String sku, int quantity) {
            this.sku = sku;
            this.quantity = quantity;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof CartItem item
                    && sku.equals(item.sku)
                    && quantity == item.quantity;
        }

        @Override
        public int hashCode() {
            return Objects.hash(sku, quantity);
        }
    }
    static sealed interface Message permits Email, Sms, Push {}
    static record Email(String to, String subject) implements Message {}
    static record Sms(String phone, String text) implements Message {}
    static record Push(String device) implements Message {}
    static String route(Message message) {
        return switch (message) {
            case Email e -> "mail to " + e.to();
            case Sms(String phone, String text) when text.length() > 160 -> "split sms to " + phone;
            case Sms(String phone, String text) -> "sms to " + phone;
            case Push p -> "push to " + p.device();
        };
    }
    public static void main(String[] args) throws Exception {
        {
            Object value = "hello";
            boolean isText = value instanceof String;          // true
            show("isText", isText);
            boolean isChars = value instanceof CharSequence;   // true, String implements it
            show("isChars", isChars);
            boolean isNumber = value instanceof Integer;       // false
            show("isNumber", isNumber);
            Object missing = null;
            boolean nullCheck = missing instanceof String;     // false, never throws
            show("nullCheck", nullCheck);
            int length = value instanceof String s ? s.length() : 0;  // 5
            show("length", length);
        }
        {
            List<String> tags = new ArrayList<>();
            boolean isList = tags instanceof List;             // true
            show("isList", isList);
            boolean isCollection = tags instanceof Collection; // true
            show("isCollection", isCollection);
            boolean isAbstract = tags instanceof AbstractList; // true
            show("isAbstract", isAbstract);
            boolean isRandom = tags instanceof RandomAccess;   // true
            show("isRandom", isRandom);
            boolean isLinked = tags instanceof LinkedList;     // false
            show("isLinked", isLinked);
        }
        {
            boolean blank = isBlankText("   ");          // true
            show("blank", blank);
            boolean notText = isBlankText(42);           // false
            show("notText", notText);
            boolean nothing = isBlankText(null);         // false
            show("nothing", nothing);
        }
        {
            int before = oldLength("tea");                // 3
            show("before", before);
            int after = newLength("coffee");             // 6
            show("after", after);
            int other = newLength(3.5);                  // 0
            show("other", other);
        }
        {
            String big = describe(250);                 // "large"
            show("big", big);
            String text = describe("250");              // "not a number"
            show("text", text);
        }
        {
            CartItem first = new CartItem("pen", 2);
            boolean same = first.equals(new CartItem("pen", 2));  // true
            show("same", same);
            boolean otherType = first.equals("pen");              // false
            show("otherType", otherType);
            boolean nullSafe = first.equals(null);                // false
            show("nullSafe", nullSafe);
        }
        {
            int[] scores = {90, 75};
            boolean primitive = scores instanceof int[];        // true
            show("primitive", primitive);
            boolean asObject = scores instanceof Object;        // true
            show("asObject", asObject);
            Integer[] boxed = {90, 75};
            boolean numbers = boxed instanceof Number[];        // true
            show("numbers", numbers);
            boolean objects = boxed instanceof Object[];        // true
            show("objects", objects);
        }
        {
            Object payload = List.of("a", "b");
            boolean anyList = payload instanceof List<?>;       // true
            show("anyList", anyList);
            List<String> names = new ArrayList<>(List.of("x"));
            boolean safe = names instanceof ArrayList<String> list && list.size() == 1;  // true
            show("safe", safe);
        }
        {
            String mail = route(new Email("ana@example.com", "Hi"));  // "mail to ana@example.com"
            show("mail", mail);
            String sms = route(new Sms("555-0100", "On my way"));      // "sms to 555-0100"
            show("sms", sms);
            String push = route(new Push("pixel"));                    // "push to pixel"
            show("push", push);
        }
        {
            Object number = 7;
            boolean pattern = number instanceof Number;              // true
            show("pattern", pattern);
            boolean exact = number.getClass() == Number.class;       // false, class is Integer
            show("exact", exact);
            Class<?> wanted = Number.class;
            boolean dynamic = wanted.isInstance(number);             // true
            show("dynamic", dynamic);
            List<Object> mixed = List.of(1, "two", 3.0);
            List<Integer> ints = mixed.stream().filter(Integer.class::isInstance).map(Integer.class::cast).toList();  // [1]
            show("ints", ints);
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
