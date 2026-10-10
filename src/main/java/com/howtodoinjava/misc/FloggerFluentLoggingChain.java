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
 * Examples for the tutorial "Flogger Fluent Logging in Java: Rate Limits vs SLF4J 2".
 * https://howtodoinjava.com/java/library/fluent-logging-with-flogger/
 */
public class FloggerFluentLoggingChain {
    static public class SlfImportJob {
        private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SlfImportJob.class);

        void finish(String file, int orders, Exception problem) {
            log.atInfo().setMessage("Imported {} orders from {}").addArgument(orders).addArgument(file).log();
            log.atInfo().addKeyValue("file", file).addKeyValue("orders", orders).log("Import finished");
            log.atWarn().setCause(problem).log("Rejected row {}", 7);
        }
    }
    public static void main(String[] args) throws Exception {
        {
            java.util.logging.Logger jul = java.util.logging.Logger.getLogger("ImportJob");
            jul.setLevel(java.util.logging.Level.FINE);        // enables atFine() for this class
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
