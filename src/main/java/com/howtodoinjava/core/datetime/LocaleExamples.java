package com.howtodoinjava.core.datetime;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.time.format.*;
import java.time.temporal.*;
import java.time.chrono.*;
import java.time.zone.*;
import java.text.*;

/**
 * Examples for the tutorial "Java Locale: Create, Match and Use Locales with Examples".
 * https://howtodoinjava.com/java/date-time/java-locale-api-examples/
 */
public class LocaleExamples {

    public static void main(String[] args) throws Exception {
        {
            Locale canadianFrench = Locale.of("fr", "CA");
            String tag = canadianFrench.toLanguageTag();                       // "fr-CA"
            show("tag", tag);
            String name = canadianFrench.getDisplayName(Locale.US);            // "French (Canada)"
            show("name", name);
            String number = NumberFormat.getInstance(canadianFrench).format(1234.5);   // "1 234,5"
            show("number", number);
            String month = LocalDate.of(2026, 3, 14).format(DateTimeFormatter.ofPattern("d MMMM", canadianFrench));   // "14 mars"
            show("month", month);
        }
        {
            Locale taiwan = Locale.forLanguageTag("zh-Hant-TW");
            String language = taiwan.getLanguage();          // "zh"
            show("language", language);
            String script = taiwan.getScript();              // "Hant"
            show("script", script);
            String region = taiwan.getCountry();             // "TW"
            show("region", region);
            String english = taiwan.getDisplayName(Locale.US);   // "Chinese (Traditional, Taiwan)"
            show("english", english);
            String iso3 = Locale.US.getISO3Country();        // "USA"
            show("iso3", iso3);
        }
        {
            String usRegion = Locale.US.getCountry();                 // "US"
            show("usRegion", usRegion);
            String englishRegion = Locale.ENGLISH.getCountry();       // ""
            show("englishRegion", englishRegion);
            boolean sameAsBuilt = Locale.US.equals(new Locale.Builder().setLanguage("en").setRegion("US").build());   // true
            show("sameAsBuilt", sameAsBuilt);
        }
        {
            Locale hindiIndia = Locale.of("hi", "IN");     // hi_IN
            show("hindiIndia", hindiIndia);
            Locale spanish = Locale.of("es");              // es
            show("spanish", spanish);
            Locale normalized = Locale.of("EN", "us");     // en_US
            show("normalized", normalized);
            boolean same = normalized.equals(Locale.US);   // true
            show("same", same);
        }
        {
            Locale brazil = new Locale.Builder().setLanguage("pt").setRegion("BR").build();   // pt_BR
            show("brazil", brazil);
            Locale thaiDigits = new Locale.Builder().setLanguage("th").setRegion("TH").setUnicodeLocaleKeyword("nu", "thai").build();   // th_TH_#u-nu-thai
            show("thaiDigits", thaiDigits);
            try { Locale bad = new Locale.Builder().setLanguage("en").setRegion("USA1").build(); show("bad", bad); } catch (Throwable _t) { System.out.println("bad -> " + _t); }
        }
        {
            Locale fromHeader = Locale.forLanguageTag("en-GB");                // en_GB
            show("fromHeader", fromHeader);
            Locale withScript = Locale.forLanguageTag("sr-Latn-RS");          // sr_RS_#Latn
            show("withScript", withScript);
            Locale wrongSeparator = Locale.forLanguageTag("en_US");           // empty locale, tag "und"
            show("wrongSeparator", wrongSeparator);
            String undefined = wrongSeparator.toLanguageTag();                // "und"
            show("undefined", undefined);
        }
        {
            List<Locale.LanguageRange> wanted = Locale.LanguageRange.parse("fr-CH,fr;q=0.9,en;q=0.8");
            List<Locale> supported = List.of(Locale.US, Locale.FRANCE, Locale.GERMANY);
            List<Locale> matches = Locale.filter(wanted, supported);          // [fr_FR, en_US]
            show("matches", matches);
            Locale best = Locale.lookup(wanted, supported);                   // null
            show("best", best);
            Locale bestByLanguage = Locale.lookup(wanted, List.of(Locale.US, Locale.FRENCH, Locale.GERMANY));   // fr
            show("bestByLanguage", bestByLanguage);
        }
        {
            Locale original = Locale.getDefault();
            Locale.setDefault(Locale.FRANCE);
            String monthName = LocalDate.of(2026, 3, 14).format(DateTimeFormatter.ofPattern("MMMM"));   // "mars"
            show("monthName", monthName);
            Locale.setDefault(original);
        }
        {
            Locale.setDefault(Locale.Category.DISPLAY, Locale.US);
            Locale.setDefault(Locale.Category.FORMAT, Locale.GERMANY);
            String amount = String.format("%,.2f", 1234.5);                 // "1.234,50"
            show("amount", amount);
            String label = Locale.GERMANY.getDisplayName();                 // "German (Germany)"
            show("label", label);
            Locale.setDefault(Locale.US);
        }
        {
            ResourceBundle french = ResourceBundle.getBundle("messages", Locale.FRANCE);
            String frText = french.getString("booking.confirmed");    // "Votre réservation est confirmée"
            show("frText", frText);
            ResourceBundle german = ResourceBundle.getBundle("messages", Locale.GERMANY);
            String deText = german.getString("booking.confirmed");    // "Your booking is confirmed"
            show("deText", deText);
        }
        {
            LocalDate checkIn = LocalDate.of(2026, 3, 14);
            String usDate = checkIn.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.US));   // "March 14, 2026"
            show("usDate", usDate);
            String spDate = checkIn.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.of("es", "ES")));   // "14 de marzo de 2026"
            show("spDate", spDate);
            String weekday = checkIn.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ITALY);   // "sabato"
            show("weekday", weekday);
        }
        {
            String usNumber = NumberFormat.getInstance(Locale.US).format(123456789.123);         // "123,456,789.123"
            show("usNumber", usNumber);
            String deNumber = NumberFormat.getInstance(Locale.GERMANY).format(123456789.123);    // "123.456.789,123"
            show("deNumber", deNumber);
            String usPrice = NumberFormat.getCurrencyInstance(Locale.US).format(123.456);        // "$123.46"
            show("usPrice", usPrice);
            String percent = NumberFormat.getPercentInstance(Locale.US).format(0.256);           // "26%"
            show("percent", percent);
        }
        {
            String turkish = "title".toUpperCase(Locale.of("tr"));     // "TİTLE"
            show("turkish", turkish);
            String safeKey = "title".toUpperCase(Locale.ROOT);          // "TITLE"
            show("safeKey", safeKey);
        }
        {
            long count = Locale.availableLocales().count();   // 1158
            show("count", count);
        }
        {
            String country = Locale.of("", "JP").getDisplayCountry(Locale.US);       // "Japan"
            show("country", country);
            String inGerman = Locale.of("", "JP").getDisplayCountry(Locale.GERMANY);  // "Japan"
            show("inGerman", inGerman);
            String inFrench = Locale.of("", "DE").getDisplayCountry(Locale.FRANCE);   // "Allemagne"
            show("inFrench", inFrench);
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
