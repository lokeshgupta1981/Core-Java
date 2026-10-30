package com.howtodoinjava.ids;

import java.util.UUID;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import com.github.f4b6a3.uuid.UuidCreator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenerationTest {

  @Test
  void whenUsingRandomUuid_thenVersion4() {
    UUID orderId = UUID.randomUUID();

    assertThat(orderId.version()).isEqualTo(4);
  }

  @Test
  void whenUsingTimeOrderedEpoch_thenVersion7() {
    UUID orderId = UuidCreator.getTimeOrderedEpoch();

    assertThat(orderId.version()).isEqualTo(7);
  }

  @Test
  void whenUsingMonotonicUlid_thenTextAndUuidFormsMatch() {
    Ulid orderId = UlidCreator.getMonotonicUlid();

    String text = orderId.toString();
    UUID forDatabase = orderId.toUuid();

    assertThat(text).hasSize(26);
    assertThat(Ulid.from(forDatabase)).isEqualTo(orderId);
  }
}
