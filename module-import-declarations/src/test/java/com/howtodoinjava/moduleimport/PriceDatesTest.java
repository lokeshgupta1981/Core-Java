package com.howtodoinjava.moduleimport;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PriceDatesTest {

  @Test
  void dateComesFromJavaSql() {
    assertThat(PriceDates.priceDate("2026-10-05"))
        .isInstanceOf(java.sql.Date.class)
        .hasToString("2026-10-05");
  }

  @Test
  void loggerComesFromTransitiveJavaLogging() {
    assertThat(PriceDates.logger()).isInstanceOf(java.util.logging.Logger.class);
  }
}
