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
 * Examples for the tutorial "Rename or Move a File or Directory in Java (Files.move)".
 * https://howtodoinjava.com/java/io/rename-move-file-directory/
 */
public class MoveOrRenameFiles {
    static Path moveSafely(Path source, Path target) throws IOException {
        try {
            return Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            return Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path dir = Files.createTempDirectory("orders");
            Path order = Files.writeString(dir.resolve("order.csv"), "id,qty");
            Path renamed = Files.move(order, order.resolveSibling("order-1.csv"));
            String newName = renamed.getFileName().toString();                       // "order-1.csv"
            show("newName", newName);
            Path processed = Files.createDirectories(dir.resolve("processed"));
            Path moved = Files.move(renamed, processed.resolve(renamed.getFileName()));
            boolean oldGone = Files.notExists(renamed);                              // true
            show("oldGone", oldGone);
            String content = Files.readString(moved);                                // "id,qty"
            show("content", content);
        }
        {
            Path inbox = Files.createTempDirectory("inbox");
            Path invoice = Files.writeString(inbox.resolve("invoice.txt"), "Total 50");
            Path taken = Files.writeString(inbox.resolve("invoice-old.txt"), "Total 10");
            try { Path clash = Files.move(invoice, taken); show("clash", clash); } catch (Throwable _t) { System.out.println("clash -> " + _t); }
            try { Path noFolder = Files.move(invoice, inbox.resolve("2026/invoice.txt")); show("noFolder", noFolder); } catch (Throwable _t) { System.out.println("noFolder -> " + _t); }
        }
        {
            Path logs = Files.createTempDirectory("logs");
            Path current = Files.writeString(logs.resolve("app.log.1"), "today");
            Path older = Files.writeString(logs.resolve("app.log.2"), "last week");
            Files.move(current, older, StandardCopyOption.REPLACE_EXISTING);
            String rotated = Files.readString(older);                                 // "today"
            show("rotated", rotated);
            boolean freed = Files.notExists(current);                                 // true
            show("freed", freed);
        }
        {
            Path shared = Files.createTempDirectory("shared");
            Path temp = Files.createTempFile(shared, "sales-", ".tmp");
            Files.writeString(temp, "region,total\nnorth,420");
            Path published = Files.move(temp, shared.resolve("sales.csv"), StandardCopyOption.ATOMIC_MOVE);
            String report = Files.readString(published);                              // "region,total\nnorth,420"
            show("report", report);
        }
        {
            Path uploads = Files.createTempDirectory("uploads");
            Path archive = Files.createTempDirectory("archive");
            boolean sameStore = Files.getFileStore(uploads).equals(Files.getFileStore(archive));   // true
            show("sameStore", sameStore);
        }
        {
            Path albums = Files.createTempDirectory("albums");
            Path trip = Files.createDirectories(albums.resolve("trip"));
            Files.writeString(trip.resolve("day1.jpg"), "jpg");
            Path paris = Files.move(trip, trip.resolveSibling("paris-2026"));
            boolean photoMoved = Files.exists(paris.resolve("day1.jpg"));            // true
            show("photoMoved", photoMoved);
            Path parent = Files.createDirectories(albums.resolve("europe"));
            Path nested = Files.move(paris, parent.resolve(paris.getFileName()));
            boolean stillThere = Files.exists(nested.resolve("day1.jpg"));           // true
            show("stillThere", stillThere);
        }
        {
            File folder = Files.createTempDirectory("legacy").toFile();
            File draft = new File(folder, "draft.txt");
            boolean created = draft.createNewFile();                                  // true
            show("created", created);
            boolean renamedOk = draft.renameTo(new File(folder, "final.txt"));        // true
            show("renamedOk", renamedOk);
            boolean missing = new File(folder, "nope.txt").renameTo(new File(folder, "x.txt"));   // false
            show("missing", missing);
        }
        {
            File folder = Files.createTempDirectory("legacy").toFile();
            File keep = Files.writeString(new File(folder, "final.txt").toPath(), "v1").toFile();
            File other = new File(folder, "other.txt");
            boolean made = other.createNewFile();                                     // true
            show("made", made);
            try { Path refused = Files.move(keep.toPath(), other.toPath()); show("refused", refused); } catch (Throwable _t) { System.out.println("refused -> " + _t); }
        }
        {
            Path photoFile = Files.writeString(Files.createTempFile("img", ".jpeg"), "jpg");
            String base = photoFile.getFileName().toString().replaceFirst("\\.jpeg$", "");
            Path withJpg = Files.move(photoFile, photoFile.resolveSibling(base + ".jpg"));
            boolean jpg = withJpg.toString().endsWith(".jpg");                      // true
            show("jpg", jpg);
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
