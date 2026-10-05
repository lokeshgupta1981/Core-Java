package com.howtodoinjava.java27;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PEMDecoder;
import java.security.PEMEncoder;
import java.security.PublicKey;

/**
 * JEP 538: PEM Encodings of Cryptographic Objects (Third Preview).
 */
public class PemKeys {

  public static String toPem(PublicKey key) {
    return PEMEncoder.of().encodeToString(key);
  }

  public static PublicKey fromPem(String pem) {
    return PEMDecoder.of().decode(pem, PublicKey.class);
  }

  public static void main(String[] args) throws NoSuchAlgorithmException {
    KeyPair pair = KeyPairGenerator.getInstance("EC").generateKeyPair();
    String pem = toPem(pair.getPublic());
    System.out.println(pem);
    System.out.println("Round trip equal: " + fromPem(pem).equals(pair.getPublic()));
  }
}
