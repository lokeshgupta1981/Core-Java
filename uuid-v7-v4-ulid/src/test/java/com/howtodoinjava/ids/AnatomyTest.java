package com.howtodoinjava.ids;

import java.util.UUID;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import com.github.f4b6a3.uuid.UuidCreator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnatomyTest {

  @Test
  void whenCreatingV4AndV7_thenVersionAndVariantFollowRfc9562() {
    UUID v4 = UUID.randomUUID();
    UUID v7 = UuidCreator.getTimeOrderedEpoch();

    assertThat(v4.version()).isEqualTo(4);
    assertThat(v7.version()).isEqualTo(7);
    assertThat(v4.variant()).isEqualTo(2);
    assertThat(v7.variant()).isEqualTo(2);
  }

  @Test
  void whenCreatingHandWrittenV7_thenVersionIs7AndVariantIs2() {
    UUID v7 = UuidV7.next();

    assertThat(v7.version()).isEqualTo(7);
    assertThat(v7.variant()).isEqualTo(2);
  }

  @Test
  void whenPrintingIds_thenUuidHas36AndUlidHas26Characters() {
    Ulid orderId = UlidCreator.getUlid();

    assertThat(UUID.randomUUID().toString()).hasSize(36);
    assertThat(orderId.toString())
        .hasSize(26)
        .matches("[0-9A-HJKMNP-TV-Z]{26}");
    assertThat(Ulid.from(orderId.toLowerCase())).isEqualTo(orderId);
  }
}
