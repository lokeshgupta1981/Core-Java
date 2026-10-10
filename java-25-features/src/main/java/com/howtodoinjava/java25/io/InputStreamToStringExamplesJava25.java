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

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

/**
 * Examples for the tutorial "InputStream to String in Java: readAllBytes and More (Java 25)".
 * https://howtodoinjava.com/java/io/inputstream-to-string/
 */
public class InputStreamToStringExamplesJava25 {

    public static void main(String[] args) throws Exception {
        {
            InputStream in = new ByteArrayInputStream("Add 2 eggs".getBytes(StandardCharsets.UTF_8));
            String text = new String(in.readAllBytes(), StandardCharsets.UTF_8);          // "Add 2 eggs"
            show("text", text);

            InputStream in2 = new ByteArrayInputStream("Add 2 eggs".getBytes(StandardCharsets.UTF_8));
            String text2 = new InputStreamReader(in2, StandardCharsets.UTF_8).readAllAsString();   // "Add 2 eggs"
            show("text2", text2);

            InputStream back = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
            int firstByte = back.read();                                                   // 65
            show("firstByte", firstByte);
        }
        {
            Path recipe = Files.writeString(Files.createTempFile("recipe", ".txt"), "Boil water\nAdd pasta\n");

            String content;
            try (InputStream in = Files.newInputStream(recipe)) {
                content = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
            long lineCount = content.lines().count();          // 2
            show("lineCount", lineCount);
            boolean endsWithNewline = content.endsWith("\n");  // true
            show("endsWithNewline", endsWithNewline);
        }
        {
            InputStream body = new ByteArrayInputStream("{\"price\": 5}".getBytes(StandardCharsets.UTF_8));
            int limit = 1_000_000;

            byte[] bytes = body.readNBytes(limit + 1);
            if (bytes.length > limit) {
                throw new IOException("Response body larger than " + limit + " bytes");
            }
            String json = new String(bytes, StandardCharsets.UTF_8);   // "{"price": 5}"
            show("json", json);
        }
        {
            InputStream in = new ByteArrayInputStream("Stir well".getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            long copied = in.transferTo(buffer);                      // 9
            show("copied", copied);
            String text = buffer.toString(StandardCharsets.UTF_8);    // "Stir well"
            show("text", text);
        }
        {
            InputStream in = new ByteArrayInputStream("Boil water\r\nAdd pasta\r\n".getBytes(StandardCharsets.UTF_8));

            String joined;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                joined = reader.lines().collect(Collectors.joining("\n"));
            }
            int joinedLength = joined.length();            // 20
            show("joinedLength", joinedLength);
            boolean hasCarriageReturn = joined.contains("\r");   // false
            show("hasCarriageReturn", hasCarriageReturn);
        }
        {
            InputStream in = new ByteArrayInputStream("Boil water\r\nAdd pasta\r\n".getBytes(StandardCharsets.UTF_8));

            String exact;
            try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                exact = reader.readAllAsString();
            }
            int exactLength = exact.length();              // 23
            show("exactLength", exactLength);

            StringWriter writer = new StringWriter();
            long chars = new StringReader(exact).transferTo(writer);   // 23
            show("chars", chars);
        }
        {
            InputStream in = new ByteArrayInputStream("Serve hot".getBytes(StandardCharsets.UTF_8));

            String text;
            try (Scanner scanner = new Scanner(in, StandardCharsets.UTF_8).useDelimiter("\\A")) {
                text = scanner.hasNext() ? scanner.next() : "";
            }
            String result = text;   // "Serve hot"
            show("result", result);
        }
        {
            Reader reader = new StringReader("tea");

            int first = reader.read();                     // 116
            show("first", first);
            char[] rest = new char[2];
            int count = reader.read(rest);                 // 2
            show("count", count);
            int end = reader.read();                       // -1
            show("end", end);

            Reader fromOf = Reader.of("coffee");
            String all = fromOf.readAllAsString();         // "coffee"
            show("all", all);
        }
        {
            String xml = "<order><item>apple</item></order>";

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            String item = document.getElementsByTagName("item").item(0).getTextContent();   // "apple"
            show("item", item);
        }
        {
            byte[] utf8 = "caf\u00e9".getBytes(StandardCharsets.UTF_8);

            int rightLength = new String(utf8, StandardCharsets.UTF_8).length();        // 4
            show("rightLength", rightLength);
            int wrongLength = new String(utf8, StandardCharsets.ISO_8859_1).length();   // 5
            show("wrongLength", wrongLength);
        }
        {
            String manifest;
            try (InputStream in = Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("META-INF/MANIFEST.MF"), "resource missing")) {
                manifest = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
            String firstLine = manifest.lines().findFirst().orElse("");   // "Manifest-Version: 1.0"
            show("firstLine", firstLine);
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
