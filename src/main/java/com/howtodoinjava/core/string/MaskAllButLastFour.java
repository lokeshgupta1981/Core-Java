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
 * Examples for the tutorial "Mask a String Except the Last 4 Characters in Java".
 * https://howtodoinjava.com/java/string/mask-string-except-last-4-chars/
 */
public class MaskAllButLastFour {
    static String maskExceptLast(String value, int visible, char mask) {
        if (value == null) {
            return null;
        }
        int maskLength = value.length() - visible;
        if (maskLength <= 0) {
            return value;
        }
        return String.valueOf(mask).repeat(maskLength) + value.substring(maskLength);
    }
    public static void main(String[] args) throws Exception {
        {
            String card = "4111222233334444";
            int visible = Math.min(4, card.length());
            String masked = "*".repeat(card.length() - visible) + card.substring(card.length() - visible);   // "************4444"
            show("masked", masked);
            String pin = "123";
            String maskedPin = pin.length() <= 4 ? pin : "*".repeat(pin.length() - 4) + pin.substring(pin.length() - 4);   // "123"
            show("maskedPin", maskedPin);
        }
        {
            String cardMask = maskExceptLast("4111222233334444", 4, '*');   // "************4444"
            show("cardMask", cardMask);
            String account = maskExceptLast("987654321", 4, 'x');           // "xxxxx4321"
            show("account", account);
            String exact = maskExceptLast("6789", 4, '*');                  // "6789"
            show("exact", exact);
            String tiny = maskExceptLast("789", 4, '*');                    // "789"
            show("tiny", tiny);
            String empty = maskExceptLast("", 4, '*');                      // ""
            show("empty", empty);
            String absent = maskExceptLast(null, 4, '*');                   // null
            show("absent", absent);
        }
        {
            String byRegex = "4111222233334444".replaceAll(".(?=.{4})", "*");   // "************4444"
            show("byRegex", byRegex);
            String shortRegex = "789".replaceAll(".(?=.{4})", "*");               // "789"
            show("shortRegex", shortRegex);
        }
        {
            String spacedCard = "4111 2222 3333 4444".replaceAll("\\d(?=(?:\\D*\\d){4})", "*");   // "**** **** **** 4444"
            show("spacedCard", spacedCard);
            String dashedPhone = "555-010-7788".replaceAll("\\d(?=(?:\\D*\\d){4})", "*");          // "***-***-7788"
            show("dashedPhone", dashedPhone);
            String splitTail = "12-34-56".replaceAll("\\d(?=(?:\\D*\\d){4})", "*");              // "**-34-56"
            show("splitTail", splitTail);
        }
        {
            String charCount = "4111 2222 3333 4444".replaceAll("[^-](?=.{4})", "*");   // "***************4444"
            show("charCount", charCount);
            String fewDigits = "12-34-56".replaceAll("[^-](?=.{4})", "*");               // "**-*4-56"
            show("fewDigits", fewDigits);
        }
        {
            String handle = "ab" + Character.toString(0x1F600) + "cdef";
            String regexMasked = handle.replaceAll(".(?=.{4})", "*");                    // "***cdef"
            show("regexMasked", regexMasked);
            String helperMasked = maskExceptLast("x" + Character.toString(0x1F600) + "cde", 4, '*');
            boolean splitEmoji = Character.isLowSurrogate(helperMasked.charAt(2));       // true
            show("splitEmoji", splitEmoji);
        }
        {
            String email = "lokesh@example.com";
            String maskedEmail = email.replaceAll("(?<=.)[^@](?=[^@]*@)", "*");   // "l*****@example.com"
            show("maskedEmail", maskedEmail);
        }
        {
            String number = "4111222233334444";
            String middle = number.length() > 8
            ? number.substring(0, 4) + "*".repeat(number.length() - 8) + number.substring(number.length() - 4)
            : number;
            String both = middle;   // "4111********4444"
            show("both", both);
        }
        {
            char[] chars = "987654321".toCharArray();
            Arrays.fill(chars, 0, Math.max(0, chars.length - 4), '*');
            String filled = new String(chars);   // "*****4321"
            show("filled", filled);
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
