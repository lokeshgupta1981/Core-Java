package com.howtodoinjava.core.datetime;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.time.format.*;
import java.time.temporal.*;
import java.time.chrono.*;
import java.time.zone.*;
import java.text.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

/**
 * Examples for the tutorial "Get Current Timestamp in Java: Instant, Epoch Millis, JDBC".
 * https://howtodoinjava.com/java/date-time/get-current-timestamp/
 */
public class CurrentTimeStamp {
    static void saveLogin(Connection con, String user, Instant at) throws SQLException {
        String sql = "INSERT INTO logins (username, login_at) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setObject(2, at.atOffset(ZoneOffset.UTC));    // TIMESTAMP WITH TIME ZONE column
            ps.executeUpdate();
        }
    }
    static record AuditEntry(String action, String user, Instant at) {}
    static class AuditLog {
        private final InstantSource source;
        private final List<AuditEntry> entries = new ArrayList<>();

        AuditLog(InstantSource source) {
            this.source = source;
        }

        AuditEntry record(String action, String user) {
            AuditEntry entry = new AuditEntry(action, user, source.instant());
            entries.add(entry);
            return entry;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            Instant now = Instant.now();                                  // current moment in UTC
            show("now", now);
            long nowMillis = System.currentTimeMillis();                  // current epoch milliseconds
            show("nowMillis", nowMillis);
            Instant fixed = Instant.parse("2026-10-10T08:15:30.250Z");
            long millis = fixed.toEpochMilli();                           // 1791620130250
            show("millis", millis);
            long seconds = fixed.getEpochSecond();                        // 1791620130
            show("seconds", seconds);
            Instant fromMillis = Instant.ofEpochMilli(1791620130250L);    // 2026-10-10T08:15:30.250Z
            show("fromMillis", fromMillis);
        }
        {
            Instant precise = Instant.parse("2026-10-10T08:15:30.250123456Z");
            Instant toMillis = precise.truncatedTo(ChronoUnit.MILLIS);      // 2026-10-10T08:15:30.250Z
            show("toMillis", toMillis);
            Instant toSeconds = precise.truncatedTo(ChronoUnit.SECONDS);    // 2026-10-10T08:15:30Z
            show("toSeconds", toSeconds);
            int nanos = precise.getNano();                                  // 250123456
            show("nanos", nanos);
        }
        {
            Instant moment = Instant.parse("2026-10-10T08:15:30Z");
            ZonedDateTime tokyo = moment.atZone(ZoneId.of("Asia/Tokyo"));              // 2026-10-10T17:15:30+09:00[Asia/Tokyo]
            show("tokyo", tokyo);
            ZonedDateTime newYork = moment.atZone(ZoneId.of("America/New_York"));      // 2026-10-10T04:15:30-04:00[America/New_York]
            show("newYork", newYork);
            boolean sameMoment = tokyo.toInstant().equals(newYork.toInstant());       // true
            show("sameMoment", sameMoment);
        }
        {
            Instant created = Instant.parse("2026-10-10T08:15:30.250Z");
            long createdAt = created.toEpochMilli();                         // 1791620130250
            show("createdAt", createdAt);
            Instant parsedBack = Instant.ofEpochMilli(createdAt);            // 2026-10-10T08:15:30.250Z
            show("parsedBack", parsedBack);
            long unixSeconds = created.getEpochSecond();                     // 1791620130
            show("unixSeconds", unixSeconds);
            Instant fromSeconds = Instant.ofEpochSecond(1791620130L);        // 2026-10-10T08:15:30Z
            show("fromSeconds", fromSeconds);
            long secondsFromMillis = TimeUnit.MILLISECONDS.toSeconds(createdAt);   // 1791620130
            show("secondsFromMillis", secondsFromMillis);
        }
        {
            Instant moment = Instant.parse("2026-10-10T08:15:30.250Z");
            DateTimeFormatter logFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneOffset.UTC);
            String logText = logFormat.format(moment);                                            // "2026-10-10 08:15:30.250"
            show("logText", logText);
            String fileName = "backup-" + DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").withZone(ZoneOffset.UTC).format(moment) + ".zip";   // "backup-20261010-081530.zip"
            show("fileName", fileName);
            String iso = DateTimeFormatter.ISO_INSTANT.format(moment);                            // "2026-10-10T08:15:30.250Z"
            show("iso", iso);
            try { String noZone = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(moment); show("noZone", noZone); } catch (Throwable _t) { System.out.println("noZone -> " + _t); }
        }
        {
            Instant moment = Instant.parse("2026-10-10T08:15:30.250Z");
            Timestamp legacy = Timestamp.from(moment);                       // the same moment as a java.sql.Timestamp
            show("legacy", legacy);
            Instant back = legacy.toInstant();                               // 2026-10-10T08:15:30.250Z
            show("back", back);
            Timestamp current = Timestamp.from(Instant.now());               // current moment for an old JDBC API
            show("current", current);
            Timestamp local = Timestamp.valueOf(LocalDateTime.of(2026, 10, 10, 13, 45, 30));
            LocalDateTime localBack = local.toLocalDateTime();               // 2026-10-10T13:45:30
            show("localBack", localBack);
        }
        {
            InstantSource fixedSource = InstantSource.fixed(Instant.parse("2026-10-10T08:15:30Z"));
            AuditEntry approved = new AuditLog(fixedSource).record("APPROVE_CLAIM", "maria");
            Instant stamped = approved.at();                                         // 2026-10-10T08:15:30Z
            show("stamped", stamped);
            long stampedMillis = approved.at().toEpochMilli();                       // 1791620130000
            show("stampedMillis", stampedMillis);
            AuditLog production = new AuditLog(InstantSource.system());              // stamps the real current time
            show("production", production);
        }
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
            String current = formatter.format(Instant.now());          // current UTC timestamp as text
            show("current", current);
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
