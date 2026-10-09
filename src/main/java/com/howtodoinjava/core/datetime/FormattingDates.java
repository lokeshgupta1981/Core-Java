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
 * Examples for the tutorial "Format a Date to String in Java: DateTimeFormatter Examples".
 * https://howtodoinjava.com/java/date-time/java-date-formatting/
 */
public class FormattingDates {

    public static void main(String[] args) throws Exception {
        {
            LocalDateTime start = LocalDateTime.of(2026, 3, 14, 17, 45, 30);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String text = start.format(formatter);         // "14/03/2026 17:45"
            show("text", text);
            String iso = start.toString();                 // "2026-03-14T17:45:30"
            show("iso", iso);
            String usStyle = start.format(DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a", Locale.US));   // "Mar 14, 2026 at 5:45 PM"
            show("usStyle", usStyle);
        }
        {
            LocalDate day = LocalDate.of(2026, 3, 14);
            String isoDate = day.format(DateTimeFormatter.ISO_LOCAL_DATE);    // "2026-03-14"
            show("isoDate", isoDate);
            String basic = day.format(DateTimeFormatter.BASIC_ISO_DATE);      // "20260314"
            show("basic", basic);
            String custom = day.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));   // "14-03-2026"
            show("custom", custom);
            String full = day.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.US));   // "Saturday, March 14, 2026"
            show("full", full);
            String shortUk = day.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.UK));   // "14/03/2026"
            show("shortUk", shortUk);
        }
        {
            DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter zoned = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z", Locale.US);
            LocalDateTime local = LocalDateTime.of(2026, 3, 14, 17, 45, 30);
            ZonedDateTime newYork = local.atZone(ZoneId.of("America/New_York"));
            String d = dateOnly.format(local.toLocalDate());      // "2026-03-14"
            show("d", d);
            String dt = dateTime.format(local);                   // "2026-03-14 17:45:30"
            show("dt", dt);
            String z = zoned.format(newYork);                     // "2026-03-14 17:45 EDT"
            show("z", z);
            String offset = newYork.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);   // "2026-03-14T17:45:30-04:00"
            show("offset", offset);
        }
        {
            Instant paidAt = Instant.parse("2026-03-14T21:45:30Z");
            String utc = DateTimeFormatter.ISO_INSTANT.format(paidAt);       // "2026-03-14T21:45:30Z"
            show("utc", utc);
            DateTimeFormatter india = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of("Asia/Kolkata"));
            String kolkata = india.format(paidAt);                            // "2026-03-15 03:15"
            show("kolkata", kolkata);
        }
        {
            LocalDateTime meeting = LocalDateTime.of(2026, 3, 14, 17, 45, 30, 123_000_000);
            String withMillis = meeting.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));            // "17:45:30.123"
            show("withMillis", withMillis);
            String dayName = meeting.format(DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.US));   // "Saturday, 14 March"
            show("dayName", dayName);
            String quoted = meeting.format(DateTimeFormatter.ofPattern("'Day' D 'of' yyyy"));          // "Day 73 of 2026"
            show("quoted", quoted);
            String french = meeting.format(DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.FRANCE));   // "samedi 14 mars 2026"
            show("french", french);
        }
        {
            LocalDate dueDate = LocalDate.of(2026, 3, 14);
            try { String bad = dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
            String atStart = dueDate.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));   // "2026-03-14 00:00"
            show("atStart", atStart);
        }
        {
            LocalDate dec29 = LocalDate.of(2025, 12, 29);
            String weekYear = dec29.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));   // "2026-12-29", wrong year
            show("weekYear", weekYear);
            String year = dec29.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));       // "2025-12-29"
            show("year", year);
            LocalTime evening = LocalTime.of(17, 45);
            String noMarker = evening.format(DateTimeFormatter.ofPattern("hh:mm"));      // "05:45", AM or PM is lost
            show("noMarker", noMarker);
            String clock24 = evening.format(DateTimeFormatter.ofPattern("HH:mm"));       // "17:45"
            show("clock24", clock24);
        }
        {
            String shortTime = LocalTime.of(17, 45).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.US));   // "5:45 PM", with U+202F
            show("shortTime", shortTime);
            boolean same = shortTime.equals("5:45 PM");                                  // false
            show("same", same);
        }
        {
            Date legacy = Date.from(Instant.parse("2026-03-14T21:45:30Z"));   // from an old API
            show("legacy", legacy);
            ZonedDateTime utcTime = legacy.toInstant().atZone(ZoneOffset.UTC);
            String formatted = utcTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.US));   // "14 Mar 2026 21:45"
            show("formatted", formatted);
        }
        {
            java.sql.Date sqlDate = java.sql.Date.valueOf("2026-03-14");
            String fromSql = sqlDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));   // "14.03.2026"
            show("fromSql", fromSql);
        }
        {
            ZonedDateTime webinar = ZonedDateTime.of(2026, 3, 14, 17, 45, 30, 0, ZoneId.of("America/New_York"));
            String json = webinar.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);                    // "2026-03-14T17:45:30-04:00"
            show("json", json);
            String fileName = "webinars-" + webinar.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm")) + ".csv";   // "webinars-20260314-1745.csv"
            show("fileName", fileName);
            String page = webinar.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).withLocale(Locale.US));   // "March 14, 2026, 5:45:30 PM EDT"
            show("page", page);
        }
        {
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));   // today's date, such as "14/03/2026"
            show("today", today);
        }
        {
            String input = "2026-03-14";
            String output = LocalDate.parse(input).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));   // "14/03/2026"
            show("output", output);
        }
        {
            String viaFormat = String.format(Locale.US, "%tF %<tR", LocalDateTime.of(2026, 3, 14, 17, 45));   // "2026-03-14 17:45"
            show("viaFormat", viaFormat);
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
