package com.howtodoinjava.java25.libraries;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PEMDecoder;
import java.security.PEMEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;

/** JEP 470 (preview): PEM encodings of cryptographic objects. */
public class PemKeys {

  public static KeyPair newKeyPair() throws NoSuchAlgorithmException {
    return KeyPairGenerator.getInstance("Ed25519").generateKeyPair();
  }

  public static String toPem(PublicKey key) {
    return PEMEncoder.of().encodeToString(key);
  }

  public static PublicKey fromPem(String pem) {
    return PEMDecoder.of().decode(pem, PublicKey.class);
  }

  public static String toEncryptedPem(PrivateKey key, char[] password) {
    return PEMEncoder.of().withEncryption(password).encodeToString(key);
  }

  public static PrivateKey fromEncryptedPem(String pem, char[] password) {
    return PEMDecoder.of().withDecryption(password).decode(pem, PrivateKey.class);
  }
}
