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
 * Examples for the tutorial "Align Text in Columns in Java: Print a Table with printf".
 * https://howtodoinjava.com/java/string/align-text-in-columns/
 */
public class PrintTableColumns {
    static String shorten(String value, int max) {
        return value.length() <= max ? value : value.substring(0, max - 3) + "...";
    }
    static enum Alignment { LEFT, CENTER, RIGHT }
    static record Column(String header, Alignment alignment) {}
    static String fit(String value, int width, Alignment alignment) {
        int gap = Math.max(0, width - value.length());
        return switch (alignment) {
            case LEFT -> value + " ".repeat(gap);
            case RIGHT -> " ".repeat(gap) + value;
            case CENTER -> " ".repeat(gap / 2) + value + " ".repeat(gap - gap / 2);
        };
    }
    static final class TextTable {
        private final List<Column> columns;
        private final List<List<String>> rows = new ArrayList<>();

        TextTable(List<Column> columns) {
            this.columns = List.copyOf(columns);
        }

        void add(Object... values) {
            if (values.length != columns.size()) {
                throw new IllegalArgumentException(
                "Expected " + columns.size() + " values, got " + values.length);
            }
            rows.add(Arrays.stream(values).map(String::valueOf).toList());
        }

        String build() {
            int[] widths = new int[columns.size()];
            for (int i = 0; i < widths.length; i++) {
                widths[i] = columns.get(i).header().length();
                for (List<String> row : rows) {
                    widths[i] = Math.max(widths[i], row.get(i).length());
                }
            }
            StringJoiner table = new StringJoiner("\n");
            table.add(line(columns.stream().map(Column::header).toList(), widths));
            table.add(separator(widths));
            rows.forEach(row -> table.add(line(row, widths)));
            return table.toString();
        }

        private String line(List<String> values, int[] widths) {
            StringJoiner line = new StringJoiner(" | ", "| ", " |");
            for (int i = 0; i < widths.length; i++) {
                line.add(fit(values.get(i), widths[i], columns.get(i).alignment()));
            }
            return line.toString();
        }

        private String separator(int[] widths) {
            StringJoiner line = new StringJoiner("|", "|", "|");
            for (int i = 0; i < widths.length; i++) {
                Alignment a = columns.get(i).alignment();
                String left = a == Alignment.RIGHT ? "-" : ":";
                String right = a == Alignment.LEFT ? "-" : ":";
                line.add(left + "-".repeat(widths[i]) + right);
            }
            return line.toString();
        }
    }
    public static void main(String[] args) throws Exception {
        {
            String rowFormat = "%-12s%-15s%6s";
            String header = rowFormat.formatted("Title", "Artist", "Plays");     // "Title       Artist          Plays"
            show("header", header);
            String row = rowFormat.formatted("Yesterday", "Beatles", 1520);      // "Yesterday   Beatles          1520"
            show("row", row);
            String line = "-".repeat(33);                                        // 12 + 15 + 6 dashes
            show("line", line);
        }
        {
            String rowFormat = "%-12s%-15s%6s%n";
            System.out.printf(rowFormat, "Title", "Artist", "Plays");
        }
        {
            List<String> titles = List.of("Yesterday", "Hallelujah", "Hey");
            int titleWidth = titles.stream().mapToInt(String::length).max().orElse(0);   // 10
            show("titleWidth", titleWidth);
            String rowFormat = "%-" + (titleWidth + 2) + "s%6d";                         // "%-12s%6d"
            show("rowFormat", rowFormat);
            String first = rowFormat.formatted("Yesterday", 1520);                       // "Yesterday     1520"
            show("first", first);
        }
        {
            String shifted = String.format("%-12s|", "Bohemian Rhapsody");           // "Bohemian Rhapsody|"
            show("shifted", shifted);
            String kept = String.format("%-12s|", shorten("Bohemian Rhapsody", 12));  // "Bohemian ...|"
            show("kept", kept);
            String cutOnly = String.format("%-12.12s|", "Bohemian Rhapsody");         // "Bohemian Rha|"
            show("cutOnly", cutOnly);
        }
        {
            TextTable table = new TextTable(List.of(
            new Column("Title", Alignment.LEFT),
            new Column("Artist", Alignment.LEFT),
            new Column("Plays", Alignment.RIGHT),
            new Column("Rating", Alignment.CENTER)));
            table.add("Yesterday", "Beatles", 1520, "4.8");
            table.add("Hallelujah", "Leonard Cohen", 980, "4.9");
            table.add("Hey", "Pixies", 75, "4.1");
            String printed = table.build();                                    // 5 lines
            show("printed", printed);
            int lineCount = (int) printed.lines().count();                     // 5
            show("lineCount", lineCount);
        }
        {
            TextTable songs = new TextTable(List.of(
            new Column("Title", Alignment.LEFT),
            new Column("Plays", Alignment.RIGHT)));
            try { songs.add("Yesterday");  } catch (Throwable _t) { System.out.println("-> " + _t); }
        }
        {
            TextTable table = new TextTable(List.of(new Column("Title", Alignment.LEFT)));
            table.add("Yesterday");
            Path report = Files.createTempFile("playlist", ".md");
            Path written = Files.writeString(report, table.build());           // the table as a Markdown file
            show("written", written);
            boolean saved = Files.exists(written);                             // true
            show("saved", saved);
        }
        {
            String bordered = String.format("| %-10s | %5d |", "Hey", 75);   // "| Hey        |    75 |"
            show("bordered", bordered);
            String border = "+" + "-".repeat(12) + "+" + "-".repeat(7) + "+";   // "+------------+-------+"
            show("border", border);
        }
        {
            String price = String.format(Locale.ROOT, "%8.2f|", 3.5);    // "    3.50|"
            show("price", price);
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
