package com.howtodoinjava.java25.io;

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

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

/**
 * Examples for the tutorial "Java MappedByteBuffer: Memory-Mapped Files and MemorySegment".
 * https://howtodoinjava.com/java/nio/memory-mapped-files-mappedbytebuffer/
 */
public class MemoryMappedFilesJava25 {
    static String readMapped(Path file) throws IOException {
        try (FileChannel channel = FileChannel.open(file, StandardOpenOption.READ)) {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            return StandardCharsets.UTF_8.decode(buffer).toString();
        }
    }
    static long[] readPrices(Path file, int fromMinute, int count) throws IOException {
        try (FileChannel channel = FileChannel.open(file, StandardOpenOption.READ)) {
            MappedByteBuffer prices = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            long[] result = new long[count];
            for (int i = 0; i < count; i++) {
                result[i] = prices.getLong((fromMinute + i) * Long.BYTES);
            }
            return result;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path file = Files.createTempFile("readings", ".bin");
            try (FileChannel channel = FileChannel.open(file, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
                MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
                map.putInt(0, 42);
                int value = map.getInt(0);                  // 42
                map.force();                                // write the change to the storage device
            }
            long size = Files.size(file);                   // 1024
            show("size", size);
        }
        {
            Path file = Files.write(Files.createTempFile("scores", ".bin"), new byte[16]);
            MappedByteBuffer map;
            try (FileChannel channel = FileChannel.open(file, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
                map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 16);
            }
            map.putLong(8, 99L);                            // the channel is closed, the mapping is not
            byte[] onDisk = Files.readAllBytes(file);
            long stored = ByteBuffer.wrap(onDisk).getLong(8);   // 99
            show("stored", stored);
        }
        {
            Path file = Files.writeString(Files.createTempFile("config", ".bin"), "AAAA");
            try (FileChannel channel = FileChannel.open(file, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
                MappedByteBuffer copy = channel.map(FileChannel.MapMode.PRIVATE, 0, 4);
                copy.put(0, (byte) 'B');
                char inMemory = (char) copy.get(0);         // 'B'
                MappedByteBuffer readOnly = channel.map(FileChannel.MapMode.READ_ONLY, 0, 4);
                try { MappedByteBuffer changed = (MappedByteBuffer) readOnly.put((byte) 'C');  } catch (Throwable _t) { System.out.println("-> " + _t); }
            }
            String onDisk = Files.readString(file);         // "AAAA"
            show("onDisk", onDisk);
        }
        {
            Path file = Files.writeString(Files.createTempFile("menu", ".txt"), "soup\nsalad\n");
            String text = readMapped(file);                 // "soup\nsalad\n"
            show("text", text);
        }
        {
            Path file = Files.createTempFile("prices", ".bin");
            ByteBuffer data = ByteBuffer.allocate(5 * Long.BYTES);
            for (long price = 100; price < 105; price++) {
                data.putLong(price);
            }
            Files.write(file, data.array());
            long[] window = readPrices(file, 2, 3);         // [102, 103, 104]
            show("window", window);
        }
        {
            Path file = Files.write(Files.createTempFile("ledger", ".bin"), new byte[4096]);
            try (FileChannel channel = FileChannel.open(file, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
                MappedByteBuffer ledger = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());
                ledger.put(StandardCharsets.UTF_8.encode("balance=250"));
                ledger.force(0, 11);                        // write only the first 11 bytes
            }
            String header = Files.readString(file).substring(0, 11);   // "balance=250"
            show("header", header);
        }
        {
            Path file = Files.write(Files.createTempFile("segment", ".bin"), new byte[4096]);
            try (Arena arena = Arena.ofConfined();
            FileChannel channel = FileChannel.open(file, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
                MemorySegment segment = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size(), arena);
                segment.set(ValueLayout.JAVA_LONG, 8, 123L);
                long value = segment.get(ValueLayout.JAVA_LONG, 8);   // 123
                long bytes = segment.byteSize();            // 4096
                segment.force();
            }
        }
        {
            Path file = Files.write(Files.createTempFile("closed", ".bin"), new byte[64]);
            MemorySegment escaped;
            try (Arena arena = Arena.ofConfined();
            FileChannel channel = FileChannel.open(file, StandardOpenOption.READ)) {
                escaped = channel.map(FileChannel.MapMode.READ_ONLY, 0, 64, arena);
            }
            try { byte first = escaped.get(ValueLayout.JAVA_BYTE, 0); show("first", first); } catch (Throwable _t) { System.out.println("first -> " + _t); }
        }
        {
            Path file = Files.createTempFile("huge", ".bin");
            try (RandomAccessFile raf = new RandomAccessFile(file.toFile(), "rw")) {
                raf.setLength(3L * 1024 * 1024 * 1024);     // sparse 3 GB file
            }
            try (Arena arena = Arena.ofConfined();
            FileChannel channel = FileChannel.open(file, StandardOpenOption.READ)) {
                long mapped = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size(), arena).byteSize();   // 3221225472
                try { MappedByteBuffer tooBig = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());  } catch (Throwable _t) { System.out.println("-> " + _t); }
            } finally {
                Files.delete(file);
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
