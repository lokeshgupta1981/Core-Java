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
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;

/**
 * Examples for the tutorial "PowerMock With JUnit and Mockito: Examples and Alternatives".
 * https://howtodoinjava.com/java/library/mock-testing-using-powermock-with-junit-and-mockito/
 */
public class PowermockVsMockito5StaticMocking {
    static class TaxRates {
        static double rateFor(String country) {
            return 0.20;                                  // in production, calls the tax service
        }
    }
    static class PdfRenderer {
        byte[] render(String text) {
            return text.getBytes(StandardCharsets.UTF_8);
        }
    }
    static class InvoiceService {
        double total(String country, double net) {
            return net + net * TaxRates.rateFor(country);
        }

        final String currency() {
            return "EUR";
        }

        int pdfSize(String text) {
            return new PdfRenderer().render(header() + text).length;
        }

        private String header() {
            return "INVOICE ";
        }
    }
    static public class InvoicePowerMockSetupTest {
    }
    public static void main(String[] args) throws Exception {
        {
            InvoiceService service = new InvoiceService();
            double total = service.total("DE", 100.0);                   // 120.0
            show("total", total);
            int size = service.pdfSize("total 120.0");                   // 19
            show("size", size);
        }
        {
            InvoiceService service = new InvoiceService();
            int withHeader = service.pdfSize("");                        // 8, the header "INVOICE " alone
            show("withHeader", withHeader);
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
