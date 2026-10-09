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
 * Examples for the tutorial "Convert String to LocalDate in Java: Patterns, Locales, Errors".
 * https://howtodoinjava.com/java/date-time/localdate-parse-string/
 */
public class ParseStringToLocalDate {
    static final class LoanRows {
        private static final DateTimeFormatter DUE_DATE =
        DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

        static Optional<LocalDate> parseDueDate(String text) {
            if (text == null || text.isBlank()) {
                return Optional.empty();
            }
            try {
                return Optional.of(LocalDate.parse(text.strip(), DUE_DATE));
            } catch (DateTimeParseException e) {
                return Optional.empty();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate iso = LocalDate.parse("2026-03-14");                          // 2026-03-14
            show("iso", iso);
            DateTimeFormatter dayFirst = DateTimeFormatter.ofPattern("dd/MM/uuuu");
            LocalDate custom = LocalDate.parse("14/03/2026", dayFirst);              // 2026-03-14
            show("custom", custom);
            DateTimeFormatter withName = DateTimeFormatter.ofPattern("d MMM uuuu", Locale.ENGLISH);
            LocalDate named = LocalDate.parse("4 Mar 2026", withName);               // 2026-03-04
            show("named", named);
            try { LocalDate wrong = LocalDate.parse("2026/03/14"); show("wrong", wrong); } catch (Throwable _t) { System.out.println("wrong -> " + _t); }
        }
        {
            LocalDate leap = LocalDate.parse("2028-02-29");          // 2028-02-29
            show("leap", leap);
            try { LocalDate oneDigit = LocalDate.parse("2026-3-14"); show("oneDigit", oneDigit); } catch (Throwable _t) { System.out.println("oneDigit -> " + _t); }
            try { LocalDate noSuchDay = LocalDate.parse("2026-02-30"); show("noSuchDay", noSuchDay); } catch (Throwable _t) { System.out.println("noSuchDay -> " + _t); }
            try { LocalDate notLeap = LocalDate.parse("2019-02-29"); show("notLeap", notLeap); } catch (Throwable _t) { System.out.println("notLeap -> " + _t); }
            try { LocalDate withTime = LocalDate.parse("2026-03-14T10:15"); show("withTime", withTime); } catch (Throwable _t) { System.out.println("withTime -> " + _t); }
        }
        {
            DateTimeFormatter usStyle = DateTimeFormatter.ofPattern("MM/dd/uuuu");
            LocalDate us = LocalDate.parse("03/14/2026", usStyle);         // 2026-03-14
            show("us", us);
            DateTimeFormatter dotted = DateTimeFormatter.ofPattern("dd.MM.uuuu");
            LocalDate german = LocalDate.parse("14.03.2026", dotted);      // 2026-03-14
            show("german", german);
            LocalDate compact = LocalDate.parse("20260314", DateTimeFormatter.BASIC_ISO_DATE);   // 2026-03-14
            show("compact", compact);
        }
        {
            LocalDate loose = LocalDate.parse("4/3/2026", DateTimeFormatter.ofPattern("d/M/uuuu"));     // 2026-03-04
            show("loose", loose);
            try { LocalDate padded = LocalDate.parse("4/3/2026", DateTimeFormatter.ofPattern("dd/MM/uuuu")); show("padded", padded); } catch (Throwable _t) { System.out.println("padded -> " + _t); }
        }
        {
            try { LocalDate minutes = LocalDate.parse("2026-03-14", DateTimeFormatter.ofPattern("uuuu-mm-dd")); show("minutes", minutes); } catch (Throwable _t) { System.out.println("minutes -> " + _t); }
            try { LocalDate weekYear = LocalDate.parse("2026-03-14", DateTimeFormatter.ofPattern("YYYY-MM-dd")); show("weekYear", weekYear); } catch (Throwable _t) { System.out.println("weekYear -> " + _t); }
        }
        {
            DateTimeFormatter smart = DateTimeFormatter.ofPattern("dd/MM/uuuu");
            LocalDate adjusted = LocalDate.parse("31/02/2026", smart);       // 2026-02-28
            show("adjusted", adjusted);
            DateTimeFormatter strict = smart.withResolverStyle(ResolverStyle.STRICT);
            try { LocalDate rejected = LocalDate.parse("31/02/2026", strict); show("rejected", rejected); } catch (Throwable _t) { System.out.println("rejected -> " + _t); }
        }
        {
            DateTimeFormatter english = DateTimeFormatter.ofPattern("dd-MMM-uuuu", Locale.ENGLISH);
            LocalDate en = LocalDate.parse("29-May-2026", english);                 // 2026-05-29
            show("en", en);
            DateTimeFormatter french = english.withLocale(Locale.FRENCH);
            LocalDate fr = LocalDate.parse("29-mai-2026", french);                  // 2026-05-29
            show("fr", fr);
            DateTimeFormatter german = DateTimeFormatter.ofPattern("d. MMMM uuuu", Locale.GERMAN);
            LocalDate de = LocalDate.parse("4. M\u00e4rz 2026", german);             // 2026-03-04
            show("de", de);
        }
        {
            DateTimeFormatter english = DateTimeFormatter.ofPattern("dd-MMM-uuuu", Locale.ENGLISH);
            try { LocalDate lower = LocalDate.parse("14-mar-2026", english); show("lower", lower); } catch (Throwable _t) { System.out.println("lower -> " + _t); }
            try { LocalDate ukSep = LocalDate.parse("14 Sep 2026", DateTimeFormatter.ofPattern("dd MMM uuuu", Locale.UK)); show("ukSep", ukSep); } catch (Throwable _t) { System.out.println("ukSep -> " + _t); }
            DateTimeFormatter anyCase = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("dd-MMM-uuuu")
                    .toFormatter(Locale.ENGLISH);
            LocalDate fixed = LocalDate.parse("14-mar-2026", anyCase);              // 2026-03-14
            show("fixed", fixed);
        }
        {
            Optional<LocalDate> ok = LoanRows.parseDueDate(" 14/03/2026 ");    // Optional[2026-03-14]
            show("ok", ok);
            Optional<LocalDate> badDay = LoanRows.parseDueDate("31/04/2026");    // Optional.empty
            show("badDay", badDay);
            Optional<LocalDate> empty = LoanRows.parseDueDate(null);             // Optional.empty
            show("empty", empty);
            LocalDate due = LoanRows.parseDueDate("14/03/2026").orElseThrow();   // 2026-03-14
            show("due", due);
        }
        {
            int errorAt = -1;
            try {
                LocalDate unused = LocalDate.parse("2026/03/14");
            } catch (DateTimeParseException e) {
                errorAt = e.getErrorIndex();
            }
            int position = errorAt;                                      // 4
            show("position", position);
        }
        {
            LocalDate fromDateTime = LocalDateTime.parse("2026-03-14T10:15:30").toLocalDate();               // 2026-03-14
            show("fromDateTime", fromDateTime);
            LocalDate isoDateTime = LocalDate.parse("2026-03-14T10:15:30", DateTimeFormatter.ISO_DATE_TIME);   // 2026-03-14
            show("isoDateTime", isoDateTime);
            Instant paidAt = Instant.parse("2026-03-14T23:30:00Z");
            LocalDate tokyoDay = LocalDate.ofInstant(paidAt, ZoneId.of("Asia/Tokyo"));                        // 2026-03-15
            show("tokyoDay", tokyoDay);
        }
        {
            DateTimeFormatter either = DateTimeFormatter.ofPattern("[dd/MM/uuuu][dd.MM.uuuu][uuuu-MM-dd]");
            LocalDate slash = LocalDate.parse("14/03/2026", either);    // 2026-03-14
            show("slash", slash);
            LocalDate dot = LocalDate.parse("14.03.2026", either);      // 2026-03-14
            show("dot", dot);
            LocalDate isoAlso = LocalDate.parse("2026-03-14", either);  // 2026-03-14
            show("isoAlso", isoAlso);
        }
        {
            DateTimeFormatter window = new DateTimeFormatterBuilder()
                    .appendPattern("dd/MM/")
                    .appendValueReduced(ChronoField.YEAR, 2, 2, 1950)
                    .toFormatter();
            LocalDate oldYear = LocalDate.parse("14/03/99", window);   // 1999-03-14
            show("oldYear", oldYear);
            LocalDate newYear = LocalDate.parse("14/03/26", window);   // 2026-03-14
            show("newYear", newYear);
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
