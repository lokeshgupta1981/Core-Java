package com.howtodoinjava.java26;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.Closeable;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class SmallApisTest {

  @Test
  void saturatingAddAndDurationBounds() {
    assertThatThrownBy(() -> Instant.MAX.plus(Duration.ofDays(1))).isInstanceOf(DateTimeException.class);
    assertThat(Instant.MAX.plusSaturating(Duration.ofDays(1))).isEqualTo(Instant.MAX);
    assertThat(Duration.MAX.getSeconds()).isEqualTo(Long.MAX_VALUE);
    assertThat(Closeable.class).isAssignableFrom(Process.class);
  }

  @Test
  void processInTryWithResources() throws Exception {
    try (Process process = new ProcessBuilder("java", "-version").start()) {
      int exitCode = process.waitFor();
      assertThat(exitCode).isZero();
    }
  }
}
