package com.howtodoinjava.core.basic;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;

/**
 * Examples for the tutorial "Abstraction in Java: Interfaces and Abstract Classes".
 * https://howtodoinjava.com/java/oops/understanding-abstraction-in-java/
 */
public class AbstractionInterfaceImplementations {
    static record Sale(String item, int units) {
    }

    static interface ReportFormat {
        List<String> lines(List<Sale> sales);
    }

    static class CsvFormat implements ReportFormat {
        public List<String> lines(List<Sale> sales) {
            List<String> out = new ArrayList<>();
            out.add("item,units");
            for (Sale sale : sales) {
                out.add(sale.item() + "," + sale.units());
            }
            return out;
        }
    }
    static class JsonLinesFormat implements ReportFormat {
        public List<String> lines(List<Sale> sales) {
            List<String> out = new ArrayList<>();
            for (Sale sale : sales) {
                out.add("{\"item\":\"" + sale.item() + "\",\"units\":" + sale.units() + "}");
            }
            return out;
        }
    }

    static class ReportService {
        private final ReportFormat format;

        ReportService(ReportFormat format) {
            this.format = format;
        }

        String firstLine(List<Sale> sales) {
            List<String> lines = format.lines(sales);
            return lines.isEmpty() ? "" : lines.get(0);
        }
    }
    static abstract class DelimitedFormat implements ReportFormat {
        protected abstract String separator();

        public final List<String> lines(List<Sale> sales) {
            List<String> out = new ArrayList<>();
            out.add("item" + separator() + "units");
            for (Sale sale : sales) {
                out.add(sale.item() + separator() + sale.units());
            }
            return out;
        }
    }

    static class TsvFormat extends DelimitedFormat {
        protected String separator() {
            return "\t";
        }
    }

    static class PipeFormat extends DelimitedFormat {
        protected String separator() {
            return "|";
        }
    }
    static interface Exportable {
        String name();

        default String fileName() {
            return clean(name()) + ".csv";
        }

        private String clean(String raw) {
            return raw.strip().toLowerCase().replace(' ', '-');
        }
    }

    static record MonthlyReport(String name) implements Exportable {
    }
    static sealed interface ExportTarget permits Download, Email {
    }

    static record Download(String fileName) implements ExportTarget {
    }

    static record Email(String address) implements ExportTarget {
    }
    static String deliver(ExportTarget target) {
        return switch (target) {
            case Download d -> "saved " + d.fileName();
            case Email e -> "sent to " + e.address();
        };
    }
    public static void main(String[] args) throws Exception {
        {
            List<Sale> sales = List.of(new Sale("apple", 5), new Sale("pear", 3));
            ReportFormat format = new CsvFormat();
            List<String> csv = format.lines(sales);           // [item,units, apple,5, pear,3]
            show("csv", csv);
            int rows = csv.size();                            // 3
            show("rows", rows);
        }
        {
            List<Sale> sales = List.of(new Sale("apple", 5), new Sale("pear", 3));
            ReportService csvService = new ReportService(new CsvFormat());
            ReportService jsonService = new ReportService(new JsonLinesFormat());
            String csvHead = csvService.firstLine(sales);     // "item,units"
            show("csvHead", csvHead);
            String jsonHead = jsonService.firstLine(sales);   // {"item":"apple","units":5}
            show("jsonHead", jsonHead);
        }
        {
            List<Sale> sales = List.of(new Sale("apple", 5), new Sale("pear", 3));
            List<String> pipe = new PipeFormat().lines(sales);  // [item|units, apple|5, pear|3]
            show("pipe", pipe);
            int tsvRows = new TsvFormat().lines(sales).size();     // 3
            show("tsvRows", tsvRows);
        }
        {
            String file = new MonthlyReport(" June Sales ").fileName();  // "june-sales.csv"
            show("file", file);
        }
        {
            String saved = deliver(new Download("june.csv"));      // "saved june.csv"
            show("saved", saved);
            String mailed = deliver(new Email("ana@example.com"));  // "sent to ana@example.com"
            show("mailed", mailed);
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
