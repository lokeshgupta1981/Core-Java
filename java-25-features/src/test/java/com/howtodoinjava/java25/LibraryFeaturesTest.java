package com.howtodoinjava.java25;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.howtodoinjava.java25.libraries.BookCatalog;
import com.howtodoinjava.java25.libraries.BookLookup;
import com.howtodoinjava.java25.libraries.KeyDerivation;
import com.howtodoinjava.java25.libraries.LibraryContext;
import com.howtodoinjava.java25.libraries.PemKeys;
import com.howtodoinjava.java25.libraries.VectorMath;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.IntStream;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;

class LibraryFeaturesTest {

  @Test
  void scopedValueIsBoundOnlyInsideRun() throws Exception {
    LibraryContext ctx = new LibraryContext();
    ctx.borrow("Lokesh", "Dune");
    assertThat(ctx.log()).containsExactly(
        "Lokesh borrowed Dune", "Librarian stamped Dune", "Lokesh left with Dune");
    assertThat(LibraryContext.memberDiscount()).isEqualTo(12);
    assertThat(LibraryContext.MEMBER.isBound()).isFalse();
    assertThat(LibraryContext.currentMemberOrGuest()).isEqualTo("guest");
    assertThatThrownBy(LibraryContext.MEMBER::get)
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("ScopedValue not bound");
  }

  @Test
  void hkdfDerivesSameKeyForSameInputs() throws Exception {
    byte[] secret = "shared-secret".getBytes(StandardCharsets.UTF_8);
    byte[] salt = "library-salt".getBytes(StandardCharsets.UTF_8);
    SecretKey k1 = KeyDerivation.deriveAesKey(secret, salt, "loan-records");
    SecretKey k2 = KeyDerivation.deriveAesKey(secret, salt, "loan-records");
    SecretKey k3 = KeyDerivation.deriveAesKey(secret, salt, "member-emails");
    assertThat(k1.getAlgorithm()).isEqualTo("AES");
    assertThat(k1.getEncoded()).hasSize(32).isEqualTo(k2.getEncoded()).isNotEqualTo(k3.getEncoded());
    assertThat(KeyDerivation.hex(k1)).startsWith("238909ed11fb6a3c");
  }

  @Test
  void stableValueIsComputedOnceEvenWithManyThreads() throws Exception {
    BookCatalog catalog = new BookCatalog();
    assertThat(catalog.isLoaded()).isFalse();
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      IntStream.range(0, 100).forEach(i -> executor.submit(() -> catalog.pages().get("Dune")));
    }
    assertThat(catalog.pages()).containsEntry("Dune", 412);
    assertThat(catalog.loadCount()).isEqualTo(1);
    assertThat(catalog.isLoaded()).isTrue();
    assertThat(catalog.banner().get()).isEqualTo("Library opened");
    assertThat(catalog.shelves().toString()).isEqualTo("[.unset, .unset, .unset]");
    assertThat(catalog.shelves().get(0)).isEqualTo("Shelf-A");
    assertThat(catalog.shelves().toString()).isEqualTo("[Shelf-A, .unset, .unset]");
  }

  @Test
  void structuredTaskScopeJoinsBothSubtasks() throws Exception {
    long start = System.nanoTime();
    BookLookup.Offer offer = new BookLookup(Duration.ofMillis(150), false).offer("Dune");
    long tookMs = (System.nanoTime() - start) / 1_000_000;
    assertThat(offer).isEqualTo(new BookLookup.Offer("Dune", 12, 2));
    assertThat(tookMs).isLessThan(240);   // 100 ms and 150 ms run in parallel
  }

  @Test
  void failedSubtaskCancelsScope() {
    assertThatThrownBy(() -> new BookLookup(Duration.ofMillis(50), true).offer("Dune"))
        .isInstanceOf(StructuredTaskScope.FailedException.class)
        .hasCauseInstanceOf(IllegalStateException.class)
        .hasRootCauseMessage("stock service unavailable");
  }

  @Test
  void timeoutStopsSlowSubtask() {
    long start = System.nanoTime();
    assertThatThrownBy(() -> new BookLookup(Duration.ofSeconds(5), false)
        .offerWithTimeout("Dune", Duration.ofMillis(300)))
        .isInstanceOf(StructuredTaskScope.TimeoutException.class);
    assertThat((System.nanoTime() - start) / 1_000_000).isLessThan(2_000);
  }

  @Test
  void forkedSubtaskInheritsScopedValue() throws Exception {
    assertThat(new BookLookup(Duration.ZERO, false).memberSeenBySubtask()).isEqualTo("Lokesh");
  }

  @Test
  void pemRoundTrip() throws Exception {
    KeyPair pair = PemKeys.newKeyPair();
    String pem = PemKeys.toPem(pair.getPublic());
    assertThat(pem).startsWith("-----BEGIN PUBLIC KEY-----").contains("-----END PUBLIC KEY-----");
    assertThat(PemKeys.fromPem(pem)).isEqualTo(pair.getPublic());

    String encrypted = PemKeys.toEncryptedPem(pair.getPrivate(), "s3cret".toCharArray());
    assertThat(encrypted).startsWith("-----BEGIN ENCRYPTED PRIVATE KEY-----");
    assertThat(PemKeys.fromEncryptedPem(encrypted, "s3cret".toCharArray()).getEncoded())
        .isEqualTo(pair.getPrivate().getEncoded());
  }

  @Test
  void vectorMultiplyMatchesScalarLoop() {
    float[] prices = new float[37];
    float[] quantities = new float[37];
    for (int i = 0; i < 37; i++) {
      prices[i] = i + 0.5f;
      quantities[i] = i % 4;
    }
    float[] totals = VectorMath.multiply(prices, quantities);
    for (int i = 0; i < 37; i++) {
      assertThat(totals[i]).isEqualTo(prices[i] * quantities[i]);
    }
    assertThat(VectorMath.multiply(new float[] {5, 3, 8, 2, 7}, new float[] {2, 4, 1, 3, 2}))
        .containsExactly(10f, 12f, 8f, 6f, 14f);
  }
}
