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
 * Examples for the tutorial "Jayway JsonPath Tutorial: Read and Filter JSON in Java".
 * https://howtodoinjava.com/java/library/jsonpath-tutorial/
 */
public class JsonpathExpressionForecastJson {
    static String forecastJson() {
        return """
        {
            "city": "Pune",
            "units": {"temp": "C", "rain": "mm"},
            "days": [
            {"date": "2026-10-10", "high": 31, "low": 21, "rain": 0.0, "summary": "Sunny"},
            {"date": "2026-10-11", "high": 29, "low": 20, "rain": 4.5, "summary": "Showers"},
            {"date": "2026-10-12", "high": 27, "low": 19, "rain": 12.5, "summary": "Rain", "alerts": ["flood"]}
            ]
        }
        """;
    }
    static record Day(String date, int high, int low, double rain, String summary) {}
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
