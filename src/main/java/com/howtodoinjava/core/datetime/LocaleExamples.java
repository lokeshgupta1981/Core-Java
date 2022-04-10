package com.howtodoinjava.core.datetime;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class LocaleExamples {
  public static void main(final String[] args) {

    //1 Get Locale Basic Info
    Locale enUsLocale = new Locale("EN", "US");

    System.out.println(enUsLocale.toLanguageTag());
    System.out.println(enUsLocale.getDisplayName());
    System.out.println(enUsLocale.getDisplayLanguage());
    System.out.println(enUsLocale.getDisplayCountry());

    //2 Localized Resource Bundle

    Locale locale = new Locale("en", "US");
    //ResourceBundle labels = ResourceBundle.getBundle("error.messages",
    // locale);
    //System.out.println(labels.getString("resource.not.found"));

    //3 Format date
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG,
        DateFormat.LONG, enUsLocale);
    System.out.println(df.format(new Date()));

    //4 Format Numbers
    NumberFormat nf = NumberFormat.getInstance(enUsLocale);
    System.out.println(nf.format(123456789L));

    //5 Format Currency
    NumberFormat cf = NumberFormat.getCurrencyInstance(locale);
    String currency = cf.format(123.456);
    System.out.println(currency);

    //6 Decimal Format
  }
}
