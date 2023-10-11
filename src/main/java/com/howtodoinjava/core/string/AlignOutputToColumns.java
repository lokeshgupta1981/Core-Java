package com.howtodoinjava.core.string;

import com.google.common.collect.Streams;
import com.howtodoinjava.core.string.TextTable.Alignment;
import com.howtodoinjava.core.string.TextTable.Column;
import org.apache.commons.io.StandardLineSeparator;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AlignOutputToColumns {

  public static void main(String[] args) {

    //1
    TextTable textTable = new TextTable(
        List.of(
            new Column("ID", Alignment.RIGHT),
            new Column("NAME", Alignment.LEFT),
            new Column("AGE", Alignment.RIGHT)
        )
    );

    textTable.add(1, "Lokesh", 40);
    textTable.add(2, "Alexandru", 27);
    textTable.add(3, "Mick", 38);

    System.out.println(textTable.build(System.lineSeparator()));

    //2
    // Sample data for a table
    int[] ids = { 1, 2, 3};
    String[] names = { "Lokesh", "Alexandru", "Mick" };
    int[] scores = { 40, 27, 38};

    // Define column widths
    int idColumnWidth = 10;
    int nameColumnWidth = 20;
    int scoreColumnWidth = 10;

    // Print table header
    System.out.println(String.format("%-" + idColumnWidth + "s%-" + nameColumnWidth
        + "s%-" + scoreColumnWidth + "s", "ID", "NAME", "AGE"));
    System.out.println("-".repeat(idColumnWidth + nameColumnWidth + scoreColumnWidth));

    // Print data in aligned columns
    for (int i = 0; i < names.length; i++) {
      String formattedId = String.format("%-" + idColumnWidth + "d", ids[i]);
      String formattedName = String.format("%-" + nameColumnWidth + "s", names[i]);
      String formattedScore = String.format("%-" + scoreColumnWidth + "d", scores[i]);
      System.out.println(formattedId + formattedName + formattedScore);
    }
  }
}

/**
 * A utility class for creating and formatting text-based tables.
 */
class TextTable {

  /**
   * Create a new column with the specified header.
   *
   * @param header The header of the column.
   * @return A new column.
   */
  public static Column column(String header) {
    return new Column(header);
  }

  /**
   * Create a new column with the specified header and alignment.
   *
   * @param header     The header of the column.
   * @param alignment  The alignment of the column.
   * @return A new column.
   */
  public static Column column(String header, Alignment alignment) {
    return new Column(header, alignment);
  }

  private final List<Column> columns;
  private final List<Row> rows = new ArrayList<>();

  /**
   * Creates a TextTable with the specified columns.
   *
   * @param columns The columns to include in the table.
   */
  public TextTable(List<Column> columns) {
    this.columns = columns;
  }

  /**
   * Build and return the table as a string with the given line ending.
   *
   * @param lineEnding The line ending to use.
   * @return The formatted table as a string.
   */
  public String build(String lineEnding) {
    StringBuilder destination = new StringBuilder();
    append(destination, lineEnding);
    return destination.toString();
  }

  /**
   * Append the table to the specified StringBuilder with the given line ending.
   *
   * @param destination The StringBuilder to append the table to.
   * @param lineEnding  The line ending to use.
   */
  public void append(StringBuilder destination, String lineEnding) {
    List<String> headers = columns.stream().map(c -> c.formatHeader(" "))
        .collect(Collectors.toList());
    printRow(destination, headers);
    destination.append(lineEnding);
    printSeparators(destination);
    for (Row row : rows) {
      destination.append(lineEnding);
      printRow(destination, row.format(columns, " "));
    }
  }

  private void printSeparators(StringBuilder destination) {
    destination.append('|');
    for (Column column : columns) {
      destination.append(column.alignment != Alignment.RIGHT ? ':' : ' ');
      destination.append(column.getSeparator('-'));
      destination.append(column.alignment != Alignment.LEFT ? ':' : ' ');
      destination.append('|');
    }
  }

