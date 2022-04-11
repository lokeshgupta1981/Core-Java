package com.howtodoinjava.cleaner;

import java.lang.ref.Cleaner;

public class AppCleanerProvider {
  private static final Cleaner CLEANER = Cleaner.create();    
  
  public static Cleaner getCleaner() {
    return CLEANER;
  }
}
