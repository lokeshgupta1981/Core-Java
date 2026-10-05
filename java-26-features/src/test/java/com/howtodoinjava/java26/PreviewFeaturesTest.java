package com.howtodoinjava.java26;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.howtodoinjava.java26.concurrency.StructuredFetch;
import com.howtodoinjava.java26.lazy.PriceList;
import com.howtodoinjava.java26.patterns.PrimitivePatterns;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PEMDecoder;
import java.security.PEMEncoder;
import java.security.PublicKey;
import java.util.List;
import org.junit.jupiter.api.Test;

class PreviewFeaturesTest {

  @Test
  void lazyConstantComputesOnce() {
    LazyConstant<PriceList> prices = LazyConstant.of(PriceList::load);
    int loadsBefore = PriceList.loads;
    assertThat(prices.isInitialized()).isFalse();
    assertThat(prices.get().prices().get("apple")).isEqualTo(5);
    assertThat(prices.get().prices().get("banana")).isEqualTo(3);
    assertThat(prices.isInitialized()).isTrue();
    assertThat(PriceList.loads - loadsBefore).isEqualTo(1);

    List<Integer> squares = List.ofLazy(5, i -> i * i);
    assertThat(squares.get(3)).isEqualTo(9);
    assertThatThrownBy(() -> squares.set(0, 1)).isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void lazyConstantRejectsNull() {
    LazyConstant<String> empty = LazyConstant.of(() -> null);
    assertThatThrownBy(empty::get).isInstanceOf(NullPointerException.class);
  }

  @Test
  void primitivePatterns() {
    assertThat(PrimitivePatterns.size(100)).isEqualTo("fits in a byte: 100");
    assertThat(PrimitivePatterns.size(300)).isEqualTo("needs an int: 300");
    assertThat(PrimitivePatterns.stars(4.0)).isEqualTo("4 stars");
    assertThat(PrimitivePatterns.stars(4.5)).isEqualTo("4.5 stars");
  }

  @Test
  void structuredConcurrencyReturnsList() throws InterruptedException {
    assertThat(StructuredFetch.stocks()).containsExactly(5, 6);
  }

  @Test
  void structuredConcurrencyFailsWhenOneSubtaskFails() {
    assertThatThrownBy(() -> {
      try (var scope = java.util.concurrent.StructuredTaskScope.open(
          java.util.concurrent.StructuredTaskScope.Joiner.<Integer>allSuccessfulOrThrow())) {
        scope.fork(() -> 5);
        scope.fork(() -> Integer.parseInt("abc"));
        scope.join();
      }
    }).isInstanceOf(java.util.concurrent.StructuredTaskScope.FailedException.class)
        .hasCauseInstanceOf(NumberFormatException.class);
  }

  @Test
  void pemRoundTrip() throws Exception {
    KeyPair pair = KeyPairGenerator.getInstance("EC").generateKeyPair();
    String pem = PEMEncoder.of().encodeToString(pair.getPublic());
    PublicKey decoded = PEMDecoder.of().decode(pem, PublicKey.class);
    assertThat(pem).startsWith("-----BEGIN PUBLIC KEY-----");
    assertThat(decoded).isEqualTo(pair.getPublic());
  }
}
