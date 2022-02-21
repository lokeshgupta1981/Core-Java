package com.howtodoinjava.core.datetime;

import java.time.Duration;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class FormatMilliseconds {
  public static void main(final String[] args) {
    
    long millis = 54321000;
    
    //Java 8
    Duration duration = Duration.ofMillis(millis);
        
    long h = duration.toHours();
    long m = duration.toMinutes() % 60;
    long s = duration.getSeconds() % 60;
    
    String timeInHms = String.format("%02d:%02d:%02d", h, m, s);
    System.out.println(timeInHms);
    
    //Apache Commons
    timeInHms = DurationFormatUtils.formatDuration(millis, "HH:mm:ss", true);
    System.out.println(timeInHms);
  }
}


