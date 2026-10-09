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
 * Examples for the tutorial "Next and Previous Date in Java (Tomorrow and Yesterday)".
 * https://howtodoinjava.com/java/date-time/java8-next-previous-date/
 */
public class FindNextPrevDay {
    static TemporalAdjuster nextWorkingDay() {
        return temporal -> {
            LocalDate day = LocalDate.from(temporal).plusDays(1);
            while (day.getDayOfWeek() == DayOfWeek.SATURDAY || day.getDayOfWeek() == DayOfWeek.SUNDAY) {
                day = day.plusDays(1);
            }
            return temporal.with(day);
        };
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate date = LocalDate.of(2026, 2, 28);
            LocalDate next = date.plusDays(1);                        // 2026-03-01
            show("next", next);
            LocalDate previous = date.minusDays(1);                   // 2026-02-27
            show("previous", previous);
            Clock clock = Clock.fixed(Instant.parse("2026-10-10T20:00:00Z"), ZoneId.of("Asia/Kolkata"));
            LocalDate tomorrowInIndia = LocalDate.now(clock).plusDays(1);   // 2026-10-12
            show("tomorrowInIndia", tomorrowInIndia);
        }
        {
            LocalDate yearEnd = LocalDate.of(2026, 12, 31);
            LocalDate newYear = yearEnd.plus(1, ChronoUnit.DAYS);          // 2027-01-01
            show("newYear", newYear);
            LocalDate leapDay = LocalDate.of(2028, 3, 1).minusDays(1);     // 2028-02-29
            show("leapDay", leapDay);
            List<LocalDate> weekend = LocalDate.of(2026, 10, 10).datesUntil(LocalDate.of(2026, 10, 12)).toList();   // [2026-10-10, 2026-10-11]
            show("weekend", weekend);
        }
        {
            LocalDate tomorrow = LocalDate.now(ZoneId.of("Europe/Berlin")).plusDays(1);    // tomorrow's date in Berlin
            show("tomorrow", tomorrow);
            LocalDate yesterday = LocalDate.now(ZoneId.of("Europe/Berlin")).minusDays(1);  // yesterday's date in Berlin
            show("yesterday", yesterday);
        }
        {
            Instant jobRun = Instant.parse("2026-10-10T20:00:00Z");
            LocalDate yesterdayUtc = LocalDate.now(Clock.fixed(jobRun, ZoneOffset.UTC)).minusDays(1);                // 2026-10-09
            show("yesterdayUtc", yesterdayUtc);
            LocalDate yesterdayKolkata = LocalDate.now(Clock.fixed(jobRun, ZoneId.of("Asia/Kolkata"))).minusDays(1); // 2026-10-10
            show("yesterdayKolkata", yesterdayKolkata);
        }
        {
            ZoneId newYork = ZoneId.of("America/New_York");
            ZonedDateTime midnight = ZonedDateTime.of(2026, 3, 8, 0, 0, 0, 0, newYork);
            ZonedDateTime nextMidnight = midnight.plusDays(1);                               // 2026-03-09T00:00-04:00[America/New_York]
            show("nextMidnight", nextMidnight);
            ZonedDateTime plusMillis = midnight.toInstant().plusMillis(86_400_000L).atZone(newYork);   // 2026-03-09T01:00-04:00[America/New_York]
            show("plusMillis", plusMillis);
        }
        {
            LocalDate saturday = LocalDate.of(2026, 10, 10);
            LocalDate nextMonday = saturday.with(TemporalAdjusters.next(DayOfWeek.MONDAY));          // 2026-10-12
            show("nextMonday", nextMonday);
            LocalDate lastFriday = saturday.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));      // 2026-10-09
            show("lastFriday", lastFriday);
            LocalDate sameDay = saturday.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));    // 2026-10-10
            show("sameDay", sameDay);
        }
        {
            LocalDate friday = LocalDate.of(2026, 10, 9);
            LocalDate afterFriday = friday.with(nextWorkingDay());                 // 2026-10-12
            show("afterFriday", afterFriday);
            LocalDate afterTuesday = LocalDate.of(2026, 10, 13).with(nextWorkingDay());  // 2026-10-14
            show("afterTuesday", afterTuesday);
        }
        {
            Date legacy = Date.from(Instant.parse("2026-03-07T23:30:00Z"));
            ZoneId zone = ZoneId.of("America/New_York");
            LocalDate legacyDay = legacy.toInstant().atZone(zone).toLocalDate();     // 2026-03-07
            show("legacyDay", legacyDay);
            LocalDate legacyNext = legacyDay.plusDays(1);                            // 2026-03-08
            show("legacyNext", legacyNext);
            Date nextAsDate = Date.from(legacyNext.atStartOfDay(zone).toInstant());
            Instant nextStart = nextAsDate.toInstant();                              // 2026-03-08T05:00:00Z
            show("nextStart", nextStart);
        }
        {
            LocalDate today = LocalDate.of(2026, 10, 10);
            LocalDate orderDay = LocalDate.of(2026, 10, 9);
            boolean isYesterday = orderDay.isEqual(today.minusDays(1));    // true
            show("isYesterday", isYesterday);
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
