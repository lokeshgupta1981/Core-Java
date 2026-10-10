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
 * Examples for the tutorial "Java NIO Channel Tutorial: FileChannel, Sockets, Scatter/Gather".
 * https://howtodoinjava.com/java/nio/java-nio-2-0-channels/
 */
public class NioChannels {
    static SocketChannel connect(String host, int port) throws IOException {
        return SocketChannel.open(new InetSocketAddress(host, port));
    }
    static long copy(ReadableByteChannel src, WritableByteChannel dest) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
        long total = 0;
        while (src.read(buffer) != -1) {
            buffer.flip();                                  // switch from filling to draining
            while (buffer.hasRemaining()) {
                total += dest.write(buffer);
            }
            buffer.clear();                                 // ready for the next read
        }
        return total;
    }
    static String tail(Path log, int bytes) throws IOException {
        try (FileChannel channel = FileChannel.open(log)) {
            long start = Math.max(0, channel.size() - bytes);
            ByteBuffer buffer = ByteBuffer.allocate(bytes);
            while (buffer.hasRemaining() && channel.read(buffer, start + buffer.position()) != -1) {
            }
            return new String(buffer.array(), 0, buffer.position(), StandardCharsets.UTF_8);
        }
    }
    static void writeTrack(Path file, int trackId, int seconds, String title) throws IOException {
        ByteBuffer header = ByteBuffer.allocate(8).putInt(trackId).putInt(seconds).flip();
        ByteBuffer body = ByteBuffer.wrap(title.getBytes(StandardCharsets.UTF_8));
        ByteBuffer[] parts = {header, body};
        try (FileChannel channel = FileChannel.open(file, StandardOpenOption.WRITE,
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            while (body.hasRemaining()) {
                channel.write(parts);                       // gathering write
            }
        }
    }
    static String readTrack(Path file) throws IOException {
        ByteBuffer header = ByteBuffer.allocate(8);
        ByteBuffer body = ByteBuffer.allocate(64);
        ByteBuffer[] parts = {header, body};
        try (FileChannel channel = FileChannel.open(file)) {
            while (channel.read(parts) > 0) {               // scattering read
            }
        }
        header.flip();
        body.flip();
        int trackId = header.getInt();
        int seconds = header.getInt();
        return trackId + " " + seconds + "s " + StandardCharsets.UTF_8.decode(body);
    }
    public static void main(String[] args) throws Exception {
        {
            Path file = Files.createTempFile("fruits", ".txt");
            try (FileChannel channel = FileChannel.open(file, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
                int written = channel.write(ByteBuffer.wrap("apple=5".getBytes(StandardCharsets.UTF_8)));   // 7
                long size = channel.size();                         // 7
                ByteBuffer buffer = ByteBuffer.allocate(16);
                int read = channel.read(buffer, 0);                 // 7
                String text = new String(buffer.array(), 0, buffer.position(), StandardCharsets.UTF_8);   // "apple=5"
            }
        }
        {
            Path file = Files.createTempFile("notes", ".txt");
            try (FileChannel reader = FileChannel.open(file);                                   // READ is the default
            FileChannel appender = FileChannel.open(file, StandardOpenOption.APPEND);      // writes go to the end
            SeekableByteChannel any = Files.newByteChannel(file, StandardOpenOption.READ)) {
                boolean open = reader.isOpen();                     // true
                long position = appender.position();                // 0
            }
        }
        {
            Path file = Files.createTempFile("legacy", ".txt");
            try (FileInputStream in = new FileInputStream(file.toFile());
            FileChannel fromStream = in.getChannel();
            ReadableByteChannel adapted = Channels.newChannel(new ByteArrayInputStream(new byte[3]))) {
                long size = fromStream.size();                      // 0
                int read = adapted.read(ByteBuffer.allocate(8));    // 3
            }
        }
        {
            try (ServerSocketChannel server = ServerSocketChannel.open().bind(new InetSocketAddress("localhost", 0));
            DatagramChannel udp = DatagramChannel.open()) {
                boolean bound = server.getLocalAddress() != null;   // true
                boolean blocking = udp.isBlocking();                // true
            }
        }
        {
            Path source = Files.writeString(Files.createTempFile("menu", ".txt"), "soup,salad,pie");
            Path target = Files.createTempFile("menu-copy", ".txt");
            try (FileChannel in = FileChannel.open(source);
            FileChannel out = FileChannel.open(target, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
                long copied = copy(in, out);                        // 14
            }
            String copy = Files.readString(target);                 // "soup,salad,pie"
            show("copy", copy);
        }
        {
            Path log = Files.writeString(Files.createTempFile("access", ".log"), "GET /a\nGET /b\nGET /c\n");
            String lastLine = tail(log, 7);                         // "GET /c\n"
            show("lastLine", lastLine);
        }
        {
            Path file = Files.writeString(Files.createTempFile("prices", ".txt"), "tea=3");
            try (FileChannel readOnly = FileChannel.open(file, StandardOpenOption.READ)) {
                try { int written = readOnly.write(ByteBuffer.wrap(new byte[]{1}));  } catch (Throwable _t) { System.out.println("-> " + _t); }
            }
        }
        {
            try (ServerSocketChannel server = ServerSocketChannel.open().bind(new InetSocketAddress("localhost", 0));
            Selector selector = Selector.open()) {
                server.configureBlocking(false);
                SelectionKey key = server.register(selector, SelectionKey.OP_ACCEPT);
                SocketChannel client = server.accept();             // null, no client is waiting
                int ready = selector.selectNow();                   // 0
            }
        }
        {
            Path file = Files.createTempFile("track", ".bin");
            writeTrack(file, 7, 215, "Blue Train");
            long size = Files.size(file);                           // 18
            show("size", size);
            String track = readTrack(file);                         // "7 215s Blue Train"
            show("track", track);
        }
        {
            Path file = Files.createTempFile("closed", ".txt");
            FileChannel channel = FileChannel.open(file, StandardOpenOption.WRITE);
            channel.close();
            channel.close();                                        // no effect
            boolean open = channel.isOpen();                        // false
            show("open", open);
            try { int written = channel.write(ByteBuffer.allocate(1)); show("written", written); } catch (Throwable _t) { System.out.println("written -> " + _t); }
        }
        {
            Path file = Files.writeString(Files.createTempFile("report", ".csv"), "q1,q2");
            try (FileChannel channel = FileChannel.open(file)) {
                Thread.currentThread().interrupt();
                try { int read = channel.read(ByteBuffer.allocate(8));  } catch (Throwable _t) { System.out.println("-> " + _t); }
            } finally {
                boolean cleared = Thread.interrupted();             // clears the flag again
            }
        }
        {
            Path file = Files.writeString(Files.createTempFile("shared", ".txt"), "abc");
            FileInputStream in = new FileInputStream(file.toFile());
            FileChannel channel = in.getChannel();
            in.close();
            boolean open = channel.isOpen();                        // false
            show("open", open);
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
