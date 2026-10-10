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
 * Examples for the tutorial "Java InputStreamReader: Decode Bytes to Text With a Charset".
 * https://howtodoinjava.com/java/io/java-inputstreamreader/
 */
public class InputStreamReaderGuideJava25 {

    public static void main(String[] args) throws Exception {
        {
            byte[] bytes = "tea,3\ncaf\u00e9,4\n".getBytes(StandardCharsets.UTF_8);

            Reader reader = new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8);
            int first = reader.read();                          // 116, the character 't'
            show("first", first);
            int restLength = reader.readAllAsString().length();   // 12, read with Java 25 readAllAsString()
            show("restLength", restLength);

            BufferedReader lines = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8));
            String firstLine = lines.readLine();                // "tea,3"
            show("firstLine", firstLine);
        }
        {
            byte[] export = "caf\u00e9".getBytes(Charset.forName("windows-1252"));
            int exportSize = export.length;                                           // 4, the last byte is 0xE9
            show("exportSize", exportSize);

            Reader right = new InputStreamReader(new ByteArrayInputStream(export), Charset.forName("windows-1252"));
            boolean correct = right.readAllAsString().equals("caf\u00e9");           // true
            show("correct", correct);

            Reader wrong = new InputStreamReader(new ByteArrayInputStream(export), StandardCharsets.UTF_8);
            String garbled = wrong.readAllAsString();
            boolean replaced = garbled.endsWith("\ufffd");                           // true
            show("replaced", replaced);
        }
        {
            byte[] export = "caf\u00e9".getBytes(Charset.forName("windows-1252"));

            CharsetDecoder strict = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT);
            Reader reader = new InputStreamReader(new ByteArrayInputStream(export), strict);
            try { String text = reader.readAllAsString(); show("text", text); } catch (Throwable _t) { System.out.println("text -> " + _t); }
        }
        {
            byte[] bytes = "apple,5".getBytes(StandardCharsets.UTF_8);

            Reader single = new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8);
            int ch = single.read();                                  // 97
            show("ch", ch);

            Reader bulk = new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8);
            char[] buffer = new char[4];
            int count = bulk.read(buffer);                           // 4
            show("count", count);
            String chunk = new String(buffer, 0, count);             // "appl"
            show("chunk", chunk);

            Reader all = new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8);
            StringWriter writer = new StringWriter();
            long copied = all.transferTo(writer);                    // 7
            show("copied", copied);
        }
        {
            Path menu = Files.writeString(Files.createTempFile("menu", ".csv"), "tea,3\ncoffee,4\n");

            List<String> items;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(menu), StandardCharsets.UTF_8))) {
                items = reader.lines().map(line -> line.split(",")[0]).toList();
            }
            List<String> result = items;   // [tea, coffee]
            show("result", result);
        }
        {
            Reader reader = new InputStreamReader(InputStream.nullInputStream(), StandardCharsets.UTF_8);
            String open = ((InputStreamReader) reader).getEncoding();     // "UTF8"
            show("open", open);
            reader.close();
            String closed = ((InputStreamReader) reader).getEncoding();   // null
            show("closed", closed);
        }
        {
            InputStream fakeConsole = new ByteArrayInputStream("Lokesh\n".getBytes(StandardCharsets.UTF_8));
            BufferedReader console = new BufferedReader(new InputStreamReader(fakeConsole, StandardCharsets.UTF_8));
            String name = console.readLine();   // "Lokesh"
            show("name", name);
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
