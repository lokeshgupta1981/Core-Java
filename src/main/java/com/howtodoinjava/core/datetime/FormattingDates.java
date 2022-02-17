package com.howtodoinjava.core.datetime;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class FormattingDates {

  public static final String ZDT_PATTERN = "yyyy-MM-dd HH:mm:ss a z";
  public static final DateTimeFormatter ZDT_FORMATTER
      = DateTimeFormatter.ofPattern(ZDT_PATTERN);

  public static final String LDT_PATTERN = "yyyy-MM-dd HH:mm:ss a";
  public static final DateTimeFormatter LDT_FORMATTER
      = DateTimeFormatter.ofPattern(LDT_PATTERN);

  public static final String LD_PATTERN = "yyyy-MM-dd";
  public static final DateTimeFormatter LD_FORMATTER
      = DateTimeFormatter.ofPattern(LD_PATTERN);

  public static void main(String[] args) {

    //1 - DateTimeFormatter

    String instanceString = ZDT_FORMATTER.format(ZonedDateTime.now());
    System.out.println(instanceString);

    String dateTimeString = LDT_FORMATTER.format(LocalDateTime.now());
    System.out.println(dateTimeString);

    String dateString = LD_FORMATTER.format(LocalDate.now());
    System.out.println(dateString);

    // 2 - SimpleDateFormat

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
    String formattedDate = sdf.format(new Date());
    System.out.println(formattedDate);

    String pattern = "EEEEE MMMMM yyyy HH:mm:ss.SSSZ";
    SimpleDateFormat sdfWithLocale =new SimpleDateFormat(pattern,
        new Locale("fr", "FR"));
    String date = sdfWithLocale.format(new Date());
    System.out.println(date);

    DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
    symbols.setAmPmStrings(new String[] { "AM", "PM" });
    sdf.setDateFormatSymbols(symbols);
    formattedDate = sdf.format(new Date());
    System.out.println(formattedDate);

  }
}
