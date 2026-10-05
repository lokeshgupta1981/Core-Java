package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Optional;

/**
 * Examples for the article "Java DateTimeFormatter".
 * All dates are fixed so the output is the same on every run.
 */
public class DateTimeFormatterExamples {

  // One formatter, created once, reused everywhere (immutable and thread-safe)
  static final DateTimeFormatter BOOKING_FORMAT =
      DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.US);

  static final DateTimeFormatter LOG_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  public static void main(String[] args) {

    LocalDateTime checkIn = LocalDateTime.of(2026, 10, 5, 14, 30, 45);
    ZonedDateTime checkInNy = checkIn.atZone(ZoneId.of("America/New_York"));

    System.out.println("== 1. Intro: format and parse ==");
    String text = BOOKING_FORMAT.format(checkIn);
    System.out.println(text);
    LocalDateTime back = LocalDateTime.parse("05 Oct 2026, 02:30 PM", BOOKING_FORMAT);
    System.out.println(back);
    try {
      LocalDate bad = LocalDate.parse("Oct 5", BOOKING_FORMAT);
      System.out.println(bad);
    } catch (DateTimeParseException e) {
      System.out.println("DateTimeParseException: " + e.getMessage());
    }

    System.out.println("== 2. Pattern letters ==");
    print("yyyy-MM-dd", checkIn);
    print("dd/MM/yy", checkIn);
    print("d MMM yyyy", checkIn);
    print("MMMM d, yyyy", checkIn);
    print("EEE, d MMM", checkIn);
    print("EEEE", checkIn);
    print("HH:mm:ss", checkIn);
    print("hh:mm a", checkIn);
    print("HH:mm:ss.SSS", checkIn);
    print("yyyy-MM-dd'T'HH:mm", checkIn);
    print("'Check-in on' EEEE 'at' h a", checkIn);

    System.out.println("== 3. Predefined constants ==");
    System.out.println(DateTimeFormatter.ISO_LOCAL_DATE.format(checkIn));
    System.out.println(DateTimeFormatter.ISO_LOCAL_TIME.format(checkIn));
    System.out.println(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(checkIn));
    System.out.println(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(checkInNy));
    System.out.println(DateTimeFormatter.ISO_ZONED_DATE_TIME.format(checkInNy));
    System.out.println(DateTimeFormatter.ISO_DATE_TIME.format(checkIn));
    System.out.println(DateTimeFormatter.ISO_DATE_TIME.format(checkInNy));
    System.out.println(DateTimeFormatter.ISO_INSTANT.format(checkInNy));
    System.out.println(DateTimeFormatter.BASIC_ISO_DATE.format(checkIn));
    System.out.println(DateTimeFormatter.RFC_1123_DATE_TIME.format(checkInNy));
    System.out.println(DateTimeFormatter.ISO_DATE.format(checkIn));
    System.out.println(DateTimeFormatter.ISO_DATE.format(checkInNy));
    // toString() of the date classes uses the ISO formatters
    System.out.println(checkIn.toString());
    System.out.println(LocalDate.parse("2026-10-05"));

    System.out.println("== 4. Localized formats ==");
    LocalDate date = checkIn.toLocalDate();
    for (FormatStyle style : FormatStyle.values()) {
      DateTimeFormatter us = DateTimeFormatter.ofLocalizedDate(style).withLocale(Locale.US);
      DateTimeFormatter de = DateTimeFormatter.ofLocalizedDate(style).withLocale(Locale.GERMANY);
      System.out.println(style + " | " + us.format(date) + " | " + de.format(date));
    }
    DateTimeFormatter mediumTime = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).withLocale(Locale.US);
    System.out.println(mediumTime.format(checkIn));
    DateTimeFormatter mixed = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT).withLocale(Locale.US);
    System.out.println(mixed.format(checkIn));
    DateTimeFormatter fullDateTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(Locale.US);
    System.out.println(fullDateTime.format(checkInNy));
    try {
      String noZone = fullDateTime.format(checkIn);
      System.out.println(noZone);
    } catch (Exception e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
    // Locale changes month and day names in ofPattern too
    DateTimeFormatter frenchFormat = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.FRANCE);
    System.out.println(frenchFormat.format(checkIn));
    DateTimeFormatter localizedPattern = DateTimeFormatter.ofLocalizedPattern("yMMMd").withLocale(Locale.US);
    System.out.println(localizedPattern.format(date));
    System.out.println(DateTimeFormatter.ofLocalizedPattern("yMMMd").withLocale(Locale.GERMANY).format(date));

    System.out.println("== 5. Parsing and DateTimeParseException ==");
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate parsed = LocalDate.parse("05/10/2026", dateFormat);
    System.out.println(parsed);
    try {
      LocalDate bad = LocalDate.parse("2026-10-05", dateFormat);
      System.out.println(bad);
    } catch (DateTimeParseException e) {
      System.out.println("DateTimeParseException: " + e.getMessage());
      System.out.println("parsedString=" + e.getParsedString() + " errorIndex=" + e.getErrorIndex());
    }
    try {
      LocalDateTime noTime = LocalDateTime.parse("05/10/2026", dateFormat);
      System.out.println(noTime);
    } catch (DateTimeParseException e) {
      System.out.println("DateTimeParseException: " + e.getMessage());
    }
    LocalDateTime withTime = LocalDate.parse("05/10/2026", dateFormat).atStartOfDay();
    System.out.println(withTime);
    System.out.println(parseDate("05/10/2026"));
    System.out.println(parseDate("Oct 5"));

    // SMART (default) vs STRICT resolver style
    LocalDate smart = LocalDate.parse("31/02/2026", dateFormat);
    System.out.println("smart: " + smart);
    DateTimeFormatter strictFormat = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    try {
      LocalDate strict = LocalDate.parse("31/02/2026", strictFormat);
      System.out.println(strict);
    } catch (DateTimeParseException e) {
      System.out.println("strict: DateTimeParseException: " + e.getMessage());
    }
    try {
      DateTimeFormatter strictY = DateTimeFormatter.ofPattern("dd/MM/yyyy").withResolverStyle(ResolverStyle.STRICT);
      LocalDate strictWithY = LocalDate.parse("05/10/2026", strictY);
      System.out.println(strictWithY);
    } catch (DateTimeParseException e) {
      System.out.println("strict yyyy: DateTimeParseException: " + e.getMessage());
    }

    System.out.println("== 6. Time zone and offset letters ==");
    print("z", checkInNy);
    print("zzzz", checkInNy);
    print("Z", checkInNy);
    print("xxx", checkInNy);
    print("X", checkInNy);
    print("XXX", checkInNy);
    print("O", checkInNy);
    print("VV", checkInNy);
    print("yyyy-MM-dd'T'HH:mm:ssXXX", checkInNy);
    print("yyyy-MM-dd HH:mm z", checkInNy);
    ZonedDateTime utc = checkInNy.withZoneSameInstant(ZoneId.of("UTC"));
    print("yyyy-MM-dd'T'HH:mm:ssX", utc);
    print("yyyy-MM-dd'T'HH:mm:ssZ", utc);
    try {
      String noZoneText = DateTimeFormatter.ofPattern("HH:mm z").format(checkIn);
      System.out.println(noZoneText);
    } catch (Exception e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
    DateTimeFormatter withZone = DateTimeFormatter.ofPattern("HH:mm z").withZone(ZoneId.of("Asia/Kolkata"));
    System.out.println(withZone.format(checkInNy));
    System.out.println(withZone.format(checkInNy.toInstant()));
    // a LocalDateTime keeps its fields, the zone is only added
    System.out.println(withZone.format(checkIn));
    ZonedDateTime parsedZoned = ZonedDateTime.parse("2026-10-05 14:30 America/New_York",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm VV"));
    System.out.println(parsedZoned);

    System.out.println("== 7. Common mistakes ==");
    LocalDate newYearsEve = LocalDate.of(2024, 12, 31);
    print("yyyy-MM-dd", newYearsEve);
    print("YYYY-MM-dd", newYearsEve);
    print("uuuu-MM-dd", newYearsEve);
    LocalTime afternoon = LocalTime.of(14, 5);
    print("HH:mm", afternoon);
    print("hh:mm", afternoon);
    print("hh:mm a", afternoon);
    print("mm/dd/yyyy", checkIn);
    print("MM/dd/yyyy", checkIn);
    print("yyyy-MM-dd HH:mm:ss", checkIn);
    print("yyyy-MM-dd hh:mm:ss", checkIn);
    try {
      LocalDate parsedUtc = LocalDate.parse("2026-10-05 UTC", DateTimeFormatter.ofPattern("yyyy-MM-dd UTC"));
      System.out.println(parsedUtc);
    } catch (IllegalArgumentException e) {
      System.out.println("IllegalArgumentException: " + e.getMessage());
    }
    print("yyyy-MM-dd 'UTC'", checkIn);
    try {
      String dateWithHour = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(newYearsEve);
      System.out.println(dateWithHour);
    } catch (Exception e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
    // week-based year in the default locale too (no Locale.US)
    System.out.println(DateTimeFormatter.ofPattern("YYYY-MM-dd").format(newYearsEve));

    System.out.println("== 8. Shared instance ==");
    String line1 = LOG_FORMAT.format(LocalDateTime.of(2026, 10, 5, 14, 30, 45));
    System.out.println(line1);
  }

  static Optional<LocalDate> parseDate(String text) {
    try {
      return Optional.of(LocalDate.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    } catch (DateTimeParseException e) {
      return Optional.empty();
    }
  }

  private static void print(String pattern, java.time.temporal.TemporalAccessor value) {
    String out = DateTimeFormatter.ofPattern(pattern, Locale.US).format(value);
    System.out.printf("%-32s -> %s%n", pattern, out);
  }
}
