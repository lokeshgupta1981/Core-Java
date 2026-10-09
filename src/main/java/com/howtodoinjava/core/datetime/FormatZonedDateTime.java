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
 * Examples for the tutorial "Format ZonedDateTime to String in Java (Patterns and Zones)".
 * https://howtodoinjava.com/java/date-time/format-zoneddatetime/
 */
public class FormatZonedDateTime {

    public static void main(String[] args) throws Exception {
        {
            ZonedDateTime auctionEnd = ZonedDateTime.of(2026, 3, 14, 19, 35, 55, 0, ZoneId.of("America/New_York"));
            String iso = auctionEnd.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);       // "2026-03-14T19:35:55-04:00"
            show("iso", iso);
            DateTimeFormatter invite = DateTimeFormatter.ofPattern("EEE, MMM d, uuuu h:mm a z", Locale.US);
            String text = auctionEnd.format(invite);                                      // "Sat, Mar 14, 2026 7:35 PM EDT"
            show("text", text);
            String asIs = auctionEnd.toString();                                          // "2026-03-14T19:35:55-04:00[America/New_York]"
            show("asIs", asIs);
        }
        {
            ZonedDateTime auctionEnd = ZonedDateTime.of(2026, 3, 14, 19, 35, 55, 0, ZoneId.of("America/New_York"));
            String zoned = auctionEnd.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);       // "2026-03-14T19:35:55-04:00[America/New_York]"
            show("zoned", zoned);
            String offset = auctionEnd.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);     // "2026-03-14T19:35:55-04:00"
            show("offset", offset);
            String instant = auctionEnd.format(DateTimeFormatter.ISO_INSTANT);             // "2026-03-14T23:35:55Z"
            show("instant", instant);
            String local = auctionEnd.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);       // "2026-03-14T19:35:55"
            show("local", local);
            String http = auctionEnd.format(DateTimeFormatter.RFC_1123_DATE_TIME);         // "Sat, 14 Mar 2026 19:35:55 -0400"
            show("http", http);
            String compact = auctionEnd.format(DateTimeFormatter.BASIC_ISO_DATE);          // "20260314-0400"
            show("compact", compact);
        }
        {
            ZonedDateTime auctionEnd = ZonedDateTime.of(2026, 3, 14, 19, 35, 55, 0, ZoneId.of("America/New_York"));
            String shortName = auctionEnd.format(DateTimeFormatter.ofPattern("z", Locale.US));       // "EDT"
            show("shortName", shortName);
            String longName = auctionEnd.format(DateTimeFormatter.ofPattern("zzzz", Locale.US));     // "Eastern Daylight Time"
            show("longName", longName);
            String generic = auctionEnd.format(DateTimeFormatter.ofPattern("vvvv", Locale.US));      // "Eastern Time"
            show("generic", generic);
            String zoneId = auctionEnd.format(DateTimeFormatter.ofPattern("VV"));                    // "America/New_York"
            show("zoneId", zoneId);
            String gmt = auctionEnd.format(DateTimeFormatter.ofPattern("O"));                        // "GMT-4"
            show("gmt", gmt);
            String isoOffset = auctionEnd.format(DateTimeFormatter.ofPattern("XXX"));                // "-04:00"
            show("isoOffset", isoOffset);
            String noColon = auctionEnd.format(DateTimeFormatter.ofPattern("Z"));                    // "-0400"
            show("noColon", noColon);
        }
        {
            DateTimeFormatter usStyle = DateTimeFormatter.ofPattern("MM/dd/uuuu - HH:mm:ss z", Locale.US);
            String formatted = ZonedDateTime.of(2026, 3, 14, 19, 35, 55, 0, ZoneId.of("America/New_York")).format(usStyle);   // "03/14/2026 - 19:35:55 EDT"
            show("formatted", formatted);
        }
        {
            ZonedDateTime utc = ZonedDateTime.of(2026, 3, 14, 23, 35, 55, 0, ZoneOffset.UTC);
            String zulu = utc.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssXXX"));      // "2026-03-14T23:35:55Z"
            show("zulu", zulu);
            String numeric = utc.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssxxx"));   // "2026-03-14T23:35:55+00:00"
            show("numeric", numeric);
            String fourDigit = utc.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssZ"));   // "2026-03-14T23:35:55+0000"
            show("fourDigit", fourDigit);
        }
        {
            ZonedDateTime auctionEnd = ZonedDateTime.of(2026, 3, 14, 19, 35, 55, 0, ZoneId.of("America/New_York"));
            String fullUs = auctionEnd.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(Locale.US));     // Saturday, March 14, 2026, 7:35:55 PM Eastern Daylight Time (U+202F before PM)
            show("fullUs", fullUs);
            String longFr = auctionEnd.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).withLocale(Locale.FRANCE)); // "14 mars 2026, 19:35:55 EDT"
            show("longFr", longFr);
            try { String fullLocal = auctionEnd.toLocalDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)); show("fullLocal", fullLocal); } catch (Throwable _t) { System.out.println("fullLocal -> " + _t); }
        }
        {
            ZonedDateTime auctionEnd = ZonedDateTime.of(2026, 3, 14, 19, 35, 0, 0, ZoneId.of("America/New_York"));
            DateTimeFormatter forBidder = DateTimeFormatter.ofPattern("EEE, MMM d, h:mm a z", Locale.US);
            String london = auctionEnd.withZoneSameInstant(ZoneId.of("Europe/London")).format(forBidder);   // "Sat, Mar 14, 11:35 PM GMT"
            show("london", london);
            String kolkata = auctionEnd.withZoneSameInstant(ZoneId.of("Asia/Kolkata")).format(forBidder);  // "Sun, Mar 15, 5:05 AM IST"
            show("kolkata", kolkata);
            String tokyo = auctionEnd.withZoneSameInstant(ZoneId.of("Asia/Tokyo")).format(forBidder);      // "Sun, Mar 15, 8:35 AM JST"
            show("tokyo", tokyo);
        }
        {
            ZonedDateTime auctionEnd = ZonedDateTime.of(2026, 3, 14, 19, 35, 0, 0, ZoneId.of("America/New_York"));
            DateTimeFormatter tokyoFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm z", Locale.US).withZone(ZoneId.of("Asia/Tokyo"));
            String inTokyo = auctionEnd.format(tokyoFormatter);   // "2026-03-15 08:35 JST"
            show("inTokyo", inTokyo);
        }
        {
            ZonedDateTime auctionEnd = ZonedDateTime.of(2026, 3, 14, 19, 35, 55, 0, ZoneId.of("America/New_York"));
            try { String noZone = auctionEnd.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm z")); show("noZone", noZone); } catch (Throwable _t) { System.out.println("noZone -> " + _t); }
            try { String fromInstant = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm").format(auctionEnd.toInstant()); show("fromInstant", fromInstant); } catch (Throwable _t) { System.out.println("fromInstant -> " + _t); }
            String wrongLabel = auctionEnd.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss'Z'"));      // "2026-03-14T19:35:55Z", not UTC
            show("wrongLabel", wrongLabel);
            String correct = auctionEnd.format(DateTimeFormatter.ISO_INSTANT);                                   // "2026-03-14T23:35:55Z"
            show("correct", correct);
        }
        {
            ZonedDateTime withMillis = ZonedDateTime.of(2026, 3, 14, 19, 35, 55, 123_000_000, ZoneId.of("America/New_York"));
            String millis = withMillis.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSXXX"));   // "2026-03-14T19:35:55.123-04:00"
            show("millis", millis);
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
