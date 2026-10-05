package com.howtodoinjava.java27;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StatusTextTest {

  @Test
  void switchOnPrimitiveWithGuards() {
    assertThat(StatusText.describe(200)).isEqualTo("OK");
    assertThat(StatusText.describe(503)).isEqualTo("server error 503");
    assertThat(StatusText.describe(404)).isEqualTo("client error 404");
  }

  @Test
  void instanceofChecksSafeConversion() {
    assertThat(StatusText.asByte(100)).isEqualTo("byte 100");
    assertThat(StatusText.asByte(300)).isEqualTo("does not fit in a byte");
  }
}
