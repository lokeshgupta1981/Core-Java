package com.howtodoinjava.misc;

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
import java.text.*;

/**
 * Examples for the tutorial "Compact Number Formatting in Java: 1K, 1M and 1 Thousand".
 * https://howtodoinjava.com/java12/compact-number-format/
 */
public class CompactNumberFormatSteps {
    static String formatViews(long views) {
        NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        fmt.setMaximumFractionDigits(1);
        fmt.setRoundingMode(RoundingMode.DOWN);
        return fmt.format(views) + " views";
    }
    public static void main(String[] args) throws Exception {
        {
            NumberFormat shortFmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
            NumberFormat longFmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.LONG);
            String views = shortFmt.format(15_300);          // "15K"
            show("views", views);
            String plays = shortFmt.format(2_000_000);       // "2M"
            show("plays", plays);
            String spoken = longFmt.format(2_000_000);       // "2 million"
            show("spoken", spoken);
            String small = shortFmt.format(999);             // "999"
            show("small", small);
        }
        {
            NumberFormat defaultFmt = NumberFormat.getCompactNumberInstance();
            NumberFormat usShort = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
            NumberFormat ukLong = NumberFormat.getCompactNumberInstance(Locale.UK, NumberFormat.Style.LONG);
            String gb = ukLong.format(15_000);               // "15 thousand"
            show("gb", gb);
        }
        {
            NumberFormat oneDigit = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
            oneDigit.setMaximumFractionDigits(1);
            String a = oneDigit.format(1_234);               // "1.2K"
            show("a", a);
            String b = oneDigit.format(15_300);              // "15.3K"
            show("b", b);
            String c = oneDigit.format(1_000);               // "1K"
            show("c", c);
        }
        {
            NumberFormat twoDigits = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
            twoDigits.setMinimumFractionDigits(2);
            String d = twoDigits.format(1_000);              // "1.00K"
            show("d", d);
            String e = twoDigits.format(15_300);             // "15.30K"
            show("e", e);
        }
        {
            NumberFormat even = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
            String first = even.format(1_500);               // "2K"
            show("first", first);
            String second = even.format(2_500);              // "2K"
            show("second", second);
            String third = even.format(3_500);               // "4K"
            show("third", third);
        }
        {
            NumberFormat down = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
            down.setMaximumFractionDigits(1);
            down.setRoundingMode(RoundingMode.DOWN);
            String almost = down.format(999_999);            // "999.9K"
            show("almost", almost);
            String exact = down.format(1_299);               // "1.2K"
            show("exact", exact);
        }
        {
            Locale india = Locale.of("en", "IN");
            Locale germany = Locale.of("de", "DE");
            NumberFormat inShort = NumberFormat.getCompactNumberInstance(india, NumberFormat.Style.SHORT);
            NumberFormat deLong = NumberFormat.getCompactNumberInstance(germany, NumberFormat.Style.LONG);
            NumberFormat deShort = NumberFormat.getCompactNumberInstance(germany, NumberFormat.Style.SHORT);
            String lakh = inShort.format(2_000_000);         // "20L"
            show("lakh", lakh);
            String german = deLong.format(2_000_000);        // "2 Millionen"
            show("german", german);
            String germanShort = deShort.format(15_000);     // "15.000"
            show("germanShort", germanShort);
        }
        {
            NumberFormat parser = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
            Number thousand = parser.parse("1K");            // 1000
            show("thousand", thousand);
            Number half = parser.parse("1.5M");              // 1500000
            show("half", half);
            Number odd = parser.parse("1.2345K");            // 1234.5
            show("odd", odd);
            Number trailing = parser.parse("1.5K views");    // 1500
            show("trailing", trailing);
            try { Number broken = parser.parse("K"); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            String[] patterns = {"", "", "", "0k", "00k", "000k", "0m", "00m", "000m", "0b"};
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);
            CompactNumberFormat lower = new CompactNumberFormat("#,##0", symbols, patterns);
            lower.setMaximumFractionDigits(1);
            String k = lower.format(1_250);                  // "1.2k"
            show("k", k);
            String m = lower.format(1_250_000);              // "1.2m"
            show("m", m);
            String plain = lower.format(999);                // "999"
            show("plain", plain);
        }
        {
            String few = formatViews(999);                  // "999 views"
            show("few", few);
            String some = formatViews(1_250);                // "1.2K views"
            show("some", some);
            String many = formatViews(999_999);              // "999.9K views"
            show("many", many);
            String viral = formatViews(15_349_000);          // "15.3M views"
            show("viral", viral);
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
