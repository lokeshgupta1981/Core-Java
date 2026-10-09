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
 * Examples for the tutorial "Java LocalTime: Time of Day Without a Date (With Examples)".
 * https://howtodoinjava.com/java/date-time/java-localtime/
 */
public class LocalTimeExamples {
    static Optional<LocalTime> parseClock(String text) {
        try {
            return Optional.of(LocalTime.parse(text.strip(), CLOCK));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    static final DateTimeFormatter CLOCK = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("h:mm a")
            .toFormatter(Locale.US);
    static boolean isWithin(LocalTime time, LocalTime start, LocalTime end) {
        if (!start.isAfter(end)) {
            return !time.isBefore(start) && time.isBefore(end);   // same-day range
        }
        return !time.isBefore(start) || time.isBefore(end);       // range crosses midnight
    }
    public static void main(String[] args) throws Exception {
        {
            LocalTime opens = LocalTime.of(7, 30);                                  // 07:30
            show("opens", opens);
            LocalTime closes = LocalTime.parse("18:00");                            // 18:00
            show("closes", closes);
            LocalTime order = LocalTime.of(17, 45, 20);                             // 17:45:20
            show("order", order);
            boolean isOpen = !order.isBefore(opens) && order.isBefore(closes);     // true
            show("isOpen", isOpen);
            LocalTime lastCall = closes.minusMinutes(15);                           // 17:45
            show("lastCall", lastCall);
            long minutesLeft = Duration.between(order, closes).toMinutes();         // 14
            show("minutesLeft", minutesLeft);
            String label = closes.format(DateTimeFormatter.ofPattern("h:mm a", Locale.US));   // "6:00 PM"
            show("label", label);
        }
        {
            LocalTime now = LocalTime.now();                                     // current time in the default zone
            show("now", now);
            LocalTime londonNow = LocalTime.now(ZoneId.of("Europe/London"));     // current time in London
            show("londonNow", londonNow);
        }
        {
            LocalTime hm = LocalTime.of(8, 20);                     // 08:20
            show("hm", hm);
            LocalTime hms = LocalTime.of(8, 20, 45);                // 08:20:45
            show("hms", hms);
            LocalTime withNanos = LocalTime.of(8, 20, 45, 60000);   // 08:20:45.000060
            show("withNanos", withNanos);
            LocalTime fromSeconds = LocalTime.ofSecondOfDay(3661);  // 01:01:01
            show("fromSeconds", fromSeconds);
            LocalTime parsed = LocalTime.parse("08:20:45.6");       // 08:20:45.600
            show("parsed", parsed);
            try { LocalTime tooLate = LocalTime.of(24, 0); show("tooLate", tooLate); } catch (Throwable _t) { System.out.println("tooLate -> " + _t); }
        }
        {
            LocalTime iso = LocalTime.parse("08:20:45.123456789");                                   // 08:20:45.123456789
            show("iso", iso);
            LocalTime dotted = LocalTime.parse("08.20.45.123456789", DateTimeFormatter.ofPattern("HH.mm.ss.nnn"));   // 08:20:45.123456789
            show("dotted", dotted);
            LocalTime twelveHour = LocalTime.parse("2:30 PM", DateTimeFormatter.ofPattern("h:mm a", Locale.US));    // 14:30
            show("twelveHour", twelveHour);
            try { LocalTime noPadding = LocalTime.parse("8:20"); show("noPadding", noPadding); } catch (Throwable _t) { System.out.println("noPadding -> " + _t); }
        }
        {
            Optional<LocalTime> lower = parseClock("2:30 pm");     // Optional[14:30]
            show("lower", lower);
            Optional<LocalTime> broken = parseClock("25:00");      // Optional.empty
            show("broken", broken);
        }
        {
            LocalTime time = LocalTime.of(14, 5);
            String clock24 = time.format(DateTimeFormatter.ofPattern("HH:mm"));                      // "14:05"
            show("clock24", clock24);
            String clock12 = time.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));         // "02:05 PM"
            show("clock12", clock12);
            String noAmPm = time.format(DateTimeFormatter.ofPattern("hh:mm"));                       // "02:05", ambiguous
            show("noAmPm", noAmPm);
            String dayPeriod = time.format(DateTimeFormatter.ofPattern("h:mm B", Locale.US));        // "2:05 in the afternoon"
            show("dayPeriod", dayPeriod);
        }
        {
            LocalTime backup = LocalTime.of(23, 30);
            LocalTime twoHoursLater = backup.plusHours(2);                 // 01:30
            show("twoHoursLater", twoHoursLater);
            LocalTime earlier = LocalTime.of(0, 15).minusMinutes(30);      // 23:45
            show("earlier", earlier);
            LocalTime later = backup.plus(Duration.ofMinutes(45));         // 00:15
            show("later", later);
            LocalTime precise = LocalTime.of(9, 5, 30, 123_000_000);
            LocalTime toMinute = precise.truncatedTo(ChronoUnit.MINUTES);  // 09:05
            show("toMinute", toMinute);
            LocalTime atTen = precise.withHour(10);                        // 10:05:30.123
            show("atTen", atTen);
        }
        {
            LocalTime nine = LocalTime.of(9, 0);
            LocalTime noon = LocalTime.NOON;
            boolean before = nine.isBefore(noon);                      // true
            show("before", before);
            boolean after = nine.isAfter(noon);                        // false
            show("after", after);
            boolean same = nine.equals(LocalTime.parse("09:00"));     // true
            show("same", same);
        }
        {
            LocalTime opens = LocalTime.of(7, 30);
            LocalTime closes = LocalTime.of(18, 0);
            boolean cafeOpen = isWithin(LocalTime.of(12, 10), opens, closes);                        // true
            show("cafeOpen", cafeOpen);
            boolean cafeClosed = isWithin(LocalTime.of(18, 0), opens, closes);                       // false
            show("cafeClosed", cafeClosed);
            boolean nightShift = isWithin(LocalTime.of(2, 15), LocalTime.of(22, 0), LocalTime.of(6, 0));    // true
            show("nightShift", nightShift);
            boolean dayTime = isWithin(LocalTime.of(14, 0), LocalTime.of(22, 0), LocalTime.of(6, 0));       // false
            show("dayTime", dayTime);
        }
        {
            LocalTime start = LocalTime.of(22, 0);
            LocalTime end = LocalTime.of(6, 0);
            Duration raw = Duration.between(start, end);                          // PT-16H
            show("raw", raw);
            Duration shift = raw.isNegative() ? raw.plusDays(1) : raw;            // PT8H
            show("shift", shift);
            long minutes = ChronoUnit.MINUTES.between(LocalTime.of(9, 15), LocalTime.of(10, 0));   // 45
            show("minutes", minutes);
            int secondOfDay = LocalTime.of(17, 45).toSecondOfDay();               // 63900
            show("secondOfDay", secondOfDay);
        }
        {
            LocalTime time = LocalTime.of(10, 0);
            LocalDateTime meeting = time.atDate(LocalDate.of(2026, 5, 14));        // 2026-05-14T10:00
            show("meeting", meeting);
            OffsetTime withOffset = time.atOffset(ZoneOffset.ofHours(2));          // 10:00+02:00
            show("withOffset", withOffset);
            LocalTime back = meeting.toLocalTime();                                // 10:00
            show("back", back);
        }
        {
            LocalTime exact = LocalTime.of(10, 15, 30, 500_000_000);
            java.sql.Time sqlTime = java.sql.Time.valueOf(exact);     // 10:15:30
            show("sqlTime", sqlTime);
            LocalTime restored = sqlTime.toLocalTime();               // 10:15:30, the 0.5 seconds are lost
            show("restored", restored);
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
