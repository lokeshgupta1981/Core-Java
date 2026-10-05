package com.howtodoinjava.core.string;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

/**
 * Checks if a String contains a substring with String.contains(): case sensitivity, the null
 * argument, case-insensitive checks (toLowerCase(Locale.ROOT), regionMatches(), a
 * CASE_INSENSITIVE Pattern, Commons Lang), contains() vs indexOf() vs matches(), checking any or
 * all of several substrings with streams, StringBuilder and other CharSequence types, a single
 * char, and whole-word checks.
 *
 * <p>Run with: mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.core.string.Contains
 */
public class Contains {

  public static void main(String[] args) {
    // 1. Basic usage
    print("\"Hello World\".contains(\"World\")", "Hello World".contains("World"));
    print("\"Hello World\".contains(\"world\")", "Hello World".contains("world"));
    print("\"Hello World\".contains(\"lo W\")", "Hello World".contains("lo W"));
    print("\"Hello World\".contains(\"\")", "Hello World".contains(""));
    print("\"\".contains(\"\")", "".contains(""));
    print("toLowerCase(ROOT).contains(\"world\")",
        "Hello World".toLowerCase(Locale.ROOT).contains("world"));
    try {
      String word = null;
      "Hello World".contains(word);
    } catch (NullPointerException e) {
      print("contains(null)", e);
    }

    // 2. Literal text, not regex
    String recipe = "Mix flour, milk and eggs.";
    print("recipe.contains(\"milk\")", recipe.contains("milk"));
    print("recipe.contains(\".\")", recipe.contains("."));
    print("recipe.contains(\"\\\\s\")", recipe.contains("\\s"));
    print("recipe.contains(\"m.lk\")", recipe.contains("m.lk"));

    // 3. Case-insensitive
    String title = "Pancakes With MAPLE Syrup";
    String search = "Maple";
    print("toLowerCase(ROOT).contains",
        title.toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT)));
    print("regionMatches helper", containsIgnoreCase(title, "maple"));
    print("regionMatches helper (missing)", containsIgnoreCase(title, "honey"));
    Pattern maple = Pattern.compile(Pattern.quote("maple"), Pattern.CASE_INSENSITIVE);
    print("Pattern CASE_INSENSITIVE", maple.matcher(title).find());
    print("Strings.CI.contains", Strings.CI.contains(title, "maple"));
    print("Strings.CI.contains(null, ..)", Strings.CI.contains(null, "maple"));
    // Older name, deprecated since commons-lang3 3.18.0 but still working
    @SuppressWarnings("deprecation")
    boolean old = StringUtils.containsIgnoreCase(title, "maple");
    print("StringUtils.containsIgnoreCase", old);

    // 3.1 Locale trap
    Locale turkish = Locale.forLanguageTag("tr");
    print("\"TITLE\".toLowerCase(tr) equals \"t\\u0131tle\"",
        "TITLE".toLowerCase(turkish).equals("t\u0131tle"));
    print("toLowerCase(tr).contains(\"title\")", "TITLE".toLowerCase(turkish).contains("title"));
    print("toLowerCase(ROOT).contains(\"title\")", "TITLE".toLowerCase(Locale.ROOT).contains("title"));

    // 4. contains() vs indexOf() vs matches()
    String greeting = "Hello World";
    print("indexOf(\"World\")", greeting.indexOf("World"));
    print("indexOf(\"Java\")", greeting.indexOf("Java"));
    print("matches(\"World\")", greeting.matches("World"));
    print("matches(\".*World.*\")", greeting.matches(".*World.*"));
    print("matches(\".*W.rld.*\")", greeting.matches(".*W.rld.*"));
    print("matches(\"(?i).*world.*\")", greeting.matches("(?i).*world.*"));
    print("lastIndexOf(\"o\")", greeting.lastIndexOf("o"));

    // 5. Any or all of several substrings
    List<String> allergens = List.of("nuts", "milk", "eggs");
    print("anyMatch", allergens.stream().anyMatch(recipe::contains));
    print("allMatch", allergens.stream().allMatch(recipe::contains));
    print("noneMatch", allergens.stream().noneMatch(recipe::contains));
    print("filter found", allergens.stream().filter(recipe::contains).toList());
    print("Strings.CS.containsAny", Strings.CS.containsAny(recipe, "nuts", "milk"));
    print("Strings.CI.containsAny", Strings.CI.containsAny("MILK", "nuts", "milk"));
    print("Pattern alternation",
        Pattern.compile("nuts|milk|eggs").matcher(recipe).find());

    // 6. CharSequence and StringBuilder
    StringBuilder list = new StringBuilder("flour, sugar");
    print("\"Add sugar now\".contains(sb sugar)", "Add sugar now".contains(new StringBuilder("sugar")));
    print("sb.indexOf(\"sugar\") >= 0", list.indexOf("sugar") >= 0);
    print("sb.toString().contains(\"salt\")", list.toString().contains("salt"));

    // 7. Single char
    print("indexOf('W') >= 0", greeting.indexOf('W') >= 0);
    print("contains(String.valueOf('W'))", greeting.contains(String.valueOf('W')));
    print("contains(\"\" + 'w')", greeting.contains("" + 'w'));
    print("chars().anyMatch(Character::isDigit)", greeting.chars().anyMatch(Character::isDigit));

    // 8. Whole word
    String note = "The category is desserts";
    print("note.contains(\"cat\")", note.contains("cat"));
    print("whole word \\bcat\\b", Pattern.compile("\\bcat\\b").matcher(note).find());
    print("whole word \\bcategory\\b", Pattern.compile("\\bcategory\\b").matcher(note).find());

    // 9. String.contains() vs List.contains()
    print("List.contains(\"mil\")", List.of("milk", "eggs").contains("mil"));
    print("\"milk\".contains(\"mil\")", "milk".contains("mil"));

    // 10. Null-safe check
    String maybeNull = null;
    print("maybeNull != null && contains", maybeNull != null && maybeNull.contains("milk"));
    print("Strings.CS.contains(null, \"milk\")", Strings.CS.contains(maybeNull, "milk"));
  }

  /** Case-insensitive contains without creating lower-case copies of the strings. */
  public static boolean containsIgnoreCase(String text, String part) {
    for (int i = 0; i <= text.length() - part.length(); i++) {
      if (text.regionMatches(true, i, part, 0, part.length())) {
        return true;
      }
    }
    return false;
  }

  private static void print(String label, Object value) {
    System.out.println(label + " -> " + value);
  }
}
