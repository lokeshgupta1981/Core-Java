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
 * Examples for the tutorial "Convert Date to LocalDate in Java (and LocalDate to Date)".
 * https://howtodoinjava.com/java/date-time/localdate-to-date/
 */
public class LocalDateToDate {
    static final class LegacyDates {
        static final ZoneId LIBRARY_ZONE = ZoneId.of("Asia/Kolkata");

        private LegacyDates() {
        }

        static LocalDate toLocalDate(Date date) {
            if (date == null) {
                return null;                                  // legacy APIs use null for "no date"
            }
            if (date instanceof java.sql.Date sqlDate) {
                return sqlDate.toLocalDate();                 // toInstant() would throw here
            }
            return LocalDate.ofInstant(date.toInstant(), LIBRARY_ZONE);
        }

        static Date toDate(LocalDate localDate) {
            return localDate == null ? null : Date.from(localDate.atStartOfDay(LIBRARY_ZONE).toInstant());
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Date fromLegacy = Date.from(Instant.parse("2026-03-13T20:00:00Z"));
            ZoneId zone = ZoneId.of("Asia/Kolkata");
            LocalDate dueDate = LocalDate.ofInstant(fromLegacy.toInstant(), zone);   // 2026-03-14
            show("dueDate", dueDate);
            LocalDate extended = dueDate.plusWeeks(1);                               // 2026-03-21
            show("extended", extended);
            Date toLegacy = Date.from(extended.atStartOfDay(zone).toInstant());
            Instant sent = toLegacy.toInstant();                                     // 2026-03-20T18:30:00Z
            show("sent", sent);
        }
        {
            Date midnightIndia = Date.from(Instant.parse("2026-03-13T18:30:00Z"));
            LocalDate inIndia = LocalDate.ofInstant(midnightIndia.toInstant(), ZoneId.of("Asia/Kolkata"));   // 2026-03-14
            show("inIndia", inIndia);
            LocalDate inUtc = LocalDate.ofInstant(midnightIndia.toInstant(), ZoneOffset.UTC);                // 2026-03-13
            show("inUtc", inUtc);
        }
        {
            Date legacy = new Date(1773426600000L);
            LocalDate date = LocalDate.ofInstant(legacy.toInstant(), ZoneId.of("Europe/Paris"));   // 2026-03-13
            show("date", date);
        }
        {
            Date legacy = new Date(1773426600000L);
            ZoneId paris = ZoneId.of("Europe/Paris");
            LocalDate viaZoned = legacy.toInstant().atZone(paris).toLocalDate();               // 2026-03-13
            show("viaZoned", viaZoned);
            LocalDate viaMillis = Instant.ofEpochMilli(legacy.getTime()).atZone(paris).toLocalDate();   // 2026-03-13
            show("viaMillis", viaMillis);
        }
        {
            Date fromDao = java.sql.Date.valueOf(LocalDate.of(2026, 3, 14));
            try { Instant broken = fromDao.toInstant(); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            Date fromDao = java.sql.Date.valueOf(LocalDate.of(2026, 3, 14));
            LocalDate safe = (fromDao instanceof java.sql.Date sql) ? sql.toLocalDate() : LocalDate.ofInstant(fromDao.toInstant(), ZoneId.of("Asia/Kolkata"));   // 2026-03-14
            show("safe", safe);
        }
        {
            LocalDate renewal = LocalDate.of(2026, 3, 14);
            ZoneId zone = ZoneId.of("Asia/Kolkata");
            Date legacyDate = Date.from(renewal.atStartOfDay(zone).toInstant());
            long millis = legacyDate.getTime();                     // 1773426600000
            show("millis", millis);
            Instant moment = legacyDate.toInstant();                // 2026-03-13T18:30:00Z
            show("moment", moment);
        }
        {
            LocalDate dstDay = LocalDate.of(2026, 9, 6);
            ZonedDateTime start = dstDay.atStartOfDay(ZoneId.of("America/Santiago"));   // 2026-09-06T01:00-03:00[America/Santiago]
            show("start", start);
        }
        {
            Date memberSince = Date.from(Instant.parse("2025-12-31T19:00:00Z"));
            LocalDate since = LegacyDates.toLocalDate(memberSince);      // 2026-01-01
            show("since", since);
            LocalDate firstRenewal = since.plusYears(1);                 // 2027-01-01
            show("firstRenewal", firstRenewal);
            Date forDao = LegacyDates.toDate(firstRenewal);
            LocalDate roundTrip = LegacyDates.toLocalDate(forDao);       // 2027-01-01
            show("roundTrip", roundTrip);
            LocalDate missing = LegacyDates.toLocalDate(null);           // null
            show("missing", missing);
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
