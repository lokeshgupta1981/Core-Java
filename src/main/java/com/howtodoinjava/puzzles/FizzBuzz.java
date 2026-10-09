package com.howtodoinjava.puzzles;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "FizzBuzz Program in Java: Loop, Streams and Switch Solutions".
 * https://howtodoinjava.com/java/puzzles/fizzbuzz-solution-java/
 */
public class FizzBuzz {
    static String wrongOrder(int n) {
        if (n % 3 == 0) {
            return "Fizz";
        } else if (n % 5 == 0) {
            return "Buzz";
        } else if (n % 15 == 0) {
            return "FizzBuzz";              // never reached
        }
        return String.valueOf(n);
    }
    static String fizzBuzz(int n) {
        if (n % 15 == 0) {
            return "FizzBuzz";
        } else if (n % 3 == 0) {
            return "Fizz";
        } else if (n % 5 == 0) {
            return "Buzz";
        }
        return String.valueOf(n);
    }
    static List<String> fizzBuzzUpTo(int n) {
        List<String> result = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            result.add(fizzBuzz(i));
        }
        return result;
    }
    static String fizzBuzzConcat(int n) {
        StringBuilder word = new StringBuilder();
        if (n % 3 == 0) {
            word.append("Fizz");
        }
        if (n % 5 == 0) {
            word.append("Buzz");
        }
        return word.isEmpty() ? String.valueOf(n) : word.toString();
    }
    static String fizzBuzzSwitch(int n) {
        return switch (Math.floorMod(n, 15)) {
            case 0 -> "FizzBuzz";
            case 3, 6, 9, 12 -> "Fizz";
            case 5, 10 -> "Buzz";
            default -> String.valueOf(n);
        };
    }
    static List<String> fizzBuzzCounters(int n) {
        List<String> result = new ArrayList<>();
        int three = 0;
        int five = 0;
        for (int i = 1; i <= n; i++) {
            three++;
            five++;
            String word = "";
            if (three == 3) {
                word += "Fizz";
                three = 0;
            }
            if (five == 5) {
                word += "Buzz";
                five = 0;
            }
            result.add(word.isEmpty() ? String.valueOf(i) : word);
        }
        return result;
    }
    static record Rule(int divisor, String word) {}
    static String applyRules(int n, List<Rule> rules) {
        StringBuilder text = new StringBuilder();
        for (Rule rule : rules) {
            if (n % rule.divisor() == 0) {
                text.append(rule.word());
            }
        }
        return text.isEmpty() ? String.valueOf(n) : text.toString();
    }
    public static void main(String[] args) throws Exception {
        {
            String seven = fizzBuzz(7);     // "7"
            show("seven", seven);
            String nine = fizzBuzz(9);      // "Fizz"
            show("nine", nine);
            String ten = fizzBuzz(10);      // "Buzz"
            show("ten", ten);
            String thirty = fizzBuzz(30);   // "FizzBuzz"
            show("thirty", thirty);
        }
        {
            String fifteen = wrongOrder(15);   // "Fizz", should be "FizzBuzz"
            show("fifteen", fifteen);
        }
        {
            List<String> first15 = fizzBuzzUpTo(15);   // [1, 2, Fizz, 4, Buzz, Fizz, 7, 8, Fizz, Buzz, 11, Fizz, 13, 14, FizzBuzz]
            show("first15", first15);
            List<String> none = fizzBuzzUpTo(0);       // []
            show("none", none);
        }
        {
            String both = fizzBuzzConcat(45);   // "FizzBuzz"
            show("both", both);
            String plain = fizzBuzzConcat(8);   // "8"
            show("plain", plain);
        }
        {
            List<String> words = IntStream.rangeClosed(1, 15).mapToObj(i -> fizzBuzz(i)).toList();   // [1, 2, Fizz, 4, Buzz, Fizz, 7, 8, Fizz, Buzz, 11, Fizz, 13, 14, FizzBuzz]
            show("words", words);
            String line = IntStream.rangeClosed(1, 5).mapToObj(i -> fizzBuzz(i)).collect(Collectors.joining(" "));   // "1 2 Fizz 4 Buzz"
            show("line", line);
        }
        {
            List<String> ternary = IntStream.rangeClosed(9, 10).mapToObj(i -> i % 15 == 0 ? "FizzBuzz" : i % 3 == 0 ? "Fizz" : i % 5 == 0 ? "Buzz" : String.valueOf(i)).toList();   // [Fizz, Buzz]
            show("ternary", ternary);
        }
        {
            String switch12 = fizzBuzzSwitch(12);   // "Fizz"
            show("switch12", switch12);
            String negative = fizzBuzzSwitch(-3);   // "Fizz"
            show("negative", negative);
            int remainder = -3 % 15;                // -3
            show("remainder", remainder);
            int floorMod = Math.floorMod(-3, 15);   // 12
            show("floorMod", floorMod);
        }
        {
            List<String> counted = fizzBuzzCounters(15);   // [1, 2, Fizz, 4, Buzz, Fizz, 7, 8, Fizz, Buzz, 11, Fizz, 13, 14, FizzBuzz]
            show("counted", counted);
        }
        {
            List<Rule> classic = List.of(new Rule(3, "Fizz"), new Rule(5, "Buzz"));
            List<Rule> fiveSeven = List.of(new Rule(5, "Fizz"), new Rule(7, "Buzz"));
            String c15 = applyRules(15, classic);       // "FizzBuzz"
            show("c15", c15);
            String s35 = applyRules(35, fiveSeven);     // "FizzBuzz"
            show("s35", s35);
            String s14 = applyRules(14, fiveSeven);     // "Buzz"
            show("s14", s14);
        }
        {
            List<Rule> batch = List.of(new Rule(100, "log "), new Rule(500, "commit "));
            String row300 = applyRules(300, batch);      // "log "
            show("row300", row300);
            String row1500 = applyRules(1500, batch);    // "log commit "
            show("row1500", row1500);
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
