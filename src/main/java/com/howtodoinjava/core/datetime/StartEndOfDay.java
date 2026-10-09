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
 * Examples for the tutorial "Start and End of Day in Java: atStartOfDay and LocalTime.MAX".
 * https://howtodoinjava.com/java/date-time/start-and-end-of-day/
 */
public class StartEndOfDay {
    static boolean isOnDay(Instant timestamp, LocalDate day, ZoneId zone) {
        Instant from = day.atStartOfDay(zone).toInstant();
        Instant until = day.plusDays(1).atStartOfDay(zone).toInstant();
        return !timestamp.isBefore(from) && timestamp.isBefore(until);
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate day = LocalDate.of(2026, 5, 14);
            ZoneId store = ZoneId.of("America/New_York");
            LocalDateTime localStart = day.atStartOfDay();                         // 2026-05-14T00:00
            show("localStart", localStart);
            ZonedDateTime start = day.atStartOfDay(store);                         // 2026-05-14T00:00-04:00[America/New_York]
            show("start", start);
            ZonedDateTime nextStart = day.plusDays(1).atStartOfDay(store);         // 2026-05-15T00:00-04:00[America/New_York]
            show("nextStart", nextStart);
            LocalDateTime lastMoment = day.atTime(LocalTime.MAX);                  // 2026-05-14T23:59:59.999999999
            show("lastMoment", lastMoment);
        }
        {
            LocalDate day = LocalDate.of(2026, 5, 14);
            LocalDateTime startOfDay1 = day.atStartOfDay();                           // 2026-05-14T00:00
            show("startOfDay1", startOfDay1);
            LocalDateTime startOfDay2 = day.atTime(LocalTime.MIN);                    // 2026-05-14T00:00
            show("startOfDay2", startOfDay2);
            LocalDateTime startOfDay3 = LocalTime.MIN.atDate(day);                    // 2026-05-14T00:00
            show("startOfDay3", startOfDay3);
            ZonedDateTime startInParis = day.atStartOfDay(ZoneId.of("Europe/Paris"));  // 2026-05-14T00:00+02:00[Europe/Paris]
            show("startInParis", startInParis);
            Instant startInUtc = day.atStartOfDay(ZoneOffset.UTC).toInstant();        // 2026-05-14T00:00:00Z
            show("startInUtc", startInUtc);
        }
        {
            ZonedDateTime orderTime = ZonedDateTime.of(2026, 5, 14, 15, 42, 10, 0, ZoneId.of("America/New_York"));
            ZonedDateTime orderDayStart = orderTime.truncatedTo(ChronoUnit.DAYS);             // 2026-05-14T00:00-04:00[America/New_York]
            show("orderDayStart", orderDayStart);
            Instant utcDayStart = orderTime.toInstant().truncatedTo(ChronoUnit.DAYS);         // 2026-05-14T00:00:00Z
            show("utcDayStart", utcDayStart);
        }
        {
            ZoneId havana = ZoneId.of("America/Havana");
            ZonedDateTime havanaStart = LocalDate.of(2026, 3, 8).atStartOfDay(havana);                     // 2026-03-08T01:00-04:00[America/Havana]
            show("havanaStart", havanaStart);
            ZonedDateTime havanaNext = LocalDate.of(2026, 3, 9).atStartOfDay(havana);                      // 2026-03-09T00:00-04:00[America/Havana]
            show("havanaNext", havanaNext);
            Duration dayLength = Duration.between(havanaStart, havanaNext);                                 // PT23H
            show("dayLength", dayLength);
            LocalTime truncatedStart = ZonedDateTime.of(2026, 3, 8, 10, 0, 0, 0, havana).truncatedTo(ChronoUnit.DAYS).toLocalTime();   // 01:00
            show("truncatedStart", truncatedStart);
        }
        {
            LocalDate day = LocalDate.of(2026, 5, 14);
            LocalDateTime endOfDay = day.atTime(LocalTime.MAX);                                     // 2026-05-14T23:59:59.999999999
            show("endOfDay", endOfDay);
            LocalDateTime endOfDay2 = LocalTime.MAX.atDate(day);                                    // 2026-05-14T23:59:59.999999999
            show("endOfDay2", endOfDay2);
            ZonedDateTime endInParis = day.atTime(LocalTime.MAX).atZone(ZoneId.of("Europe/Paris"));  // 2026-05-14T23:59:59.999999999+02:00[Europe/Paris]
            show("endInParis", endInParis);
            LocalDateTime endToSecond = day.atTime(23, 59, 59);                                     // 2026-05-14T23:59:59
            show("endToSecond", endToSecond);
        }
        {
            LocalDate reportDay = LocalDate.of(2026, 5, 14);
            ZoneId newYork = ZoneId.of("America/New_York");
            boolean lateOrder = isOnDay(Instant.parse("2026-05-15T03:59:59.999Z"), reportDay, newYork);   // true
            show("lateOrder", lateOrder);
            boolean nextDayOrder = isOnDay(Instant.parse("2026-05-15T04:00:00Z"), reportDay, newYork);   // false
            show("nextDayOrder", nextDayOrder);
            OffsetDateTime fromParam = reportDay.atStartOfDay(newYork).toOffsetDateTime();              // 2026-05-14T00:00-04:00
            show("fromParam", fromParam);
            OffsetDateTime untilParam = reportDay.plusDays(1).atStartOfDay(newYork).toOffsetDateTime(); // 2026-05-15T00:00-04:00
            show("untilParam", untilParam);
        }
        {
            ZoneId tokyo = ZoneId.of("Asia/Tokyo");
            ZonedDateTime todayStart = LocalDate.now(tokyo).atStartOfDay(tokyo);   // start of today in Tokyo
            show("todayStart", todayStart);
        }
        {
            long startMillis = LocalDate.of(2026, 5, 14).atStartOfDay(ZoneId.of("America/New_York")).toInstant().toEpochMilli();   // 1778731200000
            show("startMillis", startMillis);
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
