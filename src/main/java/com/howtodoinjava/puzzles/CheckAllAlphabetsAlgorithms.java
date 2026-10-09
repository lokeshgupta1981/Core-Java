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
 * Examples for the tutorial "Check if a String Contains All Alphabets in Java (Pangram)".
 * https://howtodoinjava.com/java/puzzles/puzzle-check-if-string-is-complete-contains-all-alphabets/
 */
public class CheckAllAlphabetsAlgorithms {
    static boolean isPangram(String text) {
        if (text == null || text.length() < 26) {
            return false;                        // too short to hold 26 letters
        }
        boolean[] seen = new boolean[26];
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                c = (char) (c + ('a' - 'A'));    // ASCII upper to lower case
            }
            if (c >= 'a' && c <= 'z' && !seen[c - 'a']) {
                seen[c - 'a'] = true;
                count++;
                if (count == 26) {
                    return true;                 // stop early
                }
            }
        }
        return false;
    }
    static boolean isPangramBits(String text) {
        final int all = (1 << 26) - 1;
        int seen = 0;
        for (int i = 0; i < text.length() && seen != all; i++) {
            int c = text.charAt(i) | 0x20;       // lower case for ASCII letters
            if (c >= 'a' && c <= 'z') {
                seen |= 1 << (c - 'a');
            }
        }
        return seen == all;
    }
    static boolean isPangramStream(String text) {
        return text.chars()
                .filter(c -> (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                .map(Character::toLowerCase)
                .distinct()
                .count() == 26;
    }
    static boolean isPangramIndexOf(String text) {
        for (char c = 'a'; c <= 'z'; c++) {
            if (text.indexOf(c) < 0 && text.indexOf(Character.toUpperCase(c)) < 0) {
                return false;                    // c is missing
            }
        }
        return true;
    }
    static Pattern pangramPattern() {
        String lookaheads = IntStream.rangeClosed('a', 'z')
                .mapToObj(c -> "(?=.*" + (char) c + ")")
                .collect(Collectors.joining());
        return Pattern.compile("(?is)" + lookaheads + ".*");
    }
    static String missingLetters(String text) {
        boolean[] seen = new boolean[26];
        for (char c : text.toCharArray()) {
            int lower = c | 0x20;
            if (lower >= 'a' && lower <= 'z') {
                seen[lower - 'a'] = true;
            }
        }
        StringBuilder missing = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            if (!seen[i]) {
                missing.append((char) ('a' + i));
            }
        }
        return missing.toString();
    }
    public static void main(String[] args) throws Exception {
        {
            boolean fox = isPangram("The quick brown fox jumps over the lazy dog");   // true
            show("fox", fox);
            boolean shout = isPangram("PACK MY BOX WITH FIVE DOZEN LIQUOR JUGS");      // true
            show("shout", shout);
            boolean hello = isPangram("Hello, World");                                 // false
            show("hello", hello);
        }
        {
            String fox = "The quick brown fox jumps over the lazy dog";
            long letters = fox.chars().filter(Character::isLetter).count();   // 35
            show("letters", letters);
        }
        {
            boolean nothing = isPangram(null);                                         // false
            show("nothing", nothing);
            boolean alphabet = isPangram("abcdefghijklmnopqrstuvwxyz");                 // true
            show("alphabet", alphabet);
            boolean noZ = isPangram("The quick brown fox jumps over the lay dog");      // false
            show("noZ", noZ);
        }
        {
            char kelvinLower = Character.toLowerCase('\u212A');   // k, the Kelvin sign becomes a Latin k
            show("kelvinLower", kelvinLower);
            int badIndex = '\u00E9' - 'a';                        // 136, outside the 26 slots
            show("badIndex", badIndex);
        }
        {
            boolean bitsFox = isPangramBits("Sphinx of black quartz, judge my vow");   // true
            show("bitsFox", bitsFox);
            int allBits = (1 << 26) - 1;                                                 // 67108863
            show("allBits", allBits);
        }
        {
            boolean streamFox = isPangramStream("Jived fox nymph grabs quick waltz");   // true
            show("streamFox", streamFox);
        }
        {
            boolean indexFox = isPangramIndexOf("The five boxing wizards jump quickly");   // true
            show("indexFox", indexFox);
        }
        {
            Pattern pangram = pangramPattern();
            boolean regexFox = pangram.matcher("The quick brown fox\njumps over the lazy dog").matches();   // true
            show("regexFox", regexFox);
        }
        {
            boolean oneLine = "The quick brown fox\njumps over the lazy dog".matches("(?i)(?=.*a)(?=.*z).*");   // false, no DOTALL
            show("oneLine", oneLine);
        }
        {
            String gaps = missingLetters("Hello, World");                           // "abcfgijkmnpqstuvxyz"
            show("gaps", gaps);
            String noGaps = missingLetters("Pack my box with five dozen liquor jugs");  // ""
            show("noGaps", noGaps);
        }
        {
            String word = "Mr Jock, TV quiz PhD, bags few lynx";
            boolean perfect = isPangram(word) && word.chars().filter(Character::isLetter).count() == 26;   // true
            show("perfect", perfect);
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
