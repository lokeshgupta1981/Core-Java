package com.howtodoinjava.core.basic;

import java.math.BigInteger;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class PrintFormattedOutput {
  public static void main(String[] args) {
    String name = "Lokesh";
    Integer age = 40;

    String formattedString_1 = new Formatter().format("My name is %s and I am %d years old.\n", name, age).toString();
    String formattedString_2 = String.format("My name is %s and I am %d years old.\n", name, age);

    System.out.print(formattedString_1);
    System.out.print(formattedString_2);
    System.out.printf("My name is %s and I am %d years old.\n", name, age);

    //Format null
    System.out.format("The value is %s%n", null);

    //Line separator
    System.out.format("Line 1%nLine 2%nLine 3%n");

    //Argument Index
    System.out.printf("The product of %1$d and %1$d is : %2$d%n", 2, 4);

    //UPPERCASE
    System.out.printf("The blog name is : %S%n", "howtodoinjava");

    System.out.printf("'%15s' %n", "howtodoinjava");
    System.out.printf("'%-15s' %n", "howtodoinjava");

    //padded substring
    System.out.printf("'%15.3s' %n", "howtodoinjava");    //Prints '            how'

    //Percent sign
    System.out.printf("The interest rate is %d%%  %n", 15);    //Prints 'The interest rate is 15%'

    //Simple Number
    System.out.printf("The int value is %d %n", 10);
    System.out.printf("The long value is %d %n", 10L);
    System.out.printf("The BigInteger value is %d %n", BigInteger.TEN);

    //Floating point numbers
    System.out.printf("The float value is %f %n", 10.0f);
    System.out.printf("The float value is %.2f %n", 10.0f);   //Prints 'The float value is 10.00'

    //Locale formatting
    System.out.printf(Locale.US, "The amount in US is %,d %n", 1000000);
    System.out.printf(Locale.ITALY, "The amount in UK is %,d %n", 1000000);

    Date today = new Date();
    System.out.printf("%1$tA, %1$tB %1$tY %n", today); //Prints 'Tuesday, September 2022'

    System.out.printf("%1$tH:%1$tM:%1$tS %n", today); //Prints '22:37:15'
    System.out.printf("%tT %n", today); //Prints '22:37:15'
    System.out.printf("%tF %n", today); //Prints '2022-09-13'
    System.out.printf("%tc %n", today); //Prints 'Tue Sep 13 22:43:17 IST 2022'
  }
}
