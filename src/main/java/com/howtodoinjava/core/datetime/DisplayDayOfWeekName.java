package com.howtodoinjava.core.datetime;

import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;
import java.util.stream.IntStream;

public class DisplayDayOfWeekName {

  public static void main(String[] args) {
    //1
    String[] weekdays = DateFormatSymbols.getInstance().getWeekdays();
    IntStream.range(0, weekdays.length)
        .filter(t -> !weekdays[t].isBlank())
        .mapToObj(t -> String.format("Day: %d -> %s",
            t, weekdays[t]))
        .forEach(System.out::println);

    Calendar calendar = Calendar.getInstance();
    int dayOfWeekValue = calendar.get(Calendar.DAY_OF_WEEK);
    System.out.println(weekdays[dayOfWeekValue]);

    //2
    DayOfWeek[] days = DayOfWeek.values();
    IntStream.range(0, days.length)
        .mapToObj(t -> String.format("Day: %d -> %s",
            t, days[t]))
        .forEach(System.out::println);

    //3
    LocalDate localDate = LocalDate.now();
    DayOfWeek weekOfTheDay = DayOfWeek.from(localDate);
    System.out.println(weekOfTheDay.getValue());
    System.out.println(weekOfTheDay.getDisplayName(TextStyle.FULL, Locale.getDefault()));
    System.out.println(weekOfTheDay.getDisplayName(TextStyle.FULL, Locale.FRENCH));
  }

}
