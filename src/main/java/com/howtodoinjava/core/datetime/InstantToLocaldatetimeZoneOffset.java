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

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;
import java.time.format.*;
import java.time.zone.*;

/**
 * Examples for the tutorial "Convert Instant to LocalDateTime in Java (LocalDate, LocalTime)".
 * https://howtodoinjava.com/java/date-time/convert-instant-to-local-date-time/
 */
public class InstantToLocaldatetimeZoneOffset {

    public static void main(String[] args) throws Exception {
        {
            Instant paidAt = Instant.parse("2026-05-14T18:45:00Z");
            ZoneId userZone = ZoneId.of("Asia/Kolkata");
            LocalDateTime dateTime = LocalDateTime.ofInstant(paidAt, userZone);    // 2026-05-15T00:15
            show("dateTime", dateTime);
            LocalDate date = LocalDate.ofInstant(paidAt, userZone);                // 2026-05-15
            show("date", date);
            LocalTime time = LocalTime.ofInstant(paidAt, userZone);                // 00:15
            show("time", time);
        }
        {
            Instant shippedAt = Instant.parse("2026-05-14T18:45:00Z");
            LocalDateTime inUtc = LocalDateTime.ofInstant(shippedAt, ZoneOffset.UTC);                       // 2026-05-14T18:45
            show("inUtc", inUtc);
            LocalDateTime inNewYork = LocalDateTime.ofInstant(shippedAt, ZoneId.of("America/New_York"));    // 2026-05-14T14:45
            show("inNewYork", inNewYork);
            LocalDateTime inTokyo = LocalDateTime.ofInstant(shippedAt, ZoneId.of("Asia/Tokyo"));            // 2026-05-15T03:45
            show("inTokyo", inTokyo);
        }
        {
            Instant shippedAt = Instant.parse("2026-05-14T18:45:00Z");
            LocalDate deliveryDay = LocalDate.ofInstant(shippedAt, ZoneId.of("Asia/Tokyo"));                           // 2026-05-15
            show("deliveryDay", deliveryDay);
            LocalTime deliveryTime = LocalTime.ofInstant(shippedAt, ZoneId.of("Asia/Tokyo"));                          // 03:45
            show("deliveryTime", deliveryTime);
            LocalDate java8Style = LocalDateTime.ofInstant(shippedAt, ZoneId.of("Asia/Tokyo")).toLocalDate();         // 2026-05-15
            show("java8Style", java8Style);
        }
        {
            Instant paidAt = Instant.parse("2026-05-14T18:45:00Z");
            ZonedDateTime inBerlin = paidAt.atZone(ZoneId.of("Europe/Berlin"));     // 2026-05-14T20:45+02:00[Europe/Berlin]
            show("inBerlin", inBerlin);
            LocalDateTime berlinDateTime = inBerlin.toLocalDateTime();              // 2026-05-14T20:45
            show("berlinDateTime", berlinDateTime);
            LocalDate berlinDate = inBerlin.toLocalDate();                          // 2026-05-14
            show("berlinDate", berlinDate);
            LocalTime berlinTime = inBerlin.toLocalTime();                          // 20:45
            show("berlinTime", berlinTime);
        }
        {
            Instant paidAt = Instant.parse("2026-05-14T18:45:00Z");
            LocalDateTime serverTime = LocalDateTime.ofInstant(paidAt, ZoneId.systemDefault());    // 2026-05-14T18:45 on a server with TZ=UTC
            show("serverTime", serverTime);
        }
        {
            Instant winter = Instant.parse("2026-01-15T12:00:00Z");
            Instant summer = Instant.parse("2026-07-15T12:00:00Z");
            LocalDateTime winterNy = LocalDateTime.ofInstant(winter, ZoneId.of("America/New_York"));     // 2026-01-15T07:00
            show("winterNy", winterNy);
            LocalDateTime summerNy = LocalDateTime.ofInstant(summer, ZoneId.of("America/New_York"));     // 2026-07-15T08:00
            show("summerNy", summerNy);
            LocalDateTime summerFixed = LocalDateTime.ofInstant(summer, ZoneOffset.of("-05:00"));        // 2026-07-15T07:00, one hour off
            show("summerFixed", summerFixed);
        }
        {
            try { ZoneId shortId = ZoneId.of("IST"); show("shortId", shortId); } catch (Throwable _t) { System.out.println("shortId -> " + _t); }
            ZoneId regionId = ZoneId.of("Asia/Kolkata");          // Asia/Kolkata
            show("regionId", regionId);
        }
        {
            LocalDateTime meetingLocal = LocalDateTime.of(2026, 5, 15, 0, 15);
            Instant fromZone = meetingLocal.atZone(ZoneId.of("Asia/Kolkata")).toInstant();    // 2026-05-14T18:45:00Z
            show("fromZone", fromZone);
            Instant fromOffset = meetingLocal.toInstant(ZoneOffset.UTC);                       // 2026-05-15T00:15:00Z
            show("fromOffset", fromOffset);
        }
        {
            ZoneId newYork = ZoneId.of("America/New_York");
            ZonedDateTime inGap = LocalDateTime.of(2026, 3, 8, 2, 30).atZone(newYork);           // 2026-03-08T03:30-04:00[America/New_York]
            show("inGap", inGap);
            Instant inOverlap = LocalDateTime.of(2026, 11, 1, 1, 30).atZone(newYork).toInstant();    // 2026-11-01T05:30:00Z
            show("inOverlap", inOverlap);
        }
        {
            long epochMillis = 1778784300000L;
            LocalDateTime fromMillis = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneOffset.UTC);    // 2026-05-14T18:45
            show("fromMillis", fromMillis);
            Date legacy = new Date(epochMillis);
            LocalDateTime fromDate = LocalDateTime.ofInstant(legacy.toInstant(), ZoneOffset.UTC);                    // 2026-05-14T18:45
            show("fromDate", fromDate);
        }
        {
            Instant renewedAt = Instant.parse("2026-02-28T23:30:00Z");
            LocalDate renewalDay = renewedAt.atZone(ZoneId.of("Europe/Paris")).toLocalDate();    // 2026-03-01
            show("renewalDay", renewalDay);
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
