package com.howtodoinjava.ids;

import java.util.UUID;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConversionTest {

  @Test
  void givenUlid_whenConvertingToUuidAndBack_thenSameValue() {
    Ulid orderId = UlidCreator.getUlid();

    UUID asUuid = orderId.toUuid();

    assertThat(Ulid.from(asUuid)).isEqualTo(orderId);
    assertThat(asUuid.getMostSignificantBits()).isEqualTo(orderId.getMostSignificantBits());
  }

  @Test
  void givenUlidStoredAsUuid_whenCheckingVersion_thenItIsNotAVersion7Uuid() {
    Ulid orderId = new Ulid(1_767_225_600_000L, new byte[10]);

    UUID asUuid = orderId.toUuid();

    assertThat(asUuid.version()).isZero();
    assertThat(orderId.toRfc4122().toUuid().version()).isEqualTo(4);
    assertThat(Ulid.from(asUuid).getTime()).isEqualTo(1_767_225_600_000L);
  }

  @Test
  void givenV7Uuid_whenWrappedAsUlid_thenTimestampIsPreserved() {
    UUID orderId = UuidV7.of(1_767_225_600_000L);

    Ulid asUlid = Ulid.from(orderId);

    assertThat(asUlid.getTime()).isEqualTo(1_767_225_600_000L);
    assertThat(asUlid.toString()).hasSize(26);
  }
}
