package com.howtodoinjava.core.string;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Converts a String to an int with Integer.parseInt(), Integer.valueOf(), Integer.decode(),
 * Long.parseLong(), NumberUtils.toInt() and Character.getNumericValue(), and shows how each
 * method handles whitespace, signs, radix, overflow and invalid input.
 *
 * <p>Run with: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.core.string.StringToInt
 */
public class StringToInt {

  public static void main(String[] args) throws ParseException {

    // 1. Integer.parseInt() returns a primitive int
    int age = Integer.parseInt("37");
    print("Integer.parseInt(\"37\")", age);
    print("Integer.parseInt(\"-15\")", Integer.parseInt("-15"));
    print("Integer.parseInt(\"+7\")", Integer.parseInt("+7"));
    print("Integer.parseInt(\"007\")", Integer.parseInt("007"));
    print("Integer.parseInt(\"age=37\", 4, 6, 10)", Integer.parseInt("age=37", 4, 6, 10));

    // 2. Integer.valueOf() returns an Integer object
    Integer boxedAge = Integer.valueOf("37");
    print("Integer.valueOf(\"37\")", boxedAge);
    print("Integer.valueOf(\"127\") == Integer.valueOf(\"127\")",
        Integer.valueOf("127") == Integer.valueOf("127"));
    print("Integer.valueOf(\"128\") == Integer.valueOf(\"128\")",
        Integer.valueOf("128") == Integer.valueOf("128"));
    print("Integer.valueOf(\"128\").equals(Integer.valueOf(\"128\"))",
        Integer.valueOf("128").equals(Integer.valueOf("128")));

    // 3. Radix
    print("Integer.parseInt(\"1010\", 2)", Integer.parseInt("1010", 2));
    print("Integer.parseInt(\"17\", 8)", Integer.parseInt("17", 8));
    print("Integer.parseInt(\"ff\", 16)", Integer.parseInt("ff", 16));
    print("Integer.parseInt(\"-FF\", 16)", Integer.parseInt("-FF", 16));
    print("Integer.parseInt(\"zz\", 36)", Integer.parseInt("zz", 36));
    attempt("Integer.parseInt(\"0xff\", 16)", () -> Integer.parseInt("0xff", 16));
    print("Integer.decode(\"0xff\")", Integer.decode("0xff"));
    print("Integer.decode(\"#ff\")", Integer.decode("#ff"));
    print("Integer.decode(\"010\")", Integer.decode("010"));
    print("Integer.decode(\"-0x10\")", Integer.decode("-0x10"));
    attempt("Integer.decode(\"08\")", () -> Integer.decode("08"));

    // 4. Invalid input throws NumberFormatException
    attempt("Integer.parseInt(\"abc\")", () -> Integer.parseInt("abc"));
    attempt("Integer.parseInt(\"\")", () -> Integer.parseInt(""));
    attempt("Integer.parseInt(null)", () -> Integer.parseInt(null));
    attempt("Integer.parseInt(\" 37\")", () -> Integer.parseInt(" 37"));
    attempt("Integer.parseInt(\"37 \")", () -> Integer.parseInt("37 "));
    attempt("Integer.parseInt(\"12.5\")", () -> Integer.parseInt("12.5"));
    attempt("Integer.parseInt(\"1,000\")", () -> Integer.parseInt("1,000"));
    attempt("Integer.parseInt(\"- 5\")", () -> Integer.parseInt("- 5"));
    attempt("Integer.parseInt(\"2147483648\")", () -> Integer.parseInt("2147483648"));
    attempt("Integer.parseInt(\"ff\")", () -> Integer.parseInt("ff"));
    attempt("Integer.parseInt(\"zz\", 16)", () -> Integer.parseInt("zz", 16));
    print("Integer.parseInt(\" 37 \".strip())", Integer.parseInt(" 37 ".strip()));
    print("Integer.parseInt(\"2147483647\")", Integer.parseInt("2147483647"));
    print("Integer.parseInt(\"-2147483648\")", Integer.parseInt("-2147483648"));

    // 4.1 Catching the exception
    String input = "3x";
    try {
      int quantity = Integer.parseInt(input);
      System.out.println("Quantity: " + quantity);
    } catch (NumberFormatException e) {
      System.out.println("Quantity must be a whole number. " + e.getMessage());
    }

    // 5. Safe helpers
    print("tryParseInt(\"37\")", tryParseInt("37"));
    print("tryParseInt(\" 37 \")", tryParseInt(" 37 "));
    print("tryParseInt(\"abc\")", tryParseInt("abc"));
    print("tryParseInt(null)", tryParseInt(null));
    print("tryParseInt(\"abc\").orElse(-1)", tryParseInt("abc").orElse(-1));
    print("parseIntOrDefault(\"abc\", 0)", parseIntOrDefault("abc", 0));
    print("parseIntOrDefault(\"40\", 0)", parseIntOrDefault("40", 0));

    // 6. Big values
    print("Long.parseLong(\"3000000000\")", Long.parseLong("3000000000"));
    attempt("Math.toIntExact(Long.parseLong(\"3000000000\"))",
        () -> Math.toIntExact(Long.parseLong("3000000000")));
    print("new BigInteger(\"99999999999999999999\")", new BigInteger("99999999999999999999"));
    print("(int) Long.parseLong(\"3000000000\")", (int) Long.parseLong("3000000000"));
    print("Integer.parseUnsignedInt(\"4294967295\")", Integer.parseUnsignedInt("4294967295"));
    print("Integer.toUnsignedString(-1)", Integer.toUnsignedString(-1));

    // 7. Apache Commons Lang NumberUtils
    print("NumberUtils.toInt(\"37\")", NumberUtils.toInt("37"));
    print("NumberUtils.toInt(\"abc\")", NumberUtils.toInt("abc"));
    print("NumberUtils.toInt(null)", NumberUtils.toInt(null));
    print("NumberUtils.toInt(\" 37\")", NumberUtils.toInt(" 37"));
    print("NumberUtils.toInt(\"abc\", -1)", NumberUtils.toInt("abc", -1));
    print("NumberUtils.isParsable(\"-37\")", NumberUtils.isParsable("-37"));
    print("NumberUtils.isParsable(\"12.5\")", NumberUtils.isParsable("12.5"));
    print("NumberUtils.isDigits(\"-37\")", NumberUtils.isDigits("-37"));

    // 8. Single characters
    print("Character.getNumericValue('7')", Character.getNumericValue('7'));
    print("'7' - '0'", '7' - '0');
    print("Character.digit('7', 10)", Character.digit('7', 10));
    print("Character.getNumericValue('a')", Character.getNumericValue('a'));
    print("Character.digit('a', 10)", Character.digit('a', 10));
    print("Character.getNumericValue('-')", Character.getNumericValue('-'));
    print("Integer.parseInt(String.valueOf('7'))", Integer.parseInt(String.valueOf('7')));
    print("\"2026\".chars().map(Character::getNumericValue).sum()",
        "2026".chars().map(Character::getNumericValue).sum());

    // 9. Streams
    List<String> quantities = List.of("5", "3", "8");
    int total = quantities.stream().mapToInt(Integer::parseInt).sum();
    print("sum of [5, 3, 8]", total);
    List<Integer> numbers = quantities.stream().map(Integer::valueOf).toList();
    print("map(Integer::valueOf).toList()", numbers);

    List<String> userInput = List.of("5", "x", " 3 ", "", "8");
    attempt("mapToInt(Integer::parseInt) on [5, x,  3 , , 8]",
        () -> userInput.stream().mapToInt(Integer::parseInt).sum());
    int validTotal = userInput.stream()
        .flatMapToInt(s -> tryParseInt(s).stream())
        .sum();
    print("flatMapToInt(tryParseInt) sum", validTotal);
    int[] parsed = Stream.of("5", "x", "8").mapToInt(s -> parseIntOrDefault(s, 0)).toArray();
    print("mapToInt(parseIntOrDefault(s, 0))", java.util.Arrays.toString(parsed));

    // 10. Other formats
    print("NumberFormat US parse(\"1,234\").intValue()",
        NumberFormat.getIntegerInstance(Locale.US).parse("1,234").intValue());
    print("Integer.parseInt(\"1,234\".replace(\",\", \"\"))",
        Integer.parseInt("1,234".replace(",", "")));
    print("(int) Double.parseDouble(\"12.7\")", (int) Double.parseDouble("12.7"));
    print("(int) Math.round(Double.parseDouble(\"12.7\"))",
        (int) Math.round(Double.parseDouble("12.7")));
    attempt("new java.math.BigDecimal(\"12.7\").intValueExact()",
        () -> new java.math.BigDecimal("12.7").intValueExact());
    print("Integer.parseInt(\"\\u0661\\u0662\")", Integer.parseInt("\u0661\u0662"));
    print("Integer.parseInt(\"\\u0967\\u0968\")", Integer.parseInt("\u0967\u0968"));
    print("Integer.parseInt(\"\\uFF11\\uFF12\")", Integer.parseInt("\uFF11\uFF12"));
    print("NumberFormat GERMANY parse(\"1.234\").intValue()",
        NumberFormat.getIntegerInstance(Locale.GERMANY).parse("1.234").intValue());
    try (Scanner scanner = new Scanner("37 15")) {
      print("new Scanner(\"37 15\").nextInt()", scanner.nextInt());
    }
    print("Integer.toString(37)", Integer.toString(37));
    print("String.valueOf(37)", String.valueOf(37));
  }

  /**
   * Returns the int value of the text, or an empty OptionalInt when the text is null, blank or
   * not a valid int. Leading and trailing whitespace is ignored.
   */
  public static OptionalInt tryParseInt(String text) {
    if (text == null) {
      return OptionalInt.empty();
    }
    try {
      return OptionalInt.of(Integer.parseInt(text.strip()));
    } catch (NumberFormatException e) {
      return OptionalInt.empty();
    }
  }

  /**
   * Returns the int value of the text, or the default value when the text cannot be parsed.
   */
  public static int parseIntOrDefault(String text, int defaultValue) {
    return tryParseInt(text).orElse(defaultValue);
  }

  private static void print(String expression, Object result) {
    System.out.println(expression + " -> " + result);
  }

  private static void attempt(String expression, Supplier<Object> call) {
    try {
      print(expression, call.get());
    } catch (RuntimeException e) {
      print(expression, e.getClass().getName() + ": " + e.getMessage());
    }
  }
}
