package com.howtodoinjava.core.string;

import java.util.concurrent.locks.*;
import java.lang.management.*;
import java.time.format.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.time.temporal.*;

/**
 * Examples for the tutorial "Convert a String to Title Case in Java (With Examples)".
 * https://howtodoinjava.com/java/string/convert-string-to-titlecase/
 */
public class TitleCaseConversion {
    static String titleCase(String text, String delimiters) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        StringBuilder result = new StringBuilder(text.length());
        boolean startOfWord = true;
        for (int i = 0; i < text.length(); ) {
            int cp = text.codePointAt(i);
            if (Character.isWhitespace(cp) || delimiters.indexOf(cp) >= 0) {
                startOfWord = true;
                result.appendCodePoint(cp);
            } else if (startOfWord) {
                result.appendCodePoint(Character.toTitleCase(cp));
                startOfWord = false;
            } else {
                result.appendCodePoint(Character.toLowerCase(cp));
            }
            i += Character.charCount(cp);
        }
        return result.toString();
    }
    static String titleCase(String text) {
        return titleCase(text, "");
    }
    static String bookTitle(String text) {
        Set<String> small = Set.of("a", "an", "and", "at", "but", "by", "for", "in", "of", "on", "or", "the", "to");
        String[] words = text.strip().toLowerCase(Locale.ROOT).split("\\s+");
        for (int i = 0; i < words.length; i++) {
            boolean firstOrLast = i == 0 || i == words.length - 1;
            if (firstOrLast || !small.contains(words[i])) {
                words[i] = titleCase(words[i]);
            }
        }
        return String.join(" ", words);
    }
    public static void main(String[] args) throws Exception {
        {
            String book = titleCase("the lord OF the rings");        // "The Lord Of The Rings"
            show("book", book);
            String spaced = titleCase("  hello   world ");            // "  Hello   World "
            show("spaced", spaced);
            String name = titleCase("jean-luc picard", "-");          // "Jean-Luc Picard"
            show("name", name);
        }
        {
            String shouted = titleCase("HOW TO DO IN JAVA");        // "How To Do In Java"
            show("shouted", shouted);
            String empty = titleCase("");                             // ""
            show("empty", empty);
            String missing = titleCase(null);                         // null
            show("missing", missing);
            String apostrophe = titleCase("don't stop");              // "Don't Stop"
            show("apostrophe", apostrophe);
            String irish = titleCase("o'neil", "'");                  // "O'Neil"
            show("irish", irish);
        }
        {
            Pattern word = Pattern.compile("(\\p{L})(\\p{L}*)");
            Function<MatchResult, String> cap = m -> m.group(1).toUpperCase(Locale.ROOT) + m.group(2).toLowerCase(Locale.ROOT);
            String result = word.matcher("the lord OF the rings").replaceAll(cap);   // "The Lord Of The Rings"
            show("result", result);
            String contraction = word.matcher("don't stop").replaceAll(cap);         // "Don'T Stop"
            show("contraction", contraction);
        }
        {
            Pattern wordWithApostrophe = Pattern.compile("(\\p{L})([\\p{L}']*)");
            Function<MatchResult, String> cap = m -> m.group(1).toUpperCase(Locale.ROOT) + m.group(2).toLowerCase(Locale.ROOT);
            String fixed = wordWithApostrophe.matcher("don't stop").replaceAll(cap);   // "Don't Stop"
            show("fixed", fixed);
        }
        {
            String text = "  the   lord of  the rings";
            Stream<String> words = Arrays.stream(text.strip().split("\\s+")).filter(w -> !w.isEmpty());
            String joined = words
                    .map(w -> w.substring(0, 1).toUpperCase(Locale.ROOT) + w.substring(1).toLowerCase(Locale.ROOT))
                    .collect(Collectors.joining(" "));
            boolean collapsed = joined.equals("The Lord Of The Rings");   // true
            show("collapsed", collapsed);
        }
        {
            try { String blank = "".split("\\s+")[0].substring(0, 1); show("blank", blank); } catch (Throwable _t) { System.out.println("blank -> " + _t); }
        }
        {
            String tolkien = bookTitle("THE LORD OF THE RINGS");       // "The Lord of the Rings"
            show("tolkien", tolkien);
            String song = bookTitle("a day in the life");             // "A Day in the Life"
            show("song", song);
            String ending = bookTitle("what dreams are made of");     // "What Dreams Are Made Of"
            show("ending", ending);
        }
        {
            int upper = Character.toUpperCase(0x01C6);               // 452, U+01C4 with both parts uppercase
            show("upper", upper);
            int title = Character.toTitleCase(0x01C6);               // 453, U+01C5 with the first part uppercase
            show("title", title);
            boolean differ = upper != title;                          // true
            show("differ", differ);
        }
        {
            String turkish = "TITLE".toLowerCase(Locale.forLanguageTag("tr"));
            int secondChar = turkish.charAt(1);                                     // 305, U+0131 dotless i
            show("secondChar", secondChar);
            String root = "TITLE".toLowerCase(Locale.ROOT);                         // "title"
            show("root", root);
            boolean same = turkish.equals(root);                                    // false
            show("same", same);
        }
        {
            String input = "hello world";
            String sentence = input.isEmpty() ? input : input.substring(0, 1).toUpperCase(Locale.ROOT) + input.substring(1);   // "Hello world"
            show("sentence", sentence);
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
