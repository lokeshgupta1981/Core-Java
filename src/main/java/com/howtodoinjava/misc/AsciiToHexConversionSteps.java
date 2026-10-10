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

/**
 * Examples for the tutorial "Convert Hex to ASCII and ASCII to Hex in Java (HexFormat)".
 * https://howtodoinjava.com/java/convert-hex-to-ascii-and-ascii-to-hex/
 */
public class AsciiToHexConversionSteps {
    static Optional<String> hexToAscii(String hex) {
        String clean = hex.strip();
        if (clean.length() % 2 != 0 || !clean.chars().allMatch(HexFormat::isHexDigit)) {
            return Optional.empty();
        }
        byte[] bytes = HexFormat.of().parseHex(clean);
        return Optional.of(new String(bytes, StandardCharsets.US_ASCII));
    }
    static String asciiToHex(String text) {
        StringBuilder hex = new StringBuilder(text.length() * 2);
        for (char c : text.toCharArray()) {
            if (c > 127) {
                throw new IllegalArgumentException("Not an ASCII character: " + c);
            }
            hex.append(Character.forDigit(c >> 4, 16))
                    .append(Character.forDigit(c & 0xF, 16));
        }
        return hex.toString();
    }
    static String hexToAsciiManual(String hex) {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Odd number of hex digits: " + hex.length());
        }
        StringBuilder text = new StringBuilder(hex.length() / 2);
        for (int i = 0; i < hex.length(); i += 2) {
            text.append((char) Integer.parseInt(hex, i, i + 2, 16));
        }
        return text.toString();
    }
    static String toHexNaive(String text) {
        StringBuilder hex = new StringBuilder();
        for (char c : text.toCharArray()) {
            hex.append(Integer.toHexString(c));
        }
        return hex.toString();
    }
    public static void main(String[] args) throws Exception {
        {
            HexFormat hex = HexFormat.of();
            String encoded = hex.formatHex("Hi!".getBytes(StandardCharsets.US_ASCII));        // "486921"
            show("encoded", encoded);
            String decoded = new String(hex.parseHex("486921"), StandardCharsets.US_ASCII);   // "Hi!"
            show("decoded", decoded);
        }
        {
            byte[] bytes = "Java".getBytes(StandardCharsets.US_ASCII);
            String plain = HexFormat.of().formatHex(bytes);                                // "4a617661"
            show("plain", plain);
            String upper = HexFormat.of().withUpperCase().formatHex(bytes);                // "4A617661"
            show("upper", upper);
            String spaced = HexFormat.ofDelimiter(" ").formatHex(bytes);                   // "4a 61 76 61"
            show("spaced", spaced);
            String listed = HexFormat.ofDelimiter(", ").withPrefix("0x").formatHex(bytes); // "0x4a, 0x61, 0x76, 0x61"
            show("listed", listed);
            String single = HexFormat.of().toHexDigits((byte) 'J');                        // "4a"
            show("single", single);
        }
        {
            HexFormat hex = HexFormat.of();
            String word = new String(hex.parseHex("4A617661"), StandardCharsets.US_ASCII);    // "Java"
            show("word", word);
            byte[] dump = HexFormat.ofDelimiter(" ").parseHex("4f 4b");
            String spaced = new String(dump, StandardCharsets.US_ASCII);                       // "OK"
            show("spaced", spaced);
            char letter = (char) HexFormat.fromHexDigits("4a");                                // J
            show("letter", letter);
        }
        {
            try { byte[] odd = HexFormat.of().parseHex("4a6"); show("odd", odd); } catch (Throwable _t) { System.out.println("odd -> " + _t); }
            try { byte[] bad = HexFormat.of().parseHex("4g"); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            Optional<String> ok = hexToAscii(" 4f4b\n");     // Optional[OK]
            show("ok", ok);
            Optional<String> odd = hexToAscii("4f4");        // Optional.empty
            show("odd", odd);
            Optional<String> bad = hexToAscii("zz");         // Optional.empty
            show("bad", bad);
        }
        {
            String encoded = asciiToHex("Tab\there");          // "5461620968657265"
            show("encoded", encoded);
            boolean roundTrip = hexToAsciiManual(encoded).equals("Tab\there");  // true
            show("roundTrip", roundTrip);
            try { String broken = hexToAsciiManual("4a6"); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            String letters = toHexNaive("OK");                 // "4f4b"
            show("letters", letters);
            String withTab = toHexNaive("A\tB");               // "41942"
            show("withTab", withTab);
            String padded = String.format("%02x", (int) '\t'); // "09"
            show("padded", padded);
        }
        {
            String word = "caf\u00e9";
            String utf8 = HexFormat.of().formatHex(word.getBytes(StandardCharsets.UTF_8));      // "636166c3a9"
            show("utf8", utf8);
            String ascii = HexFormat.of().formatHex(word.getBytes(StandardCharsets.US_ASCII));  // "6361663f"
            show("ascii", ascii);
            String chars = toHexNaive(word);                                                    // "636166e9"
            show("chars", chars);
            String back = new String(HexFormat.of().parseHex("636166c3a9"), StandardCharsets.UTF_8);
            boolean same = back.equals(word);                                                   // true
            show("same", same);
        }
        {
            String payload = "54 45 4d 50 3d 32 31 2e 35";
            byte[] bytes = HexFormat.ofDelimiter(" ").parseHex(payload);
            String message = new String(bytes, StandardCharsets.US_ASCII);     // "TEMP=21.5"
            show("message", message);
            double celsius = Double.parseDouble(message.split("=")[1]);         // 21.5
            show("celsius", celsius);
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
