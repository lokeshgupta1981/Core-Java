package com.howtodoinjava.core.security;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class EdwardAlgoExample {
  public static void main(String[] args)
      throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

    KeyPairGenerator kpg = KeyPairGenerator.getInstance("Ed448");
    KeyPair kp = kpg.generateKeyPair();

    byte[] msg = "test_string".getBytes(StandardCharsets.UTF_8);

    Signature sig = Signature.getInstance("Ed448");
    sig.initSign(kp.getPrivate());
    sig.update(msg);
    byte[] s = sig.sign();

    String encodedString = Base64.getEncoder().encodeToString(s);
    System.out.println(encodedString);
  }
}
