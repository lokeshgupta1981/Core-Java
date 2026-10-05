package com.howtodoinjava.java26;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpClient;
import java.util.Comparator;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class QuickLookTest {

  @Test
  void finalApisFromTheIntro() {
    try (HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_3).build()) {
      assertThat(client.version()).isEqualTo(HttpClient.Version.HTTP_3);
    }
    UUID first = UUID.ofEpochMillis(1_000L);
    UUID second = UUID.ofEpochMillis(2_000L);
    assertThat(first.version()).isEqualTo(7);
    assertThat(first).isLessThan(second);
    assertThat(Comparator.<String>naturalOrder().max("apple", "banana")).isEqualTo("banana");
  }
}
