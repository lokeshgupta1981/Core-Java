package com.howtodoinjava.io;

import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;
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

import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * Examples for the tutorial "Read File to Byte Array in Java (Files.readAllBytes and More)".
 * https://howtodoinjava.com/java/io/read-file-content-into-byte-array/
 */
public class ReadFileToBytes {

    public static void main(String[] args) throws Exception {
        {
            Path logo = Files.write(Files.createTempFile("logo", ".png"), new byte[] {(byte) 0x89, 'P', 'N', 'G', 13, 10, 26, 10});

            byte[] bytes = Files.readAllBytes(logo);
            int size = bytes.length;                                    // 8
            show("size", size);

            byte[] header;
            try (InputStream in = Files.newInputStream(logo)) {
                header = in.readNBytes(4);
            }
            String type = new String(header, 1, 3, StandardCharsets.US_ASCII);   // "PNG"
            show("type", type);
        }
        {
            Path invoice = Files.write(Files.createTempFile("invoice", ".pdf"), "%PDF-1.7 sample".getBytes(StandardCharsets.US_ASCII));

            byte[] pdf = Files.readAllBytes(invoice);
            int pdfSize = pdf.length;                                  // 15
            show("pdfSize", pdfSize);
            String base64 = Base64.getEncoder().encodeToString(pdf);
            String start = base64.substring(0, 8);                     // "JVBERi0x"
            show("start", start);
        }
        {
            File file = Files.write(Files.createTempFile("logo", ".png"), new byte[] {(byte) 0x89, 'P', 'N', 'G'}).toFile();
            byte[] bytes = new byte[(int) file.length()];

            int read;
            try (FileInputStream fis = new FileInputStream(file)) {
                read = fis.read(bytes);   // not guaranteed to fill the array
            }
            int bytesRead = read;         // 4 here, but a large file or a network drive can return fewer
            show("bytesRead", bytesRead);
        }
        {
            Path logo = Files.write(Files.createTempFile("logo", ".png"), new byte[] {(byte) 0x89, 'P', 'N', 'G', 13, 10, 26, 10});

            byte[] all;
            try (InputStream in = new FileInputStream(logo.toFile())) {
                all = in.readAllBytes();
            }
            int allSize = all.length;                       // 8
            show("allSize", allSize);

            byte[] firstFour;
            try (InputStream in = Files.newInputStream(logo)) {
                firstFour = in.readNBytes(4);
            }
            int firstByte = firstFour[0] & 0xFF;            // 137
            show("firstByte", firstByte);
        }
        {
            Path backup = Files.writeString(Files.createTempFile("backup", ".tar"), "abc");

            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            try (InputStream in = new DigestInputStream(Files.newInputStream(backup), sha256)) {
                in.transferTo(OutputStream.nullOutputStream());
            }
            String checksum = HexFormat.of().formatHex(sha256.digest()).substring(0, 16);   // "ba7816bf8f01cfea"
            show("checksum", checksum);
        }
        {
            byte[] classBytes;
            try (InputStream in = Objects.requireNonNull(Object.class.getResourceAsStream("Object.class"), "resource not found")) {
                classBytes = in.readNBytes(4);
            }
            String magic = HexFormat.of().formatHex(classBytes);   // "cafebabe"
            show("magic", magic);
        }
        {
            Path source = Files.writeString(Files.createTempFile("notes", ".txt"), "Buy milk");
            Path copy = Files.createTempFile("notes-copy", ".txt");

            byte[] content = Files.readAllBytes(source);
            Files.write(copy, content);
            boolean same = Arrays.equals(content, Files.readAllBytes(copy));   // true
            show("same", same);
        }
        {
            byte[] bytes = {72, 105};
            String asList = Arrays.toString(bytes);           // "[72, 105]"
            show("asList", asList);
            String asHex = HexFormat.of().formatHex(bytes);   // "4869"
            show("asHex", asHex);
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
