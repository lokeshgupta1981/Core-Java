package com.howtodoinjava.flexibleconstructors;

import java.util.List;

public final class ConstructionOrder {

  private ConstructionOrder() {
  }

  public static class Base {
    public Base(List<String> log) {
      log.add("Base prologue");
      super();
      log.add("Base epilogue");
    }
  }

  public static class Middle extends Base {
    public Middle(List<String> log) {
      log.add("Middle prologue");
      super(log);
      log.add("Middle epilogue");
    }
  }

  public static class Leaf extends Middle {
    public Leaf(List<String> log) {
      log.add("Leaf prologue");
      super(log);
      log.add("Leaf epilogue");
    }
  }
}
