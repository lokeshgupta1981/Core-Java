package com.howtodoinjava.pqc;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KEM;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class MessageSealer {

  private static final SecureRandom RANDOM = new SecureRandom();

  public SealedMessage seal(PublicKey receiverKey, String message) throws GeneralSecurityException {
    KEM.Encapsulated encapsulated = KEM.getInstance("ML-KEM")
        .newEncapsulator(receiverKey)
        .encapsulate(0, 32, "AES");

    byte[] iv = new byte[12];
    RANDOM.nextBytes(iv);
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, encapsulated.key(), new GCMParameterSpec(128, iv));
    byte[] ciphertext = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));

    return new SealedMessage(encapsulated.encapsulation(), iv, ciphertext);
  }

  public String open(PrivateKey receiverKey, SealedMessage sealed) throws GeneralSecurityException {
    SecretKey aesKey = KEM.getInstance("ML-KEM")
        .newDecapsulator(receiverKey)
        .decapsulate(sealed.encapsulation(), 0, 32, "AES");

    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(128, sealed.iv()));
    return new String(cipher.doFinal(sealed.ciphertext()), StandardCharsets.UTF_8);
  }
}
