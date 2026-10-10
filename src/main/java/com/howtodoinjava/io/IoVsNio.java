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
 * Examples for the tutorial "Java IO vs NIO: Differences and When to Use Each (Java 25)".
 * https://howtodoinjava.com/java/io/difference-between-io-nio/
 */
public class IoVsNio {

    public static void main(String[] args) throws Exception {
        {
            Path file = Files.writeString(Files.createTempFile("songs", ".txt"), "Intro\nOutro\n");
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile(), StandardCharsets.UTF_8))) {
                String first = reader.readLine();                   // "Intro", java.io stream
            }
            List<String> lines = Files.readAllLines(file);          // [Intro, Outro], NIO.2
            show("lines", lines);
            try (FileChannel channel = FileChannel.open(file)) {
                ByteBuffer buffer = ByteBuffer.allocate(64);
                int read = channel.read(buffer);                    // 12, NIO channel and buffer
            }
        }
        {
            byte[] record = ByteBuffer.allocate(8).putInt(7).putInt(215).array();
            try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(record))) {
                int track = in.readInt();                           // 7, stream reads in order
            }
            ByteBuffer buffer = ByteBuffer.wrap(record);
            int seconds = buffer.getInt(4);                         // 215, buffer reads any offset
            show("seconds", seconds);
            int track = buffer.getInt(0);                           // 7
            show("track", track);
        }
        {
            try (Selector selector = Selector.open();
            ServerSocketChannel server = ServerSocketChannel.open().bind(new InetSocketAddress("localhost", 0))) {
                server.configureBlocking(false);
                server.register(selector, SelectionKey.OP_ACCEPT);
                int ready = selector.selectNow();                   // 0, no client yet
                int watched = selector.keys().size();               // 1
            }
        }
        {
            Path missing = Files.createTempDirectory("io").resolve("gone.txt");
            boolean deleted = missing.toFile().delete();            // false, no reason given
            show("deleted", deleted);
            try { Files.delete(missing);  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            Path dir = Files.createTempDirectory("playlist");
            Path songs = Files.writeString(dir.resolve("songs.txt"), "Intro\nOutro\n");
            long count = Files.readAllLines(songs).size();          // 2
            show("count", count);
            Path backup = Files.copy(songs, dir.resolve("songs.bak"));
            boolean exists = Files.exists(backup);                  // true
            show("exists", exists);
            File legacy = songs.toFile();                           // interop with older APIs
            show("legacy", legacy);
            Path back = legacy.toPath();
        }
        {
            Path file = Files.writeString(Files.createTempFile("customer", ".json"), "{\"id\":1}");
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                List<Future<Integer>> results = new ArrayList<>();
                for (int i = 0; i < 1_000; i++) {
                    results.add(executor.submit(() -> Files.readString(file).length()));
                }
                int total = 0;
                for (Future<Integer> result : results) {
                    total += result.get();
                }
                int sum = total;                                    // 8000
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
