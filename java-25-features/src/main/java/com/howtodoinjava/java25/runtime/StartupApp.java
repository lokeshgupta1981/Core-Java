package com.howtodoinjava.java25.runtime;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** JEP 514/515: a small app that loads many JDK classes at startup. */
public class StartupApp {

  public static void main(String[] args) {
    long start = System.nanoTime();
    Logger log = Logger.getLogger("library");
    Pattern isbn = Pattern.compile("\\d{3}-\\d{10}");
    Map<Boolean, List<String>> valid = List.of("978-0441172719", "abc", "978-0141439587").stream()
        .collect(Collectors.partitioningBy(s -> isbn.matcher(s).matches()));
    String today = LocalDate.of(2025, 9, 16).format(DateTimeFormatter.ISO_DATE);
    log.fine("started");
    System.out.println("Valid ISBNs: " + valid.get(true).size() + ", Java 25 GA: " + today
        + ", main() took " + (System.nanoTime() - start) / 1_000_000 + " ms");
  }
}
