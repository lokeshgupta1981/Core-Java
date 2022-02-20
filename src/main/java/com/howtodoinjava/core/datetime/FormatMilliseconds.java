package com.howtodoinjava.core.datetime;

import java.time.Duration;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class FormatMilliseconds {
  public static void main(final String[] args) {
    Duration duration = Duration.ofMillis(38114000);
    
    //1
    long seconds = duration.getSeconds();
    
    long HH = seconds / 3600;
    long MM = seconds % 3600 / 60;
    long SS = seconds % 60;
    
    String timeInHHMMSS = String.format("%02d:%02d:%02d", HH, MM, SS);
    System.out.println(timeInHHMMSS);
    
    //2
    timeInHHMMSS = DurationFormatUtils.formatDuration(38114000, "HH:MM:SS", true);
    System.out.println(timeInHHMMSS);
    
    //3
    
  }
}


