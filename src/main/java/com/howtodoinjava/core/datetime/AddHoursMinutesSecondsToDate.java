package com.howtodoinjava.core.datetime;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class AddHoursMinutesSecondsToDate {
  public static void main(final String[] args) {
    
    //1 - Same methodss for ZonedDateTime and OffsetDateTime
    LocalDateTime now = LocalDateTime.now();
    System.out.println("Current Date and Time = " + now);
    
    LocalDateTime updatedTime = now.plusHours(2);
    System.out.println("Updated Date and Time = " + updatedTime);
    
    updatedTime = now.plusMinutes(20);
    System.out.println("Updated Date and Time = " + updatedTime);
    
    updatedTime = now.plus(Duration.ofMillis(8000));
    System.out.println("Updated Date and Time = " + updatedTime);
    
    updatedTime = now.plus(20, ChronoUnit.HOURS);
    System.out.println("Updated Date and Time = " + updatedTime);
    
    updatedTime = now.minusHours(2);
    updatedTime = now.minusMinutes(20);
    updatedTime = now.minusSeconds(300);
    updatedTime = now.minus(Duration.ofMillis(8000));
    updatedTime = now.minus(20, ChronoUnit.HOURS);
    
    Instant currentInstant = Instant.parse("2022-06-24T05:12:35Z");
    System.out.println("Current Date and Time = " + currentInstant);
    
    Instant updatedInstant = currentInstant.plus(2, ChronoUnit.HOURS);
    System.out.println("Updated Date and Time = " + updatedInstant);
    
    updatedInstant = currentInstant.plusSeconds(300);
    updatedInstant = currentInstant.plusMillis(8000);
    updatedInstant = currentInstant.plusNanos(600000);
    
    updatedInstant = currentInstant.plusSeconds(300);
    System.out.println("Updated Date and Time = " + updatedInstant);
    
    //2 java.util.Date
    Date date = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    System.out.println("Current Date and Time = " + cal.getTime());
    
    cal.add(Calendar.HOUR, 2);
    System.out.println("Updated Date and Time = " + cal.getTime());
    
    cal.add(Calendar.MINUTE, -15);
    System.out.println("Updated Date and Time = " + cal.getTime());
    
    cal.add(Calendar.SECOND, 10);
    System.out.println("Updated Date and Time = " + cal.getTime());
  }
}
