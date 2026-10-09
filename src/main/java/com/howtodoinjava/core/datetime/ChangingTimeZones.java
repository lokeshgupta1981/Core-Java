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
 * Examples for the tutorial "Convert Date and Time Between Time Zones in Java".
 * https://howtodoinjava.com/java/date-time/convert-date-between-timezones/
 */
public class ChangingTimeZones {
    static ZonedDateTime forViewer(LocalDateTime organizerTime, ZoneId organizerZone, ZoneId viewerZone) {
        return organizerTime.atZone(organizerZone).withZoneSameInstant(viewerZone);
    }
    public static void main(String[] args) throws Exception {
        {
            ZonedDateTime kolkata = ZonedDateTime.of(2026, 3, 20, 9, 30, 0, 0, ZoneId.of("Asia/Kolkata"));   // 2026-03-20T09:30+05:30[Asia/Kolkata]
            show("kolkata", kolkata);
            ZonedDateTime london = kolkata.withZoneSameInstant(ZoneId.of("Europe/London"));        // 2026-03-20T04:00Z[Europe/London]
            show("london", london);
            ZonedDateTime newYork = kolkata.withZoneSameInstant(ZoneId.of("America/New_York"));    // 2026-03-20T00:00-04:00[America/New_York]
            show("newYork", newYork);
            ZonedDateTime tokyo = kolkata.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));            // 2026-03-20T13:00+09:00[Asia/Tokyo]
            show("tokyo", tokyo);
            boolean sameMoment = london.isEqual(newYork);                                          // true
            show("sameMoment", sameMoment);
            Instant utc = tokyo.toInstant();                                                       // 2026-03-20T04:00:00Z
            show("utc", utc);
        }
        {
            ZonedDateTime callStart = ZonedDateTime.of(2026, 3, 20, 9, 30, 0, 0, ZoneId.of("Asia/Kolkata"));
            ZonedDateTime converted = callStart.withZoneSameInstant(ZoneId.of("Europe/London"));   // 2026-03-20T04:00Z[Europe/London]
            show("converted", converted);
            ZonedDateTime relabeled = callStart.withZoneSameLocal(ZoneId.of("Europe/London"));     // 2026-03-20T09:30Z[Europe/London]
            show("relabeled", relabeled);
            long minutesApart = Duration.between(callStart, relabeled).toMinutes();                // 330
            show("minutesApart", minutesApart);
        }
        {
            OffsetDateTime received = OffsetDateTime.parse("2026-03-20T09:30+05:30");
            OffsetDateTime inUtc = received.withOffsetSameInstant(ZoneOffset.UTC);              // 2026-03-20T04:00Z
            show("inUtc", inUtc);
            OffsetDateTime inMinusFour = received.withOffsetSameInstant(ZoneOffset.of("-04:00"));   // 2026-03-20T00:00-04:00
            show("inMinusFour", inMinusFour);
            ZonedDateTime inRegion = received.atZoneSameInstant(ZoneId.of("America/New_York"));   // 2026-03-20T00:00-04:00[America/New_York]
            show("inRegion", inRegion);
            try { ZoneOffset badOffset = ZoneOffset.of("00:00"); show("badOffset", badOffset); } catch (Throwable _t) { System.out.println("badOffset -> " + _t); }
        }
        {
            LocalDateTime fromColumn = LocalDateTime.of(2026, 3, 20, 4, 0);          // stored in UTC
            show("fromColumn", fromColumn);
            ZonedDateTime forKolkata = fromColumn.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of("Asia/Kolkata"));   // 2026-03-20T09:30+05:30[Asia/Kolkata]
            show("forKolkata", forKolkata);
            LocalDateTime kolkataLocal = forKolkata.toLocalDateTime();                 // 2026-03-20T09:30
            show("kolkataLocal", kolkataLocal);
            ZonedDateTime mislabeled = fromColumn.atZone(ZoneId.of("Asia/Kolkata"));   // 2026-03-20T04:00+05:30[Asia/Kolkata]
            show("mislabeled", mislabeled);
        }
        {
            Instant logged = Instant.parse("2026-03-20T04:00:00Z");
            ZonedDateTime forEngineer = logged.atZone(ZoneId.of("America/Los_Angeles"));        // 2026-03-19T21:00-07:00[America/Los_Angeles]
            show("forEngineer", forEngineer);
            ZonedDateTime fromJson = OffsetDateTime.parse("2026-03-20T04:00:00Z").atZoneSameInstant(ZoneId.of("Asia/Tokyo"));   // 2026-03-20T13:00+09:00[Asia/Tokyo]
            show("fromJson", fromJson);
        }
        {
            Instant moment = Instant.parse("2026-03-20T12:00:00Z");
            ZoneOffset londonOffset = moment.atZone(ZoneId.of("Europe/London")).getOffset();       // Z
            show("londonOffset", londonOffset);
            ZoneOffset newYorkOffset = moment.atZone(ZoneId.of("America/New_York")).getOffset();   // -04:00
            show("newYorkOffset", newYorkOffset);
            Duration difference = Duration.ofSeconds(londonOffset.getTotalSeconds() - newYorkOffset.getTotalSeconds());   // PT4H
            show("difference", difference);
        }
        {
            LocalDateTime weekly = LocalDateTime.of(2026, 3, 27, 18, 30);
            ZoneId organizer = ZoneId.of("Asia/Kolkata");
            ZonedDateTime londonView = forViewer(weekly, organizer, ZoneId.of("Europe/London"));       // 2026-03-27T13:00Z[Europe/London]
            show("londonView", londonView);
            ZonedDateTime nextLondon = forViewer(weekly.plusWeeks(1), organizer, ZoneId.of("Europe/London"));   // 2026-04-03T14:00+01:00[Europe/London]
            show("nextLondon", nextLondon);
            ZonedDateTime newYorkView = forViewer(weekly, organizer, ZoneId.of("America/New_York"));   // 2026-03-27T09:00-04:00[America/New_York]
            show("newYorkView", newYorkView);
        }
        {
            Date fromLegacyApi = Date.from(Instant.parse("2026-03-20T04:00:00Z"));
            ZonedDateTime inKolkata = fromLegacyApi.toInstant().atZone(ZoneId.of("Asia/Kolkata"));   // 2026-03-20T09:30+05:30[Asia/Kolkata]
            show("inKolkata", inKolkata);
            Date backToDate = Date.from(inKolkata.toInstant());
            boolean unchanged = backToDate.equals(fromLegacyApi);                                       // true
            show("unchanged", unchanged);
        }
        {
            ZonedDateTime local = Instant.parse("2026-03-20T04:00:00Z").atZone(ZoneId.of("Europe/Berlin"));   // 2026-03-20T05:00+01:00[Europe/Berlin]
            show("local", local);
        }
        {
            ZonedDateTime opening = LocalTime.of(9, 30).atDate(LocalDate.of(2026, 7, 1)).atZone(ZoneId.of("Asia/Kolkata"));
            LocalTime inLondon = opening.withZoneSameInstant(ZoneId.of("Europe/London")).toLocalTime();   // 05:00
            show("inLondon", inLondon);
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
