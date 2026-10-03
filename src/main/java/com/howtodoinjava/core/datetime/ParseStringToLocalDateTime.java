package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * Parses strings into LocalDateTime: the default ISO format, custom patterns, AM/PM and month
 * names with a Locale, strings with a zone or offset, date-only strings, strict resolution and
 * the DateTimeParseException messages for common mistakes. Ends with formatting back to text.
 *
 * <p>Source code for the article "Convert String to LocalDateTime in Java" on howtodoinjava.com.
 */
public class ParseStringToLocalDateTime {

  public static void main(String[] args) {
    quickExamples();
    isoFormatRules();
    customPatterns();
    amPmAndLocale();
    stringsWithZoneOrOffset();
    dateOnlyStrings();
    strictResolution();
    severalFormats();
    parseErrors();
    formatBack();
  }

  // 0. The two parse() methods
  static void quickExamples() {
    LocalDateTime iso = LocalDateTime.parse("2026-10-04T10:15:30");
    System.out.println("ISO                       : " + iso);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    LocalDateTime custom = LocalDateTime.parse("04-10-2026 10:15", formatter);
    System.out.println("dd-MM-yyyy HH:mm          : " + custom);
  }

  // 1. What ISO_LOCAL_DATE_TIME accepts
  static void isoFormatRules() {
    show("no seconds", () -> LocalDateTime.parse("2026-10-04T10:15"));
    show("seconds", () -> LocalDateTime.parse("2026-10-04T10:15:30"));
    show("millis", () -> LocalDateTime.parse("2026-10-04T10:15:30.123"));
    show("nanos", () -> LocalDateTime.parse("2026-10-04T10:15:30.123456789"));
    show("lowercase t", () -> LocalDateTime.parse("2026-10-04t10:15:30"));
    show("space instead of T", () -> LocalDateTime.parse("2026-10-04 10:15:30"));
    show("one-digit hour", () -> LocalDateTime.parse("2026-10-04T9:15:30"));
    show("no minutes", () -> LocalDateTime.parse("2026-10-04T10"));
    show("10 fraction digits", () -> LocalDateTime.parse("2026-10-04T10:15:30.1234567890"));
    show("ISO_LOCAL_DATE_TIME", () -> LocalDateTime.parse("2026-10-04T10:15:30",
        DateTimeFormatter.ISO_LOCAL_DATE_TIME));
  }

