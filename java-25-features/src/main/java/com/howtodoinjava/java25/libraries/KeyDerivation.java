package com.howtodoinjava.java25.libraries;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.HexFormat;
import javax.crypto.KDF;
import javax.crypto.SecretKey;
import javax.crypto.spec.HKDFParameterSpec;

/** JEP 510: Key Derivation Function API (HKDF). */
public class KeyDerivation {

  public static SecretKey deriveAesKey(byte[] sharedSecret, byte[] salt, String purpose)
      throws GeneralSecurityException {
    KDF hkdf = KDF.getInstance("HKDF-SHA256");
    HKDFParameterSpec params = HKDFParameterSpec.ofExtract()
        .addIKM(sharedSecret)
        .addSalt(salt)
        .thenExpand(purpose.getBytes(StandardCharsets.UTF_8), 32);
    return hkdf.deriveKey("AES", params);
  }

  public static String hex(SecretKey key) {
    return HexFormat.of().formatHex(key.getEncoded());
  }
}
