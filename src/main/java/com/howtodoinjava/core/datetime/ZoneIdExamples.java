package com.howtodoinjava.core.datetime;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRules;
import java.time.zone.ZoneRulesException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * ZoneId and ZoneOffset examples: region IDs, offset IDs, system default,
 * available zone IDs, ZoneId to ZoneOffset conversion, errors and SHORT_IDS.
 */
public class ZoneIdExamples {

  public static void main(String[] args) {
    quickReference();
    kindsOfZoneIds();
    zoneOffsetExamples();
    systemDefaultZone();
    availableZoneIds();
    zoneIdToZoneOffset();
    zonedDateTimeVsOffsetDateTime();
    unknownZoneIds();
    shortIds();
    commonZoneIdsTable();
  }

  static void quickReference() {
    System.out.println("--- quick reference ---");
    ZoneId kolkata = ZoneId.of("Asia/Kolkata");                      // Asia/Kolkata
    ZoneId newYork = ZoneId.of("America/New_York");                  // America/New_York
    ZoneOffset plusFiveThirty = ZoneOffset.of("+05:30");             // +05:30
    ZoneId utc = ZoneId.of("UTC");                                   // UTC
    ZoneId systemZone = ZoneId.systemDefault();                      // Asia/Calcutta on this machine
    Set<String> allIds = ZoneId.getAvailableZoneIds();               // 604 region IDs
    ZoneOffset nyOffset = newYork.getRules().getOffset(Instant.now());  // -04:00 or -05:00
    ZonedDateTime nowInKolkata = ZonedDateTime.now(kolkata);         // 2026-10-06T...+05:30[Asia/Kolkata]

    System.out.println(kolkata + " " + newYork + " " + plusFiveThirty + " " + utc + " " + systemZone);
    System.out.println(allIds.size() + " " + nyOffset + " " + nowInKolkata);
  }

  static void kindsOfZoneIds() {
    System.out.println("--- kinds of zone ids ---");
    ZoneId region = ZoneId.of("Europe/Paris");                       // Europe/Paris
    ZoneId offset = ZoneId.of("+02:00");                             // +02:00 (a ZoneOffset)
    ZoneId zulu = ZoneId.of("Z");                                    // Z (ZoneOffset.UTC)
    ZoneId prefixed = ZoneId.of("UTC+02:00");                        // UTC+02:00
    ZoneId gmt = ZoneId.of("GMT");                                   // GMT
    ZoneId shortForm = ZoneId.of("GMT+2");                           // GMT+02:00 (normalized)

    System.out.println(region + " " + region.getClass().getSimpleName());       // Europe/Paris ZoneRegion
    System.out.println(offset + " " + offset.getClass().getSimpleName());       // +02:00 ZoneOffset
    System.out.println(zulu + " " + (zulu == ZoneOffset.UTC));                  // Z true
    System.out.println(prefixed + " " + prefixed.getClass().getSimpleName());   // UTC+02:00 ZoneRegion
    System.out.println(gmt + " " + gmt.getClass().getSimpleName());             // GMT ZoneRegion
    System.out.println(shortForm);                                              // GMT+02:00

    ZoneId normalized = prefixed.normalized();                       // +02:00 (a ZoneOffset)
    ZoneId gmtNormalized = gmt.normalized();                         // Z
    ZoneId parisNormalized = region.normalized();                    // Europe/Paris (unchanged)
    System.out.println(normalized + " " + gmtNormalized + " " + parisNormalized);

    boolean fixed = region.getRules().isFixedOffset();               // false
    boolean fixedOffset = offset.getRules().isFixedOffset();         // true
    System.out.println(fixed + " " + fixedOffset);
  }

