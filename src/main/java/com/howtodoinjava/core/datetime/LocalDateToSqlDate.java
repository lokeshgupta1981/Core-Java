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
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * Examples for the tutorial "LocalDate to java.sql.Date in Java (and When to Skip It)".
 * https://howtodoinjava.com/java/date-time/localdate-to-sql-date/
 */
public class LocalDateToSqlDate {
    static void saveClass(Connection con, int id, LocalDate day, LocalTime start) throws SQLException {
        String sql = "INSERT INTO gym_class (id, class_day, starts_at) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setObject(2, day);          // DATE column
            ps.setObject(3, start);        // TIME column
            ps.executeUpdate();
        }
    }
    static Optional<LocalDate> findClassDay(Connection con, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT class_day FROM gym_class WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.ofNullable(rs.getObject("class_day", LocalDate.class)) : Optional.empty();
            }
        }
    }
    static java.sql.Date toSqlDate(LocalDate date) {
        return date == null ? null : java.sql.Date.valueOf(date);
    }
    static LocalDate classDayLegacy(ResultSet rs) throws SQLException {
        java.sql.Date value = rs.getDate("class_day");
        return value == null ? null : value.toLocalDate();       // the null check is easy to forget
    }
    static LocalDate classDay(ResultSet rs) throws SQLException {
        return rs.getObject("class_day", LocalDate.class);
    }
    public static void main(String[] args) throws Exception {
        {
            LocalDate classDay = LocalDate.of(2026, 3, 14);
            java.sql.Date sqlDate = java.sql.Date.valueOf(classDay);            // 2026-03-14
            show("sqlDate", sqlDate);
            LocalDate fromSql = sqlDate.toLocalDate();                          // 2026-03-14
            show("fromSql", fromSql);
            java.sql.Time sqlTime = java.sql.Time.valueOf(LocalTime.of(9, 30));  // 09:30:00
            show("sqlTime", sqlTime);
            LocalTime fromTime = sqlTime.toLocalTime();                         // 09:30
            show("fromTime", fromTime);
        }
        {
            java.sql.Date fromLocal = java.sql.Date.valueOf(LocalDate.of(2026, 3, 14));
            String text = fromLocal.toString();                              // "2026-03-14"
            show("text", text);
            java.sql.Date fromText = java.sql.Date.valueOf("2026-03-14");
            boolean same = fromText.equals(fromLocal);                       // true
            show("same", same);
            try { java.sql.Date broken = java.sql.Date.valueOf((LocalDate) null); show("broken", broken); } catch (Throwable _t) { System.out.println("broken -> " + _t); }
        }
        {
            java.sql.Date empty = toSqlDate(null);                           // null
            show("empty", empty);
            java.sql.Date filled = toSqlDate(LocalDate.of(2026, 3, 14));      // 2026-03-14
            show("filled", filled);
        }
        {
            java.sql.Date column = java.sql.Date.valueOf("2026-03-14");
            LocalDate day = column.toLocalDate();                       // 2026-03-14
            show("day", day);
            try { Instant moment = column.toInstant(); show("moment", moment); } catch (Throwable _t) { System.out.println("moment -> " + _t); }
        }
        {
            LocalTime opens = LocalTime.of(9, 30, 15, 250_000_000);
            java.sql.Time sqlOpens = java.sql.Time.valueOf(opens);          // 09:30:15
            show("sqlOpens", sqlOpens);
            LocalTime back = sqlOpens.toLocalTime();                         // 09:30:15
            show("back", back);
            boolean lossless = back.equals(opens);                           // false, 0.25 s is lost
            show("lossless", lossless);
            try { Instant noMoment = sqlOpens.toInstant(); show("noMoment", noMoment); } catch (Throwable _t) { System.out.println("noMoment -> " + _t); }
        }
        {
            LocalDateTime createdAt = LocalDateTime.of(2026, 3, 14, 9, 30, 15, 123456789);
            java.sql.Timestamp ts = java.sql.Timestamp.valueOf(createdAt);   // 2026-03-14 09:30:15.123456789
            show("ts", ts);
            LocalDateTime fromTs = ts.toLocalDateTime();                     // 2026-03-14T09:30:15.123456789
            show("fromTs", fromTs);
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
