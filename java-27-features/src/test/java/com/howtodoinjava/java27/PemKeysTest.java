package com.howtodoinjava.java27;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import org.junit.jupiter.api.Test;

class PemKeysTest {

  @Test
  void publicKeyRoundTrip() throws Exception {
    KeyPair pair = KeyPairGenerator.getInstance("EC").generateKeyPair();
    String pem = PemKeys.toPem(pair.getPublic());

    assertThat(pem).startsWith("-----BEGIN PUBLIC KEY-----");
    assertThat(PemKeys.fromPem(pem)).isEqualTo(pair.getPublic());
  }
}
