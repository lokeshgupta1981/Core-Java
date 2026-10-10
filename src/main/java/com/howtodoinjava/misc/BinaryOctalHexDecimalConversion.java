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
import java.nio.file.attribute.*;

/**
 * Examples for the tutorial "Binary, Octal, Hex and Decimal Conversion in Java".
 * https://howtodoinjava.com/java/binary-octal-and-hexadecimal-conversions-in-java/
 */
public class BinaryOctalHexDecimalConversion {
    static OptionalInt parseInBase(String text, int radix) {
        try {
            return OptionalInt.of(Integer.parseInt(text.strip(), radix));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }
    static String convert(String digits, int fromRadix, int toRadix) {
        return Integer.toString(Integer.parseInt(digits, fromRadix), toRadix);
    }
    static String toRwx(int mode) {
        StringBuilder text = new StringBuilder();
        for (int shift = 6; shift >= 0; shift -= 3) {
            int bits = (mode >> shift) & 7;
            text.append((bits & 4) != 0 ? 'r' : '-')
                    .append((bits & 2) != 0 ? 'w' : '-')
                    .append((bits & 1) != 0 ? 'x' : '-');
        }
        return text.toString();
    }
    static String toBase(int number, int radix) {
        if (number == 0) {
            return "0";
        }
        long n = Math.abs((long) number);
        StringBuilder digits = new StringBuilder();
        while (n > 0) {
            digits.append(Character.forDigit((int) (n % radix), radix));
            n /= radix;
        }
        return (number < 0 ? "-" : "") + digits.reverse();
    }
    static int fromBase(String text, int radix) {
        int value = 0;
        for (char c : text.toCharArray()) {
            int digit = Character.digit(c, radix);
            if (digit < 0) {
                throw new NumberFormatException("Not a base-" + radix + " digit: " + c);
            }
            value = value * radix + digit;
        }
        return value;
    }
    public static void main(String[] args) throws Exception {
        {
            int fromBinary = Integer.parseInt("11001010", 2);   // 202
            show("fromBinary", fromBinary);
            int fromOctal = Integer.parseInt("312", 8);         // 202
            show("fromOctal", fromOctal);
            int fromHex = Integer.parseInt("ca", 16);           // 202
            show("fromHex", fromHex);
            String binary = Integer.toBinaryString(202);        // "11001010"
            show("binary", binary);
            String octal = Integer.toOctalString(202);          // "312"
            show("octal", octal);
            String hex = Integer.toHexString(202);              // "ca"
            show("hex", hex);
            String base36 = Integer.toString(202, 36);          // "5m"
            show("base36", base36);
        }
        {
            int mask = 0b1100_1010;                // 202
            show("mask", mask);
            int permissions = 0755;                 // 493
            show("permissions", permissions);
            int color = 0xCA;                       // 202
            show("color", color);
            int padded = 010;                       // 8
            show("padded", padded);
        }
        {
            int fromBinary = Integer.parseInt("10101", 2);      // 21
            show("fromBinary", fromBinary);
            int fromOctal = Integer.parseInt("456", 8);         // 302
            show("fromOctal", fromOctal);
            int fromHex = Integer.parseInt("ABCD", 16);         // 43981
            show("fromHex", fromHex);
            int negative = Integer.parseInt("-ff", 16);         // -255
            show("negative", negative);
            Integer boxed = Integer.valueOf("777", 8);          // 511
            show("boxed", boxed);
        }
        {
            try { int badDigit = Integer.parseInt("102", 2); show("badDigit", badDigit); } catch (Throwable _t) { System.out.println("badDigit -> " + _t); }
            try { int prefixed = Integer.parseInt("0x1F", 16); show("prefixed", prefixed); } catch (Throwable _t) { System.out.println("prefixed -> " + _t); }
            int decodedHex = Integer.decode("0x1F");            // 31
            show("decodedHex", decodedHex);
            int decodedCss = Integer.decode("#1F");             // 31
            show("decodedCss", decodedCss);
            int decodedOctal = Integer.decode("017");           // 15
            show("decodedOctal", decodedOctal);
        }
        {
            OptionalInt ok = parseInBase(" 1010 ", 2);           // OptionalInt[10]
            show("ok", ok);
            OptionalInt wrong = parseInBase("12", 2);           // OptionalInt.empty
            show("wrong", wrong);
        }
        {
            String binary = Integer.toString(21, 2);            // "10101"
            show("binary", binary);
            String octal = Integer.toString(302, 8);            // "456"
            show("octal", octal);
            String hex = Integer.toString(43981, 16);           // "abcd"
            show("hex", hex);
            String shortcut = Integer.toHexString(43981);       // "abcd"
            show("shortcut", shortcut);
            String upper = Integer.toHexString(43981).toUpperCase();  // "ABCD"
            show("upper", upper);
        }
        {
            String byteBits = String.format("%8s", Integer.toBinaryString(5)).replace(' ', '0');  // "00000101"
            show("byteBits", byteBits);
            String twoDigits = String.format("%02X", 10);       // "0A"
            show("twoDigits", twoDigits);
            String eightDigits = String.format("%08x", 48879);  // "0000beef"
            show("eightDigits", eightDigits);
            String octalText = String.format("%o", 493);        // "755"
            show("octalText", octalText);
            String notBinary = String.format("%b", 5);          // "true"
            show("notBinary", notBinary);
        }
        {
            String hexFromBinary = convert("11001010", 2, 16);  // "ca"
            show("hexFromBinary", hexFromBinary);
            String octalFromHex = convert("1ed", 16, 8);        // "755"
            show("octalFromHex", octalFromHex);
            String binaryFromOctal = convert("755", 8, 2);      // "111101101"
            show("binaryFromOctal", binaryFromOctal);
        }
        {
            int rgb = Integer.parseInt("1E90FF", 16);          // 2003199
            show("rgb", rgb);
            int red = (rgb >> 16) & 0xFF;                       // 30
            show("red", red);
            int green = (rgb >> 8) & 0xFF;                      // 144
            show("green", green);
            int blue = rgb & 0xFF;                              // 255
            show("blue", blue);
            String css = String.format("#%02X%02X%02X", red, green, blue);  // "#1E90FF"
            show("css", css);
        }
        {
            String twos = Integer.toBinaryString(-5);          // "11111111111111111111111111111011"
            show("twos", twos);
            String signed = Integer.toString(-5, 2);            // "-101"
            show("signed", signed);
            String allOnes = Integer.toHexString(-1);           // "ffffffff"
            show("allOnes", allOnes);
        }
        {
            int fromSigned = Integer.parseInt("-101", 2);      // -5
            show("fromSigned", fromSigned);
            try { int tooBig = Integer.parseInt("ffffffff", 16); show("tooBig", tooBig); } catch (Throwable _t) { System.out.println("tooBig -> " + _t); }
            int fromUnsigned = Integer.parseUnsignedInt("ffffffff", 16);  // -1
            show("fromUnsigned", fromUnsigned);
            String unsigned = Integer.toUnsignedString(-1);     // "4294967295"
            show("unsigned", unsigned);
        }
        {
            long maxLong = Long.parseLong("7fffffffffffffff", 16);              // 9223372036854775807
            show("maxLong", maxLong);
            long hash = Long.parseUnsignedLong("cbf29ce484222325", 16);         // -3750763034362895579
            show("hash", hash);
            String hashHex = Long.toHexString(hash);                            // "cbf29ce484222325"
            show("hashHex", hashHex);
            BigInteger huge = new BigInteger("ffffffffffffffffffff", 16);       // 1208925819614629174706175
            show("huge", huge);
            String hugeOctal = huge.toString(8);                                // "377777777777777777777777777"
            show("hugeOctal", hugeOctal);
        }
        {
            int mode = Integer.parseInt("755", 8);                       // 493
            show("mode", mode);
            String rwx = toRwx(mode);                                     // "rwxr-xr-x"
            show("rwx", rwx);
            boolean groupCanWrite = (mode & 0020) != 0;                   // false
            show("groupCanWrite", groupCanWrite);
            Set<PosixFilePermission> perms = PosixFilePermissions.fromString(toRwx(0644));
            String applied = PosixFilePermissions.toString(perms);        // "rw-r--r--"
            show("applied", applied);
        }
        {
            String manualBinary = toBase(202, 2);               // "11001010"
            show("manualBinary", manualBinary);
            String manualHex = toBase(-255, 16);                // "-ff"
            show("manualHex", manualHex);
            int manualValue = fromBase("312", 8);               // 202
            show("manualValue", manualValue);
            try { int manualBad = fromBase("19", 8); show("manualBad", manualBad); } catch (Throwable _t) { System.out.println("manualBad -> " + _t); }
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
