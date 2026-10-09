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
 * Examples for the tutorial "Convert LocalDateTime to Date in Java and Back".
 * https://howtodoinjava.com/java/date-time/localdatetime-to-date/
 */
public class LocalDateTimeToDate {

    public static void main(String[] args) throws Exception {
        {
            Date nextRun = Date.from(Instant.parse("2026-10-12T05:00:00Z"));
            ZoneId berlin = ZoneId.of("Europe/Berlin");
            LocalDateTime local = LocalDateTime.ofInstant(nextRun.toInstant(), berlin);   // 2026-10-12T07:00
            show("local", local);
            LocalDateTime moved = local.plusHours(2);                                     // 2026-10-12T09:00
            show("moved", moved);
            Date forScheduler = Date.from(moved.atZone(berlin).toInstant());
            Instant check = forScheduler.toInstant();                                     // 2026-10-12T07:00:00Z
            show("check", check);
        }
        {
            Date created = new Date(1791781200000L);
            ZoneId tokyo = ZoneId.of("Asia/Tokyo");
            LocalDateTime viaFactory = LocalDateTime.ofInstant(created.toInstant(), tokyo);   // 2026-10-12T14:00
            show("viaFactory", viaFactory);
            LocalDateTime viaZoned = created.toInstant().atZone(tokyo).toLocalDateTime();     // 2026-10-12T14:00
            show("viaZoned", viaZoned);
            LocalDateTime inUtc = LocalDateTime.ofInstant(created.toInstant(), ZoneOffset.UTC);   // 2026-10-12T05:00
            show("inUtc", inUtc);
        }
        {
            java.sql.Timestamp column = java.sql.Timestamp.valueOf(LocalDateTime.of(2026, 10, 12, 7, 0, 0, 123456789));
            LocalDateTime fromColumn = column.toLocalDateTime();     // 2026-10-12T07:00:00.123456789
            show("fromColumn", fromColumn);
            Date asDate = column;
            long millis = asDate.getTime() % 1000;                   // 123
            show("millis", millis);
        }
        {
            LocalDateTime meeting = LocalDateTime.of(2026, 10, 12, 9, 30);
            Date inBerlin = Date.from(meeting.atZone(ZoneId.of("Europe/Berlin")).toInstant());
            Date inNewYork = Date.from(meeting.atZone(ZoneId.of("America/New_York")).toInstant());
            Instant berlinMoment = inBerlin.toInstant();             // 2026-10-12T07:30:00Z
            show("berlinMoment", berlinMoment);
            Instant newYorkMoment = inNewYork.toInstant();           // 2026-10-12T13:30:00Z
            show("newYorkMoment", newYorkMoment);
        }
        {
            ZoneId berlin = ZoneId.of("Europe/Berlin");
            LocalDateTime inGap = LocalDateTime.of(2026, 3, 29, 2, 30);
            ZonedDateTime moved = inGap.atZone(berlin);                       // 2026-03-29T03:30+02:00[Europe/Berlin]
            show("moved", moved);
            LocalDateTime roundTrip = LocalDateTime.ofInstant(Date.from(moved.toInstant()).toInstant(), berlin);   // 2026-03-29T03:30
            show("roundTrip", roundTrip);
        }
        {
            ZoneId berlin = ZoneId.of("Europe/Berlin");
            LocalDateTime twice = LocalDateTime.of(2026, 10, 25, 2, 30);
            Instant first = twice.atZone(berlin).toInstant();                              // 2026-10-25T00:30:00Z
            show("first", first);
            Instant second = twice.atZone(berlin).withLaterOffsetAtOverlap().toInstant();  // 2026-10-25T01:30:00Z
            show("second", second);
        }
        {
            ZoneId utc = ZoneOffset.UTC;
            LocalDateTime precise = LocalDateTime.of(2026, 10, 12, 7, 0, 0, 123456789);
            Date truncated = Date.from(precise.atZone(utc).toInstant());
            LocalDateTime back = LocalDateTime.ofInstant(truncated.toInstant(), utc);     // 2026-10-12T07:00:00.123
            show("back", back);
            boolean same = back.equals(precise);                                          // false
            show("same", same);
        }
        {
            Instant paidAt = Instant.parse("2026-10-12T05:00:00Z");
            Date legacyPaid = Date.from(paidAt);
            Instant restored = legacyPaid.toInstant();                    // 2026-10-12T05:00:00Z
            show("restored", restored);
            OffsetDateTime forApi = restored.atOffset(ZoneOffset.UTC);    // 2026-10-12T05:00Z
            show("forApi", forApi);
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
