package com.howtodoinjava.core.basic;

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

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Examples for the tutorial "Java WatchService Tutorial: Watch a Directory for Changes".
 * https://howtodoinjava.com/java8/java-8-watchservice-api-tutorial/
 */
public class WatchServiceExamples {
    static class TreeWatcher implements AutoCloseable {

        private final WatchService watcher = FileSystems.getDefault().newWatchService();
        private final Map<WatchKey, Path> dirs = new HashMap<>();

        TreeWatcher(Path root) throws IOException {
            registerTree(root, new ArrayList<>());
        }

        // registers dir before listing it, so a new entry is either listed or fires an event
        private void registerTree(Path dir, List<Path> found) throws IOException {
            dirs.put(dir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE), dir);
            try (Stream<Path> entries = Files.list(dir)) {
                for (Path entry : entries.toList()) {
                    found.add(entry);
                    if (Files.isDirectory(entry, LinkOption.NOFOLLOW_LINKS)) {
                        registerTree(entry, found);
                    }
                }
            }
        }

        void processEvents(BiConsumer<String, Path> listener) throws InterruptedException {
            while (!dirs.isEmpty()) {
                WatchKey key = watcher.take();
                Path dir = dirs.get(key);
                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == OVERFLOW) {
                        listener.accept("OVERFLOW", dir);   // rescan dir here
                        continue;
                    }
                    Path child = dir.resolve((Path) event.context());
                    listener.accept(event.kind().name(), child);
                    if (event.kind() == ENTRY_CREATE && Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
                        try {
                            List<Path> found = new ArrayList<>();
                            registerTree(child, found);
                            found.forEach(p -> listener.accept("FOUND", p));
                        } catch (IOException e) {
                            listener.accept("NOT_WATCHED", child);
                        }
                    }
                }
                if (!key.reset()) {
                    dirs.remove(key);
                }
            }
        }

        @Override
        public void close() throws IOException {
            watcher.close();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Path inbox = Files.createTempDirectory("inbox");

            try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
                WatchKey registered = inbox.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
                Files.createFile(inbox.resolve("orders.csv"));

                WatchKey key = watcher.poll(5, TimeUnit.SECONDS);   // the key of inbox, or null after 5 seconds
                boolean sameKey = key == registered;                // true
                List<String> events = key.pollEvents().stream().map(e -> e.kind() + " " + e.context()).toList();   // [ENTRY_CREATE orders.csv]
                boolean stillValid = key.reset();                   // true
            }
        }
        {
            Path inbox = Files.createTempDirectory("inbox");
            Path orders = Files.createFile(inbox.resolve("orders.csv"));
            WatchService watcher = FileSystems.getDefault().newWatchService();

            WatchKey key = inbox.register(watcher, ENTRY_CREATE, ENTRY_DELETE);   // valid key for inbox
            show("key", key);
            try { WatchKey fileKey = orders.register(watcher, ENTRY_MODIFY); show("fileKey", fileKey); } catch (Throwable _t) { System.out.println("fileKey -> " + _t); }
            watcher.close();
        }
        {
            Path inbox = Files.createTempDirectory("inbox");

            try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
                inbox.register(watcher, ENTRY_CREATE);
                WatchKey nothingYet = watcher.poll();                          // null
                WatchKey afterWait = watcher.poll(100, TimeUnit.MILLISECONDS);   // null
            }
        }
        {
            Path inbox = Files.createTempDirectory("inbox");

            try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
                inbox.register(watcher, ENTRY_CREATE, ENTRY_DELETE);
                Files.createFile(inbox.resolve("invoice.pdf"));

                WatchKey key = watcher.take();
                WatchEvent<?> event = key.pollEvents().getFirst();
                Path dir = (Path) key.watchable();
                Path fullPath = dir.resolve((Path) event.context());
                boolean inInbox = fullPath.getParent().equals(inbox);   // true
                int count = event.count();                              // 1
                key.reset();
            }
        }
        {
            Path inbox = Files.createTempDirectory("inbox");

            try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
                inbox.register(watcher, ENTRY_CREATE);
                for (int i = 0; i < 600; i++) {
                    Files.createFile(inbox.resolve("order-" + i + ".csv"));
                }
                WatchKey key = watcher.poll(5, TimeUnit.SECONDS);
                WatchEvent<?> first = key.pollEvents().getFirst();
                WatchEvent.Kind<?> kind = first.kind();   // OVERFLOW
                Object context = first.context();         // null
                key.reset();
            }
        }
        {
            Path inbox = Files.createTempDirectory("inbox");

            try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
                WatchKey key = inbox.register(watcher, ENTRY_CREATE);
                key.cancel();
                boolean valid = key.isValid();     // false
                boolean rearmed = key.reset();     // false
            }
        }
        {
            Path inbox = Files.createTempDirectory("inbox");
            BlockingQueue<String> events = new LinkedBlockingQueue<>();
            TreeWatcher treeWatcher = new TreeWatcher(inbox);

            Thread worker = Thread.ofVirtual().start(() -> {
                try {
                    treeWatcher.processEvents((kind, path) -> events.add(kind + " " + inbox.relativize(path)));
                } catch (InterruptedException | ClosedWatchServiceException e) {
                    // the watcher was stopped
                }
            });

            Path march = Files.createDirectories(inbox.resolve("2026/march"));
            Files.writeString(march.resolve("orders.csv"), "id,total");

            List<String> seen = new ArrayList<>();
            String line;
            while ((line = events.poll(2, TimeUnit.SECONDS)) != null) {
                seen.add(line);
            }
            treeWatcher.close();
            boolean ordersSeen = seen.stream().anyMatch(s -> s.endsWith("orders.csv"));   // true
            show("ordersSeen", ordersSeen);
        }
        {
            Path configDir = Files.createTempDirectory("config");
            Path config = Files.writeString(configDir.resolve("app.properties"), "timeout=30");

            try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
                configDir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
                Files.writeString(config, "timeout=60");

                WatchKey key = watcher.poll(5, TimeUnit.SECONDS);
                boolean configChanged = key.pollEvents().stream().anyMatch(e -> config.getFileName().equals(e.context()));   // true
                key.reset();
            }
        }
        {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            watcher.close();
            try { WatchKey key = watcher.take(); show("key", key); } catch (Throwable _t) { System.out.println("key -> " + _t); }
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
