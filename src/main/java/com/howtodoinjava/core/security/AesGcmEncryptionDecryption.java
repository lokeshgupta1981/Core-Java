package com.howtodoinjava.core.security;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;
import java.nio.ByteBuffer;

/**
 * Examples for the tutorial "Java AES Encryption and Decryption: AES-256-GCM Example".
 * https://howtodoinjava.com/java/java-security/aes-256-encryption-decryption/
 */
public class AesGcmEncryptionDecryption {
    static SecretKey newAesKey() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256);
        return generator.generateKey();
    }
    static byte[] encrypt(byte[] plaintext, SecretKey key) throws GeneralSecurityException {
        byte[] iv = new byte[IV_LENGTH];
        RANDOM.nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, iv));
        byte[] ciphertext = cipher.doFinal(plaintext);

        return ByteBuffer.allocate(iv.length + ciphertext.length).put(iv).put(ciphertext).array();
    }

    static final int IV_LENGTH = 12;
    static final int TAG_BITS = 128;
    static final SecureRandom RANDOM = new SecureRandom();
    static String encryptText(String text, SecretKey key) throws GeneralSecurityException {
        byte[] encrypted = encrypt(text.getBytes(StandardCharsets.UTF_8), key);
        return Base64.getEncoder().encodeToString(encrypted);
    }
    static byte[] decrypt(byte[] ivAndCiphertext, SecretKey key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, ivAndCiphertext, 0, IV_LENGTH));
        return cipher.doFinal(ivAndCiphertext, IV_LENGTH, ivAndCiphertext.length - IV_LENGTH);
    }
    static String decryptText(String base64, SecretKey key) throws GeneralSecurityException {
        byte[] plain = decrypt(Base64.getDecoder().decode(base64), key);
        return new String(plain, StandardCharsets.UTF_8);
    }
    static SecretKey keyFromPassword(char[] password, byte[] salt) throws GeneralSecurityException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, 600_000, 256);
        try {
            byte[] keyBytes = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES");
        } finally {
            spec.clearPassword();
        }
    }
    static void encryptFile(Path source, Path target, SecretKey key) throws IOException, GeneralSecurityException {
        byte[] iv = new byte[IV_LENGTH];
        RANDOM.nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, iv));

        try (InputStream in = Files.newInputStream(source);
        OutputStream file = Files.newOutputStream(target)) {
            file.write(iv);
            try (OutputStream out = new CipherOutputStream(file, cipher)) {
                in.transferTo(out);
            }
        }
    }
    static byte[] decryptFile(Path source, SecretKey key) throws IOException, GeneralSecurityException {
        try (InputStream file = Files.newInputStream(source)) {
            byte[] iv = file.readNBytes(IV_LENGTH);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, iv));
            try (InputStream in = new CipherInputStream(file, cipher)) {
                return in.readAllBytes();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(256);
            SecretKey key = generator.generateKey();

            byte[] encrypted = encrypt("card ends 4242".getBytes(StandardCharsets.UTF_8), key);
            String stored = Base64.getEncoder().encodeToString(encrypted);       // random text, new on every run
            show("stored", stored);
            int length = encrypted.length;                                       // 42 = 12 IV + 14 data + 16 tag
            show("length", length);

            byte[] decrypted = decrypt(Base64.getDecoder().decode(stored), key);
            String plain = new String(decrypted, StandardCharsets.UTF_8);        // "card ends 4242"
            show("plain", plain);
        }
        {
            SecretKey key = newAesKey();
            Cipher ecb = Cipher.getInstance("AES");
            ecb.init(Cipher.ENCRYPT_MODE, key);
            byte[] twoEqualBlocks = "same 16 bytes!!!same 16 bytes!!!".getBytes(StandardCharsets.UTF_8);
            byte[] ecbOut = ecb.doFinal(twoEqualBlocks);

            boolean leaks = Arrays.equals(Arrays.copyOfRange(ecbOut, 0, 16), Arrays.copyOfRange(ecbOut, 16, 32));   // true
            show("leaks", leaks);
            byte[] ecbIv = ecb.getIV();                                                                             // null, ECB has no IV
            show("ecbIv", ecbIv);
        }
        {
            KeyGenerator aesGenerator = KeyGenerator.getInstance("AES");
            aesGenerator.init(256);
            SecretKey newKey = aesGenerator.generateKey();
            int keyBytes = newKey.getEncoded().length;                              // 32
            show("keyBytes", keyBytes);

            String exported = Base64.getEncoder().encodeToString(newKey.getEncoded());
            SecretKey loaded = new SecretKeySpec(Base64.getDecoder().decode(exported), "AES");
            boolean sameKey = Arrays.equals(loaded.getEncoded(), newKey.getEncoded());   // true
            show("sameKey", sameKey);
        }
        {
            SecretKey key = newAesKey();
            String token1 = encryptText("refresh-token-abc", key);
            String token2 = encryptText("refresh-token-abc", key);
            boolean differ = !token1.equals(token2);                     // true, new IV each time
            show("differ", differ);
            int base64Length = token1.length();                          // 60 for 45 bytes
            show("base64Length", base64Length);
        }
        {
            SecretKey key = newAesKey();
            String token = encryptText("refresh-token-abc", key);
            String restored = decryptText(token, key);                   // "refresh-token-abc"
            show("restored", restored);
        }
        {
            SecretKey key = newAesKey();
            byte[] tampered = Base64.getDecoder().decode(encryptText("refresh-token-abc", key));
            tampered[20] ^= 1;
            try { byte[] fromTampered = decrypt(tampered, key); show("fromTampered", fromTampered); } catch (Throwable _t) { System.out.println("fromTampered -> " + _t); }

            SecretKey otherKey = KeyGenerator.getInstance("AES").generateKey();
            try { String wrongKey = decryptText(encryptText("refresh-token-abc", key), otherKey); show("wrongKey", wrongKey); } catch (Throwable _t) { System.out.println("wrongKey -> " + _t); }
        }
        {
            SecretKey key = newAesKey();
            byte[] fixedIv = new byte[12];
            Cipher gcm = Cipher.getInstance("AES/GCM/NoPadding");
            gcm.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, fixedIv));
            byte[] first = gcm.doFinal("a".getBytes(StandardCharsets.UTF_8));
            try { gcm.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, fixedIv));  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            SecretKey key = newAesKey();
            byte[] iv = new byte[12];
            RANDOM.nextBytes(iv);
            Cipher enc = Cipher.getInstance("AES/GCM/NoPadding");
            enc.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
            enc.updateAAD("user:42".getBytes(StandardCharsets.UTF_8));
            byte[] boundToken = enc.doFinal("token".getBytes(StandardCharsets.UTF_8));

            Cipher dec = Cipher.getInstance("AES/GCM/NoPadding");
            dec.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));
            dec.updateAAD("user:43".getBytes(StandardCharsets.UTF_8));
            try { byte[] movedRow = dec.doFinal(boundToken); show("movedRow", movedRow); } catch (Throwable _t) { System.out.println("movedRow -> " + _t); }
        }
        {
            byte[] salt = new byte[16];
            RANDOM.nextBytes(salt);
            SecretKey derived = keyFromPassword("correct horse".toCharArray(), salt);
            byte[] backup = encrypt("my notes".getBytes(StandardCharsets.UTF_8), derived);
            byte[] file = ByteBuffer.allocate(salt.length + backup.length).put(salt).put(backup).array();

            byte[] readSalt = Arrays.copyOfRange(file, 0, 16);
            SecretKey again = keyFromPassword("correct horse".toCharArray(), readSalt);
            String notes = new String(decrypt(Arrays.copyOfRange(file, 16, file.length), again), StandardCharsets.UTF_8);   // "my notes"
            show("notes", notes);
        }
        {
            SecretKey key = newAesKey();
            Path report = Files.writeString(Files.createTempFile("report", ".csv"), "id,total\n1,99.50\n");
            Path locked = Files.createTempFile("report", ".enc");
            encryptFile(report, locked, key);
            long encryptedSize = Files.size(locked);                     // 45 = 12 + 17 + 16
            show("encryptedSize", encryptedSize);
            String unlocked = new String(decryptFile(locked, key), StandardCharsets.UTF_8);   // "id,total\n1,99.50\n"
            show("unlocked", unlocked);
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
