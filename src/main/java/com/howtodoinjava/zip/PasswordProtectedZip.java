package com.howtodoinjava.zip;

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
 * Examples for the tutorial "Password Protected Zip in Java with Zip4j (AES-256)".
 * https://howtodoinjava.com/java/io/create-password-protected-zip/
 */
public class PasswordProtectedZip {
    static Path reportsFolder() throws IOException {
        Path dir = Files.createTempDirectory("reports");
        Files.writeString(dir.resolve("q1.csv"), "month,total\njan,100\n");
        Files.writeString(dir.resolve("q2.csv"), "month,total\napr,140\n");
        Files.createDirectories(dir.resolve("receipts"));
        Files.writeString(dir.resolve("receipts/r-001.txt"), "paid 40");
        return dir;
    }
    public static void main(String[] args) throws Exception {
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
