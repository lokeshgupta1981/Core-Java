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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Examples for the tutorial "Java NIO Read File With FileChannel and ByteBuffer".
 * https://howtodoinjava.com/java/nio/nio-read-file/
 */
public class NioReadFile {
    static byte[] readSmall(Path file) throws IOException {
        try (FileChannel channel = FileChannel.open(file, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(Math.toIntExact(channel.size()));
            while (buffer.hasRemaining()) {
                if (channel.read(buffer) == -1) {
                    break;                                  // the file shrank while reading
                }
            }
            return buffer.array();
        }
    }
    static String sha256(Path file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        ByteBuffer buffer = ByteBuffer.allocateDirect(64 * 1024);
        try (FileChannel channel = FileChannel.open(file)) {
            while (channel.read(buffer) != -1) {
                buffer.flip();
                digest.update(buffer);                      // consumes the remaining bytes
                buffer.clear();
            }
        }
        return HexFormat.of().formatHex(digest.digest());
    }
    static String readText(Path file, int bufferSize) throws IOException {
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPLACE)
                .onUnmappableCharacter(CodingErrorAction.REPLACE);
        ByteBuffer bytes = ByteBuffer.allocate(bufferSize);
        CharBuffer chars = CharBuffer.allocate(bufferSize);
        StringBuilder text = new StringBuilder();
        try (FileChannel channel = FileChannel.open(file)) {
            boolean end = false;
            while (!end) {
                end = channel.read(bytes) == -1;
                bytes.flip();
                decoder.decode(bytes, chars, end);          // keeps an incomplete character
                text.append(chars.flip());
                chars.clear();
                bytes.compact();
            }
            decoder.flush(chars);
            text.append(chars.flip());
        }
        return text.toString();
    }
    static String readMapped(Path file) throws IOException {
        try (FileChannel channel = FileChannel.open(file)) {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            return StandardCharsets.UTF_8.decode(buffer).toString();
        }
    }
    static boolean isPng(Path file) throws IOException {
        byte[] signature = {(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n'};
        ByteBuffer header = ByteBuffer.allocate(signature.length);
        try (FileChannel channel = FileChannel.open(file)) {
            while (header.hasRemaining() && channel.read(header, header.position()) != -1) {
            }
        }
        return header.flip().equals(ByteBuffer.wrap(signature));
    }
    public static void main(String[] args) throws Exception {
        {
            Path file = Files.writeString(Files.createTempFile("recipe", ".txt"), "flour\nsugar\n");
            String all = Files.readString(file);                    // "flour\nsugar\n"
            show("all", all);
            try (FileChannel channel = FileChannel.open(file)) {
                ByteBuffer buffer = ByteBuffer.allocate(Math.toIntExact(channel.size()));
                while (buffer.hasRemaining() && channel.read(buffer) != -1) {
                }
                String viaChannel = new String(buffer.array(), StandardCharsets.UTF_8);   // "flour\nsugar\n"
            }
        }
        {
            Path file = Files.write(Files.createTempFile("logo", ".bin"), new byte[]{1, 2, 3, 4});
            byte[] bytes = readSmall(file);
            int count = bytes.length;                               // 4
            show("count", count);
        }
        {
            long hugeSize = 3L * 1024 * 1024 * 1024;
            int wrong = (int) hugeSize;                             // -1073741824
            show("wrong", wrong);
            try { int checked = Math.toIntExact(hugeSize); show("checked", checked); } catch (Throwable _t) { System.out.println("checked -> " + _t); }
        }
        {
            Path upload = Files.writeString(Files.createTempFile("upload", ".txt"), "abc");
            String hash = sha256(upload);                           // "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad"
            show("hash", hash);
        }
        {
            Path file = Files.writeString(Files.createTempFile("menu", ".txt"), "caf\u00e9 au lait");
            StringBuilder broken = new StringBuilder();
            try (FileChannel channel = FileChannel.open(file)) {
                ByteBuffer buffer = ByteBuffer.allocate(4);         // ends inside the 2-byte e-acute
                while (channel.read(buffer) != -1) {
                    broken.append(new String(buffer.array(), 0, buffer.position(), StandardCharsets.UTF_8));
                    buffer.clear();
                }
            }
            boolean garbled = broken.toString().contains("\uFFFD");   // true, replacement characters
            show("garbled", garbled);
        }
        {
            Path file = Files.writeString(Files.createTempFile("menu", ".txt"), "caf\u00e9 au lait");
            String text = readText(file, 4);
            boolean correct = text.equals("caf\u00e9 au lait");     // true
            show("correct", correct);
        }
        {
            Path file = Files.writeString(Files.createTempFile("notes", ".txt"), "eggs\nmilk\n");
            String notes = readMapped(file);                        // "eggs\nmilk\n"
            show("notes", notes);
        }
        {
            Path image = Files.write(Files.createTempFile("avatar", ".png"),
            new byte[]{(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n', 0, 0});
            Path fake = Files.writeString(Files.createTempFile("fake", ".png"), "not an image");
            boolean real = isPng(image);                            // true
            show("real", real);
            boolean notReal = isPng(fake);                          // false
            show("notReal", notReal);
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
