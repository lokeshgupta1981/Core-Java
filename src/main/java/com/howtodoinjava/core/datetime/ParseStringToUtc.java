package com.howtodoinjava.core.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Parses date-time strings (with 'Z', with an offset, with a zone name, or without any zone)
 * into UTC values using java.time, and shows the legacy SimpleDateFormat way for old code.
 *
 * <p>Source code for the article "Parse a String to UTC Date Time in Java" on howtodoinjava.com.
 */
public class ParseStringToUtc {

  public static void main(String[] args) throws ParseException {
    isoStrings();
    stringsWithoutZone();
    customPatterns();
    convertToOtherZone();
    oldArticleExamples();
    legacySimpleDateFormat();
    commonErrors();
  }

  // 1. ISO-8601 strings: 'Z', an offset, or an offset plus a region id
  static void isoStrings() {
    Instant instant = Instant.parse("2026-10-04T10:15:30Z");
    System.out.println("Instant.parse Z          : " + instant);

    Instant fromOffset = Instant.parse("2026-10-04T15:45:30+05:30");
    System.out.println("Instant.parse +05:30     : " + fromOffset);

    OffsetDateTime odt = OffsetDateTime.parse("2026-10-04T15:45:30+05:30");
    OffsetDateTime odtUtc = odt.withOffsetSameInstant(ZoneOffset.UTC);
    System.out.println("OffsetDateTime parsed    : " + odt);
    System.out.println("OffsetDateTime in UTC    : " + odtUtc);

    ZonedDateTime zdt = ZonedDateTime.parse("2026-10-04T15:45:30+05:30[Asia/Kolkata]");
    ZonedDateTime zdtUtc = zdt.withZoneSameInstant(ZoneOffset.UTC);
    ZonedDateTime zdtUtcId = zdt.withZoneSameInstant(ZoneId.of("UTC"));
    System.out.println("ZonedDateTime parsed     : " + zdt);
    System.out.println("ZonedDateTime ZoneOffset : " + zdtUtc);
    System.out.println("ZonedDateTime ZoneId UTC : " + zdtUtcId);
    System.out.println("Same instant?            : " + zdtUtc.toInstant().equals(instant));
    System.out.println("Z equals +05:30 instant  : " + instant.equals(fromOffset));
  }

  // 2. Strings without zone information: decide the zone ourselves
  static void stringsWithoutZone() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    LocalDateTime ldt = LocalDateTime.parse("2026-10-04 10:15:30", formatter);
    System.out.println("LocalDateTime            : " + ldt);
    System.out.println("atOffset(UTC)            : " + ldt.atOffset(ZoneOffset.UTC));
    System.out.println("atZone(UTC)              : " + ldt.atZone(ZoneOffset.UTC));
    System.out.println("toInstant(UTC)           : " + ldt.toInstant(ZoneOffset.UTC));

    ZonedDateTime utc = ZonedDateTime.parse("2026-10-04 10:15:30",
        formatter.withZone(ZoneOffset.UTC));
    System.out.println("withZone(UTC) formatter  : " + utc);

