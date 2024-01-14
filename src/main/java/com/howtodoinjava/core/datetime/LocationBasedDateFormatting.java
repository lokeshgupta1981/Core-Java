package com.howtodoinjava.core.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class LocationBasedDateFormatting {

  public static void main(final String[] args) {

    Locale locale = Locale.forLanguageTag("es");

    DateTimeFormatter formatter = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.MEDIUM)
        .withLocale(locale);

    String formattedDate = formatter.format(ZonedDateTime.now());
    System.out.println(formattedDate);  // 14 ene 2024, 8:21:54

    DateTimeFormatter chFormatter = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.MEDIUM)
        .withLocale(Locale.forLanguageTag("zh"));

    formattedDate = chFormatter.format(ZonedDateTime.now());
    System.out.println(formattedDate);  // 2024年1月14日 08:21:54

    Locale.setDefault(Locale.GERMANY);

    String ld = LocalDate.now().format(
        DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
    System.out.println(ld);  // 14. Januar 2024

    String lt = LocalTime.now().format(
        DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
    System.out.println(lt);  // 08:14

    String ldt = LocalDateTime.now().format(
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
    System.out.println(ldt);  // 14.01.2024, 08:14:25

    // ofLocalizedPattern() examples

    Locale.setDefault(Locale.ENGLISH);

    String ld1 = LocalDate.now().format(
        DateTimeFormatter.ofLocalizedPattern("yMM"));
    System.out.println(ld1);  // 01.2024

    String ld2 = LocalDate.now().format(
        DateTimeFormatter.ofLocalizedPattern("yMMM"));
    System.out.println(ld2);  // 01.2024
  }
}
