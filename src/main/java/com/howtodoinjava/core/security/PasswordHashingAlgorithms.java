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

/**
 * Examples for the tutorial "Java Password Hashing: MD5, SHA, PBKDF2, bcrypt and Argon2".
 * https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
 */
public class PasswordHashingAlgorithms {
    static byte[] pbkdf2(char[] password, byte[] salt, int iterations) throws GeneralSecurityException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } finally {
            spec.clearPassword();
        }
    }

    static final int ITERATIONS = 600_000;
    static final SecureRandom RANDOM = new SecureRandom();
    static String hash(char[] password) throws GeneralSecurityException {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        byte[] derived = pbkdf2(password, salt, ITERATIONS);
        HexFormat hex = HexFormat.of();
        return "pbkdf2-sha256$" + ITERATIONS + "$" + hex.formatHex(salt) + "$" + hex.formatHex(derived);
    }
    static boolean verify(char[] password, String stored) throws GeneralSecurityException {
        String[] parts = stored.split("\\$");
        int iterations = Integer.parseInt(parts[1]);
        byte[] salt = HexFormat.of().parseHex(parts[2]);
        byte[] expected = HexFormat.of().parseHex(parts[3]);
        byte[] actual = pbkdf2(password, salt, iterations);
        return MessageDigest.isEqual(expected, actual);
    }
    public static void main(String[] args) throws Exception {
        {
            String stored = hash("s3cret-Pa55".toCharArray());           // "pbkdf2-sha256$600000$" + salt + "$" + hash, new every time
            show("stored", stored);
            boolean login = verify("s3cret-Pa55".toCharArray(), stored);   // true
            show("login", login);
            boolean typo = verify("s3cret-pa55".toCharArray(), stored);    // false
            show("typo", typo);
        }
        {
            HexFormat hex = HexFormat.of();
            byte[] word = "password".getBytes(StandardCharsets.UTF_8);
            String md5 = hex.formatHex(MessageDigest.getInstance("MD5").digest(word));       // "5f4dcc3b5aa765d61d8327deb882cf99"
            show("md5", md5);
            String sha256 = hex.formatHex(MessageDigest.getInstance("SHA-256").digest(word)); // "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"
            show("sha256", sha256);
        }
        {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);
            String wrong = salt.toString();                       // "[B@..." the array identity, not the 16 random bytes
            show("wrong", wrong);
            String right = HexFormat.of().formatHex(salt);         // 32 hex characters
            show("right", right);
            int saltHexLength = right.length();                    // 32
            show("saltHexLength", saltHexLength);
        }
        {
            String first = hash("blue-river-42".toCharArray());
            String second = hash("blue-river-42".toCharArray());
            boolean differentSalts = !first.equals(second);              // true
            show("differentSalts", differentSalts);
            boolean matches = verify("blue-river-42".toCharArray(), second);   // true
            show("matches", matches);
            int parts = first.split("\\$").length;                       // 4
            show("parts", parts);
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
