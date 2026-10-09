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
 * Examples for the tutorial "ZonedDateTime vs OffsetDateTime in Java: Key Differences".
 * https://howtodoinjava.com/java/date-time/zoneddatetime-vs-offsetdatetime/
 */
public class ZonedVsOffsetDateTime {

    public static void main(String[] args) throws Exception {
        {
            LocalDateTime local = LocalDateTime.of(2026, 3, 7, 12, 0);
            OffsetDateTime offsetTime = local.atOffset(ZoneOffset.ofHours(-5));            // 2026-03-07T12:00-05:00
            show("offsetTime", offsetTime);
            ZonedDateTime zonedTime = local.atZone(ZoneId.of("America/New_York"));         // 2026-03-07T12:00-05:00[America/New_York]
            show("zonedTime", zonedTime);
            OffsetDateTime offsetNextDay = offsetTime.plusDays(1);                         // 2026-03-08T12:00-05:00
            show("offsetNextDay", offsetNextDay);
            ZonedDateTime zonedNextDay = zonedTime.plusDays(1);                            // 2026-03-08T12:00-04:00[America/New_York]
            show("zonedNextDay", zonedNextDay);
            boolean sameInstant = offsetNextDay.toInstant().equals(zonedNextDay.toInstant());   // false
            show("sameInstant", sameInstant);
        }
        {
            OffsetDateTime utcNow = OffsetDateTime.now(ZoneOffset.UTC);                    // current time in UTC
            show("utcNow", utcNow);
            OffsetDateTime paid = OffsetDateTime.of(2026, 5, 14, 10, 0, 0, 0, ZoneOffset.ofHours(2));   // 2026-05-14T10:00+02:00
            show("paid", paid);
            OffsetDateTime parsed = OffsetDateTime.parse("2026-05-14T08:00:00Z");          // 2026-05-14T08:00Z
            show("parsed", parsed);
            try { OffsetDateTime wrongId = OffsetDateTime.now(ZoneOffset.of("UTC")); show("wrongId", wrongId); } catch (Throwable _t) { System.out.println("wrongId -> " + _t); }
        }
        {
            ZonedDateTime laNow = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));       // current time in Los Angeles
            show("laNow", laNow);
            ZonedDateTime winter = ZonedDateTime.of(2026, 1, 15, 9, 0, 0, 0, ZoneId.of("America/Los_Angeles"));   // 2026-01-15T09:00-08:00[America/Los_Angeles]
            show("winter", winter);
            ZonedDateTime summer = winter.plusMonths(6);                                         // 2026-07-15T09:00-07:00[America/Los_Angeles]
            show("summer", summer);
        }
        {
            OffsetDateTime berlinTime = OffsetDateTime.parse("2026-05-14T10:00+02:00");
            OffsetDateTime utcTime = OffsetDateTime.parse("2026-05-14T08:00Z");
            boolean sameMoment = berlinTime.isEqual(utcTime);      // true
            show("sameMoment", sameMoment);
            boolean equalObjects = berlinTime.equals(utcTime);     // false
            show("equalObjects", equalObjects);
            int order = berlinTime.compareTo(utcTime);             // 1
            show("order", order);
        }
        {
            ZonedDateTime firstReminder = ZonedDateTime.of(2026, 3, 23, 9, 0, 0, 0, ZoneId.of("Europe/London"));   // 2026-03-23T09:00Z[Europe/London]
            show("firstReminder", firstReminder);
            ZonedDateTime afterClockChange = firstReminder.plusWeeks(1);                                         // 2026-03-30T09:00+01:00[Europe/London]
            show("afterClockChange", afterClockChange);
            OffsetDateTime reminderInUtc = afterClockChange.toOffsetDateTime().withOffsetSameInstant(ZoneOffset.UTC);  // 2026-03-30T08:00Z
            show("reminderInUtc", reminderInUtc);
        }
        {
            ZonedDateTime zoned = ZonedDateTime.parse("2026-05-14T10:00+02:00[Europe/Berlin]");
            OffsetDateTime toOffset = zoned.toOffsetDateTime();                                     // 2026-05-14T10:00+02:00
            show("toOffset", toOffset);
            OffsetDateTime offset = OffsetDateTime.parse("2026-05-14T10:00+02:00");
            ZonedDateTime inRegion = offset.atZoneSameInstant(ZoneId.of("Europe/Paris"));          // 2026-05-14T10:00+02:00[Europe/Paris]
            show("inRegion", inRegion);
            ZonedDateTime fixedZone = offset.toZonedDateTime();                                     // 2026-05-14T10:00+02:00
            show("fixedZone", fixedZone);
            ZonedDateTime inKolkata = offset.atZoneSameInstant(ZoneId.of("Asia/Kolkata"));         // 2026-05-14T13:30+05:30[Asia/Kolkata]
            show("inKolkata", inKolkata);
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
