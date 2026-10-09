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
 * Examples for the tutorial "Convert LocalDateTime to ZonedDateTime in Java (and Back)".
 * https://howtodoinjava.com/java/date-time/localdatetime-to-zoneddatetime/
 */
public class LocalDateTimeToZonedDateTime {
    static ZonedDateTime saleStart(LocalDateTime picked, ZoneId venueZone, boolean secondOccurrence) {
        if (venueZone.getRules().getValidOffsets(picked).isEmpty()) {
            throw new IllegalArgumentException(picked + " does not exist in " + venueZone);
        }
        ZonedDateTime first = picked.atZone(venueZone);
        return secondOccurrence ? first.withLaterOffsetAtOverlap() : first;
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDateTime showStart = LocalDateTime.of(2026, 10, 3, 19, 30);
            ZonedDateTime atVenue = showStart.atZone(ZoneId.of("Europe/Madrid"));                 // 2026-10-03T19:30+02:00[Europe/Madrid]
            show("atVenue", atVenue);
            ZonedDateTime viaOf = ZonedDateTime.of(showStart, ZoneId.of("Europe/Madrid"));        // 2026-10-03T19:30+02:00[Europe/Madrid]
            show("viaOf", viaOf);
            ZonedDateTime forFan = atVenue.withZoneSameInstant(ZoneId.of("America/Chicago"));     // 2026-10-03T12:30-05:00[America/Chicago]
            show("forFan", forFan);
            LocalDateTime back = atVenue.toLocalDateTime();                                       // 2026-10-03T19:30
            show("back", back);
        }
        {
            ZoneId madrid = ZoneId.of("Europe/Madrid");
            ZonedDateTime winterShow = LocalDateTime.of(2026, 1, 17, 19, 30).atZone(madrid);   // 2026-01-17T19:30+01:00[Europe/Madrid]
            show("winterShow", winterShow);
            ZonedDateTime summerShow = LocalDateTime.of(2026, 7, 17, 19, 30).atZone(madrid);   // 2026-07-17T19:30+02:00[Europe/Madrid]
            show("summerShow", summerShow);
            ZonedDateTime fixedShow = LocalDateTime.of(2026, 7, 17, 19, 30).atZone(ZoneOffset.ofHours(1));   // 2026-07-17T19:30+01:00
            show("fixedShow", fixedShow);
        }
        {
            LocalDateTime doorsOpen = LocalDateTime.of(2026, 10, 3, 18, 0);
            ZoneId madrid = ZoneId.of("Europe/Madrid");
            ZonedDateTime viaAtZone = doorsOpen.atZone(madrid);                                       // 2026-10-03T18:00+02:00[Europe/Madrid]
            show("viaAtZone", viaAtZone);
            ZonedDateTime viaOfLocal = ZonedDateTime.ofLocal(doorsOpen, madrid, ZoneOffset.ofHours(2));   // 2026-10-03T18:00+02:00[Europe/Madrid]
            show("viaOfLocal", viaOfLocal);
            ZonedDateTime viaOfStrict = ZonedDateTime.ofStrict(doorsOpen, ZoneOffset.ofHours(2), madrid);   // 2026-10-03T18:00+02:00[Europe/Madrid]
            show("viaOfStrict", viaOfStrict);
            try { ZonedDateTime wrongOffset = ZonedDateTime.ofStrict(doorsOpen, ZoneOffset.ofHours(1), madrid); show("wrongOffset", wrongOffset); } catch (Throwable _t) { System.out.println("wrongOffset -> " + _t); }
            OffsetDateTime offsetOnly = doorsOpen.atOffset(ZoneOffset.ofHours(2));                    // 2026-10-03T18:00+02:00
            show("offsetOnly", offsetOnly);
        }
        {
            ZoneId madrid = ZoneId.of("Europe/Madrid");
            LocalDateTime inGap = LocalDateTime.of(2026, 3, 29, 2, 30);
            LocalDateTime inOverlap = LocalDateTime.of(2026, 10, 25, 2, 30);
            ZonedDateTime gapResult = inGap.atZone(madrid);                                              // 2026-03-29T03:30+02:00[Europe/Madrid]
            show("gapResult", gapResult);
            ZonedDateTime overlapEarlier = inOverlap.atZone(madrid);                                     // 2026-10-25T02:30+02:00[Europe/Madrid]
            show("overlapEarlier", overlapEarlier);
            ZonedDateTime overlapLater = ZonedDateTime.ofLocal(inOverlap, madrid, ZoneOffset.ofHours(1));   // 2026-10-25T02:30+01:00[Europe/Madrid]
            show("overlapLater", overlapLater);
            try { ZonedDateTime strictGap = ZonedDateTime.ofStrict(inGap, ZoneOffset.ofHours(1), madrid); show("strictGap", strictGap); } catch (Throwable _t) { System.out.println("strictGap -> " + _t); }
        }
        {
            ZoneRules rules = ZoneId.of("Europe/Madrid").getRules();
            int gapOffsets = rules.getValidOffsets(LocalDateTime.of(2026, 3, 29, 2, 30)).size();       // 0
            show("gapOffsets", gapOffsets);
            int overlapOffsets = rules.getValidOffsets(LocalDateTime.of(2026, 10, 25, 2, 30)).size();  // 2
            show("overlapOffsets", overlapOffsets);
            int normalOffsets = rules.getValidOffsets(LocalDateTime.of(2026, 10, 3, 19, 30)).size();   // 1
            show("normalOffsets", normalOffsets);
        }
        {
            LocalDateTime soldAtUtc = LocalDateTime.of(2026, 10, 3, 17, 30);                          // from a UTC column
            show("soldAtUtc", soldAtUtc);
            ZonedDateTime soldInMadrid = soldAtUtc.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of("Europe/Madrid"));   // 2026-10-03T19:30+02:00[Europe/Madrid]
            show("soldInMadrid", soldInMadrid);
            ZonedDateTime mislabeled = soldAtUtc.atZone(ZoneId.of("Europe/Madrid"));                   // 2026-10-03T17:30+02:00[Europe/Madrid]
            show("mislabeled", mislabeled);
        }
        {
            ZonedDateTime show = ZonedDateTime.of(2026, 10, 3, 19, 30, 0, 0, ZoneId.of("Europe/Madrid"));
            LocalDateTime venueTime = show.toLocalDateTime();                                                  // 2026-10-03T19:30
            show("venueTime", venueTime);
            LocalDateTime chicagoTime = show.withZoneSameInstant(ZoneId.of("America/Chicago")).toLocalDateTime();   // 2026-10-03T12:30
            show("chicagoTime", chicagoTime);
            LocalDateTime utcTime = LocalDateTime.ofInstant(show.toInstant(), ZoneOffset.UTC);                  // 2026-10-03T17:30
            show("utcTime", utcTime);
        }
        {
            ZoneId madrid = ZoneId.of("Europe/Madrid");
            ZonedDateTime normal = saleStart(LocalDateTime.of(2026, 10, 3, 10, 0), madrid, false);    // 2026-10-03T10:00+02:00[Europe/Madrid]
            show("normal", normal);
            ZonedDateTime second = saleStart(LocalDateTime.of(2026, 10, 25, 2, 30), madrid, true);    // 2026-10-25T02:30+01:00[Europe/Madrid]
            show("second", second);
            try { ZonedDateTime rejected = saleStart(LocalDateTime.of(2026, 3, 29, 2, 30), madrid, false); show("rejected", rejected); } catch (Throwable _t) { System.out.println("rejected -> " + _t); }
        }
        {
            ZonedDateTime inUtc = LocalDateTime.of(2026, 10, 3, 19, 30).atZone(ZoneId.of("Europe/Madrid")).withZoneSameInstant(ZoneOffset.UTC);   // 2026-10-03T17:30Z
            show("inUtc", inUtc);
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
