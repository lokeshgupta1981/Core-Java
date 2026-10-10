package com.howtodoinjava.core.basic;

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
 * Examples for the tutorial "Java Base64 Encode and Decode (Basic, URL-Safe and MIME)".
 * https://howtodoinjava.com/java8/base64-encoding-and-decoding/
 */
public class Base64Examples {
    static void encodeFile(Path source, Path target) throws IOException {
        try (OutputStream out = Base64.getEncoder().wrap(Files.newOutputStream(target))) {
            Files.copy(source, out);
        }
    }
    static String decodeFile(Path encoded) throws IOException {
        try (InputStream in = Base64.getDecoder().wrap(Files.newInputStream(encoded))) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
    static boolean isBase64(String text) {
        try {
            Base64.getDecoder().decode(text);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            byte[] data = "Cat".getBytes(StandardCharsets.UTF_8);

            String encoded = Base64.getEncoder().encodeToString(data);                         // "Q2F0"
            show("encoded", encoded);
            String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);  // "Cat"
            show("decoded", decoded);
            String urlSafe = Base64.getUrlEncoder().encodeToString(new byte[] {-5, -1});       // "-_8="
            show("urlSafe", urlSafe);
            String standard = Base64.getEncoder().encodeToString(new byte[] {-5, -1});         // "+/8="
            show("standard", standard);
            String unpadded = Base64.getEncoder().withoutPadding().encodeToString(new byte[] {-5, -1});  // "+/8"
            show("unpadded", unpadded);
        }
        {
            String oneByte = Base64.getEncoder().encodeToString("C".getBytes(StandardCharsets.UTF_8));     // "Qw=="
            show("oneByte", oneByte);
            String twoBytes = Base64.getEncoder().encodeToString("Ca".getBytes(StandardCharsets.UTF_8));   // "Q2E="
            show("twoBytes", twoBytes);
            String threeBytes = Base64.getEncoder().encodeToString("Cat".getBytes(StandardCharsets.UTF_8)); // "Q2F0"
            show("threeBytes", threeBytes);
        }
        {
            String credentials = "username:password";

            String encoded = Base64.getEncoder()
                    .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
            String header = "Basic " + encoded;                     // "Basic dXNlcm5hbWU6cGFzc3dvcmQ="
            show("header", header);
        }
        {
            String received = "dXNlcm5hbWU6cGFzc3dvcmQ=";

            byte[] bytes = Base64.getDecoder().decode(received);
            String credentials = new String(bytes, StandardCharsets.UTF_8);  // "username:password"
            show("credentials", credentials);
        }
        {
            byte[] note = "Hi".getBytes(StandardCharsets.UTF_8);

            String padded = Base64.getEncoder().encodeToString(note);                      // "SGk="
            show("padded", padded);
            String unpadded = Base64.getEncoder().withoutPadding().encodeToString(note);   // "SGk"
            show("unpadded", unpadded);
            String back = new String(Base64.getDecoder().decode(unpadded), StandardCharsets.UTF_8);  // "Hi"
            show("back", back);
        }
        {
            String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2tlc2giLCJyb2xlIjoiZWRpdG9yIn0.c2lnbmF0dXJl";

            String payload = jwt.split("\\.")[1];
            String claims = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);  // "{"sub":"lokesh","role":"editor"}"
            show("claims", claims);
        }
        {
            byte[] attachment = new byte[100];

            String mime = Base64.getMimeEncoder().encodeToString(attachment);
            int firstLineLength = mime.indexOf("\r\n");                   // 76
            show("firstLineLength", firstLineLength);
            int restoredLength = Base64.getMimeDecoder().decode(mime).length;  // 100
            show("restoredLength", restoredLength);
        }
        {
            byte[] certificate = new byte[60];

            String pem = Base64.getMimeEncoder(64, "\n".getBytes(StandardCharsets.US_ASCII))
                    .encodeToString(certificate);
            int pemLineLength = pem.indexOf('\n');                         // 64
            show("pemLineLength", pemLineLength);
        }
        {
            Path recipe = Files.createTempFile("recipe", ".txt");
            Files.writeString(recipe, "Mix flour and water.");
            Path encodedRecipe = Files.createTempFile("recipe", ".b64");

            encodeFile(recipe, encodedRecipe);
            String fileContent = Files.readString(encodedRecipe);     // "TWl4IGZsb3VyIGFuZCB3YXRlci4="
            show("fileContent", fileContent);
            String restored = decodeFile(encodedRecipe);              // "Mix flour and water."
            show("restored", restored);
        }
        {
            Path avatar = Files.createTempFile("avatar", ".png");
            Files.write(avatar, new byte[] {-119, 80, 78, 71});

            String dataUri = "data:image/png;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(avatar));  // "data:image/png;base64,iVBORw=="
            show("dataUri", dataUri);
        }
        {
            String fromQuery = "Q2F0 w==";                                          // a "+" became a space in a URL
            show("fromQuery", fromQuery);
            try { byte[] spaced = Base64.getDecoder().decode(fromQuery); show("spaced", spaced); } catch (Throwable _t) { System.out.println("spaced -> " + _t); }
            try { byte[] wrongDecoder = Base64.getDecoder().decode("-_8="); show("wrongDecoder", wrongDecoder); } catch (Throwable _t) { System.out.println("wrongDecoder -> " + _t); }
            String rightDecoder = Arrays.toString(Base64.getUrlDecoder().decode("-_8="));   // [-5, -1]
            show("rightDecoder", rightDecoder);
            try { byte[] newline = Base64.getDecoder().decode("Q2F0\n"); show("newline", newline); } catch (Throwable _t) { System.out.println("newline -> " + _t); }
            String stripped = new String(Base64.getDecoder().decode("Q2F0\n".strip()), StandardCharsets.UTF_8);  // "Cat"
            show("stripped", stripped);
        }
        {
            boolean valid = isBase64("Q2F0");                        // true
            show("valid", valid);
            boolean invalid = isBase64("Q2F0!");                    // false
            show("invalid", invalid);
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
