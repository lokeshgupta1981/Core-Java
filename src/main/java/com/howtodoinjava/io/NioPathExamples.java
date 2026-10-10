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
 * Examples for the tutorial "Java NIO Path: Path.of(), resolve() and relativize() Examples".
 * https://howtodoinjava.com/java/nio/how-to-define-path-in-java-nio/
 */
public class NioPathExamples {
    static String fileExtension(Path path) {
        String name = path.getFileName().toString();
        int dot = name.lastIndexOf('.');
        return dot > 0 ? name.substring(dot + 1) : "";
    }
    public static void main(String[] args) throws Exception {
        {
            Path photo = Path.of("/home/lokesh/photos/2026/beach.jpg");
            Path fileName = photo.getFileName();                        // beach.jpg
            show("fileName", fileName);
            Path folder = photo.getParent();                            // /home/lokesh/photos/2026
            show("folder", folder);
            int names = photo.getNameCount();                           // 5
            show("names", names);
            Path year = photo.getName(3);                               // 2026
            show("year", year);
            Path thumb = folder.resolve("thumbs").resolve(fileName);    // /home/lokesh/photos/2026/thumbs/beach.jpg
            show("thumb", thumb);
            Path next = photo.resolveSibling("lake.jpg");               // /home/lokesh/photos/2026/lake.jpg
            show("next", next);
            Path inLibrary = Path.of("/home/lokesh/photos").relativize(photo);   // 2026/beach.jpg
            show("inLibrary", inLibrary);
            Path clean = Path.of("photos/./2026/../2025/a.jpg").normalize();     // photos/2025/a.jpg
            show("clean", clean);
            boolean absolute = photo.isAbsolute();                      // true
            show("absolute", absolute);
        }
        {
            Path absolute = Path.of("/home/lokesh/photos/2026/beach.jpg");    // /home/lokesh/photos/2026/beach.jpg
            show("absolute", absolute);
            Path joined = Path.of("/home/lokesh", "photos", "2026", "beach.jpg");   // /home/lokesh/photos/2026/beach.jpg
            show("joined", joined);
            Path relative = Path.of("photos", "2026", "beach.jpg");             // photos/2026/beach.jpg
            show("relative", relative);
            Path inHome = Path.of(System.getProperty("user.home"), "photos");   // /home/lokesh/photos
            show("inHome", inHome);
            Path fromUri = Path.of(URI.create("file:///home/lokesh/photos/2026/beach.jpg"));   // /home/lokesh/photos/2026/beach.jpg
            show("fromUri", fromUri);
            Path fromFile = new File("photos/beach.jpg").toPath();              // photos/beach.jpg
            show("fromFile", fromFile);
            Path fromFs = FileSystems.getDefault().getPath("photos", "beach.jpg");   // photos/beach.jpg
            show("fromFs", fromFs);
        }
        {
            try { Path bad = Path.of("photos\u0000beach.jpg"); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            Path photo = Path.of("/home/lokesh/photos/2026/beach.jpg");
            Path root = photo.getRoot();                // /
            show("root", root);
            Path first = photo.getName(0);              // home
            show("first", first);
            Path middle = photo.subpath(2, 4);          // photos/2026
            show("middle", middle);
            Path relativeParent = Path.of("beach.jpg").getParent();   // null
            show("relativeParent", relativeParent);
            String ext = fileExtension(photo);          // "jpg"
            show("ext", ext);
        }
        {
            Path photo = Path.of("/home/lokesh/photos/2026/beach.jpg");
            boolean inPhotos = photo.startsWith("/home/lokesh/photos");    // true
            show("inPhotos", inPhotos);
            boolean partialName = photo.startsWith("/home/lok");           // false
            show("partialName", partialName);
            boolean textMatch = photo.toString().startsWith("/home/lok");  // true
            show("textMatch", textMatch);
            boolean isJpeg = photo.endsWith("beach.jpg");                  // true
            show("isJpeg", isJpeg);
            boolean suffixText = photo.endsWith("ach.jpg");                // false
            show("suffixText", suffixText);
        }
        {
            Path library = Path.of("/home/lokesh/photos");
            Path photo = library.resolve("2026/beach.jpg");                 // /home/lokesh/photos/2026/beach.jpg
            show("photo", photo);
            Path ignored = library.resolve("/tmp/upload.jpg");              // /tmp/upload.jpg
            show("ignored", ignored);
            Path renamed = photo.resolveSibling("beach-edited.jpg");        // /home/lokesh/photos/2026/beach-edited.jpg
            show("renamed", renamed);
            Path down = library.relativize(photo);                          // 2026/beach.jpg
            show("down", down);
            Path up = photo.relativize(library);                            // ../..
            show("up", up);
            try { Path mixed = library.relativize(Path.of("2026")); show("mixed", mixed); } catch (Throwable _t) { System.out.println("mixed -> " + _t); }
        }
        {
            Path source = Path.of("/home/lokesh/photos");
            Path backup = Path.of("/mnt/backup/photos");
            Path file = Path.of("/home/lokesh/photos/2026/beach.jpg");
            Path target = backup.resolve(source.relativize(file));      // /mnt/backup/photos/2026/beach.jpg
            show("target", target);
        }
        {
            Path base = Files.createTempDirectory("photos").toRealPath();
            Path year = Files.createDirectories(base.resolve("archive/2026"));
            Path current = Files.createSymbolicLink(base.resolve("current"), year);

            Path viaLink = current.resolve("..");                            // <base>/current/..
            show("viaLink", viaLink);
            boolean textToBase = viaLink.normalize().equals(base);           // true
            show("textToBase", textToBase);
            Path real = base.relativize(viaLink.toRealPath());               // archive
            show("real", real);
            Path canonical = base.relativize(Path.of(viaLink.toFile().getCanonicalPath()));   // archive
            show("canonical", canonical);
            Path absolute = Path.of("photos/../beach.jpg").toAbsolutePath();  // /home/lokesh/demo/photos/../beach.jpg
            show("absolute", absolute);
            try { Path missing = Path.of("no-such-folder").toRealPath(); show("missing", missing); } catch (Throwable _t) { System.out.println("missing -> " + _t); }
        }
        {
            Path photo = Path.of("/home/lokesh/photos/2026/beach.jpg");
            File legacy = photo.toFile();                     // /home/lokesh/photos/2026/beach.jpg
            show("legacy", legacy);
            Path back = legacy.toPath();                      // /home/lokesh/photos/2026/beach.jpg
            show("back", back);
            URI uri = photo.toUri();                          // file:///home/lokesh/photos/2026/beach.jpg
            show("uri", uri);
            String text = photo.toString();                   // "/home/lokesh/photos/2026/beach.jpg"
            show("text", text);
            Path fromText = Path.of(text);                    // /home/lokesh/photos/2026/beach.jpg
            show("fromText", fromText);
        }
        {
            Path library = Files.createTempDirectory("library");
            Path album = Files.createDirectories(library.resolve("2026/summer"));
            Path notes = Files.writeString(album.resolve("notes.txt"), "Beach day", StandardCharsets.UTF_8);
            boolean exists = Files.exists(notes);                         // true
            show("exists", exists);
            String content = Files.readString(notes);                     // "Beach day"
            show("content", content);
            long size = Files.size(notes);                                // 9
            show("size", size);
            boolean isFolder = Files.isDirectory(album);                  // true
            show("isFolder", isFolder);
        }
        {
            Path photo = Path.of("/home/lokesh/photos/2026/beach.jpg");
            String name = photo.getFileName().toString();
            String baseName = name.substring(0, name.lastIndexOf('.'));   // "beach"
            show("baseName", baseName);
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
