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
 * Examples for the tutorial "Convert Month Number to Name and Back in Java".
 * https://howtodoinjava.com/java/date-time/convert-between-month-name-and-number/
 */
public class ConvertBetweenMonthNameAndNumber {
    static String monthNumberToAbbr(int monthNumber, Locale locale) {
        return Month.of(monthNumber).getDisplayName(TextStyle.SHORT, locale);
    }

    static String monthNumberToFullName(int monthNumber, Locale locale) {
        return Month.of(monthNumber).getDisplayName(TextStyle.FULL, locale);
    }

    static String monthNumberToName(int monthNumber) {
        return Month.of(monthNumber).name();
    }
    static int monthNameToNumber(String monthName) {
        return Month.valueOf(monthName.strip().toUpperCase(Locale.ROOT)).getValue();
    }
    static OptionalInt monthNumber(String text, Locale locale) {
        if (text == null || text.isBlank()) {
            return OptionalInt.empty();
        }
        for (String pattern : List.of("MMMM", "MMM")) {
            DateTimeFormatter format = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive().appendPattern(pattern).toFormatter(locale);
            try {
                return OptionalInt.of(Month.from(format.parse(text.strip())).getValue());
            } catch (DateTimeParseException e) {
                // try the next pattern
            }
        }
        return OptionalInt.empty();
    }
    static Optional<String> monthAbbrToFullName(String abbreviation, Locale locale) {
        OptionalInt number = monthNumber(abbreviation, locale);
        if (number.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Month.of(number.getAsInt()).getDisplayName(TextStyle.FULL, locale));
    }
    public static void main(String[] args) throws Exception {
        {
            String full = Month.of(1).getDisplayName(TextStyle.FULL, Locale.US);              // "January"
            show("full", full);
            String abbr = Month.of(1).getDisplayName(TextStyle.SHORT, Locale.US);              // "Jan"
            show("abbr", abbr);
            String constant = Month.of(1).name();                                              // "JANUARY"
            show("constant", constant);
            int fromName = Month.valueOf("MARCH").getValue();                                  // 3
            show("fromName", fromName);
            Month parsed = Month.from(DateTimeFormatter.ofPattern("MMM", Locale.US).parse("Mar"));   // MARCH
            show("parsed", parsed);
            int fromAbbr = parsed.getValue();                                                  // 3
            show("fromAbbr", fromAbbr);
        }
        {
            String abbr = monthNumberToAbbr(1, Locale.US);               // "Jan"
            show("abbr", abbr);
            String full = monthNumberToFullName(1, Locale.US);           // "January"
            show("full", full);
            String name = monthNumberToName(1);                          // "JANUARY"
            show("name", name);
            try { String bad = monthNumberToFullName(13, Locale.US); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            LocalDate statementDate = LocalDate.of(2026, 3, 5);
            Month month = statementDate.getMonth();                                                 // MARCH
            show("month", month);
            int monthNumber = statementDate.getMonthValue();                                        // 3
            show("monthNumber", monthNumber);
            String monthText = statementDate.format(DateTimeFormatter.ofPattern("MMMM", Locale.US));  // "March"
            show("monthText", monthText);
        }
        {
            Locale czech = Locale.forLanguageTag("cs");
            String inDate = Month.JANUARY.getDisplayName(TextStyle.FULL, czech);                // "ledna"
            show("inDate", inDate);
            String onItsOwn = Month.JANUARY.getDisplayName(TextStyle.FULL_STANDALONE, czech);   // "leden"
            show("onItsOwn", onItsOwn);
        }
        {
            int january = monthNameToNumber("January");    // 1
            show("january", january);
            int march = monthNameToNumber("march");         // 3
            show("march", march);
            try { int jan = monthNameToNumber("Jan"); show("jan", jan); } catch (Throwable _t) { System.out.println("jan -> " + _t); }
        }
        {
            OptionalInt fromFull = monthNumber("January", Locale.US);         // OptionalInt[1]
            show("fromFull", fromFull);
            OptionalInt fromAbbr = monthNumber("SEP", Locale.US);             // OptionalInt[9]
            show("fromAbbr", fromAbbr);
            OptionalInt fromGerman = monthNumber("januar", Locale.GERMAN);    // OptionalInt[1]
            show("fromGerman", fromGerman);
            OptionalInt unknown = monthNumber("Smarch", Locale.US);           // OptionalInt.empty
            show("unknown", unknown);
        }
        {
            Optional<String> april = monthAbbrToFullName("Apr", Locale.US);           // Optional[April]
            show("april", april);
            Optional<String> janvier = monthAbbrToFullName("janv.", Locale.FRENCH);    // Optional[janvier]
            show("janvier", janvier);
            Optional<String> nothing = monthAbbrToFullName("Xyz", Locale.US);          // Optional.empty
            show("nothing", nothing);
        }
        {
            DateTimeFormatter usPeriod = DateTimeFormatter.ofPattern("MMM uuuu", Locale.US);
            YearMonth september = YearMonth.parse("Sep 2026", usPeriod);                                 // 2026-09
            show("september", september);
            int monthNumber = september.getMonthValue();                                                 // 9
            show("monthNumber", monthNumber);
            YearMonth ukSeptember = YearMonth.parse("Sept 2026", DateTimeFormatter.ofPattern("MMM uuuu", Locale.UK));   // 2026-09
            show("ukSeptember", ukSeptember);
            try { YearMonth ukFails = YearMonth.parse("Sep 2026", DateTimeFormatter.ofPattern("MMM uuuu", Locale.UK)); show("ukFails", ukFails); } catch (Throwable _t) { System.out.println("ukFails -> " + _t); }
        }
        {
            Calendar calendar = new GregorianCalendar(2026, Calendar.MARCH, 5);
            int legacyMonth = calendar.get(Calendar.MONTH);                    // 2
            show("legacyMonth", legacyMonth);
            Month month = Month.of(legacyMonth + 1);                           // MARCH
            show("month", month);
            int symbols = DateFormatSymbols.getInstance(Locale.US).getMonths().length;   // 13
            show("symbols", symbols);
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
