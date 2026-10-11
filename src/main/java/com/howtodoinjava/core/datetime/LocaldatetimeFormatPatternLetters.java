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

/**
 * Examples for the tutorial "How to Format LocalDateTime in Java (Patterns and Examples)".
 * https://howtodoinjava.com/java/date-time/format-localdatetime-to-string/
 */
public class LocaldatetimeFormatPatternLetters {
    static final DateTimeFormatter LOG_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
    static Optional<LocalDateTime> parseOrEmpty(String text, DateTimeFormatter formatter) {
        try {
            return Optional.of(LocalDateTime.parse(text.strip(), formatter));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDateTime meetingAt = LocalDateTime.of(2026, 3, 5, 14, 30, 15);
            String iso = meetingAt.toString();                                                          // "2026-03-05T14:30:15"
            show("iso", iso);
            String custom = meetingAt.format(DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm"));          // "05-03-2026 14:30"
            show("custom", custom);
            String twelveHour = meetingAt.format(DateTimeFormatter.ofPattern("MMM d, uuuu h:mm a", Locale.US));    // "Mar 5, 2026 2:30 PM"
            show("twelveHour", twelveHour);
        }
        {
            LocalDateTime slotStart = LocalDateTime.of(2026, 3, 5, 14, 30);
            String shortText = slotStart.toString();                                       // "2026-03-05T14:30"
            show("shortText", shortText);
            String fullText = slotStart.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);    // "2026-03-05T14:30:00"
            show("fullText", fullText);
        }
        {
            LocalDateTime loggedAt = LocalDateTime.of(2026, 3, 5, 9, 5, 7);
            String viaValue = loggedAt.format(LOG_FORMAT);           // "2026-03-05 09:05:07"
            show("viaValue", viaValue);
            String viaFormatter = LOG_FORMAT.format(loggedAt);       // "2026-03-05 09:05:07"
            show("viaFormatter", viaFormatter);
        }
        {
            LocalDateTime meetingAt = LocalDateTime.of(2026, 3, 5, 14, 30, 15);
            String isoDateTime = meetingAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);    // "2026-03-05T14:30:15"
            show("isoDateTime", isoDateTime);
            String isoDate = meetingAt.format(DateTimeFormatter.ISO_LOCAL_DATE);             // "2026-03-05"
            show("isoDate", isoDate);
            String isoTime = meetingAt.format(DateTimeFormatter.ISO_LOCAL_TIME);             // "14:30:15"
            show("isoTime", isoTime);
            String basicDate = meetingAt.format(DateTimeFormatter.BASIC_ISO_DATE);           // "20260305"
            show("basicDate", basicDate);
            String weekDate = meetingAt.format(DateTimeFormatter.ISO_WEEK_DATE);             // "2026-W10-4"
            show("weekDate", weekDate);
            String ordinalDate = meetingAt.format(DateTimeFormatter.ISO_ORDINAL_DATE);       // "2026-064"
            show("ordinalDate", ordinalDate);
        }
        {
            LocalDateTime meetingAt = LocalDateTime.of(2026, 3, 5, 14, 30, 15);
            try { String withOffset = meetingAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); show("withOffset", withOffset); } catch (Throwable _t) { System.out.println("withOffset -> " + _t); }
        }
        {
            LocalDateTime createdAt = LocalDateTime.of(2026, 3, 5, 14, 30, 15, 123_000_000);
            String dbStyle = createdAt.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));                // "2026-03-05 14:30:15"
            show("dbStyle", dbStyle);
            String europe = createdAt.format(DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm"));                    // "05/03/2026 14:30"
            show("europe", europe);
            String usStyle = createdAt.format(DateTimeFormatter.ofPattern("MM/dd/uuuu hh:mm a", Locale.US));      // "03/05/2026 02:30 PM"
            show("usStyle", usStyle);
            String withMillis = createdAt.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSS"));        // "2026-03-05 14:30:15.123"
            show("withMillis", withMillis);
            String fileName = createdAt.format(DateTimeFormatter.ofPattern("'report_'uuuuMMdd_HHmmss'.csv'"));    // "report_20260305_143015.csv"
            show("fileName", fileName);
            String longText = createdAt.format(DateTimeFormatter.ofPattern("EEEE, MMMM d 'at' HH:mm", Locale.US));    // "Thursday, March 5 at 14:30"
            show("longText", longText);
        }
        {
            LocalDateTime yearEnd = LocalDateTime.of(2025, 12, 29, 14, 30);
            String weekYear = yearEnd.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));                  // "2026-12-29"
            show("weekYear", weekYear);
            String calendarYear = yearEnd.format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));              // "2025-12-29"
            show("calendarYear", calendarYear);
            String minuteAsMonth = yearEnd.format(DateTimeFormatter.ofPattern("dd-mm-uuuu"));             // "29-30-2025"
            show("minuteAsMonth", minuteAsMonth);
            String german = yearEnd.format(DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.GERMANY));    // "29 Dezember 2025"
            show("german", german);
        }
        {
            LocalDateTime meetingAt = LocalDateTime.of(2026, 3, 5, 14, 30, 15);
            String uk = meetingAt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.UK));        // "5 Mar 2026, 14:30:15"
            show("uk", uk);
            String germany = meetingAt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.GERMANY));    // "05.03.26, 14:30"
            show("germany", germany);
            try { String longStyle = meetingAt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).withLocale(Locale.UK)); show("longStyle", longStyle); } catch (Throwable _t) { System.out.println("longStyle -> " + _t); }
        }
        {
            LocalDateTime meetingAt = LocalDateTime.of(2026, 3, 5, 14, 30, 15);
            try { String noZone = meetingAt.format(DateTimeFormatter.ofPattern("HH:mm z")); show("noZone", noZone); } catch (Throwable _t) { System.out.println("noZone -> " + _t); }
            ZonedDateTime inParis = meetingAt.atZone(ZoneId.of("Europe/Paris"));
            String withZone = inParis.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm z"));                    // "2026-03-05 14:30 CET"
            show("withZone", withZone);
            String withOffset = inParis.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);                            // "2026-03-05T14:30:15+01:00"
            show("withOffset", withOffset);
        }
        {
            DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
            LocalDateTime parsed = LocalDateTime.parse("2026-03-05 14:30:15", dbFormat);    // 2026-03-05T14:30:15
            show("parsed", parsed);
            try { LocalDateTime bad = LocalDateTime.parse("05/03/2026 14:30", dbFormat); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
            Optional<LocalDateTime> good = parseOrEmpty(" 2026-03-05 14:30:15 ", dbFormat);    // Optional[2026-03-05T14:30:15]
            show("good", good);
            Optional<LocalDateTime> wrong = parseOrEmpty("tomorrow", dbFormat);               // Optional.empty
            show("wrong", wrong);
        }
        {
            LocalDateTime orderedAt = LocalDateTime.of(2026, 7, 1, 8, 0);
            String text = orderedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));    // "2026-07-01 08:00:00"
            show("text", text);
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
