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
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Examples for the tutorial "Java Base64 Encode and Decode a File (Text, Image, Large Files)".
 * https://howtodoinjava.com/java/java-security/base64-file-encode-decode/
 */
public class Base64EncodeDecodeFile {
    static void encodeFile(Path source, Path target) throws IOException {
        try (InputStream in = Files.newInputStream(source);
        OutputStream out = Base64.getEncoder().wrap(Files.newOutputStream(target))) {
            in.transferTo(out);
        }
    }
    static void decodeFile(Path source, Path target) throws IOException {
        try (InputStream in = Base64.getDecoder().wrap(Files.newInputStream(source));
        OutputStream out = Files.newOutputStream(target)) {
            in.transferTo(out);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path original = Files.writeString(Files.createTempFile("note", ".txt"), "Buy milk\n");

            String encoded = Base64.getEncoder().encodeToString(Files.readAllBytes(original));   // "QnV5IG1pbGsK"
            show("encoded", encoded);

            Path restored = Files.createTempFile("note-restored", ".txt");
            Files.write(restored, Base64.getDecoder().decode(encoded));
            long firstDifference = Files.mismatch(original, restored);                          // -1, files are identical
            show("firstDifference", firstDifference);
        }
        {
            Path csv = Files.writeString(Files.createTempFile("prices", ".csv"), "apple,5\r\nbanana,3\r\n");
            byte[] bytes = Files.readAllBytes(csv);
            String csvBase64 = Base64.getEncoder().encodeToString(bytes);   // "YXBwbGUsNQ0KYmFuYW5hLDMNCg=="
            show("csvBase64", csvBase64);
            int fileSize = bytes.length;                                     // 19
            show("fileSize", fileSize);
            int base64Size = csvBase64.length();                             // 28 = 4 * ceil(19 / 3)
            show("base64Size", base64Size);
        }
        {
            Path csvFile = Files.writeString(Files.createTempFile("prices", ".csv"), "apple,5\r\nbanana,3\r\n");
            byte[] asciiBytes = Base64.getEncoder().encode(Files.readAllBytes(csvFile));
            Path b64File = Files.write(Files.createTempFile("prices", ".csv.b64"), asciiBytes);
            long b64FileSize = Files.size(b64File);                          // 28
            show("b64FileSize", b64FileSize);
        }
        {
            String fromApi = "YXBwbGUsNQ0KYmFuYW5hLDMNCg==";
            Path decodedCsv = Files.write(Files.createTempFile("decoded", ".csv"), Base64.getDecoder().decode(fromApi));
            List<String> rows = Files.readAllLines(decodedCsv);                // [apple,5, banana,3]
            show("rows", rows);
            long decodedSize = Files.size(decodedCsv);                         // 19
            show("decodedSize", decodedSize);
        }
        {
            BufferedImage pixel = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
            Path png = Files.createTempFile("avatar", ".png");
            ImageIO.write(pixel, "png", png.toFile());

            String imageBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(png));
            String dataUri = "data:image/png;base64," + imageBase64;
            boolean pngSignature = imageBase64.startsWith("iVBORw0KGgo");     // true
            show("pngSignature", pngSignature);

            String payload = dataUri.substring(dataUri.indexOf(',') + 1);
            Path avatarCopy = Files.write(Files.createTempFile("avatar-copy", ".png"), Base64.getDecoder().decode(payload));
            long imageDifference = Files.mismatch(png, avatarCopy);           // -1
            show("imageDifference", imageDifference);
            BufferedImage readBack = ImageIO.read(avatarCopy.toFile());
            int width = readBack.getWidth();                                  // 2
            show("width", width);

            try { byte[] withPrefix = Base64.getDecoder().decode(dataUri); show("withPrefix", withPrefix); } catch (Throwable _t) { System.out.println("withPrefix -> " + _t); }
        }
        {
            Path backup = Files.createTempFile("backup", ".zip");
            Files.write(backup, new byte[3_000_001]);
            Path backupB64 = Files.createTempFile("backup", ".b64");
            Path backupBack = Files.createTempFile("backup-back", ".zip");

            encodeFile(backup, backupB64);
            decodeFile(backupB64, backupBack);
            long encodedFileSize = Files.size(backupB64);                     // 4000004
            show("encodedFileSize", encodedFileSize);
            long backupDifference = Files.mismatch(backup, backupBack);       // -1
            show("backupDifference", backupDifference);
        }
        {
            byte[] invoice = new byte[100];
            String mime = Base64.getMimeEncoder().encodeToString(invoice);
            int mimeLength = mime.length();                                   // 138, 136 characters plus one CRLF
            show("mimeLength", mimeLength);
            int firstLineLength = mime.indexOf("\r\n");                       // 76
            show("firstLineLength", firstLineLength);

            byte[] mimeBack = Base64.getMimeDecoder().decode(mime);
            int mimeBackLength = mimeBack.length;                             // 100
            show("mimeBackLength", mimeBackLength);
            try { byte[] strictBack = Base64.getDecoder().decode(mime); show("strictBack", strictBack); } catch (Throwable _t) { System.out.println("strictBack -> " + _t); }
        }
        {
            Path edited = Files.writeString(Files.createTempFile("token", ".b64"), "QnV5IG1pbGsK\n");
            String raw = Files.readString(edited);
            try { byte[] failsOnNewline = Base64.getDecoder().decode(raw); show("failsOnNewline", failsOnNewline); } catch (Throwable _t) { System.out.println("failsOnNewline -> " + _t); }
            String fixed = new String(Base64.getDecoder().decode(raw.strip()), StandardCharsets.UTF_8);   // "Buy milk\n"
            show("fixed", fixed);
            String noPadding = new String(Base64.getDecoder().decode("YXBwbGU"), StandardCharsets.UTF_8);   // "apple"
            show("noPadding", noPadding);
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
