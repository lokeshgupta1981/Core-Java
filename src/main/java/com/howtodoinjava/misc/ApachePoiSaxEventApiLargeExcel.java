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
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Examples for the tutorial "Apache POI SAX Parser: Read Large Excel Files in Java".
 * https://howtodoinjava.com/java/library/poi-read-excel-with-sax-parser/
 */
public class ApachePoiSaxEventApiLargeExcel {
    static class ColumnSum extends DefaultHandler {
        private final String column;
        private final StringBuilder text = new StringBuilder();
        private boolean inValue;
        private boolean wanted;
        private int row;
        double sum;

        ColumnSum(String column) {
            this.column = column;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) {
            switch (localName) {
                case "row" -> row = Integer.parseInt(atts.getValue("r"));
                case "c" -> {
                    String ref = atts.getValue("r");
                    boolean number = atts.getValue("t") == null || "n".equals(atts.getValue("t"));
                    wanted = row > 1 && number && ref.equals(column + row);
                }
                case "v" -> {
                    inValue = wanted;
                    text.setLength(0);
                }
                default -> { }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (inValue) {
                text.append(ch, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if ("v".equals(localName) && inValue) {
                sum += Double.parseDouble(text.toString());
                inValue = false;
            }
        }
    }
    static record PriceRow(String item, int qty, double price) {}
    public static void main(String[] args) throws Exception {
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
