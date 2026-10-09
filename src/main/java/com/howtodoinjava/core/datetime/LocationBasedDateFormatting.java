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
 * Examples for the tutorial "Localized Date Format in Java with Locale and Time Zone".
 * https://howtodoinjava.com/java/date-time/locale-based-date-formatting/
 */
public class LocationBasedDateFormatting {
    public static void main(String[] args) throws Exception {
        {
            LocalDate classDate = LocalDate.of(2026, 3, 14);
            DateTimeFormatter medium = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
            String us = classDate.format(medium.withLocale(Locale.US));             // "Mar 14, 2026"
            show("us", us);
            String german = classDate.format(medium.withLocale(Locale.GERMANY));    // "14.03.2026"
            show("german", german);
            String french = classDate.format(medium.withLocale(Locale.FRANCE));     // "14 mars 2026"
            show("french", french);
            String japanese = classDate.format(medium.withLocale(Locale.JAPAN));    // "2026/03/14"
            show("japanese", japanese);
        }
        {
            LocalDateTime start = LocalDateTime.of(2026, 3, 14, 17, 45, 30);
            String ukTime = start.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.UK));          // "17:45"
            show("ukTime", ukTime);
            String usTime = start.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.US));          // "5:45 PM"
            show("usTime", usTime);
            String deBoth = start.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.GERMANY));   // "14.03.2026, 17:45:30"
            show("deBoth", deBoth);
            String mixed = start.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT).withLocale(Locale.US));   // "March 14, 2026, 5:45 PM"
            show("mixed", mixed);
        }
        {
            LocalDateTime noZone = LocalDateTime.of(2026, 3, 14, 17, 45, 30);
            try { String fails = noZone.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).withLocale(Locale.US)); show("fails", fails); } catch (Throwable _t) { System.out.println("fails -> " + _t); }
            ZonedDateTime withZone = noZone.atZone(ZoneId.of("America/New_York"));
            String longText = withZone.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).withLocale(Locale.US));   // "March 14, 2026, 5:45:30 PM EDT"
            show("longText", longText);
        }
        {
            LocalDate day = LocalDate.of(2026, 3, 14);
            DateTimeFormatter longDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
            Locale japaneseCalendar = Locale.forLanguageTag("ja-JP-u-ca-japanese");
            String gregorian = day.format(longDate.withLocale(japaneseCalendar));      // "2026年3月14日"
            show("gregorian", gregorian);
            String imperial = day.format(longDate.localizedBy(japaneseCalendar));      // "令和8年3月14日"
            show("imperial", imperial);
        }
        {
            Instant placedAt = Instant.parse("2026-03-14T21:45:30Z");
            DateTimeFormatter base = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
            String berlin = base.withLocale(Locale.GERMANY).withZone(ZoneId.of("Europe/Berlin")).format(placedAt);   // "14.03.2026, 22:45:30"
            show("berlin", berlin);
            String losAngeles = base.withLocale(Locale.US).withZone(ZoneId.of("America/Los_Angeles")).format(placedAt);   // "Mar 14, 2026, 2:45:30 PM"
            show("losAngeles", losAngeles);
            String tokyo = base.withLocale(Locale.JAPAN).withZone(ZoneId.of("Asia/Tokyo")).format(placedAt);       // "2026/03/15 6:45:30"
            show("tokyo", tokyo);
        }
        {
            LocalDate exam = LocalDate.of(2026, 3, 14);
            String usMonth = exam.format(DateTimeFormatter.ofLocalizedPattern("yMMM").withLocale(Locale.US));          // "Mar 2026"
            show("usMonth", usMonth);
            String deMonth = exam.format(DateTimeFormatter.ofLocalizedPattern("yMMM").withLocale(Locale.GERMANY));     // "März 2026"
            show("deMonth", deMonth);
            String usDay = exam.format(DateTimeFormatter.ofLocalizedPattern("MMMd").withLocale(Locale.US));            // "Mar 14"
            show("usDay", usDay);
            String ukDay = exam.format(DateTimeFormatter.ofLocalizedPattern("MMMd").withLocale(Locale.UK));            // "14 Mar"
            show("ukDay", ukDay);
            String frWeekday = exam.format(DateTimeFormatter.ofLocalizedPattern("yMMMEd").withLocale(Locale.FRANCE));  // "sam. 14 mars 2026"
            show("frWeekday", frWeekday);
        }
        {
            try { DateTimeFormatter wrongOrder = DateTimeFormatter.ofLocalizedPattern("MMMy"); show("wrongOrder", wrongOrder); } catch (Throwable _t) { System.out.println("wrongOrder -> " + _t); }
            String clock = LocalTime.of(17, 45).format(DateTimeFormatter.ofLocalizedPattern("jm").withLocale(Locale.GERMANY));   // "17:45"
            show("clock", clock);
        }
        {
            DateTimeFormatter usShort = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.US);
            try { LocalTime typed = LocalTime.parse("5:45 PM", usShort); show("typed", typed); } catch (Throwable _t) { System.out.println("typed -> " + _t); }
            LocalTime exact = LocalTime.parse("5:45\u202FPM", usShort);    // 17:45
            show("exact", exact);
        }
        {
            DateTimeFormatter lenient = new DateTimeFormatterBuilder()
                    .parseLenient()
                    .append(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                    .toFormatter(Locale.US);
            LocalTime fromUser = LocalTime.parse("5:45 PM", lenient);      // 17:45
            show("fromUser", fromUser);
        }
        {
            String spanish = Month.MARCH.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es"));   // "marzo"
            show("spanish", spanish);
            String italian = LocalDate.of(2026, 3, 14).format(DateTimeFormatter.ofPattern("d MMMM", Locale.ITALY));   // "14 marzo"
            show("italian", italian);
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
