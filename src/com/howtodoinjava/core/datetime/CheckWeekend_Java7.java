package com.howtodoinjava.core.datetime;

import java.util.Calendar;
import java.util.Date;

public class CheckWeekend_Java7 {
    public static void main(final String[] args) {
        Date today = new Date();
        System.out.println("Is weekend : " + isWeekend(today));
        
        @SuppressWarnings("deprecation")
        Date someDate = new Date(2021,0,2);
        System.out.println("Is weekend : " + isWeekend(someDate));
    }

    public static boolean isWeekend(final Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }
}
