package com.howtodoinjava.core.random;

import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

/**
 * Examples for the tutorial "Secure Random Number Generation in Java with SecureRandom".
 * https://howtodoinjava.com/java8/secure-random-number-generation/
 */
public class SecureRandomExamples {
    static String newResetToken(SecureRandom random) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    static String newApiKey(SecureRandom random) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return "key_" + HexFormat.of().formatHex(bytes);
    }
    static String randomCode(SecureRandom random, int length) {
        String alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            code.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return code.toString();
    }
    static class TokenService {

        private static final SecureRandom RANDOM = new SecureRandom();

        String newSessionId() {
            byte[] bytes = new byte[32];
            RANDOM.nextBytes(bytes);
            return HexFormat.of().formatHex(bytes);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            SecureRandom random = new SecureRandom();

            int diceRoll = random.nextInt(1, 7);                     // a value from 1 to 6
            show("diceRoll", diceRoll);
            int loginCode = random.nextInt(100_000, 1_000_000);      // a 6-digit code, e.g. 482915
            show("loginCode", loginCode);
            byte[] secret = new byte[32];
            random.nextBytes(secret);                                // fills 32 unpredictable bytes
            String token = Base64.getUrlEncoder().withoutPadding().encodeToString(secret);
            int tokenLength = token.length();                        // 43
            show("tokenLength", tokenLength);
        }
        {
            Random first = new Random(42);
            Random second = new Random(42);
            boolean sameValues = first.nextInt() == second.nextInt();    // true, the seed decides everything
            show("sameValues", sameValues);
        }
        {
            SecureRandom random = new SecureRandom();

            int percent = random.nextInt(101);                       // 0 to 100
            show("percent", percent);
            int fromRange = random.nextInt(18, 66);                  // 18 to 65
            show("fromRange", fromRange);
            long bigValue = random.nextLong();                       // any long value
            show("bigValue", bigValue);
            double fraction = random.nextDouble();                   // 0.0 inclusive to 1.0 exclusive
            show("fraction", fraction);
            boolean coin = random.nextBoolean();                     // true or false
            show("coin", coin);
            byte[] salt = new byte[16];
            random.nextBytes(salt);                                  // 16 random bytes for a password hash
        }
        {
            SecureRandom random = new SecureRandom();

            List<Integer> winners = random.ints(1, 50)
                    .distinct()
                    .limit(6)
                    .sorted()
                    .boxed()
                    .toList();
            int winnerCount = winners.size();                        // 6
            show("winnerCount", winnerCount);
        }
        {
            SecureRandom random = new SecureRandom();

            String resetToken = newResetToken(random);               // e.g. "Xq3v0bK1...", 43 characters
            show("resetToken", resetToken);
            int resetLength = resetToken.length();                   // 43
            show("resetLength", resetLength);
            String apiKey = newApiKey(random);                       // "key_" plus 64 hex digits
            show("apiKey", apiKey);
            int apiKeyLength = apiKey.length();                      // 68
            show("apiKeyLength", apiKeyLength);
        }
        {
            SecureRandom random = new SecureRandom();

            String otp = String.format("%06d", random.nextInt(1_000_000));   // e.g. "004817"
            show("otp", otp);
            int otpLength = otp.length();                                    // 6
            show("otpLength", otpLength);
            String voucher = randomCode(random, 10);                         // e.g. "K7QM2XHP9D"
            show("voucher", voucher);
            int voucherLength = voucher.length();                            // 10
            show("voucherLength", voucherLength);
        }
        {
            SecureRandom platformDefault = new SecureRandom();
            String algorithm = platformDefault.getAlgorithm();       // "NativePRNG" on Linux and macOS, "DRBG" on Windows
            show("algorithm", algorithm);
            SecureRandom strong = SecureRandom.getInstanceStrong();
            String strongAlgorithm = strong.getAlgorithm();          // "NativePRNGBlocking" on Linux
            show("strongAlgorithm", strongAlgorithm);
        }
        {
            SecureRandom drbg = SecureRandom.getInstance("DRBG",
            DrbgParameters.instantiation(256, DrbgParameters.Capability.PR_AND_RESEED,
            "invoice-service".getBytes(StandardCharsets.UTF_8)));
            String config = drbg.toString();                         // "Hash_DRBG,SHA-256,256,pr_and_reseed"
            show("config", config);
        }
        {
            SecureRandom legacyA = SecureRandom.getInstance("SHA1PRNG");
            legacyA.setSeed(42L);
            SecureRandom legacyB = SecureRandom.getInstance("SHA1PRNG");
            legacyB.setSeed(42L);
            boolean predictable = legacyA.nextInt() == legacyB.nextInt();   // true, only our seed is used
            show("predictable", predictable);

            SecureRandom nativeA = new SecureRandom();
            nativeA.setSeed(42L);
            SecureRandom nativeB = new SecureRandom();
            nativeB.setSeed(42L);
            boolean independent = nativeA.nextInt() != nativeB.nextInt();   // true, the OS seed is still mixed in
            show("independent", independent);
        }
        {
            TokenService service = new TokenService();
            int sessionIdLength = service.newSessionId().length();       // 64
            show("sessionIdLength", sessionIdLength);
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
