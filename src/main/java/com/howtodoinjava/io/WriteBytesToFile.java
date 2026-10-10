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

/**
 * Examples for the tutorial "Write Byte Array to File in Java: Files.write and Streams".
 * https://howtodoinjava.com/java/io/write-byte-array-to-file/
 */
public class WriteBytesToFile {

    public static void main(String[] args) throws Exception {
        {
            byte[] pngHeader = {(byte) 0x89, 'P', 'N', 'G'};
            Path file = Files.createTempFile("avatar", ".png");

            Files.write(file, pngHeader);
            String written = Arrays.toString(Files.readAllBytes(file));   // [-119, 80, 78, 71]
            show("written", written);
            long size = Files.size(file);                                 // 4
            show("size", size);

            try (FileOutputStream out = new FileOutputStream(file.toFile(), true)) {
                out.write(new byte[] {13, 10});
            }
            long afterAppend = Files.size(file);                          // 6
            show("afterAppend", afterAppend);
        }
        {
            Path file = Files.createTempFile("avatar", ".bin");

            Files.write(file, new byte[] {1, 2, 3, 4, 5});
            Files.write(file, new byte[] {9, 9});
            String content = Arrays.toString(Files.readAllBytes(file));   // [9, 9]
            show("content", content);
        }
        {
            Path file = Files.createTempDirectory("chunks").resolve("upload.part");

            Files.write(file, new byte[] {1, 2}, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.write(file, new byte[] {3, 4}, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            String joined = Arrays.toString(Files.readAllBytes(file));   // [1, 2, 3, 4]
            show("joined", joined);
        }
        {
            Path file = Files.createTempDirectory("invoices").resolve("invoice-1.pdf");

            Files.write(file, new byte[] {37, 80, 68, 70}, StandardOpenOption.CREATE_NEW);
            try { Path again = Files.write(file, new byte[] {0}, StandardOpenOption.CREATE_NEW); show("again", again); } catch (Throwable _t) { System.out.println("again -> " + _t); }
        }
        {
            Path file = Files.createTempFile("report", ".bin");
            byte[] data = {10, 20, 30, 40, 50};

            try (FileOutputStream out = new FileOutputStream(file.toFile())) {
                out.write(data);
                out.write(data, 1, 2);
            }
            String content = Arrays.toString(Files.readAllBytes(file));   // [10, 20, 30, 40, 50, 20, 30]
            show("content", content);
        }
        {
            Path uploads = Files.createTempDirectory("uploads");
            Path avatar = uploads.resolve("users/42/avatar.png");
            byte[] image = {(byte) 0x89, 'P', 'N', 'G'};

            try { Path failed = Files.write(avatar, image); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }

            Files.createDirectories(avatar.getParent());
            Files.write(avatar, image);
            long size = Files.size(avatar);             // 4
            show("size", size);
        }
        {
            Path backup = Files.createTempDirectory("backups").resolve("db.bak");
            byte[] block = new byte[1000];

            try (InputStream in = new ByteArrayInputStream(block)) {
                long copied = Files.copy(in, backup);
            }
            long size = Files.size(backup);   // 1000
            show("size", size);

            try (InputStream in = new ByteArrayInputStream(block);
            OutputStream out = Files.newOutputStream(backup, StandardOpenOption.APPEND)) {
                long transferred = in.transferTo(out);
            }
            long total = Files.size(backup);  // 2000
            show("total", total);
        }
        {
            String word = "caf\u00e9";

            byte[] utf8 = word.getBytes(StandardCharsets.UTF_8);
            byte[] latin1 = word.getBytes(StandardCharsets.ISO_8859_1);
            int utf8Length = utf8.length;       // 5
            show("utf8Length", utf8Length);
            int latin1Length = latin1.length;   // 4
            show("latin1Length", latin1Length);
        }
        {
            Path dir = Files.createTempDirectory("cache");
            Path thumbnail = dir.resolve("thumb.png");
            byte[] image = {(byte) 0x89, 'P', 'N', 'G'};

            Path tmp = Files.createTempFile(dir, "thumb", ".tmp");
            Files.write(tmp, image);
            Files.move(tmp, thumbnail, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            boolean same = Arrays.equals(Files.readAllBytes(thumbnail), image);   // true
            show("same", same);
        }
        {
            Path dir = Files.createTempDirectory("check");
            Path expected = Files.write(dir.resolve("expected.bin"), new byte[] {1, 2, 3});
            Path actual = Files.write(dir.resolve("actual.bin"), new byte[] {1, 2, 3});
            Path broken = Files.write(dir.resolve("broken.bin"), new byte[] {1, 7, 3});

            long equal = Files.mismatch(expected, actual);       // -1
            show("equal", equal);
            long differs = Files.mismatch(expected, broken);     // 1
            show("differs", differs);
        }
        {
            Path file = Files.createTempFile("buffer", ".bin");
            ByteBuffer buffer = ByteBuffer.wrap(new byte[] {1, 2, 3, 4});

            try (FileChannel channel = FileChannel.open(file, StandardOpenOption.WRITE)) {
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
            }
            long size = Files.size(file);   // 4
            show("size", size);
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
