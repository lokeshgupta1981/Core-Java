package com.howtodoinjava.core.array;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.regex.*;
import java.nio.charset.*;
import java.nio.*;
import java.security.*;
import java.lang.reflect.*;
import java.lang.invoke.*;

/**
 * Examples for the tutorial "Convert Byte Array to String in Java and String to byte[]".
 * https://howtodoinjava.com/java/array/convert-byte-array-string/
 */
public class ByteArrayToString {

    public static void main(String[] args) throws Exception {
        {
            String text = "caf\u00e9";
            byte[] bytes = text.getBytes(StandardCharsets.UTF_8);       // [99, 97, 102, -61, -87]
            show("bytes", bytes);
            String back = new String(bytes, StandardCharsets.UTF_8);
            boolean same = back.equals(text);                            // true
            show("same", same);
        }
        {
            String text = "caf\u00e9";
            byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
            String wrong = new String(bytes, StandardCharsets.ISO_8859_1);
            int wrongLength = wrong.length();                     // 5, the accented e became two characters
            show("wrongLength", wrongLength);
            boolean garbled = wrong.equals("caf\u00c3\u00a9");    // true
            show("garbled", garbled);
        }
        {
            String text = "caf\u00e9";
            int utf8Length = text.getBytes(StandardCharsets.UTF_8).length;        // 5
            show("utf8Length", utf8Length);
            int latin1Length = text.getBytes(StandardCharsets.ISO_8859_1).length;  // 4
            show("latin1Length", latin1Length);
            int utf16Length = text.getBytes(StandardCharsets.UTF_16).length;      // 10, including a 2-byte byte order mark
            show("utf16Length", utf16Length);
            byte[] ascii = text.getBytes(StandardCharsets.US_ASCII);              // [99, 97, 102, 63]
            show("ascii", ascii);
        }
        {
            String defaultName = Charset.defaultCharset().name();   // "UTF-8"
            show("defaultName", defaultName);
        }
        {
            byte[] buffer = {104, 105, 33, 0, 0, 0};
            int read = 3;
            String message = new String(buffer, 0, read, StandardCharsets.US_ASCII);   // "hi!"
            show("message", message);
        }
        {
            byte[] bytes = "caf\u00e9".getBytes(StandardCharsets.UTF_8);
            byte[] cut = Arrays.copyOf(bytes, 4);
            String replaced = new String(cut, StandardCharsets.UTF_8);
            boolean hasReplacement = replaced.endsWith("\uFFFD");   // true
            show("hasReplacement", hasReplacement);
        }
        {
            byte[] cut = Arrays.copyOf("caf\u00e9".getBytes(StandardCharsets.UTF_8), 4);
            CharsetDecoder strict = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT);
            try { String checked = strict.decode(ByteBuffer.wrap(cut)).toString(); show("checked", checked); } catch (Throwable _t) { System.out.println("checked -> " + _t); }
        }
        {
            byte[] header = {(byte) 0xFF, (byte) 0xD8, 65};
            String asText = new String(header, StandardCharsets.UTF_8);
            byte[] roundTrip = asText.getBytes(StandardCharsets.UTF_8);
            boolean equal = Arrays.equals(header, roundTrip);   // false
            show("equal", equal);
            int newLength = roundTrip.length;                   // 7, each bad byte became 3 bytes of U+FFFD
            show("newLength", newLength);
        }
        {
            byte[] header = {(byte) 0xFF, (byte) 0xD8, 65};
            String encoded = Base64.getEncoder().encodeToString(header);   // "/9hB"
            show("encoded", encoded);
            byte[] decoded = Base64.getDecoder().decode(encoded);          // [-1, -40, 65]
            show("decoded", decoded);
            boolean restored = Arrays.equals(header, decoded);             // true
            show("restored", restored);
            String urlSafe = Base64.getUrlEncoder().withoutPadding().encodeToString(header);   // "_9hB"
            show("urlSafe", urlSafe);
        }
        {
            try { byte[] notBase64 = Base64.getDecoder().decode("hello world"); show("notBase64", notBase64); } catch (Throwable _t) { System.out.println("notBase64 -> " + _t); }
        }
        {
            byte[] bytes = "caf\u00e9".getBytes(StandardCharsets.UTF_8);
            String hex = HexFormat.of().formatHex(bytes);                          // "636166c3a9"
            show("hex", hex);
            byte[] parsed = HexFormat.of().parseHex("636166c3a9");                 // [99, 97, 102, -61, -87]
            show("parsed", parsed);
            String pretty = HexFormat.ofDelimiter(":").withUpperCase().formatHex(bytes);   // "63:61:66:C3:A9"
            show("pretty", pretty);
        }
        {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] digest = sha256.digest("hello".getBytes(StandardCharsets.UTF_8));
            String checksum = HexFormat.of().formatHex(digest);   // "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"
            show("checksum", checksum);
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
