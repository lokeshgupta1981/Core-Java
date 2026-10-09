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
 * Examples for the tutorial "Java ZonedDateTime: Time Zones, DST and Zone Conversion".
 * https://howtodoinjava.com/java/date-time/zoneddatetime-class/
 */
public class ZonedDateTimeExamples {

    public static void main(String[] args) throws Exception {
        {
            ZoneId berlin = ZoneId.of("Europe/Berlin");
            ZonedDateTime standup = ZonedDateTime.of(2026, 5, 14, 9, 0, 0, 0, berlin);          // 2026-05-14T09:00+02:00[Europe/Berlin]
            show("standup", standup);
            ZonedDateTime newYork = standup.withZoneSameInstant(ZoneId.of("America/New_York"));  // 2026-05-14T03:00-04:00[America/New_York]
            show("newYork", newYork);
            ZonedDateTime kolkata = ZonedDateTime.parse("2026-05-14T12:30+05:30[Asia/Kolkata]");  // 2026-05-14T12:30+05:30[Asia/Kolkata]
            show("kolkata", kolkata);
            boolean sameMoment = kolkata.isEqual(standup);                                        // true
            show("sameMoment", sameMoment);
            ZonedDateTime nextWeek = standup.plusWeeks(1);                                        // 2026-05-21T09:00+02:00[Europe/Berlin]
            show("nextWeek", nextWeek);
            Instant utc = standup.toInstant();                                                    // 2026-05-14T07:00:00Z
            show("utc", utc);
            String label = standup.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm z", Locale.US));   // "14 May 2026 09:00 CEST"
            show("label", label);
        }
        {
            ZonedDateTime here = ZonedDateTime.now();                                       // current time, default zone
            show("here", here);
            ZonedDateTime tokyo = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));               // current time in Tokyo
            show("tokyo", tokyo);
            Clock clock = Clock.fixed(Instant.parse("2026-05-14T07:00:00Z"), ZoneId.of("Europe/Berlin"));
            ZonedDateTime fixed = ZonedDateTime.now(clock);                                // 2026-05-14T09:00+02:00[Europe/Berlin]
            show("fixed", fixed);
        }
        {
            ZoneId paris = ZoneId.of("Europe/Paris");
            ZonedDateTime fromFields = ZonedDateTime.of(2026, 11, 30, 23, 45, 59, 0, paris);           // 2026-11-30T23:45:59+01:00[Europe/Paris]
            show("fromFields", fromFields);
            ZonedDateTime fromParts = ZonedDateTime.of(LocalDate.of(2026, 3, 12), LocalTime.of(12, 44), paris);   // 2026-03-12T12:44+01:00[Europe/Paris]
            show("fromParts", fromParts);
            ZonedDateTime fromLocal = LocalDateTime.of(2026, 7, 1, 8, 0).atZone(paris);                 // 2026-07-01T08:00+02:00[Europe/Paris]
            show("fromLocal", fromLocal);
            ZonedDateTime fromInstant = Instant.parse("2026-07-01T06:00:00Z").atZone(paris);            // 2026-07-01T08:00+02:00[Europe/Paris]
            show("fromInstant", fromInstant);
            ZonedDateTime fixedOffset = ZonedDateTime.of(2026, 7, 1, 8, 0, 0, 0, ZoneId.of("UTC+1"));    // 2026-07-01T08:00+01:00[UTC+01:00]
            show("fixedOffset", fixedOffset);
        }
        {
            ZonedDateTime full = ZonedDateTime.parse("2026-03-28T10:15:30+01:00[Europe/Paris]");   // 2026-03-28T10:15:30+01:00[Europe/Paris]
            show("full", full);
            ZonedDateTime offsetOnly = ZonedDateTime.parse("2026-03-28T10:15:30+01:00");          // 2026-03-28T10:15:30+01:00
            show("offsetOnly", offsetOnly);
            try { ZonedDateTime noZone = ZonedDateTime.parse("2026-03-28T10:15:30"); show("noZone", noZone); } catch (Throwable _t) { System.out.println("noZone -> " + _t); }
        }
        {
            DateTimeFormatter parisText = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a", Locale.US).withZone(ZoneId.of("Europe/Paris"));
            ZonedDateTime fromCsv = ZonedDateTime.parse("2026-03-27 10:15:30 AM", parisText);     // 2026-03-27T10:15:30+01:00[Europe/Paris]
            show("fromCsv", fromCsv);
            DateTimeFormatter withRegion = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm VV");
            ZonedDateTime custom = ZonedDateTime.parse("14.05.2026 09:00 Europe/Berlin", withRegion);   // 2026-05-14T09:00+02:00[Europe/Berlin]
            show("custom", custom);
        }
        {
            ZonedDateTime meeting = ZonedDateTime.of(2026, 5, 14, 9, 0, 0, 0, ZoneId.of("Europe/Berlin"));
            String region = meeting.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm VV"));                // "2026-05-14 09:00 Europe/Berlin"
            show("region", region);
            String shortName = meeting.format(DateTimeFormatter.ofPattern("h:mm a z", Locale.US));             // "9:00 AM CEST"
            show("shortName", shortName);
            String longName = meeting.format(DateTimeFormatter.ofPattern("HH:mm zzzz", Locale.US));            // "09:00 Central European Summer Time"
            show("longName", longName);
            String offset = meeting.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);                             // "2026-05-14T09:00:00+02:00"
            show("offset", offset);
            String rfc = meeting.format(DateTimeFormatter.RFC_1123_DATE_TIME);                                  // "Thu, 14 May 2026 09:00:00 +0200"
            show("rfc", rfc);
        }
        {
            ZonedDateTime saturdayNoon = ZonedDateTime.of(2026, 3, 7, 12, 0, 0, 0, ZoneId.of("America/New_York"));
            ZonedDateTime plusOneDay = saturdayNoon.plusDays(1);              // 2026-03-08T12:00-04:00[America/New_York]
            show("plusOneDay", plusOneDay);
            ZonedDateTime plus24Hours = saturdayNoon.plusHours(24);           // 2026-03-08T13:00-04:00[America/New_York]
            show("plus24Hours", plus24Hours);
            Duration elapsed = Duration.between(saturdayNoon, plusOneDay);    // PT23H
            show("elapsed", elapsed);
            ZonedDateTime lastYear = saturdayNoon.minusYears(1);              // 2025-03-07T12:00-05:00[America/New_York]
            show("lastYear", lastYear);
        }
        {
            ZoneId newYork = ZoneId.of("America/New_York");
            ZonedDateTime inGap = ZonedDateTime.of(LocalDateTime.of(2026, 3, 8, 2, 30), newYork);       // 2026-03-08T03:30-04:00[America/New_York]
            show("inGap", inGap);
            ZonedDateTime inOverlap = ZonedDateTime.of(LocalDateTime.of(2026, 11, 1, 1, 30), newYork);  // 2026-11-01T01:30-04:00[America/New_York]
            show("inOverlap", inOverlap);
            ZonedDateTime secondTime = inOverlap.withLaterOffsetAtOverlap();                            // 2026-11-01T01:30-05:00[America/New_York]
            show("secondTime", secondTime);
            ZonedDateTime firstTime = secondTime.withEarlierOffsetAtOverlap();                          // 2026-11-01T01:30-04:00[America/New_York]
            show("firstTime", firstTime);
        }
        {
            try { ZonedDateTime strict = ZonedDateTime.ofStrict(LocalDateTime.of(2026, 3, 8, 2, 30), ZoneOffset.ofHours(-5), ZoneId.of("America/New_York")); show("strict", strict); } catch (Throwable _t) { System.out.println("strict -> " + _t); }
        }
        {
            ZonedDateTime berlinNine = ZonedDateTime.of(2026, 5, 14, 9, 0, 0, 0, ZoneId.of("Europe/Berlin"));
            ZonedDateTime sameInstant = berlinNine.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));   // 2026-05-14T12:30+05:30[Asia/Kolkata]
            show("sameInstant", sameInstant);
            ZonedDateTime sameLocal = berlinNine.withZoneSameLocal(ZoneId.of("Asia/Kolkata"));       // 2026-05-14T09:00+05:30[Asia/Kolkata]
            show("sameLocal", sameLocal);
        }
        {
            ZonedDateTime firstCall = ZonedDateTime.of(2026, 3, 2, 9, 0, 0, 0, ZoneId.of("America/New_York"));
            ZoneId berlinZone = ZoneId.of("Europe/Berlin");
            LocalTime beforeSwitch = firstCall.withZoneSameInstant(berlinZone).toLocalTime();              // 15:00
            show("beforeSwitch", beforeSwitch);
            LocalTime usSummer = firstCall.plusWeeks(2).withZoneSameInstant(berlinZone).toLocalTime();      // 14:00
            show("usSummer", usSummer);
            LocalTime bothSummer = firstCall.plusWeeks(4).withZoneSameInstant(berlinZone).toLocalTime();    // 15:00
            show("bothSummer", bothSummer);
        }
        {
            ZonedDateTime berlinValue = ZonedDateTime.parse("2026-05-14T09:00+02:00[Europe/Berlin]");
            ZonedDateTime londonValue = ZonedDateTime.parse("2026-05-14T08:00+01:00[Europe/London]");
            boolean sameInstant = berlinValue.isEqual(londonValue);        // true
            show("sameInstant", sameInstant);
            boolean equalObjects = berlinValue.equals(londonValue);        // false
            show("equalObjects", equalObjects);
            boolean earlier = berlinValue.isBefore(londonValue.plusMinutes(1));   // true
            show("earlier", earlier);
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
