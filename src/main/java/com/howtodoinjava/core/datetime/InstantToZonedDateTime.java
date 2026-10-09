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
 * Examples for the tutorial "Convert Instant to ZonedDateTime in Java (atZone, ofInstant)".
 * https://howtodoinjava.com/java/date-time/java-instant-to-zoneddatetime/
 */
public class InstantToZonedDateTime {
    static boolean isValidZone(String id) {
        if (id == null) {
            return false;
        }
        try {
            ZoneId.of(id);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }
    static String orderTime(Instant orderedAt, String customerZone) {
        ZoneId zone = isValidZone(customerZone) ? ZoneId.of(customerZone) : ZoneOffset.UTC;
        return orderedAt.atZone(zone).format(DateTimeFormatter.ofPattern("d MMM uuuu, h:mm a z", Locale.US));
    }
    public static void main(String[] args) throws Exception {
        {
            Instant orderedAt = Instant.parse("2026-05-14T08:00:00Z");
            ZonedDateTime india = orderedAt.atZone(ZoneId.of("Asia/Kolkata"));                       // 2026-05-14T13:30+05:30[Asia/Kolkata]
            show("india", india);
            ZonedDateTime california = ZonedDateTime.ofInstant(orderedAt, ZoneId.of("America/Los_Angeles"));   // 2026-05-14T01:00-07:00[America/Los_Angeles]
            show("california", california);
            Instant back = california.toInstant();                                                   // 2026-05-14T08:00:00Z
            show("back", back);
            boolean sameMoment = india.isEqual(california);                                          // true
            show("sameMoment", sameMoment);
        }
        {
            Instant shipped = Instant.parse("2026-01-20T23:15:00Z");
            ZonedDateTime berlin = shipped.atZone(ZoneId.of("Europe/Berlin"));        // 2026-01-21T00:15+01:00[Europe/Berlin]
            show("berlin", berlin);
            ZonedDateTime utc = shipped.atZone(ZoneOffset.UTC);                        // 2026-01-20T23:15Z
            show("utc", utc);
            try { ZonedDateTime missing = shipped.atZone(null); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
        }
        {
            Instant shipped = Instant.parse("2026-01-20T23:15:00Z");
            ZonedDateTime viaOf = ZonedDateTime.ofInstant(shipped, ZoneId.of("Europe/Berlin"));      // 2026-01-21T00:15+01:00[Europe/Berlin]
            show("viaOf", viaOf);
            ZonedDateTime viaAt = shipped.atZone(ZoneId.of("Europe/Berlin"));                        // 2026-01-21T00:15+01:00[Europe/Berlin]
            show("viaAt", viaAt);
            boolean equal = viaOf.equals(viaAt);                                                    // true
            show("equal", equal);
            ZonedDateTime fixed = ZonedDateTime.ofInstant(shipped, ZoneOffset.of("+05:30"));         // 2026-01-21T04:45+05:30
            show("fixed", fixed);
        }
        {
            Instant newYear = Instant.parse("2026-01-01T03:00:00Z");
            String inNewYork = orderTime(newYear, "America/New_York");       // "31 Dec 2025, 10:00 PM EST"
            show("inNewYork", inNewYork);
            String inTokyo = orderTime(newYear, "Asia/Tokyo");               // "1 Jan 2026, 12:00 PM JST"
            show("inTokyo", inTokyo);
            String unknown = orderTime(newYear, "Mars/Base");                // "1 Jan 2026, 3:00 AM Z"
            show("unknown", unknown);
        }
        {
            ZonedDateTime tokyo = ZonedDateTime.parse("2026-05-14T17:00+09:00[Asia/Tokyo]");
            ZonedDateTime paris = ZonedDateTime.parse("2026-05-14T10:00+02:00[Europe/Paris]");
            Instant fromTokyo = tokyo.toInstant();                     // 2026-05-14T08:00:00Z
            show("fromTokyo", fromTokyo);
            Instant fromParis = paris.toInstant();                     // 2026-05-14T08:00:00Z
            show("fromParis", fromParis);
            long epochMillis = fromTokyo.toEpochMilli();               // 1778745600000
            show("epochMillis", epochMillis);
        }
        {
            Instant instant = Instant.parse("2026-05-14T20:30:00Z");
            OffsetDateTime offset = instant.atOffset(ZoneOffset.ofHours(-4));                      // 2026-05-14T16:30-04:00
            show("offset", offset);
            LocalDateTime local = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Kolkata"));     // 2026-05-15T02:00
            show("local", local);
            LocalDate date = LocalDate.ofInstant(instant, ZoneId.of("Asia/Kolkata"));              // 2026-05-15
            show("date", date);
            ZonedDateTime fromMillis = Instant.ofEpochMilli(1778745600000L).atZone(ZoneOffset.UTC);   // 2026-05-14T08:00Z
            show("fromMillis", fromMillis);
        }
        {
            Date legacy = new Date(1778745600000L);
            ZonedDateTime fromDate = legacy.toInstant().atZone(ZoneId.of("Europe/London"));        // 2026-05-14T09:00+01:00[Europe/London]
            show("fromDate", fromDate);
            java.sql.Timestamp column = java.sql.Timestamp.from(Instant.parse("2026-05-14T08:00:00Z"));
            ZonedDateTime fromColumn = column.toInstant().atZone(ZoneId.of("Europe/London"));      // 2026-05-14T09:00+01:00[Europe/London]
            show("fromColumn", fromColumn);
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
