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
 * Examples for the tutorial "Java String Methods and String Class Tutorial with Examples".
 * https://howtodoinjava.com/java/string/java-string/
 */
public class StringMethodsGuide {
    static Optional<String> safeFileName(String original) {
        if (original == null || original.isBlank()) {
            return Optional.empty();
        }
        String name = original.strip().toLowerCase(Locale.ROOT);
        if (!name.endsWith(".pdf")) {
            return Optional.empty();
        }
        String base = name.substring(0, name.length() - ".pdf".length())
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
        return base.isEmpty() ? Optional.empty() : Optional.of(base + ".pdf");
    }
    public static void main(String[] args) throws Exception {
        {
            String fruit = "Banana";
            int length = fruit.length();                          // 6
            show("length", length);
            char first = fruit.charAt(0);                         // 'B'
            show("first", first);
            boolean same = fruit.equalsIgnoreCase("banana");      // true
            show("same", same);
            int order = fruit.compareTo("Cherry");                // -1
            show("order", order);
            int index = fruit.indexOf("an");                      // 1
            show("index", index);
            boolean has = fruit.contains("nan");                  // true
            show("has", has);
            String part = fruit.substring(1, 4);                  // "ana"
            show("part", part);
            String lower = fruit.toLowerCase(Locale.ROOT);        // "banana"
            show("lower", lower);
            String replaced = fruit.replace('a', 'o');            // "Bonono"
            show("replaced", replaced);
            String[] parts = "apple,kiwi".split(",");             // [apple, kiwi]
            show("parts", parts);
            boolean blank = "   ".isBlank();                      // true
            show("blank", blank);
            String clean = "  kiwi ".strip();                     // "kiwi"
            show("clean", clean);
        }
        {
            String name = "Lokesh";                               // literal, pooled
            show("name", name);
            String copy = new String("Lokesh");                   // separate object, avoid
            show("copy", copy);
            boolean sameObject = name == copy;                    // false
            show("sameObject", sameObject);
            String number = String.valueOf(42);                   // "42"
            show("number", number);
            String letters = String.valueOf(new char[] {'f', 'i', 'g'});   // "fig"
            show("letters", letters);
            String csv = String.join(",", "apple", "kiwi");       // "apple,kiwi"
            show("csv", csv);
        }
        {
            String query = """
            SELECT name
            FROM fruit
            """;
            int lines = (int) query.lines().count();              // 2
            show("lines", lines);
        }
        {
            String city = "paris";
            String upper = city.toUpperCase(Locale.ROOT);         // "PARIS"
            show("upper", upper);
            String unchanged = city;                              // "paris"
            show("unchanged", unchanged);
        }
        {
            String smile = "\uD83D\uDE00";                       // one emoji
            show("smile", smile);
            int units = smile.length();                           // 2
            show("units", units);
            int symbols = smile.codePointCount(0, smile.length());   // 1
            show("symbols", symbols);
        }
        {
            String typed = new StringBuilder("kiwi").toString();  // built at runtime, like user input
            show("typed", typed);
            boolean sameRef = typed == "kiwi";                    // false
            show("sameRef", sameRef);
            boolean sameText = typed.equals("kiwi");              // true
            show("sameText", sameText);
            boolean withNull = "kiwi".equals(null);               // false
            show("withNull", withNull);
        }
        {
            String input = null;
            boolean literalFirst = "kiwi".equals(input);          // false
            show("literalFirst", literalFirst);
            boolean bothNull = Objects.equals(input, null);       // true
            show("bothNull", bothNull);
        }
        {
            boolean yes = "YES".equalsIgnoreCase("yes");          // true
            show("yes", yes);
            boolean other = "Yes".equalsIgnoreCase("yeah");       // false
            show("other", other);
            boolean nullArg = "yes".equalsIgnoreCase(null);       // false
            show("nullArg", nullArg);
        }
        {
            int before = "apple".compareTo("banana");             // -1
            show("before", before);
            int after = "cherry".compareTo("apple");              // 2
            show("after", after);
            int prefix = "apple".compareTo("apples");             // -1
            show("prefix", prefix);
            int caps = "Apple".compareTo("apple");                // -32
            show("caps", caps);
            boolean sortsFirst = "apple".compareTo("banana") < 0; // true
            show("sortsFirst", sortsFirst);
        }
        {
            int raw = "apple".compareTo("Banana");                 // 31
            show("raw", raw);
            int human = Collator.getInstance(Locale.ENGLISH).compare("apple", "Banana");   // -1
            show("human", human);
        }
        {
            int ignoring = "Apple".compareToIgnoreCase("apple");  // 0
            show("ignoring", ignoring);
            List<String> sorted = Stream.of("banana", "Apple", "cherry")
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList();
            List<String> result = sorted;                         // [Apple, banana, cherry]
            show("result", result);
        }
        {
            int hash = "abc".hashCode();                          // 96354
            show("hash", hash);
            int empty = "".hashCode();                            // 0
            show("empty", empty);
            boolean collision = "Aa".hashCode() == "BB".hashCode();   // true
            show("collision", collision);
            boolean sameText = "Aa".equals("BB");                 // false
            show("sameText", sameText);
        }
        {
            char first = "kiwi".charAt(0);                        // 'k'
            show("first", first);
            char last = "kiwi".charAt("kiwi".length() - 1);       // 'i'
            show("last", last);
            try { char past = "kiwi".charAt(4); show("past", past); } catch (Throwable _t) { System.out.println("past -> " + _t); }
        }
        {
            String word = "kiwi";
            int position = 4;
            char safe = position >= 0 && position < word.length() ? word.charAt(position) : '-';   // '-'
            show("safe", safe);
        }
        {
            int firstAn = "banana".indexOf("an");                 // 1
            show("firstAn", firstAn);
            int nextAn = "banana".indexOf("an", 2);               // 3
            show("nextAn", nextAn);
            int letter = "banana".indexOf('n');                   // 2
            show("letter", letter);
            int missing = "banana".indexOf("kiwi");               // -1
            show("missing", missing);
            int inRange = "banana".indexOf("an", 2, 5);           // 3
            show("inRange", inRange);
        }
        {
            String file = "report.final.pdf";
            int lastDot = file.lastIndexOf('.');                  // 12
            show("lastDot", lastDot);
            String extension = file.substring(lastDot + 1);       // "pdf"
            show("extension", extension);
            int backward = "banana".lastIndexOf('a', 4);          // 3
            show("backward", backward);
        }
        {
            boolean found = "pineapple".contains("apple");        // true
            show("found", found);
            boolean anyCase = "PineApple".toLowerCase(Locale.ROOT).contains("apple");   // true
            show("anyCase", anyCase);
            boolean emptyArg = "pineapple".contains("");          // true
            show("emptyArg", emptyArg);
            try { boolean nullArg = "pineapple".contains(null); show("nullArg", nullArg); } catch (Throwable _t) { System.out.println("nullArg -> " + _t); }
        }
        {
            String term = null;
            boolean safeFound = term != null && "pineapple".contains(term);   // false
            show("safeFound", safeFound);
        }
        {
            boolean photo = "IMG_2041.jpg".startsWith("IMG_");    // true
            show("photo", photo);
            boolean atOffset = "IMG_2041.jpg".startsWith("2041", 4);   // true
            show("atOffset", atOffset);
            boolean lower = "img_2041.jpg".startsWith("IMG_");    // false
            show("lower", lower);
        }
        {
            boolean pdf = "invoice.pdf".endsWith(".pdf");         // true
            show("pdf", pdf);
            boolean shouting = "INVOICE.PDF".endsWith(".pdf");    // false
            show("shouting", shouting);
            boolean anyCase = "INVOICE.PDF".toLowerCase(Locale.ROOT).endsWith(".pdf");   // true
            show("anyCase", anyCase);
            boolean empty = "invoice.pdf".endsWith("");           // true
            show("empty", empty);
        }
        {
            String fruit = "pineapple";
            String tail = fruit.substring(4);                     // "apple"
            show("tail", tail);
            String head = fruit.substring(0, 4);                  // "pine"
            show("head", head);
            int size = fruit.substring(2, 5).length();            // 3
            show("size", size);
            try { String bad = fruit.substring(5, 2); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            String code = "ab";
            String firstFour = code.substring(0, Math.min(4, code.length()));   // "ab"
            show("firstFour", firstFour);
        }
        {
            String[] parts = "apple,kiwi,,".split(",");          // [apple, kiwi]
            show("parts", parts);
            String[] all = "apple,kiwi,,".split(",", -1);         // [apple, kiwi, , ]
            show("all", all);
            String[] pipes = "apple|kiwi".split("\\|");           // [apple, kiwi]
            show("pipes", pipes);
            String[] wrong = "apple|kiwi".split("|");             // [a, p, p, l, e, |, k, i, w, i]
            show("wrong", wrong);
        }
        {
            String root = "TITLE".toLowerCase(Locale.ROOT);       // "title"
            show("root", root);
            String turkish = "TITLE".toLowerCase(Locale.of("tr")); // dotless i, not "title"
            show("turkish", turkish);
            boolean differs = !turkish.equals("title");           // true
            show("differs", differs);
        }
        {
            String code = "kiwi".toUpperCase(Locale.ROOT);        // "KIWI"
            show("code", code);
            String street = "stra\u00DFe".toUpperCase(Locale.ROOT); // "STRASSE"
            show("street", street);
            int grown = street.length();                          // 7
            show("grown", grown);
        }
        {
            String chars = "banana".replace('a', 'o');            // "bonono"
            show("chars", chars);
            String literal = "a.b.c".replace(".", "-");           // "a-b-c"
            show("literal", literal);
            String regex = "a.b.c".replaceAll(".", "-");          // "-----"
            show("regex", regex);
            String escaped = "a.b.c".replaceAll("\\.", "-");      // "a-b-c"
            show("escaped", escaped);
            String digits = "order 42 of 57".replaceAll("\\d+", "#");     // "order # of #"
            show("digits", digits);
            String firstOnly = "order 42 of 57".replaceFirst("\\d+", "#"); // "order # of 57"
            show("firstOnly", firstOnly);
        }
        {
            try { String price = "10 USD".replaceAll("USD", "$"); show("price", price); } catch (Throwable _t) { System.out.println("price -> " + _t); }
            String safePrice = "10 USD".replaceAll("USD", Matcher.quoteReplacement("$"));   // "10 $"
            show("safePrice", safePrice);
        }
        {
            boolean empty = "".isEmpty();                         // true
            show("empty", empty);
            boolean spacesEmpty = "   ".isEmpty();                // false
            show("spacesEmpty", spacesEmpty);
            boolean spacesBlank = "   ".isBlank();                // true
            show("spacesBlank", spacesBlank);
            int trimmed = "\u2003kiwi\u2003".trim().length();     // 6, em spaces stay
            show("trimmed", trimmed);
            String stripped = "\u2003kiwi\u2003".strip();         // "kiwi"
            show("stripped", stripped);
            String left = "  kiwi  ".stripLeading();              // "kiwi  "
            show("left", left);
            String rule = "-".repeat(5);                          // "-----"
            show("rule", rule);
            long count = "apple\nkiwi\nfig".lines().count();      // 3
            show("count", count);
            String shout = "kiwi".transform(s -> s.toUpperCase(Locale.ROOT) + "!");   // "KIWI!"
            show("shout", shout);
            String label = "%s x %d".formatted("apple", 5);       // "apple x 5"
            show("label", label);
        }
        {
            String label = "apple" + " x " + 5;                   // "apple x 5"
            show("label", label);
            String glued = "apple".concat("pie");                 // "applepie"
            show("glued", glued);
            String withNull = "apple" + null;                     // "applenull"
            show("withNull", withNull);
            String joined = String.join(", ", List.of("apple", "kiwi"));   // "apple, kiwi"
            show("joined", joined);
            String bar = new StringBuilder().repeat("=", 3).toString();    // "==="
            show("bar", bar);
        }
        {
            StringBuilder initials = new StringBuilder();
            for (String f : List.of("apple", "kiwi", "fig")) {
                initials.append(f.charAt(0));
            }
            String result = initials.toString();                  // "akf"
            show("result", result);
        }
        {
            Optional<String> report = safeFileName("  Q3 Report (Final).PDF ");   // Optional[q3-report-final.pdf]
            show("report", report);
            Optional<String> program = safeFileName("setup.exe");                 // Optional.empty
            show("program", program);
            Optional<String> blank = safeFileName("   ");                         // Optional.empty
            show("blank", blank);
            Optional<String> onlyExt = safeFileName("(.pdf");                     // Optional.empty
            show("onlyExt", onlyExt);
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
