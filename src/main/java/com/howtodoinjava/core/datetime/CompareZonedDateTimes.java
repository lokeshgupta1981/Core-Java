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
 * Examples for the tutorial "Compare ZonedDateTime in Java: isEqual vs equals vs compareTo".
 * https://howtodoinjava.com/java/date-time/zoneddatetime-comparison/
 */
public class CompareZonedDateTimes {
    static boolean validConnection(ZonedDateTime arrival, ZonedDateTime nextDeparture, Duration minimum) {
        return !nextDeparture.isBefore(arrival.plus(minimum));
    }
    public static void main(String[] args) throws Exception {
        {
            ZonedDateTime departure = ZonedDateTime.of(2025, 6, 10, 21, 0, 0, 0, ZoneId.of("Asia/Tokyo"));
            ZonedDateTime arrival = ZonedDateTime.of(2025, 6, 10, 9, 15, 0, 0, ZoneId.of("Pacific/Honolulu"));
            boolean landsLater = arrival.isAfter(departure);                                  // true
            show("landsLater", landsLater);
            boolean localLooksEarlier = arrival.toLocalDateTime().isBefore(departure.toLocalDateTime());   // true
            show("localLooksEarlier", localLooksEarlier);
            ZonedDateTime sameInstant = departure.withZoneSameInstant(ZoneId.of("Pacific/Honolulu"));     // 2025-06-10T02:00-10:00[Pacific/Honolulu]
            show("sameInstant", sameInstant);
            boolean isEqual = departure.isEqual(sameInstant);                                 // true
            show("isEqual", isEqual);
            boolean equals = departure.equals(sameInstant);                                   // false
            show("equals", equals);
            int order = departure.compareTo(sameInstant);                                     // 1
            show("order", order);
        }
        {
            ZonedDateTime tokyo = ZonedDateTime.of(2025, 6, 10, 21, 0, 0, 0, ZoneId.of("Asia/Tokyo"));
            ZonedDateTime utc = tokyo.withZoneSameInstant(ZoneOffset.UTC);       // 2025-06-10T12:00Z
            show("utc", utc);
            boolean isEqual = tokyo.isEqual(utc);                                // true
            show("isEqual", isEqual);
            boolean isBefore = tokyo.isBefore(utc);                              // false
            show("isBefore", isBefore);
            boolean isAfter = tokyo.isAfter(utc);                                // false
            show("isAfter", isAfter);
            boolean sameEpochSecond = tokyo.toEpochSecond() == utc.toEpochSecond();   // true
            show("sameEpochSecond", sameEpochSecond);
            boolean sameInstant = tokyo.toInstant().equals(utc.toInstant());     // true
            show("sameInstant", sameInstant);
        }
        {
            ZonedDateTime tokyo = ZonedDateTime.of(2025, 6, 10, 21, 0, 0, 0, ZoneId.of("Asia/Tokyo"));
            ZonedDateTime utc = tokyo.withZoneSameInstant(ZoneOffset.UTC);
            int tokyoVsUtc = tokyo.compareTo(utc);           // 1
            show("tokyoVsUtc", tokyoVsUtc);
            int utcVsTokyo = utc.compareTo(tokyo);           // -1
            show("utcVsTokyo", utcVsTokyo);
            boolean equal = tokyo.equals(utc);               // false
            show("equal", equal);
            ZonedDateTime oneHourLater = utc.plusHours(1);
            int byInstant = tokyo.compareTo(oneHourLater);   // -1
            show("byInstant", byInstant);
        }
        {
            ZoneId chicago = ZoneId.of("America/Chicago");
            ZonedDateTime first = ZonedDateTime.of(LocalDateTime.of(2024, 11, 3, 1, 30), chicago);   // 2024-11-03T01:30-05:00[America/Chicago]
            show("first", first);
            ZonedDateTime second = first.withLaterOffsetAtOverlap();                                  // 2024-11-03T01:30-06:00[America/Chicago]
            show("second", second);
            boolean sameLocal = first.toLocalDateTime().isEqual(second.toLocalDateTime());            // true
            show("sameLocal", sameLocal);
            boolean firstEarlier = first.isBefore(second);                                            // true
            show("firstEarlier", firstEarlier);
            long minutesApart = Duration.between(first, second).toMinutes();                          // 60
            show("minutesApart", minutesApart);
            ZonedDateTime inGap = ZonedDateTime.of(LocalDateTime.of(2024, 3, 10, 2, 30), chicago);   // 2024-03-10T03:30-05:00[America/Chicago]
            show("inGap", inGap);
        }
        {
            ZonedDateTime tokyo = ZonedDateTime.of(2025, 6, 10, 23, 30, 0, 0, ZoneId.of("Asia/Tokyo"));
            ZonedDateTime london = ZonedDateTime.of(2025, 6, 10, 16, 0, 0, 0, ZoneId.of("Europe/London"));
            boolean sameDayAsWritten = tokyo.toLocalDate().isEqual(london.toLocalDate());              // true
            show("sameDayAsWritten", sameDayAsWritten);
            ZoneId viewer = ZoneId.of("Asia/Tokyo");
            LocalDate tokyoDay = tokyo.withZoneSameInstant(viewer).toLocalDate();                      // 2025-06-10
            show("tokyoDay", tokyoDay);
            LocalDate londonDay = london.withZoneSameInstant(viewer).toLocalDate();                    // 2025-06-11
            show("londonDay", londonDay);
            boolean sameDayForViewer = tokyoDay.isEqual(londonDay);                                    // false
            show("sameDayForViewer", sameDayForViewer);
        }
        {
            ZonedDateTime landsFrankfurt = ZonedDateTime.of(2025, 6, 11, 7, 5, 0, 0, ZoneId.of("Europe/Berlin"));
            ZonedDateTime leavesFrankfurt = ZonedDateTime.of(2025, 6, 11, 8, 20, 0, 0, ZoneId.of("Europe/Berlin"));
            ZonedDateTime tightDeparture = ZonedDateTime.of(2025, 6, 11, 5, 45, 0, 0, ZoneId.of("UTC"));
            boolean ok = validConnection(landsFrankfurt, leavesFrankfurt, Duration.ofMinutes(60));    // true
            show("ok", ok);
            boolean tooTight = validConnection(landsFrankfurt, tightDeparture, Duration.ofMinutes(60)); // false
            show("tooTight", tooTight);
            long layover = Duration.between(landsFrankfurt, tightDeparture).toMinutes();             // 40
            show("layover", layover);
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
