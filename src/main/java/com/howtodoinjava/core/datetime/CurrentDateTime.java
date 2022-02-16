package com.howtodoinjava.core.datetime;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class CurrentDateTime {
  public static void main(final String[] args) {
    // Java 8 or later

    // Get current date
    LocalDate today = LocalDate.now();
    System.out.println(today);

    // Get current time
    LocalTime now = LocalTime.now();
    System.out.println(now);

    // Get current date and time
    LocalDateTime instance = LocalDateTime.now();
    System.out.println(instance);

    // Get current date and time in GMT
    ZonedDateTime tzInstance = ZonedDateTime.now(ZoneId.of("GMT"));
    System.out.println(tzInstance);

    DateTimeFormatter formatter = DateTimeFormatter
      .ofPattern("dd-MM-yyyy hh:mm");
    
    String formattedString = formatter.format(instance); //15-02-2022 12:43
    System.out.println(formattedString);

    // Java 7 or earlier

    Date date = new Date();
    System.out.println(date);   //Tue Feb 15 12:43:51 IST 2022

    Calendar cal = Calendar.getInstance();
    date = new Date(cal.getTimeInMillis());
    System.out.println(date); //

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");

    System.out.println(sdf.format(date));  //15-02-2022 01:04
    System.out.println(sdf.format(cal.getTime()));  //15-02-2022 01:04
  }
}
