package com.howtodoinjava.core.string;

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
import org.apache.commons.lang3.math.NumberUtils;
import java.text.NumberFormat;

/**
 * Examples for the tutorial "Java String to int: parseInt(), valueOf() and Safe Parsing".
 * https://howtodoinjava.com/java/string/convert-string-to-int/
 */
public class ConvertStringToInt {
    public static OptionalInt tryParseInt(String text) {
        if (text == null) {
            return OptionalInt.empty();
        }
        try {
            return OptionalInt.of(Integer.parseInt(text.strip()));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }

    public static int parseIntOrDefault(String text, int defaultValue) {
        return tryParseInt(text).orElse(defaultValue);
    }
    public static void main(String[] args) throws Exception {
        {
            int age = Integer.parseInt("37");                 // 37
            show("age", age);
            Integer boxedAge = Integer.valueOf("37");         // 37 (an Integer object)
            show("boxedAge", boxedAge);
            int negative = Integer.parseInt("-15");           // -15
            show("negative", negative);
            int binary = Integer.parseInt("1010", 2);         // 10
            show("binary", binary);
            int hex = Integer.parseInt("ff", 16);             // 255
            show("hex", hex);
            int trimmed = Integer.parseInt(" 37 ".strip());   // 37
            show("trimmed", trimmed);

            try { int bad = Integer.parseInt("abc"); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
            try { int tooBig = Integer.parseInt("2147483648"); show("tooBig", tooBig); } catch (Throwable _t) { System.out.println("tooBig -> " + _t); }
            long big = Long.parseLong("3000000000");          // 3000000000
            show("big", big);

            OptionalInt safe = tryParseInt("abc");            // OptionalInt.empty (helper from section 4.3)
            show("safe", safe);
            int orZero = NumberUtils.toInt("abc");            // 0 (Apache Commons Lang)
            show("orZero", orZero);
            int digit = Character.digit('7', 10);             // 7
            show("digit", digit);
        }
        {
            int age = Integer.parseInt("age=37", 4, 6, 10);   // 37
            show("age", age);
        }
        {
            boolean small = Integer.valueOf("127") == Integer.valueOf("127");          // true (same cached object)
            show("small", small);
            boolean large = Integer.valueOf("128") == Integer.valueOf("128");          // false (two objects)
            show("large", large);
            boolean equal = Integer.valueOf("128").equals(Integer.valueOf("128"));     // true
            show("equal", equal);
        }
        {
            int fromBinary = Integer.parseInt("1010", 2);     // 10
            show("fromBinary", fromBinary);
            int fromOctal = Integer.parseInt("17", 8);        // 15
            show("fromOctal", fromOctal);
            int fromHex = Integer.parseInt("ff", 16);         // 255
            show("fromHex", fromHex);
            int negativeHex = Integer.parseInt("-FF", 16);    // -255
            show("negativeHex", negativeHex);
            int base36 = Integer.parseInt("zz", 36);          // 1295
            show("base36", base36);

            try { int prefixed = Integer.parseInt("0xff", 16); show("prefixed", prefixed); } catch (Throwable _t) { System.out.println("prefixed -> " + _t); }
        }
        {
            int hex = Integer.decode("0xff");          // 255
            show("hex", hex);
            int hashHex = Integer.decode("#ff");       // 255
            show("hashHex", hashHex);
            int octal = Integer.decode("010");         // 8 (octal)
            show("octal", octal);
            int negative = Integer.decode("-0x10");    // -16
            show("negative", negative);

            try { int padded = Integer.decode("08"); show("padded", padded); } catch (Throwable _t) { System.out.println("padded -> " + _t); }
        }
        {
            try { int fromNull = Integer.parseInt(null); show("fromNull", fromNull); } catch (Throwable _t) { System.out.println("fromNull -> " + _t); }
            try { int fromEmpty = Integer.parseInt(""); show("fromEmpty", fromEmpty); } catch (Throwable _t) { System.out.println("fromEmpty -> " + _t); }
        }
        {
            String input = "3x";
            try {
                int quantity = Integer.parseInt(input);
                System.out.println("Quantity: " + quantity);
            } catch (NumberFormatException e) {
                System.out.println("Quantity must be a whole number. " + e.getMessage());
            }
        }
        {
            OptionalInt valid = tryParseInt("37");           // OptionalInt[37]
            show("valid", valid);
            OptionalInt spaced = tryParseInt(" 37 ");        // OptionalInt[37]
            show("spaced", spaced);
            OptionalInt invalid = tryParseInt("abc");        // OptionalInt.empty
            show("invalid", invalid);
            OptionalInt missing = tryParseInt(null);         // OptionalInt.empty
            show("missing", missing);
            int fallback = tryParseInt("abc").orElse(-1);    // -1
            show("fallback", fallback);

            int pageSize = parseIntOrDefault("40", 20);      // 40
            show("pageSize", pageSize);
            int defaultSize = parseIntOrDefault("abc", 20);  // 20
            show("defaultSize", defaultSize);
        }
        {
            int valid = NumberUtils.toInt("37");             // 37
            show("valid", valid);
            int invalid = NumberUtils.toInt("abc");          // 0
            show("invalid", invalid);
            int missing = NumberUtils.toInt(null);           // 0
            show("missing", missing);
            int withDefault = NumberUtils.toInt("abc", -1);  // -1
            show("withDefault", withDefault);
            int spaced = NumberUtils.toInt(" 37");           // 0 (no stripping)
            show("spaced", spaced);
        }
        {
            long size = Long.parseLong("3000000000");                     // 3000000000
            show("size", size);
            BigInteger huge = new BigInteger("99999999999999999999");     // 99999999999999999999
            show("huge", huge);

            int wrong = (int) Long.parseLong("3000000000");               // -1294967296 (wrong, no error)
            show("wrong", wrong);
            try { int exact = Math.toIntExact(Long.parseLong("3000000000")); show("exact", exact); } catch (Throwable _t) { System.out.println("exact -> " + _t); }
        }
        {
            int checksum = Integer.parseUnsignedInt("4294967295");   // -1
            show("checksum", checksum);
            String text = Integer.toUnsignedString(checksum);         // "4294967295"
            show("text", text);
        }
        {
            int numeric = Character.getNumericValue('7');        // 7
            show("numeric", numeric);
            int digit = Character.digit('7', 10);                // 7
            show("digit", digit);
            int subtracted = '7' - '0';                          // 7
            show("subtracted", subtracted);

            int letterNumeric = Character.getNumericValue('a');  // 10 (a is the digit 10 in base 36)
            show("letterNumeric", letterNumeric);
            int letterDigit = Character.digit('a', 10);          // -1 (not a decimal digit)
            show("letterDigit", letterDigit);
            int dash = Character.getNumericValue('-');           // -1
            show("dash", dash);
        }
        {
            int digitSum = "2026".chars().map(c -> Character.digit(c, 10)).sum();   // 10
            show("digitSum", digitSum);
        }
        {
            List<String> quantities = List.of("5", "3", "8");

            int total = quantities.stream().mapToInt(Integer::parseInt).sum();                 // 16
            show("total", total);
            List<Integer> numbers = quantities.stream().map(Integer::valueOf).toList();        // [5, 3, 8]
            show("numbers", numbers);
        }
        {
            List<String> userInput = List.of("5", "x", " 3 ", "", "8");

            try { int crashed = userInput.stream().mapToInt(Integer::parseInt).sum(); show("crashed", crashed); } catch (Throwable _t) { System.out.println("crashed -> " + _t); }

            int validTotal = userInput.stream()
                    .flatMapToInt(s -> tryParseInt(s).stream())
                    .sum();                                                           // 16 (5 + 3 + 8)

            int[] withDefaults = Stream.of("5", "x", "8")
                    .mapToInt(s -> parseIntOrDefault(s, 0))
                    .toArray();                                                       // [5, 0, 8]
        }
        {
            Scanner scanner = new Scanner("37 abc");   // or new Scanner(System.in)
            show("scanner", scanner);
            int first = scanner.nextInt();             // 37
            show("first", first);
            boolean nextIsInt = scanner.hasNextInt();  // false ("abc" is next)
            show("nextIsInt", nextIsInt);
        }
        {
            int truncated = (int) Double.parseDouble("12.7");                 // 12 (cuts off the fraction)
            show("truncated", truncated);
            int rounded = (int) Math.round(Double.parseDouble("12.7"));       // 13
            show("rounded", rounded);
            try { int exact = new BigDecimal("12.7").intValueExact(); show("exact", exact); } catch (Throwable _t) { System.out.println("exact -> " + _t); }

            int grouped = NumberFormat.getIntegerInstance(Locale.US)
                    .parse("1,234").intValue();                                   // 1234 (parse() throws the checked ParseException)
            int cleaned = Integer.parseInt("1,234".replace(",", ""));         // 1234
            show("cleaned", cleaned);
        }
        {
            boolean signed = NumberUtils.isParsable("-37");     // true
            show("signed", signed);
            boolean decimal = NumberUtils.isParsable("12.5");   // true (but parseInt() throws)
            show("decimal", decimal);
            boolean digits = NumberUtils.isDigits("-37");       // false (the sign is not a digit)
            show("digits", digits);
        }
        {
            int arabic = Integer.parseInt("\u0661\u0662");      // 12 (Arabic-Indic digits)
            show("arabic", arabic);
            int devanagari = Integer.parseInt("\u0967\u0968");  // 12 (Devanagari digits)
            show("devanagari", devanagari);
            int fullWidth = Integer.parseInt("\uFF11\uFF12");   // 12 (full-width digits)
            show("fullWidth", fullWidth);
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
