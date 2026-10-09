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
import java.util.regex.*;

/**
 * Examples for the tutorial "Check If a Date Is Valid in Java (Strict Parsing Examples)".
 * https://howtodoinjava.com/java/date-time/date-validation/
 */
public class JavaDateValidations {
    static final class DateInput {
        private static final DateTimeFormatter US_DATE =
        DateTimeFormatter.ofPattern("MM-dd-uuuu").withResolverStyle(ResolverStyle.STRICT);

        static Optional<LocalDate> parse(String text) {
            if (text == null || text.isBlank()) {
                return Optional.empty();
            }
            try {
                return Optional.of(LocalDate.parse(text.strip(), US_DATE));
            } catch (DateTimeParseException e) {
                return Optional.empty();
            }
        }

        static boolean isValidDate(String text) {
            return parse(text).isPresent();
        }
    }
    static String checkAppointment(String text, LocalDate today) {
        Optional<LocalDate> parsed = DateInput.parse(text);
        if (parsed.isEmpty()) {
            return "not a valid date";
        }
        LocalDate date = parsed.get();
        if (date.isBefore(today)) {
            return "date is in the past";
        }
        if (date.isAfter(today.plusDays(90))) {
            return "more than 90 days ahead";
        }
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return "clinic is closed on Sundays";
        }
        return "ok";
    }
    public static void main(String[] args) throws Exception {
        {
            boolean leapDay = DateInput.isValidDate("02-29-2028");    // true, 2028 is a leap year
            show("leapDay", leapDay);
            boolean noLeap = DateInput.isValidDate("02-29-2026");     // false
            show("noLeap", noLeap);
            boolean april31 = DateInput.isValidDate("04-31-2026");    // false
            show("april31", april31);
            boolean isoText = DateInput.isValidDate("2026-04-30");    // false, wrong layout
            show("isoText", isoText);
        }
        {
            LocalDate valid = LocalDate.parse("2023-02-08");          // 2023-02-08
            show("valid", valid);
            int day = valid.getDayOfMonth();                          // 8
            show("day", day);
            try { LocalDate feb30 = LocalDate.parse("2023-02-30"); show("feb30", feb30); } catch (Throwable _t) { System.out.println("feb30 -> " + _t); }
            try { LocalDate oneDigit = LocalDate.parse("2023-2-8"); show("oneDigit", oneDigit); } catch (Throwable _t) { System.out.println("oneDigit -> " + _t); }
        }
        {
            DateTimeFormatter smart = DateTimeFormatter.ofPattern("MM-dd-uuuu");
            LocalDate moved = LocalDate.parse("02-30-2023", smart);      // 2023-02-28
            show("moved", moved);
            try { LocalDate day32 = LocalDate.parse("01-32-2023", smart); show("day32", day32); } catch (Throwable _t) { System.out.println("day32 -> " + _t); }
        }
        {
            Optional<LocalDate> parsed = DateInput.parse("01-26-2023");   // Optional[2023-01-26]
            show("parsed", parsed);
            Optional<LocalDate> feb30 = DateInput.parse("02-30-2023");    // Optional.empty
            show("feb30", feb30);
            Optional<LocalDate> blank = DateInput.parse("  ");            // Optional.empty
            show("blank", blank);
            boolean nullText = DateInput.isValidDate(null);               // false
            show("nullText", nullText);
        }
        {
            LocalDate fromFields = LocalDate.of(2028, 2, 29);                 // 2028-02-29
            show("fromFields", fromFields);
            try { LocalDate badFields = LocalDate.of(2026, 2, 29); show("badFields", badFields); } catch (Throwable _t) { System.out.println("badFields -> " + _t); }
            boolean dayExists = YearMonth.of(2026, 4).isValidDay(31);         // false
            show("dayExists", dayExists);
            boolean leapYear = Year.isLeap(2028);                             // true
            show("leapYear", leapYear);
            int daysInFeb = YearMonth.of(2026, 2).lengthOfMonth();            // 28
            show("daysInFeb", daysInFeb);
        }
        {
            LocalDate today = LocalDate.of(2026, 3, 14);
            String monday = checkAppointment("03-16-2026", today);    // "ok"
            show("monday", monday);
            String sunday = checkAppointment("03-15-2026", today);    // "clinic is closed on Sundays"
            show("sunday", sunday);
            String past = checkAppointment("03-01-2026", today);      // "date is in the past"
            show("past", past);
            String tooFar = checkAppointment("07-01-2026", today);    // "more than 90 days ahead"
            show("tooFar", tooFar);
            String typo = checkAppointment("03-32-2026", today);      // "not a valid date"
            show("typo", typo);
        }
        {
            DateTimeFormatter either = DateTimeFormatter.ofPattern("[MM-dd-uuuu][MM/dd/uuuu]").withResolverStyle(ResolverStyle.STRICT);
            LocalDate dash = LocalDate.parse("03-14-2026", either);     // 2026-03-14
            show("dash", dash);
            LocalDate slash = LocalDate.parse("03/14/2026", either);    // 2026-03-14
            show("slash", slash);
            try { LocalDate wrong = LocalDate.parse("02/30/2026", either); show("wrong", wrong); } catch (Throwable _t) { System.out.println("wrong -> " + _t); }
        }
        {
            boolean shapeOk = Pattern.matches("\\d{2}-\\d{2}-\\d{4}", "04-31-2026");   // true
            show("shapeOk", shapeOk);
            boolean realDate = DateInput.isValidDate("04-31-2026");                  // false
            show("realDate", realDate);
        }
        {
            DateTimeFormatter dayFirst = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
            LocalDate march = LocalDate.parse("14/03/2026", dayFirst);   // 2026-03-14
            show("march", march);
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
