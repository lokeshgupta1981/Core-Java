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
import java.security.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/**
 * Examples for the tutorial "Java File Checksum: MD5 and SHA-256 Hash Examples".
 * https://howtodoinjava.com/java/java-security/sha-md5-file-checksum-hash/
 */
public class FileChecksumMd5Sha256 {
    static String checksum(Path file, String algorithm) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        try (InputStream in = new DigestInputStream(Files.newInputStream(file), digest)) {
            in.transferTo(OutputStream.nullOutputStream());
        }
        return HexFormat.of().formatHex(digest.digest());
    }
    static boolean matchesPublished(Path file, Path checksumFile) throws IOException, NoSuchAlgorithmException {
        String published = Files.readString(checksumFile).strip().split("\\s+")[0];
        byte[] expected = HexFormat.of().parseHex(published.toLowerCase());
        byte[] actual = HexFormat.of().parseHex(checksum(file, "SHA-256"));
        return MessageDigest.isEqual(expected, actual);
    }
    public static void main(String[] args) throws Exception {
        {
            Path file = Files.writeString(Files.createTempFile("greeting", ".txt"), "hello");

            String md5 = checksum(file, "MD5");            // "5d41402abc4b2a76b9719d911017c592"
            show("md5", md5);
            String sha256 = checksum(file, "SHA-256");     // "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"
            show("sha256", sha256);
            int md5Length = md5.length();                  // 32
            show("md5Length", md5Length);
            int sha256Length = sha256.length();            // 64
            show("sha256Length", sha256Length);
        }
        {
            Path config = Files.writeString(Files.createTempFile("app", ".properties"), "hello");

            byte[] data = Files.readAllBytes(config);
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(data);
            String hex = HexFormat.of().formatHex(hash);       // "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"
            show("hex", hex);
            String upper = HexFormat.of().withUpperCase().formatHex(hash).substring(0, 8);   // "2CF24DBA"
            show("upper", upper);
        }
        {
            Path big = Files.createTempFile("disk", ".img");
            try (OutputStream out = Files.newOutputStream(big)) {
                byte[] block = new byte[1024 * 1024];
                for (int i = 0; i < 50; i++) {
                    out.write(block);
                }
            }
            long size = Files.size(big);                                    // 52428800
            show("size", size);
            String sha512 = checksum(big, "SHA-512").substring(0, 16);      // "50173936f83822c2"
            show("sha512", sha512);
        }
        {
            Path invoice = Files.writeString(Files.createTempFile("invoice", ".pdf"), "hello");

            String codecMd5;
            try (InputStream in = Files.newInputStream(invoice)) {
                codecMd5 = DigestUtils.md5Hex(in);
            }
            String codecSha = new DigestUtils(MessageDigestAlgorithms.SHA_256).digestAsHex(invoice.toFile());   // "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"
            show("codecSha", codecSha);
            boolean codecMatch = codecMd5.equals("5d41402abc4b2a76b9719d911017c592");                          // true
            show("codecMatch", codecMatch);
        }
        {
            Path zip = Files.writeString(Files.createTempFile("app-2.4", ".zip"), "hello");
            Path sums = Files.writeString(Files.createTempFile("app-2.4", ".sha256"),
            "2CF24DBA5FB0A30E26E83B2AC5B9E29E1B161E5C1FA7425E73043362938B9824  app-2.4.zip\n");
            boolean intact = matchesPublished(zip, sums);                // true
            show("intact", intact);

            Files.writeString(zip, "hellO");
            boolean damaged = matchesPublished(zip, sums);               // false
            show("damaged", damaged);
        }
        {
            Path fruit = Files.writeString(Files.createTempFile("fruit", ".txt"), "grape");
            byte[] grapeHash = MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(fruit));

            String broken = new BigInteger(1, grapeHash).toString(16);
            int brokenLength = broken.length();                          // 63
            show("brokenLength", brokenLength);
            String correct = HexFormat.of().formatHex(grapeHash);
            int correctLength = correct.length();                        // 64
            show("correctLength", correctLength);
            String start = correct.substring(0, 8);                      // "0f78fcc4"
            show("start", start);
        }
        {
            Path export = Files.writeString(Files.createTempFile("export", ".csv"), "rows=5");
            SecretKeySpec exportKey = new SecretKeySpec("demo-key-from-config".getBytes(StandardCharsets.UTF_8), "HmacSHA256");

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(exportKey);
            String tag = HexFormat.of().formatHex(mac.doFinal(Files.readAllBytes(export)));
            int tagLength = tag.length();                                // 64
            show("tagLength", tagLength);

            Files.writeString(export, "rows=500");
            String edited = HexFormat.of().formatHex(mac.doFinal(Files.readAllBytes(export)));
            boolean exportValid = MessageDigest.isEqual(tag.getBytes(StandardCharsets.US_ASCII), edited.getBytes(StandardCharsets.US_ASCII));   // false
            show("exportValid", exportValid);
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
