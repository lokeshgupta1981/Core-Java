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
 * Examples for the tutorial "Convert Date and Time to EST/EDT in Java (EST5EDT vs EST)".
 * https://howtodoinjava.com/java/date-time/convert-date-time-to-est-est5edt/
 */
public class EstTimezone {

    public static void main(String[] args) throws Exception {
        {
            ZoneId eastern = ZoneId.of("America/New_York");
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a z", Locale.US);
            ZonedDateTime winter = Instant.parse("2026-01-15T15:00:00Z").atZone(eastern);    // 2026-01-15T10:00-05:00[America/New_York]
            show("winter", winter);
            ZonedDateTime summer = Instant.parse("2026-07-15T15:00:00Z").atZone(eastern);    // 2026-07-15T11:00-04:00[America/New_York]
            show("summer", summer);
            String winterText = winter.format(format);                                        // "01/15/2026 10:00 AM EST"
            show("winterText", winterText);
            String summerText = summer.format(format);                                        // "07/15/2026 11:00 AM EDT"
            show("summerText", summerText);
            ZonedDateTime india = ZonedDateTime.of(2026, 7, 15, 20, 30, 0, 0, ZoneId.of("Asia/Kolkata"));
            ZonedDateTime indiaInEastern = india.withZoneSameInstant(eastern);                // 2026-07-15T11:00-04:00[America/New_York]
            show("indiaInEastern", indiaInEastern);
            ZonedDateTime fixedOffset = Instant.parse("2026-07-15T15:00:00Z").atZone(ZoneOffset.ofHours(-5));   // 2026-07-15T10:00-05:00
            show("fixedOffset", fixedOffset);
            try { ZoneId est = ZoneId.of("EST"); show("est", est); } catch (Throwable _t) { System.out.println("est -> " + _t); }
        }
        {
            try { ZoneId edt = ZoneId.of("EDT"); show("edt", edt); } catch (Throwable _t) { System.out.println("edt -> " + _t); }
            ZoneId estAlias = ZoneId.of("EST", ZoneId.SHORT_IDS);       // America/Panama
            show("estAlias", estAlias);
            boolean panamaDst = estAlias.getRules().isDaylightSavings(Instant.parse("2026-07-15T15:00:00Z"));   // false
            show("panamaDst", panamaDst);
        }
        {
            TimeZone legacyEst = TimeZone.getTimeZone("EST");
            boolean estHasDst = legacyEst.useDaylightTime();                     // false
            show("estHasDst", estHasDst);
            String unknownId = TimeZone.getTimeZone("EDT").getID();              // "GMT"
            show("unknownId", unknownId);
            boolean newYorkHasDst = TimeZone.getTimeZone("America/New_York").useDaylightTime();   // true
            show("newYorkHasDst", newYorkHasDst);
        }
        {
            ZoneId eastern = ZoneId.of("America/New_York");
            ZonedDateTime india = ZonedDateTime.of(2026, 7, 15, 20, 30, 0, 0, ZoneId.of("Asia/Kolkata"));
            ZonedDateTime sameMoment = india.withZoneSameInstant(eastern);   // 2026-07-15T11:00-04:00[America/New_York]
            show("sameMoment", sameMoment);
            ZonedDateTime sameClock = india.withZoneSameLocal(eastern);      // 2026-07-15T20:30-04:00[America/New_York]
            show("sameClock", sameClock);
            ZonedDateTime nowInEastern = ZonedDateTime.now(eastern);         // current time in New York
            show("nowInEastern", nowInEastern);
        }
        {
            ZoneId eastern = ZoneId.of("America/New_York");
            ZonedDateTime start = Instant.parse("2026-07-15T15:00:00Z").atZone(eastern);          // 2026-07-15T11:00-04:00[America/New_York]
            show("start", start);
            ZonedDateTime fromMillis = Instant.ofEpochMilli(1784127600000L).atZone(eastern);      // 2026-07-15T11:00-04:00[America/New_York]
            show("fromMillis", fromMillis);
            Instant stored = LocalDateTime.of(2026, 7, 15, 11, 0).atZone(eastern).toInstant();    // 2026-07-15T15:00:00Z
            show("stored", stored);
        }
        {
            LocalDateTime utcValue = LocalDateTime.of(2026, 7, 15, 15, 0);        // read from a UTC column
            show("utcValue", utcValue);
            ZonedDateTime easternTime = utcValue.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of("America/New_York"));   // 2026-07-15T11:00-04:00[America/New_York]
            show("easternTime", easternTime);
            LocalDateTime easternLocal = easternTime.toLocalDateTime();           // 2026-07-15T11:00
            show("easternLocal", easternLocal);
            ZonedDateTime wrong = utcValue.atZone(ZoneId.of("America/New_York"));  // 2026-07-15T15:00-04:00[America/New_York]
            show("wrong", wrong);
        }
        {
            Date legacyDate = Date.from(Instant.parse("2026-07-15T15:00:00Z"));
            ZonedDateTime converted = legacyDate.toInstant().atZone(ZoneId.of("America/New_York"));   // 2026-07-15T11:00-04:00[America/New_York]
            show("converted", converted);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
            calendar.setTime(legacyDate);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);                                              // 11
            show("hour", hour);
        }
        {
            ZonedDateTime summer = Instant.parse("2026-07-15T15:00:00Z").atZone(ZoneId.of("America/New_York"));
            String withLabel = summer.format(DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z", Locale.US));   // "07/15/2026 at 11:00AM EDT"
            show("withLabel", withLabel);
            String withEt = summer.format(DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma v", Locale.US));     // "07/15/2026 at 11:00AM ET"
            show("withEt", withEt);
            String inIndia = summer.format(DateTimeFormatter.ofPattern("hh:mm a z", Locale.forLanguageTag("en-IN")));   // "11:00 am GMT-04:00"
            show("inIndia", inIndia);
        }
        {
            ZoneId eastern = ZoneId.of("America/New_York");
            ZonedDateTime inGap = LocalDateTime.of(2026, 3, 8, 2, 30).atZone(eastern);          // 2026-03-08T03:30-04:00[America/New_York]
            show("inGap", inGap);
            ZonedDateTime firstPass = LocalDateTime.of(2026, 11, 1, 1, 30).atZone(eastern);     // 2026-11-01T01:30-04:00[America/New_York]
            show("firstPass", firstPass);
            ZonedDateTime secondPass = firstPass.withLaterOffsetAtOverlap();                    // 2026-11-01T01:30-05:00[America/New_York]
            show("secondPass", secondPass);
        }
        {
            ZoneRules rules = ZoneId.of("America/New_York").getRules();
            boolean summerDst = rules.isDaylightSavings(Instant.parse("2026-07-15T15:00:00Z"));    // true
            show("summerDst", summerDst);
            boolean winterDst = rules.isDaylightSavings(Instant.parse("2026-01-15T15:00:00Z"));    // false
            show("winterDst", winterDst);
            ZoneOffset offset = rules.getOffset(Instant.parse("2026-07-15T15:00:00Z"));            // -04:00
            show("offset", offset);
            ZoneOffsetTransition next = rules.nextTransition(Instant.parse("2026-04-01T00:00:00Z"));   // Transition[Overlap at 2026-11-01T02:00-04:00 to -05:00]
            show("next", next);
        }
        {
            boolean hasNewYork = ZoneId.getAvailableZoneIds().contains("America/New_York");   // true
            show("hasNewYork", hasNewYork);
            boolean hasEst = ZoneId.getAvailableZoneIds().contains("EST");                     // false
            show("hasEst", hasEst);
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
