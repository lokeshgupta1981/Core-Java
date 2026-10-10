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
 * Examples for the tutorial "OutputStream to InputStream in Java: Byte Array or Piped Streams".
 * https://howtodoinjava.com/java/io/outputstream-to-inputstream/
 */
public class OutputStreamToInputStream {
    static void writeReport(OutputStream out) throws IOException {
        Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        writer.write("customer,amount\n");
        writer.write("Lokesh,120\n");
        writer.flush();
    }
    public static void main(String[] args) throws Exception {
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write("apple,5\nbanana,3\n".getBytes(StandardCharsets.UTF_8));

            InputStream in = new ByteArrayInputStream(out.toByteArray());
            byte[] firstLine = in.readNBytes(7);
            String head = new String(firstLine, StandardCharsets.UTF_8);   // "apple,5"
            show("head", head);
        }
        {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            writeReport(buffer);
            int size = buffer.size();                                      // 27
            show("size", size);

            InputStream upload = new ByteArrayInputStream(buffer.toByteArray());
            long uploaded = upload.transferTo(OutputStream.nullOutputStream());   // 27
            show("uploaded", uploaded);
            String asText = buffer.toString(StandardCharsets.UTF_8).lines().findFirst().orElseThrow();   // "customer,amount"
            show("asText", asText);
        }
        {
            PipedInputStream in = new PipedInputStream(64 * 1024);
            PipedOutputStream out = new PipedOutputStream(in);

            Thread producer = Thread.ofVirtual().start(() -> {
                try (out) {
                    writeReport(out);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });

            String received;
            try (in) {
                received = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
            producer.join();
            long lines = received.lines().count();   // 2
            show("lines", lines);
        }
        {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);

            Thread crashed = Thread.ofVirtual().start(() -> {
                try {
                    out.write('A');   // no close(), the thread ends
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
            crashed.join();

            int first = in.read();                  // 65
            show("first", first);
            try { int next = in.read(); show("next", next); } catch (Throwable _t) { System.out.println("next -> " + _t); }
        }
        {
            Path temp = Files.createTempFile("report", ".csv");
            try (OutputStream out = Files.newOutputStream(temp)) {
                writeReport(out);
            }

            long size;
            try (InputStream in = Files.newInputStream(temp, StandardOpenOption.DELETE_ON_CLOSE)) {
                size = in.transferTo(OutputStream.nullOutputStream());
            }
            long fileSize = size;                    // 27
            show("fileSize", fileSize);
            boolean stillThere = Files.exists(temp); // false
            show("stillThere", stillThere);
        }
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write("pear,7".getBytes(StandardCharsets.UTF_8));
            String text = out.toString(StandardCharsets.UTF_8);   // "pear,7"
            show("text", text);
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
