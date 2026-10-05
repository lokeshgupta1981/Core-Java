package com.howtodoinjava.java25.runtime;

import java.util.ArrayList;
import java.util.List;

/** Target for JFR method timing (JEP 520) and CPU-time sampling (JEP 509). */
public class CatalogSearch {

  private final List<String> titles = new ArrayList<>();

  public CatalogSearch() {
    for (int i = 0; i < 20_000; i++) {
      titles.add("Book-" + i);
    }
  }

  public int search(String prefix) {
    int hits = 0;
    for (String t : titles) {
      if (t.startsWith(prefix)) {
        hits++;
      }
    }
    return hits;
  }

  public static void main(String[] args) {
    CatalogSearch catalog = new CatalogSearch();
    long total = 0;
    for (int i = 0; i < 2_000; i++) {
      total += catalog.search("Book-1");
    }
    System.out.println("Hits: " + total);
  }
}
