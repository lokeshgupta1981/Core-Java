package com.howtodoinjava.core.datetime;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateDifference {
  public static void main(final String[] args) {
    // First Date
    Date today = new Date();

    // Second Date
    Date sameDayNextMonth = new Date();
    sameDayNextMonth.setMonth(today.getMonth() + 1);

    long days = getDateDiff(today, sameDayNextMonth, TimeUnit.DAYS); // 31
    long hours = getDateDiff(today, sameDayNextMonth, TimeUnit.HOURS); // 744
    long minutes = getDateDiff(today, sameDayNextMonth, TimeUnit.MINUTES); // 44640
    long seconds = getDateDiff(today, sameDayNextMonth, TimeUnit.SECONDS); // 2678400
    long mills = getDateDiff(today, sameDayNextMonth, TimeUnit.MILLISECONDS); // 2678400000
    
    System.out.println(days);
    System.out.println(hours);
    System.out.println(minutes);
    System.out.println(seconds);
    System.out.println(mills);
  }

  public static long getDateDiff(final Date date1, final Date date2,
    final TimeUnit timeUnit) {
    long diffInMillies = date2.getTime() - date1.getTime();

    return timeUnit.convert(diffInMillies, timeUnit.MILLISECONDS);
  }
}
