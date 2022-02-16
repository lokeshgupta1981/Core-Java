package com.howtodoinjava.core.datetime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CompareZonedDateTimes {
  public static void main(final String[] args) {
    ZonedDateTime zdtNow = ZonedDateTime.now();
    System.out.println("Time in IST" + zdtNow);

    ZonedDateTime zdtNowinUTC = zdtNow.withZoneSameInstant(ZoneId.of("UTC"));
    System.out.println("Time in UTC" + zdtNowinUTC);

    if (zdtNow.toEpochSecond() == zdtNowinUTC.toEpochSecond()) { // true
      System.out.println("Epoch Seconds are equal");
    }

    System.out.println(zdtNow.isEqual(zdtNowinUTC)); // true
    System.out.println(zdtNow.isBefore(zdtNowinUTC)); // false
    System.out.println(zdtNow.isAfter(zdtNowinUTC)); // false

    System.out.println(zdtNow.compareTo(zdtNowinUTC)); // 1 - Not equal
    System.out.println(zdtNow.equals(zdtNowinUTC)); // false

    ZonedDateTime zonedDTDuringOverlap = ZonedDateTime
      .of(LocalDateTime.of(2021, 11, 07, 1, 05, 53), ZoneId.of("US/Central"));

    ZonedDateTime zonedDTAfterOverlap = ZonedDateTime
      .of(LocalDateTime.of(2021, 11, 07, 2, 05, 53), ZoneId.of("US/Central"));

    System.out.println("During overlap: " + zonedDTDuringOverlap);

    System.out.println("After overlap(): " + zonedDTAfterOverlap);

    ZonedDateTime zonedDT = zonedDTDuringOverlap.withLaterOffsetAtOverlap();

    System.out.println("Using withLaterOffsetAtOverlap(): " + zonedDT);
  }
}
