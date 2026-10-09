package com.howtodoinjava.algorithms;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import org.apache.commons.codec.language.Soundex;
import java.text.Normalizer;

/**
 * Examples for the tutorial "Soundex Algorithm in Java".
 * https://howtodoinjava.com/algorithm/implement-phonetic-search-using-soundex-algorithm/
 */
public class SoundexAlgorithm {
    static String soundex(String name) {
        if (name == null) {
            return null;
        }
        StringBuilder letters = new StringBuilder();
        for (char c : name.toUpperCase(Locale.ROOT).toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                letters.append(c);           // drop spaces, ' and -
            }
        }
        if (letters.isEmpty()) {
            return "";
        }
        StringBuilder code = new StringBuilder().append(letters.charAt(0));
        char last = digit(letters.charAt(0));
        for (int i = 1; i < letters.length() && code.length() < 4; i++) {
            char c = letters.charAt(i);
            if (c == 'H' || c == 'W') {
                continue;                   // no separator
            }
            char d = digit(c);
            if (d != '0' && d != last) {
                code.append(d);
            }
            last = d;                       // a vowel sets last to '0'
        }
        while (code.length() < 4) {
            code.append('0');
        }
        return code.toString();
    }

    static char digit(char c) {
        return switch (c) {
            case 'B', 'F', 'P', 'V' -> '1';
            case 'C', 'G', 'J', 'K', 'Q', 'S', 'X', 'Z' -> '2';
            case 'D', 'T' -> '3';
            case 'L' -> '4';
            case 'M', 'N' -> '5';
            case 'R' -> '6';
            default -> '0';                 // vowels
        };
    }
    public static void main(String[] args) throws Exception {
        {
            String robert = soundex("Robert");      // "R163"
            show("robert", robert);
            String rupert = soundex("Rupert");      // "R163"
            show("rupert", rupert);
            String ashcraft = soundex("Ashcraft");  // "A261"
            show("ashcraft", ashcraft);
            String lee = soundex("Lee");            // "L000"
            show("lee", lee);
            boolean sameSound = soundex("Smith").equals(soundex("Smyth"));   // true
            show("sameSound", sameSound);
        }
        {
            String pfister = soundex("Pfister");    // "P236"
            show("pfister", pfister);
            String tymczak = soundex("Tymczak");    // "T522"
            show("tymczak", tymczak);
            String obrien = soundex("O'Brien");     // "O165"
            show("obrien", obrien);
            String blank = soundex("");             // ""
            show("blank", blank);
            String none = soundex(null);            // null
            show("none", none);
        }
        {
            Soundex sx = new Soundex();
            String code = sx.soundex("Ashcraft");                    // "A261"
            show("code", code);
            int similarity = sx.difference("Robert", "Rupert");      // 4
            show("similarity", similarity);
            int different = sx.difference("Smith", "Jones");         // 2
            show("different", different);
            String simplified = Soundex.US_ENGLISH_SIMPLIFIED.soundex("Ashcraft");   // "A226"
            show("simplified", simplified);
        }
        {
            String plain = Normalizer.normalize("M\u00fcller", Normalizer.Form.NFD).replaceAll("\\p{M}", "");   // "Muller"
            show("plain", plain);
            String muller = new Soundex().soundex(plain);   // "M460"
            show("muller", muller);
        }
        {
            List<String> patients = List.of("Johnson", "Johnston", "Jansen", "Jackson", "Peters");
            String typed = "Jonson";
            List<String> matches = patients.stream()
                    .filter(p -> soundex(p).equals(soundex(typed)))
                    .toList();                      // [Johnson, Jansen]
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
