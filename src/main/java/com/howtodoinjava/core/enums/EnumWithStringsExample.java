package com.howtodoinjava.core.enums;

import java.util.Arrays;
import java.util.Optional;

public class EnumWithStringsExample {

  // An enum that overrides toString() to return its label
  enum Genre {
    FICTION("Fiction"), SCIENCE_FICTION("Science Fiction");

    private final String label;

    Genre(String label) {
      this.label = label;
    }

    @Override
    public String toString() {
      return label;
    }
  }

  public static void main(String[] args) {
    quickReference();
    creatingAndIterating();
    enumToString();
    stringToEnum();
    lookupByFieldValue();
    multipleValues();
    switchExamples();
  }

  static void quickReference() {
    System.out.println("--- quick reference ---");
    BookFormat format = BookFormat.EBOOK;
    String name = format.name();                                     // "EBOOK"
    String text = format.toString();                                 // "EBOOK"
    String label = format.getLabel();                                // "E-Book"
    BookFormat byName = BookFormat.valueOf("EBOOK");                 // EBOOK
    Optional<BookFormat> byLabel = BookFormat.fromLabel("e-book");   // Optional[EBOOK]
    System.out.println(name + " " + text + " " + label + " " + byName + " " + byLabel);
    try {
      BookFormat bad = BookFormat.valueOf("E-Book");                 // IllegalArgumentException
      System.out.println(bad);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
  }

  static void creatingAndIterating() {
    System.out.println("--- creating and iterating ---");
    String label = BookFormat.HARDCOVER.getLabel();                  // "Hardcover"
    String code = BookFormat.HARDCOVER.getCode();                    // "HC"
    System.out.println(label + " " + code);

    for (BookFormat format : BookFormat.values()) {
      System.out.println(format.name() + " = " + format.getLabel() + " (" + format.getCode() + ")");
    }
  }

  static void enumToString() {
    System.out.println("--- enum to String ---");
    BookFormat format = BookFormat.EBOOK;
    String name = format.name();                                     // "EBOOK"
    String text = format.toString();                                 // "EBOOK"
    String label = format.getLabel();                                // "E-Book"
    String joined = "Format: " + format;                             // "Format: EBOOK"
    System.out.println(name + " " + text + " " + label + " | " + joined);

    Genre genre = Genre.SCIENCE_FICTION;
    String genreName = genre.name();                                 // "SCIENCE_FICTION"
    String genreText = genre.toString();                             // "Science Fiction"
    String genreJoined = "Genre: " + genre;                          // "Genre: Science Fiction"
    System.out.println(genreName + " | " + genreText + " | " + genreJoined);
    try {
      Genre bad = Genre.valueOf("Science Fiction");                  // IllegalArgumentException
      System.out.println(bad);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
  }

  static void stringToEnum() {
    System.out.println("--- String to enum ---");
    BookFormat paperback = BookFormat.valueOf("PAPERBACK");          // PAPERBACK
    BookFormat viaClass = Enum.valueOf(BookFormat.class, "EBOOK");   // EBOOK
    System.out.println(paperback + " " + viaClass);

    try {
      BookFormat lower = BookFormat.valueOf("paperback");            // IllegalArgumentException
      System.out.println(lower);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
    try {
      BookFormat spaced = BookFormat.valueOf(" EBOOK ");             // IllegalArgumentException
      System.out.println(spaced);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
    try {
      BookFormat missing = BookFormat.valueOf(null);                 // NullPointerException
      System.out.println(missing);
    } catch (NullPointerException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }

    // Fix 1: normalize the input before valueOf()
    String input = " paperback ";
    BookFormat fixed = BookFormat.valueOf(input.strip().toUpperCase());  // PAPERBACK
    System.out.println(fixed);

    // Fix 2: a lookup method that returns Optional instead of throwing
    Optional<BookFormat> found = BookFormat.fromName(" ebook ");     // Optional[EBOOK]
    Optional<BookFormat> unknown = BookFormat.fromName("audio");     // Optional.empty
    Optional<BookFormat> noInput = BookFormat.fromName(null);        // Optional.empty
    BookFormat fallback = BookFormat.fromName("audio").orElse(BookFormat.PAPERBACK); // PAPERBACK
    System.out.println(found + " " + unknown + " " + noInput + " " + fallback);
    try {
      BookFormat required = BookFormat.fromName("audio")
          .orElseThrow(() -> new IllegalArgumentException("Unknown format: audio"));
      System.out.println(required);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
  }

  static void lookupByFieldValue() {
    System.out.println("--- lookup by field value ---");
    Optional<BookFormat> byLabel = BookFormat.fromLabel("E-Book");       // Optional[EBOOK]
    Optional<BookFormat> ignoreCase = BookFormat.fromLabel("e-book");    // Optional[EBOOK]
    Optional<BookFormat> unknown = BookFormat.fromLabel("Audio");        // Optional.empty
    Optional<BookFormat> fromMap = BookFormat.fromLabelMap("HARDCOVER"); // Optional[HARDCOVER]
    System.out.println(byLabel + " " + ignoreCase + " " + unknown + " " + fromMap);

    String label = "paperback";
    BookFormat format = BookFormat.fromLabel(label).orElse(BookFormat.EBOOK);  // PAPERBACK
    boolean known = BookFormat.fromLabel("Audio").isPresent();                 // false
    System.out.println(format + " " + known);
  }

  static void multipleValues() {
    System.out.println("--- multiple values ---");
    Optional<BookFormat> byCode = BookFormat.fromCode("pb");         // Optional[PAPERBACK]
    Optional<BookFormat> byLabel = BookFormat.fromLabel("Paperback"); // Optional[PAPERBACK]
    String code = BookFormat.EBOOK.getCode();                        // "EB"
    System.out.println(byCode + " " + byLabel + " " + code);

    for (BookFormat format : BookFormat.values()) {
      System.out.println(format.getCode() + " -> " + format.getLabel());
    }
  }

  static void switchExamples() {
    System.out.println("--- switch ---");
    for (BookFormat format : BookFormat.values()) {
      System.out.println(format + " ships as " + shipping(format));
    }
    System.out.println(shippingFor("e-book"));
    System.out.println(shippingFor("audio"));
  }

  // Switch expression on the enum constants, no default needed
  static String shipping(BookFormat format) {
    String shipping = switch (format) {
      case HARDCOVER, PAPERBACK -> "a parcel";
      case EBOOK -> "a download link";
    };
    return shipping;
  }

  // Convert the String first, then switch on the enum
  static String shippingFor(String label) {
    Optional<BookFormat> format = BookFormat.fromLabel(label);
    if (format.isEmpty()) {
      return "Unknown format: " + label;
    }
    return format.get() + " ships as " + shipping(format.get());
  }
}
