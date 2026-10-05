package com.howtodoinjava.moduleimport;

// java.sql exports java.sql and javax.sql, and it requires java.logging
// and java.xml transitively, so their packages are imported too.
import module java.sql;

public class PriceDates {

  // java.sql.Date, from the java.sql module
  public static Date priceDate(String isoDate) {
    return Date.valueOf(isoDate);
  }

  // java.util.logging.Logger, from java.logging (a transitive dependency of java.sql)
  public static Logger logger() {
    return Logger.getLogger("prices");
  }

  public static void main(String[] args) {
    logger().info("Price date " + priceDate("2026-10-05"));
  }
}
