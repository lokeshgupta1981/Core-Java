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

/**
 * Examples for the tutorial "Convert Reader to InputStream (and Back) in Java".
 * https://howtodoinjava.com/java/io/convert-reader-inputstream/
 */
public class ReaderInputStreamConversionJava25 {

    public static void main(String[] args) throws Exception {
        {
            Reader reader = new StringReader("apple,5");
            String text = reader.readAllAsString();                                         // "apple,5"
            show("text", text);
            InputStream in = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
            int byteCount = in.available();                                                 // 7
            show("byteCount", byteCount);

            Reader back = new InputStreamReader(in, StandardCharsets.UTF_8);
            String again = back.readAllAsString();                                          // "apple,5"
            show("again", again);
        }
        {
            Reader reader = new StringReader("banana,3");
            String text = reader.readAllAsString();                         // Java 25: "banana,3"
            show("text", text);

            Reader reader10 = new StringReader("banana,3");
            StringWriter writer = new StringWriter();
            long copied = reader10.transferTo(writer);                      // Java 10: 8
            show("copied", copied);

            InputStream in = new ByteArrayInputStream(writer.toString().getBytes(StandardCharsets.UTF_8));
            String check = new String(in.readAllBytes(), StandardCharsets.UTF_8);   // "banana,3"
            show("check", check);
        }
        {
            Reader report = new StringReader("apple,5\nbanana,3\n");
            ByteArrayOutputStream target = new ByteArrayOutputStream();

            try (Writer encoder = new OutputStreamWriter(target, StandardCharsets.UTF_8)) {
                report.transferTo(encoder);
            }
            int written = target.size();   // 17
            show("written", written);
        }
        {
            InputStream in = new ByteArrayInputStream("apple,5\nbanana,3\n".getBytes(StandardCharsets.UTF_8));

            List<String> rows;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                rows = reader.lines().toList();
            }
            List<String> result = rows;   // [apple,5, banana,3]
            show("result", result);
        }
        {
            Reader menu = new StringReader("caf\u00e9");
            InputStream in = new ByteArrayInputStream(menu.readAllAsString().getBytes(StandardCharsets.UTF_8));
            int bytes = in.available();                                            // 5
            show("bytes", bytes);

            Reader wrong = new InputStreamReader(in, StandardCharsets.ISO_8859_1);
            String decoded = wrong.readAllAsString();
            int decodedLength = decoded.length();                                  // 5
            show("decodedLength", decodedLength);
            boolean same = decoded.equals("caf\u00e9");                            // false
            show("same", same);
        }
        {
            Reader fromString = new StringReader("pear,7");
            Reader fromOf = Reader.of(new StringBuilder("pear,7"));
            String text = fromOf.readAllAsString();   // "pear,7"
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
