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
 * Examples for the tutorial "Java Format Phone Number with Regex: (123) 456-7890".
 * https://howtodoinjava.com/java/string/format-phone-number/
 */
public class FormatPhoneNumber {
    static final class PhoneFormat {
        private static final Pattern US_NUMBER = Pattern.compile("^1?(\\d{3})(\\d{3})(\\d{4})$");

        static Optional<String> format(String raw) {
            if (raw == null) {
                return Optional.empty();
            }
            Matcher m = US_NUMBER.matcher(raw.replaceAll("\\D", ""));
            if (!m.matches()) {
                return Optional.empty();
            }
            return Optional.of("(" + m.group(1) + ") " + m.group(2) + "-" + m.group(3));
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String digits = "6125550147";
            String formatted = digits.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "($1) $2-$3");  // "(612) 555-0147"
            show("formatted", formatted);
            String dashed = digits.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");       // "612-555-0147"
            show("dashed", dashed);
        }
        {
            String input = "6125550147";
            String number = input.replaceFirst("^(\\d{3})(\\d{3})(\\d{4})$", "($1) $2-$3");  // "(612) 555-0147"
            show("number", number);
            String tooLong = "61255501479".replaceFirst("^(\\d{3})(\\d{3})(\\d{4})$", "($1) $2-$3");  // "61255501479"
            show("tooLong", tooLong);
        }
        {
            String input = "6125550147";
            String hyphens = input.replaceFirst("^(\\d{3})(\\d{3})(\\d{4})$", "$1-$2-$3");   // "612-555-0147"
            show("hyphens", hyphens);
            String dots = input.replaceFirst("^(\\d{3})(\\d{3})(\\d{4})$", "$1.$2.$3");      // "612.555.0147"
            show("dots", dots);
            String e164 = input.replaceFirst("^(\\d{10})$", "+1$1");                         // "+16125550147"
            show("e164", e164);
        }
        {
            String typed = " (612) 555.0147 ";
            String onlyDigits = typed.replaceAll("\\D", "");                                // "6125550147"
            show("onlyDigits", onlyDigits);
            String withCountry = "+1 612-555-0147".replaceAll("\\D", "");                  // "16125550147"
            show("withCountry", withCountry);
        }
        {
            Optional<String> plain = PhoneFormat.format("6125550147");          // Optional[(612) 555-0147]
            show("plain", plain);
            Optional<String> messy = PhoneFormat.format("+1 (612) 555.0147");     // Optional[(612) 555-0147]
            show("messy", messy);
            Optional<String> shortOne = PhoneFormat.format("555-0147");           // Optional.empty
            show("shortOne", shortOne);
            Optional<String> missing = PhoneFormat.format(null);                  // Optional.empty
            show("missing", missing);
            String shown = PhoneFormat.format("12345").orElse("12345");           // "12345"
            show("shown", shown);
        }
        {
            String d = "6125550147";
            String viaSubstring = "(%s) %s-%s".formatted(d.substring(0, 3), d.substring(3, 6), d.substring(6));
            // "(612) 555-0147"
            try { String bad = "612555".substring(6, 10); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            Pattern ext = Pattern.compile("(?i)\\s*(?:x|ext\\.?)\\s*(\\d+)$");
            Matcher em = ext.matcher("612-555-0147 ext. 42");
            String extension = em.find() ? em.group(1) : "";                            // "42"
            show("extension", extension);
            String main = em.replaceFirst("");                                          // "612-555-0147"
            show("main", main);
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
