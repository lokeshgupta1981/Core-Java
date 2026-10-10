package com.howtodoinjava.core.exceptions;

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
import java.math.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.cert.PKIXParameters;
import java.nio.ByteBuffer;

/**
 * Examples for the tutorial "InvalidKeyException: Parameters missing in Java AES (Fixed)".
 * https://howtodoinjava.com/java/exception-handling/invalidkeyexception-parameters-missing/
 */
public class AesIvEncryptDecryptParametersMissing {
    static String encrypt(String plainText, SecretKey key) throws GeneralSecurityException {
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] message = ByteBuffer.allocate(iv.length + cipherText.length).put(iv).put(cipherText).array();
        return Base64.getEncoder().encodeToString(message);
    }
    static String decrypt(String encoded, SecretKey key) throws GeneralSecurityException {
        byte[] message = Base64.getDecoder().decode(encoded);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, message, 0, 12));
        byte[] plain = cipher.doFinal(message, 12, message.length - 12);
        return new String(plain, StandardCharsets.UTF_8);
    }
    static byte[] decryptCbc(byte[] message, SecretKey key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(message, 0, 16));
        return cipher.doFinal(message, 16, message.length - 16);
    }
    static SecretKey keyFromPassword(char[] password, byte[] salt) throws GeneralSecurityException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] raw = factory.generateSecret(new PBEKeySpec(password, salt, 600_000, 256)).getEncoded();
        return new SecretKeySpec(raw, "AES");
    }
    public static void main(String[] args) throws Exception {
        {
            SecretKey key = KeyGenerator.getInstance("AES").generateKey();
            Cipher encrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
            encrypt.init(Cipher.ENCRYPT_MODE, key);                     // no IV given, the provider creates one
            byte[] iv = encrypt.getIV();                                // 16 random bytes, store them
            show("iv", iv);
            Cipher noIv = Cipher.getInstance("AES/CBC/PKCS5Padding");
            try { noIv.init(Cipher.DECRYPT_MODE, key);  } catch (Throwable _t) { System.out.println("-> " + _t); }
            Cipher withIv = Cipher.getInstance("AES/CBC/PKCS5Padding");
            withIv.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            String mode = withIv.getAlgorithm();                        // "AES/CBC/PKCS5Padding", ready to decrypt
            show("mode", mode);
        }
        {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(256);
            SecretKey aesKey = generator.generateKey();
            String token = encrypt("hello", aesKey);                    // Base64 of IV + ciphertext + tag
            show("token", token);
            int storedBytes = Base64.getDecoder().decode(token).length; // 33
            show("storedBytes", storedBytes);
            String text = decrypt(token, aesKey);                       // "hello"
            show("text", text);
        }
        {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);
            SecretKey passwordKey = keyFromPassword("pancakes".toCharArray(), salt);
            int keyLength = passwordKey.getEncoded().length;            // 32
            show("keyLength", keyLength);
        }
        {
            SecretKey shortKey = new SecretKeySpec(new byte[10], "AES");
            Cipher shortKeyCipher = Cipher.getInstance("AES/GCM/NoPadding");
            try { shortKeyCipher.init(Cipher.ENCRYPT_MODE, shortKey);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            SecretKey cbcKey = KeyGenerator.getInstance("AES").generateKey();
            Cipher cbc = Cipher.getInstance("AES/CBC/PKCS5Padding");
            try { cbc.init(Cipher.DECRYPT_MODE, cbcKey, new IvParameterSpec(new byte[12]));  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            SecretKey gcmKey = KeyGenerator.getInstance("AES").generateKey();
            Cipher gcm = Cipher.getInstance("AES/GCM/NoPadding");
            try { gcm.init(Cipher.ENCRYPT_MODE, gcmKey, new IvParameterSpec(new byte[12]));  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            SecretKey reuseKey = KeyGenerator.getInstance("AES").generateKey();
            GCMParameterSpec fixedIv = new GCMParameterSpec(128, new byte[12]);
            Cipher reused = Cipher.getInstance("AES/GCM/NoPadding");
            reused.init(Cipher.ENCRYPT_MODE, reuseKey, fixedIv);
            byte[] first = reused.doFinal(new byte[] {1, 2, 3});         // encrypted once
            show("first", first);
            try { reused.init(Cipher.ENCRYPT_MODE, reuseKey, fixedIv);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            KeyStore emptyStore = KeyStore.getInstance("PKCS12");
            emptyStore.load(null, null);
            try { PKIXParameters pkix = new PKIXParameters(emptyStore); show("pkix", pkix); } catch (Throwable _t) { System.out.println("pkix -> " + _t); }
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
