package com.howtodoinjava.core.datetime;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.time.format.*;
import java.time.temporal.*;
import java.time.chrono.*;
import java.time.zone.*;
import java.text.*;

/**
 * Examples for the tutorial "Get the Current Locale in Java (JVM, Servlet and Spring)".
 * https://howtodoinjava.com/java/date-time/how-to-get-current-user-locale-in-java/
 */
public class CurrentUserLocale {
    static String orderSummary(BigDecimal total, LocalDate delivery, Locale locale) {
        String price = NumberFormat.getCurrencyInstance(locale).format(total);
        String date = delivery.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale));
        return price + ", delivery " + date;
    }
    public static void main(String[] args) throws Exception {
        {
            Locale current = Locale.getDefault();                          // en_US on a US machine
            show("current", current);
            String language = current.getLanguage();                       // "en"
            show("language", language);
            String country = current.getCountry();                         // "US"
            show("country", country);
            String name = current.getDisplayName(Locale.US);               // "English (United States)"
            show("name", name);
            Locale forFormatting = Locale.getDefault(Locale.Category.FORMAT);   // en_US
            show("forFormatting", forFormatting);
        }
        {
            String langProperty = System.getProperty("user.language");      // "en"
            show("langProperty", langProperty);
            String countryProperty = System.getProperty("user.country");    // "US"
            show("countryProperty", countryProperty);
        }
        {
            Locale display = Locale.getDefault(Locale.Category.DISPLAY);   // en_US
            show("display", display);
            Locale format = Locale.getDefault(Locale.Category.FORMAT);     // en_US
            show("format", format);
        }
        {
            String us = orderSummary(new BigDecimal("49.90"), LocalDate.of(2026, 3, 14), Locale.US);         // "$49.90, delivery Mar 14, 2026"
            show("us", us);
            String de = orderSummary(new BigDecimal("49.90"), LocalDate.of(2026, 3, 14), Locale.GERMANY);    // "49,90 €, delivery 14.03.2026"
            show("de", de);
            String in = orderSummary(new BigDecimal("49.90"), LocalDate.of(2026, 3, 14), Locale.of("en", "IN"));   // "₹49.90, delivery 14 Mar 2026"
            show("in", in);
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
