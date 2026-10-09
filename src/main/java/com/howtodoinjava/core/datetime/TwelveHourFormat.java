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
 * Examples for the tutorial "Java 12-Hour Format With AM/PM (hh:mm a Examples)".
 * https://howtodoinjava.com/java/date-time/format-time-12-hours-pattern/
 */
public class TwelveHourFormat {

    public static void main(String[] args) throws Exception {
        {
            DateTimeFormatter twelveHour = DateTimeFormatter.ofPattern("hh:mm a", Locale.US);
            String evening = LocalTime.of(19, 35).format(twelveHour);    // "07:35 PM"
            show("evening", evening);
            String midnight = LocalTime.MIDNIGHT.format(twelveHour);     // "12:00 AM"
            show("midnight", midnight);
            String noon = LocalTime.NOON.format(twelveHour);             // "12:00 PM"
            show("noon", noon);
            LocalTime parsed = LocalTime.parse("07:35 PM", twelveHour);  // 19:35
            show("parsed", parsed);
        }
        {
            String mixed = LocalTime.of(19, 35).format(DateTimeFormatter.ofPattern("HH:mm a", Locale.US));   // "19:35 PM"
            show("mixed", mixed);
            String noMarker = LocalTime.of(19, 35).format(DateTimeFormatter.ofPattern("hh:mm"));          // "07:35"
            show("noMarker", noMarker);
            try { String twoA = LocalTime.of(19, 35).format(DateTimeFormatter.ofPattern("hh:mm aa")); show("twoA", twoA); } catch (Throwable _t) { System.out.println("twoA -> " + _t); }
        }
        {
            LocalDateTime reservation = LocalDateTime.of(2026, 3, 14, 19, 35);
            String confirmation = reservation.format(DateTimeFormatter.ofPattern("MMM d, uuuu h:mm a", Locale.US));       // "Mar 14, 2026 7:35 PM"
            show("confirmation", confirmation);
            ZonedDateTime newYork = reservation.atZone(ZoneId.of("America/New_York"));
            String zoned = newYork.format(DateTimeFormatter.ofPattern("MMM d, h:mm a z", Locale.US));            // "Mar 14, 7:35 PM EDT"
            show("zoned", zoned);
            ZonedDateTime tokyo = newYork.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
            String tokyoText = tokyo.format(DateTimeFormatter.ofPattern("MMM d, h:mm a z", Locale.US));          // "Mar 15, 8:35 AM JST"
            show("tokyoText", tokyoText);
        }
        {
            LocalTime nowInChicago = LocalTime.now(ZoneId.of("America/Chicago"));
            String clock = nowInChicago.format(DateTimeFormatter.ofPattern("h:mm a", Locale.US));   // current Chicago time, such as "9:41 AM"
            show("clock", clock);
        }
        {
            LocalTime evening = LocalTime.of(19, 35);
            String us = evening.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));                   // "07:35 PM"
            show("us", us);
            String uk = evening.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.UK));                   // "07:35 pm"
            show("uk", uk);
            String canada = evening.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.CANADA));           // "07:35 p.m."
            show("canada", canada);
            String india = evening.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.of("en", "IN")));    // "07:35 pm"
            show("india", india);
            String germany = evening.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.GERMANY));         // "07:35 PM"
            show("germany", germany);
        }
        {
            DateTimeFormatter fixedMarker = new DateTimeFormatterBuilder()
                    .appendPattern("hh:mm ")
                    .appendText(ChronoField.AMPM_OF_DAY, Map.of(0L, "AM", 1L, "PM"))
                    .toFormatter(Locale.UK);
            String alwaysUpper = LocalTime.of(19, 35).format(fixedMarker);   // "07:35 PM"
            show("alwaysUpper", alwaysUpper);
        }
        {
            DateTimeFormatter shortUs = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.US);
            String usText = LocalTime.of(19, 35).format(shortUs);            // 7:35 PM, with U+202F before PM
            show("usText", usText);
            boolean plainSpace = usText.equals("7:35 PM");                   // false
            show("plainSpace", plainSpace);
            String german = LocalTime.of(19, 35).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.GERMANY));   // "19:35"
            show("german", german);
        }
        {
            DateTimeFormatter shortUs = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.US);
            try { LocalTime typed = LocalTime.parse("7:35 PM", shortUs); show("typed", typed); } catch (Throwable _t) { System.out.println("typed -> " + _t); }
            DateTimeFormatter loose = new DateTimeFormatterBuilder()
                    .parseLenient()
                    .appendLocalized(null, FormatStyle.SHORT)
                    .toFormatter(Locale.US);
            LocalTime accepted = LocalTime.parse("7:35 PM", loose);          // 19:35
            show("accepted", accepted);
        }
        {
            DateTimeFormatter period = DateTimeFormatter.ofPattern("h:mm B", Locale.US);
            String morning = LocalTime.of(9, 0).format(period);       // "9:00 in the morning"
            show("morning", morning);
            String afternoon = LocalTime.of(15, 0).format(period);    // "3:00 in the afternoon"
            show("afternoon", afternoon);
            String night = LocalTime.of(22, 0).format(period);        // "10:00 at night"
            show("night", night);
            String midday = LocalTime.NOON.format(period);            // "12:00 noon"
            show("midday", midday);
        }
        {
            DateTimeFormatter h24 = DateTimeFormatter.ofPattern("HH:mm");
            DateTimeFormatter h12 = DateTimeFormatter.ofPattern("hh:mm a", Locale.US);
            String to12 = LocalTime.parse("19:35", h24).format(h12);       // "07:35 PM"
            show("to12", to12);
            String to24 = LocalTime.parse("07:35 PM", h12).format(h24);    // "19:35"
            show("to24", to24);
            String midnight24 = LocalTime.parse("12:15 AM", h12).format(h24);   // "00:15"
            show("midnight24", midnight24);
        }
        {
            DateTimeFormatter h12 = DateTimeFormatter.ofPattern("hh:mm a", Locale.US);
            try { LocalTime lowercase = LocalTime.parse("07:35 pm", h12); show("lowercase", lowercase); } catch (Throwable _t) { System.out.println("lowercase -> " + _t); }
            try { LocalTime hour13 = LocalTime.parse("13:35 PM", h12); show("hour13", hour13); } catch (Throwable _t) { System.out.println("hour13 -> " + _t); }
            LocalTime oneDigit = LocalTime.parse("7:35 PM", DateTimeFormatter.ofPattern("h:mm a", Locale.US));   // 19:35
            show("oneDigit", oneDigit);
            DateTimeFormatter anyCase = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("h:mm a")
                    .toFormatter(Locale.US);
            LocalTime relaxed = LocalTime.parse("07:35 pm", anyCase);                                // 19:35
            show("relaxed", relaxed);
        }
        {
            int marker = LocalTime.of(19, 35).get(ChronoField.AMPM_OF_DAY);   // 1
            show("marker", marker);
            boolean isMorning = LocalTime.of(9, 0).isBefore(LocalTime.NOON);    // true
            show("isMorning", isMorning);
        }
        {
            Date legacy = Date.from(Instant.parse("2026-03-14T19:35:00Z"));
            String fromDate = legacy.toInstant().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));   // "07:35 PM"
            show("fromDate", fromDate);
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