  private void printRow(StringBuilder destination, List<String> values) {
    destination.append('|');
    for (String value : values) {
      destination.append(' ');
      destination.append(value);
      destination.append(' ');
      destination.append('|');
    }
  }

  /**
   * Add a row to the table with the specified values. The number of values must match the number of columns.
   *
   * @param values The values to add to the row.
   * @throws IllegalArgumentException if the number of values doesn't match the number of columns.
   */
  public void add(@Nonnull Object... values) {
    if (values.length != columns.size()) {
      throw new IllegalArgumentException(
          "Received the wrong amount of values for the table row, expected " + columns.size()
              + ", received " + values.length + ".");
    }
    Row row = new Row();
    for (int i = 0; i < values.length; i++) {
      String value = Objects.toString(values[i]);
      row.values.add(value);
      columns.get(i).fit(value);
    }
    rows.add(row);
  }

  /**
   * Clear the table by resetting column widths and removing all rows.
   */
  public void clear() {
    for (Column column : columns) {
      column.resetWidth();
    }
    rows.clear();
  }

  /**
   * Get an unmodifiable list of columns in the table.
   *
   * @return An unmodifiable list of columns.
   */
  public List<Column> getColumns() {
    return Collections.unmodifiableList(columns);
  }

  /**
   * A class representing a table column.
   */
  public static class Column {

    private String header;
    private int width;
    private Alignment alignment;

    /**
     * Create a new column with the specified header and a default left alignment.
     *
     * @param header The header of the column.
     */
    public Column(String header) {
      this(header, Alignment.LEFT);
    }

    /**
     * Create a new column with the specified header and alignment.
     *
     * @param header     The header of the column.
     * @param alignment  The alignment of the column.
     */
    public Column(String header, Alignment alignment) {
      this.header = header;
      this.width = header.length();
      this.alignment = alignment;
    }

    /**
     * Format the header with the specified padding.
     *
     * @param padding The padding to apply.
     * @return The formatted header.
     */
    public String formatHeader(String padding) {
      return format(header, padding);
    }

    /**
     * Format a value with the specified padding.
     *
     * @param value   The value to format.
     * @param padding The padding to apply.
     * @return The formatted value.
     */
    public String format(String value, String padding) {
      switch (alignment) {
        case LEFT:
          return StringUtils.rightPad(value, width, padding);
        case RIGHT:
          return StringUtils.leftPad(value, width, padding);
        default:
          int length = value.length();
          int left = (width - length) / 2;
          int leftWidth = left + length;
          return StringUtils.rightPad(StringUtils.leftPad(value, leftWidth, padding), width, padding);
      }
    }

    /**
     * Get a separator of a specific character and width.
     *
     * @param character The character to use for the separator.
     * @return The separator string.
     */
    public String getSeparator(char character) {
      return StringUtils.leftPad("", width, character);
    }

    /**
     * Adjust the column width based on the specified value.
     *
     * @param value The value to adjust the column width.
     */
    public void fit(String value) {
      if (value.length() > width) {
        width = value.length();
      }
    }

    /**
     * Reset the column width to its initial header length.
     */
    public void resetWidth() {
      this.width = header.length();
    }
  }

  /**
   * A class representing a table row.
   */
  public static class Row {
    private final ArrayList<String> values = new ArrayList<>();

    /**
     * Format the row values with the specified padding.
     *
     * @param columns The list of columns to align the values with.
     * @param padding The padding to apply.
     * @return A list of formatted values.
     * @throws IllegalArgumentException if the number of columns doesn't match the number of values.
     */
    public List<String> format(List<Column> columns, String padding) {
      if (columns.size() != values.size()) {
        throw new IllegalArgumentException(
            "Received the wrong amount of columns for the table row, expected " + columns.size()
                + ", received " + values.size() + ".");
      }
      return Streams.zip(values.stream(), columns.stream(), (v, c) -> c.format(v, padding))
          .collect(Collectors.toList());
    }
  }

  /**
   * An enum representing column alignments: LEFT, CENTER, RIGHT.
   */
  public enum Alignment {
    LEFT, CENTER, RIGHT
  }
}
