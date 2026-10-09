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
 * Examples for the tutorial "Get Current Date and Time in Java: now(), Clock and Time Zones".
 * https://howtodoinjava.com/java/date-time/current-date-time/
 */
public class CurrentDateTime {
    static record Coupon(String code, LocalDate lastDay) {}
    static class CouponService {
        private final Clock clock;

        CouponService(Clock clock) {
            this.clock = clock;
        }

        boolean isValid(Coupon coupon) {
            LocalDate today = LocalDate.now(clock);
            return !today.isAfter(coupon.lastDay());
        }
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate today = LocalDate.now();                                   // current date in the JVM default zone
            show("today", today);
            LocalTime time = LocalTime.now();                                    // current time of day in the default zone
            show("time", time);
            LocalDateTime dateTime = LocalDateTime.now();                        // current date and time, no zone stored
            show("dateTime", dateTime);
            ZonedDateTime paris = ZonedDateTime.now(ZoneId.of("Europe/Paris"));  // current date and time in Paris
            show("paris", paris);
            Instant instant = Instant.now();                                     // current moment in UTC
            show("instant", instant);
        }
        {
            Clock clock = Clock.fixed(Instant.parse("2026-10-10T08:15:30Z"), ZoneId.of("Asia/Kolkata"));
            Instant instant = Instant.now(clock);                  // 2026-10-10T08:15:30Z
            show("instant", instant);
            ZonedDateTime zoned = ZonedDateTime.now(clock);        // 2026-10-10T13:45:30+05:30[Asia/Kolkata]
            show("zoned", zoned);
            OffsetDateTime offset = OffsetDateTime.now(clock);     // 2026-10-10T13:45:30+05:30
            show("offset", offset);
            LocalDateTime dateTime = LocalDateTime.now(clock);     // 2026-10-10T13:45:30
            show("dateTime", dateTime);
            LocalDate today = LocalDate.now(clock);                // 2026-10-10
            show("today", today);
            LocalTime time = LocalTime.now(clock);                 // 13:45:30
            show("time", time);
        }
        {
            Clock serverClock = Clock.fixed(Instant.parse("2026-10-09T20:30:00Z"), ZoneOffset.UTC);
            LocalDate serverDate = LocalDate.now(serverClock);                                  // 2026-10-09
            show("serverDate", serverDate);
            LocalDate indiaDate = LocalDate.now(serverClock.withZone(ZoneId.of("Asia/Kolkata")));   // 2026-10-10
            show("indiaDate", indiaDate);
            LocalTime indiaTime = LocalTime.now(serverClock.withZone(ZoneId.of("Asia/Kolkata")));   // 02:00
            show("indiaTime", indiaTime);
        }
        {
            ZonedDateTime tokyo = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));     // current date and time in Tokyo
            show("tokyo", tokyo);
            LocalDate utcDate = LocalDate.now(ZoneOffset.UTC);                    // current date in UTC
            show("utcDate", utcDate);
            LocalTime newYorkTime = LocalTime.now(ZoneId.of("America/New_York"));  // current time of day in New York
            show("newYorkTime", newYorkTime);
            ZoneId jvmZone = ZoneId.systemDefault();                              // the zone the no-argument now() uses
            show("jvmZone", jvmZone);
        }
        {
            Clock clock = Clock.fixed(Instant.parse("2026-10-10T08:15:30Z"), ZoneId.of("Asia/Kolkata"));
            LocalDateTime dateTime = LocalDateTime.now(clock);
            String european = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));            // "10-10-2026 13:45"
            show("european", european);
            String us = dateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a", Locale.US));     // "10/10/2026 01:45 PM"
            show("us", us);
            String withZone = ZonedDateTime.now(clock).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z", Locale.US));   // "2026-10-10 13:45:30 IST"
            show("withZone", withZone);
            String iso = ZonedDateTime.now(clock).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);         // "2026-10-10T13:45:30+05:30"
            show("iso", iso);
        }
        {
            Coupon coupon = new Coupon("SAVE10", LocalDate.of(2026, 10, 10));
            Clock lastDay = Clock.fixed(Instant.parse("2026-10-10T18:00:00Z"), ZoneId.of("Europe/Berlin"));
            boolean valid = new CouponService(lastDay).isValid(coupon);                       // true
            show("valid", valid);
            Clock nextDay = Clock.offset(lastDay, Duration.ofHours(6));
            boolean expired = new CouponService(nextDay).isValid(coupon);                     // false
            show("expired", expired);
            CouponService production = new CouponService(Clock.system(ZoneId.of("Europe/Berlin")));   // reads the real clock
            show("production", production);
        }
        {
            Instant now = Instant.parse("2026-10-10T08:15:30Z");
            Date legacyDate = Date.from(now);                               // the same moment as a java.util.Date
            show("legacyDate", legacyDate);
            Instant back = legacyDate.toInstant();                          // 2026-10-10T08:15:30Z
            show("back", back);
            GregorianCalendar calendar = GregorianCalendar.from(ZonedDateTime.ofInstant(now, ZoneId.of("Asia/Kolkata")));
            int hour = calendar.get(Calendar.HOUR_OF_DAY);                  // 13
            show("hour", hour);
            java.sql.Timestamp timestamp = java.sql.Timestamp.from(now);    // the same moment for JDBC
            show("timestamp", timestamp);
        }
        {
            OffsetDateTime utcNow = OffsetDateTime.now(ZoneOffset.UTC);     // current date and time with offset Z
            show("utcNow", utcNow);
            LocalDateTime utcLocal = LocalDateTime.now(ZoneOffset.UTC);     // current UTC date and time, no zone stored
            show("utcLocal", utcLocal);
            Instant utcInstant = Instant.now();                             // current moment, printed with a Z
            show("utcInstant", utcInstant);
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
