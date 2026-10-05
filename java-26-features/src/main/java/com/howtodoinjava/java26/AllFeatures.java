package com.howtodoinjava.java26;

import com.howtodoinjava.java26.api.SmallApis;
import com.howtodoinjava.java26.concurrency.StructuredFetch;
import com.howtodoinjava.java26.finalfields.FinalFieldMutation;
import com.howtodoinjava.java26.lazy.LazyConstants;
import com.howtodoinjava.java26.patterns.PrimitivePatterns;
import com.howtodoinjava.java26.pem.PemRoundTrip;
import com.howtodoinjava.java26.runtime.StartupApp;

/** Prints the output of every example that runs without network access. */
public class AllFeatures {

  public static void main(String[] args) throws Exception {
    section("Quick look (final APIs)");
    QuickLook.main(args);
    section("JEP 500: final field mutation");
    FinalFieldMutation.main(args);
    section("JEP 516: startup app");
    StartupApp.main(args);
    section("JEP 526: lazy constants (preview)");
    LazyConstants.main(args);
    section("JEP 530: primitive patterns (preview)");
    PrimitivePatterns.main(args);
    section("JEP 525: structured concurrency (preview)");
    StructuredFetch.main(args);
    section("JEP 524: PEM encodings (preview)");
    PemRoundTrip.main(args);
    section("Smaller API additions");
    SmallApis.main(args);
  }

  private static void section(String title) {
    System.out.println();
    System.out.println("--- " + title + " ---");
  }
}