  static void zoneOffsetExamples() {
    System.out.println("--- zone offset ---");
    ZoneOffset fromText = ZoneOffset.of("+05:30");                   // +05:30
    ZoneOffset fromHours = ZoneOffset.ofHours(-8);                   // -08:00
    ZoneOffset fromHoursMinutes = ZoneOffset.ofHoursMinutes(5, 45);  // +05:45
    ZoneOffset fromSeconds = ZoneOffset.ofTotalSeconds(3600);        // +01:00
    ZoneOffset utc = ZoneOffset.UTC;                                 // Z
    int seconds = fromText.getTotalSeconds();                        // 19800
    String id = fromText.getId();                                    // "+05:30"
    ZoneOffset min = ZoneOffset.MIN;                                 // -18:00
    ZoneOffset max = ZoneOffset.MAX;                                 // +18:00

    System.out.println(fromText + " " + fromHours + " " + fromHoursMinutes + " " + fromSeconds + " " + utc);
    System.out.println(seconds + " " + id + " " + min + " " + max);

    try {
      ZoneOffset tooBig = ZoneOffset.of("+19:00");                   // DateTimeException
      System.out.println(tooBig);
    } catch (DateTimeException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
  }

  static void systemDefaultZone() {
    System.out.println("--- system default ---");
    ZoneId systemZone = ZoneId.systemDefault();                      // Asia/Calcutta on this machine
    ZoneId fromTimeZone = TimeZone.getDefault().toZoneId();          // same value
    String property = System.getProperty("user.timezone");           // "Asia/Calcutta"
    System.out.println(systemZone + " " + fromTimeZone + " " + property);

    LocalDateTime local = LocalDateTime.of(2026, 10, 6, 9, 30);
    ZonedDateTime inSystemZone = local.atZone(ZoneId.systemDefault());   // 2026-10-06T09:30+05:30[Asia/Calcutta]
    ZonedDateTime inKolkata = local.atZone(ZoneId.of("Asia/Kolkata"));   // 2026-10-06T09:30+05:30[Asia/Kolkata]
    System.out.println(inSystemZone + " " + inKolkata);
  }

  static void availableZoneIds() {
    System.out.println("--- available zone ids ---");
    Set<String> zoneIds = ZoneId.getAvailableZoneIds();              // unsorted HashSet copy
    int count = zoneIds.size();                                      // 604
    boolean hasKolkata = zoneIds.contains("Asia/Kolkata");           // true
    boolean hasOffset = zoneIds.contains("+05:30");                  // false, offsets are not listed
    System.out.println(count + " " + hasKolkata + " " + hasOffset);

    // One line that prints every zone ID in alphabetical order
    new TreeSet<>(ZoneId.getAvailableZoneIds()).forEach(System.out::println);

    // Count IDs per region prefix (the part before the first slash)
    Map<String, Long> perRegion = zoneIds.stream()
        .collect(Collectors.groupingBy(
            id -> id.contains("/") ? id.substring(0, id.indexOf('/')) : "(no slash)",
            TreeMap::new, Collectors.counting()));
    System.out.println(perRegion);

    // Only the IDs for one region
    List<String> europe = zoneIds.stream()
        .filter(id -> id.startsWith("Europe/"))
        .sorted()
        .toList();
    System.out.println(europe.size() + " " + europe.subList(0, 5));  // 64 [Europe/Amsterdam, ...]
  }

  static void zoneIdToZoneOffset() {
    System.out.println("--- zoneid to zoneoffset ---");
    ZoneId newYork = ZoneId.of("America/New_York");
    ZoneRules rules = newYork.getRules();

    Instant winter = Instant.parse("2026-01-15T12:00:00Z");
    Instant summer = Instant.parse("2026-07-15T12:00:00Z");
    ZoneOffset winterOffset = rules.getOffset(winter);               // -05:00
    ZoneOffset summerOffset = rules.getOffset(summer);               // -04:00
    boolean dstInJuly = rules.isDaylightSavings(summer);             // true
    ZoneOffset standard = rules.getStandardOffset(summer);           // -05:00
    System.out.println(winterOffset + " " + summerOffset + " " + dstInJuly + " " + standard);

    // The same answer through ZonedDateTime
    ZoneOffset nowOffset = ZonedDateTime.now(newYork).getOffset();   // -04:00 in October
    ZoneOffset viaInstant = summer.atZone(newYork).getOffset();      // -04:00
    System.out.println(nowOffset + " " + viaInstant);

    // From a local date-time instead of an instant
    LocalDateTime localWinter = LocalDateTime.of(2026, 1, 15, 9, 0);
    ZoneOffset fromLocal = rules.getOffset(localWinter);             // -05:00
    System.out.println(fromLocal);

    // Gap (clocks jump forward) and overlap (clocks fall back) in 2026
    LocalDateTime inGap = LocalDateTime.of(2026, 3, 8, 2, 30);       // does not exist in New York
    LocalDateTime inOverlap = LocalDateTime.of(2026, 11, 1, 1, 30);  // happens twice in New York
    List<ZoneOffset> gapOffsets = rules.getValidOffsets(inGap);          // []
    List<ZoneOffset> overlapOffsets = rules.getValidOffsets(inOverlap);  // [-04:00, -05:00]
    ZonedDateTime gapResolved = inGap.atZone(newYork);               // 2026-03-08T03:30-04:00[America/New_York]
    System.out.println(gapOffsets + " " + overlapOffsets + " " + gapResolved);

    // A fixed-offset zone has only one offset, so no instant is needed
    ZoneId kolkata = ZoneId.of("Asia/Kolkata");
    boolean kolkataFixed = kolkata.getRules().isFixedOffset();       // false, rules can change
    ZoneOffset kolkataOffset = kolkata.getRules().getOffset(Instant.now());   // +05:30
    System.out.println(kolkataFixed + " " + kolkataOffset);
  }

  static void zonedDateTimeVsOffsetDateTime() {
    System.out.println("--- ZonedDateTime vs OffsetDateTime ---");
    LocalDateTime local = LocalDateTime.of(2026, 1, 15, 9, 0);
    ZonedDateTime zoned = local.atZone(ZoneId.of("America/New_York"));   // 2026-01-15T09:00-05:00[America/New_York]
    OffsetDateTime offset = local.atOffset(ZoneOffset.ofHours(-5));      // 2026-01-15T09:00-05:00

    ZonedDateTime zonedJuly = zoned.plusMonths(6);                   // 2026-07-15T09:00-04:00[America/New_York]
    OffsetDateTime offsetJuly = offset.plusMonths(6);                // 2026-07-15T09:00-05:00
    System.out.println(zoned + " " + offset);
    System.out.println(zonedJuly + " " + offsetJuly);

    OffsetDateTime fromZoned = zoned.toOffsetDateTime();             // 2026-01-15T09:00-05:00
    ZonedDateTime fromOffset = offset.atZoneSameInstant(ZoneId.of("Asia/Kolkata"));   // 2026-01-15T19:30+05:30[Asia/Kolkata]
    System.out.println(fromZoned + " " + fromOffset);
  }

  static void unknownZoneIds() {
    System.out.println("--- unknown zone ids ---");
    try {
      ZoneId wrong = ZoneId.of("Asia/Calcuta");                      // ZoneRulesException
      System.out.println(wrong);
    } catch (ZoneRulesException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
    try {
      ZoneId lowerCase = ZoneId.of("asia/kolkata");                  // ZoneRulesException, IDs are case-sensitive
      System.out.println(lowerCase);
    } catch (ZoneRulesException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
    try {
      ZoneId badFormat = ZoneId.of("5:30");                          // DateTimeException
      System.out.println(badFormat);
    } catch (DateTimeException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
    try {
      ZoneId shortId = ZoneId.of("IST");                             // ZoneRulesException
      System.out.println(shortId);
    } catch (ZoneRulesException e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }

    // Safe lookups
    Optional<ZoneId> found = parseZone("Asia/Calcuta");              // Optional.empty
    Optional<ZoneId> ok = parseZone("Asia/Kolkata");                 // Optional[Asia/Kolkata]
    ZoneId withFallback = parseZone("Mars/Olympus").orElse(ZoneOffset.UTC);   // Z
    System.out.println(found + " " + ok + " " + withFallback);
  }

  static Optional<ZoneId> parseZone(String id) {
    try {
      return Optional.of(ZoneId.of(id));
    } catch (DateTimeException e) {                                  // ZoneRulesException extends DateTimeException
      return Optional.empty();
    }
  }

  static void shortIds() {
    System.out.println("--- SHORT_IDS ---");
    Map<String, String> shortIds = ZoneId.SHORT_IDS;                 // 28 entries, unmodifiable
    String ist = shortIds.get("IST");                                // "Asia/Kolkata"
    String est = shortIds.get("EST");                                // "America/Panama"
    String pst = shortIds.get("PST");                                // "America/Los_Angeles"
    System.out.println(shortIds.size() + " " + ist + " " + est + " " + pst);

    ZoneId fromShort = ZoneId.of("IST", ZoneId.SHORT_IDS);           // Asia/Kolkata
    ZoneId stillRegion = ZoneId.of("Europe/Paris", ZoneId.SHORT_IDS);   // Europe/Paris
    System.out.println(fromShort + " " + stillRegion);

    try {
      shortIds.put("NZST", "Pacific/Auckland");                      // UnsupportedOperationException
    } catch (UnsupportedOperationException e) {
      System.out.println("UnsupportedOperationException: SHORT_IDS is unmodifiable");
    }

    Map<String, String> ownAliases = new HashMap<>(ZoneId.SHORT_IDS);
    ownAliases.put("NZST", "Pacific/Auckland");
    ZoneId auckland = ZoneId.of("NZST", ownAliases);                 // Pacific/Auckland
    System.out.println(auckland);

    new TreeMap<>(shortIds).forEach((k, v) -> System.out.println(k + " -> " + v));
  }

  static void commonZoneIdsTable() {
    System.out.println("--- common zone ids, offsets on 2026-01-15 and 2026-07-15 ---");
    Instant january = Instant.parse("2026-01-15T12:00:00Z");
    Instant july = Instant.parse("2026-07-15T12:00:00Z");
    List<String> common = List.of(
        "UTC", "Etc/UTC", "GMT",
        "America/New_York", "America/Chicago", "America/Denver", "America/Phoenix",
        "America/Los_Angeles", "America/Anchorage", "America/Toronto", "America/Vancouver",
        "America/Mexico_City", "America/Bogota", "America/Lima", "America/Santiago",
        "America/Sao_Paulo", "America/Argentina/Buenos_Aires", "America/Halifax", "America/St_Johns",
        "Europe/London", "Europe/Dublin", "Europe/Lisbon", "Europe/Paris", "Europe/Berlin",
        "Europe/Madrid", "Europe/Rome", "Europe/Amsterdam", "Europe/Zurich", "Europe/Stockholm",
        "Europe/Warsaw", "Europe/Athens", "Europe/Helsinki", "Europe/Kyiv", "Europe/Istanbul",
        "Europe/Moscow",
        "Asia/Riyadh", "Asia/Jerusalem", "Asia/Dubai", "Asia/Tehran", "Asia/Karachi", "Asia/Kolkata",
        "Asia/Kathmandu", "Asia/Dhaka", "Asia/Bangkok", "Asia/Jakarta", "Asia/Ho_Chi_Minh",
        "Asia/Singapore", "Asia/Kuala_Lumpur", "Asia/Manila", "Asia/Hong_Kong", "Asia/Shanghai",
        "Asia/Taipei", "Asia/Seoul", "Asia/Tokyo",
        "Africa/Algiers", "Africa/Lagos", "Africa/Cairo", "Africa/Johannesburg",
        "Africa/Nairobi",
        "Australia/Perth", "Australia/Darwin", "Australia/Adelaide", "Australia/Brisbane",
        "Australia/Sydney", "Australia/Melbourne", "Pacific/Auckland", "Pacific/Honolulu");
    for (String id : common) {
      ZoneRules rules = ZoneId.of(id).getRules();
      String dst = rules.isDaylightSavings(january) || rules.isDaylightSavings(july) ? "yes" : "no";
      System.out.printf("| %s | %s | %s | %s |%n", id, rules.getOffset(january), rules.getOffset(july), dst);
    }
  }
}
