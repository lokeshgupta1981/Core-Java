package com.howtodoinjava.core.datetime;

import java.util.TimeZone;

public class SetJvmTimezone {
  public static void main(String[] args) {

    /*//Setting UTC time zone - 1
    System.setProperty("user.timezone", "UTC");


    //Setting UTC time zone - 2
    TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
    TimeZone.setDefault(utcTimeZone);*/

    //Verify
    TimeZone timeZone = TimeZone.getDefault();
    System.out.printf("DisplayName = %s, ID = %s, offset = %s",
        timeZone.getDisplayName(),timeZone.getID(),timeZone.getRawOffset());
  }
}
