package com.howtodoinjava.core.streams;

import java.util.regex.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import org.apache.commons.lang3.function.*;
import org.apache.commons.lang3.stream.*;

/**
 * Examples for the tutorial "Handle Exceptions in Java Streams: Wrap, Skip or Collect".
 * https://howtodoinjava.com/java/stream/handle-exceptions-in-stream/
 */
public class StreamExceptionHandling {
    static class LinkParser {

        static URI parseOrThrow(String link) {
            try {
                return new URI(link);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Bad link: " + link, e);
            }
        }

        static Optional<URI> tryParse(String link) {
            try {
                return Optional.of(new URI(link));
            } catch (URISyntaxException e) {
                System.getLogger("links").log(System.Logger.Level.WARNING, "Skipping " + link);
                return Optional.empty();
            }
        }

        static String readOrThrow(Path file) {
            try {
                return Files.readString(file);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
    static sealed interface Result<T> permits Ok, Failed {}
    static record Ok<T>(T value) implements Result<T> {}
    static record Failed<T>(String input, Exception error) implements Result<T> {}
    static Result<URI> parse(String link) {
        try {
            return new Ok<>(new URI(link));
        } catch (URISyntaxException e) {
            return new Failed<>(link, e);
        }
    }
    @FunctionalInterface
    static interface ThrowingFunction<T, R> {
        R apply(T t) throws Exception;
    }
    static <T, R> Function<T, R> unchecked(ThrowingFunction<T, R> f) {
        return t -> {
            try {
                return f.apply(t);
            } catch (RuntimeException e) {
                throw e;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } catch (Exception e) {
                throw new RuntimeException("Failed for " + t, e);
            }
        };
    }
    static Result<String> load(Path file) {
        try {
            return new Ok<>(Files.readString(file));
        } catch (IOException e) {
            return new Failed<>(file.toString(), e);
        }
    }
    static List<String> loadTemplates(List<Path> files) {
        Map<Boolean, List<Result<String>>> byOutcome = files.stream()
                .map(file -> load(file))
                .collect(Collectors.partitioningBy(r -> r instanceof Ok));
        byOutcome.get(false).forEach(f -> System.getLogger("templates")
                .log(System.Logger.Level.ERROR, "Cannot load " + ((Failed<String>) f).input()));
        return byOutcome.get(true).stream().map(r -> ((Ok<String>) r).value()).toList();
    }
    public static void main(String[] args) throws Exception {
        {
            List<String> links = List.of("https://example.com/a", "bad link", "https://example.com/b");

            List<URI> valid = links.stream().map(LinkParser::tryParse).flatMap(Optional::stream).toList();   // [https://example.com/a, https://example.com/b]
            show("valid", valid);
            try { List<URI> strict = links.stream().map(LinkParser::parseOrThrow).toList(); show("strict", strict); } catch (Throwable _t) { System.out.println("strict -> " + _t); }
        }
        {
            List<String> trusted = List.of("https://example.com/a", "https://example.com/b");

            List<URI> uris = trusted.stream()
                    .map(link -> {
                try {
                    return new URI(link);
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException("Bad link: " + link, e);
                }
            })
                    .toList();                     // [https://example.com/a, https://example.com/b]
        }
        {
            List<String> trusted = List.of("https://example.com/a", "https://example.com/b");
            List<String> links = List.of("https://example.com/a", "bad link", "https://example.com/b");

            List<URI> ok = trusted.stream().map(LinkParser::parseOrThrow).toList();       // [https://example.com/a, https://example.com/b]
            show("ok", ok);
            try { List<URI> stopped = links.stream().map(LinkParser::parseOrThrow).toList(); show("stopped", stopped); } catch (Throwable _t) { System.out.println("stopped -> " + _t); }
        }
        {
            List<String> trusted = List.of("https://example.com/a", "https://example.com/b");
            List<String> links = List.of("https://example.com/a", "bad link", "https://example.com/b");

            List<URI> created = trusted.stream().map(URI::create).toList();    // [https://example.com/a, https://example.com/b]
            show("created", created);
            try { List<URI> rejected = links.stream().map(URI::create).toList(); show("rejected", rejected); } catch (Throwable _t) { System.out.println("rejected -> " + _t); }
        }
        {
            List<String> links = List.of("https://example.com/a", "bad link", "https://example.com/b");

            List<URI> parsed = links.stream().map(LinkParser::tryParse).flatMap(Optional::stream).toList();   // [https://example.com/a, https://example.com/b]
            show("parsed", parsed);
        }
        {
            List<String> links = List.of("https://example.com/a", "bad link", "https://example.com/b");

            List<URI> pushed = links.stream()
                    .<URI>mapMulti((link, sink) -> {
                try {
                    sink.accept(new URI(link));
                } catch (URISyntaxException e) {
                    System.getLogger("links").log(System.Logger.Level.WARNING, "Skipping " + link);
                }
            })
                    .toList();                     // [https://example.com/a, https://example.com/b]
        }
        {
            List<String> links = List.of("https://example.com/a", "bad link", "https://example.com/b");

            List<Result<URI>> results = links.stream().map(link -> parse(link)).toList();
            List<String> report = results.stream()
                    .map(r -> switch (r) {
                case Ok<URI>(URI uri) -> "OK " + uri.getHost();
                case Failed<URI>(String input, Exception error) -> "FAILED " + input;
            })
                    .toList();                     // [OK example.com, FAILED bad link, OK example.com]
        }
        {
            List<Result<URI>> outcomes = Stream.of("https://example.com/a", "bad link").map(link -> parse(link)).toList();
            long failures = outcomes.stream().filter(r -> r instanceof Failed).count();   // 1
            show("failures", failures);
        }
        {
            List<String> trusted = List.of("https://example.com/a", "https://example.com/b");
            List<String> links = List.of("https://example.com/a", "bad link", "https://example.com/b");

            List<URI> wrapped = trusted.stream().map(unchecked(URI::new)).toList();     // [https://example.com/a, https://example.com/b]
            show("wrapped", wrapped);
            try { List<URI> failed = links.stream().map(unchecked(URI::new)).toList(); show("failed", failed); } catch (Throwable _t) { System.out.println("failed -> " + _t); }
        }
        {
            List<String> trusted = List.of("https://example.com/a", "https://example.com/b");
            List<String> links = List.of("https://example.com/a", "bad link", "https://example.com/b");

            List<URI> viaFailable = Streams.failableStream(trusted).map(URI::new).collect(Collectors.toList());   // [https://example.com/a, https://example.com/b]
            show("viaFailable", viaFailable);
            List<URI> viaAdapter = trusted.stream().map(Failable.asFunction(URI::new)).toList();              // [https://example.com/a, https://example.com/b]
            show("viaAdapter", viaAdapter);
            try { List<URI> notParsed = links.stream().map(Failable.asFunction(URI::new)).toList(); show("notParsed", notParsed); } catch (Throwable _t) { System.out.println("notParsed -> " + _t); }
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
