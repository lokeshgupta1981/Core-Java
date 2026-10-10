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
 * Examples for the tutorial "Make a File Read-Only in Java: setReadOnly() and POSIX".
 * https://howtodoinjava.com/java/io/make-a-file-read-only-in-java/
 */
public class ReadOnlyFileExamples {
    static void removeWriteAccess(Path file) throws IOException {
        Set<PosixFilePermission> perms = new HashSet<>(Files.getPosixFilePermissions(file));
        perms.removeAll(Set.of(PosixFilePermission.OWNER_WRITE,
        PosixFilePermission.GROUP_WRITE, PosixFilePermission.OTHERS_WRITE));
        Files.setPosixFilePermissions(file, perms);
    }
    static void makeReadOnly(Path file) throws IOException {
        if (file.getFileSystem().supportedFileAttributeViews().contains("posix")) {
            removeWriteAccess(file);
        } else {
            Files.setAttribute(file, "dos:readonly", true);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path dir = Files.createTempDirectory("config");
            Path appYml = Files.writeString(dir.resolve("app.yml"), "port: 8080");
            boolean done = appYml.toFile().setReadOnly();                        // true
            show("done", done);
            String perms = PosixFilePermissions.toString(Files.getPosixFilePermissions(appYml)); // "r--r--r--"
            show("perms", perms);
            boolean back = appYml.toFile().setWritable(true);                    // true, owner can write again
            show("back", back);
        }
        {
            Path shared = Files.createTempDirectory("shared");
            Path a = Files.writeString(shared.resolve("a.txt"), "x");
            Path b = Files.writeString(shared.resolve("b.txt"), "x");
            Path aPerms = Files.setPosixFilePermissions(a, PosixFilePermissions.fromString("rw-rw-r--")); // group can write
            show("aPerms", aPerms);
            Path bPerms = Files.setPosixFilePermissions(b, PosixFilePermissions.fromString("rw-rw-r--")); // group can write
            show("bPerms", bPerms);
            boolean aDone = a.toFile().setReadOnly();                            // true
            show("aDone", aDone);
            boolean bDone = b.toFile().setWritable(false);                       // true
            show("bDone", bDone);
            String aAfter = PosixFilePermissions.toString(Files.getPosixFilePermissions(a)); // "r--r--r--"
            show("aAfter", aAfter);
            String bAfter = PosixFilePermissions.toString(Files.getPosixFilePermissions(b)); // "r--rw-r--"
            show("bAfter", bAfter);
        }
        {
            Path dir = Files.createTempDirectory("invoices");
            File missing = dir.resolve("inv-7.pdf").toFile();
            boolean changed = missing.setReadOnly();                             // false, no such file
            show("changed", changed);
        }
        {
            Path bin = Files.createTempDirectory("bin");
            Path script = Files.writeString(bin.resolve("deploy.sh"), "echo ok");
            Path exec = Files.setPosixFilePermissions(script, PosixFilePermissions.fromString("rwxr-xr-x")); // executable
            show("exec", exec);
            removeWriteAccess(script);
            String scriptPerms = PosixFilePermissions.toString(Files.getPosixFilePermissions(script)); // "r-xr-xr-x"
            show("scriptPerms", scriptPerms);
        }
        {
            Path release = Files.createTempDirectory("release");
            Path yml = Files.writeString(release.resolve("application.yml"), "db: prod");
            Path locked = Files.setPosixFilePermissions(yml, PosixFilePermissions.fromString("r--r-----"));
            String ymlPerms = PosixFilePermissions.toString(Files.getPosixFilePermissions(locked)); // "r--r-----"
            show("ymlPerms", ymlPerms);
        }
        {
            Path docs = Files.createTempDirectory("docs");
            Path contract = Files.writeString(docs.resolve("contract.pdf"), "x");
            Path flagged = Files.setAttribute(contract, "dos:readonly", true);   // sets the flag
            show("flagged", flagged);
            boolean readOnly = Files.readAttributes(contract, DosFileAttributes.class).isReadOnly(); // true
            show("readOnly", readOnly);
        }
        {
            Path out = Files.createTempDirectory("out");
            Path audit = Files.writeString(out.resolve("audit.log"), "x");
            makeReadOnly(audit);
            String auditPerms = PosixFilePermissions.toString(Files.getPosixFilePermissions(audit)); // "r--r--r--"
            show("auditPerms", auditPerms);
        }
        {
            Path dir = Files.createTempDirectory("logs");
            Path log = Files.writeString(dir.resolve("audit.log"), "x");
            boolean locked = log.toFile().setReadOnly();                         // true
            show("locked", locked);
            boolean ownerOnly = log.toFile().setWritable(true);                  // true, owner may write
            show("ownerOnly", ownerOnly);
            String ownerPerms = PosixFilePermissions.toString(Files.getPosixFilePermissions(log)); // "rw-r--r--"
            show("ownerPerms", ownerPerms);
            boolean everyone = log.toFile().setWritable(true, false);            // true, all may write
            show("everyone", everyone);
            String allPerms = PosixFilePermissions.toString(Files.getPosixFilePermissions(log));   // "rw-rw-rw-"
            show("allPerms", allPerms);
        }
        {
            Path dir = Files.createTempDirectory("check");
            Path report = Files.writeString(dir.resolve("report.pdf"), "x");
            boolean marked = report.toFile().setReadOnly();                      // true
            show("marked", marked);
            Set<PosixFilePermission> perms = Files.getPosixFilePermissions(report);
            boolean noWriteBits = !perms.contains(PosixFilePermission.OWNER_WRITE)
                    && !perms.contains(PosixFilePermission.GROUP_WRITE)
                    && !perms.contains(PosixFilePermission.OTHERS_WRITE);
            boolean readOnlyForAll = noWriteBits;                                // true
            show("readOnlyForAll", readOnlyForAll);
        }
        {
            Path tmp = Files.createTempDirectory("tmp");
            Path note = Files.writeString(tmp.resolve("note.txt"), "keep me");
            boolean ro = note.toFile().setReadOnly();                            // true
            show("ro", ro);
            Files.delete(note);                                                  // works on Linux, folder is writable
            boolean gone = Files.notExists(note);                                // true
            show("gone", gone);
        }
        {
            Path dir = Files.createTempDirectory("team");
            Path plan = Files.writeString(dir.resolve("plan.md"), "x");
            Path set = Files.setPosixFilePermissions(plan, PosixFilePermissions.fromString("rw-r--r--")); // owner keeps write
            show("set", set);
            String planPerms = PosixFilePermissions.toString(Files.getPosixFilePermissions(plan)); // "rw-r--r--"
            show("planPerms", planPerms);
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
