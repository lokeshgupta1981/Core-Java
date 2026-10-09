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
 * Examples for the tutorial "Get Elapsed Time Since Midnight in Java (Seconds, Millis)".
 * https://howtodoinjava.com/java/date-time/get-elapsed-time-since-midnight/
 */
public class ElapsedTimeSinceMidnight {
    static Duration untilNextMidnight(ZonedDateTime now) {
        ZonedDateTime nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay(now.getZone());
        return Duration.between(now, nextMidnight);
    }
    public static void main(String[] args) throws Exception {
        {
            LocalTime time = LocalTime.of(14, 30, 15, 250_000_000);
            int seconds = time.toSecondOfDay();                                       // 52215
            show("seconds", seconds);
            long millis = time.getLong(ChronoField.MILLI_OF_DAY);                     // 52215250
            show("millis", millis);
            long nanos = time.toNanoOfDay();                                          // 52215250000000
            show("nanos", nanos);
            int londonSeconds = LocalTime.now(ZoneId.of("Europe/London")).toSecondOfDay();   // seconds since midnight in London at this moment
            show("londonSeconds", londonSeconds);
        }
        {
            LocalTime time = LocalTime.of(14, 30, 15, 250_000_000);
            long secondsBetween = ChronoUnit.SECONDS.between(LocalTime.MIDNIGHT, time);   // 52215
            show("secondsBetween", secondsBetween);
            long millisBetween = ChronoUnit.MILLIS.between(LocalTime.MIDNIGHT, time);     // 52215250
            show("millisBetween", millisBetween);
            int minuteOfDay = time.get(ChronoField.MINUTE_OF_DAY);                        // 870
            show("minuteOfDay", minuteOfDay);
            Duration sinceMidnight = Duration.between(LocalTime.MIDNIGHT, time);          // PT14H30M15.25S
            show("sinceMidnight", sinceMidnight);
            double hours = sinceMidnight.toMillis() / 3_600_000.0;                       // 14.504236111111112
            show("hours", hours);
        }
        {
            Clock clock = Clock.fixed(Instant.parse("2026-10-10T08:15:30Z"), ZoneOffset.UTC);
            int utcSeconds = LocalTime.now(clock).toSecondOfDay();                                        // 29730
            show("utcSeconds", utcSeconds);
            int newYorkSeconds = LocalTime.now(clock.withZone(ZoneId.of("America/New_York"))).toSecondOfDay();   // 15330
            show("newYorkSeconds", newYorkSeconds);
            int kolkataSeconds = LocalTime.now(clock.withZone(ZoneId.of("Asia/Kolkata"))).toSecondOfDay();       // 49530
            show("kolkataSeconds", kolkataSeconds);
            long utcMillisNow = LocalTime.now(ZoneOffset.UTC).getLong(ChronoField.MILLI_OF_DAY);        // milliseconds since UTC midnight at this moment
            show("utcMillisNow", utcMillisNow);
        }
        {
            ZoneId berlin = ZoneId.of("Europe/Berlin");
            ZonedDateTime springForward = ZonedDateTime.of(2026, 3, 29, 10, 0, 0, 0, berlin);
            int wallClockSeconds = springForward.toLocalTime().toSecondOfDay();                                 // 36000
            show("wallClockSeconds", wallClockSeconds);
            long realSeconds = Duration.between(springForward.toLocalDate().atStartOfDay(berlin), springForward).toSeconds();   // 32400
            show("realSeconds", realSeconds);
            ZonedDateTime fallBack = ZonedDateTime.of(2026, 10, 25, 10, 0, 0, 0, berlin);
            long realFallBack = Duration.between(fallBack.toLocalDate().atStartOfDay(berlin), fallBack).toSeconds();          // 39600
            show("realFallBack", realFallBack);
        }
        {
            ZoneId santiago = ZoneId.of("America/Santiago");
            ZonedDateTime dayStart = LocalDate.of(2026, 9, 6).atStartOfDay(santiago);    // 2026-09-06T01:00-03:00[America/Santiago]
            show("dayStart", dayStart);
            ZonedDateTime noon = ZonedDateTime.of(2026, 9, 6, 12, 0, 0, 0, santiago);
            long santiagoSeconds = Duration.between(dayStart, noon).toSeconds();         // 39600
            show("santiagoSeconds", santiagoSeconds);
        }
        {
            LocalDateTime orderTime = LocalDateTime.of(2026, 10, 10, 19, 5, 40);
            long orderSeconds = ChronoUnit.SECONDS.between(orderTime.toLocalDate().atStartOfDay(), orderTime);   // 68740
            show("orderSeconds", orderSeconds);
            LocalDateTime dayBegin = orderTime.truncatedTo(ChronoUnit.DAYS);                                     // 2026-10-10T00:00
            show("dayBegin", dayBegin);
            ZonedDateTime zoned = ZonedDateTime.of(2026, 10, 10, 19, 5, 40, 0, ZoneId.of("Asia/Tokyo"));
            long zonedSeconds = ChronoUnit.SECONDS.between(zoned.toLocalDate().atStartOfDay(zoned.getZone()), zoned);   // 68740
            show("zonedSeconds", zonedSeconds);
        }
        {
            ZonedDateTime current = ZonedDateTime.now(ZoneId.of("Europe/Paris"));                     // one reading of the clock
            show("current", current);
            long safeSeconds = Duration.between(current.toLocalDate().atStartOfDay(current.getZone()), current).toSeconds();   // real seconds since midnight in Paris
            show("safeSeconds", safeSeconds);
        }
        {
            ZonedDateTime requestTime = ZonedDateTime.of(2026, 10, 10, 20, 15, 30, 0, ZoneOffset.UTC);
            Duration remaining = untilNextMidnight(requestTime);                       // PT3H44M30S
            show("remaining", remaining);
            long retryAfter = remaining.toSeconds();                                   // 13470
            show("retryAfter", retryAfter);
            int simpleRemaining = 86_400 - requestTime.toLocalTime().toSecondOfDay();  // 13470
            show("simpleRemaining", simpleRemaining);
        }
        {
            long viaLocalTime = LocalTime.now(ZoneOffset.UTC).getLong(ChronoField.MILLI_OF_DAY);   // milliseconds since UTC midnight
            show("viaLocalTime", viaLocalTime);
            long viaEpoch = System.currentTimeMillis() % 86_400_000L;                           // the same value from the epoch
            show("viaEpoch", viaEpoch);
        }
        {
            LocalTime fromSeconds = LocalTime.ofSecondOfDay(52215);        // 14:30:15
            show("fromSeconds", fromSeconds);
            try { LocalTime tooLarge = LocalTime.ofSecondOfDay(86_400); show("tooLarge", tooLarge); } catch (Throwable _t) { System.out.println("tooLarge -> " + _t); }
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
