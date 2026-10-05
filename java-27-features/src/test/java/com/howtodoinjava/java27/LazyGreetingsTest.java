package com.howtodoinjava.java27;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LazyGreetingsTest {

  @Test
  void greetingIsComputedOnceOnFirstGet() {
    int before = LazyGreetings.LOADS.get();
    var app = new LazyGreetings();
    assertThat(LazyGreetings.LOADS.get()).isEqualTo(before);

    assertThat(app.greeting()).isEqualTo("Hello");
    assertThat(app.greeting()).isEqualTo("Hello");
    assertThat(LazyGreetings.LOADS.get()).isEqualTo(before + 1);
  }

  @Test
  void lazySetKeepsOnlyEnabledCandidates() {
    var app = new LazyGreetings();
    assertThat(app.enabledFlags().contains("dark-mode")).isTrue();
    assertThat(app.enabledFlags().contains("beta")).isFalse();
    assertThat(app.enabledFlags().contains("unknown")).isFalse();
    assertThat(app.enabledFlags()).containsExactly("dark-mode");
  }
}
