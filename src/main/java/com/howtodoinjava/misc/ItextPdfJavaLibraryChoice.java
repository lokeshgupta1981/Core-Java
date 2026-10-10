package com.howtodoinjava.misc;

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
 * Examples for the tutorial "Read and Write PDF in Java with iText 9 (and OpenPDF)".
 * https://howtodoinjava.com/java/library/read-generate-pdf-java-itext/
 */
public class ItextPdfJavaLibraryChoice {
    static void writeMenu(OutputStream out) throws IOException {
        try (org.openpdf.text.Document document = new org.openpdf.text.Document(org.openpdf.text.PageSize.A4)) {
            org.openpdf.text.pdf.PdfWriter.getInstance(document, out);
            document.addTitle("Daily menu");
            document.open();
            document.add(new org.openpdf.text.Paragraph("Daily menu",
            org.openpdf.text.FontFactory.getFont(org.openpdf.text.FontFactory.HELVETICA_BOLD, 18)));
            org.openpdf.text.pdf.PdfPTable table = new org.openpdf.text.pdf.PdfPTable(2);
            table.addCell("Latte");
            table.addCell("3.50");
            document.add(table);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            ByteArrayOutputStream menu = new ByteArrayOutputStream();
            writeMenu(menu);
            org.openpdf.text.pdf.PdfReader reader = new org.openpdf.text.pdf.PdfReader(menu.toByteArray());
            String producer = reader.getInfo().get("Producer");   // "OpenPDF 3.0.5"
            show("producer", producer);
            reader.close();
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
