import module java.base;
import module java.sql;

public class AmbiguousDate {
  public static void main(String[] args) {
    Date today = new Date();
    System.out.println(today);
  }
}
