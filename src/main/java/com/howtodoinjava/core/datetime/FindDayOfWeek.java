package com.howtodoinjava.core.datetime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FindDayOfWeek {
  public static void main(String[] args) {
    //1
    LocalDate today = LocalDate.now();

    DayOfWeek dayOfWeek = today.getDayOfWeek();

    System.out.println("Day of the Week :: " + dayOfWeek);
    System.out.println("Day of the Week - in Number :: "
        + dayOfWeek.getValue());
    System.out.println("Day of the Week - Formatted FULL :: "
        + dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()));
    System.out.println("Day of the Week - Formatted SHORT :: "
        + dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()));

    //2
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());

    int dayOfWeekNum = cal.get(Calendar.DAY_OF_WEEK);

    DateFormat formatter = new SimpleDateFormat("EEEE");
    String dayOfWeekString = formatter.format(cal.getTime());

    System.out.println("Day of the Week - in Number :: " + dayOfWeekNum);
    System.out.println("Day of the Week - in Text :: " + dayOfWeekString);
  }
}
