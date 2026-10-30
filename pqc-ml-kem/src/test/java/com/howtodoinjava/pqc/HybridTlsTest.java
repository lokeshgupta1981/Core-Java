package com.howtodoinjava.pqc;

import java.nio.file.Path;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLProtocolException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Run with JDK 27 for the hybrid tests and with JDK 25 for the "not supported" tests.
 */
class HybridTlsTest {

  private static final char[] PASSWORD = "changeit".toCharArray();

  @TempDir
  static Path dir;

  private static LocalTls tls;

  @BeforeAll
  static void createSelfSignedCertificate() throws Exception {
    Path keyStore = dir.resolve("server.p12");
    Path keytool = Path.of(System.getProperty("java.home"), "bin", "keytool");
    Process process = new ProcessBuilder(keytool.toString(), "-genkeypair", "-alias", "server",
        "-keyalg", "EC", "-groupname", "secp256r1", "-dname", "CN=localhost", "-validity", "30",
        "-storetype", "PKCS12", "-keystore", keyStore.toString(), "-storepass", new String(PASSWORD))
        .inheritIO().start();
    assertThat(process.waitFor()).isZero();
    tls = new LocalTls(keyStore, PASSWORD);
  }

  private static int feature() {
    return Runtime.version().feature();
  }

  @Test
  void givenJdk27_whenReadingDefaultNamedGroups_thenHybridGroupIsFirst() {
    assumeTrue(feature() >= 27);

    List<String> groups = List.of(tls.context().getDefaultSSLParameters().getNamedGroups());

    assertThat(groups).first().isEqualTo("X25519MLKEM768");
    assertThat(groups).contains("x25519", "secp256r1");
  }

  @Test
  void givenJdk27_whenServerAcceptsOnlyHybridGroup_thenHandshakeSucceeds() throws Exception {
    assumeTrue(feature() >= 27);

    String protocol = tls.handshake(new String[] {"X25519MLKEM768"}, null);

    assertThat(protocol).isEqualTo("TLSv1.3");
  }

  @Test
  void givenJdk27_whenClientOffersOnlyClassicalGroup_thenHandshakeFallsBack() throws Exception {
    assumeTrue(feature() >= 27);

    String protocol = tls.handshake(null, new String[] {"x25519"});

    assertThat(protocol).isEqualTo("TLSv1.3");
  }

  @Test
  void givenJdk27_whenNoGroupInCommon_thenHandshakeFails() {
    assumeTrue(feature() >= 27);

    assertThatThrownBy(() -> tls.handshake(new String[] {"X25519MLKEM768"}, new String[] {"x25519"}))
        .isInstanceOf(SSLProtocolException.class);
  }

  @Test
  void givenJdk25_whenReadingDefaultNamedGroups_thenNoHybridGroup() {
    assumeTrue(feature() < 27);

    assertThat(tls.context().getDefaultSSLParameters().getNamedGroups())
        .doesNotContain("X25519MLKEM768")
        .startsWith("x25519");
  }

  @Test
  void givenJdk25_whenServerAcceptsOnlyHybridGroup_thenHandshakeFails() {
    assumeTrue(feature() < 27);

    assertThatThrownBy(() -> tls.handshake(new String[] {"X25519MLKEM768"}, null))
        .isInstanceOf(SSLProtocolException.class);
  }
}
