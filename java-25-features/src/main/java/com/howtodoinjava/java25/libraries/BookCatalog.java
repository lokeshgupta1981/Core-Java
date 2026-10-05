package com.howtodoinjava.java25.libraries;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/** JEP 502 (preview): stable values. */
public class BookCatalog {

  private final AtomicInteger loads = new AtomicInteger();

  // 1. A stable value that we set on first use
  private final StableValue<Map<String, Integer>> pages = StableValue.of();

  // 2. A stable supplier: same idea, less code
  private final Supplier<String> banner = StableValue.supplier(() -> "Library opened");

  // 3. A stable list: each element is computed once, on first access
  private final List<String> shelves = StableValue.list(3, i -> "Shelf-" + (char) ('A' + i));

  public Map<String, Integer> pages() {
    return pages.orElseSet(this::loadPages);
  }

  private Map<String, Integer> loadPages() {
    loads.incrementAndGet();
    return Map.of("Dune", 412, "Emma", 474);
  }

  public int loadCount() {
    return loads.get();
  }

  public boolean isLoaded() {
    return pages.isSet();
  }

  public Supplier<String> banner() {
    return banner;
  }

  public List<String> shelves() {
    return shelves;
  }
}
