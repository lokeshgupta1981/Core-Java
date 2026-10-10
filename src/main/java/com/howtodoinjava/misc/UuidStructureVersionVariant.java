package com.howtodoinjava.misc;

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
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Examples for the tutorial "Java UUID: Generate v4, v5 and v7 UUIDs with Examples".
 * https://howtodoinjava.com/java/java-uuid/
 */
public class UuidStructureVersionVariant {
    static byte[] toBytes(UUID uuid) {
        return ByteBuffer.allocate(16)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits())
                .array();
    }
    static UUID nameUuidV5(UUID namespace, String name) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.update(toBytes(namespace));
            byte[] hash = sha1.digest(name.getBytes(StandardCharsets.UTF_8));
            hash[6] = (byte) ((hash[6] & 0x0f) | 0x50);   // version 5
            hash[8] = (byte) ((hash[8] & 0x3f) | 0x80);   // variant 2
            ByteBuffer buffer = ByteBuffer.wrap(hash, 0, 16);
            return new UUID(buffer.getLong(), buffer.getLong());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-1 is required in every JDK", e);
        }
    }
    static UUID uuidV7(long epochMillis) {
        if (epochMillis < 0 || epochMillis >= (1L << 48)) {
            throw new IllegalArgumentException("Timestamp out of range: " + epochMillis);
        }
        long msb = (epochMillis << 16) | 0x7000L | (RANDOM.nextInt() & 0x0FFF);
        long lsb = (RANDOM.nextLong() & 0x3FFFFFFFFFFFFFFFL) | 0x8000000000000000L;
        return new UUID(msb, lsb);
    }

    static final SecureRandom RANDOM = new SecureRandom();
    static Optional<UUID> parseUuid(String text) {
        if (text == null || !text.matches("[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")) {
            return Optional.empty();
        }
        return Optional.of(UUID.fromString(text));
    }
    public static void main(String[] args) throws Exception {
        {
            UUID id = UUID.randomUUID();                                     // random, e.g. 3f2a51c6-8d0e-4b7a-9c3e-5b1f0a7d2e48
            show("id", id);
            int version = id.version();                                       // 4
            show("version", version);
            int length = id.toString().length();                              // 36
            show("length", length);
            UUID parsed = UUID.fromString("1b4e28ba-2fa1-4d2e-883f-0016d3cca427");  // 1b4e28ba-2fa1-4d2e-883f-0016d3cca427
            show("parsed", parsed);
            UUID named = UUID.nameUUIDFromBytes("lokesh".getBytes(StandardCharsets.UTF_8));  // 5f0e353b-1c01-395c-8823-3f477bf4f945
            show("named", named);
        }
        {
            UUID nil = new UUID(0L, 0L);                                     // 00000000-0000-0000-0000-000000000000
            show("nil", nil);
            UUID max = new UUID(-1L, -1L);                                    // ffffffff-ffff-ffff-ffff-ffffffffffff
            show("max", max);
        }
        {
            UUID orderId = UUID.randomUUID();                                // random, e.g. 9c5b94b1-35ad-49bb-b118-8e8fc24abf80
            show("orderId", orderId);
            int orderVersion = orderId.version();                             // 4
            show("orderVersion", orderVersion);
            int orderVariant = orderId.variant();                             // 2
            show("orderVariant", orderVariant);
            String compact = orderId.toString().replace("-", "");             // 32 hex digits, no hyphens
            show("compact", compact);
        }
        {
            UUID dns = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");  // namespace for domain names
            show("dns", dns);
            byte[] name = "www.example.com".getBytes(StandardCharsets.UTF_8);
            byte[] input = ByteBuffer.allocate(16 + name.length).put(toBytes(dns)).put(name).array();
            UUID v3 = UUID.nameUUIDFromBytes(input);                          // 5df41881-3aed-3515-88a7-2f4a814cf09e
            show("v3", v3);
        }
        {
            UUID dns = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");
            UUID v5 = nameUuidV5(dns, "www.example.com");                     // 2ed6657d-e927-568b-95e1-2665a8aea6a2
            show("v5", v5);
            UUID again = nameUuidV5(dns, "www.example.com");                  // 2ed6657d-e927-568b-95e1-2665a8aea6a2
            show("again", again);
            int v5Version = v5.version();                                     // 5
            show("v5Version", v5Version);
        }
        {
            UUID first = uuidV7(1760000000000L);
            UUID later = uuidV7(1760000000001L);
            String prefix = first.toString().substring(0, 15);                // "0199c82c-c000-7"
            show("prefix", prefix);
            int firstVersion = first.version();                               // 7
            show("firstVersion", firstVersion);
            boolean ordered = first.compareTo(later) < 0;                     // true
            show("ordered", ordered);
            try { UUID broken = uuidV7(-1L); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            UUID upper = UUID.fromString("1B4E28BA-2FA1-4D2E-883F-0016D3CCA427");  // 1b4e28ba-2fa1-4d2e-883f-0016d3cca427
            show("upper", upper);
            try { UUID noHyphens = UUID.fromString("1b4e28ba2fa14d2e883f0016d3cca427"); show("noHyphens", noHyphens); } catch (Throwable _t) { System.out.println("noHyphens -> " + _t); }
            UUID tooShort = UUID.fromString("1-1-1-1-1");                           // 00000001-0001-0001-0001-000000000001
            show("tooShort", tooShort);
        }
        {
            Optional<UUID> valid = parseUuid("1b4e28ba-2fa1-4d2e-883f-0016d3cca427");  // Optional[1b4e28ba-2fa1-4d2e-883f-0016d3cca427]
            show("valid", valid);
            Optional<UUID> invalid = parseUuid("1-1-1-1-1");                           // Optional.empty
            show("invalid", invalid);
        }
        {
            UUID high = UUID.fromString("80000000-0000-0000-0000-000000000000");
            UUID low = UUID.fromString("7fffffff-0000-0000-0000-000000000000");
            int signedOrder = high.compareTo(low);                            // -1
            show("signedOrder", signedOrder);
            int textOrder = Integer.signum(high.toString().compareTo(low.toString()));  // 1
            show("textOrder", textOrder);
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
