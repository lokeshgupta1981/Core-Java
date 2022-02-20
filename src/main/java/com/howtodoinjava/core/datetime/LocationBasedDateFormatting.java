package com.howtodoinjava.core.datetime;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class LocationBasedDateFormatting {
  public static void main(final String[] args) {

    ZonedDateTime today = ZonedDateTime.now();
    
    DateTimeFormatter format = DateTimeFormatter
       .ofLocalizedDateTime(FormatStyle.MEDIUM)
       .withLocale(Locale.forLanguageTag("es"))
       .withZone(ZoneId.of("UTC"));
       //.withLocale(LocaleContextHolder.getLocale());
    
    String formmatedDate = format.format(today);
    System.out.println(formmatedDate);
  }
}
