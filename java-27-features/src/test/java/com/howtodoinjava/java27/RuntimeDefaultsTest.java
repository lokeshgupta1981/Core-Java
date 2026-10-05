package com.howtodoinjava.java27;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RuntimeDefaultsTest {

  @Test
  void g1IsTheDefaultCollector() {
    assertThat(RuntimeDefaults.garbageCollector()).isEqualTo("G1 Young Generation");
  }

  @Test
  void g1IsSelectedEvenWithOneCpu() throws Exception {
    // JDK 26 and older select Serial GC for a single CPU
    String output = ChildJvm.run("-XX:ActiveProcessorCount=1", "-Xlog:gc", "-version");
    assertThat(output).contains("Using G1");
  }

  @Test
  void compactObjectHeadersAreOnByDefault() {
    assertThat(RuntimeDefaults.compactObjectHeaders()).isTrue();
  }

  @Test
  void compactObjectHeadersCanBeTurnedOff() throws Exception {
    String output = ChildJvm.run("-XX:-UseCompactObjectHeaders",
        "com.howtodoinjava.java27.RuntimeDefaults");
    assertThat(output).contains("Compact object headers: false");
  }

  @Test
  void hybridPostQuantumGroupComesFirst() throws Exception {
    assertThat(RuntimeDefaults.tlsNamedGroups())
        .startsWith("X25519MLKEM768", "x25519")
        .doesNotContain("ffdhe6144", "ffdhe8192");
  }
}
