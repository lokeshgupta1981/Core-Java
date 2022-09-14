package com.howtodoinjava.core.basic;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import org.apache.commons.math3.dfp.DfpField.RoundingMode;
import org.apache.commons.math3.util.Precision;

public class RoundOffFloats {

  public static void main(String[] args) {
    float number = 123.456f;

    //1. Apache commons Math's Precision
    float roundedOffNumber = Precision.round(number, 2, RoundingMode.ROUND_HALF_EVEN.ordinal());
    System.out.println(roundedOffNumber); //123.45

    //2 - Math.round()
    System.out.println(roundUp(number, 2));

    //3 - Print formatted value with DecimalFormat
    DecimalFormat df = new DecimalFormat("###.##");
    System.out.println(df.format(number));

    //4 - BigDecimal
    BigDecimal bd = new BigDecimal(number);
    BigDecimal roundedOffBd = bd.setScale(2, java.math.RoundingMode.HALF_EVEN);
    System.out.println(roundedOffBd);
  }

  public static double roundUp(double value, int places) {
    double scale = Math.pow(10, places);
    return Math.round(value * scale) / scale;
  }

}
