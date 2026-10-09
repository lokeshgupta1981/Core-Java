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
 * Examples for the tutorial "Palindrome Program in Java: Check Strings and Numbers".
 * https://howtodoinjava.com/java/puzzles/java-string-palindrome-number-palindrome/
 */
public class Palindrome {
    static boolean isPalindrome(String s) {
        if (s == null) {
            return false;
        }
        for (int left = 0, right = s.length() - 1; left < right; left++, right--) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;               // first mismatch decides
            }
        }
        return true;
    }
    static boolean isPalindromePhrase(String s) {
        if (s == null) {
            return false;
        }
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            char l = s.charAt(left);
            char r = s.charAt(right);
            if (!Character.isLetterOrDigit(l)) {
                left++;
            } else if (!Character.isLetterOrDigit(r)) {
                right--;
            } else if (Character.toLowerCase(l) != Character.toLowerCase(r)) {
                return false;
            } else {
                left++;
                right--;
            }
        }
        return true;
    }
    static boolean isPalindromeRecursive(String s, int left, int right) {
        if (left >= right) {
            return true;                    // base case
        }
        if (s.charAt(left) != s.charAt(right)) {
            return false;
        }
        return isPalindromeRecursive(s, left + 1, right - 1);
    }
    static boolean isPalindromeNumber(int n) {
        if (n < 0 || (n % 10 == 0 && n != 0)) {
            return false;                   // negatives and numbers ending in 0
        }
        int reversedHalf = 0;
        while (n > reversedHalf) {
            reversedHalf = reversedHalf * 10 + n % 10;
            n /= 10;
        }
        return n == reversedHalf || n == reversedHalf / 10;   // even or odd digit count
    }
    static boolean isReverseComplementPalindrome(String dna) {
        for (int left = 0, right = dna.length() - 1; left <= right; left++, right--) {
            char expected = switch (dna.charAt(left)) {
                case 'A' -> 'T';
                case 'T' -> 'A';
                case 'G' -> 'C';
                case 'C' -> 'G';
                default -> throw new IllegalArgumentException("Not a DNA base: " + dna.charAt(left));
            };
            if (dna.charAt(right) != expected) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) throws Exception {
        {
            boolean level = isPalindrome("level");                                 // true
            show("level", level);
            boolean java = isPalindrome("java");                                   // false
            show("java", java);
            boolean phrase = isPalindromePhrase("A man, a plan, a canal: Panama"); // true
            show("phrase", phrase);
            boolean number = isPalindromeNumber(1221);                             // true
            show("number", number);
        }
        {
            boolean racecar = isPalindrome("racecar");   // true
            show("racecar", racecar);
            boolean noon = isPalindrome("noon");         // true
            show("noon", noon);
            boolean single = isPalindrome("x");          // true
            show("single", single);
            boolean empty = isPalindrome("");            // true
            show("empty", empty);
            boolean nothing = isPalindrome(null);        // false
            show("nothing", nothing);
        }
        {
            String word = "Madam";
            boolean exact = word.equals(new StringBuilder(word).reverse().toString());              // false
            show("exact", exact);
            boolean ignoreCase = word.equalsIgnoreCase(new StringBuilder(word).reverse().toString());   // true
            show("ignoreCase", ignoreCase);
        }
        {
            boolean car = isPalindromePhrase("Was it a car or a cat I saw?");   // true
            show("car", car);
            boolean nixon = isPalindromePhrase("No 'x' in Nixon");               // true
            show("nixon", nixon);
            boolean almost = isPalindromePhrase("Was it a car or a dog I saw?"); // false
            show("almost", almost);
        }
        {
            String cleaned = "Step on no pets!".replaceAll("[^A-Za-z0-9]", "").toLowerCase();   // "steponnopets"
            show("cleaned", cleaned);
            boolean pets = isPalindrome(cleaned);                                              // true
            show("pets", pets);
        }
        {
            String kayak = "kayak";
            boolean recKayak = isPalindromeRecursive(kayak, 0, kayak.length() - 1);   // true
            show("recKayak", recKayak);
            String howto = "howtodoinjava";
            boolean recHowto = isPalindromeRecursive(howto, 0, howto.length() - 1);   // false
            show("recHowto", recHowto);
        }
        {
            String radar = "radar";
            boolean streamRadar = IntStream.range(0, radar.length() / 2).allMatch(i -> radar.charAt(i) == radar.charAt(radar.length() - 1 - i));   // true
            show("streamRadar", streamRadar);
        }
        {
            boolean even = isPalindromeNumber(1221);        // true
            show("even", even);
            boolean oddDigits = isPalindromeNumber(12321);  // true
            show("oddDigits", oddDigits);
            boolean negative = isPalindromeNumber(-121);    // false
            show("negative", negative);
            boolean tens = isPalindromeNumber(10);          // false
            show("tens", tens);
            boolean zero = isPalindromeNumber(0);           // true
            show("zero", zero);
            boolean maxInt = isPalindromeNumber(Integer.MAX_VALUE);   // false
            show("maxInt", maxInt);
        }
        {
            int withUnderscores = 1_00_00_001;                          // 10000001
            show("withUnderscores", withUnderscores);
            boolean underscoreCheck = isPalindromeNumber(withUnderscores);   // true
            show("underscoreCheck", underscoreCheck);
            boolean viaString = isPalindrome(String.valueOf(1_00_00_001));   // true
            show("viaString", viaString);
        }
        {
            boolean ecoRI = isReverseComplementPalindrome("GAATTC");   // true
            show("ecoRI", ecoRI);
            boolean plain = isPalindrome("GAATTC");                     // false
            show("plain", plain);
            boolean other = isReverseComplementPalindrome("GATTTC");   // false
            show("other", other);
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
