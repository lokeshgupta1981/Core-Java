package com.howtodoinjava.misc;

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
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.format.DateTimeParseException;

/**
 * Examples for the tutorial "Calculate Age From Date of Birth in Java (Period, ChronoUnit)".
 * https://howtodoinjava.com/java/calculate-age-from-date-of-birth/
 */
public class CalculateAgePeriodBetween {
    static String describeAge(LocalDate birthDate, LocalDate on) {
        Period age = Period.between(birthDate, on);
        List<String> parts = new ArrayList<>();
        if (age.getYears() > 0) parts.add(age.getYears() + (age.getYears() == 1 ? " year" : " years"));
        if (age.getMonths() > 0) parts.add(age.getMonths() + (age.getMonths() == 1 ? " month" : " months"));
        if (age.getDays() > 0 || parts.isEmpty()) parts.add(age.getDays() + (age.getDays() == 1 ? " day" : " days"));
        return String.join(", ", parts);
    }
    static int ageInYears(LocalDate birthDate, LocalDate today) {
        Objects.requireNonNull(birthDate, "birthDate");
        if (birthDate.isAfter(today)) {
            throw new IllegalArgumentException("Birth date is in the future: " + birthDate);
        }
        return Period.between(birthDate, today).getYears();
    }
    static int currentAge(LocalDate birthDate, Clock clock) {
        return ageInYears(birthDate, LocalDate.now(clock));
    }
    static Optional<LocalDate> parseBirthDate(String text) {
        try {
            return Optional.of(LocalDate.parse(text.strip(), DAY_FIRST));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    static final DateTimeFormatter DAY_FIRST = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    static boolean isOldEnough(LocalDate birthDate, LocalDate eventDate, int minimumAge) {
        return !birthDate.isAfter(eventDate.minusYears(minimumAge));
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate birthDate = LocalDate.of(1990, Month.MAY, 14);
            LocalDate today = LocalDate.of(2026, Month.OCTOBER, 10);

            Period age = Period.between(birthDate, today);              // P36Y4M26D
            show("age", age);
            int years = age.getYears();                                 // 36
            show("years", years);
            int months = age.getMonths();                               // 4
            show("months", months);
            int days = age.getDays();                                   // 26
            show("days", days);
            long ageInYears = ChronoUnit.YEARS.between(birthDate, today); // 36
            show("ageInYears", ageInYears);
        }
        {
            String adult = describeAge(LocalDate.of(1990, 5, 14), LocalDate.of(2026, 10, 10)); // "36 years, 4 months, 26 days"
            show("adult", adult);
            String child = describeAge(LocalDate.of(2022, 8, 10), LocalDate.of(2026, 10, 10));  // "4 years, 2 months"
            show("child", child);
            String baby = describeAge(LocalDate.of(2026, 9, 9), LocalDate.of(2026, 10, 10));    // "1 month, 1 day"
            show("baby", baby);
        }
        {
            LocalDate birthDate = LocalDate.of(1990, 5, 14);
            LocalDate today = LocalDate.of(2026, 10, 10);

            long totalYears = ChronoUnit.YEARS.between(birthDate, today);    // 36
            show("totalYears", totalYears);
            long totalMonths = ChronoUnit.MONTHS.between(birthDate, today);  // 436
            show("totalMonths", totalMonths);
            long totalWeeks = ChronoUnit.WEEKS.between(birthDate, today);    // 1899
            show("totalWeeks", totalWeeks);
            long totalDays = ChronoUnit.DAYS.between(birthDate, today);      // 13298
            show("totalDays", totalDays);
            long sameDays = birthDate.until(today, ChronoUnit.DAYS);         // 13298
            show("sameDays", sameDays);
        }
        {
            LocalDate birthDate = LocalDate.of(1990, 5, 14);

            int dayBefore = Period.between(birthDate, LocalDate.of(2026, 5, 13)).getYears();  // 35
            show("dayBefore", dayBefore);
            int onBirthday = Period.between(birthDate, LocalDate.of(2026, 5, 14)).getYears(); // 36
            show("onBirthday", onBirthday);
            int yearMinus = 2026 - birthDate.getYear();                                       // 36, wrong on May 13
            show("yearMinus", yearMinus);
        }
        {
            LocalDate leapBirth = LocalDate.of(2000, 2, 29);

            int onFeb28 = Period.between(leapBirth, LocalDate.of(2025, 2, 28)).getYears();  // 24
            show("onFeb28", onFeb28);
            int onMar1 = Period.between(leapBirth, LocalDate.of(2025, 3, 1)).getYears();    // 25
            show("onMar1", onMar1);
            LocalDate adjusted = leapBirth.plusYears(25);                                   // 2025-02-28
            show("adjusted", adjusted);
        }
        {
            int valid = ageInYears(LocalDate.of(1990, 5, 14), LocalDate.of(2026, 10, 10));   // 36
            show("valid", valid);
            try { int future = ageInYears(LocalDate.of(2030, 1, 1), LocalDate.of(2026, 10, 10)); show("future", future); } catch (Throwable _t) { System.out.println("future -> " + _t); }
        }
        {
            Instant moment = Instant.parse("2026-05-13T20:00:00Z");
            Clock utcClock = Clock.fixed(moment, ZoneOffset.UTC);
            Clock sydneyClock = Clock.fixed(moment, ZoneId.of("Australia/Sydney"));

            LocalDate utcDate = LocalDate.now(utcClock);                      // 2026-05-13
            show("utcDate", utcDate);
            LocalDate sydneyDate = LocalDate.now(sydneyClock);                // 2026-05-14
            show("sydneyDate", sydneyDate);
            int ageInUtc = currentAge(LocalDate.of(1990, 5, 14), utcClock);   // 35
            show("ageInUtc", ageInUtc);
            int ageInSydney = currentAge(LocalDate.of(1990, 5, 14), sydneyClock); // 36
            show("ageInSydney", ageInSydney);
        }
        {
            LocalDate iso = LocalDate.parse("1990-05-14");                      // 1990-05-14
            show("iso", iso);
            Optional<LocalDate> fromForm = parseBirthDate(" 14/05/1990 ");      // Optional[1990-05-14]
            show("fromForm", fromForm);
            Optional<LocalDate> badDay = parseBirthDate("31/02/1990");          // Optional.empty
            show("badDay", badDay);
            try { LocalDate broken = LocalDate.parse("14/05/1990"); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            java.util.Date legacy = java.util.Date.from(Instant.parse("1990-05-14T00:00:00Z"));
            LocalDate fromUtilDate = legacy.toInstant().atZone(ZoneOffset.UTC).toLocalDate(); // 1990-05-14
            show("fromUtilDate", fromUtilDate);
            LocalDate fromSqlDate = java.sql.Date.valueOf("1990-05-14").toLocalDate();        // 1990-05-14
            show("fromSqlDate", fromSqlDate);
        }
        {
            LocalDate born = LocalDate.of(2001, 3, 1);
            LocalDate birthday = LocalDate.of(2026, 3, 1);
            long millis = Duration.between(born.atStartOfDay(), birthday.atStartOfDay()).toMillis();
            int approximate = (int) (millis / (365.25 * 24 * 60 * 60 * 1000));   // 24, wrong
            show("approximate", approximate);
            int correct = Period.between(born, birthday).getYears();             // 25
            show("correct", correct);
        }
        {
            org.joda.time.LocalDate jodaBirth = new org.joda.time.LocalDate(1990, 5, 14);
            org.joda.time.LocalDate jodaToday = new org.joda.time.LocalDate(2026, 10, 10);
            int jodaYears = org.joda.time.Years.yearsBetween(jodaBirth, jodaToday).getYears(); // 36
            show("jodaYears", jodaYears);
        }
        {
            LocalDate concert = LocalDate.of(2026, 12, 5);
            boolean eighteenOnTheDay = isOldEnough(LocalDate.of(2008, 12, 5), concert, 18);  // true
            show("eighteenOnTheDay", eighteenOnTheDay);
            boolean oneDayShort = isOldEnough(LocalDate.of(2008, 12, 6), concert, 18);       // false
            show("oneDayShort", oneDayShort);
            boolean leapDayBirth = isOldEnough(LocalDate.of(2008, 2, 29), LocalDate.of(2026, 2, 28), 18); // false
            show("leapDayBirth", leapDayBirth);
            boolean leapDayByPlus = !LocalDate.of(2008, 2, 29).plusYears(18).isAfter(LocalDate.of(2026, 2, 28)); // true
            show("leapDayByPlus", leapDayByPlus);
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