    // the string is local time in New York; convert it to UTC
    ZonedDateTime ny = ldt.atZone(ZoneId.of("America/New_York"));
    System.out.println("New York local           : " + ny);
    System.out.println("New York local -> UTC    : " + ny.withZoneSameInstant(ZoneOffset.UTC));
  }

  // 3. Custom patterns with zone names, GMT offsets and RFC 1123
  static void customPatterns() {
    DateTimeFormatter withZoneName =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH);
    System.out.println("pattern z, UTC           : "
        + ZonedDateTime.parse("2026-10-04 10:15:30 UTC", withZoneName));
    System.out.println("pattern z, GMT           : "
        + ZonedDateTime.parse("2026-10-04 10:15:30 GMT", withZoneName));
    System.out.println("pattern z, PST           : "
        + ZonedDateTime.parse("2026-10-04 03:15:30 PST", withZoneName));

    DateTimeFormatter withOffset =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX");
    System.out.println("pattern XXX, Z           : "
        + OffsetDateTime.parse("2026-10-04 10:15:30 Z", withOffset));
    System.out.println("pattern XXX, +05:30      : "
        + OffsetDateTime.parse("2026-10-04 15:45:30 +05:30", withOffset));

    DateTimeFormatter withGmtOffset =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss O", Locale.ENGLISH);
    System.out.println("pattern O, GMT+5:30      : "
        + OffsetDateTime.parse("2026-10-04 15:45:30 GMT+5:30", withGmtOffset)
            .withOffsetSameInstant(ZoneOffset.UTC));

    DateTimeFormatter noColon = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
    System.out.println("pattern Z, +0000         : "
        + OffsetDateTime.parse("2026-10-04 10:15:30 +0000", noColon));
    DateTimeFormatter neverZ = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss xxx");
    System.out.println("pattern xxx, +00:00      : "
        + OffsetDateTime.parse("2026-10-04 10:15:30 +00:00", neverZ));
    System.out.println("pattern O, GMT           : " + OffsetDateTime.parse("2026-10-04 10:15:30 GMT",
        withGmtOffset));
    DateTimeFormatter withRegion = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss VV");
    System.out.println("pattern VV               : "
        + ZonedDateTime.parse("2026-10-04 15:45:30 Asia/Kolkata", withRegion));

    ZonedDateTime gmt = ZonedDateTime.parse("2026-10-04 10:15:30 GMT", withZoneName);
    ZonedDateTime utc = ZonedDateTime.parse("2026-10-04 10:15:30 UTC", withZoneName);
    System.out.println("GMT equals UTC           : " + gmt.equals(utc));
    System.out.println("GMT isEqual UTC          : " + gmt.isEqual(utc));
    System.out.println("GMT, UTC same instant    : " + gmt.toInstant().equals(utc.toInstant()));

    ZonedDateTime rfc = ZonedDateTime.parse("Tue, 3 Jun 2008 11:05:30 GMT",
        DateTimeFormatter.RFC_1123_DATE_TIME);
    System.out.println("RFC 1123                 : " + rfc);
    System.out.println("RFC 1123 today           : " + ZonedDateTime.parse(
        "Sun, 4 Oct 2026 10:15:30 GMT", DateTimeFormatter.RFC_1123_DATE_TIME));
  }

  // 4. From UTC to another zone, and back to a string
  static void convertToOtherZone() {
    Instant instant = Instant.parse("2026-10-04T10:15:30Z");
    System.out.println("Kolkata                  : " + instant.atZone(ZoneId.of("Asia/Kolkata")));
    System.out.println("New York                 : " + instant.atZone(ZoneId.of("America/New_York")));
    System.out.println("London                   : " + instant.atZone(ZoneId.of("Europe/London")));

    DateTimeFormatter out = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH)
        .withZone(ZoneId.of("America/New_York"));
    System.out.println("Formatted New York       : " + out.format(instant));
    System.out.println("ISO_INSTANT              : " + DateTimeFormatter.ISO_INSTANT.format(
        OffsetDateTime.parse("2026-10-04T15:45:30+05:30")));
    System.out.println("Date.from(instant)       : " + Date.from(instant).getTime());
  }

  // 5. The two examples of the original article, updated
  static void oldArticleExamples() {
    DateTimeFormatter f1 = DateTimeFormatter.ofPattern("dd/MM/uuuu'T'HH:mm:ss:SSSXXXXX");
    OffsetDateTime odt = OffsetDateTime.parse("03/08/2019T16:20:17:717+05:30", f1);
    OffsetDateTime odtUtc = odt.withOffsetSameInstant(ZoneOffset.UTC);
    System.out.println("Old 1 parsed             : " + odt);
    System.out.println("Old 1 UTC                : " + odtUtc);
    System.out.println("Old 1 UTC formatted      : " + odtUtc.format(f1));

    DateTimeFormatter f2 = DateTimeFormatter.ofPattern("MM/dd/yyyy'T'HH:mm:ss:SSS z");
    ZonedDateTime zdt = ZonedDateTime.parse("08/03/2019T16:20:17:717 UTC+05:30", f2);
    ZonedDateTime zdtUtc = zdt.withZoneSameInstant(ZoneOffset.UTC);
    System.out.println("Old 2 parsed             : " + zdt);
    System.out.println("Old 2 UTC                : " + zdtUtc);
    System.out.println("Old 2 UTC formatted      : " + zdtUtc.format(f2));
  }

  // 6. Legacy code: SimpleDateFormat with setTimeZone
  static void legacySimpleDateFormat() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    Date date = sdf.parse("2026-10-04 10:15:30");
    System.out.println("SDF UTC getTime          : " + date.getTime());
    System.out.println("SDF UTC toInstant        : " + date.toInstant());

    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
    System.out.println("SDF Kolkata toInstant    : " + sdf.parse("2026-10-04 10:15:30").toInstant());

    SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    System.out.println("SDF XXX toInstant        : " + iso.parse("2026-10-04T15:45:30+05:30").toInstant());

    SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH);
    sdfOut.setTimeZone(TimeZone.getTimeZone("UTC"));
    System.out.println("SDF format in UTC        : " + sdfOut.format(date));
  }

  // 7. Common DateTimeParseException causes
  static void commonErrors() {
    tryParse("Instant without Z", () -> Instant.parse("2026-10-04T10:15:30"));
    tryParse("Instant with space", () -> Instant.parse("2026-10-04 10:15:30Z"));
    tryParse("LocalDateTime with Z", () -> LocalDateTime.parse("2026-10-04T10:15:30Z"));
    tryParse("ZonedDateTime no zone", () -> ZonedDateTime.parse("2026-10-04T10:15:30"));
    tryParse("ZonedDateTime pattern without zone", () -> ZonedDateTime.parse("2026-10-04 10:15:30",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    tryParse("Pattern Z with letter Z", () -> OffsetDateTime.parse("2026-10-04 10:15:30 Z",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")));
    tryParse("Quoted 'Z' then ZonedDateTime", () -> ZonedDateTime.parse("2026-10-04T10:15:30Z",
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")));
    tryParse("Quoted 'Z' then LocalDateTime", () -> LocalDateTime.parse("2026-10-04T10:15:30Z",
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")));
    tryParse("hh without AM/PM", () -> LocalDateTime.parse("2026-10-04 10:15:30",
        DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
    tryParse("Day name in German locale", () -> ZonedDateTime.parse("Sun, 4 Oct 2026 10:15:30 GMT",
        DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss z", Locale.GERMAN)));
    tryParse("Wrong day name", () -> ZonedDateTime.parse("Mon, 4 Oct 2026 10:15:30 GMT",
        DateTimeFormatter.RFC_1123_DATE_TIME));
  }

  interface Parse {
    Object run();
  }

  static void tryParse(String label, Parse parse) {
    try {
      System.out.println(label + " -> OK " + parse.run());
    } catch (DateTimeParseException e) {
      System.out.println(label + " -> " + e.getMessage());
    }
  }
}
