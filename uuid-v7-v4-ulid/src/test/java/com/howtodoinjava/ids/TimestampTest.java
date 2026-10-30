package com.howtodoinjava.ids;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import com.github.f4b6a3.uuid.UuidCreator;
import com.github.f4b6a3.uuid.util.UuidUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimestampTest {

  @Test
  void givenV7_whenReadingFirst48Bits_thenCreationTimeIsRevealed() {
    Instant before = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    UUID orderId = UuidCreator.getTimeOrderedEpoch();
    Instant after = Instant.now();

    Instant createdAt = Instant.ofEpochMilli(orderId.getMostSignificantBits() >>> 16);

    assertThat(createdAt).isBetween(before, after);
    assertThat(UuidUtil.getInstant(orderId)).isEqualTo(createdAt);
  }

  @Test
  void givenUlid_whenGettingInstant_thenCreationTimeIsRevealed() {
    Instant before = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    Ulid orderId = UlidCreator.getUlid();
    Instant after = Instant.now();

    assertThat(orderId.getInstant()).isBetween(before, after);
  }

  @Test
  void givenV4_whenAskingForTimestamp_thenUnsupportedOperationException() {
    UUID orderId = UUID.randomUUID();

    assertThatThrownBy(orderId::timestamp)
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Not a time-based UUID");
  }

  @Test
  void givenV7_whenCallingJdkTimestamp_thenUnsupportedOperationException() {
    UUID orderId = UuidCreator.getTimeOrderedEpoch();

    assertThatThrownBy(orderId::timestamp)
        .isInstanceOf(UnsupportedOperationException.class);
  }
}
