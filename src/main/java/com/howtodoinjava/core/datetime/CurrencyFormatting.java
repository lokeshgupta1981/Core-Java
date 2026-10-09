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
 * Examples for the tutorial "Format Currency in Java with NumberFormat, Locale and BigDecimal".
 * https://howtodoinjava.com/java/date-time/location-based-currency-formatting-in-java/
 */
public class CurrencyFormatting {
    static NumberFormat currencyFormat(Locale locale, Currency currency) {
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        format.setCurrency(currency);
        format.setMinimumFractionDigits(currency.getDefaultFractionDigits());
        format.setMaximumFractionDigits(currency.getDefaultFractionDigits());
        return format;
    }
    static Optional<BigDecimal> parsePrice(String text, Locale locale) {
        if (text == null || !(NumberFormat.getCurrencyInstance(locale) instanceof DecimalFormat format)) {
            return Optional.empty();
        }
        String input = text.strip();
        format.setParseBigDecimal(true);
        ParsePosition position = new ParsePosition(0);
        Number number = format.parse(input, position);
        if (number == null || position.getIndex() != input.length()) {
            return Optional.empty();
        }
        return Optional.of((BigDecimal) number);
    }
    public static void main(String[] args) throws Exception {
        {
            BigDecimal price = new BigDecimal("1234.56");
            String us = NumberFormat.getCurrencyInstance(Locale.US).format(price);                 // "$1,234.56"
            show("us", us);
            String germany = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(price);       // "1.234,56 €"
            show("germany", germany);
            String india = NumberFormat.getCurrencyInstance(Locale.of("en", "IN")).format(price);  // "₹1,234.56"
            show("india", india);
            String japan = NumberFormat.getCurrencyInstance(Locale.JAPAN).format(price);           // "￥1,235"
            show("japan", japan);
        }
        {
            Currency euro = Currency.getInstance("EUR");
            String code = euro.getCurrencyCode();                        // "EUR"
            show("code", code);
            String name = euro.getDisplayName(Locale.US);                // "Euro"
            show("name", name);
            String symbol = euro.getSymbol(Locale.FRANCE);               // "€"
            show("symbol", symbol);
            int decimals = euro.getDefaultFractionDigits();              // 2
            show("decimals", decimals);
            int yenDecimals = Currency.getInstance("JPY").getDefaultFractionDigits();     // 0
            show("yenDecimals", yenDecimals);
            int dinarDecimals = Currency.getInstance("BHD").getDefaultFractionDigits();   // 3
            show("dinarDecimals", dinarDecimals);
            Currency fromLocale = Currency.getInstance(Locale.JAPAN);    // JPY
            show("fromLocale", fromLocale);
        }
        {
            String forUs = Currency.getInstance("USD").getSymbol(Locale.US);         // "$"
            show("forUs", forUs);
            String forCanada = Currency.getInstance("USD").getSymbol(Locale.CANADA); // "US$"
            show("forCanada", forCanada);
        }
        {
            String noRegion = NumberFormat.getCurrencyInstance(Locale.ENGLISH).format(9.99);   // "¤9.99"
            show("noRegion", noRegion);
            try { Currency missing = Currency.getInstance(Locale.ENGLISH); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
        }
        {
            NumberFormat usInEuro = NumberFormat.getCurrencyInstance(Locale.US);
            usInEuro.setCurrency(Currency.getInstance("EUR"));
            String euroForUs = usInEuro.format(new BigDecimal("1234.56"));     // "€1,234.56"
            show("euroForUs", euroForUs);
        }
        {
            NumberFormat plain = NumberFormat.getCurrencyInstance(Locale.US);
            plain.setCurrency(Currency.getInstance("JPY"));
            String wrongYen = plain.format(new BigDecimal("1234.56"));                                         // "¥1,234.56"
            show("wrongYen", wrongYen);
            String rightYen = currencyFormat(Locale.US, Currency.getInstance("JPY")).format(new BigDecimal("1234.56"));   // "¥1,235"
            show("rightYen", rightYen);
        }
        {
            Locale accounting = Locale.forLanguageTag("en-US-u-cf-account");
            String loss = NumberFormat.getCurrencyInstance(accounting).format(new BigDecimal("-1234.50"));     // "($1,234.50)"
            show("loss", loss);
            String profit = NumberFormat.getCurrencyInstance(accounting).format(new BigDecimal("1234.50"));    // "$1,234.50"
            show("profit", profit);
        }
        {
            String french = NumberFormat.getCurrencyInstance(Locale.FRANCE).format(new BigDecimal("1234.56"));   // "1 234,56 €"
            show("french", french);
            boolean typed = french.equals("1 234,56 €");                          // false
            show("typed", typed);
            boolean exact = french.equals("1\u202f234,56\u00a0€");                // true
            show("exact", exact);
        }
        {
            NumberFormat usFormat = NumberFormat.getCurrencyInstance(Locale.US);
            Number parsed = usFormat.parse("$1,234.57");          // 1234.57
            show("parsed", parsed);
            try { Number noSymbol = usFormat.parse("1,234.57"); show("noSymbol", noSymbol); } catch (Throwable _t) { System.out.println("noSymbol -> " + _t); }
            Number partial = usFormat.parse("$12abc");            // 12
            show("partial", partial);
        }
        {
            Optional<BigDecimal> valid = parsePrice(" $1,234.57 ", Locale.US);   // Optional[1234.57]
            show("valid", valid);
            Optional<BigDecimal> invalid = parsePrice("$12abc", Locale.US);      // Optional.empty
            show("invalid", invalid);
            Optional<BigDecimal> german = parsePrice("1.234,57\u00a0€", Locale.GERMANY);   // Optional[1234.57]
            show("german", german);
        }
        {
            NumberFormat usd = NumberFormat.getCurrencyInstance(Locale.US);
            RoundingMode mode = usd.getRoundingMode();                 // HALF_EVEN
            show("mode", mode);
            String halfToEven = usd.format(new BigDecimal("0.125"));   // "$0.12"
            show("halfToEven", halfToEven);
            String halfToEven2 = usd.format(new BigDecimal("0.135"));  // "$0.14"
            show("halfToEven2", halfToEven2);
        }
        {
            NumberFormat dollars = NumberFormat.getCurrencyInstance(Locale.US);
            String fromDouble = dollars.format(2.675);                      // "$2.67"
            show("fromDouble", fromDouble);
            String fromBigDecimal = dollars.format(new BigDecimal("2.675"));  // "$2.68"
            show("fromBigDecimal", fromBigDecimal);
        }
        {
            NumberFormat halfUp = NumberFormat.getCurrencyInstance(Locale.US);
            halfUp.setRoundingMode(RoundingMode.HALF_UP);
            String receipt = halfUp.format(new BigDecimal("123456.785"));   // "$123,456.79"
            show("receipt", receipt);
            NumberFormat exactOnly = NumberFormat.getCurrencyInstance(Locale.US);
            exactOnly.setRoundingMode(RoundingMode.UNNECESSARY);
            try { String notExact = exactOnly.format(new BigDecimal("1.005")); show("notExact", notExact); } catch (Throwable _t) { System.out.println("notExact -> " + _t); }
        }
        {
            String rupees = NumberFormat.getCurrencyInstance(Locale.of("en", "IN")).format(new BigDecimal("1234567.89"));   // "₹1,234,567.89"
            show("rupees", rupees);
        }
        {
            NumberFormat amountOnly = NumberFormat.getNumberInstance(Locale.GERMANY);
            amountOnly.setMinimumFractionDigits(2);
            amountOnly.setMaximumFractionDigits(2);
            String plainAmount = amountOnly.format(new BigDecimal("1234.5"));   // "1.234,50"
            show("plainAmount", plainAmount);
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
