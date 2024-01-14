package com.howtodoinjava.core.datetime;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class LocaleExamples {
  public static void main(final String[] args) throws ParseException {

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

    //Specifying Language Range
    Locale.LanguageRange lr = new Locale.LanguageRange("de-*");

    //5 Format Currency
    /*NumberFormat cf = NumberFormat.getCurrencyInstance(locale);
    String currency = cf.format(123.456);
    System.out.println(currency);*/


    double amount = 1234.567;
    NumberFormat localizedCurrencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    String formattedCurrency = localizedCurrencyFormat.format(amount);

    System.out.println(formattedCurrency);  // Output: $1,234.57

    NumberFormat frCurrencyFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE);
    String frFormattedCurrency = frCurrencyFormat.format(amount);

    System.out.println(frFormattedCurrency);  // Output: $1,234.57

    String currencyString = "$1,234.57";

    try {
      Number parsedNumber = localizedCurrencyFormat.parse(currencyString);
      double parsedAmount = parsedNumber.doubleValue();
      System.out.println(parsedAmount);  // Output: 1234.57
    } catch (ParseException e) {
      e.printStackTrace();
    }

    try {
    NumberFormat roundedCurrencyFormat = NumberFormat.getCurrencyInstance();
    roundedCurrencyFormat.setRoundingMode(RoundingMode.HALF_UP);
    String formattedValue = roundedCurrencyFormat.format(123456.789d);
    System.out.println(formattedValue);  // Output: 1234.57
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
