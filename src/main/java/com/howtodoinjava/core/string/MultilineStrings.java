package com.howtodoinjava.core.string;

import java.util.concurrent.locks.*;
import java.lang.management.*;
import java.time.format.*;
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

import java.time.temporal.*;

/**
 * Examples for the tutorial "Java Multiline String: Text Blocks and Older Options".
 * https://howtodoinjava.com/java/string/multiline-string/
 */
public class MultilineStrings {

    public static void main(String[] args) throws Exception {
        {
            String sql = """
            SELECT title, artist
            FROM songs
            WHERE plays > 100
            """;
            int lineCount = (int) sql.lines().count();                    // 3
            show("lineCount", lineCount);
            boolean endsWithNewline = sql.endsWith("\n");                 // true
            show("endsWithNewline", endsWithNewline);
            boolean noIndent = sql.startsWith("SELECT");                  // true
            show("noIndent", noIndent);
        }
        {
            String quotes = """
            She said "hi" and left.
            It's 'fine'.""";
            boolean kept = quotes.contains("\"hi\"");                     // true
            show("kept", kept);
            int quoteLines = (int) quotes.lines().count();                // 2
            show("quoteLines", quoteLines);
        }
        {
            String flush = """
            {
                "title": "Yesterday"
            }
            """;
            String indented = """
            {
                "title": "Yesterday"
            }
            """;
            boolean flushStart = flush.startsWith("{");                    // true
            show("flushStart", flushStart);
            boolean indentedStart = indented.startsWith("  {");            // true
            show("indentedStart", indentedStart);
        }
        {
            String joined = """
            This sentence is long, so we split it \
            over two lines in the source.""";
            String padded = """
            red  \s
            green\s
            """;
            boolean oneLine = joined.lines().count() == 1;                 // true
            show("oneLine", oneLine);
            boolean noFinalBreak = !joined.endsWith("\n");                 // true
            show("noFinalBreak", noFinalBreak);
            int redLength = padded.lines().findFirst().orElse("").length(); // 6
            show("redLength", redLength);
        }
        {
            String help = """
            Usage: player [options]
            --shuffle   play in random order""";
            String shifted = help.indent(4);                              // 4 more spaces per line, ends with \n
            show("shifted", shifted);
            String loaded = "    line one\n      line two";
            String stripped = loaded.stripIndent();
            boolean strippedOk = stripped.equals("line one\n  line two");  // true
            show("strippedOk", strippedOk);
            String escaped = "tab\\there".translateEscapes();
            boolean realTab = escaped.equals("tab\there");                // true
            show("realTab", realTab);
            long optionLines = help.lines().filter(l -> l.strip().startsWith("--")).count();   // 1
            show("optionLines", optionLines);
        }
        {
            String content = "SELECT title, artist\n" +
            "FROM songs\n" +
            "WHERE plays > 100\n";
            String block = """
            SELECT title, artist
            FROM songs
            WHERE plays > 100
            """;
            boolean sameAsBlock = content.equals(block);                  // true
            show("sameAsBlock", sameAsBlock);
        }
        {
            String content = String.join("\n",
            "SELECT title, artist",
            "FROM songs",
            "WHERE plays > 100");
            int joinedLines = (int) content.lines().count();              // 3
            show("joinedLines", joinedLines);
            boolean trailing = content.endsWith("\n");                    // false
            show("trailing", trailing);
        }
        {
            List<String> songs = List.of("Yesterday", "Hallelujah", "Hey");
            String playlist = songs.stream()
                    .map(song -> "- " + song)
                    .collect(Collectors.joining("\n"));
            boolean firstLine = playlist.startsWith("- Yesterday\n");     // true
            show("firstLine", firstLine);
        }
        {
            String query = """
            SELECT title
            FROM songs
            WHERE artist = '%s'
            """.formatted("Beatles");
            boolean filled = query.contains("'Beatles'");                 // true
            show("filled", filled);
        }
        {
            String topSongs = """
            SELECT title, artist, plays
            FROM songs
            WHERE plays > ?
            ORDER BY plays DESC
            """;
            boolean usesParameter = topSongs.contains("plays > ?");       // true
            show("usesParameter", usesParameter);
        }
        {
            String expected = """
            | Title | Plays |
            |:------|------:|
            | Hey   |    75 |""";
            String actual = String.join("\n", "| Title | Plays |", "|:------|------:|", "| Hey   |    75 |");
            boolean matches = expected.equals(actual);                    // true
            show("matches", matches);
        }
        {
            String block = """
            Hey""";
            boolean sameObject = block == "Hey";                          // true
            show("sameObject", sameObject);
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
