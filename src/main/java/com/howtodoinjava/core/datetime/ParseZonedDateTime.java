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
 * Examples for the tutorial "Parse String to ZonedDateTime in Java (ISO, Patterns, Zones)".
 * https://howtodoinjava.com/java/date-time/zoneddatetime-parse/
 */
public class ParseZonedDateTime {

    public static void main(String[] args) throws Exception {
        {
            ZonedDateTime paris = ZonedDateTime.parse("2026-03-14T10:15:30+01:00[Europe/Paris]");   // 2026-03-14T10:15:30+01:00[Europe/Paris]
            show("paris", paris);
            ZoneId zone = paris.getZone();                                                           // Europe/Paris
            show("zone", zone);
            DateTimeFormatter board = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm VV");
            ZonedDateTime tokyo = ZonedDateTime.parse("14/03/2026 10:15 Asia/Tokyo", board);         // 2026-03-14T10:15+09:00[Asia/Tokyo]
            show("tokyo", tokyo);
            try { ZonedDateTime noZone = ZonedDateTime.parse("2026-03-14T10:15:30"); show("noZone", noZone); } catch (Throwable _t) { System.out.println("noZone -> " + _t); }
        }
        {
            ZonedDateTime utc = ZonedDateTime.parse("2026-03-14T09:15:30Z");                          // 2026-03-14T09:15:30Z
            show("utc", utc);
            ZonedDateTime nanos = ZonedDateTime.parse("2026-03-14T10:15:30.123+01:00[Europe/Paris]");    // 2026-03-14T10:15:30.123+01:00[Europe/Paris]
            show("nanos", nanos);
            ZonedDateTime lowerT = ZonedDateTime.parse("2026-03-14t10:15:30+01:00[Europe/Paris]");       // 2026-03-14T10:15:30+01:00[Europe/Paris]
            show("lowerT", lowerT);
            try { ZonedDateTime lowerId = ZonedDateTime.parse("2026-03-14T10:15:30+01:00[europe/paris]"); show("lowerId", lowerId); } catch (Throwable _t) { System.out.println("lowerId -> " + _t); }
        }
        {
            ZonedDateTime fixed = ZonedDateTime.parse("2026-03-14T10:15:30+01:00");
            ZonedDateTime region = ZonedDateTime.parse("2026-03-14T10:15:30+01:00[Europe/Paris]");
            String fixedZone = fixed.getZone().toString();                    // "+01:00"
            show("fixedZone", fixedZone);
            boolean sameMoment = fixed.isEqual(region);                       // true
            show("sameMoment", sameMoment);
            boolean sameValue = fixed.equals(region);                         // false
            show("sameValue", sameValue);
            ZonedDateTime fixedJuly = fixed.plusMonths(4);                    // 2026-07-14T10:15:30+01:00
            show("fixedJuly", fixedJuly);
            ZonedDateTime regionJuly = region.plusMonths(4);                  // 2026-07-14T10:15:30+02:00[Europe/Paris]
            show("regionJuly", regionJuly);
        }
        {
            DateTimeFormatter withId = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm VV");
            ZonedDateTime id = ZonedDateTime.parse("2026-03-14 10:15 America/New_York", withId);           // 2026-03-14T10:15-04:00[America/New_York]
            show("id", id);
            DateTimeFormatter withOffset = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss XXX");
            ZonedDateTime offset = ZonedDateTime.parse("2026-03-14 10:15:30 -05:00", withOffset);          // 2026-03-14T10:15:30-05:00
            show("offset", offset);
            DateTimeFormatter compact = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss xx");
            ZonedDateTime noColon = ZonedDateTime.parse("2026-03-14 10:15:30 -0500", compact);             // 2026-03-14T10:15:30-05:00
            show("noColon", noColon);
            DateTimeFormatter gmt = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss O");
            ZonedDateTime india = ZonedDateTime.parse("2026-03-14 10:15:30 GMT+5:30", gmt);                // 2026-03-14T10:15:30+05:30
            show("india", india);
        }
        {
            DateTimeFormatter wrongLetters = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss a z");
            try { ZonedDateTime broken = ZonedDateTime.parse("2026-03-27 10:15:30 am -05:00", wrongLetters); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
            DateTimeFormatter twelveHour = DateTimeFormatter.ofPattern("uuuu-MM-dd hh:mm:ss a XXX", Locale.US);
            ZonedDateTime morning = ZonedDateTime.parse("2026-03-27 10:15:30 AM -05:00", twelveHour);   // 2026-03-27T10:15:30-05:00
            show("morning", morning);
        }
        {
            DateTimeFormatter named = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm z", Locale.US);
            ZonedDateTime pst = ZonedDateTime.parse("14/03/2026 10:15 PST", named);    // 2026-03-14T10:15-07:00[America/Los_Angeles]
            show("pst", pst);
            ZonedDateTime ist = ZonedDateTime.parse("14/03/2026 10:15 IST", named);    // 2026-03-14T10:15Z[Africa/Abidjan]
            show("ist", ist);
        }
        {
            DateTimeFormatter indiaFeed = new DateTimeFormatterBuilder()
                    .appendPattern("dd/MM/uuuu HH:mm ")
                    .appendZoneText(TextStyle.SHORT, Set.of(ZoneId.of("Asia/Kolkata")))
                    .toFormatter(Locale.US);
            ZonedDateTime departure = ZonedDateTime.parse("14/03/2026 10:15 IST", indiaFeed);   // 2026-03-14T10:15+05:30[Asia/Kolkata]
            show("departure", departure);
        }
        {
            ZonedDateTime header = ZonedDateTime.parse("Sat, 14 Mar 2026 09:15:30 GMT", DateTimeFormatter.RFC_1123_DATE_TIME);   // 2026-03-14T09:15:30Z
            show("header", header);
            ZonedDateTime offsetHeader = ZonedDateTime.parse("Sat, 14 Mar 2026 10:15:30 +0100", DateTimeFormatter.RFC_1123_DATE_TIME);   // 2026-03-14T10:15:30+01:00
            show("offsetHeader", offsetHeader);
        }
        {
            DateTimeFormatter local = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
            ZoneId store = ZoneId.of("America/New_York");
            ZonedDateTime sale = LocalDateTime.parse("2026-03-14 10:15", local).atZone(store);            // 2026-03-14T10:15-04:00[America/New_York]
            show("sale", sale);
            ZonedDateTime sale2 = ZonedDateTime.parse("2026-03-14 10:15", local.withZone(store));         // 2026-03-14T10:15-04:00[America/New_York]
            show("sale2", sale2);
        }
        {
            ZoneId newYork = ZoneId.of("America/New_York");
            ZonedDateTime gap = LocalDateTime.parse("2026-03-08T02:30").atZone(newYork);            // 2026-03-08T03:30-04:00[America/New_York]
            show("gap", gap);
            ZonedDateTime overlap = LocalDateTime.parse("2026-11-01T01:30").atZone(newYork);        // 2026-11-01T01:30-04:00[America/New_York]
            show("overlap", overlap);
            ZonedDateTime later = overlap.withLaterOffsetAtOverlap();                               // 2026-11-01T01:30-05:00[America/New_York]
            show("later", later);
        }
        {
            ZonedDateTime mismatch = ZonedDateTime.parse("2026-03-14T10:15:30+05:00[Europe/Paris]");   // 2026-03-14T06:15:30+01:00[Europe/Paris]
            show("mismatch", mismatch);
            ZonedDateTime inGap = ZonedDateTime.parse("2026-03-29T02:30:00+01:00[Europe/Paris]");      // 2026-03-29T03:30+02:00[Europe/Paris]
            show("inGap", inGap);
        }
        {
            ZonedDateTime flight = ZonedDateTime.parse("2026-03-14T23:30:00-04:00[America/New_York]");
            ZonedDateTime arrivalView = flight.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));   // 2026-03-15T09:00+05:30[Asia/Kolkata]
            show("arrivalView", arrivalView);
            Instant stored = flight.toInstant();                                                 // 2026-03-15T03:30:00Z
            show("stored", stored);
            LocalDate localDay = flight.toLocalDate();                                           // 2026-03-14
            show("localDay", localDay);
            OffsetDateTime forJdbc = flight.toOffsetDateTime();                                  // 2026-03-14T23:30-04:00
            show("forJdbc", forJdbc);
        }
        {
            long epochSeconds = 1773530130L;
            ZonedDateTime fromEpoch = Instant.ofEpochSecond(epochSeconds).atZone(ZoneId.of("Europe/Paris"));   // 2026-03-15T00:15:30+01:00[Europe/Paris]
            show("fromEpoch", fromEpoch);
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
