package org.tessell.util;

/** Very basic format/parse routines for decimal numbers. */
public class NumberUtils {

  /** @return {@code value} parsed as a double, and rounded to {@code decimalPlaces} */
  public static double parse(String value, int decimalPlaces) throws NumberFormatException {
    value = value.replaceAll("[\\$,]", "");
    // Trim off insignificant digits by, when decimalPlaces=2, shifting * 100, round, / 100.
    double adjust = Math.pow(10, decimalPlaces);
    double parsed = Double.parseDouble(value);
    return Math.round(parsed * adjust) / adjust;
  }

  /** @return {@code value} formatted with commas, e.g. 1000 as "1,000". */
  public static String format(final long value) {
    boolean negative = value < 0;
    final String num = String.valueOf(Math.abs(value));
    String formattedNum = "";
    for (int i = num.length() - 1, j = 0; i >= 0; i--, j++) {
      formattedNum += num.charAt(j);
      if (i % 3 == 0 && i != 0) {
        formattedNum += ",";
      }
    }
    return negative ? "-" + formattedNum : formattedNum;
  }

  /** @return {@code value} formatted as commas to {@code decimalPlaces}, e.g. 1000.125 as "1,000.13". */
  public static String format(double value, int decimalPlaces) {
    if (decimalPlaces == 0) {
      return format(Math.round(value));
    }
    final long integerPart = (long) value;
    // Move 123.456 -> 45.6 when decimalPlaces = 2
    double adjust = Math.pow(10, decimalPlaces);
    // We actually do 45.6 + 100 (adjust) so that we get "100" (instead of just "0") and then drop the leading 1
    long decimalPart = Math.round((Math.abs(value) * adjust) % adjust) + (long) adjust;
    return format(integerPart) + "." + Long.toString(decimalPart).substring(1);
  }
}
