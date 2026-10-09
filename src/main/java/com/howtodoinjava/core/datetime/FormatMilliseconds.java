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
import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 * Examples for the tutorial "Format Milliseconds to hh:mm:ss in Java (Duration Examples)".
 * https://howtodoinjava.com/java/date-time/format-millis-to-hh-mm-ss/
 */
public class FormatMilliseconds {
    static String formatHms(Duration duration) {
        Duration d = duration.abs();
        String sign = duration.isNegative() ? "-" : "";
        if (d.toDays() > 0) {
            return String.format("%s%dd %02d:%02d:%02d", sign, d.toDaysPart(), d.toHoursPart(), d.toMinutesPart(), d.toSecondsPart());
        }
        return String.format("%s%02d:%02d:%02d", sign, d.toHours(), d.toMinutesPart(), d.toSecondsPart());
    }
    public static void main(String[] args) throws Exception {
        {
            long millis = 54_321_000L;
            Duration duration = Duration.ofMillis(millis);
            String hms = String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());   // "15:05:21"
            show("hms", hms);
            String iso = duration.toString();                                                                                      // "PT15H5M21S"
            show("iso", iso);
        }
        {
            Duration duration = Duration.ofMillis(54_321_000L);
            long totalHours = duration.toHours();          // 15
            show("totalHours", totalHours);
            long totalMinutes = duration.toMinutes();      // 905
            show("totalMinutes", totalMinutes);
            long totalSeconds = duration.toSeconds();      // 54321
            show("totalSeconds", totalSeconds);
            int minutesPart = duration.toMinutesPart();    // 5
            show("minutesPart", minutesPart);
            int secondsPart = duration.toSecondsPart();    // 21
            show("secondsPart", secondsPart);
            int millisPart = duration.toMillisPart();      // 0
            show("millisPart", millisPart);
        }
        {
            long millis = 54_321_000L;
            Duration duration = Duration.ofMillis(millis);
            long h = duration.toHours();                                   // 15
            show("h", h);
            long m = duration.toMinutes() % 60;                            // 5
            show("m", m);
            long s = duration.getSeconds() % 60;                           // 21
            show("s", s);
            String java8 = String.format("%02d:%02d:%02d", h, m, s);       // "15:05:21"
            show("java8", java8);
            long hoursWithTimeUnit = TimeUnit.MILLISECONDS.toHours(millis);              // 15
            show("hoursWithTimeUnit", hoursWithTimeUnit);
            long minutesWithTimeUnit = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;     // 5
            show("minutesWithTimeUnit", minutesWithTimeUnit);
        }
        {
            Duration longJob = Duration.ofMillis(90_061_000L);
            String totalHours = String.format("%02d:%02d:%02d", longJob.toHours(), longJob.toMinutesPart(), longJob.toSecondsPart());   // "25:01:01"
            show("totalHours", totalHours);
            String withDays = String.format("%dd %02d:%02d:%02d", longJob.toDaysPart(), longJob.toHoursPart(), longJob.toMinutesPart(), longJob.toSecondsPart());   // "1d 01:01:01"
            show("withDays", withDays);
            String wrong = String.format("%02d:%02d:%02d", longJob.toHoursPart(), longJob.toMinutesPart(), longJob.toSecondsPart());   // "01:01:01", the day is lost
            show("wrong", wrong);
        }
        {
            Duration query = Duration.ofMillis(3_723_045L);
            String withMillis = String.format("%02d:%02d:%02d.%03d", query.toHours(), query.toMinutesPart(), query.toSecondsPart(), query.toMillisPart());   // "01:02:03.045"
            show("withMillis", withMillis);
        }
        {
            Duration early = Duration.ofMillis(-5_400_000L);
            String broken = String.format("%02d:%02d:%02d", early.toHours(), early.toMinutesPart(), early.toSecondsPart());   // "-1:-30:00"
            show("broken", broken);
            Duration positive = early.abs();
            String sign = early.isNegative() ? "-" : "";
            String fixed = sign + String.format("%02d:%02d:%02d", positive.toHours(), positive.toMinutesPart(), positive.toSecondsPart());   // "-01:30:00"
            show("fixed", fixed);
        }
        {
            long millis = 54_321_000L;
            String padded = DurationFormatUtils.formatDuration(millis, "HH:mm:ss");            // "15:05:21"
            show("padded", padded);
            String unpadded = DurationFormatUtils.formatDuration(millis, "HH:mm:ss", false);   // "15:5:21"
            show("unpadded", unpadded);
            String hms = DurationFormatUtils.formatDurationHMS(millis);                        // "15:05:21.000"
            show("hms", hms);
            String longer = DurationFormatUtils.formatDuration(90_061_000L, "HH:mm:ss");       // "25:01:01"
            show("longer", longer);
            String withDays = DurationFormatUtils.formatDuration(90_061_000L, "d'd' HH:mm:ss");   // "1d 01:01:01"
            show("withDays", withDays);
            String words = DurationFormatUtils.formatDurationWords(90_061_000L, true, true);   // "1 day 1 hour 1 minute 1 second"
            show("words", words);
            String monthTrap = DurationFormatUtils.formatDuration(millis, "HH:MM:SS");         // "15:00:321000"
            show("monthTrap", monthTrap);
        }
        {
            long fiveSeconds = 5_000L;
            LocalTime asTimestamp = Instant.ofEpochMilli(fiveSeconds).atZone(ZoneId.of("Asia/Kolkata")).toLocalTime();   // 05:30:05
            show("asTimestamp", asTimestamp);
            LocalTime asTimeOfDay = LocalTime.ofNanoOfDay(Duration.ofMillis(fiveSeconds).toNanos());                     // 00:00:05
            show("asTimeOfDay", asTimeOfDay);
            String formatted = asTimeOfDay.format(DateTimeFormatter.ofPattern("HH:mm:ss"));                              // "00:00:05"
            show("formatted", formatted);
            try { LocalTime tooLong = LocalTime.ofNanoOfDay(Duration.ofMillis(90_061_000L).toNanos()); show("tooLong", tooLong); } catch (Throwable _t) { System.out.println("tooLong -> " + _t); }
        }
        {
            String invoices = formatHms(Duration.ofMillis(754_000L));                   // "00:12:34"
            show("invoices", invoices);
            String reports = formatHms(Duration.ofMillis(54_321_000L));                  // "15:05:21"
            show("reports", reports);
            String stuck = formatHms(Duration.ofMillis(90_061_000L));                    // "1d 01:01:01"
            show("stuck", stuck);
            String clockSkew = formatHms(Duration.ofMillis(-2_000L));                    // "-00:00:02"
            show("clockSkew", clockSkew);
        }
        {
            Duration track = Duration.ofMillis(225_000L);
            String mmss = String.format("%02d:%02d", track.toMinutes(), track.toSecondsPart());   // "03:45"
            show("mmss", mmss);
        }
        {
            long parsedMillis = LocalTime.parse("15:05:21").toNanoOfDay() / 1_000_000;   // 54321000
            show("parsedMillis", parsedMillis);
            String[] parts = "25:01:01".split(":");
            long longMillis = Duration.ofHours(Long.parseLong(parts[0])).plusMinutes(Long.parseLong(parts[1])).plusSeconds(Long.parseLong(parts[2])).toMillis();   // 90061000
            show("longMillis", longMillis);
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
