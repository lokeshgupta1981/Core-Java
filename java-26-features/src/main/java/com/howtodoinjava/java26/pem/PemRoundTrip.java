package com.howtodoinjava.java26.pem;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PEMDecoder;
import java.security.PEMEncoder;
import java.security.PublicKey;

/** JEP 524 (preview): encode a key to PEM text and decode it back. */
public class PemRoundTrip {

  public static void main(String[] args) throws NoSuchAlgorithmException {
    KeyPair pair = KeyPairGenerator.getInstance("EC").generateKeyPair();
    String pem = PEMEncoder.of().encodeToString(pair.getPublic());
    PublicKey decoded = PEMDecoder.of().decode(pem, PublicKey.class);
    boolean same = decoded.equals(pair.getPublic());

    System.out.println(pem.lines().findFirst().orElse(""));
    System.out.println("same key = " + same);
  }
}
