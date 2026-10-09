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
 * Examples for the tutorial "Get the Day Name From a Date in Java (Localized)".
 * https://howtodoinjava.com/java/date-time/display-name-of-week-day/
 */
public class DisplayDayOfWeekName {
    static List<String> headerRow(Locale locale) {
        DayOfWeek first = WeekFields.of(locale).getFirstDayOfWeek();
        return IntStream.range(0, 7)
                .mapToObj(i -> first.plus(i).getDisplayName(TextStyle.SHORT_STANDALONE, locale))
                .toList();
    }
    static Optional<DayOfWeek> parseDayName(String text, Locale locale) {
        if (text == null || text.isBlank()) {
            return Optional.empty();
        }
        for (String pattern : List.of("EEEE", "EEE")) {
            DateTimeFormatter format = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive().appendPattern(pattern).toFormatter(locale);
            try {
                return Optional.of(DayOfWeek.from(format.parse(text.strip())));
            } catch (DateTimeParseException e) {
                // try the next pattern
            }
        }
        return Optional.empty();
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate date = LocalDate.of(2026, 9, 16);
            DayOfWeek day = date.getDayOfWeek();                                            // WEDNESDAY
            show("day", day);
            String full = day.getDisplayName(TextStyle.FULL, Locale.US);                    // "Wednesday"
            show("full", full);
            String shortName = day.getDisplayName(TextStyle.SHORT, Locale.US);              // "Wed"
            show("shortName", shortName);
            String narrow = day.getDisplayName(TextStyle.NARROW, Locale.US);                // "W"
            show("narrow", narrow);
            String french = day.getDisplayName(TextStyle.FULL, Locale.FRENCH);              // "mercredi"
            show("french", french);
            String withDate = date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM uuuu", Locale.US));   // "Wednesday, 16 September 2026"
            show("withDate", withDate);
        }
        {
            String germanShort = DayOfWeek.WEDNESDAY.getDisplayName(TextStyle.SHORT, Locale.GERMAN);               // "Mi."
            show("germanShort", germanShort);
            String germanHeader = DayOfWeek.WEDNESDAY.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.GERMAN);   // "Mi"
            show("germanHeader", germanHeader);
            String dutch = DayOfWeek.WEDNESDAY.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("nl"));         // "woensdag"
            show("dutch", dutch);
        }
        {
            LocalDate date = LocalDate.of(2026, 9, 16);
            String header = date.format(DateTimeFormatter.ofPattern("EEE dd.MM.", Locale.GERMAN));      // "Mi. 16.09."
            show("header", header);
            String usFull = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.US));          // "Wednesday, September 16, 2026"
            show("usFull", usFull);
            String germanFull = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.GERMANY)); // "Mittwoch, 16. September 2026"
            show("germanFull", germanFull);
        }
        {
            DayOfWeek day = LocalDate.of(2026, 9, 16).getDayOfWeek();
            String forLogs = day.toString();                                     // "WEDNESDAY"
            show("forLogs", forLogs);
            String forUsers = day.getDisplayName(TextStyle.FULL, Locale.UK);     // "Wednesday"
            show("forUsers", forUsers);
            int isoNumber = day.getValue();                                      // 3
            show("isoNumber", isoNumber);
        }
        {
            List<String> usRow = headerRow(Locale.US);           // [Sun, Mon, Tue, Wed, Thu, Fri, Sat]
            show("usRow", usRow);
            List<String> germanRow = headerRow(Locale.GERMANY);   // [Mo, Di, Mi, Do, Fr, Sa, So]
            show("germanRow", germanRow);
        }
        {
            DayOfWeek fromConstant = DayOfWeek.valueOf("WEDNESDAY");                                                   // WEDNESDAY
            show("fromConstant", fromConstant);
            DayOfWeek fromFrench = DayOfWeek.from(DateTimeFormatter.ofPattern("EEEE", Locale.FRENCH).parse("mercredi")); // WEDNESDAY
            show("fromFrench", fromFrench);
            try { DayOfWeek wrongCase = DayOfWeek.valueOf("Wednesday"); show("wrongCase", wrongCase); } catch (Throwable _t) { System.out.println("wrongCase -> " + _t); }
            try { DayOfWeek wrongLength = DayOfWeek.from(DateTimeFormatter.ofPattern("EEE", Locale.US).parse("Wednesday")); show("wrongLength", wrongLength); } catch (Throwable _t) { System.out.println("wrongLength -> " + _t); }
        }
        {
            Optional<DayOfWeek> german = parseDayName("mittwoch", Locale.GERMAN);   // Optional[WEDNESDAY]
            show("german", german);
            Optional<DayOfWeek> shortUs = parseDayName(" WED ", Locale.US);         // Optional[WEDNESDAY]
            show("shortUs", shortUs);
            Optional<DayOfWeek> unknown = parseDayName("someday", Locale.US);       // Optional.empty
            show("unknown", unknown);
        }
        {
            String[] weekdays = DateFormatSymbols.getInstance(Locale.US).getWeekdays();
            int length = weekdays.length;                                                         // 8
            show("length", length);
            String legacyName = weekdays[Calendar.WEDNESDAY];                                     // "Wednesday"
            show("legacyName", legacyName);
            String modernName = DayOfWeek.WEDNESDAY.getDisplayName(TextStyle.FULL, Locale.US);    // "Wednesday"
            show("modernName", modernName);
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
