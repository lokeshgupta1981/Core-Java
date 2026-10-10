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
 * Examples for the tutorial "InputStream to OutputStream in Java: transferTo and Files.copy".
 * https://howtodoinjava.com/java/io/inputstream-to-outputstream/
 */
public class InputStreamToOutputStreamExamples {
    static long copyWithLimit(InputStream in, OutputStream out, long maxBytes) throws IOException {
        byte[] buffer = new byte[8192];
        long total = 0;
        int read;
        while ((read = in.read(buffer)) != -1) {
            total += read;
            if (total > maxBytes) {
                throw new IOException("Upload larger than " + maxBytes + " bytes");
            }
            out.write(buffer, 0, read);
        }
        return total;
    }
    public static void main(String[] args) throws Exception {
        {
            InputStream in = new ByteArrayInputStream(new byte[] {(byte) 0x89, 'P', 'N', 'G'});
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            long copied = in.transferTo(out);          // 4
            show("copied", copied);
            byte[] result = out.toByteArray();
            int firstByte = result[0] & 0xFF;          // 137
            show("firstByte", firstByte);
        }
        {
            Path invoice = Files.write(Files.createTempFile("invoice", ".pdf"), new byte[20_000]);
            Path download = Files.createTempFile("download", ".pdf");

            long bytes;
            try (InputStream in = Files.newInputStream(invoice);
            OutputStream out = Files.newOutputStream(download)) {
                bytes = in.transferTo(out);
            }
            long copiedBytes = bytes;                   // 20000
            show("copiedBytes", copiedBytes);
            long targetSize = Files.size(download);    // 20000
            show("targetSize", targetSize);
        }
        {
            InputStream small = new ByteArrayInputStream(new byte[3_000]);
            long copied = copyWithLimit(small, OutputStream.nullOutputStream(), 5_000);      // 3000
            show("copied", copied);

            InputStream big = new ByteArrayInputStream(new byte[9_000]);
            try { long rejected = copyWithLimit(big, OutputStream.nullOutputStream(), 5_000); show("rejected", rejected); } catch (Throwable _t) { System.out.println("rejected -> " + _t); }
        }
        {
            Path target = Files.createTempDirectory("uploads").resolve("photo.png");
            InputStream upload = new ByteArrayInputStream(new byte[500]);
            long saved = Files.copy(upload, target);                                  // 500
            show("saved", saved);

            InputStream again = new ByteArrayInputStream(new byte[700]);
            try { long failed = Files.copy(again, target); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }

            InputStream replacement = new ByteArrayInputStream(new byte[700]);
            long replaced = Files.copy(replacement, target, StandardCopyOption.REPLACE_EXISTING);   // 700
            show("replaced", replaced);

            ByteArrayOutputStream response = new ByteArrayOutputStream();
            long sent = Files.copy(target, response);                                 // 700
            show("sent", sent);
        }
        {
            InputStream in = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5});
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            out.write(in.readNBytes(3));
            int size = out.size();   // 3
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
