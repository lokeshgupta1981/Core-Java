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

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Examples for the tutorial "Unzip a File in Java with Subdirectories (Zip Slip Safe)".
 * https://howtodoinjava.com/java/io/unzip-file-with-subdirectories/
 */
public class UnzipWithSubdirectories {
    static Path sampleZip() throws IOException {
        Path zip = Files.createTempDirectory("zips").resolve("data.zip");
        try (ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(zip))) {
            out.putNextEntry(new ZipEntry("docs/"));
            out.closeEntry();
            out.putNextEntry(new ZipEntry("docs/readme.txt"));
            out.write("Read me first".getBytes(StandardCharsets.UTF_8));
            out.closeEntry();
            out.putNextEntry(new ZipEntry("docs/img/logo.png"));
            out.write(new byte[] {1, 2, 3});
            out.closeEntry();
        }
        return zip;
    }
    static void unzip(Path zip, Path targetDir) throws IOException {
        Path target = targetDir.toAbsolutePath().normalize();
        Files.createDirectories(target);
        try (ZipFile zipFile = new ZipFile(zip.toFile())) {
            for (ZipEntry entry : Collections.list(zipFile.entries())) {
                Path out = safeResolve(target, entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(out);
                } else {
                    Files.createDirectories(out.getParent());
                    try (InputStream in = zipFile.getInputStream(entry)) {
                        Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }
    }
    static Path safeResolve(Path target, String entryName) throws IOException {
        Path out = target.resolve(entryName).normalize();
        if (!out.startsWith(target)) {
            throw new IOException("Entry outside target folder: " + entryName);
        }
        return out;
    }
    static List<String> unzipStream(InputStream source, Path targetDir) throws IOException {
        Path target = targetDir.toAbsolutePath().normalize();
        List<String> written = new ArrayList<>();
        try (ZipInputStream zin = new ZipInputStream(source)) {
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                Path out = safeResolve(target, entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(out);
                } else {
                    Files.createDirectories(out.getParent());
                    Files.copy(zin, out, StandardCopyOption.REPLACE_EXISTING);
                    written.add(entry.getName());
                }
            }
        }
        return written;
    }
    static long copyLimited(InputStream in, Path out, long maxBytes) throws IOException {
        long total = 0;
        byte[] buffer = new byte[8_192];
        try (OutputStream os = Files.newOutputStream(out)) {
            int n;
            while ((n = in.read(buffer)) != -1) {
                total += n;
                if (total > maxBytes) {
                    throw new IOException("Entry larger than " + maxBytes + " bytes: " + out.getFileName());
                }
                os.write(buffer, 0, n);
            }
        }
        return total;
    }
    public static void main(String[] args) throws Exception {
        {
            Path zip = sampleZip();
            Path target = Files.createTempDirectory("data");
            try (ZipFile zipFile = new ZipFile(zip.toFile())) {
                for (ZipEntry entry : Collections.list(zipFile.entries())) {
                    Path out = target.resolve(entry.getName()).normalize();
                    if (!out.startsWith(target)) {
                        throw new IOException("Entry outside target folder: " + entry.getName());
                    }
                    if (entry.isDirectory()) {
                        Files.createDirectories(out);
                    } else {
                        Files.createDirectories(out.getParent());
                        try (InputStream in = zipFile.getInputStream(entry)) {
                            Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
            }
            boolean nested = Files.exists(target.resolve("docs/img/logo.png"));     // true
            show("nested", nested);
            String text = Files.readString(target.resolve("docs/readme.txt"));     // "Read me first"
            show("text", text);
        }
        {
            Path zip = sampleZip();
            String fileName = zip.getFileName().toString();
            Path target = zip.resolveSibling(fileName.substring(0, fileName.lastIndexOf('.')));
            unzip(zip, target);
            String folder = target.getFileName().toString();                      // "data"
            show("folder", folder);
            long files;
            try (Stream<Path> s = Files.walk(target)) {
                files = s.filter(Files::isRegularFile).count();
            }
            long extracted = files;                                               // 2
            show("extracted", extracted);
        }
        {
            Path target = Files.createTempDirectory("uploads").toAbsolutePath().normalize();
            Path ok = safeResolve(target, "docs/readme.txt");
            boolean inside = ok.startsWith(target);                                 // true
            show("inside", inside);
            try { Path evil = safeResolve(target, "../config.yaml"); show("evil", evil); } catch (Throwable _t) { System.out.println("evil -> " + _t); }
        }
        {
            boolean asString = "/srv/out2/x".startsWith("/srv/out");                       // true
            show("asString", asString);
            boolean asPath = Path.of("/srv/out2/x").startsWith(Path.of("/srv/out"));       // false
            show("asPath", asPath);
        }
        {
            byte[] upload = Files.readAllBytes(sampleZip());
            Path themeDir = Files.createTempDirectory("theme");
            List<String> files = unzipStream(new ByteArrayInputStream(upload), themeDir);   // [docs/readme.txt, docs/img/logo.png]
            show("files", files);
        }
        {
            byte[] big = new byte[50_000];
            Path out = Files.createTempFile("entry", ".bin");
            long small = copyLimited(new ByteArrayInputStream(new byte[100]), out, 10_000);   // 100
            show("small", small);
            try { long bomb = copyLimited(new ByteArrayInputStream(big), out, 10_000); show("bomb", bomb); } catch (Throwable _t) { System.out.println("bomb -> " + _t); }
        }
        {
            Path zip = sampleZip();
            String readme;
            List<String> entries;
            try (FileSystem zipFs = FileSystems.newFileSystem(zip)) {
                readme = Files.readString(zipFs.getPath("docs/readme.txt"));
                try (Stream<Path> s = Files.walk(zipFs.getPath("/"))) {
                    entries = s.filter(Files::isRegularFile).map(Path::toString).sorted().toList();
                }
            }
            String content = readme;                                             // "Read me first"
            show("content", content);
            String all = entries.toString();                                     // "[/docs/img/logo.png, /docs/readme.txt]"
            show("all", all);
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
