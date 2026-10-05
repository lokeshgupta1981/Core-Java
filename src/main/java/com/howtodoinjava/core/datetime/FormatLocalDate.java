package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

/**
 * Formats a LocalDate to String with toString(), the ISO constants, custom
 * patterns and localized styles, and parses the String back.
 */
public class FormatLocalDate {

  public static void main(String[] args) {

    LocalDate date = LocalDate.of(2026, 10, 5);

    // 1. Quick reference
    System.out.println("--- 1. Quick reference ---");
    String iso = date.toString();
    System.out.println("toString()              : " + iso);

    DateTimeFormatter dmy = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    String custom = date.format(dmy);
    System.out.println("dd-MM-yyyy              : " + custom);

    DateTimeFormatter longStyle = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.US);
    String localized = date.format(longStyle);
    System.out.println("LONG, Locale.US         : " + localized);

    // 2. Default format and the ISO constants
    System.out.println("--- 2. ISO constants ---");
    String isoLocal = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    System.out.println("ISO_LOCAL_DATE          : " + isoLocal);
    String isoDate = date.format(DateTimeFormatter.ISO_DATE);
    System.out.println("ISO_DATE                : " + isoDate);
    String basic = date.format(DateTimeFormatter.BASIC_ISO_DATE);
    System.out.println("BASIC_ISO_DATE          : " + basic);
    String ordinal = date.format(DateTimeFormatter.ISO_ORDINAL_DATE);
    System.out.println("ISO_ORDINAL_DATE        : " + ordinal);
    String week = date.format(DateTimeFormatter.ISO_WEEK_DATE);
    System.out.println("ISO_WEEK_DATE           : " + week);
    String sameAsToString = DateTimeFormatter.ISO_LOCAL_DATE.format(date);
    System.out.println("formatter.format(date)  : " + sameAsToString);

    // 3. Custom patterns
    System.out.println("--- 3. Custom patterns ---");
    String slash = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    System.out.println("MM/dd/yyyy              : " + slash);
    String monthName = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    System.out.println("dd MMM yyyy             : " + monthName);
    String full = date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
    System.out.println("EEEE, MMMM d, yyyy      : " + full);
    String noSeparator = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    System.out.println("yyyyMMdd                : " + noSeparator);
    String literal = date.format(DateTimeFormatter.ofPattern("'Week' w 'of' yyyy"));
    System.out.println("'Week' w 'of' yyyy      : " + literal);
    String withLocale = date.format(
        DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.GERMANY));
    System.out.println("Locale.GERMANY          : " + withLocale);

    // 4. Localized styles
    System.out.println("--- 4. FormatStyle ---");
    for (FormatStyle style : FormatStyle.values()) {
      String us = date.format(
          DateTimeFormatter.ofLocalizedDate(style).withLocale(Locale.US));
      String uk = date.format(
          DateTimeFormatter.ofLocalizedDate(style).withLocale(Locale.UK));
      String fr = date.format(
          DateTimeFormatter.ofLocalizedDate(style).withLocale(Locale.FRANCE));
      System.out.printf("%-6s US: %-26s UK: %-26s FR: %s%n", style, us, uk, fr);
    }

    // 5. LocalDateTime and ZonedDateTime
    System.out.println("--- 5. LocalDateTime and ZonedDateTime ---");
    LocalDateTime dateTime = LocalDateTime.of(2026, 10, 5, 14, 30, 0);
    String dt = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    System.out.println("LocalDateTime           : " + dt);
    String dtDefault = dateTime.toString();
    System.out.println("LocalDateTime.toString(): " + dtDefault);

    ZonedDateTime zoned = dateTime.atZone(ZoneId.of("Asia/Kolkata"));
    String zdt = zoned.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm z"));
    System.out.println("ZonedDateTime           : " + zdt);
    String zdtOffset = zoned.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmXXX"));
    System.out.println("ZonedDateTime offset    : " + zdtOffset);

    try {
      String bad = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
      System.out.println(bad);
    } catch (UnsupportedTemporalTypeException e) {
      System.out.println("LocalDate with HH:mm    : " + e.getMessage());
    }
    String fixed = date.atStartOfDay()
        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    System.out.println("atStartOfDay() first    : " + fixed);

    // 6. String back to LocalDate
    System.out.println("--- 6. parse() ---");
    LocalDate parsedIso = LocalDate.parse("2026-10-05");
    System.out.println("parse ISO               : " + parsedIso);
    LocalDate parsedCustom = LocalDate.parse("05-10-2026", dmy);
    System.out.println("parse dd-MM-yyyy        : " + parsedCustom);
    Optional<LocalDate> safe = parseSafely("2026/10/05", dmy);
    System.out.println("parse wrong text        : " + safe);

    // 7. YYYY vs yyyy
    System.out.println("--- 7. YYYY vs yyyy ---");
    LocalDate newYearsEve = LocalDate.of(2025, 12, 31);
    String right = newYearsEve.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    System.out.println("dd-MM-yyyy              : " + right);
    String wrong = newYearsEve.format(
        DateTimeFormatter.ofPattern("dd-MM-YYYY", Locale.US));
    System.out.println("dd-MM-YYYY              : " + wrong);

    // 8. Legacy java.util.Date
    System.out.println("--- 8. java.util.Date ---");
    Date legacyDate = Date.from(
        date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    LocalDate fromLegacy = legacyDate.toInstant()
        .atZone(ZoneId.systemDefault()).toLocalDate();
    String legacyFormatted = fromLegacy.format(dmy);
    System.out.println("Date -> LocalDate       : " + legacyFormatted);
  }

  static Optional<LocalDate> parseSafely(String text, DateTimeFormatter formatter) {
    try {
      return Optional.of(LocalDate.parse(text, formatter));
    } catch (DateTimeParseException e) {
      return Optional.empty();
    }
  }
}
