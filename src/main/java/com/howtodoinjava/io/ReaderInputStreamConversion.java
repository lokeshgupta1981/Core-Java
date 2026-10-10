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
 * Examples for the tutorial "Convert Reader to InputStream (and Back) in Java".
 * https://howtodoinjava.com/java/io/convert-reader-inputstream/
 */
public class ReaderInputStreamConversion {

    public static void main(String[] args) throws Exception {
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
