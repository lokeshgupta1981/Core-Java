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
 * Examples for the tutorial "ResolverStyle.STRICT vs SMART vs LENIENT Date Parsing in Java".
 * https://howtodoinjava.com/java/date-time/resolverstyle-strict-date-parsing/
 */
public class ResolverStyleStrict {
    static final class CheckInDates {
        private static final DateTimeFormatter FORMAT =
        DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

        static Optional<LocalDate> parse(String text) {
            if (text == null) {
                return Optional.empty();
            }
            try {
                return Optional.of(LocalDate.parse(text.strip(), FORMAT));
            } catch (DateTimeParseException e) {
                return Optional.empty();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            DateTimeFormatter strict = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
            LocalDate valid = LocalDate.parse("2019-02-28", strict);      // 2019-02-28
            show("valid", valid);
            try { LocalDate invalid = LocalDate.parse("2019-02-29", strict); show("invalid", invalid); } catch (Throwable _t) { System.out.println("invalid -> " + _t); }
            DateTimeFormatter smart = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            LocalDate adjusted = LocalDate.parse("2019-02-29", smart);    // 2019-02-28
            show("adjusted", adjusted);
        }
        {
            DateTimeFormatter base = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            LocalDate s1 = LocalDate.parse("2026-04-31", base);                                          // 2026-04-30
            show("s1", s1);
            try { LocalDate s2 = LocalDate.parse("2026-02-32", base); show("s2", s2); } catch (Throwable _t) { System.out.println("s2 -> " + _t); }
            LocalDate l1 = LocalDate.parse("2026-02-32", base.withResolverStyle(ResolverStyle.LENIENT));  // 2026-03-04
            show("l1", l1);
            LocalDate l2 = LocalDate.parse("2026-13-01", base.withResolverStyle(ResolverStyle.LENIENT));  // 2027-01-01
            show("l2", l2);
        }
        {
            ResolverStyle fromPattern = DateTimeFormatter.ofPattern("dd/MM/uuuu").getResolverStyle();   // SMART
            show("fromPattern", fromPattern);
            ResolverStyle isoDate = DateTimeFormatter.ISO_LOCAL_DATE.getResolverStyle();                 // STRICT
            show("isoDate", isoDate);
            ResolverStyle rfc = DateTimeFormatter.RFC_1123_DATE_TIME.getResolverStyle();                 // SMART
            show("rfc", rfc);
            try { LocalDate isoParse = LocalDate.parse("2019-02-29"); show("isoParse", isoParse); } catch (Throwable _t) { System.out.println("isoParse -> " + _t); }
        }
        {
            DateTimeFormatter yearOfEra = DateTimeFormatter.ofPattern("yyyy-MM-dd").withResolverStyle(ResolverStyle.STRICT);
            try { LocalDate fails = LocalDate.parse("2026-03-14", yearOfEra); show("fails", fails); } catch (Throwable _t) { System.out.println("fails -> " + _t); }
        }
        {
            DateTimeFormatter proleptic = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
            LocalDate checkIn = LocalDate.parse("2026-03-14", proleptic);        // 2026-03-14
            show("checkIn", checkIn);
            try { LocalDate badCheckIn = LocalDate.parse("2026-02-30", proleptic); show("badCheckIn", badCheckIn); } catch (Throwable _t) { System.out.println("badCheckIn -> " + _t); }
            DateTimeFormatter eraDefault = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd")
                    .parseDefaulting(ChronoField.ERA, 1)
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate withEra = LocalDate.parse("2026-03-14", eraDefault);       // 2026-03-14
            show("withEra", withEra);
        }
        {
            DateTimeFormatter stamp = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSS").withResolverStyle(ResolverStyle.STRICT);
            LocalDateTime ok = LocalDateTime.parse("2019-02-28T11:23:56.1234", stamp);     // 2019-02-28T11:23:56.123400
            show("ok", ok);
            try { LocalDateTime leap = LocalDateTime.parse("2019-02-29T11:23:56.1234", stamp); show("leap", leap); } catch (Throwable _t) { System.out.println("leap -> " + _t); }
        }
        {
            DateTimeFormatter minuteSmart = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
            LocalDateTime nextDay = LocalDateTime.parse("2026-03-14T24:00", minuteSmart);   // 2026-03-15T00:00
            show("nextDay", nextDay);
            DateTimeFormatter minuteStrict = minuteSmart.withResolverStyle(ResolverStyle.STRICT);
            try { LocalDateTime noHour24 = LocalDateTime.parse("2026-03-14T24:00", minuteStrict); show("noHour24", noHour24); } catch (Throwable _t) { System.out.println("noHour24 -> " + _t); }
            LocalTime lenientTime = LocalTime.parse("10:75", DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.LENIENT));   // 11:15
            show("lenientTime", lenientTime);
        }
        {
            Optional<LocalDate> april30 = CheckInDates.parse("30/04/2026");   // Optional[2026-04-30]
            show("april30", april30);
            Optional<LocalDate> april31 = CheckInDates.parse("31/04/2026");   // Optional.empty
            show("april31", april31);
            Optional<LocalDate> leapDay = CheckInDates.parse("29/02/2028");   // Optional[2028-02-29]
            show("leapDay", leapDay);
        }
        {
            DateTimeFormatter loose = DateTimeFormatter.ofPattern("d/M/uuuu").withResolverStyle(ResolverStyle.STRICT);
            LocalDate oneDigit = LocalDate.parse("4/3/2026", loose);   // 2026-03-04
            show("oneDigit", oneDigit);
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
