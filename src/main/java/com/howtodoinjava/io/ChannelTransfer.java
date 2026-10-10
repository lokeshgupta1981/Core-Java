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
 * Examples for the tutorial "Java FileChannel transferTo() and transferFrom() Examples".
 * https://howtodoinjava.com/java/nio/transfer-data-between-channels/
 */
public class ChannelTransfer {
    static long copyWithTransferFrom(Path source, Path target) throws IOException {
        try (FileChannel in = FileChannel.open(source, StandardOpenOption.READ);
        FileChannel out = FileChannel.open(target, StandardOpenOption.WRITE,
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            long size = in.size();
            long position = 0;
            while (position < size) {
                position += out.transferFrom(in, position, size - position);
            }
            return position;
        }
    }
    static void merge(List<Path> parts, Path target) throws IOException {
        try (FileChannel out = FileChannel.open(target, StandardOpenOption.WRITE,
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Path part : parts) {
                try (FileChannel in = FileChannel.open(part, StandardOpenOption.READ)) {
                    long size = in.size();
                    long position = 0;
                    while (position < size) {
                        position += in.transferTo(position, size - position, out);
                    }
                }
            }
        }
    }
    static void sendFile(Path file, SocketChannel socket) throws IOException {
        try (FileChannel in = FileChannel.open(file, StandardOpenOption.READ)) {
            long size = in.size();
            long position = 0;
            while (position < size) {
                position += in.transferTo(position, size - position, socket);
            }
        }
    }
    static long save(InputStream in, Path target) throws IOException {
        try (ReadableByteChannel src = Channels.newChannel(in);
        FileChannel out = FileChannel.open(target, StandardOpenOption.WRITE,
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            long position = 0;
            long n;
            while ((n = out.transferFrom(src, position, 1024 * 1024)) > 0) {
                position += n;
            }
            return position;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path source = Files.writeString(Files.createTempFile("invoice", ".txt"), "total=120");
            Path target = Files.createTempFile("invoice-copy", ".txt");
            try (FileChannel in = FileChannel.open(source, StandardOpenOption.READ);
            FileChannel out = FileChannel.open(target, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
                long size = in.size();                              // 9
                long position = 0;
                while (position < size) {
                    position += in.transferTo(position, size - position, out);
                }
            }
            String copy = Files.readString(target);                 // "total=120"
            show("copy", copy);
        }
        {
            Path source = Files.writeString(Files.createTempFile("song", ".txt"), "verse");
            Path target = Files.writeString(Files.createTempFile("old", ".txt"), "much longer old content");
            long copied = copyWithTransferFrom(source, target);     // 5
            show("copied", copied);
            String result = Files.readString(target);               // "verse"
            show("result", result);
        }
        {
            Path dir = Files.createTempDirectory("logs");
            Path h1 = Files.writeString(dir.resolve("01.log"), "start\n");
            Path h2 = Files.writeString(dir.resolve("02.log"), "login\n");
            Path h3 = Files.writeString(dir.resolve("03.log"), "stop\n");
            Path daily = dir.resolve("daily.log");
            merge(List.of(h1, h2, h3), daily);
            String merged = Files.readString(daily);                // "start\nlogin\nstop\n"
            show("merged", merged);
        }
        {
            Path file = Files.writeString(Files.createTempFile("poem", ".txt"), "roses");
            ByteArrayOutputStream memory = new ByteArrayOutputStream();
            try (FileChannel in = FileChannel.open(file);
            WritableByteChannel target = Channels.newChannel(memory)) {
                long sent = in.transferTo(0, in.size(), target);    // 5
            }
            String received = memory.toString(StandardCharsets.UTF_8);   // "roses"
            show("received", received);
        }
        {
            Path target = Files.createTempFile("download", ".bin");
            long saved = save(new ByteArrayInputStream(new byte[3000]), target);   // 3000
            show("saved", saved);
            long size = Files.size(target);                         // 3000
            show("size", size);
        }
        {
            Path file = Files.writeString(Files.createTempFile("short", ".txt"), "abc");
            try (FileChannel in = FileChannel.open(file);
            WritableByteChannel sink = Channels.newChannel(OutputStream.nullOutputStream())) {
                long pastEnd = in.transferTo(10, 5, sink);          // 0, position is past the end
                long fewer = in.transferTo(1, 100, sink);           // 2, only 2 bytes are left
                long unchanged = in.position();                     // 0
            }
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
