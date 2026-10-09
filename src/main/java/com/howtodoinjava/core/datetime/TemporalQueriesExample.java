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
 * Examples for the tutorial "Java TemporalQuery Interface with TemporalQueries Examples".
 * https://howtodoinjava.com/java/date-time/temporalquery/
 */
public class TemporalQueriesExample {
    static boolean isBusinessHours(TemporalAccessor temporal) {
        LocalTime time = LocalTime.from(temporal);
        return !time.isBefore(LocalTime.of(9, 0)) && time.isBefore(LocalTime.of(17, 0));
    }
    static record FiscalQuarter(Month firstMonth) implements TemporalQuery<Integer> {
        @Override
        public Integer queryFrom(TemporalAccessor temporal) {
            int month = temporal.get(ChronoField.MONTH_OF_YEAR);
            int monthsIntoYear = Math.floorMod(month - firstMonth.getValue(), 12);
            return monthsIntoYear / 3 + 1;
        }
    }
    static Map<Integer, Long> ordersPerQuarter(List<OffsetDateTime> orders, ZoneId companyZone) {
        TemporalQuery<Integer> quarter = new FiscalQuarter(Month.APRIL);
        return orders.stream()
                .map(o -> o.atZoneSameInstant(companyZone))
                .collect(Collectors.groupingBy(z -> z.query(quarter), TreeMap::new, Collectors.counting()));
    }
    public static void main(String[] args) throws Exception {
        {
            ZonedDateTime order = ZonedDateTime.of(2026, 3, 14, 9, 30, 0, 0, ZoneId.of("Europe/Paris"));
            LocalDate date = order.query(TemporalQueries.localDate());           // 2026-03-14
            show("date", date);
            ZoneId zone = order.query(TemporalQueries.zone());                   // Europe/Paris
            show("zone", zone);
            TemporalUnit precision = order.query(TemporalQueries.precision());   // Nanos
            show("precision", precision);
            YearMonth month = order.query(YearMonth::from);                      // 2026-03
            show("month", month);
            TemporalQuery<Boolean> isWeekend = t -> DayOfWeek.from(t).getValue() >= 6;
            boolean weekend = order.query(isWeekend);                            // true
            show("weekend", weekend);
        }
        {
            LocalDate day = LocalDate.of(2026, 3, 14);
            YearMonth viaQuery = day.query(YearMonth::from);                            // 2026-03
            show("viaQuery", viaQuery);
            YearMonth viaQueryFrom = ((TemporalQuery<YearMonth>) YearMonth::from).queryFrom(day);   // 2026-03
            show("viaQueryFrom", viaQueryFrom);
        }
        {
            LocalDateTime local = LocalDateTime.of(2026, 3, 14, 9, 30);
            OffsetDateTime offsetTime = OffsetDateTime.of(local, ZoneOffset.ofHours(2));
            ZoneId strict = offsetTime.query(TemporalQueries.zoneId());           // null
            show("strict", strict);
            ZoneId lenient = offsetTime.query(TemporalQueries.zone());            // +02:00
            show("lenient", lenient);
            ZoneOffset offset = offsetTime.query(TemporalQueries.offset());       // +02:00
            show("offset", offset);
            ZoneId noZone = local.query(TemporalQueries.zone());                  // null
            show("noZone", noZone);
            TemporalUnit dateOnly = LocalDate.of(2026, 3, 14).query(TemporalQueries.precision());   // Days
            show("dateOnly", dateOnly);
        }
        {
            ZonedDateTime paid = ZonedDateTime.of(2026, 3, 14, 9, 30, 0, 0, ZoneId.of("Europe/Paris"));
            LocalDate paidOn = paid.query(LocalDate::from);          // 2026-03-14
            show("paidOn", paidOn);
            YearMonth billingMonth = paid.query(YearMonth::from);    // 2026-03
            show("billingMonth", billingMonth);
            DayOfWeek weekday = paid.query(DayOfWeek::from);         // SATURDAY
            show("weekday", weekday);
            MonthDay yearlyDate = paid.query(MonthDay::from);        // --03-14
            show("yearlyDate", yearlyDate);
            try { LocalDate fromTime = LocalTime.of(9, 30).query(LocalDate::from); show("fromTime", fromTime); } catch (Throwable _t) { System.out.println("fromTime -> " + _t); }
        }
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd[ HH:mm]");
            LocalDateTime withTime = formatter.parse("2026-03-14 10:15", LocalDateTime::from);                 // 2026-03-14T10:15
            show("withTime", withTime);
            TemporalAccessor best = formatter.parseBest("2026-03-14", LocalDateTime::from, LocalDate::from);   // 2026-03-14
            show("best", best);
        }
        {
            TemporalQuery<Boolean> businessHours = t -> isBusinessHours(t);   // a query built from the method
            show("businessHours", businessHours);
            boolean morning = LocalTime.of(9, 0).query(businessHours);                                   // true
            show("morning", morning);
            boolean closing = LocalDateTime.of(2026, 3, 13, 17, 0).query(businessHours);                 // false
            show("closing", closing);
            boolean zoned = ZonedDateTime.of(2026, 3, 13, 16, 59, 0, 0, ZoneId.of("Asia/Kolkata")).query(businessHours);   // true
            show("zoned", zoned);
        }
        {
            TemporalQuery<Integer> aprilYear = new FiscalQuarter(Month.APRIL);
            int calendarQuarter = LocalDate.of(2026, 3, 14).get(IsoFields.QUARTER_OF_YEAR);   // 1
            show("calendarQuarter", calendarQuarter);
            int march = LocalDate.of(2026, 3, 14).query(aprilYear);                           // 4
            show("march", march);
            int april = LocalDate.of(2026, 4, 1).query(aprilYear);                            // 1
            show("april", april);
            int december = YearMonth.of(2026, 12).query(aprilYear);                           // 3
            show("december", december);
            try { int noDate = LocalTime.NOON.query(aprilYear); show("noDate", noDate); } catch (Throwable _t) { System.out.println("noDate -> " + _t); }
        }
        {
            List<OffsetDateTime> orders = List.of(
            OffsetDateTime.parse("2026-03-31T21:00-04:00"),
            OffsetDateTime.parse("2026-03-02T10:00+01:00"),
            OffsetDateTime.parse("2026-05-20T08:00+05:30"));
            Map<Integer, Long> report = ordersPerQuarter(orders, ZoneId.of("Asia/Kolkata"));   // {1=2, 4=1}
            show("report", report);
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