  // 2. Custom patterns
  static void customPatterns() {
    show("yyyy-MM-dd HH:mm:ss", () -> LocalDateTime.parse("2026-10-04 10:15:30",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    show("dd/MM/yyyy HH:mm", () -> LocalDateTime.parse("04/10/2026 22:15",
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    show("MM/dd/yyyy HH:mm", () -> LocalDateTime.parse("10/04/2026 22:15",
        DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")));
    show("yyyyMMddHHmmss", () -> LocalDateTime.parse("20261004101530",
        DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
    show("yyyy-MM-dd HH:mm:ss.SSS", () -> LocalDateTime.parse("2026-10-04 10:15:30.123",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
    show("SSSSSS micros", () -> LocalDateTime.parse("2026-10-04 10:15:30.123456",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));
    show("SSS with 6 digits", () -> LocalDateTime.parse("2026-10-04 10:15:30.123456",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
    show("yyyy-MM-dd'T'HH:mm",() -> LocalDateTime.parse("2026-10-04T10:15",
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
    show("d/M/yyyy H:mm", () -> LocalDateTime.parse("4/10/2026 9:05",
        DateTimeFormatter.ofPattern("d/M/yyyy H:mm")));
    show("d/M/yyyy H:mm two digits", () -> LocalDateTime.parse("14/10/2026 19:05",
        DateTimeFormatter.ofPattern("d/M/yyyy H:mm")));
    show("dd/MM/yyyy with 4/10/2026", () -> LocalDateTime.parse("4/10/2026 09:05",
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    // the old article's example: HH with a
    show("yyyy-MM-dd HH:mm:ss a", () -> LocalDateTime.parse("2019-03-27 10:15:30 AM",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a", Locale.ENGLISH)));
    show("HH=22 with AM", () -> LocalDateTime.parse("2019-03-27 22:15:30 AM",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a", Locale.ENGLISH)));
  }

  // 3. AM/PM and month names need a Locale
  static void amPmAndLocale() {
    DateTimeFormatter twelveHour =
        DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
    show("hh:mm a PM", () -> LocalDateTime.parse("04-10-2026 10:15 PM", twelveHour));
    show("hh:mm a AM", () -> LocalDateTime.parse("04-10-2026 10:15 AM", twelveHour));
    show("12:30 AM", () -> LocalDateTime.parse("04-10-2026 12:30 AM", twelveHour));
    show("12:30 PM", () -> LocalDateTime.parse("04-10-2026 12:30 PM", twelveHour));
    show("lowercase pm", () -> LocalDateTime.parse("04-10-2026 10:15 pm", twelveHour));

    DateTimeFormatter caseInsensitive = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern("dd-MM-yyyy hh:mm a")
        .toFormatter(Locale.ENGLISH);
    show("pm, parseCaseInsensitive", () -> LocalDateTime.parse("04-10-2026 10:15 pm",
        caseInsensitive));

    show("hh without a", () -> LocalDateTime.parse("04-10-2026 10:15",
        DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm")));

    DateTimeFormatter monthName =
        DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a", Locale.ENGLISH);
    show("MMM d, yyyy h:mm a", () -> LocalDateTime.parse("Oct 4, 2026 10:15 AM", monthName));
    show("MMMM full name", () -> LocalDateTime.parse("October 4, 2026 10:15 AM",
        DateTimeFormatter.ofPattern("MMMM d, yyyy h:mm a", Locale.ENGLISH)));
    show("MMM with GERMAN", () -> LocalDateTime.parse("Oct 4, 2026 10:15 AM",
        DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a", Locale.GERMAN)));
    show("Sep with en-GB", () -> LocalDateTime.parse("Sep 4, 2026 10:15",
        DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm", Locale.UK)));
    show("Sept with en-GB", () -> LocalDateTime.parse("Sept 4, 2026 10:15",
        DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm", Locale.UK)));
    show("Sep with ENGLISH", () -> LocalDateTime.parse("Sep 4, 2026 10:15",
        DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm", Locale.ENGLISH)));
    show("EEE, MMM", () -> LocalDateTime.parse("Sun, Oct 4 2026 10:15",
        DateTimeFormatter.ofPattern("EEE, MMM d yyyy HH:mm", Locale.ENGLISH)));

    // the old article's French example
    DateTimeFormatter french = DateTimeFormatter.ofPattern("yyyy-MMMM-dd HH:mm:ss a")
        .withLocale(Locale.FRENCH);
    show("French MMMM", () -> LocalDateTime.parse("2019-mai-29 10:15:30 AM", french));
    show("French octobre", () -> LocalDateTime.parse("4 octobre 2026 10:15",
        DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm", Locale.FRENCH)));
    show("French oct.", () -> LocalDateTime.parse("4 oct. 2026 10:15",
        DateTimeFormatter.ofPattern("d MMM yyyy HH:mm", Locale.FRENCH)));
  }

  // 4. Strings with an offset or zone
  static void stringsWithZoneOrOffset() {
    show("LocalDateTime with Z", () -> LocalDateTime.parse("2026-10-04T10:15:30Z"));
    show("OffsetDateTime Z", () -> OffsetDateTime.parse("2026-10-04T10:15:30Z")
        .toLocalDateTime());
    show("OffsetDateTime +05:30", () -> OffsetDateTime.parse("2026-10-04T10:15:30+05:30")
        .toLocalDateTime());
    show("ZonedDateTime region", () -> ZonedDateTime.parse(
        "2026-10-04T10:15:30+05:30[Asia/Kolkata]").toLocalDateTime());
    show("pattern with XXX", () -> OffsetDateTime.parse("2026-10-04 10:15:30 +05:30",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX")).toLocalDateTime());
    show("LocalDateTime pattern XXX", () -> LocalDateTime.parse("2026-10-04 10:15:30 +05:30",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX")));
    show("ISO_DATE_TIME", () -> LocalDateTime.parse("2026-10-04T10:15:30+05:30",
        DateTimeFormatter.ISO_DATE_TIME));
  }

  // 5. Date-only strings
  static void dateOnlyStrings() {
    show("LocalDateTime date only", () -> LocalDateTime.parse("2026-10-04"));
    show("pattern without time", () -> LocalDateTime.parse("04-10-2026",
        DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    show("atStartOfDay", () -> LocalDate.parse("2026-10-04").atStartOfDay());
    show("pattern atStartOfDay", () -> LocalDate.parse("04-10-2026",
        DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay());
    show("atTime(9, 30)", () -> LocalDate.parse("2026-10-04").atTime(9, 30));
    show("atTime(LocalTime.MAX)", () -> LocalDate.parse("2026-10-04").atTime(LocalTime.MAX));

    DateTimeFormatter dateWithDefaults = new DateTimeFormatterBuilder()
        .appendPattern("dd-MM-yyyy")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .toFormatter();
    show("parseDefaulting", () -> LocalDateTime.parse("04-10-2026", dateWithDefaults));
  }

  // 6. SMART vs STRICT resolution and the uuuu trap
  static void strictResolution() {
    DateTimeFormatter smart = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    show("SMART 30-02", () -> LocalDateTime.parse("30-02-2026 10:15", smart));
    show("SMART 31-04", () -> LocalDateTime.parse("31-04-2026 10:15", smart));
    show("SMART 32-01", () -> LocalDateTime.parse("32-01-2026 10:15", smart));
    show("SMART 24:00", () -> LocalDateTime.parse("04-10-2026 24:00", smart));
    show("ISO 2026-02-30", () -> LocalDateTime.parse("2026-02-30T10:15"));

    DateTimeFormatter strictYyyy = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        .withResolverStyle(ResolverStyle.STRICT);
    show("STRICT yyyy valid", () -> LocalDateTime.parse("04-10-2026 10:15", strictYyyy));

    DateTimeFormatter strict = DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm")
        .withResolverStyle(ResolverStyle.STRICT);
    show("STRICT uuuu valid", () -> LocalDateTime.parse("04-10-2026 10:15", strict));
    show("STRICT uuuu 30-02", () -> LocalDateTime.parse("30-02-2026 10:15", strict));
    show("STRICT uuuu 24:00", () -> LocalDateTime.parse("04-10-2026 24:00", strict));

    DateTimeFormatter lenient = DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm")
        .withResolverStyle(ResolverStyle.LENIENT);
    show("LENIENT 30-02", () -> LocalDateTime.parse("30-02-2026 10:15", lenient));
  }

  // 7. Optional sections for more than one input format
  static void severalFormats() {
    DateTimeFormatter flexible = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'][ ]HH:mm[:ss]");
    show("flexible T", () -> LocalDateTime.parse("2026-10-04T10:15:30", flexible));
    show("flexible space", () -> LocalDateTime.parse("2026-10-04 10:15", flexible));
  }

  // 8. DateTimeParseException messages and details
  static void parseErrors() {
    try {
      LocalDateTime.parse("2026-10-04 10:15:30");
    } catch (DateTimeParseException e) {
      System.out.println("getMessage                : " + e.getMessage());
      System.out.println("getErrorIndex             : " + e.getErrorIndex());
      System.out.println("getParsedString           : " + e.getParsedString());
    }
    show("wrong separator", () -> LocalDateTime.parse("04/10/2026 10:15",
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
    show("extra text", () -> LocalDateTime.parse("04-10-2026 10:15:30",
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
    show("month 13", () -> LocalDateTime.parse("04-13-2026 10:15",
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
    show("mm for month", () -> LocalDateTime.parse("04-10-2026 10:15",
        DateTimeFormatter.ofPattern("dd-mm-yyyy HH:mm")));
    show("YYYY week year", () -> LocalDateTime.parse("04-10-2026 10:15",
        DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm")));
    show("DD day of year", () -> LocalDateTime.parse("04-10-2026 10:15",
        DateTimeFormatter.ofPattern("DD-MM-yyyy HH:mm")));
    show("null text", () -> LocalDateTime.parse(null));
  }

  // 9. Back to a String
  static void formatBack() {
    LocalDateTime dateTime = LocalDateTime.parse("04-10-2026 22:15",
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    System.out.println("toString                  : " + dateTime);
    System.out.println("toString zero seconds     : " + LocalDateTime.parse("2026-10-04T10:15:00"));
    System.out.println("ISO_LOCAL_DATE_TIME       : "
        + dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    System.out.println("yyyy-MM-dd HH:mm:ss       : "
        + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    System.out.println("dd MMM yyyy, hh:mm a      : "
        + dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.ENGLISH)));
    System.out.println("EEEE, MMMM d              : "
        + dateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH)));
    System.out.println("atZone Europe/Paris       : " + dateTime.atZone(ZoneId.of("Europe/Paris")));
    show("round trip",() -> LocalDateTime.parse(
        dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")).equals(dateTime));
  }

  static void show(String label, Supplier<Object> parse) {
    String name = String.format("%-26s: ", label);
    try {
      System.out.println(name + parse.get());
    } catch (RuntimeException e) {
      System.out.println(name + e.getClass().getSimpleName() + ": " + e.getMessage());
    }
  }
}
