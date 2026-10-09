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
 * Examples for the tutorial "How to Set the JVM Default Time Zone (TZ, -Duser.timezone)".
 * https://howtodoinjava.com/java/date-time/setting-jvm-timezone/
 */
public class SetJvmTimezone {
    static LocalDate invoiceDate(Clock clock) {
        return LocalDate.now(clock);
    }
    public static void main(String[] args) throws Exception {
        {
            // started with: java -Duser.timezone=UTC -jar app.jar
            ZoneId zone = ZoneId.systemDefault();                    // UTC
            show("zone", zone);
            String legacyId = TimeZone.getDefault().getID();         // "UTC"
            show("legacyId", legacyId);
            String property = System.getProperty("user.timezone");   // "UTC"
            show("property", property);
        }
        {
            ZoneId current = ZoneId.systemDefault();                                          // default zone of this JVM
            show("current", current);
            ZoneOffset offsetToday = current.getRules().getOffset(Instant.parse("2026-07-15T12:00:00Z"));   // offset of the default zone on that date
            show("offsetToday", offsetToday);
            int zoneCount = ZoneId.getAvailableZoneIds().size();                              // number of zone IDs in the JDK
            show("zoneCount", zoneCount);
            int rawNewYork = TimeZone.getTimeZone("America/New_York").getRawOffset();         // -18000000
            show("rawNewYork", rawNewYork);
        }
        {
            TimeZone timeZone = TimeZone.getDefault();
            String id = timeZone.getID();                        // "UTC"
            show("id", id);
            String name = timeZone.getDisplayName(Locale.US);    // "Coordinated Universal Time"
            show("name", name);
        }
        {
            // JVM started with -Duser.timezone=UTC
            String first = TimeZone.getDefault().getID();         // "UTC", now cached
            show("first", first);
            System.setProperty("user.timezone", "Asia/Tokyo");
            String later = TimeZone.getDefault().getID();         // "UTC"
            show("later", later);
        }
        {
            // JVM started with -Duser.timezone=UTC
            TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
            ZoneId afterChange = ZoneId.systemDefault();                    // Europe/Berlin
            show("afterChange", afterChange);
            String propertyAfter = System.getProperty("user.timezone");     // "UTC"
            show("propertyAfter", propertyAfter);
            TimeZone.setDefault(null);
            ZoneId restored = ZoneId.systemDefault();                       // UTC
            show("restored", restored);
        }
        {
            Instant lateEvening = Instant.parse("2026-04-30T20:30:00Z");
            LocalDate forIndia = invoiceDate(Clock.fixed(lateEvening, ZoneId.of("Asia/Kolkata")));   // 2026-05-01
            show("forIndia", forIndia);
            LocalDate onUtcServer = invoiceDate(Clock.fixed(lateEvening, ZoneOffset.UTC));           // 2026-04-30
            show("onUtcServer", onUtcServer);
            Clock production = Clock.system(ZoneId.of("Asia/Kolkata"));                              // system clock in Kolkata
            show("production", production);
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
